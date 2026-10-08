package aarogya.inventory.purchaseOrderDisplay.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHIndent_Mdl extends KeyField implements Serializable, IsSerializable{
    private String entryDate;
    private String toDepartmentName;
    private String fromDepartmentName;
    private String indentorName;

    public TxtHIndent_Mdl() {
    }
    
    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getToDepartmentName() {
        return toDepartmentName;
    }

    public void setToDepartmentName(String toDepartmentName) {
        this.toDepartmentName = toDepartmentName;
    }

    public String getFromDepartmentName() {
        return fromDepartmentName;
    }

    public void setFromDepartmentName(String fromDepartmentName) {
        this.fromDepartmentName = fromDepartmentName;
    }

    public String getIndentorName() {
        return indentorName;
    }

    public void setIndentorName(String indentorName) {
        this.indentorName = indentorName;
    }

    
}
