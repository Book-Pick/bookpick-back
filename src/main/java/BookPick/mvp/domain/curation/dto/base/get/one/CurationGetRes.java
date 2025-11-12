// CurationGetRes.java
package BookPick.mvp.domain.curation.dto.base.get.one;

import BookPick.mvp.domain.curation.entity.Curation;
import java.time.LocalDateTime;
import java.util.List;

public record CurationGetRes(
        Long id,
        Long userId,
        ThumbnailInfo thumbnail,
        BookInfo book,
        String review,
        RecommendInfo recommend,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CurationGetRes from(Curation curation) {
        return new CurationGetRes(
                curation.getId(),
                curation.getUser().getId(),
                new ThumbnailInfo(curation.getThumbnailUrl(), curation.getThumbnailColor()),
                new BookInfo(curation.getBookTitle(), curation.getBookAuthor(), curation.getBookIsbn()),
                curation.getReview(),
                new RecommendInfo(curation.getMoods(), curation.getGenres(),
                        curation.getKeywords(), curation.getStyles()),
                curation.getCreatedAt(),
                curation.getUpdatedAt()
        );
    }

    public record ThumbnailInfo(String imageUrl, String imageColor) {}
    public record BookInfo(String title, String author, String isbn) {}
    public record RecommendInfo(List<String> moods, List<String> genres,
                                List<String> keywords, List<String> styles) {}
}