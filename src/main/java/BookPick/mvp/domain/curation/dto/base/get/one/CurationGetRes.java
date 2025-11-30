// CurationGetRes.java
package BookPick.mvp.domain.curation.dto.base.get.one;

import BookPick.mvp.domain.curation.entity.Curation;

import java.time.LocalDateTime;
import java.util.List;

public record CurationGetRes(
        Long id,
        Long userId,
        String nickName,
        String profileImageUrl,
        String introduction,
        boolean subscribed,
        String title,
        ThumbnailInfo thumbnail,
        BookInfo book,
        String review,
        RecommendInfo recommend,
        Boolean isLiked,
        Integer likeCount,
        Integer viewCount,
        Integer CommentCount,

        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CurationGetRes from(Curation curation, boolean subscribed, boolean isLiked) {
        return new CurationGetRes(
                curation.getId(),
                curation.getUser().getId(),
                curation.getUser().getNickname(),
                curation.getUser().getProfileImageUrl(),
                curation.getUser().getBio(),
                subscribed,
                curation.getTitle(),
                new ThumbnailInfo(curation.getThumbnailUrl(), curation.getThumbnailColor()),
                new BookInfo(curation.getBookTitle(), curation.getBookAuthor(), curation.getBookIsbn()),
                curation.getReview(),
                new RecommendInfo(curation.getMoods(), curation.getGenres(),
                        curation.getKeywords(), curation.getStyles()),
                isLiked,
                curation.getLikeCount(),
                curation.getViewCount(),
                curation.getCommentCount(),
                curation.getCreatedAt(),
                curation.getUpdatedAt()
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