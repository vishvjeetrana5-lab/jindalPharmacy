package dataman.accounts.additionalPlaceOfBusiness.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class TxtHCity_Mdl extends KeyField implements Serializable, IsSerializable {

    private String stateCode;
    private String stateName;
    private String stateNumericCode;
    private String countryName;

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getStateNumericCodeX() {
        return stateNumericCode;
    }

    public void setStateNumericCodeX(String stateNumericCode) {
        this.stateNumericCode = stateNumericCode;
    }

    public TxtHCity_Mdl() {
    }

}
