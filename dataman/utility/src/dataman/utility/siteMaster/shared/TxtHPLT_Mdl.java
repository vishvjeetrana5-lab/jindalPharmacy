package dataman.utility.siteMaster.shared;

import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHPLT_Mdl extends KeyField implements Serializable {

    private String shortName;

    public TxtHPLT_Mdl() {
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

}
