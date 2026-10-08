package aarogya.selfToolkit.stStatic.server;

import aarogya.projectMenu.mi.shared.ExtendedLocalSettings;
import aarogya.selfToolkit.stStatic.shared.STClient_Srv;
import aarogya.selfToolkit.stStatic.shared.STLocalSetting_Mdl;
import com.google.gson.reflect.TypeToken;
import com.jolbox.bonecp.ConnectionHandle;
import com.microsoft.sqlserver.jdbc.SQLServerDataTable;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.jvmSerialization.shared.JVMLocalSetting_Mdl;
import dataman.dmBase.self.server.DmIJasperCompiled;
import dataman.dmBase.self.server.DmJasperStyle;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.io.File;
import java.security.Key;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class STClient_SrvImpl extends DmRSSPrivate implements STClient_Srv {

    private String codifyKey1 = "~!≡s5*.d", codifyKey2 = "T←mÄn╤sY";
    private static String codifyAlgorithm = "AES";

    @Override
    public DmResult<STLocalSetting_Mdl> getSettingsFromJson(String jsonString) {
        DmResult<STLocalSetting_Mdl> result = new DmResult<>();
        try {

            JVMLocalSetting_Mdl settings = DmServer.convertFromJson(jsonString, new TypeToken<JVMLocalSetting_Mdl>() {
            });

            STLocalSetting_Mdl settingsModel = new STLocalSetting_Mdl();
            if (settings != null) {
                settingsModel.setDmLocalSettingPrintMap_Mdls(settings.getReportMapSettings());
                settingsModel.setDmLocalSettingPrinter_Mdls(settings.getPrinterSettings());
                settingsModel.setExtendedLocalSettingJsonString(DmShared.xNull(settings.getExtendedLocalSetting(ExtendedLocalSettings.MI_Configurations.toString())));
            }
            result.setData(settingsModel);
            result.setStatus(true);
        } catch (Exception ex) {
            result.setStatus(false);
            result.setErrorMessage(ex.getMessage());
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public DmResult<String> updatePrintStatus(String tableName, String fieldName, String docId) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        dmrTemp.setData("");
        String sqlQuery;
        DmPreparedStatement psTemp = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "update " + tableName + " set " + fieldName + " = isnull(" + fieldName + ",0)+1 "
                    + " where docid = :docid ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            psTemp.executeUpdate();
            psTemp.close();

            dmrTemp.setData(docId);
            dmrTemp.setStatus(true);
            cnTemp.close();
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    @Override
    public DmResult<String> sendReportToEmail(String docId, String vType, LinkedHashMap<String, FieldUDT_Mdl> filter, String regionalLanguage, String reportTitle) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        dmrTemp.setData("");
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        String reportName = "", documentMailTemplate = "", documentMailUser = "";
        ResultSet rsTemp = null;
        SQLServerDataTable table = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        DmIJasperCompiled jasperCompiled;
        LinkedHashMap<String, Object> hm = new LinkedHashMap<String, Object>();
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

//            sqlQuery = "select  top 1 swrc.reportName, swrc.pageSize "
//                    + "         from siteWiseReportConfig swrc  "
//                    + "         where swrc.v_Type = :v_Type and isNull(swrc.site_Code,:siteCode) = :siteCode  "
//                    + "         order by swrc.site_Code desc ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("v_Type", DmShared.iNull(vType).toString());
//            psTemp.setString("siteCode", DmShared.iNull(DmServer.getSsnSiteCode(this)).toString());
//            rsTemp = psTemp.executeQuery();
//            if (rsTemp.next()) {
//                reportName = DmShared.xNull(rsTemp.getString("reportName"));
//            }
//            rsTemp.close();
//            psTemp.close();
            sqlQuery = "select documentMailReportName from voucher_Type where v_Type = :v_Type";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("v_Type", DmShared.iNull(vType).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                reportName = DmShared.xNull(rsTemp.getString("documentMailReportName"));
            }
            rsTemp.close();
            psTemp.close();

            //============================
            sqlQuery = "select vt.documentMailUser,wjt.name as documentMailTemplateName from voucher_type vt\n"
                    + "left join wjTemplateMast wjt on wjt.code=vt.documentMailTemplateCode\n"
                    + "\n"
                    + "where vt.v_type= :v_Type  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("v_Type", DmShared.iNull(vType).toString());

            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                documentMailTemplate = DmShared.xNull(rsTemp.getString("documentMailTemplateName"));
                documentMailUser = DmShared.xNull(rsTemp.getString("documentMailUser"));
            }
            rsTemp.close();
            psTemp.close();

            if (!documentMailTemplate.equalsIgnoreCase("") && !documentMailUser.equalsIgnoreCase("") && !reportName.equalsIgnoreCase("")) {

                //===============================
                table = DmServer.getFieldUDT(filter);
                //===============================

                DmReportProperty_Mdl reportProperty = new DmReportProperty_Mdl();
                reportProperty.setPrintToPrinter(false);
                reportProperty.setExportType(DmEnum.DmExportType.PDF);
//==========================================================
                DmJasperStyle jasperStyle = new DmJasperStyle("regionalLanguageStyle");
                jasperStyle.setFontName(DmEnum.RegionalLanguage.getValueFrom(regionalLanguage).getFontName());

                jasperCompiled = DmServer.compileJasper(reportName, "osr", true, jasperStyle);
                jasperReport = jasperCompiled.getReport();

                hm.putAll(jasperCompiled.getSubReports());
                hm.put("REPORT_CONNECTION", ((ConnectionHandle) cnTemp).getInternalConnection());
                hm.put("fieldUDT", table);
                hm.put("SUBREPORT_DIR", "osr");

                jasperPrint = JasperFillManager.fillReport(jasperReport, DmServer.setJasperVariables(this, reportTitle, jasperReport, filter, hm));
                DmServer.setDmCustomJasperProperties(jasperReport, jasperPrint);
                dmrTemp.setData(DmServer.showJasper(this, jasperPrint, "", reportProperty));

                sendMail2(docId, DmServer.Path.sessions + File.separatorChar + this.getLocalRequest().getSession().getId().trim() + File.separatorChar + dmrTemp.getData(), documentMailUser, documentMailTemplate);
            }

            dmrTemp.setStatus(true);

            cnTemp.close();
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }
    //==========================================

    private void sendMail2(String docid, String filePath, String documentMailUser, String documentMailTemplate) {

        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

//        String to = "webaarogya@gmail.com";
//        String from = "webaarogya@gmail.com";
//        final String username = "webaarogya@gmail.com";
//        final String password = "bhagcvigslkvwhox";
//        String host = "smtp.gmail.com";
//        String port = "465";
        String username = "";
        String password = "";
        String from = "";
        String host = "";
        String port = "";
        String toEmail = "";
        String ccEmail = "";

        Properties props = new Properties();

        try {

            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            sqlQuery = "select top 1 emailId,password   from userPLTConfig where  userName=:userName";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("userName", documentMailUser);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                username = DmShared.xNull(rsTemp.getString("emailId"));
                from = DmShared.xNull(rsTemp.getString("emailId"));
                password = dCodify(DmShared.xNull(rsTemp.getString("password")));

            }
            rsTemp.close();
            psTemp.close();

            cnTemp.setCatalog(DmServer.getSsnDBName(this));

            sqlQuery = "select wj_SmtpServer,wj_SmtpPort  from enviro  where site_Code= :site_Code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                host = DmShared.xNull(rsTemp.getString("wj_SmtpServer"));
                port = DmShared.xNull(rsTemp.getString("wj_SmtpPort"));

            }
            rsTemp.close();
            psTemp.close();

            /////===============================Template========================
//            String mailTemplateName = "eMail_PoMail";
            String mailTemplateName = documentMailTemplate;
            String mailSqlFunction = "";
            String mailMsgBody = "";
            String subject = "";

            //==================Get template Parameters======================
            sqlQuery = " select functionName,message,subject from wjTemplateMast where name = :mailTemplateName ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("mailTemplateName", mailTemplateName);

            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mailSqlFunction = DmShared.xNull(rsTemp.getString("functionName"));
                mailMsgBody = DmShared.xNull(rsTemp.getString("message"));
                subject = DmShared.xNull(rsTemp.getString("subject"));

            }
            rsTemp.close();
            psTemp.close();

            //=================================================================
            Map<String, String> map = new HashMap<String, String>();
            sqlQuery = "select * from  dbo." + mailSqlFunction + "('','',:docid)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docid", docid);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                for (int i = 1; i <= rsTemp.getMetaData().getColumnCount(); i++) {
                    if (rsTemp.getMetaData().getColumnTypeName(i).equalsIgnoreCase("date")) {
                        map.put(rsTemp.getMetaData().getColumnName(i), DmShared.dNull(rsTemp.getDate(rsTemp.getMetaData().getColumnName(i))));
                    } else {
                        map.put(rsTemp.getMetaData().getColumnName(i), rsTemp.getString(rsTemp.getMetaData().getColumnName(i)));
                    }

                }
            }
            rsTemp.close();
            psTemp.close();

            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                if (mailMsgBody.contains("{" + key + "}")) {
                    mailMsgBody = mailMsgBody.replace("{" + key + "}", value);
                }
                if (key.equalsIgnoreCase("Send_To_EMail")) {
                    toEmail = value;
                }
                if (key.equalsIgnoreCase("Send_Cc_EMail")) {
                    ccEmail = value;
                }
            }

            String msgBody = mailMsgBody;

//            String encodedMsg = java.net.URLEncoder.encode(msgBody, "UTF-8");
            String encodedMsg = msgBody;

            //==================================================================
            //============================================================Send Mail=================================================
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", port);
            props.put("mail.smtp.password", password);
            props.put("userName", username);
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(props.getProperty("userName"), props.getProperty("mail.smtp.password"));
                }
            });

            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));

            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(toEmail));

            if (!DmShared.xNull(ccEmail).equalsIgnoreCase("")) {
                message.setRecipients(Message.RecipientType.CC,
                        InternetAddress.parse(ccEmail));
            }

            message.setSubject(subject);

            BodyPart messageBodyPart = new MimeBodyPart();

            messageBodyPart.setContent(encodedMsg, "text/html; charset=UTF-8");

            MimeMultipart multipart = new MimeMultipart("related");

            multipart.addBodyPart(messageBodyPart);

            messageBodyPart = new MimeBodyPart();
//            String filename = "C:\\Users\\dataman\\Desktop\\Pankaj\\saleBill.pdf";
            String filename = filePath;
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName("attachment.pdf");
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);

            Transport transport = session.getTransport("smtp");
            transport.connect(host, username, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

            System.out.println("Sent mail successfully by external thread.");

            cnTemp.close();

        } catch (Exception e) {

        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public String dCodify(String stringToDCodify) throws Exception {
        byte[] keyBytes = convertStringToBytes(codifyKey1 + codifyKey2);
        Key key = new SecretKeySpec(keyBytes, codifyAlgorithm);
        Cipher cipher = Cipher.getInstance(codifyAlgorithm);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedValue64 = java.util.Base64.getDecoder().decode(stringToDCodify);
        byte[] decryptedValue = cipher.doFinal(decryptedValue64);
        String decryptedString = new String(decryptedValue, "utf-8");
        return decryptedString;
    }

    private byte[] convertStringToBytes(String str) {
        char[] charArray = str.toCharArray();
        byte[] bytes = new byte[str.toCharArray().length];
        for (int i = 0; i < charArray.length; i++) {
            bytes[i] = (byte) charArray[i];
        }
        return bytes;
    }

}
