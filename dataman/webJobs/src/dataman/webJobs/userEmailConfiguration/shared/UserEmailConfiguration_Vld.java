package dataman.webJobs.userEmailConfiguration.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;

public class UserEmailConfiguration_Vld {

    public static void chkSave(GrdUserEmaiEdit_Mdl mrTemp) {
//        DmShared.isValidEMail(mrTemp.getEmail(), 100);
        DmShared.isValidText(mrTemp.getEmail(), "Email", 100);
        DmShared.isValidText(mrTemp.getPassword(), "Password", 200);
        if (!DmShared.xNull(mrTemp.getPassword()).equals(DmShared.xNull(mrTemp.getConfirmPassword()))) {
            throw new DmException(300, "Confirm Password Not Matched.");
        }

    }
}
