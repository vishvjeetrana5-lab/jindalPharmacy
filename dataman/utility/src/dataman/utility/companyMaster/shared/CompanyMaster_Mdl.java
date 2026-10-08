package dataman.utility.companyMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.List;

public class CompanyMaster_Mdl extends KeyField implements Serializable,IsSerializable{

    private String productLicenseKyCode;
    private String productLicenseKyName;
    private String companyCode;
    private String shortName;
    private String currentYear;
    private String previousYear;
    private String yearStartDate;
    private String yearEndDate;
    private String voucherPrefix;
    private String dataBaseName;
    private String fileDataBaseName;
    private String finacialYearTag;
    private String finacialYearText;
    private String majorCurrency;
    private String minorCurrency;

    private List<GrdCompanyMaster_Mdl> grdData;
    private List<Find_Mdl> findList;

    public CompanyMaster_Mdl() {
    }

    public List<Find_Mdl> getFindList() {
        return findList;
    }

    public List<GrdCompanyMaster_Mdl> getGrdData() {
        return grdData;
    }

    public String getProductLicenseKyCode() {
        return productLicenseKyCode;
    }

    public void setProductLicenseKyCode(String productLicenseKyCode) {
        this.productLicenseKyCode = productLicenseKyCode;
    }

    public String getProductLicenseKyName() {
        return productLicenseKyName;
    }

    public void setProductLicenseKyName(String productLicenseKyName) {
        this.productLicenseKyName = productLicenseKyName;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(String currentYear) {
        this.currentYear = currentYear;
    }

    public String getPreviousYear() {
        return previousYear;
    }

    public void setPreviousYear(String previousYear) {
        this.previousYear = previousYear;
    }

    public String getYearStartDate() {
        return yearStartDate;
    }

    public void setYearStartDate(String yearStartDate) {
        this.yearStartDate = yearStartDate;
    }

    public String getYearEndDate() {
        return yearEndDate;
    }

    public void setYearEndDate(String yearEndDate) {
        this.yearEndDate = yearEndDate;
    }

    public String getVoucherPrefix() {
        return voucherPrefix;
    }

    public void setVoucherPrefix(String voucherPrefix) {
        this.voucherPrefix = voucherPrefix;
    }

    public String getDataBaseName() {
        return dataBaseName;
    }

    public void setDataBaseName(String dataBaseName) {
        this.dataBaseName = dataBaseName;
    }

    public String getFileDataBaseName() {
        return fileDataBaseName;
    }

    public void setFileDataBaseName(String fileDataBaseName) {
        this.fileDataBaseName = fileDataBaseName;
    }

    public String getFinacialYearTag() {
        return finacialYearTag;
    }

    public void setFinacialYearTag(String finacialYearTag) {
        this.finacialYearTag = finacialYearTag;
    }

    public String getFinacialYearText() {
        return finacialYearText;
    }

    public void setFinacialYearText(String finacialYearText) {
        this.finacialYearText = finacialYearText;
    }

    public String getMajorCurrency() {
        return majorCurrency;
    }

    public void setMajorCurrency(String majorCurrency) {
        this.majorCurrency = majorCurrency;
    }

    public String getMinorCurrency() {
        return minorCurrency;
    }

    public void setMinorCurrency(String minorCurrency) {
        this.minorCurrency = minorCurrency;
    }

   

}
