package aarogya.master.eod.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class EOD_Mdl implements Serializable, IsSerializable {

    private String currentDate, currentTime, siteCode, siteShortName, userName, databaseName, voucherPrefix, companyCode;
    private String controlAc, controlAcName, selfAc, selfAcName, hospitalAc, hospitalAcName, IPDunbilledserviceAcsubCode, IPDunbilledServiceAcName;
    private String allowDiscountPosting;

    public EOD_Mdl() {
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getSiteShortName() {
        return siteShortName;
    }

    public void setSiteShortName(String siteShortName) {
        this.siteShortName = siteShortName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getVoucherPrefix() {
        return voucherPrefix;
    }

    public void setVoucherPrefix(String voucherPrefix) {
        this.voucherPrefix = voucherPrefix;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getControlAc() {
        return controlAc;
    }

    public void setControlAc(String controlAc) {
        this.controlAc = controlAc;
    }

    public String getControlAcName() {
        return controlAcName;
    }

    public void setControlAcName(String controlAcName) {
        this.controlAcName = controlAcName;
    }

    public String getSelfAc() {
        return selfAc;
    }

    public void setSelfAc(String selfAc) {
        this.selfAc = selfAc;
    }

    public String getSelfAcName() {
        return selfAcName;
    }

    public void setSelfAcName(String selfAcName) {
        this.selfAcName = selfAcName;
    }

    public String getHospitalAc() {
        return hospitalAc;
    }

    public void setHospitalAc(String hospitalAc) {
        this.hospitalAc = hospitalAc;
    }

    public String getHospitalAcName() {
        return hospitalAcName;
    }

    public void setHospitalAcName(String hospitalAcName) {
        this.hospitalAcName = hospitalAcName;
    }

   

    public String getAllowDiscountPosting() {
        return allowDiscountPosting;
    }

    public void setAllowDiscountPosting(String allowDiscountPosting) {
        this.allowDiscountPosting = allowDiscountPosting;
    }

    public String getIPDunbilledserviceAcsubCode() {
        return IPDunbilledserviceAcsubCode;
    }

    public void setIPDunbilledserviceAcsubCode(String IPDunbilledserviceAcsubCode) {
        this.IPDunbilledserviceAcsubCode = IPDunbilledserviceAcsubCode;
    }

    public String getIPDunbilledServiceAcName() {
        return IPDunbilledServiceAcName;
    }

    public void setIPDunbilledServiceAcName(String IPDunbilledServiceAcName) {
        this.IPDunbilledServiceAcName = IPDunbilledServiceAcName;
    }

}
