/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dataman.dmToolkit.gst.shared;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

/**
 *
 * @author Lenovo
 */
public class EinvExportDtls_Mdl implements Serializable, IsSerializable {

    @JsonProperty("ShipBNo")
    private String shippingBillNo;
    @JsonProperty("ShipBDt")
    private String shippingBillDate;
    @JsonProperty("CntCode")
    private Object gstCountryCode;
    @JsonProperty("ForCur")
    private String currencyCode;
    @JsonProperty("InvForCur")
    private Object invForCur;
    @JsonProperty("Port")
    private String portCode;
    @JsonProperty("RefClm")
    private String refundClaim;

    public EinvExportDtls_Mdl() {
    }

    public String getShippingBillNo() {
        return shippingBillNo;
    }

    public void setShippingBillNo(String shippingBillNo) {
        this.shippingBillNo = shippingBillNo;
    }

    public String getShippingBillDate() {
        return shippingBillDate;
    }

    public void setShippingBillDate(String shippingBillDate) {
        this.shippingBillDate = shippingBillDate;
    }

    public Object getGstCountryCode() {
        return gstCountryCode;
    }

    public void setGstCountryCode(Object gstCountryCode) {
        this.gstCountryCode = gstCountryCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Object getInvForCur() {
        return invForCur;
    }

    public void setInvForCur(Object invForCur) {
        this.invForCur = invForCur;
    }

    public String getPortCode() {
        return portCode;
    }

    public void setPortCode(String portCode) {
        this.portCode = portCode;
    }

    public String getRefundClaim() {
        return refundClaim;
    }

    public void setRefundClaim(String refundClaim) {
        this.refundClaim = refundClaim;
    }

}

