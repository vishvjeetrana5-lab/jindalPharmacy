package aarogya.pharmacy.saleBillWholeSale.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdRequisition_Mdl extends KeyField implements Serializable, IsSerializable {

    private String requisitionBYCode;
    private String requisitionBYName;
    private String departmentCode;
    private String departmentName;

    public GrdRequisition_Mdl() {
    }

    public String getRequisitionBYCode() {
        return requisitionBYCode;
    }

    public void setRequisitionBYCode(String requisitionBYCode) {
        this.requisitionBYCode = requisitionBYCode;
    }

    public String getRequisitionBYName() {
        return requisitionBYName;
    }

    public void setRequisitionBYName(String requisitionBYName) {
        this.requisitionBYName = requisitionBYName;
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

}
