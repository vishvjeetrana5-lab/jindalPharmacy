package aarogya.master.cardTypeMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String linkedAccount;
    private String nature;
    private String active;
    private String physicallyRequired;
    private String onAmount;
    private String points;
    private String valuePerPoint;
    private String shortName;
    private String cardTypeName;
    private String validDays;

    public Find_Mdl() {
    }

    public String getLinkedAccount() {
        return linkedAccount;
    }

    public void setLinkedAccount(String linkedAccount) {
        this.linkedAccount = linkedAccount;
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

    public String getPhysicallyRequired() {
        return physicallyRequired;
    }

    public void setPhysicallyRequired(String physicallyRequired) {
        this.physicallyRequired = physicallyRequired;
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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCardTypeName() {
        return cardTypeName;
    }

    public void setCardTypeName(String cardTypeName) {
        this.cardTypeName = cardTypeName;
    }

    public String getValidDays() {
        return validDays;
    }

    public void setValidDays(String validDays) {
        this.validDays = validDays;
    }

}
