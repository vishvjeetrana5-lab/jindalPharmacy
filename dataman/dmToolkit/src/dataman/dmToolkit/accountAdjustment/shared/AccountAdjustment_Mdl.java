package dataman.dmToolkit.accountAdjustment.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AccountAdjustment_Mdl implements Serializable, IsSerializable {

    private boolean isEditable;
//    private int rowIndex;
    private String acCode;
    private String acName;
    private String amtCr;
    private String amtDr;
    private List<GrdAdjustment_Mdl> lstAdjustment = new ArrayList<>();

    public AccountAdjustment_Mdl() {
    }

    public boolean getIsEditable() {
        return isEditable;
    }

    public void setIsEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }

//    public int getRowIndex() {
//        return rowIndex;
//    }
//
//    public void setRowIndex(int rowIndex) {
//        this.rowIndex = rowIndex;
//    }
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

    public List<GrdAdjustment_Mdl> getLstAdjustment() {
        return lstAdjustment;
    }

    public void setLstAdjustment(List<GrdAdjustment_Mdl> lstAdjustment) {
        this.lstAdjustment = lstAdjustment;
    }

}
