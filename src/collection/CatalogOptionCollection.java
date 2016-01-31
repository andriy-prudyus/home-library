package collection;

import constants.ConstantsDb;
import item.CatalogOption;
import item.Item;
import sql_builder.FieldValueType;
import sql_builder.SqlQuery;

import java.util.List;
import java.util.Map;

public class CatalogOptionCollection extends Collection {

    public CatalogOptionCollection() {
        setMainTable(ConstantsDb.TABLE_CATALOG_OPTIONS);
    }

    private boolean insertCatalogOption(CatalogOption catalogOption) {
        sqlBuilderInsert = new SqlQuery.BuilderInsert();
        sqlBuilderInsert.setTable(ConstantsDb.TABLE_CATALOG_OPTIONS)
                .addFieldValue(
                        new FieldValueType(
                                ConstantsDb.TABLE_CATALOG_OPTIONS_FIELD_CATALOG_ID,
                                String.valueOf(catalogOption.getCatalogId()),
                                ConstantsDb.TYPE_INT
                        )
                )
                .addFieldValue(
                        new FieldValueType(
                                ConstantsDb.TABLE_CATALOG_OPTIONS_FIELD_VALUE,
                                catalogOption.getValue(),
                                ConstantsDb.TYPE_STRING
                        )
                );

        return database.executeQuery(sqlBuilderInsert.build(), true);
    }

    private List<Item> load(int catalogId, String optionValue) {
        sqlBuilderSelect.where(
                MAIN_TABLE_ALIAS + "." + ConstantsDb.TABLE_CATALOG_OPTIONS_FIELD_CATALOG_ID + " = " +
                        catalogId + " AND " + MAIN_TABLE_ALIAS + "." + ConstantsDb.TABLE_CATALOG_OPTIONS_FIELD_VALUE +
                        " = '" + optionValue + "'"
                );

        return load();
    }

    @Override
    public List<Item> load() {
        clearItems();
        List<Map<String, String>> dbData = loadData();
        CatalogOption item;

        for (Map<String, String> row : dbData) {
            item = new CatalogOption();

            for (Map.Entry<String, String> entry : row.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                switch (key) {
                    case ConstantsDb.TABLE_CATALOG_OPTIONS_FIELD_ID:
                        item.setId(Integer.parseInt(value));
                        break;
                    case ConstantsDb.TABLE_CATALOG_OPTIONS_FIELD_CATALOG_ID:
                        item.setCatalogId(Integer.parseInt(value));
                        break;
                    case ConstantsDb.TABLE_CATALOG_OPTIONS_FIELD_VALUE:
                        item.setValue(value);
                        break;
                }
            }

            items.add(item);
        }

        return items;
    }

    @Override
    public List<Item> load(int optionId) {
        clearItems();
        CatalogOption item = new CatalogOption();

        if (optionId < 1) {
            items.add(item);
            return items;
        }

        getSqlBuilderSelect().where(
                MAIN_TABLE_ALIAS + "." + ConstantsDb.TABLE_CATALOG_OPTIONS_FIELD_ID + "=" + optionId
        );

        return load();
    }

    public List<Item> loadByCatalogId(int catalogId) {
        sqlBuilderSelect.where(
                MAIN_TABLE_ALIAS + "." + ConstantsDb.TABLE_CATALOG_OPTIONS_FIELD_CATALOG_ID + "=" + catalogId
        );

        return load();
    }

    public List<Item> loadEmptyCatalogOptions() {
        sqlBuilderSelect
                .joinLeft(
                        ConstantsDb.TABLE_BOOK_TO_CATALOG_OPTIONS,
                        "bco",
                        "bco." + ConstantsDb.TABLE_BOOK_TO_CATALOG_OPTIONS_FIELD_OPTION_ID + "=" + MAIN_TABLE_ALIAS +
                                "." + ConstantsDb.TABLE_CATALOG_OPTIONS_FIELD_ID
                )
                .where("bco." + ConstantsDb.TABLE_BOOK_TO_CATALOG_OPTIONS_FIELD_OPTION_ID + " IS NULL");

        return load();
    }

    public CatalogOption getOptionByBookIdAndCatalogId(int bookId, int catalogId) {
        sqlBuilderSelect
                .joinLeft(
                        ConstantsDb.TABLE_BOOK_TO_CATALOG_OPTIONS, "bco", "bco." +
                        ConstantsDb.TABLE_BOOK_TO_CATALOG_OPTIONS_FIELD_OPTION_ID + "=" + MAIN_TABLE_ALIAS + "." +
                        ConstantsDb.TABLE_CATALOG_OPTIONS_FIELD_ID
                )
                .where(
                        MAIN_TABLE_ALIAS + "." + ConstantsDb.TABLE_CATALOG_OPTIONS_FIELD_CATALOG_ID + "=" +
                        catalogId + " AND bco." + ConstantsDb.TABLE_BOOK_TO_CATALOG_OPTIONS_FIELD_BOOK_ID + "=" + bookId
                );

        CatalogOption option = new CatalogOption();
        List<Item> data = load();

        if (!data.isEmpty()) {
            option = (CatalogOption) data.get(0);
        }

        return option;
    }

    public int getOptionId(int catalogId, String optionValue) {
        int optionId = -1;
        List<Item> options = load(catalogId, optionValue);

        for (Item item : options) {
            optionId = item.getId();
        }

        return optionId;
    }

    @Override
    public boolean save() {
        boolean result = true;
        CatalogOption catalogOption;

        for (Item item : items) {
            catalogOption = (CatalogOption) item;
            int id = catalogOption.getId();

            if (id < 1) {
                result = insertCatalogOption(catalogOption);
            }
        }

        return result;
    }

}
