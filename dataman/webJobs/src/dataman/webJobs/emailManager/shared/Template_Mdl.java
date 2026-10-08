package dataman.webJobs.emailManager.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Template_Mdl extends KeyField implements Serializable, IsSerializable {

    private String templateGroupText;
    private String templateGroupTag;
    private String funcOrFileName;
    private String messageTemplate;
    private String subjectTemplate;
    private String sessionTable;
    private DmWebJobDataSources webJobDataSource = DmWebJobDataSources.FUNCTION;

    public Template_Mdl() {
    }

    public DmWebJobDataSources getWebJobDataSource() {
        return webJobDataSource;
    }

    public String getTemplateGroupText() {
        return templateGroupText;
    }

    public void setTemplateGroupText(String templateGroupText) {
        this.templateGroupText = templateGroupText;
    }

    public String getTemplateGroupTag() {
        return templateGroupTag;
    }

    public void setTemplateGroupTag(String templateGroupTag) {
        this.templateGroupTag = templateGroupTag;
    }

    public String getFuncOrFileName() {
        return funcOrFileName;
    }

    public void setFuncOrFileName(String funcOrFileName) {
        this.funcOrFileName = funcOrFileName;
    }

    public String getMessageTemplate() {
        return messageTemplate;
    }

    public void setMessageTemplate(String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    public void setWebJobDataSource(DmWebJobDataSources webJobDataSource) {
        this.webJobDataSource = webJobDataSource;
    }

    public String getSubjectTemplate() {
        return subjectTemplate;
    }

    public void setSubjectTemplate(String subjectTemplate) {
        this.subjectTemplate = subjectTemplate;
    }

    public String getSessionTable() {
        return sessionTable;
    }

    public void setSessionTable(String sessionTable) {
        this.sessionTable = sessionTable;
    }

}
