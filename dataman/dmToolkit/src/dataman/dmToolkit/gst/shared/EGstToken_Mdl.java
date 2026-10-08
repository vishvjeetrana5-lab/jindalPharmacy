package dataman.dmToolkit.gst.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class EGstToken_Mdl implements Serializable, IsSerializable {

    public String userGstin;
    public String authtoken;
    public String tokenDt;
    public String sek;
    private String clientId;
    private String userName;
    private String tokenExpiry;

    public EGstToken_Mdl() {
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    public String getSek() {
        return sek;
    }

    public void setSek(String sek) {
        this.sek = sek;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTokenExpiry() {
        return tokenExpiry;
    }

    public void setTokenExpiry(String tokenExpiry) {
        this.tokenExpiry = tokenExpiry;
    }

    public String getTokenDt() {
        return tokenDt;
    }

    public void setTokenDt(String tokenDt) {
        this.tokenDt = tokenDt;
    }

    public String getUserGstin() {
        return userGstin;
    }

    public void setUserGstin(String userGstin) {
        this.userGstin = userGstin;
    }
  
}
