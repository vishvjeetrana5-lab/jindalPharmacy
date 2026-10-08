package aarogya.pharmacy.reports.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHTransferFromDept_Mdl extends KeyField implements Serializable, IsSerializable {

    public TxtHTransferFromDept_Mdl() {
    }
    private String deptName;
    private String deptCode;
    private String entryDate;
    private String transferToCode;
    private String transferToName;
    private String fromSiteCode;

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getTransferToCode() {
        return transferToCode;
    }

    public void setTransferToCode(String transferToCode) {
        this.transferToCode = transferToCode;
    }

    public String getTransferToName() {
        return transferToName;
    }

    public void setTransferToName(String transferToName) {
        this.transferToName = transferToName;
    }

    public String getFromSiteCode() {
        return fromSiteCode;
    }

    public void setFromSiteCode(String fromSiteCode) {
        this.fromSiteCode = fromSiteCode;
    }

}
