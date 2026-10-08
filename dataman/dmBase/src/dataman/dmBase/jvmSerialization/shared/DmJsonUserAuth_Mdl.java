package dataman.dmBase.jvmSerialization.shared;

import com.google.gson.annotations.SerializedName;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.DmEnum;
import java.io.Serializable;

public class DmJsonUserAuth_Mdl implements Serializable,IsSerializable{

    @SerializedName(value = "userName")
    private byte[] userName;
    
    @SerializedName(value = "password")
    private byte[] password;
    
    @SerializedName(value = "userValidationMode")
    private DmEnum.DmUserValidationMode userValidationMode = DmEnum.DmUserValidationMode.USERNAME;

    public String getUserName() {
        return new String(userName);
    }

    public void setUserName(String userName) {
        this.userName = userName.getBytes();
    }

    public String getPassword() {
        return new String(password);
    }

    public void setPassword(String password) {
        this.password = password.getBytes();
    }

    public DmEnum.DmUserValidationMode getUserValidationMode() {
        return userValidationMode;
    }

    public void setUserValidationMode(DmEnum.DmUserValidationMode userValidationMode) {
        this.userValidationMode = userValidationMode;
    }

}
