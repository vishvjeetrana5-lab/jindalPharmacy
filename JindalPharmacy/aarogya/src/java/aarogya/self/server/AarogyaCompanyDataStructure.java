package aarogya.self.server;

import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.dmRefreshDataStructure.server.DmConfigDataStructure;
import dataman.dmBase.dmRefreshDataStructure.server.DmRefreshDataStructure;

public class AarogyaCompanyDataStructure {

    private DmRefreshDataStructure refreshDataStructure;

    public AarogyaCompanyDataStructure(DmRefreshDataStructure refreshDataStructure) throws Exception {
        this.refreshDataStructure = refreshDataStructure;
        userLinkAccount("userLinkAccount");
        machineInterfaceConfig("machineInterfaceConfig");
        machineSubTestMapping("machineSubTestMapping");
        machineSubTestOutput("machineSubTestOutput");
    }

    private void userLinkAccount(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("departmentCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("nursingStationCodeList", SQLDataType.VARCHARMAX, null, null, null, null);
        configDataStructure.setColumnValue("allowedDepartmentCodeList", SQLDataType.VARCHARMAX, null, null, null, null);
        configDataStructure.setColumnValue("allowedDepartmentCodeList", SQLDataType.VARCHARMAX, null, null, null, null);
        configDataStructure.setColumnValue("allowedDiscountPercentUpto", SQLDataType.FLOAT, null, null, null, null);
    }

    private void machineInterfaceConfig(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("machineName", SQLDataType.VARCHAR, "50", SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("machineCode", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("ipAddress", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("portNo", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("comPort", SQLDataType.VARCHAR, "5", null, null, null);
        configDataStructure.setColumnValue("baudRate", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("stopBit", SQLDataType.VARCHAR, "5", null, null, null);
        configDataStructure.setColumnValue("dataBit", SQLDataType.VARCHAR, "5", null, null, null);
        configDataStructure.setColumnValue("parity", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("hardwareControl", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);

    }

    private void machineSubTestMapping(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("machineName", SQLDataType.VARCHAR, "50", SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("machineSubTestName", SQLDataType.VARCHAR, "50", SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("ourSubTestName", SQLDataType.VARCHAR, "50", SQLColumnType.PRIMARY, null, null);
    }

    private void machineSubTestOutput(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("machineName", SQLDataType.VARCHAR, "50", null, null, null);
//        configDataStructure.setColumnValue("labRecId", SQLDataType.VARCHAR, "20", SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("sampleRecId", SQLDataType.VARCHAR, "20", SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("machineSubTestName", SQLDataType.VARCHAR, "50", SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("machineSubTestValue", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("machineSubTestUnit", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("localPC_IP", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("remotePC_IP", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("localPCName", SQLDataType.VARCHAR, "50", null, null, null);
    }
}
