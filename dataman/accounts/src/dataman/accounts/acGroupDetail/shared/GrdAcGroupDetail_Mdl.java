package dataman.accounts.acGroupDetail.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class GrdAcGroupDetail_Mdl implements Serializable, IsSerializable {

    private String amtDr;
    private String amtCr;
    private String particularsTag;
    private String particularsText;
    private String amtBalance;
    private boolean acgroup;

    public GrdAcGroupDetail_Mdl() {

    }

    public boolean isAcgroup() {
        return acgroup;
    }

    public void setAcgroup(boolean acgroup) {
        this.acgroup = acgroup;
    }

    public String getAmtBalance() {
        return amtBalance;
    }

    public void setAmtBalance(String amtBalance) {
        this.amtBalance = amtBalance;
    }

    public String getAmtDr() {
        return amtDr;
    }

    public void setAmtDr(String amtDr) {
        this.amtDr = amtDr;
    }

    public String getAmtCr() {
        return amtCr;
    }

    public void setAmtCr(String amtCr) {
        this.amtCr = amtCr;
    }

    public String getParticularsTag() {
        return particularsTag;
    }

    public void setParticularsTag(String particularsTag) {
        this.particularsTag = particularsTag;
    }

    public String getParticularsText() {
        return particularsText;
    }

    public void setParticularsText(String particularsText) {
        this.particularsText = particularsText;
    }

}
