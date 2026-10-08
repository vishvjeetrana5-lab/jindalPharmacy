/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataman.dmToolkit.gst.shared;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

/**
 *
 * @author DATAMAN
 */
public class EinvCancelInfoDtls_Mdl  implements Serializable, IsSerializable{

    @JsonProperty("InfCd")
    private String InfCd;
    @JsonProperty("Desc")
    private Object Desc;

    public EinvCancelInfoDtls_Mdl() {
    }

    
    public String getInfCd() {
        return InfCd;
    }

    public void setInfCd(String InfCd) {
        this.InfCd = InfCd;
    }

    public Object getDesc() {
        return Desc;
    }

    public void setDesc(Object Desc) {
        this.Desc = Desc;
    }

}
