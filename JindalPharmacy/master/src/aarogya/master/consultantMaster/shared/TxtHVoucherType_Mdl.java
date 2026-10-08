package aarogya.master.consultantMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHVoucherType_Mdl extends KeyField implements Serializable, IsSerializable {

    private String recId;
    private String defaultAc;
    private String daName;
    private String combination;

    public TxtHVoucherType_Mdl() {
    }

    public String getRecId() {
        return recId;
    }

    public void setRecId(String RecId) {
        this.recId = RecId;
    }

    public String getDefaultAc() {
        return defaultAc;
    }

    public void setDefaultAc(String DefaultAc) {
        this.defaultAc = DefaultAc;
    }

    public String getDAName() {
        return daName;
    }

    public void setDAName(String DAName) {
        this.daName = DAName;
    }

    public String getCombination() {
        return combination;
    }

    public void setCombination(String Combination) {
        this.combination = Combination;
    }
}
