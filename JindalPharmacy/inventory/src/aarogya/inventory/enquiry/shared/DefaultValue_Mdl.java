package aarogya.inventory.enquiry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DefaultValue_Mdl implements Serializable, IsSerializable {

    private String enquiryTypeCode;
    private String enquiryTypeName;
    private String guiTemplate;
    private String departmentCode;
    private String departmentName;

    public DefaultValue_Mdl() {
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getEnquiryTypeCode() {
        return enquiryTypeCode;
    }

    public void setEnquiryTypeCode(String enquiryTypeCode) {
        this.enquiryTypeCode = enquiryTypeCode;
    }

    public String getEnquiryTypeName() {
        return enquiryTypeName;
    }

    public void setEnquiryTypeName(String enquiryTypeName) {
        this.enquiryTypeName = enquiryTypeName;
    }
    private String currentDate;

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getGuiTemplate() {
        return guiTemplate;
    }

    public void setGuiTemplate(String guiTemplate) {
        this.guiTemplate = guiTemplate;
    }

}
