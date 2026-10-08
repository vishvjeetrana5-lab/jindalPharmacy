package dataman.payroll.loanEntry.shared;

import dataman.payroll.loanEntry.shared.*;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class LoanEntry_Vld {

    public static void chkSave(LoanEntry_Mdl scMdl) {

    DmShared.isValidText(scMdl.getEmployeeCode(), "Employee", 100);
    DmShared.isValidText(scMdl.getLoanAmount(), "Loan Amount", 200);
    DmShared.isValidText(scMdl.getForMonth(), "For Month", 300);
    DmShared.isValidText(scMdl.getLoanStartDate(), "Loan start date", 400);
    DmShared.isValidText(scMdl.getEarningDeductionCode(), "Entry Type", 500);
    }   
}
