package dataman.dmBase.self.shared;

import dataman.dmBase.dmBaseStatic.shared.DmBaseShared; 


public class UserMaster_Vld {

    public static void chkSave(UserMaster_Mdl mrTemp) {
        DmBaseShared.isValidText(mrTemp.getSharedKeyFieldName(), "User Name", 100);
        DmBaseShared.isValidText(mrTemp.getAdministratorYNCode(), "Administrator", 200);
        DmBaseShared.isValidText(mrTemp.getEmail(), "eMail", 300);
        DmBaseShared.isValidEMail(mrTemp.getEmail(), 400);
        DmBaseShared.isValidText(mrTemp.getActiveYNCode(), "Is Active", 500);
    }
}
