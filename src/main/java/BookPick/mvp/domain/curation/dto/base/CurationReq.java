package BookPick.mvp.domain.curation.dto.base;

import BookPick.mvp.domain.curation.dto.base.create.ETC.BookDto;
import BookPick.mvp.domain.curation.dto.base.create.ETC.RecommendDto;
import BookPick.mvp.domain.curation.dto.base.create.ETC.ThumbnailDto;

// 메인 요청 DTO
public record CurationReq(
        String title,
        ThumbnailDto thumbnail,
        BookDto book,
        String review,
        RecommendDto recommend
) {
}




