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
public class EInvDocDtls_Mdl implements Serializable, IsSerializable {
    
    @JsonProperty("Typ")
    private String typ;
    @JsonProperty("No")
    private String no;
    @JsonProperty("Dt")
    private String dt;

    public EInvDocDtls_Mdl() {
    }

    
    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }
    
}
