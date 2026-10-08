package dataman.accounts.budgetDisbursement.shared;

import dataman.accounts.budgetDisbursement.shared.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.List;

public class BudgetDisbursement_Vld {

    public static void chkSave(BudgetDisbursement_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getAllocationTypeTag(), "Allocation Type", 100);
        DmShared.isValidText(mrTemp.getDtAllocation(), "Date", 200);
        DmShared.isValidText(mrTemp.getBudgetNameText(), "Budget", 300);

        /*==============================<< other validations >>===============================*/
        boolean flag = false;
        List<GrdBudgetDisbursementDetail_Mdl> listBudgetHeadDetail = mrTemp.getGrdBudgetHeadDetails();
        for (GrdBudgetDisbursementDetail_Mdl detail_Mdl : listBudgetHeadDetail) {
            if (!DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                flag = true;
            }
            if (!DmShared.xNull(detail_Mdl.getFromDate()).trim().equalsIgnoreCase("") && !DmShared.xNull(detail_Mdl.getToDate()).trim().equalsIgnoreCase("")) {
                if (DmShared.convertDate(detail_Mdl.getToDate()).getTime() < DmShared.convertDate(detail_Mdl.getFromDate()).getTime()) {
                    throw new DmException(500, "Main", "0", "Incorrect date range in line No." + DmShared.iNull(listBudgetHeadDetail.indexOf(detail_Mdl) + 1).toString());
                }
            }
        }
        if (!flag) {
            throw new DmException(500, "Budget detail is mandatory.");
        }

    }
}
