package dataman.dmBase.dmFileManager.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DmFileRuleType implements Serializable, IsSerializable {

    private String ruleCode;
    private String fileTypeName;
    private String fileSizeLimit;
    private boolean defaultRuleCode;
    private String filters;
    private String sessionId;
    private List<DmAttachment_Mdl> listFiles;

    public DmFileRuleType() {
        listFiles = new ArrayList<>();
    }

    public String getFileTypeName() {
        return fileTypeName;
    }

    public void setFileTypeName(String fileTypeName) {
        this.fileTypeName = fileTypeName;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public String getFileSizeLimit() {
        return fileSizeLimit;
    }

    public void setFileSizeLimit(String fileSizeLimit) {
        this.fileSizeLimit = fileSizeLimit;
    }

    public boolean IsDefaultRuleCode() {
        return defaultRuleCode;
    }

    public void setDefaultRuleCode(boolean defaultRuleCode) {
        this.defaultRuleCode = defaultRuleCode;
    }

    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public List<DmAttachment_Mdl> getListFiles() {
        return listFiles;
    }
    
}
