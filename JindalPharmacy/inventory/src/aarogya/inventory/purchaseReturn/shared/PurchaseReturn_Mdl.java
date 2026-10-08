package aarogya.inventory.purchaseReturn.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmToolkit.overHead.shared.OverHead_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.RecId;
import dataman.dmToolkit.dmGUI.shared.DmGUI_Mdl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.serviceGrid.shared.ServiceDetail_Mdl;

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
    private String partyBillNo;
    private String partyBillDate;
    private String gstReasonCode;
    private String gstReasonName;
    private String billNo;
    private String billNoTag;
    private String billDate;
    private String remark;
    private String gateNoTag;
    private String gateNoText;
    private String transporterCode;
    private String transporterName;
    private String ewayNo;
    private String ewayDate;
    private String ewayBillRequiredCode;
    private String ewayBillRequiredName;
    private String transportModeCode;
    private String transportModeName;
    private String vehicleNo;
    private String approvedBy;
    private String approvedDateTime;
    private String grossAmount;
    private String roundOffAmount;
    private String wtaCode;
    private String guiConfigCode;
    private String u_EntDt_LatestLine;
    private String pbItemChangeCondition;
    private String locationCode;

    private String goodsGrossAmt;
    private String serviceGrossAmt;
    private String discount;
    private String goodsTaxableAmt;
    private String serviceTaxableAmt;
    private String taxableAmt;
    private String sgstUgst;
    private String sgst;
    private String ugst;
    private String cgst;
    private String igst;
    private String subTotal;
    private String finalDiscountPer;
    private String finalDiscount;
    private String finalDiscountAcCode;
    private String totalAmt;
    private String roundOffAcCode;
    private String netAmount;
    private String landedValue;
    private boolean inventoryAccountingActive = false;
    private String balancingAc;

    private DefaultValue_Mdl defaultValueMdl;
    private ModifyReason_Mdl modifyReasonMdl;
    private DmGUI_Mdl guiMdl = new DmGUI_Mdl();
    private List<GrdItemDetail_Mdl> listItemDetail = new ArrayList<GrdItemDetail_Mdl>();
    private List<OverHead_Mdl> overHeadList = new ArrayList<OverHead_Mdl>();
    private List<ServiceDetail_Mdl> grdServiceDetail = new ArrayList<ServiceDetail_Mdl>();

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

    public DmGUI_Mdl getGuiMdl() {
        return guiMdl;
    }

    public void setGuiMdl(DmGUI_Mdl guiMdl) {
        this.guiMdl = guiMdl;
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

    public String getPartyBillNo() {
        return partyBillNo;
    }

    public void setPartyBillNo(String partyBillNo) {
        this.partyBillNo = partyBillNo;
    }

    public String getPartyBillDate() {
        return partyBillDate;
    }

    public void setPartyBillDate(String partyBillDate) {
        this.partyBillDate = partyBillDate;
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

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getBillNoTag() {
        return billNoTag;
    }

    public void setBillNoTag(String billNoTag) {
        this.billNoTag = billNoTag;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getGateNoTag() {
        return gateNoTag;
    }

    public void setGateNoTag(String gateNoTag) {
        this.gateNoTag = gateNoTag;
    }

    public String getGateNoText() {
        return gateNoText;
    }

    public void setGateNoText(String gateNoText) {
        this.gateNoText = gateNoText;
    }

    public String getTransporterCode() {
        return transporterCode;
    }

    public void setTransporterCode(String transporterCode) {
        this.transporterCode = transporterCode;
    }

    public String getTransporterName() {
        return transporterName;
    }

    public void setTransporterName(String transporterName) {
        this.transporterName = transporterName;
    }

    public String getEwayNo() {
        return ewayNo;
    }

    public void setEwayNo(String ewayNo) {
        this.ewayNo = ewayNo;
    }

    public String getEwayDate() {
        return ewayDate;
    }

    public void setEwayDate(String ewayDate) {
        this.ewayDate = ewayDate;
    }

    public String getEwayBillRequiredCode() {
        return ewayBillRequiredCode;
    }

    public void setEwayBillRequiredCode(String ewayBillRequiredCode) {
        this.ewayBillRequiredCode = ewayBillRequiredCode;
    }

    public String getEwayBillRequiredName() {
        return ewayBillRequiredName;
    }

    public void setEwayBillRequiredName(String ewayBillRequiredName) {
        this.ewayBillRequiredName = ewayBillRequiredName;
    }

    public String getTransportModeCode() {
        return transportModeCode;
    }

    public void setTransportModeCode(String transportModeCode) {
        this.transportModeCode = transportModeCode;
    }

    public String getTransportModeName() {
        return transportModeName;
    }

    public void setTransportModeName(String transportModeName) {
        this.transportModeName = transportModeName;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
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

    public String getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(String grossAmount) {
        this.grossAmount = grossAmount;
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

    public DefaultValue_Mdl getDefaultValueMdl() {
        return defaultValueMdl;
    }

    public void setDefaultValueMdl(DefaultValue_Mdl defaultValueMdl) {
        this.defaultValueMdl = defaultValueMdl;
    }

    public List<ServiceDetail_Mdl> getGrdServiceDetail() {
        return grdServiceDetail;
    }

    public void setGrdServiceDetail(List<ServiceDetail_Mdl> grdServiceDetail) {
        this.grdServiceDetail = grdServiceDetail;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getGoodsGrossAmt() {
        return goodsGrossAmt;
    }

    public void setGoodsGrossAmt(String goodsGrossAmt) {
        this.goodsGrossAmt = goodsGrossAmt;
    }

    public String getServiceGrossAmt() {
        return serviceGrossAmt;
    }

    public void setServiceGrossAmt(String serviceGrossAmt) {
        this.serviceGrossAmt = serviceGrossAmt;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getGoodsTaxableAmt() {
        return goodsTaxableAmt;
    }

    public void setGoodsTaxableAmt(String goodsTaxableAmt) {
        this.goodsTaxableAmt = goodsTaxableAmt;
    }

    public String getServiceTaxableAmt() {
        return serviceTaxableAmt;
    }

    public void setServiceTaxableAmt(String serviceTaxableAmt) {
        this.serviceTaxableAmt = serviceTaxableAmt;
    }

    public String getTaxableAmt() {
        return taxableAmt;
    }

    public void setTaxableAmt(String taxableAmt) {
        this.taxableAmt = taxableAmt;
    }

    public String getSgstUgst() {
        return sgstUgst;
    }

    public void setSgstUgst(String sgstUgst) {
        this.sgstUgst = sgstUgst;
    }

    public String getSgst() {
        return sgst;
    }

    public void setSgst(String sgst) {
        this.sgst = sgst;
    }

    public String getUgst() {
        return ugst;
    }

    public void setUgst(String ugst) {
        this.ugst = ugst;
    }

    public String getCgst() {
        return cgst;
    }

    public void setCgst(String cgst) {
        this.cgst = cgst;
    }

    public String getIgst() {
        return igst;
    }

    public void setIgst(String igst) {
        this.igst = igst;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getFinalDiscountPer() {
        return finalDiscountPer;
    }

    public void setFinalDiscountPer(String finalDiscountPer) {
        this.finalDiscountPer = finalDiscountPer;
    }

    public String getFinalDiscount() {
        return finalDiscount;
    }

    public void setFinalDiscount(String finalDiscount) {
        this.finalDiscount = finalDiscount;
    }

    public String getFinalDiscountAcCode() {
        return finalDiscountAcCode;
    }

    public void setFinalDiscountAcCode(String finalDiscountAcCode) {
        this.finalDiscountAcCode = finalDiscountAcCode;
    }

    public String getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        this.totalAmt = totalAmt;
    }

    public String getRoundOffAcCode() {
        return roundOffAcCode;
    }

    public void setRoundOffAcCode(String roundOffAcCode) {
        this.roundOffAcCode = roundOffAcCode;
    }

    public String getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(String netAmount) {
        this.netAmount = netAmount;
    }

    public String getLandedValue() {
        return landedValue;
    }

    public void setLandedValue(String landedValue) {
        this.landedValue = landedValue;
    }

    public boolean isInventoryAccountingActive() {
        return inventoryAccountingActive;
    }

    public void setInventoryAccountingActive(boolean inventoryAccountingActive) {
        this.inventoryAccountingActive = inventoryAccountingActive;
    }

    public String getBalancingAc() {
        return balancingAc;
    }

    public void setBalancingAc(String balancingAc) {
        this.balancingAc = balancingAc;
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
        grn_Mdl.setVoucherCategory(this.getVoucherCategory());
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
        grn_Mdl.setPartyBillNo(this.getPartyBillNo());
        grn_Mdl.setPartyBillDate(this.getPartyBillDate());
        grn_Mdl.setGstReasonCode(this.getGstReasonCode());
        grn_Mdl.setGstReasonName(this.getGstReasonName());
        grn_Mdl.setBillNo(this.getBillNo());
        grn_Mdl.setBillNoTag(this.getBillNoTag());
        grn_Mdl.setBillDate(this.getBillDate());
        grn_Mdl.setRemark(this.getRemark());
        grn_Mdl.setGateNoTag(this.getGateNoTag());
        grn_Mdl.setGateNoText(this.getGateNoText());
        grn_Mdl.setTransporterCode(this.getTransporterCode());
        grn_Mdl.setTransporterName(this.getTransporterName());
        grn_Mdl.setEwayNo(this.getEwayNo());
        grn_Mdl.setEwayDate(this.getEwayDate());
        grn_Mdl.setEwayBillRequiredCode(this.getEwayBillRequiredCode());
        grn_Mdl.setEwayBillRequiredName(this.getEwayBillRequiredName());
        grn_Mdl.setTransportModeCode(this.getTransportModeCode());
        grn_Mdl.setTransportModeName(this.getTransportModeName());
        grn_Mdl.setVehicleNo(this.getVehicleNo());
        grn_Mdl.setApprovedBy(this.getApprovedBy());
        grn_Mdl.setApprovedDateTime(this.getApprovedDateTime());
        grn_Mdl.setRoundOffAmount(this.getRoundOffAmount());
        grn_Mdl.setGrossAmount(this.getGrossAmount());
        grn_Mdl.setGoodsGrossAmt(this.getGoodsGrossAmt());
        grn_Mdl.setServiceGrossAmt(this.getServiceGrossAmt());
        grn_Mdl.setDiscount(this.getDiscount());
        grn_Mdl.setGoodsTaxableAmt(this.getGoodsTaxableAmt());
        grn_Mdl.setServiceTaxableAmt(this.getServiceTaxableAmt());
        grn_Mdl.setTaxableAmt(this.getTaxableAmt());
        grn_Mdl.setSgstUgst(this.getSgstUgst());
        grn_Mdl.setSgst(this.getSgst());
        grn_Mdl.setUgst(this.getUgst());
        grn_Mdl.setCgst(this.getCgst());
        grn_Mdl.setIgst(this.getIgst());
        grn_Mdl.setSubTotal(this.getSubTotal());
        grn_Mdl.setFinalDiscountPer(this.getFinalDiscountPer());
        grn_Mdl.setFinalDiscount(this.getFinalDiscount());
        grn_Mdl.setFinalDiscountAcCode(this.getFinalDiscountAcCode());
        grn_Mdl.setTotalAmt(this.getTotalAmt());
        grn_Mdl.setRoundOffAmount(this.getRoundOffAmount());
        grn_Mdl.setRoundOffAcCode(this.getRoundOffAcCode());
        grn_Mdl.setNetAmount(this.getNetAmount());
        grn_Mdl.setLandedValue(this.getLandedValue());
        grn_Mdl.setWtaCode(this.getWtaCode());
        grn_Mdl.setGuiConfigCode(this.getGuiConfigCode());
        grn_Mdl.setU_EntDt_LatestLine(this.getU_EntDt_LatestLine());
        grn_Mdl.setPbItemChangeCondition(this.getPbItemChangeCondition());
        grn_Mdl.setGuiMdl(this.getGuiMdl()); // Not Correct Cloning
        grn_Mdl.setInventoryAccountingActive(this.isInventoryAccountingActive());
        grn_Mdl.getListItemDetail().addAll(cloneListItemDetail(this.getListItemDetail()));
        grn_Mdl.getOverHeadList().addAll(OverHead_Mdl.getOHListClone(this.getOverHeadList()));
        grn_Mdl.getGrdServiceDetail().addAll(cloneGrdServiceDetailList(this.getGrdServiceDetail()));
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

    private List<ServiceDetail_Mdl> cloneGrdServiceDetailList(List<ServiceDetail_Mdl> list) {
        List<ServiceDetail_Mdl> grdServiceDetail_Mdls = null;

        if (list != null) {
            grdServiceDetail_Mdls = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                grdServiceDetail_Mdls.add(list.get(i).clone());
            }
        }

        return grdServiceDetail_Mdls;
    }
}
