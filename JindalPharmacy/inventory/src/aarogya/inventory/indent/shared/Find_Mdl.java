package aarogya.inventory.indent.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String indentType;
    private String department;
    private String indentor;
    private String requisition;
    private String indentDate;
    private String approval;

    public Find_Mdl() {
    }

    public String getIndentType() {
        return indentType;
    }

    public void setIndentType(String indentType) {
        this.indentType = indentType;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getIndentor() {
        return indentor;
    }

    public void setIndentor(String indentor) {
        this.indentor = indentor;
    }

    public String getIndentDate() {
        return indentDate;
    }

    public void setIndentDate(String indentDate) {
        this.indentDate = indentDate;
    }

    public String getRequisition() {
        return requisition;
    }

    public void setRequisition(String requisition) {
        this.requisition = requisition;
    }

    public String getApproval() {
        return approval;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }

}
