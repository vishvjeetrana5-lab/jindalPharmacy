package aarogya.inventory.projectIndicator.shared;

import aarogya.inventory.projectIndicator.shared.*;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyFieldExtended;
import java.io.Serializable;

public class DefaultValue_Mdl implements Serializable, IsSerializable {

    private String currentDate;

    //New Fields
    private boolean isContinuousNewMode = false;

    private String siteCode;
    private String vDate;
    private KeyFieldExtended voucherType = new KeyFieldExtended();
    private KeyFieldExtended newVoucherType = new KeyFieldExtended();

    public DefaultValue_Mdl() {
    }

    public String getSiteCode() {
        return siteCode;
    }
    
    public String getCurrentDate() {
        return currentDate;
    }

    public KeyFieldExtended getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(KeyFieldExtended voucherType) {
        this.voucherType = voucherType;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public boolean getIsContinuousNewMode() {
        return isContinuousNewMode;
    }

    public void setIsContinuousNewMode(boolean isContinuousNewMode) {
        this.isContinuousNewMode = isContinuousNewMode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getvDate() {
        return vDate;
    }

    public void setvDate(String vDate) {
        this.vDate = vDate;
    }

    public KeyFieldExtended getNewVoucherType() {
        return newVoucherType;
    }

    public void setNewVoucherType(KeyFieldExtended newVoucherType) {
        this.newVoucherType = newVoucherType;
    }

}
