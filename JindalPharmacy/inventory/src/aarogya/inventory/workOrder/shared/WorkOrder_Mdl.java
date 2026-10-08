package aarogya.inventory.workOrder.shared;

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

public class WorkOrder_Mdl extends KeyField implements Serializable, IsSerializable {

    private RecId recIdOrder = new RecId();
    private String orderNoDate;
    private String supplierTag;
    private String supplierText;
    private String refNo;
    private String refDate;
    private String quotationDate;
    private String quotationNoTag;
    private String quotationNoText;
    private String voucherTypeTag;
    private String voucherDate;
    private String voucherTypeText;
    private String enquiryTag;
    private String enquiryText;
    private String indentNoTag;
    private String indentNoText;
    private String wtaCode;
    private String u_EntDt_LatestLine;
    private DmGUI_Mdl guiMdl = new DmGUI_Mdl();
    private String guiConfigCode;
    private String isShowUsedQuotationsAlso;
    private String quotationItemChangeCondition;
    private String indItemChangeCondition;
    private String grossAmount;

    private List<GrdItemDetail_Mdl> grdItemDetail = new ArrayList<GrdItemDetail_Mdl>();
    private List<GrdIndentDetail_Mdl> gridIndentDetail = new ArrayList<GrdIndentDetail_Mdl>();
    private List<OverHead_Mdl> overHeadList = new ArrayList<OverHead_Mdl>();
    private String modifiedBy;
    private String preparedBy;
    private String approvedBy;
    private ModifyReason_Mdl modifyReasonMdl;

    public WorkOrder_Mdl() {
    }

    public String getIndItemChangeCondition() {
        return indItemChangeCondition;
    }

    public void setIndItemChangeCondition(String indItemChangeCondition) {
        this.indItemChangeCondition = indItemChangeCondition;
    }

    public ModifyReason_Mdl getModifyReasonMdl() {
        return modifyReasonMdl;
    }

    public void setModifyReasonMdl(ModifyReason_Mdl modifyReasonMdl) {
        this.modifyReasonMdl = modifyReasonMdl;
    }

    public List<OverHead_Mdl> getOverHeadList() {
        return overHeadList;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public String getGuiConfigCode() {
        return guiConfigCode;
    }

    public void setGuiConfigCode(String guiConfigCode) {
        this.guiConfigCode = guiConfigCode;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getPreparedBy() {
        return preparedBy;
    }

    public void setPreparedBy(String preparedBy) {
        this.preparedBy = preparedBy;
    }

    public String getOrderNoDate() {
        return orderNoDate;
    }

    public void setOrderNoDate(String orderNoDate) {
        this.orderNoDate = orderNoDate;
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

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getRefDate() {
        return refDate;
    }

    public void setRefDate(String refDate) {
        this.refDate = refDate;
    }

    public String getQuotationDate() {
        return quotationDate;
    }

    public void setQuotationDate(String quotationDate) {
        this.quotationDate = quotationDate;
    }

    public String getQuotationNoTag() {
        return quotationNoTag;
    }

    public void setQuotationNoTag(String quotationNoTag) {
        this.quotationNoTag = quotationNoTag;
    }

    public String getQuotationNoText() {
        return quotationNoText;
    }

    public void setQuotationNoText(String quotationNoText) {
        this.quotationNoText = quotationNoText;
    }

    public String getIndentNoTag() {
        return indentNoTag;
    }

    public void setIndentNoTag(String indentNoTag) {
        this.indentNoTag = indentNoTag;
    }

    public String getIndentNoText() {
        return indentNoText;
    }

    public void setIndentNoText(String indentNoText) {
        this.indentNoText = indentNoText;
    }

    public String getEnquiryTag() {
        return enquiryTag;
    }

    public void setEnquiryTag(String enquiryTag) {
        this.enquiryTag = enquiryTag;
    }

    public String getEnquiryText() {
        return enquiryText;
    }

    public void setEnquiryText(String enquiryText) {
        this.enquiryText = enquiryText;
    }

    public String getVoucherTypeTag() {
        return voucherTypeTag;
    }

    public void setVoucherTypeTag(String voucherTypeTag) {
        this.voucherTypeTag = voucherTypeTag;
    }

    public String getVoucherTypeText() {
        return voucherTypeText;
    }

    public void setVoucherTypeText(String voucherTypeText) {
        this.voucherTypeText = voucherTypeText;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public List<GrdItemDetail_Mdl> getGrdItemDetail() {
        return grdItemDetail;
    }

    public List<GrdIndentDetail_Mdl> getGridIndentDetail() {
        return gridIndentDetail;
    }

    public RecId getRecIdOrder() {
        return recIdOrder;
    }

    public void setRecIdOrder(RecId recIdOrder) {
        this.recIdOrder = recIdOrder;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getWtaCode() {
        return wtaCode;
    }

    public void setWtaCode(String wtaCode) {
        this.wtaCode = wtaCode;
    }

    public String getU_EntDt_LatestLine() {
        return u_EntDt_LatestLine;
    }

    public void setU_EntDt_LatestLine(String u_EntDt_LatestLine) {
        this.u_EntDt_LatestLine = u_EntDt_LatestLine;
    }

    public DmGUI_Mdl getGuiMdl() {
        return guiMdl;
    }

    public void setGuiMdl(DmGUI_Mdl guiMdl) {
        this.guiMdl = guiMdl;
    }

    public String getIsShowUsedQuotationsAlso() {
        return isShowUsedQuotationsAlso;
    }

    public void setIsShowUsedQuotationsAlso(String isShowUsedQuotationsAlso) {
        this.isShowUsedQuotationsAlso = isShowUsedQuotationsAlso;
    }

    public String getQuotationItemChangeCondition() {
        return quotationItemChangeCondition;
    }

    public void setQuotationItemChangeCondition(String quotationItemChangeCondition) {
        this.quotationItemChangeCondition = quotationItemChangeCondition;
    }

    public String getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(String grossAmount) {
        this.grossAmount = grossAmount;
    }
}
