package aarogya.selfToolkit.labRequisition.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHTest_Mdl extends KeyField implements Serializable, IsSerializable {

    private String categoryTag, categoryText, defaultDepartmentCode, defaultDepartmentName, siteCode, siteName;

    public String getCategoryTag() {
        return categoryTag;
    }

    public void setCategoryTag(String categoryTag) {
        this.categoryTag = categoryTag;
    }

    public String getCategoryText() {
        return categoryText;
    }

    public void setCategoryText(String categoryText) {
        this.categoryText = categoryText;
    }

    public String getDefaultDepartmentCode() {
        return defaultDepartmentCode;
    }

    public void setDefaultDepartmentCode(String defaultDepartmentCode) {
        this.defaultDepartmentCode = defaultDepartmentCode;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getDefaultDepartmentName() {
        return defaultDepartmentName;
    }

    public void setDefaultDepartmentName(String defaultDepartmentName) {
        this.defaultDepartmentName = defaultDepartmentName;
    }

}
