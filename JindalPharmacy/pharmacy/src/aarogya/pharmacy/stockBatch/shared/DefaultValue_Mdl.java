package aarogya.pharmacy.stockBatch.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DefaultValue_Mdl implements IsSerializable, Serializable {

    private String openingValue;
    private String displayOpeningValue;
    private String balanceValue;
    private String displayBalanceValue;

    private Setting_Mdl setting_Mdl = new Setting_Mdl();

    public DefaultValue_Mdl() {
    }

    public String getOpeningValue() {
        return openingValue;
    }

    public void setOpeningValue(String openingValue) {
        this.openingValue = openingValue;
    }

    public String getDisplayOpeningValue() {
        return displayOpeningValue;
    }

    public void setDisplayOpeningValue(String displayOpeningValue) {
        this.displayOpeningValue = displayOpeningValue;
    }

    public String getBalanceValue() {
        return balanceValue;
    }

    public void setBalanceValue(String balanceValue) {
        this.balanceValue = balanceValue;
    }

    public String getDisplayBalanceValue() {
        return displayBalanceValue;
    }

    public void setDisplayBalanceValue(String displayBalanceValue) {
        this.displayBalanceValue = displayBalanceValue;
    }

    public Setting_Mdl getSetting_Mdl() {
        return setting_Mdl;
    }

    public void setSetting_Mdl(Setting_Mdl setting_Mdl) {
        this.setting_Mdl = setting_Mdl;
    }

}
