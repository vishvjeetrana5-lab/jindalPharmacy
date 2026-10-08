package aarogya.pharmacy.indent.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHRequisition_Mdl extends KeyField implements Serializable, IsSerializable {

    private String date;
    private String department;
    private String requisitionBy;

    public TxtHRequisition_Mdl() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRequisitionBy() {
        return requisitionBy;
    }

    public void setRequisitionBy(String requisitionBy) {
        this.requisitionBy = requisitionBy;
    }

}
