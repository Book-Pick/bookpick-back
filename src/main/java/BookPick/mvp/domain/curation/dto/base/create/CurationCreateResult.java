package BookPick.mvp.domain.curation.dto.base.create;

import BookPick.mvp.domain.curation.dto.base.update.CurationUpdateRes;
import BookPick.mvp.global.api.SuccessCode.SuccessCode;

public record CurationCreateResult(
        CurationCreateRes curationCreateRes,
        SuccessCode successCode
) {
    public static CurationCreateResult from(CurationCreateRes curationCreateRes, SuccessCode successCode) {
        return new CurationCreateResult(curationCreateRes, successCode);
    }
}
