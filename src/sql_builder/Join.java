package sql_builder;

public class Join {

    private String typeJoin;
    private String tableName;
    private String tableAlias;
    private String onCondition;
    
    public String getTypeJoin() {
        return typeJoin;
    }
    
    public String getTableName() {
        return tableName;
    }
    
    public String getTableAlias() {
        return tableAlias;
    }
    
    public String getOnCondition() {
        return onCondition;
    }
    
    public void setTypeJoin(String type) {
        this.typeJoin = type;
    }
    
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    
    public void setTableAlias(String tableAlias) {
        this.tableAlias = tableAlias;
    }
    
    public void setOnCondition(String onCondition) {
        this.onCondition = onCondition;
    }
    
}
