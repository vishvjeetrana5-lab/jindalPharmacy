package aarogya.master.overHeadCategoryMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String nature;
    private String itsOnSaleText;
    private String acServiceText;

    public Find_Mdl() {
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getItsOnSaleText() {
        return itsOnSaleText;
    }

    public void setItsOnSaleText(String itsOnSaleText) {
        this.itsOnSaleText = itsOnSaleText;
    }

    public String getAcServiceText() {
        return acServiceText;
    }

    public void setAcServiceText(String acServiceText) {
        this.acServiceText = acServiceText;
    }

}
