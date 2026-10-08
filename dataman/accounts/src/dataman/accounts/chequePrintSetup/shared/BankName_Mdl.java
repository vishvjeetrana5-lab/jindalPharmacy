package dataman.accounts.chequePrintSetup.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class BankName_Mdl extends KeyField implements Serializable, IsSerializable {

    //sharedKeyFieldCode=bankCode
    //sharedKeyFieldName=bankName
    public BankName_Mdl() {
    }

    private String manualCode;
    private String cityCode;
    private String cityName;

    public String getManualCode() {
        return manualCode;
    }

    public void setManualCode(String manualCode) {
        this.manualCode = manualCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

}
