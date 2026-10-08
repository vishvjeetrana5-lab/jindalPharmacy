package dataman.dmBase.self.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserMenu implements Serializable, IsSerializable {

    private String menuName;
    private String toolTip;
    private String permission;
    private String saveDate, viewDate, deleteDate;
    private double allowValueUpto;
    private List<String> controlPermission = new ArrayList<>();
    private List<UserMenu> UserMenuList = new ArrayList<>();

    public UserMenu() {
    }

    public UserMenu(String name) {
        this.menuName = name;
    }

    public UserMenu(String name, String permission) {
        this.menuName = name;
        this.permission = permission;
    }

    public UserMenu(String name, List<UserMenu> menuList) {
        this.menuName = name;
        this.UserMenuList = menuList;
    }

    public List<UserMenu> getUserMenu() {
        return UserMenuList;
    }

    public String getToolTip() {
        return toolTip;
    }

    public void setToolTip(String toolTip) {
        this.toolTip = toolTip;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setUserPermission(String permission) {
        this.permission = permission;
    }

    public String getUserPermission() {
        return permission;
    }

    public List<String> getControlPermission() {
        return controlPermission;
    }

    public void addControlPermission(String caption) {
        controlPermission.add(caption);
    }

    public void setControlPermission(List<String> controlPermission) {
        this.controlPermission = controlPermission;
    }

    public String getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(String saveDate) {
        this.saveDate = saveDate;
    }

    public String getViewDate() {
        return viewDate;
    }

    public void setViewDate(String viewDate) {
        this.viewDate = viewDate;
    }

    public String getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(String deleteDate) {
        this.deleteDate = deleteDate;
    }

    public double getAllowValueUpto() {
        return allowValueUpto;
    }

    public void setAllowValueUpto(double allowValueUpto) {
        this.allowValueUpto = allowValueUpto;
    }

    public UserMenu clone() {
        UserMenu userMenu = new UserMenu();

        userMenu.menuName = this.menuName;
        userMenu.deleteDate = this.deleteDate;
        userMenu.permission = this.permission;
        userMenu.saveDate = this.saveDate;
        userMenu.toolTip = this.toolTip;
        userMenu.viewDate = this.viewDate;

        if (this.getControlPermission() != null) {
            for (String s : this.getControlPermission()) {
                userMenu.getControlPermission().add(s);
            }
        }
        
        if (this.getUserMenu() != null) {
            for (UserMenu userMenu1 : this.getUserMenu()) {
                userMenu.getUserMenu().add(userMenu1.clone());
            }
        }

        return userMenu;
    }

}
