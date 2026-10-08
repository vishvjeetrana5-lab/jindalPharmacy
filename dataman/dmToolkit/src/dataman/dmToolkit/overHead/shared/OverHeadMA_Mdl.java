package dataman.dmToolkit.overHead.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class OverHeadMA_Mdl extends KeyField implements Serializable, IsSerializable {

    String amount;

    public OverHeadMA_Mdl() {
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public OverHeadMA_Mdl clone() {
        OverHeadMA_Mdl overHeadMA_Mdl = new OverHeadMA_Mdl();

        super.copy(overHeadMA_Mdl);

        overHeadMA_Mdl.setAmount(this.getAmount());

        return overHeadMA_Mdl;
    }
}
