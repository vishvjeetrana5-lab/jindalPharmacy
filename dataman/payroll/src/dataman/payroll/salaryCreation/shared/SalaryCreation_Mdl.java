package dataman.payroll.salaryCreation.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SalaryCreation_Mdl extends KeyField implements Serializable, IsSerializable {

private List<GrdSalaryCreation_Mdl> grdSalaryCreationList = new ArrayList<GrdSalaryCreation_Mdl>();
private String v_Type;
private String salaryMethodName;
private String departmentName;
private String salaryMethodCode;
private String departmentCode;
private String fromDate;
private String toDate;

 public SalaryCreation_Mdl() {
    }

    public String getSalaryMethodName() {
        return salaryMethodName;
    }

    public void setSalaryMethodName(String salaryMethodName) {
        this.salaryMethodName = salaryMethodName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getSalaryMethodCode() {
        return salaryMethodCode;
    }

    public void setSalaryMethodCode(String salaryMethodCode) {
        this.salaryMethodCode = salaryMethodCode;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }
 
    public List<GrdSalaryCreation_Mdl> getGrdSalaryCreationList() {
        return grdSalaryCreationList;
    }

    public String getV_Type() {
        return v_Type;
    }

    public void setV_Type(String v_Type) {
        this.v_Type = v_Type;
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

    
}

