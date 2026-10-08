package dataman.accounts.addressBook.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class AddressBook_Vld {

    public static void chkSave(AddressBook_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getCompanyGroup(), "Account/Group Name", 100);
        DmShared.isValidText(mrTemp.getNature(), "Nature", 200);
        DmShared.isValidText(mrTemp.getContactPerson(), "Contact Person", 300);
        DmShared.isValidEMail(mrTemp.getEmail(), 400);
        DmShared.isValidText(mrTemp.getIsDefaultTag(), "Default", 350);
    }

}
