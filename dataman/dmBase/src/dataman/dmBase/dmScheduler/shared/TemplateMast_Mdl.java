package dataman.dmBase.dmScheduler.shared;

import dataman.dmBase.self.shared.DmEnum;

public class TemplateMast_Mdl {

    private Long tempId;
    private String tempName;
    private String smsAPITemplateId;
    private DmEnum.DmWebJobTypes jobType;
    private DmEnum.DmWebJobDataSources dataSource;
    private String msgFormat;
    private String fileorfuncName;
    private String subjectFormat;

    public Long getTemplateId() {
        return tempId;
    }

    public void setTemplateId(Long tempId) {
        this.tempId = tempId;
    }

    public String getTemplateName() {
        return tempName;
    }

    public void setTemplateName(String tempName) {
        this.tempName = tempName;
    }

    public DmEnum.DmWebJobTypes getWebJobType() {
        return jobType;
    }

    public void setWebJobType(DmEnum.DmWebJobTypes jobType) {
        this.jobType = jobType;
    }

    public DmEnum.DmWebJobDataSources getWebJobDataSource() {
        return dataSource;
    }

    public void setWebJobDataSource(DmEnum.DmWebJobDataSources dataSource) {
        this.dataSource = dataSource;
    }

    public String getMessageFormat() {
        return msgFormat;
    }

    public void setMessageFormat(String msgFormat) {
        this.msgFormat = msgFormat;
    }

    public String getFileOrFuncName() {
        return fileorfuncName;
    }

    public void setFileOrFuncName(String fileorfuncName) {
        this.fileorfuncName = fileorfuncName;
    }

    public String getSubjectFormat() {
        return subjectFormat;
    }

    public void setSubjectFormat(String subjectFormat) {
        this.subjectFormat = subjectFormat;
    }

    @Override
    public boolean equals(Object obj) {
        boolean retVal = false;
        if (obj != null) {
            if (obj instanceof TemplateMast_Mdl) {
                retVal = this.tempId.equals(((TemplateMast_Mdl) obj).getTemplateId());
            }
        }
        return retVal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.tempId != null ? this.tempId.hashCode() : 0);
        return hash;
    }

    public String getSmsAPITemplateId() {
        return smsAPITemplateId;
    }

    public void setSmsAPITemplateId(String smsAPITemplateId) {
        this.smsAPITemplateId = smsAPITemplateId;
    }
}
