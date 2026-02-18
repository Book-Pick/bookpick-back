package BookPick.mvp.domain.curation.dto.base;

import BookPick.mvp.domain.curation.dto.base.create.ETC.BookDto;
import BookPick.mvp.domain.curation.dto.base.create.ETC.RecommendDto;
import BookPick.mvp.domain.curation.dto.base.create.ETC.ThumbnailDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// 메인 요청 DTO
public record CurationReq(
        @NotBlank(message = "제목은 필수입니다.")
        @Size(max = 100, message = "제목은 최대 100자까지 입력 가능합니다.")
        String title,

        ThumbnailDto thumbnail,
        BookDto book,

        @Size(max = 5000, message = "리뷰는 최대 5000자까지 입력 가능합니다.")
        String review,

        RecommendDto recommend,
        @NotNull Boolean isDrafted) {}
