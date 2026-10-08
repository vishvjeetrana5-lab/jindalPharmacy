package aarogya.master.eod.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdAcPostedDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    private String accountName;
    private String contraSubName;
    private String narration;
    private String amtDr;
    private String amtCr;

    public GrdAcPostedDetail_Mdl() {
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getContraSubName() {
        return contraSubName;
    }

    public void setContraSubName(String contraSubName) {
        this.contraSubName = contraSubName;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
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
