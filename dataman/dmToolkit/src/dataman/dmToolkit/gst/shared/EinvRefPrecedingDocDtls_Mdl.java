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
public class EinvRefPrecedingDocDtls_Mdl implements Serializable, IsSerializable {

    @JsonProperty("InvNo")
    private String refRecId;
    @JsonProperty("InvDt")
    private String refV_Date;
    @JsonProperty("OthRefNo")
    private String refDocOtherRef;

    public EinvRefPrecedingDocDtls_Mdl() {
    }

    public String getRefRecId() {
        return refRecId;
    }

    public void setRefRecId(String refRecId) {
        this.refRecId = refRecId;
    }

    public String getRefV_Date() {
        return refV_Date;
    }

    public void setRefV_Date(String refV_Date) {
        this.refV_Date = refV_Date;
    }

    public String getRefDocOtherRef() {
        return refDocOtherRef;
    }

    public void setRefDocOtherRef(String refDocOtherRef) {
        this.refDocOtherRef = refDocOtherRef;
    }
}
