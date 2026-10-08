package dataman.accounts.nationalityMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class NationalityMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String countryLocale;

    public NationalityMaster_Mdl() {
    }

    public String getCountryLocale() {
        return countryLocale;
    }

    public void setCountryLocale(String countryLocale) {
        this.countryLocale = countryLocale;
    }
}
