package aarogya.pharmacy.medicineTransferWholeSale.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MedicineIssueWithRequisition_Mdl implements Serializable, IsSerializable {

    private String requisitionByName, requisitionByCode, departmentToName, departmentToCode;
    private String requisitionCode;
    private String requisitionName;
    List<GrdMedicineDetail_Mdl> grdItemDetailList = new ArrayList<>();

    public MedicineIssueWithRequisition_Mdl() {
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

    public String getDepartmentToName() {
        return departmentToName;
    }

    public void setDepartmentToName(String departmentToName) {
        this.departmentToName = departmentToName;
    }

    public String getDepartmentToCode() {
        return departmentToCode;
    }

    public void setDepartmentToCode(String departmentToCode) {
        this.departmentToCode = departmentToCode;
    }

    public String getRequisitionCode() {
        return requisitionCode;
    }

    public void setRequisitionCode(String requisitionCode) {
        this.requisitionCode = requisitionCode;
    }

    public String getRequisitionName() {
        return requisitionName;
    }

    public void setRequisitionName(String requisitionName) {
        this.requisitionName = requisitionName;
    }

    public List<GrdMedicineDetail_Mdl> getGrdItemDetailList() {
        return grdItemDetailList;
    }

    public void setGrdItemDetailList(List<GrdMedicineDetail_Mdl> grdItemDetailList) {
        this.grdItemDetailList = grdItemDetailList;
    }

}
