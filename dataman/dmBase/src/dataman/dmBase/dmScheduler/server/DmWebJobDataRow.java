package dataman.dmBase.dmScheduler.server;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.dmScheduler.shared.WebJobAttachment_Mdl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DmWebJobDataRow implements Serializable, IsSerializable {

    private Map<String, String> data = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private String msgFormat;
    private String formattedMessage;
    private String subFormat;
    private String formattedSubject;
    private long templateId;
    private String templateName;
    private String functionOrfileName;
    private DmEnum.DmWebJobDataSources dataSource;
    private DmEnum.DmWebJobTypes jobtype;
    private String activeDb;
    private List<WebJobAttachment_Mdl> lstAttachments = new ArrayList<WebJobAttachment_Mdl>();

    public void setActiveDB(String dbName) {
        this.activeDb = dbName.trim();
    }

    public String getActiveDB() {
        return this.activeDb;
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

    public DmEnum.DmWebJobDataSources getDataSource() {
        return dataSource;
    }

    public void setDataSource(DmEnum.DmWebJobDataSources dataSource) {
        this.dataSource = dataSource;
    }

    public DmEnum.DmWebJobTypes getJobtype() {
        return jobtype;
    }

    public void setJobtype(DmEnum.DmWebJobTypes jobtype) {
        this.jobtype = jobtype;
    }

    public Map<String, String> getDataRow() {
        return this.data;
    }

    public String getMessgageFormat() {
        return msgFormat;
    }

    public void setMessgageFormat(String msgFormat) {
        this.msgFormat = msgFormat;
    }

    public void setSubjectFormat(String subFormat) {
        this.subFormat = subFormat.trim();
    }

    public String getSubjectFormat() {
        return this.subFormat;
    }

    public String getMessageFromUnformatted(String messageToFormat) {
        String retValue = "";
        Map<String, String> params = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        for (Map.Entry<String, String> cells : this.data.entrySet()) {
            params.put(cells.getKey().toLowerCase(), cells.getValue());
        }
        retValue = DmMapFormat.format(messageToFormat, params);
        return retValue;
    }

    public String getFormattedMessage() {
        return this.formattedMessage;
    }

    public void setFormattedMessage(String formattedMessage) {
        this.formattedMessage = formattedMessage;
    }

    public String getFormattedSubject() {
        return this.formattedSubject;
    }

    public void setFormattedSubject(String formattedSubject) {
        this.formattedSubject = formattedSubject;
    }

    public List<WebJobAttachment_Mdl> getLstAttachments() {
        return lstAttachments;
    }

}
