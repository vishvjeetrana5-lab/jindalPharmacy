package aarogya.pharmacy.proformaPurchaseReturn.shared;

import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import dataman.dmBase.self.shared.RecId;
import dataman.dmToolkit.dmGUI.shared.DmGUI_Mdl;
import dataman.dmToolkit.overHead.shared.OverHead_Mdl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProformaPurchaseReturn_Mdl extends KeyField implements Serializable, IsSerializable {

    private RecId recIdProformaPurchaseReturn = new RecId();

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

    private String gstReasonCode;
    private String gstReasonName;

//    private String billNo;                 //  <MJ>
//    private String billNoTag; 
//    private String billDate;
//    private String partyBillNo;
//    private String partyBillDate;
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
    private String isCancelRecord;
    private String isMakeProforma;
    private DefaultValue_Mdl defaultValueMdl;
    private ModifyReason_Mdl modifyReasonMdl;
    private DmGUI_Mdl guiMdl = new DmGUI_Mdl();
    private List<GrdItemDetail_Mdl> listItemDetail = new ArrayList<GrdItemDetail_Mdl>();
    private List<OverHead_Mdl> overHeadList = new ArrayList<OverHead_Mdl>();

    public ProformaPurchaseReturn_Mdl() {
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

    public RecId getRecIdProformaPurchaseReturn() {
        return recIdProformaPurchaseReturn;
    }

    public void setRecIdProformaPurchaseReturn(RecId recIdProformaPurchaseReturn) {
        this.recIdProformaPurchaseReturn = recIdProformaPurchaseReturn;
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

    public String getIsCancelRecord() {
        return isCancelRecord;
    }

    public void setIsCancelRecord(String isCancelRecord) {
        this.isCancelRecord = isCancelRecord;
    }

    public String getIsMakeProforma() {
        return isMakeProforma;
    }

    public void setIsMakeProforma(String isMakeProforma) {
        this.isMakeProforma = isMakeProforma;
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

    public ProformaPurchaseReturn_Mdl clone() {
        ProformaPurchaseReturn_Mdl return_Mdl = new ProformaPurchaseReturn_Mdl();

        copy(return_Mdl);

        return return_Mdl;
    }

    private void copy(ProformaPurchaseReturn_Mdl ppr_Mdl) {
        ppr_Mdl.setSharedKeyFieldCode(this.getSharedKeyFieldCode());
        ppr_Mdl.setSharedKeyFieldName(this.getSharedKeyFieldName());
        ppr_Mdl.setRecIdProformaPurchaseReturn(this.getRecIdProformaPurchaseReturn()); // Not Correct Cloning
        ppr_Mdl.setVoucherTypeCode(this.getVoucherTypeCode());
        ppr_Mdl.setVoucherTypeName(this.getVoucherTypeName());
        ppr_Mdl.setVoucherDate(this.getVoucherDate());
        ppr_Mdl.setSupplierCode(this.getSupplierCode());
        ppr_Mdl.setSupplierName(this.getSupplierName());
        ppr_Mdl.setSupplierStateCode(this.getSupplierStateCode());
        ppr_Mdl.setSupplierGstin(this.getSupplierGstin());
        ppr_Mdl.setSupplierGstinDate(this.getSupplierGstinDate());
        ppr_Mdl.setPartyType(this.getPartyType());
        ppr_Mdl.setPartyTypeForEntry(this.getPartyTypeForEntry());
        ppr_Mdl.setRcmApplicable(this.getRcmApplicable());
        ppr_Mdl.setPlaceOfSupply(this.getPlaceOfSupply());

        ppr_Mdl.setGstReasonCode(this.getGstReasonCode());
        ppr_Mdl.setGstReasonName(this.getGstReasonName());

//        ppr_Mdl.setPartyBillNo(this.getPartyBillNo());
//        ppr_Mdl.setPartyBillDate(this.getPartyBillDate());
//        ppr_Mdl.setBillNo(this.getBillNo());
//        ppr_Mdl.setBillNoTag(this.getBillNoTag());
//        ppr_Mdl.setBillDate(this.getBillDate());
        ppr_Mdl.setRemark(this.getRemark());
        ppr_Mdl.setApprovedBy(this.getApprovedBy());
        ppr_Mdl.setApprovedDateTime(this.getApprovedDateTime());
        ppr_Mdl.setRoundOffAmount(this.getRoundOffAmount());
        ppr_Mdl.setWtaCode(this.getWtaCode());
        ppr_Mdl.setGuiConfigCode(this.getGuiConfigCode());
        ppr_Mdl.setU_EntDt_LatestLine(this.getU_EntDt_LatestLine());
        ppr_Mdl.setPbItemChangeCondition(this.getPbItemChangeCondition());
        ppr_Mdl.setGrossAmount(this.getGrossAmount());
        ppr_Mdl.setGuiMdl(this.getGuiMdl()); 
        ppr_Mdl.setIsMakeProforma(this.getIsMakeProforma()); 
        ppr_Mdl.getListItemDetail().addAll(cloneListItemDetail(this.getListItemDetail()));
        ppr_Mdl.getOverHeadList().addAll(OverHead_Mdl.getOHListClone(this.getOverHeadList()));
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
