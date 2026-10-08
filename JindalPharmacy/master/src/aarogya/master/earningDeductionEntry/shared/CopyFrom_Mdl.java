package aarogya.master.earningDeductionEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class CopyFrom_Mdl extends KeyField implements Serializable, IsSerializable {

    private String empName;
    private String empCode;
    private String salName;
    private String salCode;

    public CopyFrom_Mdl() {
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public String getSalName() {
        return salName;
    }

    public void setSalName(String salName) {
        this.salName = salName;
    }

    public String getSalCode() {
        return salCode;
    }

    public void setSalCode(String salCode) {
        this.salCode = salCode;
    }

}
