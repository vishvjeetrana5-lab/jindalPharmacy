package aarogya.inventory.workBillDirect.shared;

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

public class WorkBillDirect_Mdl extends KeyField implements Serializable, IsSerializable {

    private RecId objRecId = new RecId();
    private String wtaCode;
    private String voucherTypeCode;
    private String voucherTypeName;
    private String voucherCategory;
    private String voucherDate;
    private String supplierGstin;
    private String supplierGstinDate;
    private String supplierStateCode;
    private String supplierTag;
    private String supplierText;
    private String supplierBillNo;
    private String supplierBillDate;
    private String calculateOnTag;
    private String calculateOnText;
    private String remark;
    private String departmentTag;
    private String departmentText;
    private String approvedBy;
    private String approvedDateTime;
    private String partyTypeForEntry;
    private String partyType;
    private String rcmApplicable;
    private String placeOfSupply;
    private String roundOffAmount;
    private ModifyReason_Mdl modifyReasonMdl;
    private String guiConfigCode;
    private String u_EntDt_LatestLine;
    private String lineItemCodesCsv;
    private String poItemChangeCondition;
    private String grossAmount;
    private boolean isPurchaseOrderExists;
    private DefaultValue_Mdl defaultValueMdl;
    private DmGUI_Mdl guiMdl = new DmGUI_Mdl();
    private List<GrdGrnDetail_Mdl> grdGrnDetail = new ArrayList<GrdGrnDetail_Mdl>();
    private List<OverHead_Mdl> overHeadList = new ArrayList<OverHead_Mdl>();

    public WorkBillDirect_Mdl() {
    }

    public WorkBillDirect_Mdl clone() {
        WorkBillDirect_Mdl grn_Mdl = new WorkBillDirect_Mdl();

        copy(grn_Mdl);

        return grn_Mdl;
    }

    public void copy(WorkBillDirect_Mdl grn_Mdl) {
        grn_Mdl.setSharedKeyFieldCode(this.getSharedKeyFieldCode());
        grn_Mdl.setSharedKeyFieldName(this.getSharedKeyFieldName());
        grn_Mdl.setApprovedBy(this.getApprovedBy());
        grn_Mdl.setApprovedDateTime(this.getApprovedDateTime());
        grn_Mdl.setDepartmentTag(this.getDepartmentTag());
        grn_Mdl.setDepartmentText(this.getDepartmentText());
        grn_Mdl.setGuiMdl(this.getGuiMdl());
        grn_Mdl.setGuiConfigCode(this.getGuiConfigCode());
        grn_Mdl.setU_EntDt_LatestLine(this.getU_EntDt_LatestLine());
        grn_Mdl.setLineItemCodesCsv(this.getLineItemCodesCsv());
        grn_Mdl.setPoItemChangeCondition(this.getPoItemChangeCondition());
        grn_Mdl.setModifyReasonMdl(this.getModifyReasonMdl());
        grn_Mdl.setObjRecId(this.getObjRecId());
        grn_Mdl.setRemark(this.getRemark());
        grn_Mdl.setSupplierTag(this.getSupplierTag());
        grn_Mdl.setSupplierText(this.getSupplierText());
        grn_Mdl.setSupplierBillNo(this.getSupplierBillNo());
        grn_Mdl.setSupplierBillDate(this.getSupplierBillDate());
        grn_Mdl.setCalculateOnTag(this.getCalculateOnTag());
        grn_Mdl.setCalculateOnText(this.getCalculateOnText());
        grn_Mdl.setSupplierGstin(this.getSupplierGstin());
        grn_Mdl.setSupplierGstinDate(this.getSupplierGstinDate());
        grn_Mdl.setVoucherDate(this.getVoucherDate());
        grn_Mdl.setVoucherTypeCode(this.getVoucherTypeCode());
        grn_Mdl.setVoucherTypeName(this.getVoucherTypeName());
        grn_Mdl.setVoucherCategory(this.getVoucherCategory());
        grn_Mdl.setWtaCode(this.getWtaCode());
        grn_Mdl.setPartyTypeForEntry(this.getPartyTypeForEntry());
        grn_Mdl.setPartyType(this.getPartyType());
        grn_Mdl.setRcmApplicable(this.getRcmApplicable());
        grn_Mdl.setPlaceOfSupply(this.getPlaceOfSupply());
        grn_Mdl.setRoundOffAmount(this.getRoundOffAmount());
        grn_Mdl.setGrossAmount(this.getGrossAmount());
        grn_Mdl.setDefaultValueMdl(this.getDefaultValueMdl());
        grn_Mdl.setIsPurchaseOrderExists(this.getIsPurchaseOrderExists());
        grn_Mdl.getGrdGrnDetail().addAll(cloneGrdGrnDetailList(this.getGrdGrnDetail()));
    }

    public List<GrdGrnDetail_Mdl> cloneGrdGrnDetailList(List<GrdGrnDetail_Mdl> list) {
        List<GrdGrnDetail_Mdl> grdGrn_MdlList = null;

        if (list != null) {
            grdGrn_MdlList = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                grdGrn_MdlList.add(list.get(i).clone());
            }
        }

        return grdGrn_MdlList;
    }

    public ModifyReason_Mdl getModifyReasonMdl() {
        return modifyReasonMdl;
    }

    public void setModifyReasonMdl(ModifyReason_Mdl modifyReasonMdl) {
        this.modifyReasonMdl = modifyReasonMdl;
    }

    public RecId getObjRecId() {
        return objRecId;
    }

    public void setObjRecId(RecId objRecId) {
        this.objRecId = objRecId;
    }

    public String getWtaCode() {
        return wtaCode;
    }

    public void setWtaCode(String wtaCode) {
        this.wtaCode = wtaCode;
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

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
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

    public String getCalculateOnTag() {
        return calculateOnTag;
    }

    public void setCalculateOnTag(String calculateOnTag) {
        this.calculateOnTag = calculateOnTag;
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

    public String getDepartmentTag() {
        return departmentTag;
    }

    public void setDepartmentTag(String departmentTag) {
        this.departmentTag = departmentTag;
    }

    public String getDepartmentText() {
        return departmentText;
    }

    public void setDepartmentText(String departmentText) {
        this.departmentText = departmentText;
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

    public List<GrdGrnDetail_Mdl> getGrdGrnDetail() {
        return grdGrnDetail;
    }

    public List<OverHead_Mdl> getOverHeadList() {
        return overHeadList;
    }

    public String getGuiConfigCode() {
        return guiConfigCode;
    }

    public void setGuiConfigCode(String guiConfigCode) {
        this.guiConfigCode = guiConfigCode;
    }

    public DmGUI_Mdl getGuiMdl() {
        return guiMdl;
    }

    public void setGuiMdl(DmGUI_Mdl guiMdl) {
        this.guiMdl = guiMdl;
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

    public String getPartyType() {
        return partyType;
    }

    public void setPartyType(String partyType) {
        this.partyType = partyType;
    }

    public String getRoundOffAmount() {
        return roundOffAmount;
    }

    public void setRoundOffAmount(String roundOffAmount) {
        this.roundOffAmount = roundOffAmount;
    }

    public String getSupplierStateCode() {
        return supplierStateCode;
    }

    public void setSupplierStateCode(String supplierStateCode) {
        this.supplierStateCode = supplierStateCode;
    }

    public String getU_EntDt_LatestLine() {
        return u_EntDt_LatestLine;
    }

    public void setU_EntDt_LatestLine(String u_EntDt_LatestLine) {
        this.u_EntDt_LatestLine = u_EntDt_LatestLine;
    }

    public String getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(String grossAmount) {
        this.grossAmount = grossAmount;
    }

    public String getLineItemCodesCsv() {
        return lineItemCodesCsv;
    }

    public void setLineItemCodesCsv(String lineItemCodesCsv) {
        this.lineItemCodesCsv = lineItemCodesCsv;
    }

    public String getPoItemChangeCondition() {
        return poItemChangeCondition;
    }

    public void setPoItemChangeCondition(String poItemChangeCondition) {
        this.poItemChangeCondition = poItemChangeCondition;
    }

    public DefaultValue_Mdl getDefaultValueMdl() {
        return defaultValueMdl;
    }

    public void setDefaultValueMdl(DefaultValue_Mdl defaultValueMdl) {
        this.defaultValueMdl = defaultValueMdl;
    }

    public boolean getIsPurchaseOrderExists() {
        return isPurchaseOrderExists;
    }

    public void setIsPurchaseOrderExists(boolean isPurchaseOrderExists) {
        this.isPurchaseOrderExists = isPurchaseOrderExists;
    }
}
