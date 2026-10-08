package dataman.utility.copyUserPermission.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.Objects;

public class GrdCopyPermissionFromTo_Mdl extends KeyField implements Serializable,IsSerializable{

    private String userDescription;
    private String isAdmin;
    private String isSite;
    private String isCostCenter;
    private String isUserPermission;
    private String isVoucherPermission;
    private String isCheckedOrDelete;
    private String fromCompanyCode;
    private String fromCompanyName;
    private String fromCompVrPrefix;
    private String toCompanyCode;
    private String toCompanyName;
    private String toCompVrPrefix;
    private String isCopy;
    

    public GrdCopyPermissionFromTo_Mdl() {
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getIsSite() {
        return isSite;
    }

    public void setIsSite(String isSite) {
        this.isSite = isSite;
    }

    public String getIsCostCenter() {
        return isCostCenter;
    }

    public void setIsCostCenter(String isCostCenter) {
        this.isCostCenter = isCostCenter;
    }

    public String getIsUserPermission() {
        return isUserPermission;
    }

    public void setIsUserPermission(String isUserPermission) {
        this.isUserPermission = isUserPermission;
    }

    public String getIsVoucherPermission() {
        return isVoucherPermission;
    }

    public void setIsVoucherPermission(String isVoucherPermission) {
        this.isVoucherPermission = isVoucherPermission;
    }

    public String getIsCheckedOrDelete() {
        return isCheckedOrDelete;
    }

    public void setIsCheckedOrDelete(String isCheckedOrDelete) {
        this.isCheckedOrDelete = isCheckedOrDelete;
    }

    public String getFromCompanyCode() {
        return fromCompanyCode;
    }

    public void setFromCompanyCode(String fromCompanyCode) {
        this.fromCompanyCode = fromCompanyCode;
    }

    public String getFromCompanyName() {
        return fromCompanyName;
    }

    public void setFromCompanyName(String fromCompanyName) {
        this.fromCompanyName = fromCompanyName;
    }

    public String getFromCompVrPrefix() {
        return fromCompVrPrefix;
    }

    public void setFromCompVrPrefix(String fromCompVrPrefix) {
        this.fromCompVrPrefix = fromCompVrPrefix;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.getSharedKeyFieldName());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GrdCopyPermissionFromTo_Mdl other = (GrdCopyPermissionFromTo_Mdl) obj;
        if (!Objects.equals(this.getSharedKeyFieldName(), other.getSharedKeyFieldName())) {
            return false;
        }
        return true;
    }

    public String getToCompanyCode() {
        return toCompanyCode;
    }

    public void setToCompanyCode(String toCompanyCode) {
        this.toCompanyCode = toCompanyCode;
    }

    public String getToCompanyName() {
        return toCompanyName;
    }

    public void setToCompanyName(String toCompanyName) {
        this.toCompanyName = toCompanyName;
    }

    public String getToCompVrPrefix() {
        return toCompVrPrefix;
    }

    public void setToCompVrPrefix(String toCompVrPrefix) {
        this.toCompVrPrefix = toCompVrPrefix;
    }

    public String getIsCopy() {
        return isCopy;
    }

    public void setIsCopy(String isCopy) {
        this.isCopy = isCopy;
    }

}
