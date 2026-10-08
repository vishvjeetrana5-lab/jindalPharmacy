/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataman.dmToolkit.gst.shared;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author DATAMAN
 */
public class EInvItemDtls_Mdl implements Serializable, IsSerializable {

    @JsonProperty("ItemNo")
    private String itemNo;
    @JsonProperty("SlNo")
    private String slNo;
    @JsonProperty("PrdDesc")
    private String prdDesc;
    @JsonProperty("IsServc")
    private String isServc;
    @JsonProperty("HsnCd")
    private String hsnCd;
    @JsonProperty("Barcde")
    private String barcde;
    @JsonProperty("Qty")
    private String qty;
    @JsonProperty("FreeQty")
    private String freeQty;
    @JsonProperty("Unit")
    private String unit;
    @JsonProperty("UnitPrice")
    private String unitPrice;
    @JsonProperty("TotAmt")
    private String totAmt;
    @JsonProperty("Discount")
    private String discount;
    @JsonProperty("PreTaxVal")
    private String preTaxVal;
    @JsonProperty("AssAmt")
    private String assAmt;
    @JsonProperty("GstRt")
    private String gstRt;
    @JsonProperty("IgstAmt")
    private String igstAmt;
    @JsonProperty("CgstAmt")
    private String cgstAmt;
    @JsonProperty("SgstAmt")
    private String sgstAmt;
    @JsonProperty("CesRt")
    private String cesRt;
    @JsonProperty("CesAmt")
    private String cesAmt;
    @JsonProperty("CesNonAdvlAmt")
    private String cesNonAdvlAmt;
    @JsonProperty("StateCesRt")
    private String stateCesRt;
    @JsonProperty("StateCesAmt")
    private String stateCesAmt;
    @JsonProperty("StateCesNonAdvlAmt")
    private String stateCesNonAdvlAmt;
    @JsonProperty("OthChrg")
    private String othChrg;
    @JsonProperty("TotItemVal")
    private String totItemVal;
    @JsonProperty("OrdLineRef")
    private String ordLineRef;
    @JsonProperty("OrgCntry")
    private String orgCntry;
    @JsonProperty("PrdSlNo")
    private String prdSlNo;
    @JsonProperty("BchDtls")
    private EinvItmBatchDtls_Mdl bchDtls;
    @JsonProperty("AttribDtls")
    private List<EinvItmAttributeDtls_Mdl> attribDtls;

    public EInvItemDtls_Mdl() {
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getSlNo() {
        return slNo;
    }

    public void setSlNo(String slNo) {
        this.slNo = slNo;
    }

    public String getPrdDesc() {
        return prdDesc;
    }

    public void setPrdDesc(String prdDesc) {
        this.prdDesc = prdDesc;
    }

    public String getIsServc() {
        return isServc;
    }

    public void setIsServc(String isServc) {
        this.isServc = isServc;
    }

    public String getHsnCd() {
        return hsnCd;
    }

    public void setHsnCd(String hsnCd) {
        this.hsnCd = hsnCd;
    }

    public String getBarcde() {
        return barcde;
    }

    public void setBarcde(String barcde) {
        this.barcde = barcde;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getFreeQty() {
        return freeQty;
    }

    public void setFreeQty(String freeQty) {
        this.freeQty = freeQty;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getTotAmt() {
        return totAmt;
    }

    public void setTotAmt(String totAmt) {
        this.totAmt = totAmt;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getPreTaxVal() {
        return preTaxVal;
    }

    public void setPreTaxVal(String preTaxVal) {
        this.preTaxVal = preTaxVal;
    }

    public String getAssAmt() {
        return assAmt;
    }

    public void setAssAmt(String assAmt) {
        this.assAmt = assAmt;
    }

    public String getGstRt() {
        return gstRt;
    }

    public void setGstRt(String gstRt) {
        this.gstRt = gstRt;
    }

    public String getIgstAmt() {
        return igstAmt;
    }

    public void setIgstAmt(String igstAmt) {
        this.igstAmt = igstAmt;
    }

    public String getCgstAmt() {
        return cgstAmt;
    }

    public void setCgstAmt(String cgstAmt) {
        this.cgstAmt = cgstAmt;
    }

    public String getSgstAmt() {
        return sgstAmt;
    }

    public void setSgstAmt(String sgstAmt) {
        this.sgstAmt = sgstAmt;
    }

    public String getCesRt() {
        return cesRt;
    }

    public void setCesRt(String cesRt) {
        this.cesRt = cesRt;
    }

    public String getCesAmt() {
        return cesAmt;
    }

    public void setCesAmt(String cesAmt) {
        this.cesAmt = cesAmt;
    }

    public String getCesNonAdvlAmt() {
        return cesNonAdvlAmt;
    }

    public void setCesNonAdvlAmt(String cesNonAdvlAmt) {
        this.cesNonAdvlAmt = cesNonAdvlAmt;
    }

    public String getStateCesRt() {
        return stateCesRt;
    }

    public void setStateCesRt(String stateCesRt) {
        this.stateCesRt = stateCesRt;
    }

    public String getStateCesAmt() {
        return stateCesAmt;
    }

    public void setStateCesAmt(String stateCesAmt) {
        this.stateCesAmt = stateCesAmt;
    }

    public String getStateCesNonAdvlAmt() {
        return stateCesNonAdvlAmt;
    }

    public void setStateCesNonAdvlAmt(String stateCesNonAdvlAmt) {
        this.stateCesNonAdvlAmt = stateCesNonAdvlAmt;
    }

    public String getOthChrg() {
        return othChrg;
    }

    public void setOthChrg(String othChrg) {
        this.othChrg = othChrg;
    }

    public String getTotItemVal() {
        return totItemVal;
    }

    public void setTotItemVal(String totItemVal) {
        this.totItemVal = totItemVal;
    }

    public String getOrdLineRef() {
        return ordLineRef;
    }

    public void setOrdLineRef(String ordLineRef) {
        this.ordLineRef = ordLineRef;
    }

    public String getOrgCntry() {
        return orgCntry;
    }

    public void setOrgCntry(String orgCntry) {
        this.orgCntry = orgCntry;
    }

    public String getPrdSlNo() {
        return prdSlNo;
    }

    public void setPrdSlNo(String prdSlNo) {
        this.prdSlNo = prdSlNo;
    }

    public EinvItmBatchDtls_Mdl getBchDtls() {
        return bchDtls;
    }

    public void setBchDtls(EinvItmBatchDtls_Mdl bchDtls) {
        this.bchDtls = bchDtls;
    }

    public List<EinvItmAttributeDtls_Mdl> getAttribDtls() {
        return attribDtls;
    }

    public void setAttribDtls(List<EinvItmAttributeDtls_Mdl> attribDtls) {
        this.attribDtls = attribDtls;
    }

}
