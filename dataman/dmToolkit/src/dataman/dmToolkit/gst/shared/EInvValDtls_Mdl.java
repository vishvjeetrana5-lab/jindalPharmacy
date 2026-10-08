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
public class EInvValDtls_Mdl implements Serializable, IsSerializable{
    
    @JsonProperty("AssVal")
    private String assVal;
    @JsonProperty("CgstVal")
    private String cgstVal;
    @JsonProperty("SgstVal")
    private String sgstVal;
    @JsonProperty("IgstVal")
    private String igstVal;
    @JsonProperty("CesVal")
    private String cesVal;
    @JsonProperty("StCesVal")
    private String stCesVal;
    @JsonProperty("Discount")
    private String discount;
    @JsonProperty("OthChrg")
    private String othChrg;
    @JsonProperty("RndOffAmt")
    private String rndOffAmt;
    @JsonProperty("TotInvVal")
    private String totInvVal;
    @JsonProperty("TotInvValFc")
    private String totInvValFc;

    public EInvValDtls_Mdl() {
    }

    
    public String getAssVal() {
        return assVal;
    }

    public void setAssVal(String assVal) {
        this.assVal = assVal;
    }

    public String getCgstVal() {
        return cgstVal;
    }

    public void setCgstVal(String cgstVal) {
        this.cgstVal = cgstVal;
    }

    public String getSgstVal() {
        return sgstVal;
    }

    public void setSgstVal(String sgstVal) {
        this.sgstVal = sgstVal;
    }

    public String getIgstVal() {
        return igstVal;
    }

    public void setIgstVal(String igstVal) {
        this.igstVal = igstVal;
    }

    public String getCesVal() {
        return cesVal;
    }

    public void setCesVal(String cesVal) {
        this.cesVal = cesVal;
    }

    public String getStCesVal() {
        return stCesVal;
    }

    public void setStCesVal(String stCesVal) {
        this.stCesVal = stCesVal;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getOthChrg() {
        return othChrg;
    }

    public void setOthChrg(String othChrg) {
        this.othChrg = othChrg;
    }

    public String getRndOffAmt() {
        return rndOffAmt;
    }

    public void setRndOffAmt(String rndOffAmt) {
        this.rndOffAmt = rndOffAmt;
    }

    public String getTotInvVal() {
        return totInvVal;
    }

    public void setTotInvVal(String totInvVal) {
        this.totInvVal = totInvVal;
    }

    public String getTotInvValFc() {
        return totInvValFc;
    }

    public void setTotInvValFc(String totInvValFc) {
        this.totInvValFc = totInvValFc;
    }
    
}
