package dataman.payroll.leaveEntryApproval.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Search_Mdl extends KeyField implements Serializable, IsSerializable {

    private String employeeName;
    private String applyDate;
    private String departmentName;
    private String FieldName;
    private String FieldNameValue;
    private String status;


    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFieldName() {
        return FieldName;
    }

    public void setFieldName(String FieldName) {
        this.FieldName = FieldName;
    }

    public String getFieldNameValue() {
        return FieldNameValue;
    }

    public void setFieldNameValue(String FieldNameValue) {
        this.FieldNameValue = FieldNameValue;
    }


}
