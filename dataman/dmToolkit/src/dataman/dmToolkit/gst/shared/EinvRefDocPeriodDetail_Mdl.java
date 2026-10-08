/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dataman.dmToolkit.gst.shared;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

/**
 *
 * @author Lenovo
 */
public class EinvRefDocPeriodDetail_Mdl implements Serializable, IsSerializable {

    @JsonProperty("InvStDt")
    private String refInvDocStartDate;
    @JsonProperty("InvEndDt")
    private String refInvDocEndDate;

    public EinvRefDocPeriodDetail_Mdl() {
    }

    public String getRefInvDocStartDate() {
        return refInvDocStartDate;
    }

    public void setRefInvDocStartDate(String refInvDocStartDate) {
        this.refInvDocStartDate = refInvDocStartDate;
    }

    public String getRefInvDocEndDate() {
        return refInvDocEndDate;
    }

    public void setRefInvDocEndDate(String refInvDocEndDate) {
        this.refInvDocEndDate = refInvDocEndDate;
    }
    
    

}
