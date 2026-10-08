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
public class EInvTranDtls_Mdl implements Serializable, IsSerializable {

    @JsonProperty("TaxSch")
    private String taxSch;
    @JsonProperty("SupTyp")
    private String supTyp;
    @JsonProperty("RegRev")
    private String regRev;
    @JsonProperty("EcmGstin")
    private String ecmGstin;
    @JsonProperty("IgstOnIntra")
    private String igstOnIntra;

    public EInvTranDtls_Mdl() {
    }

    
    public String getTaxSch() {
        return taxSch;
    }

    public void setTaxSch(String taxSch) {
        this.taxSch = taxSch;
    }

    public String getSupTyp() {
        return supTyp;
    }

    public void setSupTyp(String supTyp) {
        this.supTyp = supTyp;
    }

    public String getRegRev() {
        return regRev;
    }

    public void setRegRev(String regRev) {
        this.regRev = regRev;
    }

    public String getEcmGstin() {
        return ecmGstin;
    }

    public void setEcmGstin(String ecmGstin) {
        this.ecmGstin = ecmGstin;
    }

    public String getIgstOnIntra() {
        return igstOnIntra;
    }

    public void setIgstOnIntra(String igstOnIntra) {
        this.igstOnIntra = igstOnIntra;
    }
    
}
