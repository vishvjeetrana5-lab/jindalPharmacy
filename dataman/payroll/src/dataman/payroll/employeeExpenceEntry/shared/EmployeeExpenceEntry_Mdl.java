package dataman.payroll.employeeExpenceEntry.shared;


import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EmployeeExpenceEntry_Mdl extends KeyField implements Serializable, IsSerializable {

    private String entryDate;
    private String employeeText;
    private String employeeTag;
    private String salaryPeriodText;
    private String salaryPeriodTag;
    private RecId recId = new RecId();
    private String dtVoucher;
    private String time;
    private String voucherTypeCode;
    private String totalValue;
    private String staticCode;
    
     private List<GridEmployeeExpenceEntry_Mdl> grdDetailMdl = new ArrayList<GridEmployeeExpenceEntry_Mdl>();
    

    public EmployeeExpenceEntry_Mdl() {
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }



    public List<GridEmployeeExpenceEntry_Mdl> getGrdDetailMdl() {
        return grdDetailMdl;
    }

    public String getEmployeeText() {
        return employeeText;
    }

    public void setEmployeeText(String employeeText) {
        this.employeeText = employeeText;
    }

    public String getEmployeeTag() {
        return employeeTag;
    }

    public void setEmployeeTag(String employeeTag) {
        this.employeeTag = employeeTag;
    }

    public String getSalaryPeriodText() {
        return salaryPeriodText;
    }

    public void setSalaryPeriodText(String salaryPeriodText) {
        this.salaryPeriodText = salaryPeriodText;
    }

    public String getSalaryPeriodTag() {
        return salaryPeriodTag;
    }

    public void setSalaryPeriodTag(String salaryPeriodTag) {
        this.salaryPeriodTag = salaryPeriodTag;
    }

    public RecId getRecId() {
        return recId;
    }

    public void setRecId(RecId recId) {
        this.recId = recId;
    }

    public String getDtVoucher() {
        return dtVoucher;
    }

    public void setDtVoucher(String dtVoucher) {
        this.dtVoucher = dtVoucher;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVoucherTypeCode() {
        return voucherTypeCode;
    }

    public void setVoucherTypeCode(String voucherTypeCode) {
        this.voucherTypeCode = voucherTypeCode;
    }

    public String getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(String totalValue) {
        this.totalValue = totalValue;
    }

    public String getStaticCode() {
        return staticCode;
    }

    public void setStaticCode(String staticCode) {
        this.staticCode = staticCode;
    }



   


  



   

  



}
