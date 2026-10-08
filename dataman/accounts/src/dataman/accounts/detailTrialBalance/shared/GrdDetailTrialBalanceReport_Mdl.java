package dataman.accounts.detailTrialBalance.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class GrdDetailTrialBalanceReport_Mdl implements Serializable, IsSerializable {

    private String amtDr;
    private String amtCr;
    private String particularsTag;
    private String particularsText;
    private String amtOpening;
    private String amtOpDrCr;
    private String amtClosing;
    private String amtClDrCr;

    public GrdDetailTrialBalanceReport_Mdl() {
    }

    public String getAmtOpening() {
        return amtOpening;
    }

    public void setAmtOpening(String amtOpening) {
        this.amtOpening = amtOpening;
    }

    public String getAmtOpDrCr() {
        return amtOpDrCr;
    }

    public void setAmtOpDrCr(String amtOpDrCr) {
        this.amtOpDrCr = amtOpDrCr;
    }

    public String getAmtClosing() {
        return amtClosing;
    }

    public void setAmtClosing(String amtClosing) {
        this.amtClosing = amtClosing;
    }

    public String getAmtClDrCr() {
        return amtClDrCr;
    }

    public void setAmtClDrCr(String amtClDrCr) {
        this.amtClDrCr = amtClDrCr;
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
