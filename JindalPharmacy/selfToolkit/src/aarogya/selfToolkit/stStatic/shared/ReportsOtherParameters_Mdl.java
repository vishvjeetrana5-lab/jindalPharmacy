package aarogya.selfToolkit.stStatic.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class ReportsOtherParameters_Mdl extends KeyField implements Serializable, IsSerializable {

   private String sessionId,   widgetName,siteCode, reportTitle,filePaths, fileNames;
   private String toEmail, ccEmail, subject,  activeDB,encodedMsg ;
   private boolean callFromScheduler;
   
   
   
   

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getWidgetName() {
        return widgetName;
    }

    public void setWidgetName(String widgetName) {
        this.widgetName = widgetName;
    }

    public boolean isCallFromScheduler() {
        return callFromScheduler;
    }

    public void setCallFromScheduler(boolean callFromScheduler) {
        this.callFromScheduler = callFromScheduler;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public String getFilePaths() {
        return filePaths;
    }

    public void setFilePaths(String filePaths) {
        this.filePaths = filePaths;
    }

    public String getFileNames() {
        return fileNames;
    }

    public void setFileNames(String fileNames) {
        this.fileNames = fileNames;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getCcEmail() {
        return ccEmail;
    }

    public void setCcEmail(String ccEmail) {
        this.ccEmail = ccEmail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getEncodedMsg() {
        return encodedMsg;
    }

    public void setEncodedMsg(String encodedMsg) {
        this.encodedMsg = encodedMsg;
    }

    public String getActiveDB() {
        return activeDB;
    }

    public void setActiveDB(String activeDB) {
        this.activeDB = activeDB;
    }
 
}
