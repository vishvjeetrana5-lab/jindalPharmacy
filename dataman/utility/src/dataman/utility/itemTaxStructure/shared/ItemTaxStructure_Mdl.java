package dataman.utility.itemTaxStructure.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItemTaxStructure_Mdl extends KeyField implements Serializable, IsSerializable {

    private String applicableFrom,totalTaxPercent,nature;
    private List<GrdItemTaxStructure_Mdl> grdItemTaxStructure = new ArrayList<GrdItemTaxStructure_Mdl>();

    public ItemTaxStructure_Mdl() {
    }

    public List<GrdItemTaxStructure_Mdl> getGrdItemTaxStructure() {
        return grdItemTaxStructure;
    }

    public String getApplicableFrom() {
        return applicableFrom;
    }

    public void setApplicableFrom(String applicableFrom) {
        this.applicableFrom = applicableFrom;
    }

    public String getTotalTaxPercent() {
        return totalTaxPercent;
    }

    public void setTotalTaxPercent(String totalTaxPercent) {
        this.totalTaxPercent = totalTaxPercent;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }
}
