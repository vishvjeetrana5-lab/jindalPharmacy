package dataman.accounts.gstVoucherEntryApproval.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;

public class TxtHVoucherNo_Mdl extends KeyField implements Serializable, IsSerializable {

    private String reffNo;
    private String reffDate;

    public TxtHVoucherNo_Mdl() {
    }

    public String getReffNo() {
        return reffNo;
    }

    public void setReffNo(String reffNo) {
        this.reffNo = reffNo;
    }

    public String getReffDate() {
        return reffDate;
    }

    public void setReffDate(String reffDate) {
        this.reffDate = reffDate;
    }

}
