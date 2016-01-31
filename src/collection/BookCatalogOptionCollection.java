package collection;

import constants.ConstantsDb;
import item.BookCatalogOption;
import item.Item;
import sql_builder.FieldValueType;
import sql_builder.SqlQuery;

import java.util.List;
import java.util.Map;

public class BookCatalogOptionCollection extends Collection {

    public BookCatalogOptionCollection() {
        setMainTable(ConstantsDb.TABLE_BOOK_TO_CATALOG_OPTIONS);
    }

    private boolean insertBookCatalogOption(BookCatalogOption bookCatalogOption) {
        sqlBuilderInsert = new SqlQuery.BuilderInsert();
        sqlBuilderInsert.setTable(ConstantsDb.TABLE_BOOK_TO_CATALOG_OPTIONS)
                .addFieldValue(
                        new FieldValueType(
                                ConstantsDb.TABLE_BOOK_TO_CATALOG_OPTIONS_FIELD_BOOK_ID,
                                String.valueOf(bookCatalogOption.getBookId()),
                                ConstantsDb.TYPE_INT
                        )
                )
                .addFieldValue(
                        new FieldValueType(
                                ConstantsDb.TABLE_BOOK_TO_CATALOG_OPTIONS_FIELD_OPTION_ID,
                                String.valueOf(bookCatalogOption.getCatalogOptionId()),
                                ConstantsDb.TYPE_INT
                        )
                );

        return database.executeQuery(sqlBuilderInsert.build(), true);
    }

    @Override
    public List<Item> load() {
        clearItems();
        List<Map<String, String>> dbData = loadData();
        BookCatalogOption item;

        for (Map<String, String> row : dbData) {
            item = new BookCatalogOption();

            for (Map.Entry<String, String> entry : row.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                switch (key) {
                    case ConstantsDb.TABLE_BOOK_TO_CATALOG_OPTIONS_FIELD_ID:
                        item.setId(Integer.parseInt(value));
                        break;
                    case ConstantsDb.TABLE_BOOK_TO_CATALOG_OPTIONS_FIELD_BOOK_ID:
                        item.setBookId(Integer.parseInt(value));
                        break;
                    case ConstantsDb.TABLE_BOOK_TO_CATALOG_OPTIONS_FIELD_OPTION_ID:
                        item.setCatalogOptionId(Integer.parseInt(value));
                        break;
                }
            }

            items.add(item);
        }

        return items;
    }

    @Override
    public List<Item> load(int id) {
        clearItems();
        BookCatalogOption item = new BookCatalogOption();

        if (id < 1) {
            items.add(item);
            return items;
        }

        sqlBuilderSelect.where(
                MAIN_TABLE_ALIAS + "." + ConstantsDb.TABLE_BOOK_TO_CATALOG_OPTIONS_FIELD_ID + " = " + id
        );

        return load();
    }

    @Override
    public boolean save() {
        boolean result = true;
        BookCatalogOption bookCatalogOption;

        for (Item item : items) {
            bookCatalogOption = (BookCatalogOption) item;
            int id = bookCatalogOption.getId();

            if (id < 1) {
                result = insertBookCatalogOption(bookCatalogOption);
            }
        }

        return result;
    }

}
