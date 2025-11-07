package BookPick.mvp.domain.curation.dto.base.create;

import BookPick.mvp.domain.curation.dto.base.create.Req.BookDto;
import BookPick.mvp.domain.curation.dto.base.create.Req.RecommendDto;
import BookPick.mvp.domain.curation.dto.base.create.Req.ThumbnailDto;

// 메인 요청 DTO
public record CurationCreateReq(
    ThumbnailDto thumbnail,
    BookDto book,
    String review,
    RecommendDto recommend
) {}




