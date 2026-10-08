package dataman.dmBase.dmScheduler.server;

import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmScheduler.shared.SchedulerMast_Mdl;
import dataman.dmBase.dmScheduler.shared.SiteEnviro_Mdl;
import dataman.dmBase.dmScheduler.shared.TemplateMast_Mdl;
import dataman.dmBase.self.server.DmCsvReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DmManualScheduler extends DmScheduler {

    private Map<String, List<String>> namesWithValues = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    public DmManualScheduler(SiteEnviro_Mdl siteEnviro, SchedulerMast_Mdl model, Date dtTemp, Map<String, List<String>> namesWithValues) {
        super(model, dtTemp);
        this.getEnviros().clear();
        this.getEnviros().put(siteEnviro.getSiteCode(), siteEnviro);
        fillNamesWithValues(namesWithValues);
    }

    private void fillNamesWithValues(Map<String, List<String>> maps) {
        this.namesWithValues.clear();

        for (Map.Entry<String, List<String>> entry : maps.entrySet()) {
            String colName = entry.getKey();
            List<String> values = entry.getValue();
            List<String> nValues = new ArrayList<>();
            nValues.addAll(values);
            this.namesWithValues.put(colName, nValues);
        }
    }

    @Override
    public boolean shouldExecute(Calendar dtPrevious, Calendar dtNow) {
        return true;
    }

    @Override
    public void fillJobDetails(Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        try {

            String sql = "SELECT STEMP.CODE,STEMP.TEMPLATETYPE,STEMP.TEMPNAME,STEMP.DATASOURCE,STEMP.FUNCTIONNAME,STEMP.MESSAGE,UTEMP.NAME,STEMP.SUBJECT FROM WEBJOBTEMPLATEMAST STEMP LEFT JOIN UNDERTEMPLATEMAST UTEMP ON (STEMP.UNDTEMPCODE=UTEMP.CODE) WHERE (STEMP.CODE=:tempCode)";
            DmPreparedStatement.prepareStatement(cnTemp, sql);
            psTemp.setLong("tempCode", this.getSchedularModel().getWebJobTemplateId());
            TemplateMast_Mdl tModel = new TemplateMast_Mdl();
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                tModel.setTemplateId(DmBaseShared.iNull(rsTemp.getLong("CODE")).longValue());
                tModel.setTemplateName(DmBaseShared.xNull(rsTemp.getString("TEMPNAME")));
                tModel.setWebJobType(DmEnum.DmWebJobTypes.getValueFrom(DmBaseShared.iNull(rsTemp.getShort("TEMPLATETYPE")).intValue()));
                tModel.setWebJobDataSource(DmEnum.DmWebJobDataSources.getValueFrom(DmBaseShared.iNull(rsTemp.getShort("DATASOURCE")).intValue()));
                tModel.setMessageFormat(DmBaseShared.xNull(rsTemp.getString("MESSAGE")));
                tModel.setFileOrFuncName(DmBaseShared.xNull(rsTemp.getString("FUNCTIONNAME")));
                tModel.setSubjectFormat(DmBaseShared.xNull(rsTemp.getString("SUBJECT")));
            }

            rsTemp.close();
            psTemp.close();

            this.setTemplateModel(tModel);
        } catch (Exception ex) {

        } finally {
            if (psTemp != null) {
                psTemp.close();
                psTemp = null;
            }

            if (rsTemp != null) {
                rsTemp.close();
                rsTemp = null;
            }
        }
    }

    @Override
    public void fillAttachmentDetails(Connection cnTemp) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void fillSiteCodes(Connection cnTemp) throws Exception {
    }

    @Override
    public void fillDataAndParametersFromDB(SiteEnviro_Mdl siteEnviro, Connection cnTemp) throws Exception {

        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        try {

            String wSql = "";
            Map<String, String> params = new HashMap<>();
            for (Map.Entry<String, List<String>> entry : this.namesWithValues.entrySet()) {
                String colName = entry.getKey();
                List<String> values = entry.getValue();
                if (values.size() > 0) {
                    if (values.size() == 1) {
                        params.put(colName.trim(), values.get(0).trim());
                        wSql += " AND (" + colName + "=:" + colName + ")";
                    } else {
                        wSql += " AND (" + colName + " IN (";
                        for (int i = 0; i < values.size(); i++) {
                            params.put(colName + String.valueOf(i), values.get(i));
                            wSql += ":" + colName + String.valueOf(i) + ",";
                        }
                        if (wSql.endsWith(",")) {
                            wSql = wSql.substring(0, wSql.length() - 1);
                        }
                        wSql += "))";
                    }
                }
            }

            String sql = "SELECT * FROM " + this.getFileOrFunctionName() + "(:templateId,:siteCode,:uniqueKey) WHERE (1=1) " + wSql;
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sql);
            psTemp.setString("templateId", String.valueOf(this.getTemplateId()));
            psTemp.setString("siteCode", String.valueOf(siteEnviro.getSiteCode()));
            psTemp.setString("uniqueKey", "");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                psTemp.setString(entry.getKey(), entry.getValue());
            }
            rsTemp = psTemp.executeQuery();
            ResultSetMetaData colMetaData = rsTemp.getMetaData();
            for (int i = 1; i <= colMetaData.getColumnCount(); i++) {
                String colName = colMetaData.getColumnName(i);
                DmMessageParam param = new DmMessageParam(colName);
                if (!this.getMessageParams().containsKey(param.getColumnName())) {
                    this.getMessageParams().put(param.getColumnName(), param);
                }
            }

            while (rsTemp.next()) {
                DmWebJobDataRow wjRow = new DmWebJobDataRow();
                wjRow.setMessgageFormat(wjRow.getMessageFromUnformatted(this.getMessageFormat()));
                Iterator<String> keys = this.getMessageParams().keySet().iterator();
                while (keys.hasNext()) {
                    String key = keys.next();
                    wjRow.getDataRow().put(key, DmBaseShared.xNull(rsTemp.getString(key)));
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

    @Override
    public void fillDataAndParametersFromFile(SiteEnviro_Mdl siteEnviro) throws Exception {

        String readDirectoryPath = "";
        if (this.getSchedularModel().getWebJobType() == DmEnum.DmWebJobTypes.SMS) {
            readDirectoryPath = DmBaseServer.Path.csvTemplates_SMS_eMail;
        } else if (this.getSchedularModel().getWebJobType() == DmEnum.DmWebJobTypes.EMAIL) {
            readDirectoryPath = DmBaseServer.Path.csvTemplates_SMS_eMail;
        }
        String fullFilePath = readDirectoryPath + "/" + String.valueOf(this.getTemplateId()) + ".csv";

        DmCsvReader reader = new DmCsvReader(fullFilePath);
        reader.readLines();
        for (int i = 0; i < reader.getHeaders().size(); i++) {
            String key = reader.getHeaders().get(i);
            if (!this.getMessageParams().containsKey(key)) {
                DmMessageParam param = new DmMessageParam(key);
                this.getMessageParams().put(key, param);
            }
        }

        for (int i = 0; i < reader.getRows().size(); i++) {
            DmWebJobDataRow wjRow = new DmWebJobDataRow();
            Iterator<String> keys = this.getMessageParams().keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = (String) reader.getRows().get(i).getCells().get(key);
                wjRow.getDataRow().put(key, value);
            }
            if (willAddToDatasource(wjRow)) {
                this.addWebJobRow(wjRow);
            }
        }
    }

    private boolean willAddToDatasource(DmWebJobDataRow row) {
        boolean bool = false;
        try {

            if (this.namesWithValues.size() == 0) {
                bool = true;
            } else {
                List<Boolean> lstFlags = new ArrayList<>();
                for (Map.Entry<String, List<String>> entry : this.namesWithValues.entrySet()) {
                    if (row.getDataRow().containsKey(entry.getKey())) {
                        for (int i = 0; i < entry.getValue().size(); i++) {
                            String valueToCompare = entry.getValue().get(i);
                            lstFlags.add(row.getDataRow().get(entry.getKey()).equalsIgnoreCase(valueToCompare));
                        }
                    }
                }
                bool = (lstFlags.stream().filter(b -> b == true).count() == this.namesWithValues.size());
            }

        } catch (Exception ex) {
            bool = false;
            ex.printStackTrace();
        }
        return bool;
    }

}
