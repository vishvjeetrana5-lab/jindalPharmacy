package aarogya.selfToolkit.D365_To_His_DataSync.shared;

import aarogya.selfToolkit.toolTip.shared.ToolTip_Mdl;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GrdMedicineDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    private String vSno;
    private String dm_itemCode;
    private String qty;
    private String reqDate;
    private String remarks;
    private String approval;
    private String srNo;
    private String isStatus;
    private String approvedBy;
    private String unitName;
    private String decimalPlace;
    private String requisitionDocId;
    private String requisitionNo;
    private String requisitionVsNo;
    private String requisitionFor;
    private String requisitionDate;
    private String mrp;
    private String dmStatus;

    private List<ToolTip_Mdl> data = new ArrayList<>();

    private List<KeyFieldExtended> lstOldRequisition = new ArrayList();

    public String getRequisitionFor() {
        return requisitionFor;
    }

    public String getRequisitionDate() {
        return requisitionDate;
    }

    public void setRequisitionDate(String requisitionDate) {
        this.requisitionDate = requisitionDate;
    }

    public void setRequisitionFor(String requisitionFor) {
        this.requisitionFor = requisitionFor;
    }

    public List<ToolTip_Mdl> getData() {
        return data;
    }

    public void setData(List<ToolTip_Mdl> data) {
        this.data = data;
    }

    public GrdMedicineDetail_Mdl() {
    }

    public String getRequisitionDocId() {
        return requisitionDocId;
    }

    public void setRequisitionDocId(String requisitionDocId) {
        this.requisitionDocId = requisitionDocId;
    }

    public String getRequisitionNo() {
        return requisitionNo;
    }

    public void setRequisitionNo(String requisitionNo) {
        this.requisitionNo = requisitionNo;
    }

    public String getReqDate() {
        return reqDate;
    }

    public void setReqDate(String reqDate) {
        this.reqDate = reqDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getApproval() {
        return approval;
    }

    public void setApproval(String Approval) {
        this.approval = Approval;
    }

    public String getSrNo() {
        return srNo;
    }

    public void setSrNo(String srNo) {
        this.srNo = srNo;
    }

    public String getIsStatus() {
        return isStatus;
    }

    public void setIsStatus(String isStatus) {
        this.isStatus = isStatus;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getDecimalPlace() {
        return decimalPlace;
    }

    public void setDecimalPlace(String decimalPlace) {
        this.decimalPlace = decimalPlace;
    }

    public String getvSno() {
        return vSno;
    }

    public void setvSno(String vSno) {
        this.vSno = vSno;
    }

    public String getRequisitionVsNo() {
        return requisitionVsNo;
    }

    public void setRequisitionVsNo(String requisitionVsNo) {
        this.requisitionVsNo = requisitionVsNo;
    }

    public List<KeyFieldExtended> getLstOldRequisition() {
        return lstOldRequisition;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getDm_itemCode() {
        return dm_itemCode;
    }

    public void setDm_itemCode(String dm_itemCode) {
        this.dm_itemCode = dm_itemCode;
    }

    public String getDmStatus() {
        return dmStatus;
    }

    public void setDmStatus(String dmStatus) {
        this.dmStatus = dmStatus;
    }

}
