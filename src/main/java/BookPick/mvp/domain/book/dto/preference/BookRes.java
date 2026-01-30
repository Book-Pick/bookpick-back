package BookPick.mvp.domain.book.dto.preference;

public record BookRes(String title, String author, String image, String isbn) {

    public static BookRes from(String title, String author, String image, String isbn) {
        return new BookRes(title, author, image, isbn);
    }
}
