package dataman.accounts.voucherEntryPaymentJV.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class LedgerBalance_Mdl  extends KeyField implements Serializable, IsSerializable {
    private String opening;
    private String balance;

    private String ytdDr;
    private String ytdCr;
    private String tdsDr;
    private String tdsCr;
    
    public LedgerBalance_Mdl(){
        
    }

    public String getOpening() {
        return opening;
    }

    public void setOpening(String opening) {
        this.opening = opening;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getYtdDr() {
        return ytdDr;
    }

    public void setYtdDr(String ytdDr) {
        this.ytdDr = ytdDr;
    }

    public String getYtdCr() {
        return ytdCr;
    }

    public void setYtdCr(String ytdCr) {
        this.ytdCr = ytdCr;
    }

    public String getTdsDr() {
        return tdsDr;
    }

    public void setTdsDr(String tdsDr) {
        this.tdsDr = tdsDr;
    }

    public String getTdsCr() {
        return tdsCr;
    }

    public void setTdsCr(String tdsCr) {
        this.tdsCr = tdsCr;
    }
    
    
}
