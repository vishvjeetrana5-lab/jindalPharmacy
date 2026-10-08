package aarogya.inventory.purchaseBillApproval.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;

public class TxtHIndentNo_Mdl extends KeyField implements Serializable, IsSerializable {

    private String indentType;
    private String indentDate;

    public TxtHIndentNo_Mdl() {
    }

    public String getIndentType() {
        return indentType;
    }

    public void setIndentType(String indentType) {
        this.indentType = indentType;
    }

    public String getIndentDate() {
        return indentDate;
    }

    public void setIndentDate(String indentDate) {
        this.indentDate = indentDate;
    }

}
