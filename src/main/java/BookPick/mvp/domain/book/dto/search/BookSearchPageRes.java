package BookPick.mvp.domain.book.dto.search;

import BookPick.mvp.global.dto.PageInfo;
import java.util.List;

public record BookSearchPageRes(List<BookSearchRes> books, PageInfo pageInfo) {}
