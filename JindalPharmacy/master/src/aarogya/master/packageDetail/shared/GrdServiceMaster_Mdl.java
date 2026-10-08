package aarogya.master.packageDetail.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdServiceMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String quantity;
    private String rate;
    private String amount;
    private String pkgAmount;
    private String pkgRate;

    public GrdServiceMaster_Mdl() {
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPkgAmount() {
        return pkgAmount;
    }

    public void setPkgAmount(String pkgAmount) {
        this.pkgAmount = pkgAmount;
    }

    public String getPkgRate() {
        return pkgRate;
    }

    public void setPkgRate(String pkgRate) {
        this.pkgRate = pkgRate;
    }
}
