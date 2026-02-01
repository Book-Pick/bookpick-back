package BookPick.mvp.domain.comment.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("페이지네이션 서비스 테스트")
class PagenationServiceTest {

    @InjectMocks private PagenationService pagenationService;

    @Test
    @DisplayName("음수 페이지 번호를 0으로 변환")
    void changeMinusPageToZeroPage_minusPage() {
        // given
        int minusPage = -1;

        // when
        int result = pagenationService.changeMinusPageToZeroPage(minusPage);

        // then
        assertThat(result).isZero();
    }

    @Test
    @DisplayName("0 또는 양수 페이지 번호는 그대로 유지")
    void changeMinusPageToZeroPage_notMinusPage() {
        // given
        int zeroPage = 0;
        int positivePage = 10;

        // when
        int zeroResult = pagenationService.changeMinusPageToZeroPage(zeroPage);
        int positiveResult = pagenationService.changeMinusPageToZeroPage(positivePage);

        // then
        assertThat(zeroResult).isEqualTo(zeroPage);
        assertThat(positiveResult).isEqualTo(positivePage);
    }
}
