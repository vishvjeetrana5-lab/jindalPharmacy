package dataman.dmToolkit.ledgerPostingDisplayPopup.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;


public class GrdLedgerDisplayDetail_Mdl extends KeyField implements Serializable, IsSerializable {
    
    private String amtCr ;
    private String amtDr ;

    public GrdLedgerDisplayDetail_Mdl() {
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
    
    
    
}
