package item;

public class BookCatalogOption extends Item {

    private int bookId;
    private int catalogOptionId;

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getCatalogOptionId() {
        return catalogOptionId;
    }

    public void setCatalogOptionId(int catalogOptionId) {
        this.catalogOptionId = catalogOptionId;
    }

}
