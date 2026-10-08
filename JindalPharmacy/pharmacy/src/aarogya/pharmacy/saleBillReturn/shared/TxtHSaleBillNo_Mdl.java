package aarogya.pharmacy.saleBillReturn.shared;

import aarogya.selfToolkit.cardPaymentPopup.shared.CardPaymentPopup_Mdl;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

/**
 *
 * @author Dataman
 */
public class TxtHSaleBillNo_Mdl extends KeyField implements IsSerializable, Serializable {

    //sharedKeyFieldCode=saleBillDocId (docid)
    //sharedKeyFieldName=saleBillNo(recId)
    private String lmCode;
    private String lmName;
    private String lmManualCode;
    private String patient;
    private String saleBillDate;
    private String departmentCode;
    private String departmentName;
    private String v_Type;
    private String netAmount;
    private String consultantName;
    private String consultantCode;
    private String paymentSourceCode;
    private String paymentSourceName;
    private String regTypeHIS;

    private String regNo_HIS;
    private String uhidNo_HIS;
    private String regDocId_HIS;
    private String siteCode_HIS;
    private boolean allowCredittoRoom;

    public TxtHSaleBillNo_Mdl() {
    }

    public String getLmName() {
        return lmName;
    }

    public void setLmName(String lmName) {
        this.lmName = lmName;
    }

    public String getLmManualCode() {
        return lmManualCode;
    }

    public void setLmManualCode(String lmManualCode) {
        this.lmManualCode = lmManualCode;
    }

    public String getConsultantName() {
        return consultantName;
    }

    public void setConsultantName(String consultantName) {
        this.consultantName = consultantName;
    }

    public String getConsultantCode() {
        return consultantCode;
    }

    public void setConsultantCode(String consultantCode) {
        this.consultantCode = consultantCode;
    }

    public String getV_Type() {
        return v_Type;
    }

    public void setV_Type(String v_Type) {
        this.v_Type = v_Type;
    }

    public String getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(String netAmount) {
        this.netAmount = netAmount;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getSaleBillDate() {
        return saleBillDate;
    }

    public void setSaleBillDate(String saleBillDate) {
        this.saleBillDate = saleBillDate;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getLmCode() {
        return lmCode;
    }

    public void setLmCode(String lmCode) {
        this.lmCode = lmCode;
    }

    public String getPaymentSourceCode() {
        return paymentSourceCode;
    }

    public void setPaymentSourceCode(String paymentSourceCode) {
        this.paymentSourceCode = paymentSourceCode;
    }

    public String getPaymentSourceName() {
        return paymentSourceName;
    }

    public void setPaymentSourceName(String paymentSourceName) {
        this.paymentSourceName = paymentSourceName;
    }

    public String getRegTypeHIS() {
        return regTypeHIS;
    }

    public void setRegTypeHIS(String regTypeHIS) {
        this.regTypeHIS = regTypeHIS;
    }

    public String getRegNo_HIS() {
        return regNo_HIS;
    }

    public void setRegNo_HIS(String regNo_HIS) {
        this.regNo_HIS = regNo_HIS;
    }

    public String getUhidNo_HIS() {
        return uhidNo_HIS;
    }

    public void setUhidNo_HIS(String uhidNo_HIS) {
        this.uhidNo_HIS = uhidNo_HIS;
    }

    public String getRegDocId_HIS() {
        return regDocId_HIS;
    }

    public void setRegDocId_HIS(String regDocId_HIS) {
        this.regDocId_HIS = regDocId_HIS;
    }

    public String getSiteCode_HIS() {
        return siteCode_HIS;
    }

    public void setSiteCode_HIS(String siteCode_HIS) {
        this.siteCode_HIS = siteCode_HIS;
    }

    public boolean isAllowCredittoRoom() {
        return allowCredittoRoom;
    }

    public void setAllowCredittoRoom(boolean allowCredittoRoom) {
        this.allowCredittoRoom = allowCredittoRoom;
    }

}
