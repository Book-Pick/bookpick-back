// CurationGetRes.java
package BookPick.mvp.domain.curation.dto.base;

import BookPick.mvp.domain.curation.entity.Curation;

import java.time.LocalDateTime;
import java.util.List;

public record CurationRes(
        Long id,
        Long userId,
        String title,
        ThumbnailInfo thumbnail,
        BookInfo book,
        String review,
        RecommendInfo recommend,
        Boolean isDeleted,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt
) {
    public static CurationRes from(Curation curation) {
        return new CurationRes(
                curation.getId(),
                curation.getUser().getId(),
                curation.getTitle(),
                new ThumbnailInfo(curation.getThumbnailUrl(), curation.getThumbnailColor()),
                new BookInfo(curation.getBookTitle(), curation.getBookAuthor(), curation.getBookIsbn()),
                curation.getReview(),
                new RecommendInfo(curation.getMoods(), curation.getGenres(), curation.getKeywords(), curation.getStyles()),
                curation.isDrafted(),
                curation.getCreatedAt(),
                curation.getUpdatedAt(),
                curation.getDeletedAt()
        );
    }

    public record ThumbnailInfo(String imageUrl, String imageColor) {
    }

    public record BookInfo(String title, String author, String isbn) {
    }

    public record RecommendInfo(List<String> moods, List<String> genres,
                                List<String> keywords, List<String> styles) {
    }
}