package dataman.accounts.trialBalance.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DefaultValue_Mdl implements Serializable, IsSerializable {

    private String dtTo;
    private String dtFrom;
    private String isDifference;
    private String amtDr;
    private String isDrCr;
    private String amtCr;
    private String amtDiff;
    private List<TrialBalance_Mdl> lstTrialBalane = new ArrayList<TrialBalance_Mdl>();

    public DefaultValue_Mdl() {
    }

    public List<TrialBalance_Mdl> getLstTrialBalane() {
        return lstTrialBalane;
    }

    public String getDtTo() {
        return dtTo;
    }

    public void setDtTo(String dtTo) {
        this.dtTo = dtTo;
    }

    public String getIsDifference() {
        return isDifference;
    }

    public void setIsDifference(String isDifference) {
        this.isDifference = isDifference;
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

    public String getAmtDiff() {
        return amtDiff;
    }

    public void setAmtDiff(String amtDiff) {
        this.amtDiff = amtDiff;
    }

    public String getIsDrCr() {
        return isDrCr;
    }

    public void setIsDrCr(String isDrCr) {
        this.isDrCr = isDrCr;
    }

    public String getDtFrom() {
        return dtFrom;
    }

    public void setDtFrom(String dtFrom) {
        this.dtFrom = dtFrom;
    }
}
