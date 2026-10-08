package aarogya.pharmacy.wholeSaleBillReturn.shared;

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
    private boolean allowCredittoRoom;

    public TxtHSaleBillNo_Mdl() {
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

    public boolean isAllowCredittoRoom() {
        return allowCredittoRoom;
    }

    public void setAllowCredittoRoom(boolean allowCredittoRoom) {
        this.allowCredittoRoom = allowCredittoRoom;
    }

}
