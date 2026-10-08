package aarogya.pharmacy.medicineDepartmentInfo.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MedicineDepartmentInfo_Mdl extends KeyField implements Serializable, IsSerializable {

    private String departmentCode;
    private String departmentName;
    private String siteCode;
    private String siteName;

    private List<GrdMedicineDepartmentInfo_Mdl> grdMed_Mdl = new ArrayList<GrdMedicineDepartmentInfo_Mdl>();

    public MedicineDepartmentInfo_Mdl() {
    }

    public List<GrdMedicineDepartmentInfo_Mdl> getGrdMed_Mdl() {
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
