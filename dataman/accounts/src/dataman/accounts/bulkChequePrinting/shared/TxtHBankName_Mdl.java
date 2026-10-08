package dataman.accounts.bulkChequePrinting.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHBankName_Mdl extends KeyField implements Serializable, IsSerializable {

    private String City;
    private String SCode;

    public TxtHBankName_Mdl() {
    }



    public String getSCode() {
        return SCode;
    }

    public void setSCode(String SCode) {
        this.SCode = SCode;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String City) {
        this.City = City;
    }

}
