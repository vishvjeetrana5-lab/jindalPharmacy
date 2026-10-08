package dataman.dmBase.dmScheduler.server;

import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmScheduler.shared.SiteEnviro_Mdl;
import dataman.dmBase.dmScheduler.shared.TemplateMast_Mdl;
import dataman.dmBase.dmScheduler.shared.WebJobAttachment_Mdl;
import dataman.dmBase.self.server.DmCsvReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DmAnonymousWebJobTemplateEngine implements Runnable {

    private TemplateMast_Mdl tempModel;
    private DmWebJobAttachments webJobAttachments;
    private List<DmDynamicDataSourceRow> lstWebJobs;
    private List<WebJobAttachment_Mdl> lstAttachments;
    private Map<String, String> extraRowParams;
    private SiteEnviro_Mdl siteEnviro;
    private String activeDbName;
    private Map<String, DmMessageParam> lstMsgParams = new HashMap<>();
    private List<String> uniqueIds = new ArrayList<>();

    public DmAnonymousWebJobTemplateEngine(TemplateMast_Mdl tModel) {
        this.lstWebJobs = new ArrayList<>();
        this.lstAttachments = new ArrayList<>();
        this.extraRowParams = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        this.webJobAttachments = null;
        this.tempModel = tModel;

//        this.manualAttachments = new ArrayList<>();
//        this.executors = Executors.newCachedThreadPool();
    }

    public List<String> getUniqueIDs() {
        return this.uniqueIds;
    }

    public DmWebJobAttachments getWebJobAttachments() {
        return webJobAttachments;
    }

    public void setWebJobAttachments(DmWebJobAttachments webJobAttachments) {
        this.webJobAttachments = webJobAttachments;
    }

    public void setActiveDbName(String dbName) {
        this.activeDbName = dbName.trim();
    }

    public String getActiveDbName() {
        return this.activeDbName;
    }

    public TemplateMast_Mdl getTemplateModel() {
        return this.tempModel;
    }

    public List<DmDynamicDataSourceRow> getDataRows() {
        return this.lstWebJobs;
    }

    public void setEnviroSettings(SiteEnviro_Mdl siteEnviro) {
        this.siteEnviro = siteEnviro;
    }

    public SiteEnviro_Mdl getEnviroSettings() {
        return this.siteEnviro;
    }

    public List<WebJobAttachment_Mdl> getFileAttachments() {
        return this.lstAttachments;
    }

    private void fillDataSource() {
        Connection cnTemp = null;

        try {
            this.lstWebJobs.clear();
            cnTemp = DmBaseServer.getConnection(activeDbName);

            fillAttachmentDetails(cnTemp);
            if (this.tempModel.getWebJobDataSource() == DmEnum.DmWebJobDataSources.FUNCTION) {
                fillDataAndParametersFromDB(siteEnviro, cnTemp);
            } else if (this.tempModel.getWebJobDataSource() == DmEnum.DmWebJobDataSources.CSV) {
                fillDataAndParametersFromFile(siteEnviro);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                    cnTemp = null;
                }
            } catch (Exception ex) {
            }
        }
    }

    private void fillAttachmentDetails(Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;

        try {
            if (this.tempModel.getWebJobType() == DmEnum.DmWebJobTypes.EMAIL) {
                String sql = "SELECT * FROM DBO.wjTemplateAttachment WHERE (TEMPLATECODE=:templateId) ORDER BY V_SNO";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sql);
                psTemp.setString("templateId", String.valueOf(this.tempModel.getTemplateId()));
                rsTemp = psTemp.executeQuery();
                while (rsTemp.next()) {
                    WebJobAttachment_Mdl attachment = new WebJobAttachment_Mdl();
                    attachment.setTemplateId(this.tempModel.getTemplateId());
                    attachment.setProcName(DmBaseShared.xNull(rsTemp.getString("procedureName")));
                    attachment.setRptName(DmBaseShared.xNull(rsTemp.getString("rptName")));
                    attachment.setActFileName(DmBaseShared.xNull(rsTemp.getString("fileName")));
                    this.lstAttachments.add(attachment);
                }
                rsTemp.close();
                psTemp.close();
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (rsTemp != null) {
                rsTemp.close();
                rsTemp = null;
            }
            if (psTemp != null) {
                psTemp.close();
                psTemp = null;
            }
        }
    }

    private void fillDataAndParametersFromDB(SiteEnviro_Mdl siteEnviro, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;

        try {
            Map<String, String> params = new HashMap<>();
            String wSql = "";
            if (this.uniqueIds.size() > 0) {
                wSql = "AND (UNIQUEID IN (";
                for (int i = 0; i < this.uniqueIds.size(); i++) {
                    String param = "uniqueid" + String.valueOf(i);
                    params.put(param, this.uniqueIds.get(i).trim());
                    wSql += ":" + param + ",";
                }
                if (wSql.endsWith(",")) {
                    wSql = wSql.substring(0, wSql.length() - 1);
                }
                wSql += "))";
            }

            String sql = "SELECT * FROM DBO." + this.tempModel.getFileOrFuncName() + "(:templateId,:siteCode,:uniqueKey) WHERE (1=1) " + wSql;
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sql);
            psTemp.setString("templateId", DmBaseShared.iNull(this.tempModel.getTemplateId()).toString());
            psTemp.setString("siteCode", DmBaseShared.iNull(siteEnviro.getSiteCode()).toString());
            psTemp.setString("uniqueKey", DmBaseShared.xNull(uniqueIds.get(0)));
            for (Map.Entry<String, String> entry : params.entrySet()) {
                psTemp.setString(entry.getKey(), entry.getValue());
            }
            rsTemp = psTemp.executeQuery();
            ResultSetMetaData colMetaData = rsTemp.getMetaData();
            for (int i = 1; i <= colMetaData.getColumnCount(); i++) {
                String colName = colMetaData.getColumnName(i);
                DmMessageParam param = new DmMessageParam(colName);
                if (!this.lstMsgParams.containsKey(param.getColumnName())) {
                    this.lstMsgParams.put(param.getColumnName().toLowerCase(), param);
                }
            }

            while (rsTemp.next()) {
                DmDynamicDataSourceRow wjRow = new DmDynamicDataSourceRow();
                Iterator<String> keys = this.lstMsgParams.keySet().iterator();
                while (keys.hasNext()) {
                    String key = keys.next();
                    wjRow.getCells().put(key, DmBaseShared.xNull(rsTemp.getString(key)));
                }
                this.addWebJobRow(wjRow);
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                    rsTemp = null;
                }
                if (psTemp != null) {
                    psTemp.close();
                    psTemp = null;
                }
            } catch (Exception ex) {
            }
        }
    }

    private String getMessageFromUnformatted(DmDynamicDataSourceRow row, String messageToFormat) {
        String retValue = "";
        Map<String, String> params = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        for (Map.Entry<String, String> cells : row.getCells().entrySet()) {
            params.put(cells.getKey().toLowerCase(), cells.getValue());
        }
        retValue = DmMapFormat.format(messageToFormat, params);
        return retValue;
    }

    private void fillDataAndParametersFromFile(SiteEnviro_Mdl siteEnviro) throws Exception {
        String readDirectoryPath = "";
        if (this.tempModel.getWebJobType() == DmEnum.DmWebJobTypes.SMS) {
            readDirectoryPath = DmBaseServer.Path.csvTemplates_SMS_eMail;
        } else if (this.tempModel.getWebJobType() == DmEnum.DmWebJobTypes.EMAIL) {
            readDirectoryPath = DmBaseServer.Path.csvTemplates_SMS_eMail;
        }
        String fullFilePath = readDirectoryPath + "/" + String.valueOf(this.tempModel.getTemplateId()) + ".csv";

        DmCsvReader reader = new DmCsvReader(fullFilePath);
        reader.readLines();
        for (int i = 0; i < reader.getHeaders().size(); i++) {
            String key = reader.getHeaders().get(i);
            if (!this.lstMsgParams.containsKey(key)) {
                DmMessageParam param = new DmMessageParam(key);
                this.lstMsgParams.put(key, param);
            }
        }

        for (int i = 0; i < reader.getRows().size(); i++) {
            DmDynamicDataSourceRow wjRow = new DmDynamicDataSourceRow();
            Iterator<String> keys = this.lstMsgParams.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = (String) reader.getRows().get(i).getCells().get(key);
                wjRow.getCells().put(key, value);
            }
            if (willAddToDatasource(wjRow)) {
                this.addWebJobRow(wjRow);
            }
        }
    }

    private boolean willAddToDatasource(DmDynamicDataSourceRow wjRow) {
        boolean bool = false;
        try {
            if (wjRow.getCells().containsKey("uniqueid")) {
                String idValue = wjRow.getCells().get("uniqueid");
                bool = this.uniqueIds.contains(idValue.trim());
            }
        } catch (Exception ex) {
            bool = false;
            ex.printStackTrace();
        }
        return bool;
    }

    public List<DmDynamicDataSourceRow> getDataSourceRecords() {
        return this.lstWebJobs;
    }

    public Map<String, String> getExtraRowParams() {
        return extraRowParams;
    }

    private void addWebJobRow(DmDynamicDataSourceRow row) {
        row.setMessgageFormat(this.tempModel.getMessageFormat());
        row.setSubjectFormat(this.tempModel.getSubjectFormat());
        row.setFormattedMessage(this.getMessageFromUnformatted(row, this.tempModel.getMessageFormat()));
        if (this.tempModel.getWebJobType() == DmEnum.DmWebJobTypes.EMAIL) {
            row.setFormattedSubject(this.getMessageFromUnformatted(row, this.tempModel.getSubjectFormat()));
        }
        row.setDataSource(this.tempModel.getWebJobDataSource());
        row.setMessgageFormat(this.tempModel.getMessageFormat());
        row.setFunctionOrfileName(this.tempModel.getFileOrFuncName());
        row.setJobtype(this.tempModel.getWebJobType());
        row.setTemplateId(this.tempModel.getTemplateId());
        row.setTemplateName(this.tempModel.getTemplateName());
        row.setSmsAPITemplateId(this.tempModel.getSmsAPITemplateId());
        if (!extraRowParams.isEmpty()) {
            row.getCells().putAll(extraRowParams);
        }
        List<WebJobAttachment_Mdl> rowAttachments = new ArrayList<>();
        if (!lstAttachments.isEmpty()) {
            rowAttachments.addAll(rowAttachments);
        }

        this.lstWebJobs.add(row);
    }

    public void runWebJobs(List<DmDynamicDataSourceRow> dataRows, List<WebJobAttachment_Mdl> manualAttachments) {
        try {
            this.lstWebJobs.clear();
            this.uniqueIds.clear();
            this.lstAttachments.clear();
            this.lstWebJobs.addAll(dataRows);
            if (manualAttachments != null) {
                this.lstAttachments.addAll(manualAttachments);
            }
            runJobs();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void runJobs() {
        ExecutorService executors = null;
        try {
            executors = Executors.newCachedThreadPool();
            for (int i = 0; i < this.lstWebJobs.size(); i++) {
                IDmAnonymousWebJob webJob = null;
                if (this.tempModel.getWebJobType() == DmEnum.DmWebJobTypes.EMAIL) {
                    if (lstAttachments != null && lstAttachments.size() > 0) {
                        this.lstWebJobs.get(i).getLstAttachments().addAll(lstAttachments);
                    }
                    this.getWebJobAttachments().getAttachments(this.tempModel.getTemplateId(), this.lstWebJobs.get(i), lstAttachments);
                    webJob = new DmAnonymousEMailWebJob(siteEnviro, this.lstWebJobs.get(i));
                } else if (this.tempModel.getWebJobType() == DmEnum.DmWebJobTypes.SMS) {
                    webJob = new DmAnonymousSMSWebJob(siteEnviro, this.lstWebJobs.get(i));
                }
                if (webJob != null) {
                    executors.submit(webJob);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (executors != null) {
                executors.shutdown();
            }
            executors = null;
        }
    }

    @Override
    public void run() {
        try {
            fillDataSource();
            runJobs();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            getDataSourceRecords().clear();
            getUniqueIDs().clear();
            getFileAttachments().clear();
        }
    }

}
