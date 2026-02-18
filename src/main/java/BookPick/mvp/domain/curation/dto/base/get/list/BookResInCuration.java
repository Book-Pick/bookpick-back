package BookPick.mvp.domain.curation.dto.base.get.list;

public record BookResInCuration(String title, String author, String isbn) {

    public static BookResInCuration from(String title, String author, String isbn) {
        return new BookResInCuration(title, author, isbn);
    }
}
