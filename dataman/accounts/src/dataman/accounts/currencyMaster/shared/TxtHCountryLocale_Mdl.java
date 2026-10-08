package dataman.accounts.currencyMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHCountryLocale_Mdl extends KeyField implements Serializable, IsSerializable {

    private String countryName;
    private String currencyName;

    public String getCountryName() {
        return countryName;
    } 

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }
}
