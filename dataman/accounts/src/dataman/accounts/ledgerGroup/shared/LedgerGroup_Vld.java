package dataman.accounts.ledgerGroup.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class LedgerGroup_Vld {

    public static void chkSave(LedgerGroup_Mdl mrTemp) {
        DmShared.isValidText(DmShared.xNull(mrTemp.getSharedKeyFieldName()), "Ledger Group", 100);
      
    }
}
