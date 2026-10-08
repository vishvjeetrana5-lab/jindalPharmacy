
package aarogya.login.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;


public class ForgetPassword_Mdl implements Serializable, IsSerializable {

    private String pltCode;
    private String pltName;
    private String companyCode;
    private String fyName;
    private String userName;
    private String mobile;
    private String email;

    public ForgetPassword_Mdl() {
    }

    public String getPltCode() {
        return pltCode;
    }

    public void setPltCode(String pltCode) {
        this.pltCode = pltCode;
    }

    public String getPltName() {
        return pltName;
    }

    public void setPltName(String pltName) {
        this.pltName = pltName;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getFyName() {
        return fyName;
    }

    public void setFyName(String fyName) {
        this.fyName = fyName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
