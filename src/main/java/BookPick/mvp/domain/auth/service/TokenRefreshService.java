package BookPick.mvp.domain.auth.service;

import BookPick.mvp.domain.auth.dto.LoginRes;
import BookPick.mvp.domain.auth.util.Manager.login.jwt.JwtAuthManager;
import BookPick.mvp.domain.auth.util.Manager.login.jwt.TokenBlacklistManager;
import BookPick.mvp.global.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenRefreshService {

    private final JwtAuthManager jwtAuthManager;
    private final TokenBlacklistManager tokenBlacklistManager;
    private final JwtUtil jwtUtil;

    public LoginRes refreshTokens(CustomUserDetails customUserDetails, String refreshToken) {

        if (refreshToken == null || refreshToken.isBlank()) {
            throw new RuntimeException("리프레시 토큰이 없습니다.");
        }

        // 🔒 블랙리스트 확인
        if (tokenBlacklistManager.isBlacklisted(refreshToken)) {
            throw new RuntimeException("유효하지 않은 리프레시 토큰입니다.");
        }

        // ✅ 토큰 유효성 검증
        if (!jwtUtil.validateToken(refreshToken, false)) {
            throw new RuntimeException("리프레시 토큰이 만료되었거나 유효하지 않습니다.");
        }

        // Claims 추출
        Claims claims = jwtUtil.extractRefreshToken(refreshToken);
        Double userIdDouble = claims.get("userId", Double.class);
        Long userId = userIdDouble.longValue();  // Double → Long

        if (!customUserDetails.getId().equals(userId)) {
            throw new RuntimeException("토큰 사용자 정보와 일치하지 않습니다.");
        }

        // Authentication 객체 생성
        Authentication auth = new UsernamePasswordAuthenticationToken(
                customUserDetails,
                null,
                customUserDetails.getAuthorities()
        );

        // 새 토큰 발급
        JwtAuthManager.TokenPair tokenPair = jwtAuthManager.createTokens(auth);

        return LoginRes.from(customUserDetails, tokenPair.accessToken(), tokenPair.refreshToken());
    }
}
