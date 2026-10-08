package dataman.dmBase.dmScheduler.server;

import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.dmScheduler.shared.SiteEnviro_Mdl;
import dataman.dmBase.dmScheduler.shared.WebJobAttachment_Mdl;
import dataman.dmBase.dmScheduler.shared.WebJobEmailResult_Mdl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class DmAnonymousEMailWebJob implements IDmAnonymousWebJob<WebJobEmailResult_Mdl> {

    private SiteEnviro_Mdl siteEnviro;
    private DmDynamicDataSourceRow dataRow;
    private boolean isMore = false;

    public DmAnonymousEMailWebJob(SiteEnviro_Mdl sModel, DmDynamicDataSourceRow dRow) {
        this.siteEnviro = sModel;
        this.dataRow = dRow;
    }

    @Override
    public DmWebJobResult<WebJobEmailResult_Mdl> call() throws Exception {
        DmWebJobResult<WebJobEmailResult_Mdl> result = new DmWebJobResult<>();

        try {
            WebJobEmailResult_Mdl model = new WebJobEmailResult_Mdl();
            model.setFromEmailId(siteEnviro.getSenderEmailUserName());
            model.setUniqueId(dataRow.getCells().get("uniqueid"));
            model.setToEmailId(dataRow.getCells().get("send_to_email"));
            model.setSubject(dataRow.getFormattedSubject());
            model.setBodyText(dataRow.getFormattedMessage());
            model.getLstBcc().addAll(siteEnviro.getLstEmailBcc());
            model.getLstCc().addAll(siteEnviro.getLstEmailCc());

            Properties props = new Properties();
            if (siteEnviro.getSslRequired() == DmEnum.DmYesNo.YES) {
                props.put("mail.smtp.starttls.enable", true);
                props.put("mail.smtp.auth", true);
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            }

            props.put("mail.smtp.host", siteEnviro.getWebJobSMTPServer());
            props.put("mail.smtp.user", model.getFromUserName());
            props.put("mail.smtp.password", DmBaseServer.dCodify(siteEnviro.getSenderEmailPassword()));;
            props.put("mail.smtp.port", siteEnviro.getWebJobSMTPPort());

            Session session = null;
            if (siteEnviro.getSslRequired() == DmEnum.DmYesNo.YES) {
                session = Session.getInstance(props,
                        new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(model.getFromUserName(), props.getProperty("mail.smtp.password"));
                    }
                });
            } else {
                session = Session.getInstance(props, null);
            }

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(model.getFromUserName()));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(model.getToEmailId()));

            for (int i = 0; i < siteEnviro.getLstEmailBcc().size(); i++) {
                message.addRecipient(Message.RecipientType.BCC, new InternetAddress(siteEnviro.getLstEmailBcc().get(i)));
            }

            for (int i = 0; i < siteEnviro.getLstEmailCc().size(); i++) {
                message.addRecipient(Message.RecipientType.CC, new InternetAddress(siteEnviro.getLstEmailCc().get(i)));
            }

            message.setSentDate(new Date());
            message.setSubject(model.getSubject());

            BodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(model.getBodyText(), "text/html; charset=UTF-8");
            List<MimeBodyPart> fileAttachments = new ArrayList<>();
            MimeMultipart multiPart = new MimeMultipart("related");

            for (int i = 0; i < this.dataRow.getLstAttachments().size(); i++) {
                WebJobAttachment_Mdl attachment = this.dataRow.getLstAttachments().get(i);
                if (attachment.getIsManual() == DmEnum.DmYesNo.NO) {
                    attachImage(multiPart, bodyPart, model, attachment, fileAttachments);
                } else if (attachment.getIsManual() == DmEnum.DmYesNo.YES) {
                    MimeBodyPart fileAttachment = new MimeBodyPart();
                    fileAttachment.setDataHandler(new DataHandler(new FileDataSource(attachment.getFileDirectoryWithSlash() + attachment.getActFileName())));
                    fileAttachment.setFileName(attachment.getDisplayFileName());
                    fileAttachments.add(fileAttachment);
                }
            }

            multiPart.addBodyPart(bodyPart);
            for (int i = 0; i < fileAttachments.size(); i++) {
                multiPart.addBodyPart(fileAttachments.get(i));
            }

            message.setContent(multiPart);

            Transport transport = session.getTransport("smtp");
            transport.connect(siteEnviro.getWebJobSMTPServer(), model.getFromUserName(), props.getProperty("mail.smtp.password"));
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public void attachImage(MimeMultipart multiPart, BodyPart bodyPart, WebJobEmailResult_Mdl model, WebJobAttachment_Mdl attachment, List<MimeBodyPart> fileAttachments) throws MessagingException {
        try {
            if (DmBaseShared.xNull(attachment.getFileType()).equalsIgnoreCase("attachment")) {
                MimeBodyPart fileAttachment = new MimeBodyPart();
                fileAttachment.setDataHandler(new DataHandler(new FileDataSource(DmBaseShared.xNull(attachment.getFileDirectory()))));
                fileAttachment.setFileName(DmBaseShared.xNull(attachment.getActFileName()));

                fileAttachments.add(fileAttachment);
            } else {
                if (!this.isMore) {
                    bodyPart.setHeader("Content-Type", "text/html; charset=\"utf-8\"");
                    bodyPart.setContent(model.getBodyText(), "text/html; charset=utf-8");
                    multiPart.addBodyPart(bodyPart);
                }

                MimeBodyPart inlineFileAttachment = new MimeBodyPart();
                DataSource fds = new FileDataSource(DmBaseShared.xNull(attachment.getFileDirectory()));
                inlineFileAttachment.setDataHandler(new DataHandler(fds));
                inlineFileAttachment.setHeader("Content-ID", "<" + DmBaseShared.xNull(attachment.getMongoFileLink()) + ">");
                inlineFileAttachment.setDisposition(MimeBodyPart.INLINE);
                multiPart.addBodyPart(inlineFileAttachment);
                this.isMore = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
