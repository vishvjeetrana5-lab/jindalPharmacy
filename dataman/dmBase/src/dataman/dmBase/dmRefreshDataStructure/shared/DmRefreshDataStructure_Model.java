package dataman.dmBase.dmRefreshDataStructure.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.DmEnum.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DmRefreshDataStructure_Model {

    public static class RDSException implements Serializable,IsSerializable{

        private String value1, value2, value3, value4, value5, message;

        public RDSException() {

        }

        public String getValue1() {
            return value1;
        }

        public void setValue1(String value1) {
            this.value1 = value1;
        }

        public String getValue2() {
            return value2;
        }

        public void setValue2(String value2) {
            this.value2 = value2;
        }

        public String getValue3() {
            return value3;
        }

        public void setValue3(String value3) {
            this.value3 = value3;
        }

        public String getValue4() {
            return value4;
        }

        public void setValue4(String value4) {
            this.value4 = value4;
        }

        public String getValue5() {
            return value5;
        }

        public void setValue5(String value5) {
            this.value5 = value5;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

    }

    public static class RDSTable implements Serializable,IsSerializable{

        private String moduleName, tableName;
        private List<RDSColumn> columnsList = new ArrayList();
        private List<RDSForeignKey> foreignKeysList = new ArrayList();

        public RDSTable() {
        }

        public String getModuleName() {
            return moduleName;
        }

        public void setModuleName(String moduleName) {
            this.moduleName = moduleName;
        }

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

        public List<RDSColumn> getColumnsList() {
            return columnsList;
        }

        public void setColumnsList(List<RDSColumn> columnsList) {
            this.columnsList = columnsList;
        }

        public List<RDSForeignKey> getForeignKeysList() {
            return foreignKeysList;
        }

        public void setForeignKeysList(List<RDSForeignKey> foreignKeysList) {
            this.foreignKeysList = foreignKeysList;
        }
    }

    public static class RDSColumn implements Serializable,IsSerializable{

        private String columnName, length, defaultValue;
        private SQLDataType sqlDataType;
        private boolean primaryKey, allowNull;

        public RDSColumn() {
        }

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public String getLength() {
            return length;
        }

        public void setLength(String length) {
            this.length = length;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        public void setDefaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
        }

        public SQLDataType getSqlDataType() {
            return sqlDataType;
        }

        public void setSqlDataType(SQLDataType sqlDataType) {
            this.sqlDataType = sqlDataType;
        }

        public boolean isPrimaryKey() {
            return primaryKey;
        }

        public void setPrimaryKey(boolean primaryKey) {
            this.primaryKey = primaryKey;
        }

        public boolean isAllowNull() {
            return allowNull;
        }

        public void setAllowNull(boolean allowNull) {
            this.allowNull = allowNull;
        }
    }

    public static class RDSForeignKey implements Serializable,IsSerializable{

        private String onColumn, withColumn, withTable;

        public RDSForeignKey() {
        }

        public String getOnColumn() {
            return onColumn;
        }

        public void setOnColumn(String onColumn) {
            this.onColumn = onColumn;
        }

        public String getWithColumn() {
            return withColumn;
        }

        public void setWithColumn(String withColumn) {
            this.withColumn = withColumn;
        }

        public String getWithTable() {
            return withTable;
        }

        public void setWithTable(String withTable) {
            this.withTable = withTable;
        }

    }
}
