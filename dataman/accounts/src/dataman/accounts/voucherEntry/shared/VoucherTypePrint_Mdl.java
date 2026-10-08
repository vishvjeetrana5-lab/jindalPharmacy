package dataman.accounts.voucherEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class VoucherTypePrint_Mdl extends KeyField implements Serializable, IsSerializable {

    private String category;

    public VoucherTypePrint_Mdl() {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
