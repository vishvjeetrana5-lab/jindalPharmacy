package aarogya.inventory.gateOut.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String entry_No;
    private String voucherType;
    private String date;
    private String time;
    private String driver;
    private String party_Name;

    private String out_Date;
    private String out_Time;
    private String remark;
    private String purposeName;

    private String passType;
    private String department;
    private String destination;
    private String mode;
    private String purpose;
    private String estdDate;
    private String billCancelled;
    private String status;

    public Find_Mdl() {
    }

    public String getEntry_No() {
        return entry_No;
    }

    public void setEntry_No(String entry_No) {
        this.entry_No = entry_No;
    }

    public String getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getParty_Name() {
        return party_Name;
    }

    public void setParty_Name(String party_Name) {
        this.party_Name = party_Name;
    }

    public String getOut_Date() {
        return out_Date;
    }

    public void setOut_Date(String out_Date) {
        this.out_Date = out_Date;
    }

    public String getOut_Time() {
        return out_Time;
    }

    public void setOut_Time(String out_Time) {
        this.out_Time = out_Time;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getPurposeName() {
        return purposeName;
    }

    public void setPurposeName(String purposeName) {
        this.purposeName = purposeName;
    }

    public String getPassType() {
        return passType;
    }

    public void setPassType(String passType) {
        this.passType = passType;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getEstdDate() {
        return estdDate;
    }

    public void setEstdDate(String estdDate) {
        this.estdDate = estdDate;
    }

    public String getBillCancelled() {
        return billCancelled;
    }

    public void setBillCancelled(String billCancelled) {
        this.billCancelled = billCancelled;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
