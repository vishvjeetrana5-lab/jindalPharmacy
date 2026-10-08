package aarogya.master.organizationMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String shortName;
    private String active;
    private String nature;
    private String tdsAc;
    private String gsTin;
    private String orgEMail;
    private String mobile;

    public Find_Mdl() {
    }

    public String getOrgEMail() {
        return orgEMail;
    }

    public void setOrgEMail(String orgEMail) {
        this.orgEMail = orgEMail;
    }

  
    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getTdsAc() {
        return tdsAc;
    }

    public void setTdsAc(String tdsAc) {
        this.tdsAc = tdsAc;
    }

    public String getGsTin() {
        return gsTin;
    }

    public void setGsTin(String gsTin) {
        this.gsTin = gsTin;
    }

   
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
