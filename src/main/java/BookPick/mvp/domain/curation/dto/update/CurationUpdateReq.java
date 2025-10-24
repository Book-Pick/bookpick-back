// CurationUpdateReq.java
package BookPick.mvp.domain.curation.dto.update;

import BookPick.mvp.domain.curation.dto.create.Req.BookDto;
import BookPick.mvp.domain.curation.dto.create.Req.RecommendDto;
import BookPick.mvp.domain.curation.dto.create.Req.ThumbnailDto;

public record CurationUpdateReq(
        ThumbnailDto thumbnail,
        BookDto book,
        String review,
        RecommendDto recommend
) {}