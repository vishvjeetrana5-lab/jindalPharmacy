package dataman.payroll.reports.shared;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.LinkedHashMap;

public class RptDeptWiseSalarySheet_Vld {

    public static void validateInput(LinkedHashMap<String, FieldUDT_Mdl> hashMapFilter) {
        DmShared.isValidText(hashMapFilter.get("salaryMethod").getText(), "Salary Method", 100);
    }
}
