package dataman.accounts.finalPaymentRequestEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class GrdPBillSearchDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    private String billType;
    private String pbDate;
    private String pbAmount;
    private String supplierTag;
    private String supplierText;
    private String approveDt;
    private boolean remove;
   
    public GrdPBillSearchDetail_Mdl() {

    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getPbDate() {
        return pbDate;
    }

    public void setPbDate(String pbDate) {
        this.pbDate = pbDate;
    }

    public String getPbAmount() {
        return pbAmount;
    }

    public void setPbAmount(String pbAmount) {
        this.pbAmount = pbAmount;
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

    public String getApproveDt() {
        return approveDt;
    }

    public void setApproveDt(String approveDt) {
        this.approveDt = approveDt;
    }

    public boolean isRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }
    
}
