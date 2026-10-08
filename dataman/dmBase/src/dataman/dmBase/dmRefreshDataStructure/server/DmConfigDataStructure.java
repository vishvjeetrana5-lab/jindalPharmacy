package dataman.dmBase.dmRefreshDataStructure.server;

import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;

public class DmConfigDataStructure {

    String tableName, classPath;
    DmRefreshDataStructure refreshDataStructure;

    public DmConfigDataStructure(String classPath, String tableName, DmRefreshDataStructure refreshDataStructure) {
        this.tableName = tableName;
        this.refreshDataStructure = refreshDataStructure;
        this.classPath = classPath;
    }

    public void setColumnValue(String columnName, SQLDataType dataType, String precisionLength, SQLColumnType columnType, SQLIsNullable isNullable, String defaultValue) throws Exception {
        try {
            chkValidation();
            refreshDataStructure.addActualStructureLog(classPath, tableName, columnName, dataType, precisionLength, columnType, isNullable, defaultValue, null, null, null, null);
        } catch (Exception ex) {
            refreshDataStructure.addResultLog("setColumnValue", tableName, classPath, columnName, ex.getMessage());
            throw new DmException(0, ex.getMessage());
        }
    }

    public void setForeignKey(String onColumn, String withColumn, String withTable) throws Exception {
        try {
            chkValidation();
            refreshDataStructure.addActualStructureLog(classPath, tableName, null, null, null, null, null, null, onColumn, withColumn, withTable, "FOREIGN");
        } catch (Exception ex) {
            refreshDataStructure.addResultLog("setForeignKey", tableName, classPath, onColumn, ex.getMessage());
            throw new DmException(0, ex.getMessage());
        }
    }

    public void setUnique(String onColumn) throws Exception {
        try {
            chkValidation();
            refreshDataStructure.addActualStructureLog(classPath, tableName, null, null, null, null, null, null, onColumn, null, null, "UNIQUE");
        } catch (Exception ex) {
            refreshDataStructure.addResultLog("setUnique", tableName, classPath, onColumn, ex.getMessage());
            throw new DmException(0, ex.getMessage());
        }
    }

    public void setIndex(String onColumn) throws Exception {
        try {
            chkValidation();
            refreshDataStructure.addActualStructureLog(classPath, tableName, null, null, null, null, null, null, onColumn, null, null, "INDEX");
        } catch (Exception ex) {
            refreshDataStructure.addResultLog("setIndex", tableName, classPath, onColumn, ex.getMessage());
            throw new DmException(0, ex.getMessage());
        }
    }

    private void chkValidation() {
        if (DmBaseShared.xNull(classPath).trim().equalsIgnoreCase("")) {
            throw new DmException(0, "Class path is mandatory");
        }

        if (DmBaseShared.xNull(tableName).trim().equalsIgnoreCase("")) {
            throw new DmException(0, "Table name is mandatory");
        }

        if (refreshDataStructure == null) {
            throw new DmException(0, "Refresh Data Structure instacnce could not be nullable");
        }
    }
}
