package dataman.payroll.attendanceEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    
    private String salaryMethod;
    private String departmentName;

    public Find_Mdl() {
    }

 public String getSalaryMethod() {
        return salaryMethod;
    }

    public void setSalaryMethod(String salaryMethod) {
        this.salaryMethod = salaryMethod;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
    

}
