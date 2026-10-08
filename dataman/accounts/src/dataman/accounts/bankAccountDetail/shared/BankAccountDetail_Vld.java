package dataman.accounts.bankAccountDetail.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class BankAccountDetail_Vld {

    public static void chkSave(BankAccountDetail_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldCode(), "Link With A/c", 100);
        DmShared.isValidText(mrTemp.getAccountNo(), "Account No.", 200);
        DmShared.isValidText(mrTemp.getBankName(), "Bank", 300);
        DmShared.isValidText(mrTemp.getBankBranch(), "Branch", 400);
        DmShared.isValidText(mrTemp.getDefaultTag(), "Default", 500);
    }

}
