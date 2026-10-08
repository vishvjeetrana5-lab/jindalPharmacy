package aarogya.login.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class Login_Validation {

    public static void chkLogin(Login_Model.Body mrTemp) {
        if (DmShared.xNull(mrTemp.getUserName()).equalsIgnoreCase("")) {
            throw new UnsupportedOperationException("User Name is mandatory.");
        }
//        if (DmShared.xNull(mrTemp.getPassword()).equalsIgnoreCase("")){throw new UnsupportedOperationException("Password is mandatory.");}
    }
}
