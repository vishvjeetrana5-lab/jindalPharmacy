package dataman.utility.siteMaster.shared;

import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHBranch_Mdl extends KeyField implements Serializable {

    private String cityName;

    public TxtHBranch_Mdl() {
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

}
