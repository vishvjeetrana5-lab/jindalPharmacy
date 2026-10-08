package dataman.accounts.budgetHeadMaster.shared;

import dataman.accounts.accountGroup.shared.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class BudgetHeadMaster_Vld {

    public static void chkSave(BudgetHeadMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "Budget Head Name", 100);
        DmShared.isValidText(mrTemp.getManualCode(), "Manual Code", 200);
    }

}
