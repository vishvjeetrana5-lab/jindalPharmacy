package dataman.dmToolkit.gst.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class GstTrans_Mdl implements Serializable, IsSerializable {

    private String docId;
    private String v_Sno;
    private String site_Code;
    private String v_Type;
    private String recID;
    private String recIdPrefix;
    private String recIdSerial;
    private String v_Date;
    private String partyBillNo;
    private String partyBillDt;
    private String shippingBillNo;
    private String shippingBillDt;
    private String shippingPortCode;
    private String partyCode;
    private String consignee;
    private String consigneeType;
    private String placeOfSupply;
    private String refDocId;
    private String refRecId;
    private String refV_Date;
    private String refV_Type;
    private String refDocValue;
    private String refPartyBillNo;
    private String refPartyBillDt;
    private String gstReasonCode;
    private String rcmApplicable;
    private String itemCode;
    private String hsnCode;
    private String itemGSTNature;
    private String departmentCode;
    private String qty;
    private String gstUnitCode;
    private String itemGrossAmount;
    private String itemTaxableAmount;
    private String itemCgstRate;
    private String itemCgstAmount;
    private String itemSgstRate;
    private String itemSgstAmout;
    private String itemIgstRate;
    private String itemIgstAmount;
    private String itemUgstRate;
    private String itemUgstAmount;
    private String itemCessRate;
    private String itemCessAmount;
    private String itemNetAmount;
    private String invoiceSGST;
    private String invoiceCGST;
    private String invoiceIGST;
    private String invoiceUGST;
    private String invoiceValue;
    private String uploadYN;
    private String gstType;
    private String isTaxInclusive;
    private String partyType;
    private String gstin;
    private String gstinDate;
//    private String 1ItemSgstAmount;
    private String transporterCode;
    private String lrNo;
    private String lrDate;
    private String ewayBillNo;
    private String ewayBillDt;
    private String transportMode;
    private String vehicleNo;
    private String isEWayBillRequired;
    private String gstDocTypeCode;
    private String gstDocTypeName;
    private String gstDocNatureCode;
    private String gstDocNatureName;
    private String gstTransNature;

    public GstTrans_Mdl() {
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getV_Sno() {
        return v_Sno;
    }

    public void setV_Sno(String v_Sno) {
        this.v_Sno = v_Sno;
    }

    public String getSite_Code() {
        return site_Code;
    }

    public void setSite_Code(String site_Code) {
        this.site_Code = site_Code;
    }

    public String getV_Type() {
        return v_Type;
    }

    public void setV_Type(String v_Type) {
        this.v_Type = v_Type;
    }

    public String getRecID() {
        return recID;
    }

    public void setRecID(String recID) {
        this.recID = recID;
    }

    public String getRecIdPrefix() {
        return recIdPrefix;
    }

    public void setRecIdPrefix(String recIdPrefix) {
        this.recIdPrefix = recIdPrefix;
    }

    public String getRecIdSerial() {
        return recIdSerial;
    }

    public void setRecIdSerial(String recIdSerial) {
        this.recIdSerial = recIdSerial;
    }

    public String getV_Date() {
        return v_Date;
    }

    public void setV_Date(String v_Date) {
        this.v_Date = v_Date;
    }

    public String getPartyBillNo() {
        return partyBillNo;
    }

    public void setPartyBillNo(String partyBillNo) {
        this.partyBillNo = partyBillNo;
    }

    public String getPartyBillDt() {
        return partyBillDt;
    }

    public void setPartyBillDt(String partyBillDt) {
        this.partyBillDt = partyBillDt;
    }

    public String getShippingBillNo() {
        return shippingBillNo;
    }

    public void setShippingBillNo(String shippingBillNo) {
        this.shippingBillNo = shippingBillNo;
    }

    public String getShippingBillDt() {
        return shippingBillDt;
    }

    public void setShippingBillDt(String shippingBillDt) {
        this.shippingBillDt = shippingBillDt;
    }

    public String getShippingPortCode() {
        return shippingPortCode;
    }

    public void setShippingPortCode(String shippingPortCode) {
        this.shippingPortCode = shippingPortCode;
    }

    public String getPartyCode() {
        return partyCode;
    }

    public void setPartyCode(String partyCode) {
        this.partyCode = partyCode;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getConsigneeType() {
        return consigneeType;
    }

    public void setConsigneeType(String consigneeType) {
        this.consigneeType = consigneeType;
    }

    public String getPlaceOfSupply() {
        return placeOfSupply;
    }

    public void setPlaceOfSupply(String placeOfSupply) {
        this.placeOfSupply = placeOfSupply;
    }

    public String getRefDocId() {
        return refDocId;
    }

    public void setRefDocId(String refDocId) {
        this.refDocId = refDocId;
    }

    public String getRefRecId() {
        return refRecId;
    }

    public void setRefRecId(String refRecId) {
        this.refRecId = refRecId;
    }

    public String getRefV_Date() {
        return refV_Date;
    }

    public void setRefV_Date(String refV_Date) {
        this.refV_Date = refV_Date;
    }

    public String getRefV_Type() {
        return refV_Type;
    }

    public void setRefV_Type(String refV_Type) {
        this.refV_Type = refV_Type;
    }

    public String getRefDocValue() {
        return refDocValue;
    }

    public void setRefDocValue(String refDocValue) {
        this.refDocValue = refDocValue;
    }

    public String getRefPartyBillNo() {
        return refPartyBillNo;
    }

    public void setRefPartyBillNo(String refPartyBillNo) {
        this.refPartyBillNo = refPartyBillNo;
    }

    public String getRefPartyBillDt() {
        return refPartyBillDt;
    }

    public void setRefPartyBillDt(String refPartyBillDt) {
        this.refPartyBillDt = refPartyBillDt;
    }

    public String getGstReasonCode() {
        return gstReasonCode;
    }

    public void setGstReasonCode(String gstReasonCode) {
        this.gstReasonCode = gstReasonCode;
    }

    public String getRcmApplicable() {
        return rcmApplicable;
    }

    public void setRcmApplicable(String rcmApplicable) {
        this.rcmApplicable = rcmApplicable;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getHsnCode() {
        return hsnCode;
    }

    public void setHsnCode(String hsnCode) {
        this.hsnCode = hsnCode;
    }

    public String getItemGSTNature() {
        return itemGSTNature;
    }

    public void setItemGSTNature(String itemGSTNature) {
        this.itemGSTNature = itemGSTNature;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getGstUnitCode() {
        return gstUnitCode;
    }

    public void setGstUnitCode(String gstUnitCode) {
        this.gstUnitCode = gstUnitCode;
    }

    public String getItemGrossAmount() {
        return itemGrossAmount;
    }

    public void setItemGrossAmount(String itemGrossAmount) {
        this.itemGrossAmount = itemGrossAmount;
    }

    public String getItemTaxableAmount() {
        return itemTaxableAmount;
    }

    public void setItemTaxableAmount(String itemTaxableAmount) {
        this.itemTaxableAmount = itemTaxableAmount;
    }

    public String getItemCgstRate() {
        return itemCgstRate;
    }

    public void setItemCgstRate(String itemCgstRate) {
        this.itemCgstRate = itemCgstRate;
    }

    public String getItemCgstAmount() {
        return itemCgstAmount;
    }

    public void setItemCgstAmount(String itemCgstAmount) {
        this.itemCgstAmount = itemCgstAmount;
    }

    public String getItemSgstRate() {
        return itemSgstRate;
    }

    public void setItemSgstRate(String itemSgstRate) {
        this.itemSgstRate = itemSgstRate;
    }

    public String getItemSgstAmout() {
        return itemSgstAmout;
    }

    public void setItemSgstAmout(String itemSgstAmout) {
        this.itemSgstAmout = itemSgstAmout;
    }

    public String getItemIgstRate() {
        return itemIgstRate;
    }

    public void setItemIgstRate(String itemIgstRate) {
        this.itemIgstRate = itemIgstRate;
    }

    public String getItemIgstAmount() {
        return itemIgstAmount;
    }

    public void setItemIgstAmount(String itemIgstAmount) {
        this.itemIgstAmount = itemIgstAmount;
    }

    public String getItemUgstRate() {
        return itemUgstRate;
    }

    public void setItemUgstRate(String itemUgstRate) {
        this.itemUgstRate = itemUgstRate;
    }

    public String getItemUgstAmount() {
        return itemUgstAmount;
    }

    public void setItemUgstAmount(String itemUgstAmount) {
        this.itemUgstAmount = itemUgstAmount;
    }

    public String getItemCessRate() {
        return itemCessRate;
    }

    public void setItemCessRate(String itemCessRate) {
        this.itemCessRate = itemCessRate;
    }

    public String getItemCessAmount() {
        return itemCessAmount;
    }

    public void setItemCessAmount(String itemCessAmount) {
        this.itemCessAmount = itemCessAmount;
    }

    public String getItemNetAmount() {
        return itemNetAmount;
    }

    public void setItemNetAmount(String itemNetAmount) {
        this.itemNetAmount = itemNetAmount;
    }

    public String getInvoiceSGST() {
        return invoiceSGST;
    }

    public void setInvoiceSGST(String invoiceSGST) {
        this.invoiceSGST = invoiceSGST;
    }

    public String getInvoiceCGST() {
        return invoiceCGST;
    }

    public void setInvoiceCGST(String invoiceCGST) {
        this.invoiceCGST = invoiceCGST;
    }

    public String getInvoiceIGST() {
        return invoiceIGST;
    }

    public void setInvoiceIGST(String invoiceIGST) {
        this.invoiceIGST = invoiceIGST;
    }

    public String getInvoiceUGST() {
        return invoiceUGST;
    }

    public void setInvoiceUGST(String invoiceUGST) {
        this.invoiceUGST = invoiceUGST;
    }

    public String getInvoiceValue() {
        return invoiceValue;
    }

    public void setInvoiceValue(String invoiceValue) {
        this.invoiceValue = invoiceValue;
    }

    public String getUploadYN() {
        return uploadYN;
    }

    public void setUploadYN(String uploadYN) {
        this.uploadYN = uploadYN;
    }

    public String getGstType() {
        return gstType;
    }

    public void setGstType(String gstType) {
        this.gstType = gstType;
    }

    public String getIsTaxInclusive() {
        return isTaxInclusive;
    }

    public void setIsTaxInclusive(String isTaxInclusive) {
        this.isTaxInclusive = isTaxInclusive;
    }

    public String getPartyType() {
        return partyType;
    }

    public void setPartyType(String partyType) {
        this.partyType = partyType;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getGstinDate() {
        return gstinDate;
    }

    public void setGstinDate(String gstinDate) {
        this.gstinDate = gstinDate;
    }

//    public String getItemSgstAmount() {
//        return ItemSgstAmount;
//    }
//
//    public void setItemSgstAmount(String ItemSgstAmount) {
//        this.ItemSgstAmount = ItemSgstAmount;
//    }
    public String getTransporterCode() {
        return transporterCode;
    }

    public void setTransporterCode(String transporterCode) {
        this.transporterCode = transporterCode;
    }

    public String getLrNo() {
        return lrNo;
    }

    public void setLrNo(String lrNo) {
        this.lrNo = lrNo;
    }

    public String getLrDate() {
        return lrDate;
    }

    public void setLrDate(String lrDate) {
        this.lrDate = lrDate;
    }

    public String getEwayBillNo() {
        return ewayBillNo;
    }

    public void setEwayBillNo(String ewayBillNo) {
        this.ewayBillNo = ewayBillNo;
    }

    public String getEwayBillDt() {
        return ewayBillDt;
    }

    public void setEwayBillDt(String ewayBillDt) {
        this.ewayBillDt = ewayBillDt;
    }

    public String getTransportMode() {
        return transportMode;
    }

    public void setTransportMode(String transportMode) {
        this.transportMode = transportMode;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getIsEWayBillRequired() {
        return isEWayBillRequired;
    }

    public void setIsEWayBillRequired(String isEWayBillRequired) {
        this.isEWayBillRequired = isEWayBillRequired;
    }

    public String getGstDocTypeCode() {
        return gstDocTypeCode;
    }

    public void setGstDocTypeCode(String gstDocTypeCode) {
        this.gstDocTypeCode = gstDocTypeCode;
    }

    public String getGstDocTypeName() {
        return gstDocTypeName;
    }

    public void setGstDocTypeName(String gstDocTypeName) {
        this.gstDocTypeName = gstDocTypeName;
    }

    public String getGstDocNatureCode() {
        return gstDocNatureCode;
    }

    public void setGstDocNatureCode(String gstDocNatureCode) {
        this.gstDocNatureCode = gstDocNatureCode;
    }

    public String getGstDocNatureName() {
        return gstDocNatureName;
    }

    public void setGstDocNatureName(String gstDocNatureName) {
        this.gstDocNatureName = gstDocNatureName;
    }

    public String getGstTransNature() {
        return gstTransNature;
    }

    public void setGstTransNature(String gstTransNature) {
        this.gstTransNature = gstTransNature;
    }

}
