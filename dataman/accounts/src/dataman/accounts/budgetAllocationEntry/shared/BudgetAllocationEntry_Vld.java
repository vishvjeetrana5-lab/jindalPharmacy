package dataman.accounts.budgetAllocationEntry.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.List;

public class BudgetAllocationEntry_Vld {

    public static void chkSave(BudgetAllocationEntry_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getAllocationTypeTag(), "Allocation Type", 100);
        DmShared.isValidText(mrTemp.getDtAllocation(), "Date", 200);
        DmShared.isValidText(mrTemp.getBudgetNameText(), "Budget", 300);
        DmShared.isValidText(mrTemp.getFromDate(), "From Date", 300);
        DmShared.isValidText(mrTemp.getFromDate(), "To Date", 300);

        /*==============================<< other validations >>===============================*/
        if (DmShared.xNull(mrTemp.getVoucherCategory()).trim().equalsIgnoreCase("BUDAL") &&
                DmShared.convertDate(mrTemp.getFromDate()).getTime() < DmShared.convertDate(mrTemp.getDtAllocation()).getTime()) {
            throw new DmException(500, "Main", "0", "From Date can't be less than Budget Date.");
        }

        if (DmShared.convertDate(mrTemp.getToDate()).getTime() < DmShared.convertDate(mrTemp.getFromDate()).getTime()) {
            throw new DmException(500, "Main", "0", "From Date can't be greater than To Date.");
        }

        if (DmShared.xNull(mrTemp.getVoucherCategory()).trim().equalsIgnoreCase("BUDTR")
                && DmShared.vNull(mrTemp.getTotalBudget()).doubleValue() != 0) {
            throw new DmException(500, "Main", "0", "Total Budget must be 0 in case of Budget Transfer.");
        }

        boolean flag = false;
        List<GrdBudgetAllocationDetail_Mdl> listBudgetHeadDetail = mrTemp.getGrdBudgetHeadDetails();
        for (GrdBudgetAllocationDetail_Mdl detail_Mdl : listBudgetHeadDetail) {
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
