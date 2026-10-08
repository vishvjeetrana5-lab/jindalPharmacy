package aarogya.selfToolkit.walletSystem.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHCardType_Mdl extends KeyField implements Serializable, IsSerializable {

    private String nature;
    private String linkAccount;
    private String subCode;
    private String onAmount;
    private String points;
    private String valuePerPoint;
    private String expiryDate;
    private boolean physicallyRequired = false;

    public TxtHCardType_Mdl() {
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getLinkAccount() {
        return linkAccount;
    }

    public void setLinkAccount(String linkAccount) {
        this.linkAccount = linkAccount;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getOnAmount() {
        return onAmount;
    }

    public void setOnAmount(String onAmount) {
        this.onAmount = onAmount;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getValuePerPoint() {
        return valuePerPoint;
    }

    public void setValuePerPoint(String valuePerPoint) {
        this.valuePerPoint = valuePerPoint;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isPhysicallyRequired() {
        return physicallyRequired;
    }

    public void setPhysicallyRequired(boolean physicallyRequired) {
        this.physicallyRequired = physicallyRequired;
    }
}
