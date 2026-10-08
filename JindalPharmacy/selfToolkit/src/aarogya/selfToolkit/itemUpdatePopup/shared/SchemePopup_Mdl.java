package aarogya.selfToolkit.itemUpdatePopup.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class SchemePopup_Mdl implements Serializable, IsSerializable {

    private String masterSchemeOnQty;
    private String masterSchemeFreeQty;
    private String currentQty;
    private String currentFreeQty;
    private String currentSchemeOnQty;
    private String currentSchemeFreeQty;

    public SchemePopup_Mdl() {
    }

    public String getMasterSchemeOnQty() {
        return masterSchemeOnQty;
    }

    public void setMasterSchemeOnQty(String masterSchemeOnQty) {
        this.masterSchemeOnQty = masterSchemeOnQty;
    }

    public String getMasterSchemeFreeQty() {
        return masterSchemeFreeQty;
    }

    public void setMasterSchemeFreeQty(String masterSchemeFreeQty) {
        this.masterSchemeFreeQty = masterSchemeFreeQty;
    }

    public String getCurrentQty() {
        return currentQty;
    }

    public void setCurrentQty(String currentQty) {
        this.currentQty = currentQty;
    }

    public String getCurrentFreeQty() {
        return currentFreeQty;
    }

    public void setCurrentFreeQty(String currentFreeQty) {
        this.currentFreeQty = currentFreeQty;
    }

    public String getCurrentSchemeOnQty() {
        return currentSchemeOnQty;
    }

    public void setCurrentSchemeOnQty(String currentSchemeOnQty) {
        this.currentSchemeOnQty = currentSchemeOnQty;
    }

    public String getCurrentSchemeFreeQty() {
        return currentSchemeFreeQty;
    }

    public void setCurrentSchemeFreeQty(String currentSchemeFreeQty) {
        this.currentSchemeFreeQty = currentSchemeFreeQty;
    }

}
