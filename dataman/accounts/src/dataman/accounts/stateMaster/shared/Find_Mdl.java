package dataman.accounts.stateMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String numericCode;
    private String textCode;
    private String isUT;

    public Find_Mdl() {
    }

    public String getNumericCode() {
        return numericCode;
    }

    public void setNumericCode(String numericCode) {
        this.numericCode = numericCode;
    }

    public String getTextCode() {
        return textCode;
    }

    public void setTextCode(String textCode) {
        this.textCode = textCode;
    }

    public String getIsUT() {
        return isUT;
    }

    public void setIsUT(String isUT) {
        this.isUT = isUT;
    }

}
