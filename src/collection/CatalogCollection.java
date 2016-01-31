package collection;

import constants.ConstantsDb;
import item.Catalog;
import item.Item;
import model.CatalogOptionModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CatalogCollection extends Collection {

    public CatalogCollection() {
        setMainTable(ConstantsDb.TABLE_CATALOGS);
    }

    @Override
    public List<Item> load() {
        List<Item> items = new ArrayList<>();
        List<Map<String, String>> dbData = loadData();
        Catalog item;

        for (Map<String, String> row : dbData) {
            item = new Catalog();

            for (Map.Entry<String, String> entry : row.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                switch (key) {
                    case ConstantsDb.TABLE_CATALOGS_FIELD_ID:
                        item.setId(Integer.parseInt(value));
                        break;
                    case ConstantsDb.TABLE_CATALOGS_FIELD_NAME:
                        item.setName(value);
                        break;
                    case ConstantsDb.TABLE_CATALOGS_FIELD_CODE:
                        item.setCode(value);
                        break;
                    case ConstantsDb.TABLE_CATALOG_TO_LIBRARIES_FIELD_SORT:
                        item.setSort(Integer.parseInt(value));
                        break;
                    case ConstantsDb.TABLE_CATALOGS_FIELD_IS_SYSTEM:
                        if ("1".equals(value)) {
                            item.setSystem(true);
                        } else {
                            item.setSystem(false);
                        }
                        break;
                }
            }

            item.setOptions(
                    ((CatalogOptionCollection) new CatalogOptionModel().getCollection()).loadByCatalogId(item.getId())
            );
            items.add(item);
        }

        return items;
    }

    @Override
    public List<Item> load(int catalogId) {
        List<Item> items = new ArrayList<>();
        Catalog item = new Catalog();

        if (catalogId < 1) {
            items.add(item);
            return items;
        }

        getSqlBuilderSelect().where(MAIN_TABLE_ALIAS + "." + ConstantsDb.TABLE_CATALOGS_FIELD_ID + " = " + catalogId);

        return load();
    }

    public List<Item> loadByLibraryId(int libraryId) {
        getSqlBuilderSelect()
                .joinLeft(ConstantsDb.TABLE_CATALOG_TO_LIBRARIES, "cl", "cl.catalog_id = " + MAIN_TABLE_ALIAS + ".id")
                .where("cl.library_id = " + libraryId);

        return load();
    }

    /**
     * Forms list of catalogs which can be shown for user
     *
     * @return list of catalogs
     */
    public List<Item> loadForView() {
        sqlBuilderSelect.where(
                MAIN_TABLE_ALIAS + "." + ConstantsDb.TABLE_CATALOGS_FIELD_IS_SYSTEM + " <> 1 OR " + MAIN_TABLE_ALIAS +
                "." + ConstantsDb.TABLE_CATALOGS_FIELD_CODE + " = '" + ConstantsDb.CATALOG_CODE_GENRES + "'"
        );

        return load();
    }

    @Override
    public boolean save() {
        return false;
    }
}
