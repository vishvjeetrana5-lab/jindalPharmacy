package dataman.dmToolkit.itemAdjustment.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItemAdjustment_Mdl implements Serializable, IsSerializable {

    private boolean isEditable;
    private String acCode;
    private String acName;
    private String amtCr;
    private String amtDr;
    private List<GrdItemAdjustment_Mdl> lstItemAdjustment = new ArrayList<>();

    public ItemAdjustment_Mdl() {
    }

    public boolean getIsEditable() {
        return isEditable;
    }

    public void setIsEditable(boolean isEditable) {
        this.isEditable = isEditable;   
    }

    public String getAcCode() {
        return acCode;
    }

    public void setAcCode(String acCode) {
        this.acCode = acCode;
    }

    public String getAcName() {
        return acName;
    }

    public void setAcName(String acName) {
        this.acName = acName;
    }

    public String getAmtCr() {
        return amtCr;
    }

    public void setAmtCr(String amtCr) {
        this.amtCr = amtCr;
    }

    public String getAmtDr() {
        return amtDr;
    }

    public void setAmtDr(String amtDr) {
        this.amtDr = amtDr;
    }

    public List<GrdItemAdjustment_Mdl> getLstItemAdjustment() {
        return lstItemAdjustment;
    }

    public void setLstItemAdjustment(List<GrdItemAdjustment_Mdl> lstItemAdjustment) {
        this.lstItemAdjustment = lstItemAdjustment;
    }

}
