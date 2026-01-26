package BookPick.mvp.domain.author.exceptions;

import BookPick.mvp.domain.author.enums.AuthroErrorCode;
import BookPick.mvp.domain.book.dto.preference.BookDto;
import BookPick.mvp.global.exception.BusinessException;

public class BookNotFoundException extends BusinessException {
    public BookNotFoundException(){
        super(AuthroErrorCode.BOOK_NOT_FOUND);
    }
}
