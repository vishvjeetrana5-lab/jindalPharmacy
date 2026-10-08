/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dataman.dmToolkit.gst.shared;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Lenovo
 */
public class EinvRefDtls_Mdl implements Serializable, IsSerializable {
    
    @JsonProperty("InvRm")
    private String invRm;
    @JsonProperty("DocPerdDtls")
    private EinvRefDocPeriodDetail_Mdl docPerdDtls;
    @JsonProperty("PrecDocDtls")
    private List<EinvRefPrecedingDocDtls_Mdl> precDocDtls;
    @JsonProperty("ContrDtls")
    private List<EinvRefContractDtls_Mdl> contrDtls;

    public EinvRefDtls_Mdl() {
    }

    public String getInvRm() {
        return invRm;
    }

    public void setInvRm(String invRm) {
        this.invRm = invRm;
    }

    public EinvRefDocPeriodDetail_Mdl getDocPerdDtls() {
        return docPerdDtls;
    }

    public void setDocPerdDtls(EinvRefDocPeriodDetail_Mdl docPerdDtls) {
        this.docPerdDtls = docPerdDtls;
    }

    public List<EinvRefPrecedingDocDtls_Mdl> getPrecDocDtls() {
        return precDocDtls;
    }

    public void setPrecDocDtls(List<EinvRefPrecedingDocDtls_Mdl> precDocDtls) {
        this.precDocDtls = precDocDtls;
    }

    public List<EinvRefContractDtls_Mdl> getContrDtls() {
        return contrDtls;
    }

    public void setContrDtls(List<EinvRefContractDtls_Mdl> contrDtls) {
        this.contrDtls = contrDtls;
    }
    
    
    
}
