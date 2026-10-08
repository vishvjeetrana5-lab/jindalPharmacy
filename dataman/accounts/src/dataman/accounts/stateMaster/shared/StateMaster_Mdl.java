package dataman.accounts.stateMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class StateMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String numericCode;
    private String textCode;
    private String utCode;
    private String utName;

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

    public String getUtCode() {
        return utCode;
    }

    public void setUtCode(String utCode) {
        this.utCode = utCode;
    }

    public String getUtName() {
        return utName;
    }

    public void setUtName(String utName) {
        this.utName = utName;
    }

}
