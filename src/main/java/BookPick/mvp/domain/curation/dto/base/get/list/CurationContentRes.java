package BookPick.mvp.domain.curation.dto.base.get.list;

import BookPick.mvp.domain.curation.dto.prefer.ReadingPreferenceInfo;
import BookPick.mvp.domain.curation.model.Curation;
import BookPick.mvp.domain.curation.util.gemini.dto.CurationMatchResult;
import BookPick.mvp.domain.user.entity.User;

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
        Integer similarity,
        String matched,
        Integer popularityScore,
        String createdAt
) {
    public static CurationContentRes from(Curation curation) {
        return new CurationContentRes(
                curation.getId(),
                curation.getBookTitle(),
                curation.getUser().getId(),
                curation.getUser().getNickname(),
                new ThumbnailRes(curation.getThumbnailUrl(), curation.getThumbnailColor()),
                curation.getReview(), new BookRes(curation.getBookTitle(), curation.getBookAuthor()),
                curation.getLikeCount(),
                curation.getCommentCount(),
                curation.getViewCount(),
                null,
                null,
                curation.getPopularityScore(),
                curation.getCreatedAt().toString()
        );
    }

    public static CurationContentRes from(CurationMatchResult matchResult, ReadingPreferenceInfo preferenceInfo) {
        Curation curation = matchResult.getCuration();
        return new CurationContentRes(
                curation.getId(),
                curation.getBookTitle(),
                curation.getUser().getId(),
                matchResult.getUser().getNickname(),
                new ThumbnailRes(curation.getThumbnailUrl(), curation.getThumbnailColor()),
                curation.getReview(), new BookRes(curation.getBookTitle(), curation.getBookAuthor()),
                curation.getLikeCount(),
                curation.getCommentCount(),
                curation.getViewCount(),
                getSimilarity(matchResult, preferenceInfo),
                matchResult.getMatched(),
                curation.getPopularityScore(),
                curation.getCreatedAt().toString()
        );
    }

    // 1. 유사도 계산법                                 100%
    // 1) 작가                                        19%
    // 2) Matched -> 이거 , 로 분리해서 개수 Count        하나당 20%

     static Integer getSimilarity(CurationMatchResult matchResult, ReadingPreferenceInfo preferenceInfo){
        Integer similarity = 0;
        User user = matchResult.getUser();
        String userPrferAuthor = preferenceInfo.
        String userPrferAuthor = matchResult.getCuration().getBookAuthor();
        if(matchResult.getCuration().getBookAuthor().equals(user)
        return null;
    }
}