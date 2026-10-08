package dataman.dmToolkit.eGstPopup.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHCity_Mdl extends KeyField implements Serializable, IsSerializable {

    private String stateCode;
    private String stateName;
    private String districtCode;
    private String districtName;
    private String stateNumericCode;
    private String countryCode;
    private String gstCountryCode;
    private String countryName;

    public TxtHCity_Mdl() {
    }

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

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getStateNumericCode() {
        return stateNumericCode;
    }

    public void setStateNumericCode(String stateNumericCode) {
        this.stateNumericCode = stateNumericCode;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getGstCountryCode() {
        return gstCountryCode;
    }

    public void setGstCountryCode(String gstCountryCode) {
        this.gstCountryCode = gstCountryCode;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

}
