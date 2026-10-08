package dataman.accounts.currencyMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class CurrencyMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String countryLocale;

    public CurrencyMaster_Mdl() {
    }

    public String getCountryLocale() {
        return countryLocale;
    }

    public void setCountryLocale(String countryLocale) {
        this.countryLocale = countryLocale;
    }
}
