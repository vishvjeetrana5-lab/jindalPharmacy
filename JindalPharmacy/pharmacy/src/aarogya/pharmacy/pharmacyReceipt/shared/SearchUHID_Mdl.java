package aarogya.pharmacy.pharmacyReceipt.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class SearchUHID_Mdl implements Serializable, IsSerializable {

    private String regDocId, regRecId;
    private String voucherCategory, voucherDate;

    public SearchUHID_Mdl() {
    }

    public String getRegDocId() {
        return regDocId;
    }

    public void setRegDocId(String regDocId) {
        this.regDocId = regDocId;
    }

    public String getRegRecId() {
        return regRecId;
    }

    public void setRegRecId(String regRecId) {
        this.regRecId = regRecId;
    }

    public String getVoucherCategory() {
        return voucherCategory;
    }

    public void setVoucherCategory(String voucherCategory) {
        this.voucherCategory = voucherCategory;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }
}
