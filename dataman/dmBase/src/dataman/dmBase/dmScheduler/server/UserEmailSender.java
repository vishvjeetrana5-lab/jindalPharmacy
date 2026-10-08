package dataman.dmBase.dmScheduler.server;

import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class UserEmailSender implements Runnable {

    private String fromUser = "SA";
    private String fromAddress;
    private String fromPassword;
    private String toAddress;
    private String smtpServer;
    private int smtpPort;
    private boolean smtpSslRequired;
    private String siteCode;
    private String compCode;
    private String actDbName;
    private String bodyHtmlText;
    private String subjectText;

    public UserEmailSender(String sendToEmailId, String strSubject, String htmlBodyText, String siteCode, String compCode) {
        this.toAddress = sendToEmailId.trim();
        this.siteCode = siteCode.trim();
        this.compCode = compCode.trim();
        this.subjectText = strSubject.trim();
        this.bodyHtmlText = htmlBodyText.trim();
    }

    @Override
    public void run() {
        try {
            fillUserCredentials();

            getSiteEnviro();

            sendEMail();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void fillUserCredentials() throws Exception {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        try {
            cnTemp = DmBaseServer.getConnection(DmBaseServer.dbCompany);
            String sql = "SELECT USERSITE.*,COMPANY.CENTRALDATA_PATH AS COMPANY$CENTRALDATA_PATH FROM USERSITE,COMPANY WHERE "
                    + "(USERSITE.compCode=COMPANY.comp_Code) AND (USERSITE.USER_NAME=:userName) AND (COMPANY.COMP_CODE=:compCode)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sql);
            psTemp.setString("userName", this.fromUser);
            psTemp.setString("compCode", this.compCode);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                this.fromAddress = DmBaseShared.xNull(rsTemp.getString("emailid"));
                this.actDbName = DmBaseShared.xNull(rsTemp.getString("COMPANY$CENTRALDATA_PATH"));
                this.fromPassword = DmBaseServer.dCodify(DmBaseShared.xNull(rsTemp.getString("passWd")));
            }
            rsTemp.close();
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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                    cnTemp = null;
                }
            } catch (Exception ex) {
            }
        }
    }

    private void getSiteEnviro() throws Exception {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        try {
            cnTemp = DmBaseServer.getConnection(actDbName);
            String sql = "SELECT WJ_SMTPSERVER,WJ_SMTPPORT,WJ_SMTPSSL FROM ENVIRO WHERE (SITE_CODE=:siteCode)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sql);
            psTemp.setString("siteCode", this.siteCode);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                this.smtpServer = DmBaseShared.xNull(rsTemp.getString("WJ_SMTPSERVER"));
                this.smtpPort = DmBaseShared.iNull(rsTemp.getInt("WJ_SMTPPORT")).intValue();
                this.smtpSslRequired = (DmBaseShared.iNull(rsTemp.getInt("WJ_SMTPSSL")).intValue() == 1 ? true : false);
            }
            rsTemp.close();
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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                    cnTemp = null;
                }
            } catch (Exception ex) {
            }
        }
    }

    private void sendEMail() throws Exception {
        Properties props = new Properties();

        if (this.smtpSslRequired) {
            props.put("mail.smtp.starttls.enable", true);
            props.put("mail.smtp.auth", true);
        }
        props.put("mail.smtp.host", this.smtpServer);
        props.put("mail.smtp.user", (this.fromAddress.indexOf("@") >= 0 ? this.fromAddress.substring(0, this.fromAddress.indexOf("@")) : this.fromAddress));
        props.put("mail.smtp.password", this.fromPassword);
        props.put("mail.smtp.port", String.valueOf(this.smtpPort));

        Session session = Session.getInstance(props, null);

        MimeMessage mailMessage = new MimeMessage(session);
        mailMessage.setFrom(new InternetAddress(props.getProperty("mail.smtp.user")));
        mailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));
        mailMessage.setSubject(this.subjectText);
        mailMessage.setSentDate(new Date());
        Multipart multipart = new MimeMultipart();
        BodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(this.bodyHtmlText, "text/html");
        multipart.addBodyPart(bodyPart);
        mailMessage.setContent(multipart);

        Transport transport = session.getTransport("smtp");
        transport.connect(props.getProperty("mail.smtp.host"), props.getProperty("mail.smtp.user"), props.getProperty("mail.smtp.password"));
        transport.sendMessage(mailMessage, mailMessage.getAllRecipients());
        transport.close();

        System.out.println("Email send successfully...");
    }
}
