package aarogya.pharmacy.profitSlab.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.DmEnum;
import java.io.Serializable;

public class DefaultValue_Mdl implements Serializable, IsSerializable {

    private int rateDecimalPlaces;
    private DmEnum.DmLocaleCountry localeCountry;

    public DefaultValue_Mdl() {
    }

    public int getRateDecimalPlaces() {
        return rateDecimalPlaces;
    }

    public void setRateDecimalPlaces(int rateDecimalPlaces) {
        this.rateDecimalPlaces = rateDecimalPlaces;
    }

    public DmEnum.DmLocaleCountry getLocaleCountry() {
        return localeCountry;
    }

    public void setLocaleCountry(DmEnum.DmLocaleCountry localeCountry) {
        this.localeCountry = localeCountry;
    }

}
