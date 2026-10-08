package dataman.utility.guiConfig.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GUIConfig_Mdl extends KeyField implements Serializable, IsSerializable {

    private String tempKeyCode;
    private String templateCode, templateName, tableName, sqlQuery;
    private String reportPrefix;
    private List<GUIRow_Mdl> grdData = new ArrayList<GUIRow_Mdl>();
    private List<KeyFieldExtended> grdSite = new ArrayList<KeyFieldExtended>();

    public GUIConfig_Mdl() {
    }

    public String getTempKeyCode() {
        return tempKeyCode;
    }

    public void setTempKeyCode(String tempKeyCode) {
        this.tempKeyCode = tempKeyCode;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public List<GUIRow_Mdl> getGrdData() {
        return grdData;
    }

    public List<KeyFieldExtended> getGrdSite() {
        return grdSite;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSqlQuery() {
        return sqlQuery;
    }

    public void setSqlQuery(String sqlQuery) {
        this.sqlQuery = sqlQuery;
    }

    public String getReportPrefix() {
        return reportPrefix;
    }

    public void setReportPrefix(String reportPrefix) {
        this.reportPrefix = reportPrefix;
    }
}
