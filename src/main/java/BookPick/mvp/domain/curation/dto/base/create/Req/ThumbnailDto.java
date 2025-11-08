package BookPick.mvp.domain.curation.dto.base.create.Req;

// 썸네일 정보
public record ThumbnailDto(
    String imageUrl,
    String imageColor
) {}