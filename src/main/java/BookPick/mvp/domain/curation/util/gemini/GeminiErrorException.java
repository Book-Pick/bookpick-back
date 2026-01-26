package BookPick.mvp.domain.curation.util.gemini;

import BookPick.mvp.domain.curation.util.gemini.enums.GeminiErrorCode;
import BookPick.mvp.global.exception.BusinessException;

public class GeminiErrorException extends BusinessException {

    public GeminiErrorException(){
    super(GeminiErrorCode.GEMINI_API_CALL_FAILED);

    }
}
