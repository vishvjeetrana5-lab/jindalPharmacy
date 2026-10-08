package dataman.utility.walkThroughMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;

public class GrdDefaultDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    private String v_TypeCode;
    private String v_TypeName;
    private String siteName;
    private String siteCode;
    private String departmentCode;
    private String departmentName;
    private String valueUpto;

    public GrdDefaultDetail_Mdl() {
    }

    public String getValueUpto() {
        return valueUpto;
    }

    public void setValueUpto(String valueUpto) {
        this.valueUpto = valueUpto;
    }

    public String getV_TypeCode() {
        return v_TypeCode;
    }

    public void setV_TypeCode(String v_TypeCode) {
        this.v_TypeCode = v_TypeCode;
    }

    public String getV_TypeName() {
        return v_TypeName;
    }

    public void setV_TypeName(String v_TypeName) {
        this.v_TypeName = v_TypeName;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
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
