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
public class Eway_Mdl implements Serializable, IsSerializable{

    @JsonProperty("EwbNo")
    private String ewayBillNo;
    @JsonProperty("EwbDt")
    private String ewayBillDate;
    @JsonProperty("EwbValidTill")
    private String validUpto;
    private String alert;
    
   

    public Eway_Mdl() {
    }

    public String getEwayBillNo() {
        return ewayBillNo;
    }

    public void setEwayBillNo(String ewayBillNo) {
        this.ewayBillNo = ewayBillNo;
    }

    public String getEwayBillDate() {
        return ewayBillDate;
    }

    public void setEwayBillDate(String ewayBillDate) {
        this.ewayBillDate = ewayBillDate;
    }

    public String getValidUpto() {
        return validUpto;
    }

    public void setValidUpto(String validUpto) {
        this.validUpto = validUpto;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }
    
}
