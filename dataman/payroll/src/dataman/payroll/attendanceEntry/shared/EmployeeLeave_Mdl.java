package dataman.payroll.attendanceEntry.shared;

import dataman.payroll.salaryCreation.shared.*;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class EmployeeLeave_Mdl extends KeyField implements Serializable, IsSerializable {


private List<String> rtnList = new ArrayList<>();
private String leaveNature;
private String leaveCode;



 public EmployeeLeave_Mdl() {
    }

    public List<String> getRtnList() {
        return rtnList;
    }

    public void setRtnList(List<String> rtnList) {
        this.rtnList = rtnList;
    }

    public String getLeaveNature() {
        return leaveNature;
    }

    public void setLeaveNature(String leaveNature) {
        this.leaveNature = leaveNature;
    }

    public String getLeaveCode() {
        return leaveCode;
    }

    public void setLeaveCode(String leaveCode) {
        this.leaveCode = leaveCode;
    }

  
    
}

