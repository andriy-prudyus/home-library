package collection;

import constants.ConstantsDb;
import item.Item;
import item.WishList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WishListCollection extends Collection {

    public WishListCollection() {
        setMainTable(ConstantsDb.TABLE_WISH_LIST);
    }

    @Override
    public List<Item> load() {
        List<Item> items = new ArrayList<>();
        List<Map<String, String>> dbData = loadData();

        for (Map<String, String> row : dbData) {
            WishList item = new WishList();

            for (Map.Entry<String, String> entry : row.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                switch (key) {
                    case ConstantsDb.TABLE_WISH_LIST_FIELD_ID:
                        item.setId(Integer.parseInt(value));
                        break;
                    case ConstantsDb.TABLE_WISH_LIST_FIELD_NAME:
                        item.setName(value);
                        break;
                    case ConstantsDb.TABLE_WISH_LIST_FIELD_SORT:
                        item.setSort(Integer.parseInt(value));
                        break;
                }

                items.add(item);
            }
        }

        return items;
    }

    @Override
    public List<Item> load(int wishListId) {
        List<Item> items = new ArrayList<>();
        WishList item = new WishList();

        if (wishListId < 1) {
            items.add(item);
            return items;
        }

        getSqlBuilderSelect().where(MAIN_TABLE_ALIAS + "." + ConstantsDb.TABLE_WISH_LIST_FIELD_ID + " = " + wishListId);

        return load();
    }

    @Override
    public boolean save() {
        return false;
    }

}
