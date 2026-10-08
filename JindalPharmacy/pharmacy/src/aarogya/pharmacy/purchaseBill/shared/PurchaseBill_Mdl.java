package aarogya.pharmacy.purchaseBill.shared;

import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmToolkit.dmGUI.shared.DmGUIRow_Mdl;
import dataman.dmToolkit.overHead.shared.OverHead_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGUI.shared.DmGUI_Mdl;
import dataman.dmToolkit.tdsCategoryPopup.shared.TdsCategoryPopup_Mdl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PurchaseBill_Mdl extends KeyField implements Serializable, IsSerializable {

    private RecId objRecId = new RecId();
    private ModifyReason_Mdl modifyReasonMdl;
    private String wtaCode;
    private String voucherTypeCode;
    private String voucherTypeName;
    private String voucherCategory;
    private String supplierTag;
    private String supplierText;
    private String supplierBillNo;
    private String supplierBillDate;

    private String supplierGstin;
    private String supplierGstinDate;
    private String supplierStateCode;
    private String partyType;
    private String partyTypeForEntry;
    private String rcmApplicable;
    private String placeOfSupply;
    private String u_EntDt_LatestLine;
    private String roundOffAmount;
    private String netAmount;

    private String voucherDate;
    private String paymentDueDate;
    private String calculateOnTag;
    private String remark;
    private String approvedBy;
    private String approvalDateTime;
    private String calculateOnText;
    private String guiConfigCode;
    private String grnItemChangeCondition;
    private String grossAmount;
    private String lastTransactionDate;
    private DefaultValue_Mdl defaultValueMdl;
    private String tdsAmt;
    private List<GrdItemDetail_Mdl> listItemDetail = new ArrayList<GrdItemDetail_Mdl>();
    private List<OverHead_Mdl> overHeadList = new ArrayList<OverHead_Mdl>();
    private DmGUI_Mdl guiMdl = new DmGUI_Mdl();
    private TdsCategoryPopup_Mdl tdsCatMdl = new TdsCategoryPopup_Mdl();

    public PurchaseBill_Mdl() {
    }

    public String getTdsAmt() {
        return tdsAmt;
    }

    public void setTdsAmt(String tdsAmt) {
        this.tdsAmt = tdsAmt;
    }

    public TdsCategoryPopup_Mdl getTdsCatMdl() {
        return tdsCatMdl;
    }

    public void setTdsCatMdl(TdsCategoryPopup_Mdl tdsCatMdl) {
        this.tdsCatMdl = tdsCatMdl;
    }

    public List<GrdItemDetail_Mdl> getListItemDetail() {
        return listItemDetail;
    }

    public List<OverHead_Mdl> getOverHeadList() {
        return overHeadList;
    }

    public RecId getObjRecId() {
        return objRecId;
    }

    public void setObjRecId(RecId objRecId) {
        this.objRecId = objRecId;
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

    public String getSupplierTag() {
        return supplierTag;
    }

    public void setSupplierTag(String supplierTag) {
        this.supplierTag = supplierTag;
    }

    public String getSupplierText() {
        return supplierText;
    }

    public void setSupplierText(String supplierText) {
        this.supplierText = supplierText;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getCalculateOnTag() {
        return calculateOnTag;
    }

    public void setCalculateOnTag(String calculateOnTag) {
        this.calculateOnTag = calculateOnTag;
    }

    public String getSupplierBillNo() {
        return supplierBillNo;
    }

    public void setSupplierBillNo(String supplierBillNo) {
        this.supplierBillNo = supplierBillNo;
    }

    public String getSupplierBillDate() {
        return supplierBillDate;
    }

    public void setSupplierBillDate(String supplierBillDate) {
        this.supplierBillDate = supplierBillDate;
    }

    public String getCalculateOnText() {
        return calculateOnText;
    }

    public void setCalculateOnText(String calculateOnText) {
        this.calculateOnText = calculateOnText;
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

    public String getApprovalDateTime() {
        return approvalDateTime;
    }

    public void setApprovalDateTime(String approvalDateTime) {
        this.approvalDateTime = approvalDateTime;
    }

    public String getWtaCode() {
        return wtaCode;
    }

    public void setWtaCode(String wtaCode) {
        this.wtaCode = wtaCode;
    }

    public DmGUI_Mdl getGuiMdl() {
        return guiMdl;
    }

    public void setGuiMdl(DmGUI_Mdl guiMdl) {
        this.guiMdl = guiMdl;
    }

    public ModifyReason_Mdl getModifyReasonMdl() {
        return modifyReasonMdl;
    }

    public void setModifyReasonMdl(ModifyReason_Mdl modifyReasonMdl) {
        this.modifyReasonMdl = modifyReasonMdl;
    }

    public String getGuiConfigCode() {
        return guiConfigCode;
    }

    public void setGuiConfigCode(String guiConfigCode) {
        this.guiConfigCode = guiConfigCode;
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

    public String getU_EntDt_LatestLine() {
        return u_EntDt_LatestLine;
    }

    public void setU_EntDt_LatestLine(String u_EntDt_LatestLine) {
        this.u_EntDt_LatestLine = u_EntDt_LatestLine;
    }

    public String getRoundOffAmount() {
        return roundOffAmount;
    }

    public void setRoundOffAmount(String roundOffAmount) {
        this.roundOffAmount = roundOffAmount;
    }

    public String getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(String netAmount) {
        this.netAmount = netAmount;
    }

    public String getGrnItemChangeCondition() {
        return grnItemChangeCondition;
    }

    public void setGrnItemChangeCondition(String grnItemChangeCondition) {
        this.grnItemChangeCondition = grnItemChangeCondition;
    }

    public String getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(String grossAmount) {
        this.grossAmount = grossAmount;
    }

    public DefaultValue_Mdl getDefaultValueMdl() {
        return defaultValueMdl;
    }

    public void setDefaultValueMdl(DefaultValue_Mdl defaultValueMdl) {
        this.defaultValueMdl = defaultValueMdl;
    }

    public PurchaseBill_Mdl clone() {
        PurchaseBill_Mdl grn_Mdl = new PurchaseBill_Mdl();

        copy(grn_Mdl);

        return grn_Mdl;
    }

    public void copy(PurchaseBill_Mdl grn_Mdl) {
        grn_Mdl.setSharedKeyFieldCode(this.getSharedKeyFieldCode());
        grn_Mdl.setSharedKeyFieldName(this.getSharedKeyFieldName());
        grn_Mdl.setApprovalDateTime(this.getApprovalDateTime());
        grn_Mdl.setApprovedBy(this.getApprovedBy());
        grn_Mdl.setCalculateOnTag(this.getCalculateOnTag());
        grn_Mdl.setCalculateOnText(this.getCalculateOnText());
        grn_Mdl.setGuiMdl(this.getGuiMdl());
        grn_Mdl.setModifyReasonMdl(this.getModifyReasonMdl());
        grn_Mdl.setObjRecId(this.getObjRecId());
        grn_Mdl.setRemark(this.getRemark());
        grn_Mdl.setSupplierBillDate(this.getSupplierBillDate());
        grn_Mdl.setSupplierBillNo(this.getSupplierBillNo());
        grn_Mdl.setSupplierTag(this.getSupplierTag());
        grn_Mdl.setSupplierText(this.getSupplierText());
        grn_Mdl.setVoucherDate(this.getVoucherDate());
        grn_Mdl.setVoucherTypeCode(this.getVoucherTypeCode());
        grn_Mdl.setVoucherTypeName(this.getVoucherTypeName());
        grn_Mdl.setVoucherCategory(this.getVoucherCategory());
        grn_Mdl.setWtaCode(this.getWtaCode());
        grn_Mdl.setGuiConfigCode(this.getGuiConfigCode());
        grn_Mdl.setSupplierGstin(this.getSupplierGstin());
        grn_Mdl.setSupplierGstinDate(this.getSupplierGstinDate());
        grn_Mdl.setSupplierStateCode(this.getSupplierStateCode());
        grn_Mdl.setPartyType(this.getPartyType());
        grn_Mdl.setPartyTypeForEntry(this.getPartyTypeForEntry());
        grn_Mdl.setRcmApplicable(this.getRcmApplicable());
        grn_Mdl.setPlaceOfSupply(this.getPlaceOfSupply());
        grn_Mdl.setU_EntDt_LatestLine(this.getU_EntDt_LatestLine());
        grn_Mdl.setRoundOffAmount(this.getRoundOffAmount());
        grn_Mdl.setNetAmount(this.getNetAmount());
        grn_Mdl.setGrnItemChangeCondition(this.getGrnItemChangeCondition());
        grn_Mdl.setGrossAmount(this.getGrossAmount());
        grn_Mdl.setDefaultValueMdl(this.getDefaultValueMdl());

        grn_Mdl.getListItemDetail().addAll(cloneGrdGrnDetailList(this.getListItemDetail()));
    }

    public List<GrdItemDetail_Mdl> cloneGrdGrnDetailList(List<GrdItemDetail_Mdl> list) {
        List<GrdItemDetail_Mdl> grdGrn_MdlList = null;

        if (list != null) {
            grdGrn_MdlList = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                grdGrn_MdlList.add(list.get(i).clone());
            }
        }

        return grdGrn_MdlList;
    }

    public String getPaymentDueDate() {
        return paymentDueDate;
    }

    public void setPaymentDueDate(String paymentDueDate) {
        this.paymentDueDate = paymentDueDate;
    }

    public String getLastTransactionDate() {
        return lastTransactionDate;
    }

    public void setLastTransactionDate(String lastTransactionDate) {
        this.lastTransactionDate = lastTransactionDate;
    }

}
