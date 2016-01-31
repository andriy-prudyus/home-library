package sql_builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import constants.ConstantsDb;
import constants.ConstantsSql;

public class SqlQuery {
    
    private SqlQuery() {}

    /**
     * Builds sql select statement
     */
    public static class BuilderSelect extends SqlBuilder {
        private List<FieldToSelect> fieldsToSelect = new ArrayList<>();
        private List<Join> joins = new ArrayList<>();
        private List<OrderBy> orderBy = new ArrayList<>();
        private List<GroupBy> groupBy = new ArrayList<>();
        private From from;
        private String where;

        private String generateSelectPart() {
            StringBuilder result = new StringBuilder("SELECT ");

            if (fieldsToSelect == null) {
                String tableAlias = from.getTableAlias();

                if (tableAlias != null && !tableAlias.isEmpty()) {
                    result.append(tableAlias);
                } else {
                    result.append(from.getTableName());
                }

                result.append(".*");
            } else {
                boolean flag = false;

                for (FieldToSelect field : fieldsToSelect) {
                    String tableName = field.getTableName();
                    String fieldAlias = field.getFieldAlias();

                    if (flag) {
                        result.append(", ");
                    }

                    if (tableName != null && !tableName.isEmpty()) {
                        result.append(tableName);
                        result.append(".");
                    }

                    result.append(field.getFieldName());

                    if (fieldAlias != null && !fieldAlias.isEmpty()) {
                        result.append(" ");
                        result.append(fieldAlias);
                    }

                    flag = true;
                }
            }

            return result.toString();
        }

        private String generateFromPart() {
            StringBuilder result = new StringBuilder(" FROM ");
            result.append(from.getTableName());
            String tableAlias = from.getTableAlias();

            if (tableAlias != null && !tableAlias.isEmpty()) {
                result.append(" ");
                result.append(tableAlias);
            }

            return result.toString();
        }

        private String generateJoinPart() {
            StringBuilder result = new StringBuilder();

            for (Join join : joins) {
                result.append(" ");
                result.append(join.getTypeJoin());
                result.append(" JOIN ");
                result.append(join.getTableName());
                result.append(" ");
                result.append(join.getTableAlias());
                result.append(" ON ");
                result.append(join.getOnCondition());
            }

            return result.toString();
        }

        private String generateWherePart() {
            StringBuilder result = new StringBuilder();

            if (where != null && !where.isEmpty()) {
                result.append(" WHERE ");
                result.append(where);
            }

            return result.toString();
        }

        private String generateGroupByPart() {
            StringBuilder result = new StringBuilder();
            boolean flag = false;

            if (!groupBy.isEmpty()) {
                result.append(" GROUP BY ");
            }

            for (GroupBy grBy : groupBy) {
                String tableName = grBy.getTableName();

                if (flag) {
                    result.append(", ");
                }

                if (tableName != null && !tableName.isEmpty()) {
                    result.append(tableName);
                    result.append(".");
                }

                result.append(grBy.getFieldName());
                flag = true;
            }

            return result.toString();
        }

        private String generateOrderByPart() {
            StringBuilder result = new StringBuilder();
            boolean flag = false;

            for (OrderBy ordBy : orderBy) {
                String tableName = ordBy.getTableName();

                if (flag) {
                    result.append(", ");
                }

                if (tableName != null && !tableName.isEmpty()) {
                    result.append(tableName);
                    result.append(".");
                }

                result.append(ordBy.getFieldName());
                result.append(" ");
                result.append(ordBy.getDirection());
                flag = true;
            }

            return result.toString();
        }

        public BuilderSelect addFieldToSelect(String tableName, String fieldName, String fieldAlias) {
            FieldToSelect field = new FieldToSelect();
            field.setTableName(tableName);
            field.setFieldName(fieldName);
            field.setFieldAlias(fieldAlias);     
            
            fieldsToSelect.add(field);
            
            return this;
        }
        
        public BuilderSelect from(String tableName, String tableAlias) {
            from = new From();
            from.setTableName(tableName);
            from.setTableAlias(tableAlias);
            
            return this;
        }
        
        public BuilderSelect joinLeft(String tableName, String tableAlias, String onCondition) {
            Join join = new Join();
            join.setTypeJoin(ConstantsSql.JOIN_LEFT);
            join.setTableName(tableName);
            join.setTableAlias(tableAlias);
            join.setOnCondition(onCondition);
            
            this.joins.add(join);
            
            return this;
        }
        
        public BuilderSelect joinInner(String tableName, String tableAlias, String onCondition) {
            Join join = new Join();
            join.setTypeJoin(ConstantsSql.JOIN_INNER);
            join.setTableName(tableName);
            join.setTableAlias(tableAlias);
            join.setOnCondition(onCondition);
            
            this.joins.add(join);
            
            return this;
        }
        
        public BuilderSelect where(String whereCondition) {
            where = whereCondition;
            
            return this;
        }
        
        public BuilderSelect orderBy(String tableName, String fieldName, String direction) {
            OrderBy orderBy = new OrderBy();
            orderBy.setTableName(tableName);
            orderBy.setFieldName(fieldName);
            orderBy.setDirection(direction);
            
            this.orderBy.add(orderBy);
            
            return this;
        }
        
        public BuilderSelect groupBy(String tableName, String fieldName) {
            GroupBy groupBy = new GroupBy();
            groupBy.setTableName(tableName);
            groupBy.setFieldName(fieldName);
            
            this.groupBy.add(groupBy);
            
            return this;
        }

        @Override
        public String build() {
            if (from == null) {
                return null;
            }

            return generateSelectPart() + generateFromPart() + generateJoinPart() + generateWherePart() +
                    generateGroupByPart() + generateOrderByPart();
        }
    }

    /**
     * Builds sql insert statement
     */
    public static class BuilderInsert extends SqlBuilder {

        private String table;
        private List<FieldValueType> fieldValues = new ArrayList<>();

        private String generateIntoTablePart() {
            return "INSERT INTO " + table;
        }

        private String generateFieldValuesPart() {
            StringBuilder fieldsPart = new StringBuilder(" (");
            StringBuilder valuesPart = new StringBuilder(" VALUES (");
            boolean flag = false;

            for (FieldValueType fieldValueType : fieldValues) {
                if (flag) {
                    fieldsPart.append(", ");
                    valuesPart.append(", ");
                }

                fieldsPart.append(fieldValueType.getField());
                boolean valueInt = false;

                if (ConstantsDb.TYPE_INT.equals(fieldValueType.getType())) {
                    valueInt = true;
                }

                if (!valueInt) {
                    valuesPart.append("'");
                }

                valuesPart.append(fieldValueType.getValue());

                if (!valueInt) {
                    valuesPart.append("'");
                }

                flag = true;
            }

            fieldsPart.append(")");
            valuesPart.append(")");

            return fieldsPart.toString() + valuesPart.toString();
        }

        public BuilderInsert setTable(String table) {
            this.table = table;
            return this;
        }

        public BuilderInsert addFieldValue(FieldValueType fieldValueType) {
            fieldValues.add(fieldValueType);
            return this;
        }

        public BuilderInsert addFieldValueAll(List<FieldValueType> fieldValueTypes) {
            for (FieldValueType fieldValueType : fieldValueTypes) {
                fieldValues.add(fieldValueType);
            }

            return this;
        }

        @Override
        public String build() {
            return generateIntoTablePart() + generateFieldValuesPart();
        }
    }

    /**
     * Builds sql update statement
     */
    public static class BuilderUpdate extends SqlBuilder {
        private String table;
        private List<FieldValueType> fieldValues = new ArrayList<>();
        private String where;

        private String generateUpdatePart() {
            return "UPDATE " + table;
        }

        private String generateSetPart() {
            StringBuilder result = new StringBuilder(" SET ");
            boolean flag = false;
            boolean isString;

            for (FieldValueType fieldValue : fieldValues) {
                isString = ConstantsDb.TYPE_STRING.equals(fieldValue.getType());

                if (flag) {
                    result.append(", ");
                }

                result.append(fieldValue.getField());
                result.append(isString ? "='" : "=");
                result.append(fieldValue.getValue());
                result.append(isString ? "'" : "");

                flag = true;
            }

            return result.toString();
        }

        private String generateWherePart() {
            return " WHERE " + where;
        }

        public BuilderUpdate addFieldValue(FieldValueType fieldValueType) {
            fieldValues.add(fieldValueType);
            return this;
        }

        public BuilderUpdate addFieldValueAll(List<FieldValueType> fieldValueTypes) {
            for (FieldValueType fieldValueType : fieldValueTypes) {
                fieldValues.add(fieldValueType);
            }

            return this;
        }

        public BuilderUpdate setTable(String table) {
            this.table = table;
            return this;
        }

        public BuilderUpdate setWhere(String where) {
            this.where = where;
            return this;
        }

        @Override
        public String build() {
            return generateUpdatePart() + generateSetPart() + generateWherePart();
        }
    }

    public static class BuilderDelete extends SqlBuilder {
        private String table;
        private String where;

        private String generateDeleteFromPart() {
            return "DELETE FROM " + table;
        }

        private String generateWherePart() {
            return " WHERE " + where;
        }

        public BuilderDelete from(String table) {
            this.table = table;
            return this;
        }

        public BuilderDelete where(String where) {
            this.where = where;
            return this;
        }

        @Override
        public String build() {
            return generateDeleteFromPart() + generateWherePart();
        }
    }
    
}
