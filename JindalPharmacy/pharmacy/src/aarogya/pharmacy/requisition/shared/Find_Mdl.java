package aarogya.pharmacy.requisition.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String requisitionNo;
    private String date;
    private String departmentBy;
    private String departmentTo;
    private String subName;
    private String approval;

    public Find_Mdl() {
    }

    public String getRequisitionNo() {
        return requisitionNo;
    }

    public void setRequisitionNo(String requisitionNo) {
        this.requisitionNo = requisitionNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDepartmentBy() {
        return departmentBy;
    }

    public void setDepartmentBy(String departmentBy) {
        this.departmentBy = departmentBy;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getApproval() {
        return approval;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }

    public String getDepartmentTo() {
        return departmentTo;
    }

    public void setDepartmentTo(String departmentTo) {
        this.departmentTo = departmentTo;
    }

}
