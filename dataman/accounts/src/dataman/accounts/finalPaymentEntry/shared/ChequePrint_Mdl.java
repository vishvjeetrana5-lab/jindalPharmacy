package dataman.accounts.finalPaymentEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class ChequePrint_Mdl extends KeyField implements Serializable, IsSerializable {

    private String voucherTypeTag;
    private String accountCode;
    private String headerAccountCode;

    public ChequePrint_Mdl() {
    }

    public String getHeaderAccountCode() {
        return headerAccountCode;
    }

    public void setHeaderAccountCode(String headerAccountCode) {
        this.headerAccountCode = headerAccountCode;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getVoucherTypeTag() {
        return voucherTypeTag;
    }

    public void setVoucherTypeTag(String voucherTypeTag) {
        this.voucherTypeTag = voucherTypeTag;
    }

}
