package dataman.webJobs.self.server;

import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.dmRefreshDataStructure.server.DmConfigDataStructure;
import dataman.dmBase.dmRefreshDataStructure.server.DmRefreshDataStructure;

public class WebJobsDataStructure {

    DmRefreshDataStructure refreshDataStructure;

    public WebJobsDataStructure(DmRefreshDataStructure refreshDataStructure) throws Exception {
        this.refreshDataStructure = refreshDataStructure;

        enviro("enviro");
        wjTemplateGroupMast("wjTemplateGroupMast");
        wjTemplateMast("wjTemplateMast");
        wjTemplateAttachment("wjTemplateAttachment");
        wjTemplateFileDetail("wjTemplateFileDetail");
        wjTemplateStaffInfo("wjTemplateStaffInfo");
        notificationEntry("notificationEntry");
    }

    private void wjTemplateFileDetail(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("templateCode", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("V_SNo", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("fileCode", SQLDataType.VARCHAR, "36", null, null, null);
        configDataStructure.setColumnValue("ruleCode", SQLDataType.VARCHAR, "20", null, null, null);

        configDataStructure.setForeignKey("templateCode", "code", "wjTemplateMast");
    }

    private void enviro(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("wj_SmtpServer", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("wj_SmtpPort", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("wj_SmtpSsl", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "1");
        configDataStructure.setColumnValue("wj_Url", SQLDataType.VARCHAR, "250", null, null, null);
        configDataStructure.setColumnValue("wj_AcName", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("wj_Password", SQLDataType.VARCHAR, "25", null, null, null);
        configDataStructure.setColumnValue("wj_Format", SQLDataType.VARCHAR, "250", null, null, null);
        configDataStructure.setColumnValue("wj_MsgLength", SQLDataType.INT, null, null, null, null);
    }

    private void wjTemplateGroupMast(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);

    }

    private void wjTemplateMast(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("TemplateGroupCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("templateType", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("dataSource", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("functionName", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("subject", SQLDataType.NVARCHAR, "500", null, null, null);
        configDataStructure.setColumnValue("message", SQLDataType.NVARCHARMAX, null, null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("apiTemplateId", SQLDataType.VARCHAR, "30", null, null, null);

        configDataStructure.setForeignKey("TemplateGroupCode", "code", "wjTemplateGroupMast");
    }

    private void wjTemplateAttachment(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("templateCode", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("v_SNo", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("procedureName", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("rptName", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("fileName", SQLDataType.VARCHAR, "50", null, null, null);

        configDataStructure.setForeignKey("templateCode", "code", "wjTemplateMast");
    }

    private void wjTemplateStaffInfo(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("templateCode", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("subCode", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);

        configDataStructure.setForeignKey("templateCode", "code", "wjTemplateMast");
        configDataStructure.setForeignKey("subCode", "subCode", "subGroup");
    }

    private void notificationEntry(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("siteCode", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("notification", SQLDataType.VARCHAR, "500", null, null, null);
        configDataStructure.setColumnValue("entryDate", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("expiryDate", SQLDataType.DATE, null, null, null, null);
    }

    public void initilizer() throws Exception {
//        Connection cnTemp = null;
//        DmPreparedStatement psTemp = null;
//        ResultSet rsTemp = null;
//        String sqlQuery = "", codeValue = "";
//
//        try {
//            cnTemp = DmServer.getConnection(refreshDataStructure.getDatabaseName());
//
//            
//        } catch (Exception ex) {
//            refreshDataStructure.addResultLog("initilizer", "", this.getClass().getName(), sqlQuery, ex.getMessage());
//            throw new DmException(0, ex.getMessage());
//        } finally {
//            if (rsTemp != null) {
//                rsTemp.close();
//            }
//            if (psTemp != null) {
//                psTemp.close();
//            }
//            if (cnTemp != null && !cnTemp.isClosed()) {
//                cnTemp.close();
//            }
//        }
    }
}
