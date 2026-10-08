package dataman.payroll.attendanceEntry.shared;

import dataman.payroll.salaryCreation.shared.*;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SalaryMethod_Mdl extends KeyField implements Serializable, IsSerializable {


private String salaryMethodName;
private String fromDate;



 public SalaryMethod_Mdl() {
    }

    public String getSalaryMethodName() {
        return salaryMethodName;
    }

    public void setSalaryMethodName(String salaryMethodName) {
        this.salaryMethodName = salaryMethodName;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }
//
//    public String getSalaryMethodCode() {
//        return salaryMethodCode;
//    }
//
//    public void setSalaryMethodCode(String salaryMethodCode) {
//        this.salaryMethodCode = salaryMethodCode;
//    }

    
}

