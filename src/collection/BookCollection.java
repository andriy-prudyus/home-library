package collection;

import constants.ConstantsDb;
import item.Book;
import item.BookCatalogOption;
import item.Catalog;
import item.CatalogOption;
import item.Item;
import model.CatalogOptionModel;
import sql_builder.FieldValueType;
import sql_builder.SqlBuilder;
import sql_builder.SqlQuery;
import view.helper.Author;
import view.helper.Helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BookCollection extends Collection {

    private int lastInsertId = -1;
    private Set<Integer> catalogOptionIds;

    public BookCollection() {
        setMainTable(ConstantsDb.TABLE_BOOKS);
    }

    private boolean addBook(Book book) {
        sqlBuilderInsert = new SqlQuery.BuilderInsert();
        sqlBuilderInsert
                .setTable(ConstantsDb.TABLE_BOOKS)
                .addFieldValueAll(getFieldValues(book));
        boolean result = database.executeQuery(sqlBuilderInsert.build(), true);

        if (result) {
            lastInsertId = database.getLastInsertId();
        }

        return result;
    }

    private boolean updateBook(Book book) {
        sqlBuilderUpdate = new SqlQuery.BuilderUpdate();
        sqlBuilderUpdate
                .setTable(ConstantsDb.TABLE_BOOKS)
                .setWhere(ConstantsDb.TABLE_BOOKS_FIELD_ID + "=" + book.getId())
                .addFieldValueAll(getFieldValues(book));

        return database.executeQuery(sqlBuilderUpdate.build(), false);
    }

    private static List<FieldValueType> getFieldValues(Book book) {
        List<FieldValueType> result = new ArrayList<>();
        result.add(
                new FieldValueType(
                        ConstantsDb.TABLE_BOOKS_FIELD_AUTHOR,
                        book.getNewValueAuthor(),
                        ConstantsDb.TYPE_STRING
                )
        );
        result.add(
                new FieldValueType(
                        ConstantsDb.TABLE_BOOKS_FIELD_BOOK_NAME,
                        book.getNewValueBookName(),
                        ConstantsDb.TYPE_STRING
                )
        );
        result.add(
                new FieldValueType(
                        ConstantsDb.TABLE_BOOKS_FIELD_PUBLICATION_YEAR,
                        String.valueOf(book.getNewValuePublicationYear()),
                        ConstantsDb.TYPE_INT
                )
        );
        result.add(
                new FieldValueType(
                        ConstantsDb.TABLE_BOOKS_FIELD_PUBLICATION,
                        book.getNewValuePublication(),
                        ConstantsDb.TYPE_STRING
                )
        );
        result.add(
                new FieldValueType(
                        ConstantsDb.TABLE_BOOKS_FIELD_NOTES,
                        book.getNewValueNotes(),
                        ConstantsDb.TYPE_STRING
                )
        );
        result.add(
                new FieldValueType(
                        ConstantsDb.TABLE_BOOKS_FIELD_LIBRARY_ID,
                        String.valueOf(book.getNewValueLibraryId()),
                        ConstantsDb.TYPE_INT
                )
        );

        return result;
    }

    private boolean processCatalogs(Book book) {
        boolean result = true;
        int catalogId;
        Catalog catalog;
        List<Item> catalogs = new CatalogCollection().load();

        for (Item item : catalogs) {
            catalog = (Catalog) item;
            catalogId = catalog.getId();

            if (catalog.isSystem()) {
                String catalogCode = catalog.getCode();

                switch (catalogCode) {
                    case ConstantsDb.CATALOG_CODE_AUTHOR:
                        processCatalogAuthor(book, catalogId);
                        break;
                    case ConstantsDb.CATALOG_CODE_BOOK_NAME:
                        processCatalogBookName(book, catalogId);
                        break;
                    case ConstantsDb.CATALOG_CODE_PUBLICATION_YEAR:
                        processCatalogPublicationYear(book, catalogId);
                        break;
                    case ConstantsDb.CATALOG_CODE_GENRES:
                        processCatalogGenre(book);
                        break;
                }
            } else {
                // TODO fill not system catalogs
            }
        }

        return result;
    }

    private void processCatalogAuthor(Book book, int catalogId) {
        for (Author author : Helper.getAuthorListFromValue(book.getNewValueAuthor())) {
            String lastName = author.getLastName();

            if (lastName.isEmpty()) {
                continue;
            }

            processCatalogOptionAndBook(book, String.valueOf(lastName.charAt(0)).toUpperCase(), catalogId);
        }
    }

    private void processCatalogBookName(Book book, int catalogId) {
        processCatalogOptionAndBook(book, String.valueOf(book.getNewValueBookName().charAt(0)).toUpperCase(), catalogId);
    }

    private void processCatalogPublicationYear(Book book, int catalogId) {
        processCatalogOptionAndBook(book, String.valueOf(book.getNewValuePublicationYear()), catalogId);
    }

    private void processCatalogGenre(Book book) {
        int bookId = book.getId() < 1 ? lastInsertId : book.getId();
        Collection collection = new BookCatalogOptionCollection();
        BookCatalogOption bookCatalogOption;

        for (Integer optionId : catalogOptionIds) {
            bookCatalogOption = new BookCatalogOption();
            bookCatalogOption.setCatalogOptionId(optionId);
            bookCatalogOption.setBookId(bookId);
            collection.addItem(bookCatalogOption);
        }

        collection.save();
    }

    private void processCatalogOptionAndBook(Book book, String optionValue, int catalogId) {
        CatalogOptionCollection catalogOptionCollection = new CatalogOptionCollection();
        int optionId = catalogOptionCollection.getOptionId(catalogId, optionValue);
        int bookId = book.getId() < 1 ? lastInsertId : book.getId();

        if (optionId < 1) {
            optionId = saveCatalogOption(catalogId, optionValue);
        }

        if (optionId > 0) {
            saveBookToCatalogOption(bookId, optionId);
        }
    }

    private static int saveCatalogOption(int catalogId, String value) {
        int optionId = -1;
        CatalogOption option = new CatalogOption();
        option.setCatalogId(catalogId);
        option.setValue(value);

        CatalogOptionCollection catalogOptionCollection = new CatalogOptionCollection();
        catalogOptionCollection.addItem(option);
        boolean result = catalogOptionCollection.save();

        if (result) {
            optionId = catalogOptionCollection.getDatabase().getLastInsertId();
        }

        return optionId;
    }

    private static boolean saveBookToCatalogOption(int bookId, int optionId) {
        BookCatalogOption bookCatalogOption = new BookCatalogOption();
        bookCatalogOption.setBookId(bookId);
        bookCatalogOption.setCatalogOptionId(optionId);

        BookCatalogOptionCollection bookCatalogOptionCollection = new BookCatalogOptionCollection();
        bookCatalogOptionCollection.addItem(bookCatalogOption);

        return bookCatalogOptionCollection.save();
    }

    /**
     * Deletes mapping of book to catalog options by book id
     *
     * @param book records will be deleted by this book
     */
    private void deleteBookCatalogOptions(Book book) {
        if (book.getId() < 1) {
            return;
        }

        sqlBuilderDelete = new SqlQuery.BuilderDelete();
        sqlBuilderDelete
                .from(ConstantsDb.TABLE_BOOK_TO_CATALOG_OPTIONS)
                .where(
                        ConstantsDb.TABLE_BOOK_TO_CATALOG_OPTIONS_FIELD_BOOK_ID + "=" + book.getId()
                );
        database.executeQuery(sqlBuilderDelete.build(), false);
    }

    /**
     * Deletes catalog options which are not linked any book to
     */
    private void deleteEmptyCatalogOptions() {
        String[] optionIds = getEmptyCatalogOptionIds();

        if (optionIds.length == 0) {
            return;
        }

        sqlBuilderDelete = new SqlQuery.BuilderDelete();
        sqlBuilderDelete
                .from(ConstantsDb.TABLE_CATALOG_OPTIONS)
                .where(ConstantsDb.TABLE_CATALOG_OPTIONS_FIELD_ID + " IN (" + String.join(",", optionIds) + ")");
        database.executeQuery(sqlBuilderDelete.build(), false);
    }

    private String[] getEmptyCatalogOptionIds() {
        List<Item> options = ((CatalogOptionCollection) new CatalogOptionModel().getCollection()).loadEmptyCatalogOptions();
        String[] result = new String[options.size()];
        int i = 0;

        for (Item item : options) {
            result[i++] = String.valueOf(item.getId());
        }

        return result;
    }

    @Override
    public List<Item> load() {
        clearItems();
        List<Map<String, String>> dbData = loadData();
        Book item;

        for (Map<String, String> row : dbData) {
            item = new Book();

            for (Map.Entry<String, String> entry : row.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                switch (key) {
                    case ConstantsDb.TABLE_BOOKS_FIELD_ID:
                        item.setId(Integer.parseInt(value));
                        break;
                    case ConstantsDb.TABLE_BOOKS_FIELD_BOOK_NAME:
                        item.setOriginalValueBookName(value);
                        break;
                    case ConstantsDb.TABLE_BOOKS_FIELD_AUTHOR:
                        item.setOriginalValueAuthor(value);
                        break;
                    case ConstantsDb.TABLE_BOOKS_FIELD_PUBLICATION:
                        item.setOriginalValuePublication(value);
                        break;
                    case ConstantsDb.TABLE_BOOKS_FIELD_PUBLICATION_YEAR:
                        item.setOriginalValuePublicationYear(Integer.parseInt(value));
                        break;
                    case ConstantsDb.TABLE_BOOKS_FIELD_NOTES:
                        item.setOriginalValueNotes(value);
                        break;
                    case ConstantsDb.TABLE_BOOKS_FIELD_LIBRARY_ID:
                        item.setOriginalValueLibraryId(Integer.parseInt(value));
                        break;
                }
            }

            items.add(item);
        }

        return items;
    }

    @Override
    public List<Item> load(int bookId) {
        clearItems();
        Book item = new Book();

        if (bookId < 1) {
            items.add(item);
            return items;
        }

        getSqlBuilderSelect().where(MAIN_TABLE_ALIAS + "." + ConstantsDb.TABLE_BOOKS_FIELD_ID + " = " + bookId);

        return load();
    }

    public List<Item> loadByLibraryId(int libraryId) {
        sqlBuilderSelect.where(MAIN_TABLE_ALIAS + "." + ConstantsDb.TABLE_BOOKS_FIELD_LIBRARY_ID + " = " + libraryId);

        return load();
    }

    @Override
    public boolean save() {
        boolean result = true;
        Book book;

        for (Item item : items) {
            book = (Book) item;

            if (book.getId() < 1) {
                result = addBook(book);
            } else {
                result = updateBook(book);
                deleteBookCatalogOptions(book);
            }

            if (result) {
                result = processCatalogs(book);
            }
        }

        deleteEmptyCatalogOptions();

        return result;
    }

    public void setCatalogOptionIds(Set<Integer> catalogOptionIds) {
        this.catalogOptionIds = catalogOptionIds;
    }
}
