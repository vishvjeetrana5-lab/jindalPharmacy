package aarogya.inventory.indent.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DefaultValue_Mdl implements Serializable, IsSerializable {

    private String indentTypeTag, indentTypeText;
    private String departmentTag, departmentText;
    private String currentDate;
    private String currentTime;
    private String defaultPurchaseDepartment;
    private String defaultPurchaseDepartmentText;

    public DefaultValue_Mdl() {
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getIndentTypeTag() {
        return indentTypeTag;
    }

    public void setIndentTypeTag(String indentTypeTag) {
        this.indentTypeTag = indentTypeTag;
    }

    public String getIndentTypeText() {
        return indentTypeText;
    }

    public void setIndentTypeText(String indentTypeText) {
        this.indentTypeText = indentTypeText;
    }

    public String getDepartmentTag() {
        return departmentTag;
    }

    public void setDepartmentTag(String departmentTag) {
        this.departmentTag = departmentTag;
    }

    public String getDepartmentText() {
        return departmentText;
    }

    public void setDepartmentText(String departmentText) {
        this.departmentText = departmentText;
    }

    public String getDefaultPurchaseDepartment() {
        return defaultPurchaseDepartment;
    }

    public void setDefaultPurchaseDepartment(String defaultPurchaseDepartment) {
        this.defaultPurchaseDepartment = defaultPurchaseDepartment;
    }

    public String getDefaultPurchaseDepartmentText() {
        return defaultPurchaseDepartmentText;
    }

    public void setDefaultPurchaseDepartmentText(String defaultPurchaseDepartmentText) {
        this.defaultPurchaseDepartmentText = defaultPurchaseDepartmentText;
    }

}
