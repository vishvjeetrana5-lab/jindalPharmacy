package aarogya.ribbon.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class UserInfo_Mdl implements Serializable, IsSerializable {

    private String userName, userDescription, eMail;
    private String buidDetail;
    private String versionDetail;

    public UserInfo_Mdl() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getBuidDetail() {
        return buidDetail;
    }

    public void setBuidDetail(String buidDetail) {
        this.buidDetail = buidDetail;
    }

    public String getVersionDetail() {
        return versionDetail;
    }

    public void setVersionDetail(String versionDetail) {
        this.versionDetail = versionDetail;
    }
}
