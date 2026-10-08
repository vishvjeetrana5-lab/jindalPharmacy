package aarogya.master.packageDetail.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PackageDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    private String packageCode;
    private String packageName;
    private String packageShortName;
    private String packageNatureCode;
    private String packageNatureName;
    private String packageAmount;
    private String organizationCode;
    private String organizationName;
    private String orgCode;
    private String roomCategoryCode;
    private String roomCategoryName;
    private String fromDate;
    private String toDate;
    private String scheduleCode;
    private String scheduleName;
    private String activeCode;
    private String activeName;
    private String actualValue;
    private String defaultRoomCategoryCode;
    private String defaultRoomCategoryName;
    private String defaultOrganizationCode;
    private String defaultOrganizationName;
    private String rateListPrice;
    private String rateListCode;

    private List<GrdServiceMaster_Mdl> listService = new ArrayList<GrdServiceMaster_Mdl>();
    private List<GrdServiceMaster_Mdl> listInvestigation = new ArrayList<GrdServiceMaster_Mdl>();
    private List<GrdServiceMaster_Mdl> listMedicineItem = new ArrayList<GrdServiceMaster_Mdl>();

    public PackageDetail_Mdl() {
    }

    public String getPackageCode() {
        return packageCode;
    }

    public void setPackageCode(String packageCode) {
        this.packageCode = packageCode;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageShortName() {
        return packageShortName;
    }

    public void setPackageShortName(String packageShortName) {
        this.packageShortName = packageShortName;
    }

    public String getPackageNatureCode() {
        return packageNatureCode;
    }

    public void setPackageNatureCode(String packageNatureCode) {
        this.packageNatureCode = packageNatureCode;
    }

    public String getPackageNatureName() {
        return packageNatureName;
    }

    public void setPackageNatureName(String packageNatureName) {
        this.packageNatureName = packageNatureName;
    }

    public String getPackageAmount() {
        return packageAmount;
    }

    public void setPackageAmount(String packageAmount) {
        this.packageAmount = packageAmount;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getRoomCategoryCode() {
        return roomCategoryCode;
    }

    public void setRoomCategoryCode(String roomCategoryCode) {
        this.roomCategoryCode = roomCategoryCode;
    }

    public String getRoomCategoryName() {
        return roomCategoryName;
    }

    public void setRoomCategoryName(String roomCategoryName) {
        this.roomCategoryName = roomCategoryName;
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

    public String getScheduleCode() {
        return scheduleCode;
    }

    public void setScheduleCode(String scheduleCode) {
        this.scheduleCode = scheduleCode;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public String getActiveCode() {
        return activeCode;
    }

    public void setActiveCode(String activeCode) {
        this.activeCode = activeCode;
    }

    public String getActiveName() {
        return activeName;
    }

    public void setActiveName(String activeName) {
        this.activeName = activeName;
    }

    public String getActualValue() {
        return actualValue;
    }

    public void setActualValue(String actualValue) {
        this.actualValue = actualValue;
    }

    public String getDefaultRoomCategoryCode() {
        return defaultRoomCategoryCode;
    }

    public void setDefaultRoomCategoryCode(String defaultRoomCategoryCode) {
        this.defaultRoomCategoryCode = defaultRoomCategoryCode;
    }

    public String getDefaultRoomCategoryName() {
        return defaultRoomCategoryName;
    }

    public void setDefaultRoomCategoryName(String defaultRoomCategoryName) {
        this.defaultRoomCategoryName = defaultRoomCategoryName;
    }

    public String getDefaultOrganizationCode() {
        return defaultOrganizationCode;
    }

    public void setDefaultOrganizationCode(String defaultOrganizationCode) {
        this.defaultOrganizationCode = defaultOrganizationCode;
    }

    public String getDefaultOrganizationName() {
        return defaultOrganizationName;
    }

    public void setDefaultOrganizationName(String defaultOrganizationName) {
        this.defaultOrganizationName = defaultOrganizationName;
    }

    public List<GrdServiceMaster_Mdl> getListService() {
        return listService;
    }

    public List<GrdServiceMaster_Mdl> getListInvestigation() {
        return listInvestigation;
    }

    public List<GrdServiceMaster_Mdl> getListMedicineItem() {
        return listMedicineItem;
    }

    public String getRateListPrice() {
        return rateListPrice;
    }

    public void setRateListPrice(String rateListPrice) {
        this.rateListPrice = rateListPrice;
    }

    public String getRateListCode() {
        return rateListCode;
    }

    public void setRateListCode(String rateListCode) {
        this.rateListCode = rateListCode;
    }
    
}
