package BookPick.mvp.domain.curation.dto.base.update;

import BookPick.mvp.global.api.SuccessCode.SuccessCode;

public record UpdateResult(
        CurationUpdateRes curationUpdateRes,
        SuccessCode successCode
) {
    public static UpdateResult from(CurationUpdateRes curationUpdateRes, SuccessCode successCode) {
        return new UpdateResult(curationUpdateRes, successCode);
    }
}
