package aarogya.master.serviceMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class ServiceMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String shortName;
    private String categoryCode;
    private String categoryName;
    private String groupCode;
    private String groupName;
    private String prnSrNo;
    private String activeText;
    private String activeTag;
    private String routineText;
    private String routineTag;
    private String packageTag;
    private String packageText;
    private String organizationCode;
    private String isDiscountAllowText;
    private String isDiscountAllowTag;
    private String rateEditableText;
    private String rateEditableTag;
    private String nightShiftTag;
    private String nightShiftText;
    private String nightShiftAmount;

    private String typeCode;
    private String typeName;

    private String linkDepartmentName;
    private String linkDepartmentCode;
    private String serviceNatureName;
    private String serviceNatureCode;

    public String getNightShiftAmount() {
        return nightShiftAmount;
    }

    public void setNightShiftAmount(String nightShiftAmount) {
        this.nightShiftAmount = nightShiftAmount;
    }

    public String getNightShiftTag() {
        return nightShiftTag;
    }

    public void setNightShiftTag(String nightShiftTag) {
        this.nightShiftTag = nightShiftTag;
    }

    public String getNightShiftText() {
        return nightShiftText;
    }

    public void setNightShiftText(String nightShiftText) {
        this.nightShiftText = nightShiftText;
    }

    public ServiceMaster_Mdl() {
    }

    public String getActiveTag() {
        return activeTag;
    }

    public void setActiveTag(String activeTag) {
        this.activeTag = activeTag;
    }

    public String getPackageText() {
        return packageText;
    }

    public void setPackageText(String packageText) {
        this.packageText = packageText;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getPackageTag() {
        return packageTag;
    }

    public void setPackageTag(String packageTag) {
        this.packageTag = packageTag;
    }

    public String getActiveText() {
        return activeText;
    }

    public void setActiveText(String activeText) {
        this.activeText = activeText;
    }

    public String getPrnSrNo() {
        return prnSrNo;
    }

    public void setPrnSrNo(String prnSrNo) {
        this.prnSrNo = prnSrNo;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
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

    public String getRoutineText() {
        return routineText;
    }

    public void setRoutineText(String routineText) {
        this.routineText = routineText;
    }

    public String getRoutineTag() {
        return routineTag;
    }

    public void setRoutineTag(String routineTag) {
        this.routineTag = routineTag;
    }

    public String getIsDiscountAllowTag() {
        return isDiscountAllowTag;
    }

    public void setIsDiscountAllowTag(String isDiscountAllowTag) {
        this.isDiscountAllowTag = isDiscountAllowTag;
    }

    public String getIsDiscountAllowText() {
        return isDiscountAllowText;
    }

    public void setIsDiscountAllowText(String isDiscountAllowText) {
        this.isDiscountAllowText = isDiscountAllowText;
    }

    public String getRateEditableText() {
        return rateEditableText;
    }

    public void setRateEditableText(String rateEditableText) {
        this.rateEditableText = rateEditableText;
    }

    public String getRateEditableTag() {
        return rateEditableTag;
    }

    public void setRateEditableTag(String rateEditableTag) {
        this.rateEditableTag = rateEditableTag;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getLinkDepartmentName() {
        return linkDepartmentName;
    }

    public void setLinkDepartmentName(String linkDepartmentName) {
        this.linkDepartmentName = linkDepartmentName;
    }

    public String getLinkDepartmentCode() {
        return linkDepartmentCode;
    }

    public void setLinkDepartmentCode(String linkDepartmentCode) {
        this.linkDepartmentCode = linkDepartmentCode;
    }

    public String getServiceNatureName() {
        return serviceNatureName;
    }

    public void setServiceNatureName(String serviceNatureName) {
        this.serviceNatureName = serviceNatureName;
    }

    public String getServiceNatureCode() {
        return serviceNatureCode;
    }

    public void setServiceNatureCode(String serviceNatureCode) {
        this.serviceNatureCode = serviceNatureCode;
    }

}
