package dataman.accounts.accountGroup.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class AccountGroup_Vld {

    public static void chkSave(AccountGroup_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "Account Group", 100);
        DmShared.isValidText(mrTemp.getContraGroupName(), "Contra Group", 200);
        DmShared.isValidText(mrTemp.getGroupNature(), "Main Group", 300);
        DmShared.isValidText(mrTemp.getNatureCode(), "Nature", 400);
    }

}
