package aarogya.master.provisionalStatement.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.DmEnum.DmMonths;
import java.io.Serializable;
import java.util.HashMap;

public class DefaultData_Mdl implements Serializable, IsSerializable {

    private HashMap<DmMonths, String[]> hmMonths = new HashMap<DmMonths, String[]>();
    private String currentSiteCode, currentSiteName;
    private String vrType, serviceCategoryCSV;

    public DefaultData_Mdl() {
    }

    public HashMap<DmMonths, String[]> getHmMonths() {
        return this.hmMonths;
    }

    public String getCurrentSiteCode() {
        return currentSiteCode;
    }

    public void setCurrentSiteCode(String currentSiteCode) {
        this.currentSiteCode = currentSiteCode;
    }

    public String getCurrentSiteName() {
        return currentSiteName;
    }

    public void setCurrentSiteName(String currentSiteName) {
        this.currentSiteName = currentSiteName;
    }

    public String getVrType() {
        return vrType;
    }

    public void setVrType(String vrType) {
        this.vrType = vrType;
    }

    public String getServiceCategoryCSV() {
        return serviceCategoryCSV;
    }

    public void setServiceCategoryCSV(String serviceCategoryCSV) {
        this.serviceCategoryCSV = serviceCategoryCSV;
    }
}
