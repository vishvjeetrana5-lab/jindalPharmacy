package dataman.accounts.wholesaleBillSettlement.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class TxtHSupplier_Mdl extends KeyField implements Serializable, IsSerializable {

    private String manualCode;
    private String cityName;
    private String tdsCatCode;
    private String tdsCatName;
    private String displayName;

    public TxtHSupplier_Mdl() {
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getManualCode() {
        return manualCode;
    }

    public void setManualCode(String manualCode) {
        this.manualCode = manualCode;
    }

    public String getTdsCatCode() {
        return tdsCatCode;
    }

    public void setTdsCatCode(String tdsCatCode) {
        this.tdsCatCode = tdsCatCode;
    }

    public String getTdsCatName() {
        return tdsCatName;
    }

    public void setTdsCatName(String tdsCatName) {
        this.tdsCatName = tdsCatName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}
