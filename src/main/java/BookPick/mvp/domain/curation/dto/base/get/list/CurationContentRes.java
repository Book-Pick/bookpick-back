package BookPick.mvp.domain.curation.dto.base.get.list;

import BookPick.mvp.domain.curation.dto.prefer.ReadingPreferenceInfo;
import BookPick.mvp.domain.curation.entity.Curation;
import BookPick.mvp.domain.curation.util.gemini.dto.CurationMatchResult;
import BookPick.mvp.domain.curation.util.list.similarity.SimilarityMatcher;
import java.time.LocalDateTime;

public record CurationContentRes(

        // 1. 헤더
        Long curationId,
        String title,

        // 2. 작성자 정보
        Long userId,
        String nickName,
        String profileImageUrl,
        String introduction,

        // 3. 작성 내용
        ThumbnailRes thumbnail,
        String review,
        BookResInCuration book,

        // 4. 부수 정보
        int likeCount,
        int commentCount,
        int viewCount,
        int similarity,
        String matched,
        int popularityScore,
        boolean isLiked,
        Boolean isDrafted,

        // 5. 시간
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {

    public static CurationContentRes from(Curation curation, boolean isLiked) {
        return new CurationContentRes(
                curation.getId(),
                curation.getTitle(),
                curation.getUser().getId(),
                curation.getUser().getNickname(),
                curation.getUser().getProfileImageUrl(),
                curation.getUser().getBio(),
                new ThumbnailRes(curation.getThumbnailUrl(), curation.getThumbnailColor()),
                curation.getReview(),
                //                BookResInCuration.from(curation.getTitle(),
                // curation.getBookAuthor(), curation.getBookIsbn()),
                null,
                curation.getLikeCount(),
                curation.getCommentCount(),
                curation.getViewCount(),
                0,
                null,
                curation.getPopularityScore(),
                isLiked,
                curation.getIsDrafted(),
                curation.getCreatedAt(),
                curation.getUpdatedAt());
    }

    public static CurationContentRes from(
            CurationMatchResult matchResult,
            ReadingPreferenceInfo preferenceInfo,
            boolean isLiked) {
        Curation curation = matchResult.getCuration();
        return new CurationContentRes(
                curation.getId(),
                curation.getTitle(),
                curation.getUser().getId(),
                matchResult.getUser().getNickname(),
                matchResult.getUser().getProfileImageUrl(),
                matchResult.getUser().getBio(),

                // Todo 1. 팩토리 메서드 구현 필요
                new ThumbnailRes(curation.getThumbnailUrl(), curation.getThumbnailColor()),
                curation.getReview(),
                new BookResInCuration(
                        curation.getBookTitle(), curation.getBookAuthor(), curation.getBookIsbn()),
                curation.getLikeCount(),
                curation.getCommentCount(),
                curation.getViewCount(),
                getSimilarity(matchResult, preferenceInfo),
                matchResult.getMatched(),
                curation.getPopularityScore(),
                isLiked,
                curation.getIsDrafted(),
                curation.getCreatedAt(),
                curation.getUpdatedAt());
    }

    /**
     * 유사도 계산 (SimilarityMatcher 사용)
     *
     * 점수 구성:
     * - 기본: 30점
     * - 장르 매칭: 최대 25점 (1개: 15점, 2개+: 25점)
     * - 키워드 매칭: 최대 20점 (1개: 12점, 2개+: 20점)
     * - 분위기 매칭: 최대 15점 (1개: 9점, 2개+: 15점)
     * - 스타일 매칭: 최대 10점 (1개: 6점, 2개+: 10점)
     * - 작가 매칭: 10점
     */
    static Integer getSimilarity(
            CurationMatchResult matchResult, ReadingPreferenceInfo preferenceInfo) {
        return SimilarityMatcher.calculate(matchResult.getCuration(), preferenceInfo);
    }

    /**
     * 순수 매칭용 팩토리 메서드 (점수와 매칭 정보 직접 전달)
     */
    public static CurationContentRes fromWithScore(
            Curation curation,
            int score,
            String matched,
            boolean isLiked) {
        return new CurationContentRes(
                curation.getId(),
                curation.getTitle(),
                curation.getUser().getId(),
                curation.getUser().getNickname(),
                curation.getUser().getProfileImageUrl(),
                curation.getUser().getBio(),
                new ThumbnailRes(curation.getThumbnailUrl(), curation.getThumbnailColor()),
                curation.getReview(),
                new BookResInCuration(
                        curation.getBookTitle(), curation.getBookAuthor(), curation.getBookIsbn()),
                curation.getLikeCount(),
                curation.getCommentCount(),
                curation.getViewCount(),
                score,
                matched,
                curation.getPopularityScore(),
                isLiked,
                curation.getIsDrafted(),
                curation.getCreatedAt(),
                curation.getUpdatedAt());
    }
}
