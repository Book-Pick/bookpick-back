package BookPick.mvp.domain.curation.dto.base.update;

import BookPick.mvp.global.api.SuccessCode.SuccessCode;

public record CurationUpdateResult(
        CurationUpdateRes curationUpdateRes,
        SuccessCode successCode
) {
    public static CurationUpdateResult from(CurationUpdateRes curationUpdateRes, SuccessCode successCode) {
        return new CurationUpdateResult(curationUpdateRes, successCode);
    }
}
