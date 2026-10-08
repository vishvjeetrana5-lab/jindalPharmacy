package aarogya.pharmacy.purchaseReturn.shared;

import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmToolkit.overHead.shared.OverHead_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.RecId;
import dataman.dmToolkit.dmGUI.shared.DmGUIRow_Mdl;
import dataman.dmToolkit.dmGUI.shared.DmGUI_Mdl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PurchaseReturn_Mdl extends KeyField implements Serializable, IsSerializable {

    private RecId recIdPurchaseReturn = new RecId();

    private String voucherTypeCode;
    private String voucherTypeName;
    private String voucherCategory;
    private String voucherDate;
    private String supplierCode;
    private String supplierName;
    private String supplierStateCode;
    private String supplierGstin;
    private String supplierGstinDate;
    private String partyType;
    private String partyTypeForEntry;
    private String rcmApplicable;
    private String placeOfSupply;

//    private String partyBillNo;
//    private String partyBillDate;
    private String departmentCode;
    private String departmentName;
    private String gstReasonCode;
    private String gstReasonName;
    private String proformaBillNo;
    private String proformaBillNoTag;
    private String proformaBillDate;
    private String remark;
    private String approvedBy;
    private String approvedDateTime;
    private String roundOffAmount;
    private String wtaCode;
    private String guiConfigCode;
    private String u_EntDt_LatestLine;
    private String pbItemChangeCondition;
    private String grossAmount;
    private String netAmount;
    private DefaultValue_Mdl defaultValueMdl;
    private ModifyReason_Mdl modifyReasonMdl;
    private DmGUI_Mdl guiMdl = new DmGUI_Mdl();
    private List<GrdItemDetail_Mdl> listItemDetail = new ArrayList<GrdItemDetail_Mdl>();
    private List<OverHead_Mdl> overHeadList = new ArrayList<OverHead_Mdl>();

    public PurchaseReturn_Mdl() {
    }

    public ModifyReason_Mdl getModifyReasonMdl() {
        return modifyReasonMdl;
    }

    public void setModifyReasonMdl(ModifyReason_Mdl modifyReasonMdl) {
        this.modifyReasonMdl = modifyReasonMdl;
    }

    public List<GrdItemDetail_Mdl> getListItemDetail() {
        return listItemDetail;
    }

    public List<OverHead_Mdl> getOverHeadList() {
        return overHeadList;
    }

    public RecId getRecIdPurchaseReturn() {
        return recIdPurchaseReturn;
    }

    public void setRecIdPurchaseReturn(RecId recIdPurchaseReturn) {
        this.recIdPurchaseReturn = recIdPurchaseReturn;
    }

    public String getVoucherTypeCode() {
        return voucherTypeCode;
    }

    public void setVoucherTypeCode(String voucherTypeCode) {
        this.voucherTypeCode = voucherTypeCode;
    }

    public String getVoucherTypeName() {
        return voucherTypeName;
    }

    public void setVoucherTypeName(String voucherTypeName) {
        this.voucherTypeName = voucherTypeName;
    }

    public String getVoucherCategory() {
        return voucherCategory;
    }

    public void setVoucherCategory(String voucherCategory) {
        this.voucherCategory = voucherCategory;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierGstin() {
        return supplierGstin;
    }

    public void setSupplierGstin(String supplierGstin) {
        this.supplierGstin = supplierGstin;
    }

    public String getSupplierGstinDate() {
        return supplierGstinDate;
    }

    public void setSupplierGstinDate(String supplierGstinDate) {
        this.supplierGstinDate = supplierGstinDate;
    }

    public String getSupplierStateCode() {
        return supplierStateCode;
    }

    public void setSupplierStateCode(String supplierStateCode) {
        this.supplierStateCode = supplierStateCode;
    }

    public String getPartyType() {
        return partyType;
    }

    public void setPartyType(String partyType) {
        this.partyType = partyType;
    }

    public String getPartyTypeForEntry() {
        return partyTypeForEntry;
    }

    public void setPartyTypeForEntry(String partyTypeForEntry) {
        this.partyTypeForEntry = partyTypeForEntry;
    }

    public String getRcmApplicable() {
        return rcmApplicable;
    }

    public void setRcmApplicable(String rcmApplicable) {
        this.rcmApplicable = rcmApplicable;
    }

    public String getPlaceOfSupply() {
        return placeOfSupply;
    }

    public void setPlaceOfSupply(String placeOfSupply) {
        this.placeOfSupply = placeOfSupply;
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

    public String getGstReasonCode() {
        return gstReasonCode;
    }

    public void setGstReasonCode(String gstReasonCode) {
        this.gstReasonCode = gstReasonCode;
    }

    public String getGstReasonName() {
        return gstReasonName;
    }

    public void setGstReasonName(String gstReasonName) {
        this.gstReasonName = gstReasonName;
    }

    public String getProformaBillNo() {
        return proformaBillNo;
    }

    public void setProformaBillNo(String proformaBillNo) {
        this.proformaBillNo = proformaBillNo;
    }

    public String getProformaBillNoTag() {
        return proformaBillNoTag;
    }

    public void setProformaBillNoTag(String proformaBillNoTag) {
        this.proformaBillNoTag = proformaBillNoTag;
    }

    public String getProformaBillDate() {
        return proformaBillDate;
    }

    public void setProformaBillDate(String proformaBillDate) {
        this.proformaBillDate = proformaBillDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getApprovedDateTime() {
        return approvedDateTime;
    }

    public void setApprovedDateTime(String approvedDateTime) {
        this.approvedDateTime = approvedDateTime;
    }

    public String getRoundOffAmount() {
        return roundOffAmount;
    }

    public void setRoundOffAmount(String roundOffAmount) {
        this.roundOffAmount = roundOffAmount;
    }

    public String getWtaCode() {
        return wtaCode;
    }

    public void setWtaCode(String wtaCode) {
        this.wtaCode = wtaCode;
    }

    public String getGuiConfigCode() {
        return guiConfigCode;
    }

    public void setGuiConfigCode(String guiConfigCode) {
        this.guiConfigCode = guiConfigCode;
    }

    public String getU_EntDt_LatestLine() {
        return u_EntDt_LatestLine;
    }

    public void setU_EntDt_LatestLine(String u_EntDt_LatestLine) {
        this.u_EntDt_LatestLine = u_EntDt_LatestLine;
    }

    public String getPbItemChangeCondition() {
        return pbItemChangeCondition;
    }

    public void setPbItemChangeCondition(String pbItemChangeCondition) {
        this.pbItemChangeCondition = pbItemChangeCondition;
    }

    public String getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(String grossAmount) {
        this.grossAmount = grossAmount;
    }

    public String getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(String netAmount) {
        this.netAmount = netAmount;
    }

    public DefaultValue_Mdl getDefaultValueMdl() {
        return defaultValueMdl;
    }

    public void setDefaultValueMdl(DefaultValue_Mdl defaultValueMdl) {
        this.defaultValueMdl = defaultValueMdl;
    }

    public DmGUI_Mdl getGuiMdl() {
        return guiMdl;
    }

    public void setGuiMdl(DmGUI_Mdl guiMdl) {
        this.guiMdl = guiMdl;
    }

    public PurchaseReturn_Mdl clone() {
        PurchaseReturn_Mdl return_Mdl = new PurchaseReturn_Mdl();

        copy(return_Mdl);

        return return_Mdl;
    }

    private void copy(PurchaseReturn_Mdl grn_Mdl) {
        grn_Mdl.setSharedKeyFieldCode(this.getSharedKeyFieldCode());
        grn_Mdl.setSharedKeyFieldName(this.getSharedKeyFieldName());
        grn_Mdl.setRecIdPurchaseReturn(this.getRecIdPurchaseReturn()); // Not Correct Cloning
        grn_Mdl.setVoucherTypeCode(this.getVoucherTypeCode());
        grn_Mdl.setVoucherTypeName(this.getVoucherTypeName());
        grn_Mdl.setVoucherDate(this.getVoucherDate());
        grn_Mdl.setSupplierCode(this.getSupplierCode());
        grn_Mdl.setSupplierName(this.getSupplierName());
        grn_Mdl.setSupplierStateCode(this.getSupplierStateCode());
        grn_Mdl.setSupplierGstin(this.getSupplierGstin());
        grn_Mdl.setSupplierGstinDate(this.getSupplierGstinDate());
        grn_Mdl.setPartyType(this.getPartyType());
        grn_Mdl.setPartyTypeForEntry(this.getPartyTypeForEntry());
        grn_Mdl.setRcmApplicable(this.getRcmApplicable());
        grn_Mdl.setPlaceOfSupply(this.getPlaceOfSupply());

        grn_Mdl.setDepartmentCode(this.getDepartmentCode());
        grn_Mdl.setDepartmentName(this.getDepartmentName());
        grn_Mdl.setGstReasonCode(this.getGstReasonCode());
        grn_Mdl.setGstReasonName(this.getGstReasonName());
        grn_Mdl.setProformaBillNo(this.getProformaBillNo());
        grn_Mdl.setProformaBillNoTag(this.getProformaBillNoTag());
        grn_Mdl.setProformaBillDate(this.getProformaBillDate());
        grn_Mdl.setRemark(this.getRemark());
        grn_Mdl.setApprovedBy(this.getApprovedBy());
        grn_Mdl.setApprovedDateTime(this.getApprovedDateTime());
        grn_Mdl.setRoundOffAmount(this.getRoundOffAmount());
        grn_Mdl.setWtaCode(this.getWtaCode());
        grn_Mdl.setGuiConfigCode(this.getGuiConfigCode());
        grn_Mdl.setU_EntDt_LatestLine(this.getU_EntDt_LatestLine());
        grn_Mdl.setPbItemChangeCondition(this.getPbItemChangeCondition());
        grn_Mdl.setGrossAmount(this.getGrossAmount());
        grn_Mdl.setGuiMdl(this.getGuiMdl()); // Not Correct Cloning
        grn_Mdl.getListItemDetail().addAll(cloneListItemDetail(this.getListItemDetail()));
        grn_Mdl.getOverHeadList().addAll(OverHead_Mdl.getOHListClone(this.getOverHeadList()));
    }

    private List<GrdItemDetail_Mdl> cloneListItemDetail(List<GrdItemDetail_Mdl> list) {
        List<GrdItemDetail_Mdl> newList = null;

        if (list != null) {
            newList = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                newList.add(list.get(i).clone());
            }
        }

        return newList;
    }
}
