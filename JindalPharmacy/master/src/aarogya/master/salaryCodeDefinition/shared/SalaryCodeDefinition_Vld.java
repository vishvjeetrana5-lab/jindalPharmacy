package aarogya.master.salaryCodeDefinition.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class SalaryCodeDefinition_Vld {

    public static void chkSave(SalaryCodeDefinition_Mdl mrTemp) {

        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "Name", 100);
        DmShared.isValidText(mrTemp.getFromDate(), "From Date", 200);
        DmShared.isValidText(mrTemp.getToDate(), "To Date", 300);
        if (DmShared.convertDate(mrTemp.getFromDate()).getTime() > DmShared.convertDate(mrTemp.getToDate()).getTime()) {
            throw new DmException(300, " TO Date must be greater than From Date");
        }
    }
}
