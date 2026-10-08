package aarogya.pharmacy.medicineIssue.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;

public class TxtHRequisitionNo_Mdl extends KeyField implements Serializable, IsSerializable {

    private String requisitionBy;
    private String requisitionByCode;
    private String requisitionDate;
    private String departmentCode;
    private String departmentName;
    private String departmentToCode;
    private String departmentToName;

    public TxtHRequisitionNo_Mdl() {
    }

    public String getRequisitionBy() {
        return requisitionBy;
    }

    public void setRequisitionBy(String requisitionBy) {
        this.requisitionBy = requisitionBy;
    }

    public String getRequisitionByCode() {
        return requisitionByCode;
    }

    public void setRequisitionByCode(String requisitionByCode) {
        this.requisitionByCode = requisitionByCode;
    }

    public String getRequisitionDate() {
        return requisitionDate;
    }

    public void setRequisitionDate(String requisitionDate) {
        this.requisitionDate = requisitionDate;
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

}
