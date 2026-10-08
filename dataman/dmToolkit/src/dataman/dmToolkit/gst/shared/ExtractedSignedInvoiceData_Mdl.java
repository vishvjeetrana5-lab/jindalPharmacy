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
public class ExtractedSignedInvoiceData_Mdl implements Serializable, IsSerializable {

    @JsonProperty("AckNo")
    private String ackNo;
    @JsonProperty("AckDt")
    private String ackDt;
    @JsonProperty("Version")
    private String version;
    @JsonProperty("Irn")
    private String irn;
    @JsonProperty("TranDtls")
    private EInvTranDtls_Mdl tranDtls;
    @JsonProperty("DocDtls")
    private EInvDocDtls_Mdl docDtls;
    @JsonProperty("SellerDtls")
    private Party_Mdl sellerDtls;
    @JsonProperty("BuyerDtls")
    private Party_Mdl buyerDtls;
    @JsonProperty("DispDtls")
    private Party_Mdl dispDtls;
    @JsonProperty("ShipDtls")
    private Party_Mdl shipDtls;
    @JsonProperty("EwbDtls")
    private EInvEwbDtlsResponse_Mdl ewbDtls;
    @JsonProperty("ValDtls")
    private EInvValDtls_Mdl valDtls;
    @JsonProperty("PayDtls")
    private EinvPayeeDtls_Mdl payDtls;
    @JsonProperty("ExpDtls")
    private EinvExportDtls_Mdl expDtls;
    @JsonProperty("RefDtls")
    private EinvRefDtls_Mdl refDtls;
    @JsonProperty("AddlDocDtls")
    private List<EinvAdditionalDocDtls_Mdl> addlDocDtls;
    @JsonProperty("ItemList")
    private List<EInvItemDtls_Mdl> itemList;

    public ExtractedSignedInvoiceData_Mdl() {
    }

    public String getAckNo() {
        return ackNo;
    }

    public void setAckNo(String ackNo) {
        this.ackNo = ackNo;
    }

    public String getAckDt() {
        return ackDt;
    }

    public void setAckDt(String ackDt) {
        this.ackDt = ackDt;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getIrn() {
        return irn;
    }

    public void setIrn(String irn) {
        this.irn = irn;
    }

    public EInvTranDtls_Mdl getTranDtls() {
        return tranDtls;
    }

    public void setTranDtls(EInvTranDtls_Mdl tranDtls) {
        this.tranDtls = tranDtls;
    }

    public EInvDocDtls_Mdl getDocDtls() {
        return docDtls;
    }

    public void setDocDtls(EInvDocDtls_Mdl docDtls) {
        this.docDtls = docDtls;
    }

    public Party_Mdl getSellerDtls() {
        return sellerDtls;
    }

    public void setSellerDtls(Party_Mdl sellerDtls) {
        this.sellerDtls = sellerDtls;
    }

    public Party_Mdl getBuyerDtls() {
        return buyerDtls;
    }

    public void setBuyerDtls(Party_Mdl buyerDtls) {
        this.buyerDtls = buyerDtls;
    }

    public Party_Mdl getDispDtls() {
        return dispDtls;
    }

    public void setDispDtls(Party_Mdl dispDtls) {
        this.dispDtls = dispDtls;
    }

    public Party_Mdl getShipDtls() {
        return shipDtls;
    }

    public void setShipDtls(Party_Mdl shipDtls) {
        this.shipDtls = shipDtls;
    }

    public EInvValDtls_Mdl getValDtls() {
        return valDtls;
    }

    public void setValDtls(EInvValDtls_Mdl valDtls) {
        this.valDtls = valDtls;
    }

    public EinvExportDtls_Mdl getExpDtls() {
        return expDtls;
    }

    public void setExpDtls(EinvExportDtls_Mdl expDtls) {
        this.expDtls = expDtls;
    }

    public EinvPayeeDtls_Mdl getPayDtls() {
        return payDtls;
    }

    public void setPayDtls(EinvPayeeDtls_Mdl payDtls) {
        this.payDtls = payDtls;
    }

    public EinvRefDtls_Mdl getRefDtls() {
        return refDtls;
    }

    public void setRefDtls(EinvRefDtls_Mdl refDtls) {
        this.refDtls = refDtls;
    }

    public List<EinvAdditionalDocDtls_Mdl> getAddlDocDtls() {
        return addlDocDtls;
    }

    public void setAddlDocDtls(List<EinvAdditionalDocDtls_Mdl> addlDocDtls) {
        this.addlDocDtls = addlDocDtls;
    }

    public EInvEwbDtlsResponse_Mdl getEwbDtls() {
        return ewbDtls;
    }

    public void setEwbDtls(EInvEwbDtlsResponse_Mdl ewbDtls) {
        this.ewbDtls = ewbDtls;
    }

    public List<EInvItemDtls_Mdl> getItemList() {
        return itemList;
    }

    public void setItemList(List<EInvItemDtls_Mdl> itemList) {
        this.itemList = itemList;
    }

}
