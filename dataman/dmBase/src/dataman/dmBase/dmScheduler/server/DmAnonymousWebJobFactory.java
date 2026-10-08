package dataman.dmBase.dmScheduler.server;

import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmScheduler.shared.SiteEnviro_Mdl;
import dataman.dmBase.dmScheduler.shared.TemplateMast_Mdl;
import dataman.dmBase.dmScheduler.shared.WebJobAttachment_Mdl;
import dataman.dmBase.dmSession.server.DmRSS;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DmAnonymousWebJobFactory {

    public DmAnonymousWebJobFactory() {
    }

    public SiteEnviro_Mdl getEnviroSettings(DmRSS servletInstance) {
        return fillSiteAndTemplateDetails(DmBaseServer.getSsnDBName(servletInstance), DmBaseServer.getSsnUserName(servletInstance), DmBaseShared.iNull(DmBaseServer.getSsnSiteCode(servletInstance)).intValue(), DmBaseShared.iNull(DmBaseServer.getSsnPLTCode(servletInstance)).intValue());
    }

    private SiteEnviro_Mdl fillSiteAndTemplateDetails(String actDbName, String userName, int siteCode, int pltCode) {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        ResultSet rsTemp = null;
        boolean flag;
        SiteEnviro_Mdl settings = new SiteEnviro_Mdl();
        String sqlQuery = "", emailId = "", passWd = "", bcc = "", cc = "";
        try {

            cnTemp = DmBaseServer.getConnection(actDbName);

            sqlQuery = "Select  '1' as type,enviro.wj_smtpServer,enviro.wj_smtpPort,"
                    + "         enviro.wj_smtpSsl,enviro.wj_url,enviro.wj_acName,enviro.wj_password,"
                    + "         enviro.wj_format,enviro.wj_msgLength "
                    + "         from dbo.enviro "
                    + "         where (enviro.site_code = :siteCode); "
                    //2nd Query
                    + "         select  t.type,upc.emailId,upc.password,upc.bcc,upc.cc  "
                    + "         from (	"
                    + "                 select	3 as type "
                    + "         ) as t "
                    + "         left join ( "
                    + "                 select  3 as type,upc.emailId,upc.password,upc.bcc,upc.cc  "
                    + "                         from " + DmBaseServer.dbCompany + ".dbo.userPltConfig upc  "
                    + "                         where (upc.userName = :userName) and (upc.pltCode = :pltCode) "
                    + "         ) as upc on t.type = upc.type ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", String.valueOf(siteCode));
            psTemp.setString("userName", userName);
            psTemp.setString("pltCode", String.valueOf(pltCode));
            boolean hasResults = psTemp.execute();
            while (hasResults) {
                rsTemp = psTemp.getResultSet();
                while (rsTemp.next()) {
                    if (DmBaseShared.iNull(rsTemp.getString("type")).intValue() == 1) {
                        settings.setSiteCode(siteCode);
                        settings.setWebJobSMTPServer(DmBaseShared.xNull(rsTemp.getString("wj_smtpServer")));
                        settings.setWebJobSMTPPort(DmBaseShared.xNull(rsTemp.getString("wj_smtpPort")));
                        settings.setSslRequired(DmEnum.DmYesNo.valueOf(DmBaseShared.iNull(rsTemp.getInt("wj_smtpSsl")).intValue()));
                        settings.setWebJobSMSUrl(DmBaseShared.xNull(rsTemp.getString("wj_url")));
                        settings.setWebJobPassword(DmBaseShared.xNull(rsTemp.getString("wj_password")));
                        settings.setWebJobFormat(DmBaseShared.xNull(rsTemp.getString("wj_format")));
                        settings.setWebJobAccName(DmBaseShared.xNull(rsTemp.getString("wj_acName")));
                        settings.setMessageLength(DmBaseShared.iNull(rsTemp.getInt("wj_msgLength")).intValue());
                    } else if (DmBaseShared.iNull(rsTemp.getString("type")).intValue() == 3) {
                        flag = true;
                        if (DmBaseShared.xNull(rsTemp.getString("emailId")).equalsIgnoreCase("")) {
                            flag = false;
                            rsTemp.close();
                            sqlQuery = "select emailId,password,bcc,cc "
                                    + "from " + DmBaseServer.dbCompany + ".dbo.userPltConfig "
                                    + "where (userName = (select user_Role from " + DmBaseServer.dbCompany + ".dbo.userMast where user_Name = :userName)) "
                                    + "and (pltCode=:pltCode)";
                            psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp1.setString("userName", userName);
                            psTemp1.setString("pltCode", String.valueOf(pltCode));
                            rsTemp = psTemp1.executeQuery();
                            if (rsTemp.next()) {
                                flag = true;
                            }
                        }
                        if (flag) {
                            emailId = DmBaseShared.xNull(rsTemp.getString("emailId"));
                            passWd = DmBaseShared.xNull(rsTemp.getString("password"));
                            bcc = DmBaseShared.xNull(rsTemp.getString("bcc"));
                            cc = DmBaseShared.xNull(rsTemp.getString("cc"));

                            if (!emailId.equalsIgnoreCase("")) {
                                settings.setSenderEmailUserName(emailId);
                            }
                            if (!passWd.equalsIgnoreCase("")) {
                                settings.setSenderEmailPassword(passWd);
                            }
                            if (!bcc.equalsIgnoreCase("")) {
                                String[] tmpBcc = bcc.split("\\|");
                                for (int i = 0; i < tmpBcc.length; i++) {
                                    settings.getLstEmailBcc().add(tmpBcc[i].trim());
                                }
                            }
                            if (!cc.equalsIgnoreCase("")) {
                                String[] tmpCc = cc.split("\\|");
                                for (int i = 0; i < tmpCc.length; i++) {
                                    settings.getLstEmailCc().add(tmpCc[i].trim());
                                }
                            }
                        }
                    }
                }
                hasResults = psTemp.getMoreResults();
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

        } catch (Exception ex) {
            ex.printStackTrace();
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
                if (psTemp1 != null) {
                    psTemp1.close();
                    psTemp1 = null;
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                    cnTemp = null;
                }
            } catch (Exception ex) {

            }
        }
        return settings;
    }

    public void runJobs(DmRSS servletInstance, SiteEnviro_Mdl settings, List<Long> templateIds, List<DmDynamicDataSourceRow> dataRows, List<WebJobAttachment_Mdl> manualEmailAttachments) {
        try {
            if (templateIds.size() > 1) {
                throw new Exception("Template Ids can't be more than one when calling other than voucher.");
            }

            Iterator<DmAnonymousWebJobTemplateEngine> iterator = fillTemplateSettings(servletInstance.getLocalRequest().getSession().getId(), DmBaseServer.getSsnFileDBName(servletInstance), DmBaseServer.getSsnDBName(servletInstance), settings, templateIds).values().iterator();
            while (iterator.hasNext()) {
                DmAnonymousWebJobTemplateEngine engine = iterator.next();
                engine.runWebJobs(dataRows, manualEmailAttachments);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Map<Long, DmAnonymousWebJobTemplateEngine> fillTemplateSettings(String sessionId, String fileDbName, String actDbName, SiteEnviro_Mdl settings, List<Long> lstTemplateIds) throws Exception {
        Map<Long, DmAnonymousWebJobTemplateEngine> lstTemplateWebJobs = new HashMap<>();

        if (lstTemplateIds != null) {
            for (int i = 0; i < lstTemplateIds.size(); i++) {
                TemplateMast_Mdl tModel = new TemplateMast_Mdl();
                tModel.setTemplateId(lstTemplateIds.get(i));
                DmAnonymousWebJobTemplateEngine tmpEngine = new DmAnonymousWebJobTemplateEngine(tModel);
                tmpEngine.setWebJobAttachments(new DmWebJobAttachments(sessionId, actDbName, fileDbName));
                tmpEngine.setEnviroSettings(settings);

                lstTemplateWebJobs.put(tModel.getTemplateId(), tmpEngine);
            }
            fillTemplateDetails(actDbName, lstTemplateWebJobs);
        }

        return lstTemplateWebJobs;
    }

    private void fillTemplateDetails(String actDbName, Map<Long, DmAnonymousWebJobTemplateEngine> lstTemplateWebJobs) {
        Map<String, String> params = new HashMap<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;

        try {
            cnTemp = DmBaseServer.getConnection(actDbName);

            String tempSql = "";
            if (lstTemplateWebJobs.size() > 0) {
                tempSql = "(";
                int paramIndex = 1;
                Iterator<Long> iterator = lstTemplateWebJobs.keySet().iterator();
                while (iterator.hasNext()) {
                    String paramName = "tempCode" + String.valueOf(paramIndex);
                    String templateId = iterator.next().toString();
                    params.put(paramName, templateId);
                    tempSql += ":" + paramName + ",";
                    paramIndex += 1;
                }
                if (tempSql.endsWith(",")) {
                    tempSql = tempSql.substring(0, tempSql.length() - 1);
                }
                tempSql += ")";
            }

            String sql = "SELECT WJTEMPLATEMAST.* FROM WJTEMPLATEMAST WHERE (1=1)" + ((tempSql.length() > 0) ? " AND (CODE IN " + tempSql + ")" : "");

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sql);
            for (Map.Entry<String, String> entry : params.entrySet()) {
                psTemp.setString(entry.getKey(), entry.getValue());
            }

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                Long tempId = DmBaseShared.iNull(rsTemp.getLong("CODE")).longValue();
                lstTemplateWebJobs.get(tempId).setActiveDbName(actDbName);
                lstTemplateWebJobs.get(tempId).getTemplateModel().setFileOrFuncName(DmBaseShared.xNull(rsTemp.getString("FUNCTIONNAME")));
                lstTemplateWebJobs.get(tempId).getTemplateModel().setMessageFormat(DmBaseShared.xNull(rsTemp.getString("MESSAGE")));
                lstTemplateWebJobs.get(tempId).getTemplateModel().setSubjectFormat(DmBaseShared.xNull(rsTemp.getString("SUBJECT")));
                lstTemplateWebJobs.get(tempId).getTemplateModel().setTemplateId(DmBaseShared.iNull(rsTemp.getLong("CODE")).longValue());
                lstTemplateWebJobs.get(tempId).getTemplateModel().setTemplateName(DmBaseShared.xNull(rsTemp.getString("NAME")));
                lstTemplateWebJobs.get(tempId).getTemplateModel().setSmsAPITemplateId(DmBaseShared.xNull(rsTemp.getString("apiTemplateId")));
                lstTemplateWebJobs.get(tempId).getTemplateModel().setWebJobDataSource(DmEnum.DmWebJobDataSources.getValueFrom(DmBaseShared.iNull(rsTemp.getInt("DATASOURCE")).intValue()));
                lstTemplateWebJobs.get(tempId).getTemplateModel().setWebJobType(DmEnum.DmWebJobTypes.getValueFrom(DmBaseShared.iNull(rsTemp.getInt("TEMPLATETYPE")).intValue()));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                    cnTemp = null;
                }
            } catch (Exception ex) {
            }
        }
    }

//    public void runJobsAsync(DmRSS servletInstance, SiteEnviro_Mdl settings, List<Long> templateIds, List<String> uniqueIds, List<WebJobAttachment_Mdl> manualEmailAttachments) {
//        runJobsAsync(servletInstance, settings, templateIds, uniqueIds, null, manualEmailAttachments);
//    }
    public void runJobsAsync(DmRSS servletInstance, SiteEnviro_Mdl settings, List<Long> templateIds, List<String> uniqueIds, List<WebJobAttachment_Mdl> manualEmailAttachments) {
        ExecutorService executorService = null;
        try {
            executorService = Executors.newFixedThreadPool(10);

            Iterator<DmAnonymousWebJobTemplateEngine> iterator = fillTemplateSettings(servletInstance.getLocalRequest().getSession().getId(), DmBaseServer.getSsnFileDBName(servletInstance), DmBaseServer.getSsnDBName(servletInstance), settings, templateIds).values().iterator();

            while (iterator.hasNext()) {
                DmAnonymousWebJobTemplateEngine engine = iterator.next();
//                engine.getExtraRowParams().putAll(rowExtraParams);
                engine.getUniqueIDs().addAll(uniqueIds);
                if (manualEmailAttachments != null) {
                    engine.getFileAttachments().addAll(manualEmailAttachments);
                }
                executorService.submit(engine);
//                Thread th = new Thread(engine);
//                th.start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (executorService != null) {
                executorService.shutdown();
                executorService = null;
            }
        }
    }

}
