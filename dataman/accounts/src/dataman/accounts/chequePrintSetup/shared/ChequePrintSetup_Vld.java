package dataman.accounts.chequePrintSetup.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class ChequePrintSetup_Vld {

    public static void chkSave(ChequePrintSetup_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getBankCode().trim(), "Bank Name", 100);

    }
}
