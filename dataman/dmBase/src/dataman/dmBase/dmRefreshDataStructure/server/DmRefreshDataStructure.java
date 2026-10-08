package dataman.dmBase.dmRefreshDataStructure.server;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import java.io.FileWriter;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;

public class DmRefreshDataStructure {

    public interface CallBack {

        public Connection getConnection(String databaseName);
    }

    private String actualStructureFileName = "", resultLogFileName = "", databaseName = "";
    private FileWriter actualStructureLog;
    private FileWriter resultLog;
    private Connection cnTemp;
    private CallBack callBack;
    private boolean dropAllKeysBeforeRepair = false;

    public DmRefreshDataStructure(String databaseName, boolean dropAllKeysBeforeRepair, CallBack callBack) throws Exception {
        String sqlQuery, fileName;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;

        this.dropAllKeysBeforeRepair = dropAllKeysBeforeRepair;
        this.callBack = callBack;
        this.databaseName = databaseName.trim();
        fileName = String.valueOf(new Date().getTime()) + "_" + this.databaseName;
        actualStructureFileName = DmBaseServer.Path.repository + "/logs/" + fileName + "_actualStructure.log";
        resultLogFileName = DmBaseServer.Path.repository + "/logs/" + fileName + "_resultLog.log";

        try {
//            cnTemp = DmBaseServer.getConnection("mysql");
            cnTemp = callBack.getConnection("master");
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, "Select count(*) as cnt From information_schema.SCHEMATA where Schema_Name = '" + this.databaseName + "';");
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, "select count(*) as cnt from sysdatabases where name = '" + this.databaseName + "';");
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getInt("cnt") <= 0) {
                rsTemp.close();
                psTemp.close();
//                psTemp = DmPreparedStatement.prepareStatement(cnTemp, "CREATE DATABASE `" + this.databaseName + "` DEFAULT CHARACTER SET utf8;");
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, "create database " + this.databaseName + ";");
                psTemp.executeUpdate();
                psTemp.close();
                cnTemp.close();
                Thread.sleep(5000);
            }
        } catch (Exception ex) {
            throw new DmException(0, ex.getMessage());
        } finally {
            if (rsTemp != null) {
                rsTemp.close();
            }

            if (psTemp != null) {
                psTemp.close();
            }

            if (cnTemp != null && !cnTemp.isClosed()) {
                cnTemp.close();
            }
        }

        try {
            cnTemp = callBack.getConnection(databaseName);
//            cnTemp = DmBaseServer.getConnection(this.boneCP, databaseName);

            actualStructureLog = new FileWriter(actualStructureFileName);
//            sqlQuery = "drop table if exists actualStructure;";
            sqlQuery = "IF OBJECT_ID('tempdb.dbo.##actualStructure', 'U') IS NOT NULL DROP TABLE ##actualStructure;";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.executeUpdate();
            psTemp.close();
            actualStructureLog.write(sqlQuery + "\n");

//            sqlQuery = "create temporary table actualStructure ( "
//                    + "rowId bigint unsigned auto_increment primary key, "
//                    + "classPath varchar(100), "
//                    + "tableName varchar(100), "
//                    + "columnName varchar(100), "
//                    + "dataType varchar(100), "
//                    + "precisionLength varchar(20), "
//                    + "columnType varchar(100), "
//                    + "isNullable varchar(20), "
//                    + "defaultValue varchar(100), "
//                    + "onColumn varchar(100), "
//                    + "withColumn varchar(100), "
//                    + "withTable varchar(100) "
//                    + ") "
//                    + "ENGINE=InnoDB DEFAULT CHARSET=utf8;";
            sqlQuery = "create table ##actualStructure ( "
                    + "rowId bigint identity(1,1) primary key, "
                    + "classPath varchar(100), "
                    + "tableName varchar(100), "
                    + "columnName varchar(100), "
                    + "dataType varchar(100), "
                    + "precisionLength varchar(20), "
                    + "columnType varchar(100), "
                    + "isNullable varchar(20), "
                    + "defaultValue varchar(100), "
                    + "onColumn varchar(100), "
                    + "withColumn varchar(100), "
                    + "withTable varchar(100) "
                    + "); ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.executeUpdate();
            psTemp.close();
            actualStructureLog.write(sqlQuery + "\n");
            actualStructureLog.close();
            actualStructureLog = null;

            resultLog = new FileWriter(resultLogFileName);
//            sqlQuery = "drop table if exists result;";
            sqlQuery = "IF OBJECT_ID('tempdb.dbo.##result', 'U') IS NOT NULL DROP TABLE ##result;";
            resultLog.write(sqlQuery + "\n");

//            sqlQuery = "create temporary table result ( "
//                    + "rowId bigint unsigned auto_increment primary key, "
//                    + "procedureName varchar(50), "
//                    + "tableName varchar(50), "
//                    + "otherInfo varchar(50), "
//                    + "sqlStatement varchar(1000), "
//                    + "sqlResult varchar(1000) "
//                    + ") "
//                    + "ENGINE=InnoDB DEFAULT CHARSET=utf8;";
            sqlQuery = "create table ##result ( "
                    + "rowId bigint identity(1,1) primary key, "
                    + "procedureName varchar(50), "
                    + "tableName varchar(50), "
                    + "otherInfo varchar(50), "
                    + "sqlStatement varchar(1000), "
                    + "sqlResult varchar(1000) "
                    + "); ";
            resultLog.write(sqlQuery + "\n");
            resultLog.close();
            resultLog = null;
        } catch (Exception ex) {
            throw new DmException(0, ex.getMessage());
        } finally {
            if (psTemp != null) {
                psTemp.close();
            }
        }
    }

    public String getDatabaseName() {
        return this.databaseName;
    }

    public String getResultLogFilePath() {
        return resultLogFileName;
    }

    public String getActualStructureFilePath() {
        return actualStructureFileName;
    }

    public void close() {
        try {
            if (cnTemp != null && !cnTemp.isClosed()) {
                cnTemp.close();
                cnTemp = null;
            }
        } catch (Exception ex) {
        }

        try {
            if (resultLog != null) {
                resultLog.close();
                resultLog = null;
            }
        } catch (Exception ex) {
        }

        try {
            if (actualStructureLog != null) {
                actualStructureLog.close();
                actualStructureLog = null;
            }
        } catch (Exception ex) {
        }

    }

    //Note:- This Is A Function For Creating Tables In Related Database
    //It Does Not Remove Fields But Add/Alter Fields And Keys
    public void executeDBScript() throws Exception {
        //Note:- Making temporary tables and collecting keys 
//        collectKeys(rdsTableList);

        if (dropAllKeysBeforeRepair) {
            //Note:- Droping PK / FK Values
            dropAll_Keys();
        }

        //Note:- Adding/Altering Tables In Database
        synchronizeAllTables();

        //Note:- Setting Primary Keys/ Foreign Keys For All Tables
        addAll_PK_FK();

        addAll_UNQ();

        addAll_IDX();
    }

//    //Note:- Making temporary tables and collecting keys 
//    public void collectKeys(List<DmRefreshDataStructure_Model.RDSTable> rdsTableList) throws Exception {
//        int i;
//
//        actualStructureLog.write("Declare @TmpTable As Table(ColType NVarChar(20),OnColumn NVarChar(100), ");
//        actualStructureLog.write("OnTable NVarChar(100),WithColumn NVarChar(100),WithTable NVarChar(100)) ");
//
//        for (i = 0; i < rdsTableList.size(); i++) {
//            collectPrimaryKey(rdsTableList.get(i).getTableName(), rdsTableList.get(i).getColumnsList());
//            collectForeignKey(rdsTableList.get(i).getTableName(), rdsTableList.get(i).getForeignKeysList());
//        }
//    }
//
//    //Note:- This Function Searchs For Primary Keys In Array And Make A Temporary Query 
//    //And Return That String
//    private void collectPrimaryKey(String tableName, List<DmRefreshDataStructure_Model.RDSColumn> rdsColumnList) throws Exception {
//        int i;
//
//        if (rdsColumnList != null) {
//            for (i = 0; i < rdsColumnList.size(); i++) {
//                if (rdsColumnList.get(i).isPrimaryKey()) {
//                    actualStructureLog.write("Insert Into @TmpTable(ColType,OnColumn,OnTable) Values"
//                            + "('PK','" + rdsColumnList.get(i).getColumnName() + "','" + tableName + "'); ");
//                }
//            }
//        }
//    }
//
//    //Note:- This Function Searchs For Foreign Keys In Array And Make A Temporary Query 
//    //And Return That String
//    private void collectForeignKey(String tableName, List<DmRefreshDataStructure_Model.RDSForeignKey> rdsForeignKeyList) throws Exception {
//        int i;
//
//        if (rdsForeignKeyList != null) {
//            for (i = 0; i < rdsForeignKeyList.size(); i++) {
//                actualStructureLog.write("Insert Into @TmpTable(ColType,OnColumn,OnTable,WithColumn,WithTable) Values "
//                        + "('FK','" + rdsForeignKeyList.get(i).getOnColumn() + "','" + tableName + "', "
//                        + "'" + rdsForeignKeyList.get(i).getWithColumn() + "','" + rdsForeignKeyList.get(i).getWithTable() + "'); ");
//            }
//        }
//    }
    //Note:- Droping Constraints PK / FK Values
    public void dropAll_Keys() throws Exception {
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;

        try {
            sqlQuery = "Select SO.Name As Constraint_Name,SO1.Name As Table_Name "
                    + "From SysObjects SO "
                    + "Left Join SysObjects SO1 On SO.Parent_Obj=SO1.ID "
                    + "Where SO.XType in ('F','D','UQ') and isNull(SO1.Name,'') <> '' "
                    + "Order By SO.XType ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                dropConstraintKey(DmBaseShared.xNull(rsTemp.getString("Table_Name")), DmBaseShared.xNull(rsTemp.getString("Constraint_Name")));
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception ex) {
            addResultLog("dropAllKeys", "", "Constraint(F,D,UQ) Key", sqlQuery, ex.getMessage());
        } finally {
            if (rsTemp != null) {
                rsTemp.close();
            }
            if (psTemp != null) {
                psTemp.close();
            }
        }

        try {
            sqlQuery = "Select SO.Name As Constraint_Name,SO1.Name As Table_Name "
                    + "From SysObjects SO "
                    + "Left Join SysObjects SO1 On SO.Parent_Obj=SO1.ID "
                    + "Where SO.XType in ('PK') and isNull(SO1.Name,'') <> '' and so1.xtype <> 'TT' "
                    + "Order By SO.XType ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                dropPrimaryKey(DmBaseShared.xNull(rsTemp.getString("Table_Name")), DmBaseShared.xNull(rsTemp.getString("Constraint_Name")));
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception ex) {
            addResultLog("dropAllKeys", "", "Primary Key", sqlQuery, ex.getMessage());
        } finally {
            if (rsTemp != null) {
                rsTemp.close();
            }
            if (psTemp != null) {
                psTemp.close();
            }
        }

        try {
            sqlQuery = "Select SI.Name As Index_Name, SO1.Name As Table_Name "
                    + "From Sys.Indexes SI "
                    + "Left Join SysObjects SO1 On SI.Object_ID = SO1.ID  "
                    + "Where SI.Type Not In (0) And SO1.XType = 'U' ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                dropIndexKey(DmBaseShared.xNull(rsTemp.getString("Table_Name")), DmBaseShared.xNull(rsTemp.getString("Index_Name")));
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception ex) {
            addResultLog("dropAllKeys", "", "Index Key", sqlQuery, ex.getMessage());
        } finally {
            if (rsTemp != null) {
                rsTemp.close();
            }
            if (psTemp != null) {
                psTemp.close();
            }
        }
    }

    //Note:- This procedure drops specified constraint key
    private void dropConstraintKey(String onTableName, String constraintName) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";

        try {
            sqlQuery = "alter table [" + onTableName + "]  "
                    + "drop constraint [" + constraintName + "]; ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.executeUpdate();
            psTemp.close();
        } catch (Exception ex) {
            addResultLog("dropConstraintKey", onTableName, constraintName, sqlQuery, ex.getMessage());
        } finally {
            if (psTemp != null) {
                psTemp.close();
            }
        }

    }

    //Note:- This procedure drops specified primary key
    private void dropPrimaryKey(String onTableName, String constraintName) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";

        try {
            sqlQuery = "alter table [" + onTableName + "]  "
                    + "drop constraint [" + constraintName + "]; ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.executeUpdate();
            psTemp.close();
        } catch (Exception ex) {
            addResultLog("dropPrimaryKey", onTableName, "", sqlQuery, ex.getMessage());
        } finally {
            if (psTemp != null) {
                psTemp.close();
            }
        }
    }

    //Note:- This procedure drops specified index key
    private void dropIndexKey(String onTableName, String indexName) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";

        try {
            sqlQuery = "drop index [" + indexName + "] on [" + onTableName + "]; ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.executeUpdate();
            psTemp.close();
        } catch (Exception ex) {
            addResultLog("dropIndexKey", onTableName, "", sqlQuery, ex.getMessage());
        } finally {
            if (psTemp != null) {
                psTemp.close();
            }
        }
    }

    public void addAll_PK_FK() throws Exception {
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;

        //Note:- Setting Primary Keys For All Tables
        try {
            sqlQuery = "Select tableName From ##actualStructure Where columnType = 'PRIMARY' Group By tableName; ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                addPrimaryKey(DmBaseShared.xNull(rsTemp.getString("tableName")));
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception ex) {
            addResultLog("setAll_PK_FK", "", "Primary Key", sqlQuery, ex.getMessage());
        } finally {
            if (rsTemp != null) {
                rsTemp.close();
            }
            if (psTemp != null) {
                psTemp.close();
            }
        }

        //Note:- Setting Foreign Keys For All Tables
        try {
            sqlQuery = "Select tableName,onColumn,withColumn,withTable "
                    + "From ##actualStructure "
                    + "Where columnType = 'FOREIGN' "
                    + "Group By tableName,onColumn,withColumn,withTable; ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                addForeignKey(DmBaseShared.xNull(rsTemp.getString("tableName")), DmBaseShared.xNull(rsTemp.getString("onColumn")), DmBaseShared.xNull(rsTemp.getString("withColumn")), DmBaseShared.xNull(rsTemp.getString("withTable")));
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception ex) {
            addResultLog("setAll_PK_FK", "", "Foreign Key", sqlQuery, ex.getMessage());
        } finally {
            if (rsTemp != null) {
                rsTemp.close();
            }
            if (psTemp != null) {
                psTemp.close();
            }
        }
    }

    //Note:- This procedure add primary key to related table
    private void addPrimaryKey(String onTableName) throws Exception {
        String sqlQuery = "", pkFields = "", constraintName = "";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;

        try {
            constraintName = "PK_" + onTableName;
            sqlQuery = "Select columnName  "
                    + "From ##actualStructure "
                    + "Where columnType = 'PRIMARY' And tableName = '" + onTableName + "' "
                    + "Group By columnName "
                    + "Order By Min(rowId); ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                if (!pkFields.equalsIgnoreCase("")) {
                    pkFields = pkFields + ",";
                }
                pkFields = pkFields + DmBaseShared.xNull(rsTemp.getString("columnName")) + " asc ";
                constraintName = constraintName + "_" + DmBaseShared.xNull(rsTemp.getString("columnName"));
            }
            rsTemp.close();
            psTemp.close();

            if (!pkFields.equalsIgnoreCase("")) {
                sqlQuery = "Select SO.Name As Constraint_Name,SO1.Name As Table_Name "
                        + "From SysObjects SO "
                        + "Left Join SysObjects SO1 On SO.Parent_Obj=SO1.ID "
                        + "Where SO.XType in ('PK') and SO.Name = '" + constraintName + "' ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                rsTemp = psTemp.executeQuery();
                if (!rsTemp.next()) {
                    rsTemp.close();
                    psTemp.close();

                    sqlQuery = "alter table " + onTableName + " add constraint [" + constraintName + "] "
                            + "primary key clustered (" + pkFields + "); ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.executeUpdate();
                }
                psTemp.close();
            }
        } catch (Exception ex) {
            addResultLog("addPrimaryKey", onTableName, pkFields, sqlQuery, ex.getMessage());
        } finally {
            if (rsTemp != null) {
                rsTemp.close();
            }
            if (psTemp != null) {
                psTemp.close();
            }
        }

    }

    //Note:- This procedure add foreign key to related table
    private void addForeignKey(String onTableName, String onColumnName, String withColumnName, String withTableName) throws Exception {
        String sqlQuery = "", constraintName = "";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        try {
            constraintName = "FK_" + onTableName + "_" + withTableName + "_" + onColumnName.replace(",", "_") + "";
            sqlQuery = "Select SO.Name As Constraint_Name,SO1.Name As Table_Name "
                    + "From SysObjects SO "
                    + "Left Join SysObjects SO1 On SO.Parent_Obj=SO1.ID "
                    + "Where SO.XType in ('F') and SO.Name = '" + constraintName + "' ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (!rsTemp.next()) {
                rsTemp.close();
                psTemp.close();
                sqlQuery = "alter table " + onTableName + " add constraint "
                        + "[" + constraintName + "] "
                        + "foreign key ([" + onColumnName.replace(",", "],[") + "]) "
                        + "references [" + withTableName + "] ([" + withColumnName.replace(",", "],[") + "]); ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.executeUpdate();
            }
            psTemp.close();
        } catch (Exception ex) {
            addResultLog("addForeignKey", onTableName, onColumnName, sqlQuery, ex.getMessage());
        } finally {
            if (rsTemp != null) {
                rsTemp.close();
            }
            if (psTemp != null) {
                psTemp.close();
            }
        }
    }

    public void addAll_UNQ() throws Exception {
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;

        try {
//            sqlQuery = "Select tableName,onColumn,columnType "
//                    + "From actualStructure "
//                    + "Where columnType In ('UNIQUE','INDEX') "
//                    + "Group By tableName,onColumn; ";
            sqlQuery = "Select tableName,onColumn " //,max(columnType) as columnType
                    + "From ##actualStructure "
                    + "Where columnType In ('UNIQUE') " //,'INDEX'
                    + "Group By tableName,onColumn; ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                addUNQ(DmBaseShared.xNull(rsTemp.getString("tableName")), DmBaseShared.xNull(rsTemp.getString("onColumn")));
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception ex) {
            addResultLog("addAll_UNQ", "", "UNIQUE", sqlQuery, ex.getMessage());
        } finally {
            if (rsTemp != null) {
                rsTemp.close();
            }
            if (psTemp != null) {
                psTemp.close();
            }
        }
    }

    private void addUNQ(String onTableName, String onColumnName) throws Exception {
        String sqlQuery = "", constraintName = "";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;

        try {

            constraintName = "UNQ_" + onTableName + "_" + onColumnName.replace(",", "_");

//            sqlQuery = "select s.INDEX_NAME "
//                    + "from INFORMATION_SCHEMA.STATISTICS s "
//                    + "where s.TABLE_SCHEMA = '" + databaseName + "' "
//                    + "and s.Table_Name ='" + onTableName + "' "
//                    + "and s.INDEX_NAME = '" + indexName + "' ;";
            sqlQuery = "Select SO.Name As Constraint_Name "
                    + "From SysObjects SO "
                    + "Left Join SysObjects SO1 On SO.Parent_Obj=SO1.ID "
                    + "Where SO.XType in ('UQ') and "
                    + "SO1.Name = '" + onTableName + "' and "
                    + "SO.Name = '" + constraintName + "' ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (!rsTemp.next()) {
                rsTemp.close();
                psTemp.close();

                sqlQuery = "alter table " + onTableName + " add constraint [" + constraintName + "] unique "
                        + "([" + onColumnName.replace(",", "],[") + "]) ";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.executeUpdate();
                psTemp.close();
            }

        } catch (Exception ex) {
            addResultLog("addUNQ", onTableName, onColumnName, sqlQuery, ex.getMessage());
        } finally {
            if (rsTemp != null) {
                rsTemp.close();
            }

            if (psTemp != null) {
                psTemp.close();
            }
        }
    }

    public void addAll_IDX() throws Exception {
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;

        try {
            sqlQuery = "Select tableName,onColumn "
                    + "From ##actualStructure "
                    + "Where columnType In ('INDEX') "
                    + "Group By tableName,onColumn; ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                addIDX(DmBaseShared.xNull(rsTemp.getString("tableName")), DmBaseShared.xNull(rsTemp.getString("onColumn")));
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception ex) {
            addResultLog("addAll_IDX", "", "INDEX", sqlQuery, ex.getMessage());
        } finally {
            if (rsTemp != null) {
                rsTemp.close();
            }
            if (psTemp != null) {
                psTemp.close();
            }
        }
    }

    private void addIDX(String onTableName, String onColumnName) throws Exception {
        String sqlQuery = "", indexName = "";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;

        try {

            indexName = "IDX_" + onTableName + "_" + onColumnName.replace(",", "_");

            sqlQuery = "Select SI.Name As Index_Name "
                    + "From Sys.Indexes SI "
                    + "Left Join SysObjects SO1 On SI.Object_ID = SO1.ID  "
                    + "Where SI.Name = '" + indexName + "' and "
                    + "SO1.Name = '" + onTableName + "' ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (!rsTemp.next()) {
                rsTemp.close();
                psTemp.close();

                sqlQuery = "create nonClustered index [" + indexName + "] on " + onTableName + " "
                        + "([" + onColumnName.replace(",", "],[") + "]) ";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.executeUpdate();
                psTemp.close();
            }

        } catch (Exception ex) {
            addResultLog("addIDX", onTableName, onColumnName, sqlQuery, ex.getMessage());
        } finally {
            if (rsTemp != null) {
                rsTemp.close();
            }

            if (psTemp != null) {
                psTemp.close();
            }
        }
    }

    //Note:- Adding/Altering Tables In Database
    public void synchronizeAllTables() throws Exception {
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;

        try {
            sqlQuery = "Select tableName "
                    + "From ##actualStructure "
                    + "Group By tableName; ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                createTable(DmBaseShared.xNull(rsTemp.getString("tableName")));
                createAllColumn(DmBaseShared.xNull(rsTemp.getString("tableName")));
                dropColumn(DmBaseShared.xNull(rsTemp.getString("tableName")), "__Temp__");
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception ex) {
            addResultLog("synchronizeAllTables", "", "", sqlQuery, ex.getMessage());
        } finally {
            if (rsTemp != null) {
                rsTemp.close();
            }
            if (psTemp != null) {
                psTemp.close();
            }
        }
    }

    //Note:- This procedure create a table with temporary field __Temp__
    private void createTable(String tableName) throws Exception {
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;

        try {
            //Note:- Checking That Table Exist Or Not
//            sqlQuery = "SELECT  count(*) as cnt "
//                    + "FROM information_schema.TABLES T "
//                    + "Where T.Table_Schema = '" + databaseName + "'  "
//                    + "And T.Table_Type = 'BASE TABLE'  "
//                    + "And T.Table_Name = '" + tableName + "' ;";
            sqlQuery = "Select Count(*) as cnt from SysObjects Where Name = '" + tableName + "'; ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getInt("cnt") > 0) {
                    //sqlResult = "Table already exist.";
                } else {
                    rsTemp.close();
                    psTemp.close();
//                    sqlQuery = "create table " + tableName + " (__Temp__ varChar(1)) "
//                            + "ENGINE=InnoDB DEFAULT CHARSET=utf8;";
                    sqlQuery = "create table " + tableName + " (__Temp__ varChar(1)) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.executeUpdate();
                }
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception ex) {
            addResultLog("createTable", tableName, "", sqlQuery, ex.getMessage());
        } finally {
            if (rsTemp != null) {
                rsTemp.close();
            }
            if (psTemp != null) {
                psTemp.close();
            }
        }
    }

    //Note:- Drops Related Column
    private void dropColumn(String tableName, String columnName) throws Exception {
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;

        try {
            //Note:- Checking that column exists in table or not
//            sqlQuery = "SELECT  count(*) as cnt "
//                    + "FROM information_schema.COLUMNS C "
//                    + "Where C.Table_Schema = '" + databaseName + "' "
//                    + "And C.Table_Name In ('" + tableName + "') "
//                    + "And C.Column_Name in ('" + columnName + "');";

            sqlQuery = "select  count(*) as cnt "
                    + "from sysColumns sc "
                    + "left join sysObjects so on sc.id = so.id "
                    + "where so.name = '" + tableName + "' and sc.name = '" + columnName + "' ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getInt("cnt") > 0) {
                    rsTemp.close();
                    psTemp.close();
                    sqlQuery = "alter table " + tableName + " drop column [" + columnName + "];";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.executeUpdate();
                } else {
                    //sqlResult = "Column does not exist.";
                }
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception ex) {
            addResultLog("dropColumn", tableName, columnName, sqlQuery, ex.getMessage());
        } finally {
            if (rsTemp != null) {
                rsTemp.close();
            }
            if (psTemp != null) {
                psTemp.close();
            }
        }
    }

    //Note:- This Function Add/Alter Column And Set Default Value
    private void createAllColumn(String tableName) throws Exception {
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        ColumnProperty columnProperty;

        try {
//            sqlQuery = "drop table if exists actualStructure_clone;";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.executeUpdate();
//            psTemp.close();
//
//            sqlQuery = "create temporary table actualStructure_clone as select max(rowId) as rowId "
//                    + "from actualStructure "
//                    + "where tableName = '" + tableName + "' and isNull(columnName,'') <> '' "
//                    + "group by columnName;";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.executeUpdate();
//            psTemp.close();

//            sqlQuery = "select c.Column_Name as sysColumnName,tmp.* "
//                    + "from "
//                    + "(Select '" + databaseName + "' as Table_Schema,actualStructure.* "
//                    + "From actualStructure "
//                    + "where rowId In ( "
//                    + "select rowId "
//                    + "from actualStructure_clone  "
//                    + "where tableName = '" + tableName + "' and isNull(columnName,'') <> '' "
//                    + ")) as tmp "
//                    + "left join information_schema.COLUMNS C on "
//                    + "tmp.Table_Schema = c.Table_Schema "
//                    + "and tmp.tableName = c.Table_Name "
//                    + "and tmp.columnName = c.Column_Name "
//                    + "order by rowId;";
            sqlQuery = "select sc.Name as sysColumnName,tmp.*  "
                    + "from "
                    + "( "
                    + "Select a.* "
                    + "From ##actualStructure a "
                    + "where a.rowId In "
                    + "( "
                    + "select max(aa.rowId) "
                    + "from ##actualStructure aa "
                    + "where aa.tableName = '" + tableName + "' and isNull(columnName,'') <> '' "
                    + "group by aa.columnName "
                    + ") "
                    + ") as tmp "
                    + "left join sysObjects so on so.name = tmp.tableName and so.xtype = 'U' "
                    + "left join sysColumns sc on sc.id = so.id and sc.name = tmp.columnName "
                    + "where so.name = '" + tableName + "' "
                    + "order by rowId; ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                columnProperty = new ColumnProperty();
                columnProperty.setColumnName(DmBaseShared.xNull(rsTemp.getString("columnName")));
                columnProperty.setSysColumnName(DmBaseShared.xNull(rsTemp.getString("sysColumnName")));
                columnProperty.setPrecisionLength(DmBaseShared.xNull(rsTemp.getString("precisionLength")));
                columnProperty.setColumnType(DmBaseShared.xNull(rsTemp.getString("columnType")));
                columnProperty.setIsNullable(DmBaseShared.xNull(rsTemp.getString("isNullable")));
                columnProperty.setDefaultValue(DmBaseShared.xNull(rsTemp.getString("defaultValue")));
                columnProperty.setDataType(DmBaseShared.xNull(rsTemp.getString("dataType")));

                addColumn(tableName, columnProperty);
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception ex) {
            addResultLog("createAllColumn", tableName, "", sqlQuery, ex.getMessage());
        } finally {
            if (rsTemp != null) {
                rsTemp.close();
            }
            if (psTemp != null) {
                psTemp.close();
            }
        }
    }

    public static class ColumnProperty implements Serializable, IsSerializable {

        private String columnName, sysColumnName, precisionLength, columnType, isNullable, defaultValue, dataType;

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public String getSysColumnName() {
            return sysColumnName;
        }

        public void setSysColumnName(String sysColumnName) {
            this.sysColumnName = sysColumnName;
        }

        public String getPrecisionLength() {
            return precisionLength;
        }

        public void setPrecisionLength(String precisionLength) {
            this.precisionLength = precisionLength;
        }

        public String getColumnType() {
            return columnType;
        }

        public void setColumnType(String columnType) {
            this.columnType = columnType;
        }

        public String getIsNullable() {
            return isNullable;
        }

        public void setIsNullable(String isNullable) {
            this.isNullable = isNullable;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        public void setDefaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
        }

        public String getDataType() {
            return dataType;
        }

        public void setDataType(String dataType) {
            this.dataType = dataType;
        }
    }

    //Note:- This Function Add/Alter Column And Set Default Value
    private void addColumn(String tableName, ColumnProperty columnProperty) throws Exception {
        String actionToDo = " add column ", additionalChecks = "";
//        , columnLength = ""
        String sqlQuery = "", constraintName = "";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        try {
            //Note:- Checking Wheather To Add Or Alter Column

//            DTTemp = FGetDatTable("Select Count(*),Max(SC.Name) As ColName,Max(ST.Name) As DType,Max(SC.Length) As Length " & _
//               "From SysColumns SC Left Join SysTypes ST On SC.XType=ST.XUserType " & _
//               "Where SC.Name='" & ModuleTableCol.StrName & "' And " & _
//               "SC.ID In (Select ID from SysObjects Where Name='" & StrTableName & "')", GCnVar)
//            If Not DTTemp.Rows(0).Item(0) > 0 Then
//                StrToDo = " Add "
//            Else
//                StrToDo = " Alter Column "
//                FValidateColumnDif(LIExpRtn, XNull(DTTemp.Rows(0).Item("DType")), ModuleTableCol.StrDataType, _
//                                    VNull(DTTemp.Rows(0).Item("Length")), ModuleTableCol.IntLength)
//            End If
            if (DmBaseShared.xNull(columnProperty.getSysColumnName()).trim().equalsIgnoreCase("")) {
                actionToDo = " add ";
            } else {
                // actionToDo = " modify column ";
                actionToDo = " alter column ";
//                        + " [" + DmBaseShared.xNull(columnProperty.getColumnName()).trim() + "] ";
//                sqlQuery = "alter table " + tableName + " alter " + DmBaseShared.xNull(rsColumn.getString("columnName")) + " drop default;";
//                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//                psTemp.executeUpdate();
//                psTemp.close();
            }

            //Note:- Checking Situations
//            columnLength = "";
//            if (!DmBaseShared.xNull(columnProperty.getPrecisionLength()).trim().equalsIgnoreCase("")) {
//                columnLength = "(" + DmBaseShared.xNull(columnProperty.getPrecisionLength()).trim() + ")";
//            }
            additionalChecks = "";
            if (DmBaseShared.xNull(columnProperty.getColumnType()).trim().equalsIgnoreCase(SQLColumnType.PRIMARY.toString())) {
                additionalChecks = additionalChecks + " not null ";
            } else {
                if (DmBaseShared.xNull(columnProperty.getIsNullable()).trim().equalsIgnoreCase(SQLIsNullable.NOT_NULL.toString())) {
                    additionalChecks = additionalChecks + " not null ";
                }

//                if (!DmBaseShared.xNull(columnProperty.getDefaultValue()).trim().equalsIgnoreCase("")) {
//                    additionalChecks = additionalChecks + " default '" + DmBaseShared.xNull(columnProperty.getDefaultValue()).trim() + "' ";
//                }
            }

            //Note:- Adding/Altering Columns
//            sqlQuery = "alter table " + tableName + " " + actionToDo + " `"
//                    + DmBaseShared.xNull(columnProperty.getColumnName()).trim() + "` "
//                    + DmBaseShared.xNull(columnProperty.getDataType()).toUpperCase().replace("_UNSIGNED", "") + " "
//                    + columnLength + " " + getUnsigned(DmBaseShared.xNull(columnProperty.getDataType())) + " " + additionalChecks + ";";
//            sqlQuery = "alter table " + tableName + " " + actionToDo + " ["
//                    + DmBaseShared.xNull(columnProperty.getColumnName()).trim() + "] "
//                    + DmBaseShared.xNull(columnProperty.getDataType()) + " "
//                    + columnLength + " " + additionalChecks + ";";
            sqlQuery = "alter table " + tableName + " " + actionToDo + " ["
                    + DmBaseShared.xNull(columnProperty.getColumnName()).trim() + "] "
                    + getDataType(DmBaseShared.xNull(columnProperty.getDataType()), DmBaseShared.xNull(columnProperty.getPrecisionLength()).trim()) + " "
                    + additionalChecks + ";";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.executeUpdate();
            psTemp.close();

            if (!DmBaseShared.xNull(columnProperty.getDefaultValue()).trim().equalsIgnoreCase("")) {

                constraintName = "DF_" + tableName + "_" + DmBaseShared.xNull(columnProperty.getColumnName()).trim() + " ";
                sqlQuery = "Select SO.Name As Constraint_Name,SO1.Name As Table_Name "
                        + "From SysObjects SO "
                        + "Left Join SysObjects SO1 On SO.Parent_Obj=SO1.ID "
                        + "Where SO.XType in ('D') and SO.Name = '" + constraintName + "' ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                rsTemp = psTemp.executeQuery();
                if (!rsTemp.next()) {
                    rsTemp.close();
                    psTemp.close();

                    sqlQuery = "alter table " + tableName + " "
                            + "add constraint " + constraintName + " "
                            + "default '" + DmBaseShared.xNull(columnProperty.getDefaultValue()).trim() + "' "
                            + "for " + DmBaseShared.xNull(columnProperty.getColumnName()).trim() + " ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.executeUpdate();
                }
                psTemp.close();
            }
        } catch (Exception ex) {
            addResultLog("addColumn", tableName, DmBaseShared.xNull(columnProperty.getColumnName()), sqlQuery, ex.getMessage());
        } finally {
            if (rsTemp != null) {
                rsTemp.close();
            }
            if (psTemp != null) {
                psTemp.close();
            }
        }
    }

    private String getDataType(String dataType, String length) {
        String returnDataType = "";

//        if (dataType.trim().equalsIgnoreCase(SQLDataType.BIGINT.toString())) {
//            returnDataType = dataType;
//        } else if (dataType.trim().equalsIgnoreCase(SQLDataType.DECIMAL.toString())) {
//            returnDataType = dataType;
//        } else if (dataType.trim().equalsIgnoreCase(SQLDataType.FLOAT.toString())) {
//            returnDataType = dataType;
//        } else if (dataType.trim().equalsIgnoreCase(SQLDataType.INT.toString())) {
//            returnDataType = dataType;
//        } else if (dataType.trim().equalsIgnoreCase(SQLDataType.SMALLINT.toString())) {
//            returnDataType = dataType;
//        } else if (dataType.trim().equalsIgnoreCase(SQLDataType.TINYINT.toString())) {
//            returnDataType = dataType;
//        } else if (dataType.trim().equalsIgnoreCase(SQLDataType.DATE.toString())) {
//            returnDataType = dataType;
//        } else if (dataType.trim().equalsIgnoreCase(SQLDataType.DATETIME.toString())) {
//            returnDataType = dataType;
//        } else if (dataType.trim().equalsIgnoreCase(SQLDataType.VARCHAR.toString())) {
//            returnDataType = dataType + "(" + length + ")";
//        } else if (dataType.trim().equalsIgnoreCase(SQLDataType.VARCHARMAX.toString())) {
//            returnDataType = "VARCHAR(MAX)";
//        } else {
//            
//        }
        if (dataType.trim().equalsIgnoreCase(SQLDataType.VARCHAR.toString())) {
            returnDataType = dataType + "(" + length + ")";
        } else if (dataType.trim().equalsIgnoreCase(SQLDataType.NVARCHAR.toString())) {
            returnDataType = dataType + "(" + length + ")";
        } else if (dataType.trim().equalsIgnoreCase(SQLDataType.NVARCHARMAX.toString())) {
            returnDataType = "NVARCHAR(MAX)";
        } else if (dataType.trim().equalsIgnoreCase(SQLDataType.VARCHARMAX.toString())) {
            returnDataType = "VARCHAR(MAX)";
        } else {
            returnDataType = dataType;
            if (!DmBaseShared.xNull(length).trim().equalsIgnoreCase("")) {
                returnDataType += "(" + length + ")";
            }
        }

        return returnDataType;
    }

    public void addResultLog(String procedureName, String tableName, String otherInfo, String sqlStatement, String sqlResult) throws Exception {
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        Connection cnResult = null;

        try {
            cnResult = callBack.getConnection("master");
//            cnResult = DmBaseServer.getConnection(this.boneCP, "master");
            resultLog = new FileWriter(resultLogFileName, true);
            sqlQuery = "insert into ##result(procedureName,tableName,otherInfo,sqlStatement,sqlResult) values"
                    + "('" + DmBaseShared.xNull(procedureName) + "','" + DmBaseShared.xNull(tableName) + "',"
                    + ":otherInfo,:sqlStatement,:sqlResult);";
            psTemp = DmPreparedStatement.prepareStatement(cnResult, sqlQuery);
            psTemp.setString("otherInfo", DmBaseShared.xNull(otherInfo));
            psTemp.setString("sqlStatement", DmBaseShared.xNull(sqlStatement));
            psTemp.setString("sqlResult", DmBaseShared.xNull(sqlResult));
            resultLog.write(psTemp.getQuery() + "\n");
            resultLog.close();
            resultLog = null;
            psTemp.close();
            cnResult.close();
        } catch (Exception ex) {
            throw new DmException(0, ex.getMessage());
        } finally {
            if (psTemp != null) {
                psTemp.close();
            }
            if (cnResult != null) {
                cnResult.close();
            }
            if (resultLog != null) {
                resultLog.close();
                resultLog = null;
            }
        }
    }

    void addActualStructureLog(String classPath, String tableName, String columnName,
            SQLDataType dataType, String precisionLength, SQLColumnType columnType,
            SQLIsNullable isNullable, String defaultValue,
            String onColumn, String withColumn, String withTable, String columnTypeOther) throws Exception {
        String sqlQuery;
        DmPreparedStatement psTemp = null;

        try {
            actualStructureLog = new FileWriter(actualStructureFileName, true);
            sqlQuery = "insert into ##actualStructure(classPath,tableName,columnName,dataType,"
                    + "precisionLength,columnType,isNullable,defaultValue,onColumn,withColumn,withTable) values"
                    + "('" + DmBaseShared.xNull(classPath) + "','" + DmBaseShared.xNull(tableName) + "',"
                    + "'" + DmBaseShared.xNull(columnName) + "','" + (dataType == null ? "" : dataType.toString()) + "',"
                    + "'" + DmBaseShared.xNull(precisionLength) + "',"
                    + "'" + (DmBaseShared.xNull(withTable).trim().equalsIgnoreCase("") ? (columnType == null ? columnTypeOther : columnType.toString()) : columnTypeOther) + "',"
                    + "'" + (isNullable == null ? "" : isNullable.toString()) + "','" + DmBaseShared.xNull(defaultValue) + "',"
                    + "'" + DmBaseShared.xNull(onColumn) + "','" + DmBaseShared.xNull(withColumn) + "',"
                    + "'" + DmBaseShared.xNull(withTable) + "');";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.executeUpdate();
            psTemp.close();
//            cnTemp.commit();
            actualStructureLog.write(sqlQuery + "\n");
            actualStructureLog.close();
            actualStructureLog = null;
        } catch (Exception ex) {
            throw new DmException(0, ex.getMessage());
        } finally {
            if (psTemp != null) {
                psTemp.close();
            }
            if (actualStructureLog != null) {
                actualStructureLog.close();
                actualStructureLog = null;
            }
        }
    }
}
