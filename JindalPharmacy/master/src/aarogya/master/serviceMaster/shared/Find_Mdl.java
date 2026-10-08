package aarogya.master.serviceMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String shortName;
    private String categoryName;
    private String printSNo;
    private String isDiscountAllow;
    private String groupName;
    private String isType;

    private String serviceRoutine;
    private String isNightShift;
    private String nightShiftAmount;
    private String isRateEditable;
    private String isActive;

    private String serviceNatureCode;
    private String serviceNatureName;

    public Find_Mdl() {
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getPrintSNo() {
        return printSNo;
    }

    public void setPrintSNo(String printSNo) {
        this.printSNo = printSNo;
    }

    public String getIsDiscountAllow() {
        return isDiscountAllow;
    }

    public void setIsDiscountAllow(String isDiscountAllow) {
        this.isDiscountAllow = isDiscountAllow;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getServiceRoutine() {
        return serviceRoutine;
    }

    public void setServiceRoutine(String serviceRoutine) {
        this.serviceRoutine = serviceRoutine;
    }

    public String getIsNightShift() {
        return isNightShift;
    }

    public void setIsNightShift(String isNightShift) {
        this.isNightShift = isNightShift;
    }

    public String getNightShiftAmount() {
        return nightShiftAmount;
    }

    public void setNightShiftAmount(String nightShiftAmount) {
        this.nightShiftAmount = nightShiftAmount;
    }

    public String getIsRateEditable() {
        return isRateEditable;
    }

    public void setIsRateEditable(String isRateEditable) {
        this.isRateEditable = isRateEditable;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getIsType() {
        return isType;
    }

    public void setIsType(String isType) {
        this.isType = isType;
    }

    public String getServiceNatureCode() {
        return serviceNatureCode;
    }

    public void setServiceNatureCode(String serviceNatureCode) {
        this.serviceNatureCode = serviceNatureCode;
    }

    public String getServiceNatureName() {
        return serviceNatureName;
    }

    public void setServiceNatureName(String serviceNatureName) {
        this.serviceNatureName = serviceNatureName;
    }

}
