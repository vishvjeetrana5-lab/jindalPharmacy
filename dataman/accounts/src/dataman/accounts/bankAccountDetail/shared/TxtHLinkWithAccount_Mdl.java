package dataman.accounts.bankAccountDetail.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHLinkWithAccount_Mdl extends KeyField implements Serializable, IsSerializable {

    private String ManualCode;
    private String City;

    public TxtHLinkWithAccount_Mdl() {
    }

    public String getManualCode() {
        return ManualCode;
    }

    public void setManualCode(String ManualCode) {
        this.ManualCode = ManualCode;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String City) {
        this.City = City;
    }

}
