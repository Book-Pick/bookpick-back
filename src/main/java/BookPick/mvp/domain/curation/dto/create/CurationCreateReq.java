package BookPick.mvp.domain.curation.dto.create;

import BookPick.mvp.domain.curation.dto.create.Req.BookDto;
import BookPick.mvp.domain.curation.dto.create.Req.RecommendDto;
import BookPick.mvp.domain.curation.dto.create.Req.ThumbnailDto;

import java.util.List;

// 메인 요청 DTO
public record CurationCreateReq(
    ThumbnailDto thumbnail,
    BookDto book,
    String review,
    RecommendDto recommend
) {}




