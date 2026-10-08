
package dataman.payroll.employeeMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;


public class GrdLeave_Mdl extends KeyField implements Serializable, IsSerializable{
    private String leaves;
    private String fromDate;
    private String toDate;
    private String leave;
    private String leaveCode;
    private String sNo;
    private String leaveDocId;
    private String employeeLeaveDocId;

    public String getEmployeeLeaveDocId() {
        return employeeLeaveDocId;
    }

    public void setEmployeeLeaveDocId(String employeeLeaveDocId) {
        this.employeeLeaveDocId = employeeLeaveDocId;
    }

    public GrdLeave_Mdl() {
    }

    public String getLeaves() {
        return leaves;
    }

    public void setLeaves(String leaves) {
        this.leaves = leaves;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getLeave() {
        return leave;
    }

    public void setLeave(String leave) {
        this.leave = leave;
    }

    public String getLeaveCode() {
        return leaveCode;
    }

    public void setLeaveCode(String leaveCode) {
        this.leaveCode = leaveCode;
    }

    public String getsNo() {
        return sNo;
    }

    public void setsNo(String sNo) {
        this.sNo = sNo;
    }

    public String getLeaveDocId() {
        return leaveDocId;
    }

    public void setLeaveDocId(String leaveDocId) {
        this.leaveDocId = leaveDocId;
    }
}
