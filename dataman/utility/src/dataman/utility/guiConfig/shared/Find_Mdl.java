package dataman.utility.guiConfig.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String templateName, tableName, reportPrefix, applicableSite;

    public Find_Mdl() {
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getReportPrefix() {
        return reportPrefix;
    }

    public void setReportPrefix(String reportPrefix) {
        this.reportPrefix = reportPrefix;
    }

    public String getApplicableSite() {
        return applicableSite;
    }

    public void setApplicableSite(String applicableSite) {
        this.applicableSite = applicableSite;
    }
}
