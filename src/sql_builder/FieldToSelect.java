package sql_builder;

public class FieldToSelect {
    
    private String tableName;
    private String fieldName = "*";
    private String fieldAlias;
    
    public String getTableName() {
        return tableName;
    }
    
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    
    public String getFieldName() {
        return fieldName;
    }
    
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    
    public String getFieldAlias() {
        return fieldAlias;
    }
    
    public void setFieldAlias(String fieldAlias) {
        this.fieldAlias = fieldAlias;
    }

}
