
package aarogya.pharmacy.procurementPlannerDisplay.shared;

import aarogya.pharmacy.procurementPlannerDisplay.shared.*;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import dataman.dmBase.self.shared.RecId;
import java.io.Serializable;

public class SearchValue_Mdl extends KeyField implements Serializable, IsSerializable {

    private RecId recIdProcurementPlanner = new RecId();

    
    private String fieldName;
    private String fieldNameValue;
    private String departmentCode;
    private String departmentName;
    private String companyCode;
    private String companyName;
    private String groupCode;
    private String groupName;
    private String categoryCode;
    private String categoryName;
    private String medicineCode;
    private String medicineName;
    private String consumptionDays;
    private String projectionDays;
    private boolean onlyShortForDept;
    private boolean onlyShortForSite;
    private String stockRunDaysDept;
    private String stockRunDaysSite;
    private String asOnDate;
    private String voucherTypeTag;
    private String recId;
    private String voucherDate;

    public SearchValue_Mdl() {
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldNameValue() {
        return fieldNameValue;
    }

    public void setFieldNameValue(String fieldNameValue) {
        this.fieldNameValue = fieldNameValue;
    }
    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getMedicineCode() {
        return medicineCode;
    }

    public void setMedicineCode(String medicineCode) {
        this.medicineCode = medicineCode;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getProjectionDays() {
        return projectionDays;
    }

    public void setProjectionDays(String projectionDays) {
        this.projectionDays = projectionDays;
    }

    public String getConsumptionDays() {
        return consumptionDays;
    }

    public void setConsumptionDays(String consumptionDays) {
        this.consumptionDays = consumptionDays;
    }

    public boolean isOnlyShortForDept() {
        return onlyShortForDept;
    }

    public void setOnlyShortForDept(boolean onlyShortForDept) {
        this.onlyShortForDept = onlyShortForDept;
    }

    public boolean isOnlyShortForSite() {
        return onlyShortForSite;
    }

    public void setOnlyShortForSite(boolean onlyShortForSite) {
        this.onlyShortForSite = onlyShortForSite;
    }

    public String getStockRunDaysDept() {
        return stockRunDaysDept;
    }

    public void setStockRunDaysDept(String stockRunDaysDept) {
        this.stockRunDaysDept = stockRunDaysDept;
    }

    public String getStockRunDaysSite() {
        return stockRunDaysSite;
    }

    public void setStockRunDaysSite(String stockRunDaysSite) {
        this.stockRunDaysSite = stockRunDaysSite;
    }

    public String getAsOnDate() {
        return asOnDate;
    }

    public void setAsOnDate(String asOnDate) {
        this.asOnDate = asOnDate;
    }

    public String getVoucherTypeTag() {
        return voucherTypeTag;
    }

    public void setVoucherTypeTag(String voucherTypeTag) {
        this.voucherTypeTag = voucherTypeTag;
    }

    public String getRecId() {
        return recId;
    }

    public void setRecId(String recId) {
        this.recId = recId;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }
    
 public RecId getRecIdProcurementPlanner() {
        return recIdProcurementPlanner;
    }

    public void setRecIdProcurementPlanner(RecId recIdProcurementPlanner) {
        this.recIdProcurementPlanner = recIdProcurementPlanner;
    }   

}