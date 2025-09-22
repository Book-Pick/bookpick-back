package BookPick.mvp.global.config;

import BookPick.mvp.global.ApiResponse;
import BookPick.mvp.global.util.JwtUtil; // JWT 파싱/검증 유틸
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims; // JWT Payload(클레임) 타입
import jakarta.servlet.FilterChain; // 필터 체인 (다음 필터 호출용)
import jakarta.servlet.ServletException; // 서블릿 예외
import jakarta.servlet.http.HttpServletRequest; // HTTP 요청
import jakarta.servlet.http.HttpServletResponse; // HTTP 응답
import lombok.RequiredArgsConstructor; // 생성자 자동 생성
import org.springframework.security.authentication.AuthenticationServiceException; // 인증 관련 예외
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // 인증 객체 구현체
import org.springframework.security.core.authority.SimpleGrantedAuthority; // 권한 객체
import org.springframework.security.core.context.SecurityContextHolder; // 인증 정보를 담는 컨텍스트
import org.springframework.stereotype.Component; // 스프링 빈 등록
import org.springframework.web.filter.OncePerRequestFilter; // 요청마다 한 번 실행되는 필터

import java.io.IOException; // IO 예외
import java.util.List; // 권한 리스트

/**
 * JWT 필터 (MVP 버전, 리프레시 없음)
 * - Authorization 헤더에서 토큰 추출
 * - JwtUtil.parse(token)으로 만료/서명 검증
 * - 성공하면 SecurityContext에 인증 정보 세팅
 * - 실패하면 401 Unauthorized 반환
 */
@Component // 스프링 컴포넌트 스캔에 의해 빈 등록
@RequiredArgsConstructor // final 필드 생성자 자동 주입
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil; // JwtUtil 주입받음

    private static final String AUTH_HEADER = "Authorization"; // 토큰이 담기는 헤더 이름
    private static final String BEARER = "Bearer ";            // 토큰 앞에 붙는 접두어

    @Override
    protected void doFilterInternal(HttpServletRequest request,   // 들어온 요청
                                    HttpServletResponse response, // 응답
                                    FilterChain chain)            // 필터 체인 : 필터들을 관리하는 객체 , chain.dofilter -> 다음필터 실행
            throws ServletException, IOException {

        // 1. 요청 헤더에서 Authorization 값 꺼냄
        String header = request.getHeader(AUTH_HEADER);

        // 2. 헤더가 없거나 Bearer로 시작하지 않으면 → 그냥 다음 필터로 넘김
        if (header == null || !header.startsWith(BEARER)) {
            chain.doFilter(request, response);      // 다음 필터로 넘기는 코드
            return;
        }

        // 3. "Bearer " 부분 제거하고 실제 토큰만 추출
        String token = header.substring(BEARER.length()).trim();

        try {
            // 4. JwtUtil로 토큰 검증 (만료·서명 체크 포함)
            Claims claims = jwtUtil.parse(token);

            // 5. 클레임에서 사용자 정보 꺼내기
            String email = claims.get("email", String.class);
            String role  = claims.get("role", String.class);

            // 6. 권한 객체 생성 (ROLE_ 접두어 보정)
            SimpleGrantedAuthority authority =
                    (role != null && role.startsWith("ROLE_"))
                            ? new SimpleGrantedAuthority(role)
                            : new SimpleGrantedAuthority("ROLE_" + role);

            // 7. 인증 객체 생성 (principal: email, credentials: null, authorities: 권한)
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(email, null, List.of(authority));

            // 8. SecurityContext에 인증 객체 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 9. 다음 필터로 진행
            chain.doFilter(request, response);

        } catch (AuthenticationServiceException ex) {
            // 10. 토큰이 만료되었거나 유효하지 않음
            SecurityContextHolder.clearContext(); // 기존 인증 정보 제거
            ApiResponse<Void> body = new ApiResponse<>(401, ex.getMessage(), null);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 반환
            response.setContentType("application/json;charset=UTF-8"); // JSON 응답
            new ObjectMapper().writeValue(response.getWriter(), body);
            return; // 컨트롤러로 요청 안 넘어감
        }
    }
}
