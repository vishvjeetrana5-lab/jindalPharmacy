package aarogya.inventory.itemMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdItemDepartmentInfo_Mdl extends KeyField implements Serializable,IsSerializable{

    private String minStock;
    private String maxStock;
    private String reOrder;
    private String binName;
    private String siteCode;
    private String siteName;
    private String aMConsumptionValue;

    public GrdItemDepartmentInfo_Mdl() {
    }

    public String getMinStock() {
        return minStock;
    }

    public void setMinStock(String minStock) {
        this.minStock = minStock;
    }

    public String getMaxStock() {
        return maxStock;
    }

    public void setMaxStock(String maxStock) {
        this.maxStock = maxStock;
    }

    public String getReOrder() {
        return reOrder;
    }

    public void setReOrder(String reOrder) {
        this.reOrder = reOrder;
    }

    public String getBinName() {
        return binName;
    }

    public void setBinName(String binName) {
        this.binName = binName;
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

    public String getAMConsumptionValue() {
        return aMConsumptionValue;
    }

    public void setAMConsumptionValue(String aMConsumptionValue) {
        this.aMConsumptionValue = aMConsumptionValue;
    }

    

}
