package dataman.dmBase.dmScheduler.shared;

import dataman.dmBase.self.shared.DmEnum.*;

public class WebJobAttachment_Mdl {

    private long templateId;
    private String procName;
    private String rptName;
    private String actFileName;
    private String fileDirectory;
    private String displayFileName;
    private DmYesNo isManual = DmYesNo.NO;
    private String mongoFileLink;
    private String fileRuleCode;
    private String fileType;

    public WebJobAttachment_Mdl() {
    }

    public long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(long templateId) {
        this.templateId = templateId;
    }

    public String getProcName() {
        return procName;
    }

    public void setProcName(String procName) {
        this.procName = procName;
    }

    public String getRptName() {
        return rptName;
    }

    public void setRptName(String rptName) {
        this.rptName = rptName;
    }

    public String getActFileName() {
        return actFileName;
    }

    public void setActFileName(String actFileName) {
        this.actFileName = actFileName;
    }

    public String getFileDirectory() {
        return fileDirectory;
    }

    public void setFileDirectory(String fileDirectory) {
        this.fileDirectory = fileDirectory;
    }

    public String getFileDirectoryWithSlash() {
        return (this.fileDirectory.endsWith("/") ? this.fileDirectory : this.fileDirectory + "/");
    }

    public String getDisplayFileName() {
        return displayFileName;
    }

    public void setDisplayFileName(String displayFileName) {
        this.displayFileName = displayFileName;
    }

    public DmYesNo getIsManual() {
        return isManual;
    }

    public void setIsManual(DmYesNo isManual) {
        this.isManual = isManual;
    }

    public String getMongoFileLink() {
        return mongoFileLink;
    }

    public void setMongoFileLink(String mongoFileLink) {
        this.mongoFileLink = mongoFileLink;
    }

    public String getFileRuleCode() {
        return fileRuleCode;
    }

    public void setFileRuleCode(String fileRuleCode) {
        this.fileRuleCode = fileRuleCode;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

}
