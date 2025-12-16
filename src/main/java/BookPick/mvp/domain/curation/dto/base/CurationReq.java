package BookPick.mvp.domain.curation.dto.base;

import BookPick.mvp.domain.curation.dto.base.create.ETC.BookDto;
import BookPick.mvp.domain.curation.dto.base.create.ETC.RecommendDto;
import BookPick.mvp.domain.curation.dto.base.create.ETC.ThumbnailDto;
import BookPick.mvp.domain.curation.enums.common.State;
import jakarta.validation.constraints.NotNull;

// 메인 요청 DTO
public record CurationReq(
        String title,
        ThumbnailDto thumbnail,
        BookDto book,
        String review,
        RecommendDto recommend,

        @NotNull
        Boolean isDrafted
) {
}




