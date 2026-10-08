package dataman.dmBase.dmScheduler.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.DmEnum;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SiteEnviro_Mdl implements Serializable, IsSerializable {

    private String wjSMTPServer;
    private String wjSMTPPort;
    private int wjSMTPSsl;
    private DmEnum.DmYesNo sslRequired;
    private String wjSMSUrl;
    private String wjAccName;
    private String wjPassword;
    private String wjFormat;
    private int siteCode;
    private int pltCode;
    private long messageLength;
    private String senderEmailUserName;
    private String senderEmailPassword;
    private List<String> lstEmailBcc = new ArrayList<>();
    private List<String> lstEmailCc = new ArrayList<>();

    public SiteEnviro_Mdl() {
    }

    public long getMessageLength() {
        return messageLength;
    }

    public String getSenderEmailUserName() {
        return senderEmailUserName;
    }

    public void setSenderEmailUserName(String senderEmailUserName) {
        this.senderEmailUserName = senderEmailUserName;
    }

    public String getSenderEmailPassword() {
        return senderEmailPassword;
    }

    public void setSenderEmailPassword(String senderEmailPassword) {
        this.senderEmailPassword = senderEmailPassword;
    }

    public List<String> getLstEmailBcc() {
        return lstEmailBcc;
    }

    public List<String> getLstEmailCc() {
        return lstEmailCc;
    }

    public void setMessageLength(long messageLength) {
        this.messageLength = messageLength;
    }

    public int getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(int siteCode) {
        this.siteCode = siteCode;
    }

    public int getPltCode() {
        return pltCode;
    }

    public void setPltCode(int pltCode) {
        this.pltCode = pltCode;
    }

    public String getWebJobSMTPServer() {
        return wjSMTPServer;
    }

    public void setWebJobSMTPServer(String wjSMTPServer) {
        this.wjSMTPServer = wjSMTPServer;
    }

    public String getWebJobSMTPPort() {
        return wjSMTPPort;
    }

    public void setWebJobSMTPPort(String wjSMTPPort) {
        this.wjSMTPPort = wjSMTPPort;
    }

    public int getWebJobSMTPSsl() {
        return wjSMTPSsl;
    }

    public void setWebJobSMTPSsl(int wjSMTPSsl) {
        this.wjSMTPSsl = wjSMTPSsl;
    }

    public DmEnum.DmYesNo getSslRequired() {
        return sslRequired;
    }

    public void setSslRequired(DmEnum.DmYesNo sslRequired) {
        this.sslRequired = sslRequired;
    }

    public String getWebJobSMSUrl() {
        return wjSMSUrl;
    }

    public void setWebJobSMSUrl(String wjSMSUrl) {
        this.wjSMSUrl = wjSMSUrl;
    }

    public String getWebJobAccName() {
        return wjAccName;
    }

    public void setWebJobAccName(String wjAccName) {
        this.wjAccName = wjAccName;
    }

    public String getWebJobPassword() {
        return wjPassword;
    }

    public void setWebJobPassword(String wjPassword) {
        this.wjPassword = wjPassword;
    }

    public String getWebJobFormat() {
        return wjFormat;
    }

    public void setWebJobFormat(String wjFormat) {
        this.wjFormat = wjFormat;
    }

}
