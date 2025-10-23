package BookPick.mvp.domain.curation.dto.create.Req;

// 썸네일 정보
public record ThumbnailDto(
    String imageUrl,
    String imageColor
) {}