package aarogya.dashBoard.requisitionInventory.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdRequisitionInventory_Mdl extends KeyField implements Serializable, IsSerializable {

    private String V_No;
    private String requisitionByName;
    private String requisitionByCode;
    private String departmentCode;
    private String departmentName;
    private String date;
    private String reqDateTime;
    private String departmentToCode;
    private String departmentToName;
    private String priorityColor;
    private String v_Type;
    private String v_TypeCat;

    public GrdRequisitionInventory_Mdl() {
    }

    public String getReqDateTime() {
        return reqDateTime;
    }

    public void setReqDateTime(String reqDateTime) {
        this.reqDateTime = reqDateTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getV_No() {
        return V_No;
    }

    public void setV_No(String V_No) {
        this.V_No = V_No;
    }

    public String getRequisitionByName() {
        return requisitionByName;
    }

    public void setRequisitionByName(String requisitionByName) {
        this.requisitionByName = requisitionByName;
    }

    public String getRequisitionByCode() {
        return requisitionByCode;
    }

    public void setRequisitionByCode(String requisitionByCode) {
        this.requisitionByCode = requisitionByCode;
    }

    public String getDepartmentToCode() {
        return departmentToCode;
    }

    public void setDepartmentToCode(String departmentToCode) {
        this.departmentToCode = departmentToCode;
    }

    public String getDepartmentToName() {
        return departmentToName;
    }

    public void setDepartmentToName(String departmentToName) {
        this.departmentToName = departmentToName;
    }

    public String getPriorityColor() {
        return priorityColor;
    }

    public void setPriorityColor(String priorityColor) {
        this.priorityColor = priorityColor;
    }

    public String getV_Type() {
        return v_Type;
    }

    public void setV_Type(String v_Type) {
        this.v_Type = v_Type;
    }

    public String getV_TypeCat() {
        return v_TypeCat;
    }

    public void setV_TypeCat(String v_TypeCat) {
        this.v_TypeCat = v_TypeCat;
    }

}
