package collection;

import database.Database;
import item.Item;
import sql_builder.SqlQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Collection {

    private String mainTable;

    protected SqlQuery.BuilderSelect sqlBuilderSelect = new SqlQuery.BuilderSelect();
    protected Database database = new Database();
    protected static final String MAIN_TABLE_ALIAS = "main_table";
    protected SqlQuery.BuilderInsert sqlBuilderInsert;
    protected SqlQuery.BuilderUpdate sqlBuilderUpdate;
    protected SqlQuery.BuilderDelete sqlBuilderDelete;
    protected List<Item> items = new ArrayList<>();

    public abstract List<Item> load();
    public abstract List<Item> load(int itemId);
    public abstract boolean save();

    private List<Map<String, String>> getDataFromDb(String sql) {
        return database.executeSelect(sql);
    }

    protected void setMainTable(String mainTable) {
        this.mainTable = mainTable;
        sqlBuilderSelect.addFieldToSelect(MAIN_TABLE_ALIAS, "*", null);
        sqlBuilderSelect.from(mainTable, MAIN_TABLE_ALIAS);
    }

    protected List<Map<String, String>> loadData() {
        return getDataFromDb(sqlBuilderSelect.build());
    }

    public SqlQuery.BuilderSelect getSqlBuilderSelect() {
        return sqlBuilderSelect;
    }

    public Database getDatabase() {
        return database;
    }
    /*public SqlQuery.BuilderInsert getSqlBuilderInsert() {
        return sqlBuilderInsert;
    }*/

    public void addItem(Item item) {
        items.add(item);
    }

    public void clearItems() {
        items = new ArrayList<>();
    }

}
