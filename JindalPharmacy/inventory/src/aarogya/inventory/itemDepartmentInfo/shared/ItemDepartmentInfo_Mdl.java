package aarogya.inventory.itemDepartmentInfo.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItemDepartmentInfo_Mdl extends KeyField implements Serializable, IsSerializable {

    private String departmentCode;
    private String departmentName;
    private String siteCode;
    private String siteName;

    private List<GrdItemDepartmentInfo_Mdl> grdMed_Mdl = new ArrayList<GrdItemDepartmentInfo_Mdl>();

    public ItemDepartmentInfo_Mdl() {
    }

    public List<GrdItemDepartmentInfo_Mdl> getGrdMed_Mdl() {
        return grdMed_Mdl;
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

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

}
