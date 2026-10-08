package dataman.dmBase.self.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class UserMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String userId;
    private String roleCode;
    private String roleName;
    private String description;
    private String lockTimeOut;
    private String administratorYNCode;
    private String administratorYNName;
    private String activeYNCode;
    private String activeYNName;

    private String linkedAccountCode;
    private String linkedAccountName;
    private String natureCode;
    private String natureName;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
    private String email;
    private String mobile;
    private String cpUserName;
    private String cpUserNameCode;
    private String gridLinkedUser;
    private String tokenKey;
    private String departmentCode;
    private String departmentName;

    public UserMaster_Mdl() {
    }

    public String getActiveYNCode() {
        return activeYNCode;
    }

    public void setActiveYNCode(String activeYNCode) {
        this.activeYNCode = activeYNCode;
    }

    public String getActiveYNName() {
        return activeYNName;
    }

    public void setActiveYNName(String activeYNName) {
        this.activeYNName = activeYNName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdministratorYNCode() {
        return administratorYNCode;
    }

    public void setAdministratorYNCode(String administratorYNCode) {
        this.administratorYNCode = administratorYNCode;
    }

    public String getLinkedAccountCode() {
        return linkedAccountCode;
    }

    public void setLinkedAccountCode(String linkedAccountCode) {
        this.linkedAccountCode = linkedAccountCode;
    }

    public String getLinkedAccountName() {
        return linkedAccountName;
    }

    public void setLinkedAccountName(String linkedAccountName) {
        this.linkedAccountName = linkedAccountName;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getAdministratorYNName() {
        return administratorYNName;
    }

    public void setAdministratorYNName(String administratorYNName) {
        this.administratorYNName = administratorYNName;
    }

    public String getGridLinkedUser() {
        return gridLinkedUser;
    }

    public void setGridLinkedUser(String gridLinkedUser) {
        this.gridLinkedUser = gridLinkedUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpUserName() {
        return cpUserName;
    }

    public void setCpUserName(String cpUserName) {
        this.cpUserName = cpUserName;
    }

    public String getCpUserNameCode() {
        return cpUserNameCode;
    }

    public void setCpUserNameCode(String cpUserNameCode) {
        this.cpUserNameCode = cpUserNameCode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTokenKey() {
        return tokenKey;
    }

    public void setTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
    }

    public String getNatureCode() {
        return natureCode;
    }

    public void setNatureCode(String natureCode) {
        this.natureCode = natureCode;
    }

    public String getNatureName() {
        return natureName;
    }

    public void setNatureName(String natureName) {
        this.natureName = natureName;
    }

    public String getLockTimeOut() {
        return lockTimeOut;
    }

    public void setLockTimeOut(String lockTimeOut) {
        this.lockTimeOut = lockTimeOut;
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
    
    
}
