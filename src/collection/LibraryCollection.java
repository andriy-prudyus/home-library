package collection;

import constants.ConstantsDb;
import item.Item;
import item.Library;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LibraryCollection extends Collection {

    public LibraryCollection() {
        setMainTable(ConstantsDb.TABLE_LIBRARIES);
    }

    @Override
    public List<Item> load() {
        List<Item> items = new ArrayList<>();
        List<Map<String, String>> dbData = loadData();
        Library item;

        for (Map<String, String> row : dbData) {
            item = new Library();

            for (Map.Entry<String, String> entry : row.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                switch (key) {
                    case ConstantsDb.TABLE_LIBRARIES_FIELD_ID:
                        item.setId(Integer.parseInt(value));
                        break;
                    case ConstantsDb.TABLE_LIBRARIES_FIELD_NAME:
                        item.setName(value);
                        break;
                    case ConstantsDb.TABLE_LIBRARIES_FIELD_SORT:
                        item.setSort(Integer.parseInt(value));
                        break;
                }
            }

            items.add(item);
        }

        return items;
    }

    @Override
    public List<Item> load(int libraryId) {
        List<Item> items = new ArrayList<>();
        Library item = new Library();

        if (libraryId < 1) {
            items.add(item);
            return items;
        }

        getSqlBuilderSelect().where(MAIN_TABLE_ALIAS + "." + ConstantsDb.TABLE_WISH_LIST_FIELD_ID + " = " + libraryId);

        return load();
    }

    @Override
    public boolean save() {
        return false;
    }

}
