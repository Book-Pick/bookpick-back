// CurationUpdateReq.java
package BookPick.mvp.domain.curation.dto.base.update;

import BookPick.mvp.domain.curation.dto.base.create.ETC.BookDto;
import BookPick.mvp.domain.curation.dto.base.create.ETC.RecommendDto;
import BookPick.mvp.domain.curation.dto.base.create.ETC.ThumbnailDto;

public record CurationUpdateReq(
        String title,
        ThumbnailDto thumbnail,
        BookDto book,
        String review,
        RecommendDto recommend
) {}