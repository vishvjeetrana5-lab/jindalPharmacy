package dataman.dmBase.self.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class PLT implements Serializable,IsSerializable{

    private byte[] pltCode;
    private byte[] pltName;
    private byte[] userLinkedAccountCode;
    private byte[] userLinkedAccountName;

    public PLT() {
    }

    public String getPLTCode() {
        return new String(pltCode);
    }

    public void setPLTCode(String pltCode) {
        this.pltCode = pltCode.getBytes();
    }

    public String getPLTName() {
        return new String(pltName);
    }

    public void setPLTName(String pltName) {
        this.pltName = pltName.getBytes();
    }

    public String getUserLinkedAccountCode() {
        return new String(userLinkedAccountCode);
    }

    public void setUserLinkedAccountCode(String userLinkedAccountCode) {
        this.userLinkedAccountCode = userLinkedAccountCode.getBytes();
    }
    
    public String getUserLinkedAccountName() {
        return new String(userLinkedAccountName);
    }

    public void setUserLinkedAccountName(String userLinkedAccountName) {
        this.userLinkedAccountName = userLinkedAccountName.getBytes();
    }
}
