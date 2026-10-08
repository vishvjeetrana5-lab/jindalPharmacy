package dataman.payroll.employeeShiftDetail.shared;

import dataman.payroll.shiftMaster.shared.*;
import dataman.payroll.attendenceEntryManual.shared.*;
import dataman.payroll.attendenceEntryManual.shared.*;
import dataman.payroll.attendenceEntryManual.shared.*;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EmployeeShiftDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    private String shiftName;
    private String shiftCode;
    private String employeeName;
    private String employeeCode;
    private String fromDate;
    private String toDate;
    private String voucherTypeCode;
    private RecId recIdEmpShiftDetail = new RecId();
    private String dtVoucher;
   
    public EmployeeShiftDetail_Mdl() {
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
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

    public String getVoucherTypeCode() {
        return voucherTypeCode;
    }

    public void setVoucherTypeCode(String voucherTypeCode) {
        this.voucherTypeCode = voucherTypeCode;
    }

    public RecId getRecIdEmpShiftDetail() {
        return recIdEmpShiftDetail;
    }

    public void setRecIdEmpShiftDetail(RecId recIdEmpShiftDetail) {
        this.recIdEmpShiftDetail = recIdEmpShiftDetail;
    }

    public String getDtVoucher() {
        return dtVoucher;
    }

    public void setDtVoucher(String dtVoucher) {
        this.dtVoucher = dtVoucher;
    }

   
}
