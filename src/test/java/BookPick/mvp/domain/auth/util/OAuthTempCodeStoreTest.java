package BookPick.mvp.domain.auth.util;

import static org.assertj.core.api.Assertions.assertThat;

import BookPick.mvp.domain.auth.dto.LoginRes;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("OAuthTempCodeStore 테스트")
class OAuthTempCodeStoreTest {

    private OAuthTempCodeStore tempCodeStore;

    @BeforeEach
    void setUp() {
        tempCodeStore = new OAuthTempCodeStore();
    }

    @Test
    @DisplayName("임시 코드 저장 및 조회 성공")
    void store_and_consume_success() {
        // given
        LoginRes loginRes = createLoginRes();

        // when
        String code = tempCodeStore.store(loginRes);
        Optional<LoginRes> result = tempCodeStore.consume(code);

        // then
        assertThat(code).isNotNull();
        assertThat(result).isPresent();
        assertThat(result.get().userId()).isEqualTo(1L);
        assertThat(result.get().email()).isEqualTo("test@kakao.com");
        assertThat(result.get().accessToken()).isEqualTo("access_token");
    }

    @Test
    @DisplayName("임시 코드는 일회용 - 두 번째 조회 시 빈 값 반환")
    void consume_twice_returns_empty() {
        // given
        LoginRes loginRes = createLoginRes();
        String code = tempCodeStore.store(loginRes);

        // when
        Optional<LoginRes> firstResult = tempCodeStore.consume(code);
        Optional<LoginRes> secondResult = tempCodeStore.consume(code);

        // then
        assertThat(firstResult).isPresent();
        assertThat(secondResult).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 코드 조회 시 빈 값 반환")
    void consume_nonexistent_code_returns_empty() {
        // when
        Optional<LoginRes> result = tempCodeStore.consume("nonexistent-code");

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("여러 코드 독립적으로 저장 및 조회")
    void multiple_codes_independent() {
        // given
        LoginRes loginRes1 = new LoginRes(1L, "user1@test.com", "User1", null, null, true, "token1", "refresh1");
        LoginRes loginRes2 = new LoginRes(2L, "user2@test.com", "User2", null, null, false, "token2", "refresh2");

        // when
        String code1 = tempCodeStore.store(loginRes1);
        String code2 = tempCodeStore.store(loginRes2);

        Optional<LoginRes> result1 = tempCodeStore.consume(code1);
        Optional<LoginRes> result2 = tempCodeStore.consume(code2);

        // then
        assertThat(code1).isNotEqualTo(code2);
        assertThat(result1).isPresent();
        assertThat(result2).isPresent();
        assertThat(result1.get().userId()).isEqualTo(1L);
        assertThat(result2.get().userId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("UUID 형식의 코드 생성")
    void generates_uuid_format_code() {
        // given
        LoginRes loginRes = createLoginRes();

        // when
        String code = tempCodeStore.store(loginRes);

        // then
        assertThat(code).matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
    }

    private LoginRes createLoginRes() {
        return new LoginRes(
                1L,
                "test@kakao.com",
                "테스터",
                "자기소개",
                "profile.jpg",
                true,
                "access_token",
                "refresh_token");
    }
}
