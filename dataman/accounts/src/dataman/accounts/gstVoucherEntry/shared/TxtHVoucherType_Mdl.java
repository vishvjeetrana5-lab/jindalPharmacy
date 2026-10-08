package dataman.accounts.gstVoucherEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHVoucherType_Mdl extends KeyField implements Serializable, IsSerializable {

    private String category;

    public TxtHVoucherType_Mdl() {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
