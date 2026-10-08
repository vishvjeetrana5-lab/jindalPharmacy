/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aarogya.master.organizationMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dataman
 */
public class GrdDiscountPercent_Mdl extends KeyField implements Serializable,IsSerializable{
    
    private String OrganizationCode;
    private List<GrdPercentage_Mdl> percentages = new ArrayList<GrdPercentage_Mdl>();

    public GrdDiscountPercent_Mdl() {
    }

    public String getOrganizationCode() {
        return OrganizationCode;
    }

    public void setOrganizationCode(String OrganizationCode) {
        this.OrganizationCode = OrganizationCode;
    }

    public List<GrdPercentage_Mdl> getPercentages() {
        return percentages;
    }

    public void setPercentages(List<GrdPercentage_Mdl> percentages) {
        this.percentages = percentages;
    }
    
    
    
}
