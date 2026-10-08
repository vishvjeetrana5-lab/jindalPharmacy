package dataman.dmToolkit.walkThrough.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class WalkThroughDefault_Mdl implements IsSerializable {

    String userName, vType, siteCode, departmentCode, valueUpto;

    public WalkThroughDefault_Mdl() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getvType() {
        return vType;
    }

    public void setvType(String vType) {
        this.vType = vType;
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

    public String getValueUpto() {
        return valueUpto;
    }

    public void setValueUpto(String valueUpto) {
        this.valueUpto = valueUpto;
    }
}
