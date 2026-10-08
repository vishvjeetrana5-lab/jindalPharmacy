package dataman.webJobs.emailManager.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchValue_Mdl implements Serializable, IsSerializable {

    private String funOrFileName;
    private String templateId;
    private String messageTemplate;
    private String subjectTemplate;
    private String sessionTable;
    private List<FilterDataPopup_Mdl> filterDataList = new ArrayList<>();

    public SearchValue_Mdl() {
    }

    public String getFunOrFileName() {
        return funOrFileName;
    }

    public void setFunOrFileName(String funOrFileName) {
        this.funOrFileName = funOrFileName;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getMessageTemplate() {
        return messageTemplate;
    }

    public void setMessageTemplate(String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    public String getSessionTable() {
        return sessionTable;
    }

    public void setSessionTable(String sessionTable) {
        this.sessionTable = sessionTable;
    }

    public String getSubjectTemplate() {
        return subjectTemplate;
    }

    public void setSubjectTemplate(String subjectTemplate) {
        this.subjectTemplate = subjectTemplate;
    }

    public List<FilterDataPopup_Mdl> getFilterDataList() {
        return filterDataList;
    }
}
