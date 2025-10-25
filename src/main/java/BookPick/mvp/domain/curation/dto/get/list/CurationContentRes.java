package BookPick.mvp.domain.curation.dto.get.list;

import BookPick.mvp.domain.curation.entity.Curation;

public record CurationContentRes(
        Long curationId,
        String title,
        Long userId,
        String nickName,
        ThumbnailRes thumbnail,
        String summary,
        BookRes book,
        int likeCount,
        int commentCount,
        int viewCount,
        Double similarity,
        String matched,
        Integer popularityScore,
        String createdAt
) {
    public static CurationContentRes from(Curation curation) {
        return new CurationContentRes(
                curation.getId(),
                curation.getBookTitle(),
                curation.getUser().getId(),
                "닉네임", // TODO: User 조인 필요
                new ThumbnailRes(curation.getThumbnailUrl(), curation.getThumbnailColor()),
                curation.getReview(),
                new BookRes(curation.getBookTitle(), curation.getBookAuthor()),
                0, // TODO: 좋아요 수
                0, // TODO: 댓글 수
                0, // TODO: 조회수
                null,
                null,
                curation.getPopularityScore(), // TODO: popularityScore 계산
                curation.getCreatedAt().toString()
        );
    }
}