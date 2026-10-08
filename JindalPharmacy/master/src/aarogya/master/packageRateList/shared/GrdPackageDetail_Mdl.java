package aarogya.master.packageRateList.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdPackageDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    private String packageCode;
    private String packageName;
    private String shortName;
    private String orgManualCode;
    private String amount;
    private String applicable;
    private String nature;
    private String active;
    private String applicableFrom;
    private String applicableTo;

    public GrdPackageDetail_Mdl() {
    }

    public String getPackageCode() {
        return packageCode;
    }

    public void setPackageCode(String packageCode) {
        this.packageCode = packageCode;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getOrgManualCode() {
        return orgManualCode;
    }

    public void setOrgManualCode(String orgManualCode) {
        this.orgManualCode = orgManualCode;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getApplicable() {
        return applicable;
    }

    public void setApplicable(String applicable) {
        this.applicable = applicable;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getApplicableFrom() {
        return applicableFrom;
    }

    public void setApplicableFrom(String applicableFrom) {
        this.applicableFrom = applicableFrom;
    }

    public String getApplicableTo() {
        return applicableTo;
    }

    public void setApplicableTo(String applicableTo) {
        this.applicableTo = applicableTo;
    }

}
