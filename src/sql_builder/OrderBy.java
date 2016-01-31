package sql_builder;

public class OrderBy {
    
    private String tableName;
    private String fieldName;
    private String direction;
    
    public String getTableName() {
        return tableName;
    }
    
    public String getFieldName() {
        return fieldName;
    }
    
    public String getDirection() {
        return direction;
    }
    
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    
    public void setDirection(String direction) {
        this.direction = direction;
    }

}
