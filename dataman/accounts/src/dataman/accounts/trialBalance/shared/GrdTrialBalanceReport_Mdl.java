package dataman.accounts.trialBalance.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class GrdTrialBalanceReport_Mdl implements Serializable, IsSerializable {

    private String particularsTag;
    private String particularsText;
    private String isChild;
    private String isBold;
    private String childBalance;
    private String amtDr;
    private String amtCr;

    public GrdTrialBalanceReport_Mdl() {
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

    public String getIsChild() {
        return isChild;
    }

    public void setIsChild(String isChild) {
        this.isChild = isChild;
    }

    public String getIsBold() {
        return isBold;
    }

    public void setIsBold(String isBold) {
        this.isBold = isBold;
    }

    public String getChildBalance() {
        return childBalance;
    }

    public void setChildBalance(String childBalance) {
        this.childBalance = childBalance;
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

}
