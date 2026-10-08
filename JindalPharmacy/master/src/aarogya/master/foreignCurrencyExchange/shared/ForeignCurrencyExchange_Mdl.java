package aarogya.master.foreignCurrencyExchange.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ForeignCurrencyExchange_Mdl extends KeyField implements Serializable, IsSerializable {

    private String exchangeDate;
    private boolean newMode;
    private List<GrdForeignCurrencyExchange_Mdl> lstCurrencyExchange = new ArrayList<GrdForeignCurrencyExchange_Mdl>();

    public ForeignCurrencyExchange_Mdl() {

    }

    public boolean isNewMode() {
        return newMode;
    }

    public void setNewMode(boolean newMode) {
        this.newMode = newMode;
    }

    public String getExchangeDate() {
        return exchangeDate;
    }

    public void setExchangeDate(String exchangeDate) {
        this.exchangeDate = exchangeDate;
    }

    public List<GrdForeignCurrencyExchange_Mdl> getLstCurrencyExchange() {
        return lstCurrencyExchange;
    }
}
