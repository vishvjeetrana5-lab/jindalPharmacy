package aarogya.pharmacy.schemeMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SchemeMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    public SchemeMaster_Mdl() {
    }

    private List<GrdSchemeDetails_Mdl> lstSchemeDetails = new ArrayList<GrdSchemeDetails_Mdl>();
    private String itemUnitCode;
    private String itemUnitName;

    public List<GrdSchemeDetails_Mdl> getLstSchemeDetails() {
        return lstSchemeDetails;
    }

    public void setLstSchemeDetails(List<GrdSchemeDetails_Mdl> lstSchemeDetails) {
        this.lstSchemeDetails = lstSchemeDetails;
    }

    public String getItemUnitCode() {
        return itemUnitCode;
    }

    public void setItemUnitCode(String itemUnitCode) {
        this.itemUnitCode = itemUnitCode;
    }

    public String getItemUnitName() {
        return itemUnitName;
    }

    public void setItemUnitName(String itemUnitName) {
        this.itemUnitName = itemUnitName;
    }

}
