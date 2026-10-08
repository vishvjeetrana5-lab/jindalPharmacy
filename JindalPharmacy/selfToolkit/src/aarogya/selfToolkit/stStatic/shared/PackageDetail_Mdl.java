package aarogya.selfToolkit.stStatic.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class PackageDetail_Mdl implements Serializable, IsSerializable {

    private String packageDetailCode;
    private String packageDetailName;
    private String packageCode;
    private String packageName;
    private String shortName;
    private String organizationName;
    private String organizationCode;
    private String roomCatName;
    private String roomCatCode;
    private String amount;

    public PackageDetail_Mdl() {
    }

    public String getPackageDetailCode() {
        return packageDetailCode;
    }

    public void setPackageDetailCode(String packageDetailCode) {
        this.packageDetailCode = packageDetailCode;
    }

    public String getPackageDetailName() {
        return packageDetailName;
    }

    public void setPackageDetailName(String packageDetailName) {
        this.packageDetailName = packageDetailName;
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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getRoomCatName() {
        return roomCatName;
    }

    public void setRoomCatName(String roomCatName) {
        this.roomCatName = roomCatName;
    }

    public String getRoomCatCode() {
        return roomCatCode;
    }

    public void setRoomCatCode(String roomCatCode) {
        this.roomCatCode = roomCatCode;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

}
