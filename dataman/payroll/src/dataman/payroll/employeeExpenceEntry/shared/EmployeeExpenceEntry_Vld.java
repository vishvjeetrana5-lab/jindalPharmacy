package dataman.payroll.employeeExpenceEntry.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class EmployeeExpenceEntry_Vld {
   

    public static void chkSave(EmployeeExpenceEntry_Mdl mrTemp) {
        boolean exists = false;
        int size = 0; 
        
        
        DmShared.isValidText(mrTemp.getEntryDate(), "Entry Date", 100);
        DmShared.isValidText(mrTemp.getEmployeeText(), "Employee", 200);
        DmShared.isValidText(mrTemp.getSalaryPeriodText(), "Salary Period", 300);
        

        if (mrTemp.getGrdDetailMdl() != null) {
            for (GridEmployeeExpenceEntry_Mdl grdMdl : mrTemp.getGrdDetailMdl()) {
                if (!DmShared.xNull(grdMdl.getValue()).equalsIgnoreCase("")) {
                    size++;
                }

            }

        }
        if (size == 0) {
            throw new DmException(500, "Please enter atleast one value");
        }

  
    }
}
