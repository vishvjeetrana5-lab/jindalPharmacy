package aarogya.pharmacy.requisition.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DefaultValue_Mdl implements Serializable, IsSerializable {

    private String departmentCode;
    private String departmentName;
    private String requisitionTypeCode;
    private String requisitionTypeName;
    private String requisitionByCode;
    private String requisitionByName;
    private String currentDate;
    private String currentTime;

    private String defaultPurchaseDepartment;
    private String defaultPurchaseDepartmentText;
    
    public DefaultValue_Mdl() {
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getRequisitionTypeCode() {
        return requisitionTypeCode;
    }

    public void setRequisitionTypeCode(String requisitionTypeCode) {
        this.requisitionTypeCode = requisitionTypeCode;
    }

    public String getRequisitionTypeName() {
        return requisitionTypeName;
    }

    public void setRequisitionTypeName(String requisitionTypeName) {
        this.requisitionTypeName = requisitionTypeName;
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

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getRequisitionByCode() {
        return requisitionByCode;
    }

    public void setRequisitionByCode(String requisitionByCode) {
        this.requisitionByCode = requisitionByCode;
    }

    public String getRequisitionByName() {
        return requisitionByName;
    }

    public void setRequisitionByName(String requisitionByName) {
        this.requisitionByName = requisitionByName;
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
