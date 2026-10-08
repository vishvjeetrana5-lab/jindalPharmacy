package dataman.dmToolkit.overHeadLine.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class SyncOverHeadMA_Mdl extends KeyField implements Serializable, IsSerializable {

    String amount;

    public SyncOverHeadMA_Mdl() {
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public SyncOverHeadMA_Mdl clone() {
        SyncOverHeadMA_Mdl overHeadMA_Mdl = new SyncOverHeadMA_Mdl();

        super.copy(overHeadMA_Mdl);

        overHeadMA_Mdl.setAmount(this.getAmount());

        return overHeadMA_Mdl;
    }
}
