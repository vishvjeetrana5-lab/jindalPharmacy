package dataman.accounts.finalPaymentRequestEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String date;
    private String moduleTag;
    private String moduleText;
    private String supplierTag;
    private String supplierText;
    private String pbNo;
    private String finalDuePay;
    private String paymnetDt;

    public Find_Mdl() {

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getModuleText() {
        return moduleText;
    }

    public void setModuleText(String moduleText) {
        this.moduleText = moduleText;
    }

    public String getModuleTag() {
        return moduleTag;
    }

    public void setModuleTag(String moduleTag) {
        this.moduleTag = moduleTag;
    }

    public String getSupplierTag() {
        return supplierTag;
    }

    public void setSupplierTag(String supplierTag) {
        this.supplierTag = supplierTag;
    }

    public String getSupplierText() {
        return supplierText;
    }

    public void setSupplierText(String supplierText) {
        this.supplierText = supplierText;
    }



    public String getPbNo() {
        return pbNo;
    }

    public void setPbNo(String pbNo) {
        this.pbNo = pbNo;
    }

    public String getFinalDuePay() {
        return finalDuePay;
    }

    public void setFinalDuePay(String finalDuePay) {
        this.finalDuePay = finalDuePay;
    }

    public String getPaymnetDt() {
        return paymnetDt;
    }

    public void setPaymnetDt(String paymnetDt) {
        this.paymnetDt = paymnetDt;
    }

}
