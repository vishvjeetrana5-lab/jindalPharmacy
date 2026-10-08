/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aarogya.master.consultantMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

/**
 *
 * @author dataman
 */
public class GrdSiteDetail_Mdl extends KeyField implements Serializable,IsSerializable{
    
    private boolean OPDConsultant,allowForSite;
    private String siteName;

    public GrdSiteDetail_Mdl() {
    }

    public boolean isOPDConsultant() {
        return OPDConsultant;
    }

    public void setIsOPDConsultant(boolean isOPDConsultant) {
        this.OPDConsultant = isOPDConsultant;
    }

    public boolean isAllowForSite() {
        return allowForSite;
    }

    public void setAllowForSite(boolean allowForSite) {
        this.allowForSite = allowForSite;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }
    
    
    
}
