package aarogya.master.overHeadCategoryMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class OverHeadCategoryMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String itsOnSaleTag;
    private String itsOnSaleText;
    private String acServiceTag;
    private String acServiceText;

    public OverHeadCategoryMaster_Mdl() {
    }

    public String getItsOnSaleTag() {
        return itsOnSaleTag;
    }

    public void setItsOnSaleTag(String itsOnSaleTag) {
        this.itsOnSaleTag = itsOnSaleTag;
    }

    public String getItsOnSaleText() {
        return itsOnSaleText;
    }

    public void setItsOnSaleText(String itsOnSaleText) {
        this.itsOnSaleText = itsOnSaleText;
    }

    public String getAcServiceTag() {
        return acServiceTag;
    }

    public void setAcServiceTag(String acServiceTag) {
        this.acServiceTag = acServiceTag;
    }

    public String getAcServiceText() {
        return acServiceText;
    }

    public void setAcServiceText(String acServiceText) {
        this.acServiceText = acServiceText;
    }

}
