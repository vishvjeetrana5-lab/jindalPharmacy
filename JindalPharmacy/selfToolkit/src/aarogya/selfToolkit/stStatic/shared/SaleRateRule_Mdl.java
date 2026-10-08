package aarogya.selfToolkit.stStatic.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class SaleRateRule_Mdl implements Serializable, IsSerializable {

    private String marginFrom;
    private String marginTo;
    private String profitToBeTaken;

    public SaleRateRule_Mdl() {
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

    public void setMarginTo(String marginTo) {
        this.marginTo = marginTo;
    }

    public String getProfitToBeTaken() {
        return profitToBeTaken;
    }

    public void setProfitToBeTaken(String profitToBeTaken) {
        this.profitToBeTaken = profitToBeTaken;
    }

}
