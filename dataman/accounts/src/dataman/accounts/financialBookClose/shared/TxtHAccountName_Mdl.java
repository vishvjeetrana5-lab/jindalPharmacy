package dataman.accounts.financialBookClose.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class TxtHAccountName_Mdl extends KeyField implements Serializable, IsSerializable {

    private String manualCode;
    private String groupName;
    private String cityName;
    private String address1;
    private String address2;
    private String accountNameAccDesc;

    public TxtHAccountName_Mdl() {
    }

    public String getManualCode() {
        return manualCode;
    }

    public void setManualCode(String manualCode) {
        this.manualCode = manualCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAccountNameAccDesc() {
        return accountNameAccDesc;
    }

    public void setAccountNameAccDesc(String accountNameAccDesc) {
        this.accountNameAccDesc = accountNameAccDesc;
    }

}
