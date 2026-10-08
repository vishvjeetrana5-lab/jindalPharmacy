package dataman.payroll.salaryCreation.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class SalaryCreation_Vld {

    public static void chkSave(SalaryCreation_Mdl scMdl) {

    DmShared.isValidText(scMdl.getSalaryMethodName(), "Salary Method", 100);
    DmShared.isValidText(scMdl.getDepartmentCode(), "Department", 200);
    
   
    if (scMdl.getGrdSalaryCreationList().size() == 0)
       {
          throw new DmException(1700, "Please Fill Details.");
        }   

    }
}
