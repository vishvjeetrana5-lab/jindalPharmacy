package dataman.dmBase.self.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.UserMenu;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

public class UserPermission implements Serializable,IsSerializable{

    private LinkedHashMap<String, List<UserMenu>> userMenuList;
    private List<VoucherPermission> voucherPermissionList;
    private String voucherType;
    private String voucherDate;
    private String voucherValue;

    public UserPermission() {

    }

    public UserPermission(LinkedHashMap<String, List<UserMenu>> userMenuList, List<VoucherPermission> voucherPermissionList) {
        this.userMenuList = userMenuList;
        this.voucherPermissionList = voucherPermissionList;
    }

    public LinkedHashMap<String, List<UserMenu>> getUserMenu() {
        return userMenuList;
    }

    public void setUserMenu(LinkedHashMap<String, List<UserMenu>> userMenu) {
        this.userMenuList = userMenu;
    }

    public List<VoucherPermission> getVoucherPermission() {
        return voucherPermissionList;
    }

    public void setVoucherPermission(List<VoucherPermission> voucherPermissionList) {
        this.voucherPermissionList = voucherPermissionList;
    }

    public String getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getVoucherValue() {
        return voucherValue;
    }

    public void setVoucherValue(String voucherValue) {
        this.voucherValue = voucherValue;
    }
}
