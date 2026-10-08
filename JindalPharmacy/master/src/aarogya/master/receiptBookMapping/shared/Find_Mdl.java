package aarogya.master.receiptBookMapping.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;

import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String entryVoucherName;
    private String receiptVoucherName;
    private String isDefault;

    public Find_Mdl() {
    }

    public String getEntryVoucherName() {
        return entryVoucherName;
    }

    public void setEntryVoucherName(String entryVoucherName) {
        this.entryVoucherName = entryVoucherName;
    }

    public String getReceiptVoucherName() {
        return receiptVoucherName;
    }

    public void setReceiptVoucherName(String receiptVoucherName) {
        this.receiptVoucherName = receiptVoucherName;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

}
