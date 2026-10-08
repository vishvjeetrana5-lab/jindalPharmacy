package dataman.accounts.reports.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHItemMedCompany_Mdl extends KeyField implements Serializable, IsSerializable {

    private String shortName;
    private String unitCode;
    private String unit;
    private String manualCode;

    public TxtHItemMedCompany_Mdl() {
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getManualCode() {
        return manualCode;
    }

    public void setManualCode(String manualCode) {
        this.manualCode = manualCode;
    }

}
