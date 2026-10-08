package aarogya.pharmacy.profitSlab.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProfitSlab_Mdl extends KeyField implements Serializable, IsSerializable {

    private List<GrdProfitSlab_Mdl> grdData = new ArrayList<GrdProfitSlab_Mdl>();
    private String marginFrom;
    private String marginTo;
    private String profitPercent;

    public ProfitSlab_Mdl() {
    }

    public List<GrdProfitSlab_Mdl> getGrdData() {
        return grdData;
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

    public String getProfitPercent() {
        return profitPercent;
    }

    public void setProfitPercent(String profitPercent) {
        this.profitPercent = profitPercent;
    }

}
