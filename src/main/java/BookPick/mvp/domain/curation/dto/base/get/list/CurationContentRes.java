package BookPick.mvp.domain.curation.dto.base.get.list;

import BookPick.mvp.domain.author.entity.Author;
import BookPick.mvp.domain.curation.dto.prefer.ReadingPreferenceInfo;
import BookPick.mvp.domain.curation.entity.Curation;
import BookPick.mvp.domain.curation.enums.common.State;
import BookPick.mvp.domain.curation.util.gemini.dto.CurationMatchResult;
import BookPick.mvp.domain.user.entity.User;

import java.time.LocalDateTime;
import java.util.Set;

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
        LocalDateTime updatedAt

) {
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
//                BookResInCuration.from(curation.getTitle(), curation.getBookAuthor(), curation.getBookIsbn()),
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
                curation.getUpdatedAt()
        );
    }

    public static CurationContentRes from(CurationMatchResult matchResult, ReadingPreferenceInfo preferenceInfo,  boolean isLiked) {
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
                curation.getReview(), new BookResInCuration(curation.getBookTitle(), curation.getBookAuthor(), curation.getBookIsbn()),
                curation.getLikeCount(),

                curation.getCommentCount(),
                curation.getViewCount(),
                getSimilarity(matchResult, preferenceInfo),
                matchResult.getMatched(),
                curation.getPopularityScore(),
                isLiked,
                curation.getIsDrafted(),

                curation.getCreatedAt(),
                curation.getUpdatedAt()
        );
    }

    static Integer getSimilarity(CurationMatchResult matchResult, ReadingPreferenceInfo preferenceInfo) {
        Integer similarity = 50;
//        Random random = new Random();
        User user = matchResult.getUser();

        // 1. 매칭된 큐레이션의 작가중
        String author = matchResult.getCuration().getBookAuthor();


        //2. 유저 독서취향의 작가들 안에 존재하면 +20
        Set<Author> favoriteAuthors = preferenceInfo.favoriteAuthors();
        if(favoriteAuthors.contains(author)){
            similarity+=10;
        }

        // 3. 각 키워드들마다 매칭되는거 있으면 +10
        similarity += matchResult.getTotalMatchCount()*10;


        // 4. 1의 자리수 랜덤값으로 조정하여 다채롭게 (mvp단계 한정)
        // similarity+=random.nextInt(10);

        return similarity;
    }
}