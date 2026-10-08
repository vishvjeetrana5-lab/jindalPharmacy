package aarogya.inventory.itemTransfer.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ItemIssueWithRequisition_Mdl implements Serializable, IsSerializable {

    private String requisitionByName, requisitionByCode, departmentToName, departmentToCode;
    private String requisitionCode;
    private String requisitionName;
    List<GrdItemDetail_Mdl> grdItemDetailList = new ArrayList<>();

    public ItemIssueWithRequisition_Mdl() {
    }

    public String getRequisitionName() {
        return requisitionName;
    }

    public void setRequisitionName(String requisitionName) {
        this.requisitionName = requisitionName;
    }

    public String getRequisitionCode() {
        return requisitionCode;
    }

    public void setRequisitionCode(String requisitionCode) {
        this.requisitionCode = requisitionCode;
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

//    public List<SelectedRequisition_Mdl> getSelectedRequisitionList() {
//        return selectedRequisitionList;
//    }
//
//    public void setSelectedRequisitionList(List<SelectedRequisition_Mdl> selectedRequisitionList) {
//        this.selectedRequisitionList = selectedRequisitionList;
//    }
    public List<GrdItemDetail_Mdl> getGrdItemDetailList() {
        return grdItemDetailList;
    }

    public void setGrdItemDetailList(List<GrdItemDetail_Mdl> grdItemDetailList) {
        this.grdItemDetailList = grdItemDetailList;
    }

}
