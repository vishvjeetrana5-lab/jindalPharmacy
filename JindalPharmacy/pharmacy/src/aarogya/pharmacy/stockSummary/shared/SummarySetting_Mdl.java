package aarogya.pharmacy.stockSummary.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class SummarySetting_Mdl implements Serializable, IsSerializable {

    private String fromDate;
    private String toDate;
    private String medicineGroupCode;
    private String medicineGroupName;
    private String medicineMakeCode;
    private String medicineMakeName;
    private String medicineCatCode;
    private String medicineCatName;
    private String medicineCode;
    private String medicineName;
    private String departmentCode;
    private String departmentName;
    private String siteCode;
    private String siteName;
    private String valueOn;
    private String groupBy;
    private String stockStatus;
    private String particularCode;
    private String particularName;
    private String particularCaption;
    private String titleName;
    private String sorting;
    private String stepIndex;
    private String narcoticsName;
    private String narcoticsCode;
    private String consignmentItemName;
    private String consignmentItemCode;
    private String highValueName;
    private String highValueCode;
    private String billedItemName;
    private String billedItemCode;
    private String flowThroughName;
    private String flowThroughCode;
    private String lifeSavingName;
    private String lifeSavingCode;

    public SummarySetting_Mdl() {
    }

    public String getMedicineGroupCode() {
        return medicineGroupCode;
    }

    public void setMedicineGroupCode(String medicineGroupCode) {
        this.medicineGroupCode = medicineGroupCode;
    }

    public String getMedicineGroupName() {
        return medicineGroupName;
    }

    public void setMedicineGroupName(String medicineGroupName) {
        this.medicineGroupName = medicineGroupName;
    }

    public String getMedicineMakeCode() {
        return medicineMakeCode;
    }

    public void setMedicineMakeCode(String medicineMakeCode) {
        this.medicineMakeCode = medicineMakeCode;
    }

    public String getMedicineMakeName() {
        return medicineMakeName;
    }

    public void setMedicineMakeName(String medicineMakeName) {
        this.medicineMakeName = medicineMakeName;
    }

    public String getMedicineCatCode() {
        return medicineCatCode;
    }

    public void setMedicineCatCode(String medicineCatCode) {
        this.medicineCatCode = medicineCatCode;
    }

    public String getMedicineCatName() {
        return medicineCatName;
    }

    public void setMedicineCatName(String medicineCatName) {
        this.medicineCatName = medicineCatName;
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

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getValueOn() {
        return valueOn;
    }

    public void setValueOn(String valueOn) {
        this.valueOn = valueOn;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public String getStockStatus() {
        return stockStatus;
    }

    public void setStockStatus(String stockStatus) {
        this.stockStatus = stockStatus;
    }

    public String getParticularCode() {
        return particularCode;
    }

    public void setParticularCode(String particularCode) {
        this.particularCode = particularCode;
    }

    public String getParticularName() {
        return particularName;
    }

    public void setParticularName(String particularName) {
        this.particularName = particularName;
    }

    public String getParticularCaption() {
        return particularCaption;
    }

    public void setParticularCaption(String particularCaption) {
        this.particularCaption = particularCaption;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getSorting() {
        return sorting;
    }

    public void setSorting(String sorting) {
        this.sorting = sorting;
    }

    public String getStepIndex() {
        return stepIndex;
    }

    public void setStepIndex(String stepIndex) {
        this.stepIndex = stepIndex;
    }

    public String getNarcoticsName() {
        return narcoticsName;
    }

    public void setNarcoticsName(String narcoticsName) {
        this.narcoticsName = narcoticsName;
    }

    public String getNarcoticsCode() {
        return narcoticsCode;
    }

    public void setNarcoticsCode(String narcoticsCode) {
        this.narcoticsCode = narcoticsCode;
    }

    public String getConsignmentItemName() {
        return consignmentItemName;
    }

    public void setConsignmentItemName(String consignmentItemName) {
        this.consignmentItemName = consignmentItemName;
    }

    public String getConsignmentItemCode() {
        return consignmentItemCode;
    }

    public void setConsignmentItemCode(String consignmentItemCode) {
        this.consignmentItemCode = consignmentItemCode;
    }

    public String getHighValueName() {
        return highValueName;
    }

    public void setHighValueName(String highValueName) {
        this.highValueName = highValueName;
    }

    public String getHighValueCode() {
        return highValueCode;
    }

    public void setHighValueCode(String highValueCode) {
        this.highValueCode = highValueCode;
    }

    public String getBilledItemName() {
        return billedItemName;
    }

    public void setBilledItemName(String billedItemName) {
        this.billedItemName = billedItemName;
    }

    public String getBilledItemCode() {
        return billedItemCode;
    }

    public void setBilledItemCode(String billedItemCode) {
        this.billedItemCode = billedItemCode;
    }

    public String getFlowThroughName() {
        return flowThroughName;
    }

    public void setFlowThroughName(String flowThroughName) {
        this.flowThroughName = flowThroughName;
    }

    public String getFlowThroughCode() {
        return flowThroughCode;
    }

    public void setFlowThroughCode(String flowThroughCode) {
        this.flowThroughCode = flowThroughCode;
    }

    public String getLifeSavingName() {
        return lifeSavingName;
    }

    public void setLifeSavingName(String lifeSavingName) {
        this.lifeSavingName = lifeSavingName;
    }

    public String getLifeSavingCode() {
        return lifeSavingCode;
    }

    public void setLifeSavingCode(String lifeSavingCode) {
        this.lifeSavingCode = lifeSavingCode;
    }

}
