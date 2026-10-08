package dataman.dmBase.self.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PCSData implements Serializable,IsSerializable{

    private String loginDate = null;
    private String userDescription = null;
    private String userName = null;
    private String regionalLanguage = null;
    private String localeCountry = null;

    public List<PLT> lstPLTs = new ArrayList<>();
    public PLT pltSelected = null;

    public List<Company> lstCompanys = new ArrayList<>();
    public Company cSelected = null;

    public List<Site> lstSites = new ArrayList<>();
    public Site sSelected = null;

    public PCSData() {
    }

    public String getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(String loginDate) {
        this.loginDate = loginDate;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRegionalLanguage() {
        return regionalLanguage;
    }

    public void setRegionalLanguage(String regionalLanguage) {
        this.regionalLanguage = regionalLanguage;
    }

    public void setPLT(PCSData pltTemp) {
        this.lstPLTs = pltTemp.lstPLTs;
        this.pltSelected = pltTemp.pltSelected;
    }

    public void setCompany(PCSData pltTemp) {
        this.lstCompanys = pltTemp.lstCompanys;
        this.cSelected = pltTemp.cSelected;
    }

    public void setSite(PCSData pltTemp) {
        this.lstSites = pltTemp.lstSites;
        this.sSelected = pltTemp.sSelected;
    }

    public String getLocaleCountry() {
        return localeCountry;
    }

    public void setLocaleCountry(String localeCountry) {
        this.localeCountry = localeCountry;
    }
}
