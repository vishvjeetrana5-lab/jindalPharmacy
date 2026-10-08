package dataman.utility.docIdMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdDocIdMaster_Mdl extends KeyField implements Serializable,IsSerializable{

    private String voucherDescription;
    private String voucherSerise;

    public GrdDocIdMaster_Mdl() {
    }

    public String getVoucherDescription() {
        return voucherDescription;
    }

    public void setVoucherDescription(String voucherDescription) {
        this.voucherDescription = voucherDescription;
    }

    public String getVoucherSerise() {
        return voucherSerise;
    }

    public void setVoucherSerise(String voucherSerise) {
        this.voucherSerise = voucherSerise;
    }
}
