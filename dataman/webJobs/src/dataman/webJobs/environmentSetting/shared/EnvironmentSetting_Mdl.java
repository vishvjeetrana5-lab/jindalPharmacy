package dataman.webJobs.environmentSetting.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class EnvironmentSetting_Mdl extends KeyField implements Serializable, IsSerializable {

    private String smtpServer;
    private String smtpPort;
    private String smtpSslTag;
    private String smtpSslText;
    private String sms;
    private String url;
    private String accName;
    private String password;
    private String format;
    private String msgLength;
    private String payRefOtpMobile;
    private String uName;
    private String date;
    private String time;
    private String siteCode;
    private String siteName;

    public EnvironmentSetting_Mdl() {
    }

    public String getSmtpServer() {
        return smtpServer;
    }

    public void setSmtpServer(String smtpServer) {
        this.smtpServer = smtpServer;
    }

    public String getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }

    public String getSmtpSslTag() {
        return smtpSslTag;
    }

    public void setSmtpSslTag(String smtpSslTag) {
        this.smtpSslTag = smtpSslTag;
    }

    public String getSmtpSslText() {
        return smtpSslText;
    }

    public void setSmtpSslText(String smtpSslText) {
        this.smtpSslText = smtpSslText;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getMsgLength() {
        return msgLength;
    }

    public void setMsgLength(String msgLength) {
        this.msgLength = msgLength;
    }

    public String getPayRefOtpMobile() {
        return payRefOtpMobile;
    }

    public void setPayRefOtpMobile(String payRefOtpMobile) {
        this.payRefOtpMobile = payRefOtpMobile;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

}
