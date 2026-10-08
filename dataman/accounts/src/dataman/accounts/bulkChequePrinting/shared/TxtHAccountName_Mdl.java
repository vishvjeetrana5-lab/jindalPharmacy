package dataman.accounts.bulkChequePrinting.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHAccountName_Mdl extends KeyField implements Serializable, IsSerializable {

    //sharedKeyFieldCode=code
    //sharedKeyFieldName=name
    private String city;
    private String manualCode;

    public TxtHAccountName_Mdl() {
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getManualCode() {
        return manualCode;
    }

    public void setManualCode(String manualCode) {
        this.manualCode = manualCode;
    }

}
