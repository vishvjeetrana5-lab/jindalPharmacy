package dataman.payroll.loanEntry.shared;

import dataman.payroll.loanEntry.shared.*;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LoanEntry_Mdl extends KeyField implements Serializable, IsSerializable {

private List<GrdLoanEntry_Mdl> grdLoanDet = new ArrayList<GrdLoanEntry_Mdl>();
private String v_Type;
private String employeeName;
private String employeeCode;
private String earningDeductionName;
private String earningDeductionCode;
private String v_Date;
private RecId recId = new RecId();
private String loanAmount;
private String interestRate;
private String forMonth;
private String loanStartDate;
private String stopStatus;

 public LoanEntry_Mdl() {
    }

    public String getV_Type() {
        return v_Type;
    }

    public void setV_Type(String v_Type) {
        this.v_Type = v_Type;
    }

    public List<GrdLoanEntry_Mdl> getGrdLoanDet() {
        return grdLoanDet;
    }

    public void setGrdLoanDet(List<GrdLoanEntry_Mdl> grdLoanDet) {
        this.grdLoanDet = grdLoanDet;
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

    public String getEarningDeductionName() {
        return earningDeductionName;
    }

    public void setEarningDeductionName(String earningDeductionName) {
        this.earningDeductionName = earningDeductionName;
    }

    public String getEarningDeductionCode() {
        return earningDeductionCode;
    }

    public void setEarningDeductionCode(String earningDeductionCode) {
        this.earningDeductionCode = earningDeductionCode;
    }

    
    public String getV_Date() {
        return v_Date;
    }

    public void setV_Date(String v_Date) {
        this.v_Date = v_Date;
    }

    public RecId getRecId() {
        return recId;
    }

    public void setRecId(RecId recId) {
        this.recId = recId;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate;
    }

    public String getForMonth() {
        return forMonth;
    }

    public void setForMonth(String forMonth) {
        this.forMonth = forMonth;
    }

    public String getLoanStartDate() {
        return loanStartDate;
    }

    public void setLoanStartDate(String loanStartDate) {
        this.loanStartDate = loanStartDate;
    }

    public String getStopStatus() {
        return stopStatus;
    }

    public void setStopStatus(String stopStatus) {
        this.stopStatus = stopStatus;
    }
 
}

