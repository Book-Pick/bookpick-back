package BookPick.mvp.domain.curation.dto.base.get.list;

import BookPick.mvp.domain.author.entity.Author;
import BookPick.mvp.domain.curation.dto.prefer.ReadingPreferenceInfo;
import BookPick.mvp.domain.curation.entity.Curation;
import BookPick.mvp.domain.curation.util.gemini.dto.CurationMatchResult;
import BookPick.mvp.domain.user.entity.User;

import java.util.Set;

public record CurationContentRes(
        Long curationId,
        String title,
        Long userId,
        String nickName,
        String profileImageUrl,
        String introduction,
        ThumbnailRes thumbnail,
        String review,
        BookResInCuration book,
        int likeCount,
        int commentCount,
        int viewCount,
        Integer similarity,
        String matched,
        Integer popularityScore,
        boolean isDrafted,
        String createdAt
) {
    public static CurationContentRes from(Curation curation) {
        return new CurationContentRes(
                curation.getId(),
                curation.getTitle(),
                curation.getUser().getId(),
                curation.getUser().getNickname(),
                curation.getUser().getProfileImageUrl(),
                curation.getUser().getBio(),
                new ThumbnailRes(curation.getThumbnailUrl(), curation.getThumbnailColor()),
                curation.getReview(),
                BookResInCuration.from(curation.getTitle(), curation.getBookAuthor(), curation.getBookIsbn()),
                curation.getLikeCount(),
                curation.getCommentCount(),
                curation.getViewCount(),
                null,
                null,
                curation.getPopularityScore(),
                curation.isDrafted(),
                curation.getCreatedAt().toString()
        );
    }

    public static CurationContentRes from(CurationMatchResult matchResult, ReadingPreferenceInfo preferenceInfo) {
        Curation curation = matchResult.getCuration();
        return new CurationContentRes(
                curation.getId(),
                curation.getTitle(),
                curation.getUser().getId(),
                matchResult.getUser().getNickname(),
                matchResult.getUser().getProfileImageUrl(),
                matchResult.getUser().getBio(),
                new ThumbnailRes(curation.getThumbnailUrl(), curation.getThumbnailColor()),
                curation.getReview(), new BookResInCuration(curation.getBookTitle(), curation.getBookAuthor(), curation.getBookIsbn()),
                curation.getLikeCount(),
                curation.getCommentCount(),
                curation.getViewCount(),
                getSimilarity(matchResult, preferenceInfo),
                matchResult.getMatched(),
                curation.getPopularityScore(),
                curation.isDrafted(),
                curation.getCreatedAt().toString()
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