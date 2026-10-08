/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataman.dmToolkit.gst.shared;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.List;

public class EInvoice_Mdl implements Serializable, IsSerializable {

    @JsonProperty("Status")
    private String status;
    @JsonProperty("Data")
    private String data;
    @JsonProperty("ErrorDetails")
    private List<ErrorDetails_Mdl> errorDetails;
    @JsonProperty("InfoDtls")
    private List<EinvCancelInfoDtls_Mdl> infoDtls;

    public EInvoice_Mdl() {
    }
    
    
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<ErrorDetails_Mdl> getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(List<ErrorDetails_Mdl> errorDetails) {
        this.errorDetails = errorDetails;
    }

    public List<EinvCancelInfoDtls_Mdl> getInfoDtls() {
        return infoDtls;
    }

    public void setInfoDtls(List<EinvCancelInfoDtls_Mdl> infoDtls) {
        this.infoDtls = infoDtls;
    }

}
