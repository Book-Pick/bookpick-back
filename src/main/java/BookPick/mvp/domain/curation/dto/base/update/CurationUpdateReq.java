// CurationUpdateReq.java
package BookPick.mvp.domain.curation.dto.base.update;

import BookPick.mvp.domain.curation.dto.base.create.Req.BookDto;
import BookPick.mvp.domain.curation.dto.base.create.Req.RecommendDto;
import BookPick.mvp.domain.curation.dto.base.create.Req.ThumbnailDto;

public record CurationUpdateReq(
        String title,
        ThumbnailDto thumbnail,
        BookDto book,
        String review,
        RecommendDto recommend
) {}