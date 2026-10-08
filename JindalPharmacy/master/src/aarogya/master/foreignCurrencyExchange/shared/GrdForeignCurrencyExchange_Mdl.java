package aarogya.master.foreignCurrencyExchange.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdForeignCurrencyExchange_Mdl extends KeyField implements Serializable, IsSerializable {

    private String base;
    private String rate;

    public GrdForeignCurrencyExchange_Mdl() {

    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
