package aarogya.master.packageDetail.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHPackage_Mdl extends KeyField implements Serializable, IsSerializable {

    private String packageCode;
    private String packageName;
    private String packageShortName;
    private String organizationManualCode;
    private String organizationName;
    private String roomCategoryName;
    private String fromDate;
    private String toDate;
    private String packageNature;
    private String packageAmount;

    public TxtHPackage_Mdl() {
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

    public String getOrganizationManualCode() {
        return organizationManualCode;
    }

    public void setOrganizationManualCode(String organizationManualCode) {
        this.organizationManualCode = organizationManualCode;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
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

    public String getPackageNature() {
        return packageNature;
    }

    public void setPackageNature(String packageNature) {
        this.packageNature = packageNature;
    }

    public String getPackageAmount() {
        return packageAmount;
    }

    public void setPackageAmount(String packageAmount) {
        this.packageAmount = packageAmount;
    }

}
