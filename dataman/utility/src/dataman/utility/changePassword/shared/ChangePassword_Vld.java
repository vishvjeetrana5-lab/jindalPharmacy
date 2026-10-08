package dataman.utility.changePassword.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;

public class ChangePassword_Vld {

    public static void chkSave(ChangePassword_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getOldPassword(), "Old Password", 100);
        DmShared.isValidText(mrTemp.getNewPassword(), "New Password", 200);
        DmShared.isValidText(mrTemp.getConfirmPasword(), "Confirm Password", 300);
        if (!DmShared.xNull(mrTemp.getNewPassword()).trim().toUpperCase().equalsIgnoreCase(DmShared.xNull(mrTemp.getConfirmPasword()))) {
            throw new DmException(400, "Confirm Password Not Matched.");
        }
    }
}
