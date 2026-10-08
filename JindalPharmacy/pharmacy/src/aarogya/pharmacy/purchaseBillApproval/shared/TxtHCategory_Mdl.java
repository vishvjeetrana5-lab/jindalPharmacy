package aarogya.pharmacy.purchaseBillApproval.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;

public class TxtHCategory_Mdl extends KeyField implements Serializable, IsSerializable {

    private String description;

    public TxtHCategory_Mdl() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
