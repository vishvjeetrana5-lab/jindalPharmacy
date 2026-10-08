package dataman.dmBase.dmScheduler.server;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.dmScheduler.shared.WebJobAttachment_Mdl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DmDynamicDataSourceRow implements Serializable, IsSerializable {

    private String messgageFormat;
    private String formattedMessage;
    private String subjectFormat;
    private String formattedSubject;
    private long templateId;
    private String templateName;
    private String functionOrfileName;
    private String smsAPITemplateId;
    private DmWebJobDataSources dataSource;
    private DmWebJobTypes jobtype;
    private Map<String, String> cells = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private List<WebJobAttachment_Mdl> lstAttachments = new ArrayList<WebJobAttachment_Mdl>();

    public DmDynamicDataSourceRow() {
    }

    public String getMessgageFormat() {
        return messgageFormat;
    }

    public void setMessgageFormat(String messgageFormat) {
        this.messgageFormat = messgageFormat;
    }

    public String getFormattedMessage() {
        return formattedMessage;
    }

    public void setFormattedMessage(String formattedMessage) {
        this.formattedMessage = formattedMessage;
    }

    public String getSubjectFormat() {
        return subjectFormat;
    }

    public void setSubjectFormat(String subjectFormat) {
        this.subjectFormat = subjectFormat;
    }

    public String getFormattedSubject() {
        return formattedSubject;
    }

    public void setFormattedSubject(String formattedSubject) {
        this.formattedSubject = formattedSubject;
    }

    public long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(long templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getFunctionOrfileName() {
        return functionOrfileName;
    }

    public void setFunctionOrfileName(String functionOrfileName) {
        this.functionOrfileName = functionOrfileName;
    }

    public DmWebJobDataSources getDataSource() {
        return dataSource;
    }

    public void setDataSource(DmWebJobDataSources dataSource) {
        this.dataSource = dataSource;
    }

    public DmWebJobTypes getJobtype() {
        return jobtype;
    }

    public void setJobtype(DmWebJobTypes jobtype) {
        this.jobtype = jobtype;
    }

    public Map<String, String> getCells() {
        return cells;
    }

    public List<WebJobAttachment_Mdl> getLstAttachments() {
        return lstAttachments;
    }

    public String getSmsAPITemplateId() {
        return smsAPITemplateId;
    }

    public void setSmsAPITemplateId(String smsAPITemplateId) {
        this.smsAPITemplateId = smsAPITemplateId;
    }

}
