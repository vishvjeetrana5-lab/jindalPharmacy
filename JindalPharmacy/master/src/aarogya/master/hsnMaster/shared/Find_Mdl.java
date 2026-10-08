package aarogya.master.hsnMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String HSN;
    private String taxBasedOn;
    private String nature;

    public Find_Mdl() {
    }

    public String getHSNCode() {
        return HSN;
    }

    public void setHSNCode(String hsnCode) {
        this.HSN = hsnCode;
    }

    public String getHSN() {
        return HSN;
    }

    public void setHSN(String HSN) {
        this.HSN = HSN;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getTaxBasedOn() {
        return taxBasedOn;
    }

    public void setTaxBasedOn(String taxBasedOn) {
        this.taxBasedOn = taxBasedOn;
    }

}
