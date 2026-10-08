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
public class EinvPayeeDtls_Mdl implements Serializable, IsSerializable {
    
    @JsonProperty("Nm") 
    private String nm;
    @JsonProperty("AccDet") 
    private Object accDet;
    @JsonProperty("Mode") 
    private String mode;
    @JsonProperty("FinInsBr") 
    private String finInsBr;
    @JsonProperty("CrTrn") 
    private String crTrn;
    @JsonProperty("PayInstr") 
    private String payInstr;
    @JsonProperty("PayTerm") 
    private String payTerm;
    @JsonProperty("DirDr") 
    private String dirDr;
    @JsonProperty("CrDay") 
    private int crDay;
    @JsonProperty("PaidAmt") 
    private int paidAmt;
    @JsonProperty("PaymtDue") 
    private int paymtDue;

    public EinvPayeeDtls_Mdl() {
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public Object getAccDet() {
        return accDet;
    }

    public void setAccDet(Object accDet) {
        this.accDet = accDet;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getFinInsBr() {
        return finInsBr;
    }

    public void setFinInsBr(String finInsBr) {
        this.finInsBr = finInsBr;
    }

    public String getCrTrn() {
        return crTrn;
    }

    public void setCrTrn(String crTrn) {
        this.crTrn = crTrn;
    }

    public String getPayInstr() {
        return payInstr;
    }

    public void setPayInstr(String payInstr) {
        this.payInstr = payInstr;
    }

    public String getPayTerm() {
        return payTerm;
    }

    public void setPayTerm(String payTerm) {
        this.payTerm = payTerm;
    }

    public String getDirDr() {
        return dirDr;
    }

    public void setDirDr(String dirDr) {
        this.dirDr = dirDr;
    }

    public int getCrDay() {
        return crDay;
    }

    public void setCrDay(int crDay) {
        this.crDay = crDay;
    }

    public int getPaidAmt() {
        return paidAmt;
    }

    public void setPaidAmt(int paidAmt) {
        this.paidAmt = paidAmt;
    }

    public int getPaymtDue() {
        return paymtDue;
    }

    public void setPaymtDue(int paymtDue) {
        this.paymtDue = paymtDue;
    }
    
    
    
}
