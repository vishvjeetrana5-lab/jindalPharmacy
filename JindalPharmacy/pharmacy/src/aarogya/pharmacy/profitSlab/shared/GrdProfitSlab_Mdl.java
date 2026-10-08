package aarogya.pharmacy.profitSlab.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdProfitSlab_Mdl extends KeyField implements Serializable, IsSerializable {

    private String marginFrom;
    private String marginTo;
    private String profitPercent;

    public GrdProfitSlab_Mdl() {
    }

    public String getMarginFrom() {
        return marginFrom;
    }

    public void setMarginFrom(String marginFrom) {
        this.marginFrom = marginFrom;
    }

    public String getMarginTo() {
        return marginTo;
    }

    public void setMarginTo(String MarginTo) {
        this.marginTo = MarginTo;
    }

    public String getProfitPercent() {
        return profitPercent;
    }

    public void setProfitPercent(String profitPercent) {
        this.profitPercent = profitPercent;
    }

}
