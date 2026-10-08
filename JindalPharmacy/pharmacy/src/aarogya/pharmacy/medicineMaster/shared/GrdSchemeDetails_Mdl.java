package aarogya.pharmacy.medicineMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdSchemeDetails_Mdl extends KeyField implements Serializable, IsSerializable {

    public GrdSchemeDetails_Mdl() {
    }

    //sharedkeyFieldCode=freeItemCode
    //sharedkeyFieldName=freeitemName
    private String v_SNo;
    private String schemeOnQty;
    private String schemeFreeQty;
    private String description;
    private String freeItemUnitCode;
    private String freeItemUnitName;

    public String getFreeItemUnitCode() {
        return freeItemUnitCode;
    }

    public void setFreeItemUnitCode(String freeItemUnitCode) {
        this.freeItemUnitCode = freeItemUnitCode;
    }

    public String getFreeItemUnitName() {
        return freeItemUnitName;
    }

    public void setFreeItemUnitName(String freeItemUnitName) {
        this.freeItemUnitName = freeItemUnitName;
    }

    public String getV_SNo() {
        return v_SNo;
    }

    public void setV_SNo(String v_SNo) {
        this.v_SNo = v_SNo;
    }

    public String getSchemeOnQty() {
        return schemeOnQty;
    }

    public void setSchemeOnQty(String schemeOnQty) {
        this.schemeOnQty = schemeOnQty;
    }

    public String getSchemeFreeQty() {
        return schemeFreeQty;
    }

    public void setSchemeFreeQty(String schemeFreeQty) {
        this.schemeFreeQty = schemeFreeQty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
