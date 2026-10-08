package redirect.changePassword.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class ChangePassword_Mdl extends KeyField implements Serializable, IsSerializable {

    private String oldPassword;
    private String userName;
    private String userNatureCode;
    private String newPassword;
    private String confirmPasword;

    public ChangePassword_Mdl() {
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPasword() {
        return confirmPasword;
    }

    public void setConfirmPasword(String confirmPasword) {
        this.confirmPasword = confirmPasword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNatureCode() {
        return userNatureCode;
    }

    public void setUserNatureCode(String userNatureCode) {
        this.userNatureCode = userNatureCode;
    }
}
