/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataman.dmToolkit.gst.shared;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

/**
 *
 * @author DATAMAN
 */
public class ExtractedSignedQrCode_Mdl implements Serializable, IsSerializable {

    @JsonProperty("SellerGstin")
    private String sellerGstin;
    @JsonProperty("BuyerGstin")
    private String buyerGstin;
    @JsonProperty("DocNo")
    private String docNo;
    @JsonProperty("DocTyp")
    private String docTyp;
    @JsonProperty("DocDt")
    private String docDt;
    @JsonProperty("TotInvVal")
    private String totInvVal;
    @JsonProperty("ItemCnt")
    private String itemCnt;
    @JsonProperty("MainHsnCode")
    private String mainHsnCode;
    @JsonProperty("Irn")
    private String irn;

    public ExtractedSignedQrCode_Mdl() {
    }

    
    public String getSellerGstin() {
        return sellerGstin;
    }

    public void setSellerGstin(String sellerGstin) {
        this.sellerGstin = sellerGstin;
    }

    public String getBuyerGstin() {
        return buyerGstin;
    }

    public void setBuyerGstin(String buyerGstin) {
        this.buyerGstin = buyerGstin;
    }

    public String getDocNo() {
        return docNo;
    }

    public void setDocNo(String docNo) {
        this.docNo = docNo;
    }

    public String getDocTyp() {
        return docTyp;
    }

    public void setDocTyp(String docTyp) {
        this.docTyp = docTyp;
    }

    public String getDocDt() {
        return docDt;
    }

    public void setDocDt(String docDt) {
        this.docDt = docDt;
    }

    public String getTotInvVal() {
        return totInvVal;
    }

    public void setTotInvVal(String totInvVal) {
        this.totInvVal = totInvVal;
    }

    public String getItemCnt() {
        return itemCnt;
    }

    public void setItemCnt(String itemCnt) {
        this.itemCnt = itemCnt;
    }

    public String getMainHsnCode() {
        return mainHsnCode;
    }

    public void setMainHsnCode(String mainHsnCode) {
        this.mainHsnCode = mainHsnCode;
    }

    public String getIrn() {
        return irn;
    }

    public void setIrn(String irn) {
        this.irn = irn;
    }

}
