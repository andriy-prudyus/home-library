package sql_builder;

public class From {
    
    private String tableName;
    private String tableAlias;
    
    public String getTableName() {
        return tableName;
    }
    
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    
    public String getTableAlias() {
        return tableAlias;
    }
    
    public void setTableAlias(String tableAlias) {
        this.tableAlias = tableAlias;
    }

}
