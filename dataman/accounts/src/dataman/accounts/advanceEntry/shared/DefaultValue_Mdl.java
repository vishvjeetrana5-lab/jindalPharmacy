package dataman.accounts.advanceEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class DefaultValue_Mdl extends KeyField implements Serializable, IsSerializable {

    private String paymentMode;

    public DefaultValue_Mdl() {
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }


}
