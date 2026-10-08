package dataman.utility.userPermission.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserPermission_Mdl extends KeyField implements Serializable, IsSerializable {

    private String userId;
    private String roleCode;
    private String roleName;
    private String description;
    private String isAdministrator;
    private String linkedAccountCode;
    private String linkedAccountName;
    private String email;
    private List<GrdVoucher_Mdl> listVoucher = new ArrayList<GrdVoucher_Mdl>();
    private List<GrdMenu_Mdl> listMenuDetail = new ArrayList<GrdMenu_Mdl>();
    private List<GrdSite_Mdl> listSite = new ArrayList<GrdSite_Mdl>();
    private List<GrdCostCenter_Mdl> listCostCenter = new ArrayList<GrdCostCenter_Mdl>();

    public UserPermission_Mdl() {
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

    public String getIsAdministrator() {
        return isAdministrator;
    }

    public void setIsAdministrator(String isAdministrator) {
        this.isAdministrator = isAdministrator;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<GrdVoucher_Mdl> getListVoucher() {
        return listVoucher;
    }

    public List<GrdMenu_Mdl> getListMenuDetail() {
        return listMenuDetail;
    }

    public List<GrdSite_Mdl> getListSite() {
        return listSite;
    }

    public List<GrdCostCenter_Mdl> getListCostCenter() {
        return listCostCenter;
    }
}
