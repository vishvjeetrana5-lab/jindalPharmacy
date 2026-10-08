package aarogya.pharmacy.saleBill.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String billNo;
    private String date;
    private String time;
    private String opdIPDNO;
    private String uhidNo;
    private String consDocName;
    private String patientName;
    private String deptName;
    private String status;

    private String preparedBy;
    private String preparedDate;
    private String preparedTime;
    private String estimateNo;

    public Find_Mdl() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOpdIPDNO() {
        return opdIPDNO;
    }

    public void setOpdIPDNO(String opdIPDNO) {
        this.opdIPDNO = opdIPDNO;
    }

    public String getUhidNo() {
        return uhidNo;
    }

    public void setUhidNo(String uhidNo) {
        this.uhidNo = uhidNo;
    }

    public String getConsDocName() {
        return consDocName;
    }

    public void setConsDocName(String consDocName) {
        this.consDocName = consDocName;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPreparedBy() {
        return preparedBy;
    }

    public void setPreparedBy(String preparedBy) {
        this.preparedBy = preparedBy;
    }

    public String getPreparedDate() {
        return preparedDate;
    }

    public void setPreparedDate(String preparedDate) {
        this.preparedDate = preparedDate;
    }

    public String getPreparedTime() {
        return preparedTime;
    }

    public void setPreparedTime(String preparedTime) {
        this.preparedTime = preparedTime;
    }

    public String getEstimateNo() {
        return estimateNo;
    }

    public void setEstimateNo(String estimateNo) {
        this.estimateNo = estimateNo;
    }



}
