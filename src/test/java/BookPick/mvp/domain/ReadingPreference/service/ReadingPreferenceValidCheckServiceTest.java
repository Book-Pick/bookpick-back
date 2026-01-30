package BookPick.mvp.domain.ReadingPreference.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import BookPick.mvp.domain.ReadingPreference.Exception.fail.WrongReadingPreferenceRequestException;
import BookPick.mvp.domain.ReadingPreference.dto.ReadingPreferenceReq;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("독서취향 검증 서비스 테스트")
class ReadingPreferenceValidCheckServiceTest {

    @InjectMocks private ReadingPreferenceValidCheckService validCheckService;

    @Test
    @DisplayName("정상 독서취향 요청 검증 성공")
    void validateReadingPreferenceReq_success() {
        // given
        ReadingPreferenceReq req =
                new ReadingPreferenceReq(
                        "INTJ",
                        Set.of(),
                        Set.of(),
                        List.of("차분한"),
                        List.of("매일 읽기"),
                        List.of("소설"),
                        List.of("성장"),
                        List.of("몰입형"));

        // when & then
        assertThatCode(() -> validCheckService.validateReadingPreferenceReq(req))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("MBTI null 또는 빈 문자열 - 검증 통과")
    void validateReadingPreferenceReq_nullOrEmptyMbti_success() {
        // given
        ReadingPreferenceReq req1 =
                new ReadingPreferenceReq(
                        null,
                        Set.of(),
                        Set.of(),
                        List.of("차분한"),
                        List.of("매일 읽기"),
                        List.of("소설"),
                        List.of("성장"),
                        List.of("몰입형"));

        ReadingPreferenceReq req2 =
                new ReadingPreferenceReq(
                        "",
                        Set.of(),
                        Set.of(),
                        List.of("차분한"),
                        List.of("매일 읽기"),
                        List.of("소설"),
                        List.of("성장"),
                        List.of("몰입형"));

        // when & then
        assertThatCode(() -> validCheckService.validateReadingPreferenceReq(req1))
                .doesNotThrowAnyException();
        assertThatCode(() -> validCheckService.validateReadingPreferenceReq(req2))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("잘못된 MBTI - 검증 실패")
    void validateReadingPreferenceReq_invalidMbti_fail() {
        // given
        ReadingPreferenceReq req =
                new ReadingPreferenceReq(
                        "INVALID",
                        Set.of(),
                        Set.of(),
                        List.of("차분한"),
                        List.of("매일 읽기"),
                        List.of("소설"),
                        List.of("성장"),
                        List.of("몰입형"));

        // when & then
        assertThatThrownBy(() -> validCheckService.validateReadingPreferenceReq(req))
                .isInstanceOf(WrongReadingPreferenceRequestException.class);
    }

    @Test
    @DisplayName("잘못된 Mood - 검증 실패")
    void validateReadingPreferenceReq_invalidMood_fail() {
        // given
        ReadingPreferenceReq req =
                new ReadingPreferenceReq(
                        "INTJ",
                        Set.of(),
                        Set.of(),
                        List.of("잘못된_무드"),
                        List.of("매일 읽기"),
                        List.of("소설"),
                        List.of("성장"),
                        List.of("몰입형"));

        // when & then
        assertThatThrownBy(() -> validCheckService.validateReadingPreferenceReq(req))
                .isInstanceOf(WrongReadingPreferenceRequestException.class);
    }

    @Test
    @DisplayName("잘못된 ReadingHabit - 검증 실패")
    void validateReadingPreferenceReq_invalidReadingHabit_fail() {
        // given
        ReadingPreferenceReq req =
                new ReadingPreferenceReq(
                        "INTJ",
                        Set.of(),
                        Set.of(),
                        List.of("차분한"),
                        List.of("잘못된_습관"),
                        List.of("소설"),
                        List.of("성장"),
                        List.of("몰입형"));

        // when & then
        assertThatThrownBy(() -> validCheckService.validateReadingPreferenceReq(req))
                .isInstanceOf(WrongReadingPreferenceRequestException.class);
    }

    @Test
    @DisplayName("잘못된 Genre - 검증 실패")
    void validateReadingPreferenceReq_invalidGenre_fail() {
        // given
        ReadingPreferenceReq req =
                new ReadingPreferenceReq(
                        "INTJ",
                        Set.of(),
                        Set.of(),
                        List.of("차분한"),
                        List.of("매일 읽기"),
                        List.of("잘못된_장르"),
                        List.of("성장"),
                        List.of("몰입형"));

        // when & then
        assertThatThrownBy(() -> validCheckService.validateReadingPreferenceReq(req))
                .isInstanceOf(WrongReadingPreferenceRequestException.class);
    }

    @Test
    @DisplayName("잘못된 Keyword - 검증 실패")
    void validateReadingPreferenceReq_invalidKeyword_fail() {
        // given
        ReadingPreferenceReq req =
                new ReadingPreferenceReq(
                        "INTJ",
                        Set.of(),
                        Set.of(),
                        List.of("차분한"),
                        List.of("매일 읽기"),
                        List.of("소설"),
                        List.of("잘못된_키워드"),
                        List.of("몰입형"));

        // when & then
        assertThatThrownBy(() -> validCheckService.validateReadingPreferenceReq(req))
                .isInstanceOf(WrongReadingPreferenceRequestException.class);
    }

    @Test
    @DisplayName("잘못된 ReadingStyle - 검증 실패")
    void validateReadingPreferenceReq_invalidReadingStyle_fail() {
        // given
        ReadingPreferenceReq req =
                new ReadingPreferenceReq(
                        "INTJ",
                        Set.of(),
                        Set.of(),
                        List.of("차분한"),
                        List.of("매일 읽기"),
                        List.of("소설"),
                        List.of("성장"),
                        List.of("잘못된_스타일"));

        // when & then
        assertThatThrownBy(() -> validCheckService.validateReadingPreferenceReq(req))
                .isInstanceOf(WrongReadingPreferenceRequestException.class);
    }

    @Test
    @DisplayName("null 컬렉션 - 검증 통과")
    void validateReadingPreferenceReq_nullCollections_success() {
        // given
        ReadingPreferenceReq req =
                new ReadingPreferenceReq("INTJ", Set.of(), Set.of(), null, null, null, null, null);

        // when & then
        assertThatCode(() -> validCheckService.validateReadingPreferenceReq(req))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("빈 컬렉션 - 검증 통과")
    void validateReadingPreferenceReq_emptyCollections_success() {
        // given
        ReadingPreferenceReq req =
                new ReadingPreferenceReq(
                        "INTJ", Set.of(), Set.of(), List.of(), List.of(), List.of(), List.of(),
                        List.of());

        // when & then
        assertThatCode(() -> validCheckService.validateReadingPreferenceReq(req))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("여러 올바른 값 - 검증 통과")
    void validateReadingPreferenceReq_multipleValidValues_success() {
        // given
        ReadingPreferenceReq req =
                new ReadingPreferenceReq(
                        "ENFP",
                        Set.of(),
                        Set.of(),
                        List.of("차분한", "설레는"),
                        List.of("매일 읽기", "주말에 읽기"),
                        List.of("소설", "에세이"),
                        List.of("성장", "힐링"),
                        List.of("몰입형", "건너뛰며"));

        // when & then
        assertThatCode(() -> validCheckService.validateReadingPreferenceReq(req))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("혼합된 올바른 값과 잘못된 값 - 검증 실패")
    void validateReadingPreferenceReq_mixedValidAndInvalid_fail() {
        // given
        ReadingPreferenceReq req =
                new ReadingPreferenceReq(
                        "INTJ",
                        Set.of(),
                        Set.of(),
                        List.of("차분한", "잘못된_무드"),
                        List.of("매일 읽기"),
                        List.of("소설"),
                        List.of("성장"),
                        List.of("몰입형"));

        // when & then
        assertThatThrownBy(() -> validCheckService.validateReadingPreferenceReq(req))
                .isInstanceOf(WrongReadingPreferenceRequestException.class);
    }
}
