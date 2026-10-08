package dataman.utility.itemTaxStructure.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdItemTaxStructure_Mdl extends KeyField implements Serializable, IsSerializable {

    private String locationText;
    private String locationTag;
    private String overHeadTag;
    private String overHeadText;
    private String accntAffectedTag;
    private String accntAffectedText;
    private String siteTag;
    private String siteText;
    private String atRate;
    private String amount;

    public GrdItemTaxStructure_Mdl() {
    }

    public String getOverHeadTag() {
        return overHeadTag;
    }

    public void setOverHeadTag(String overHeadTag) {
        this.overHeadTag = overHeadTag;
    }

    public String getOverHeadText() {
        return overHeadText;
    }

    public void setOverHeadText(String overHeadText) {
        this.overHeadText = overHeadText;
    }

    public String getAccntAffectedTag() {
        return accntAffectedTag;
    }

    public void setAccntAffectedTag(String accntAffectedTag) {
        this.accntAffectedTag = accntAffectedTag;
    }

    public String getAccntAffectedText() {
        return accntAffectedText;
    }

    public void setAccntAffectedText(String accntAffectedText) {
        this.accntAffectedText = accntAffectedText;
    }

    public String getSiteTag() {
        return siteTag;
    }

    public void setSiteTag(String siteTag) {
        this.siteTag = siteTag;
    }

    public String getSiteText() {
        return siteText;
    }

    public void setSiteText(String siteText) {
        this.siteText = siteText;
    }

    public String getAtRate() {
        return atRate;
    }

    public void setAtRate(String atRate) {
        this.atRate = atRate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getLocationText() {
        return locationText;
    }

    public void setLocationText(String locationText) {
        this.locationText = locationText;
    }

    public String getLocationTag() {
        return locationTag;
    }

    public void setLocationTag(String locationTag) {
        this.locationTag = locationTag;
    }
}
