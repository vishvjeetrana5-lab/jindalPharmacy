package dataman.dmToolkit.gst.shared;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.List;

public class EInvCancelResponse_Mdl implements Serializable, IsSerializable {

    @JsonProperty("Status") 
    private String status;
    @JsonProperty("Data") 
    private String data;
    @JsonProperty("ErrorDetails") 
    private ErrorDetails_Mdl errorDetails;
    @JsonProperty("InfoDtls") 
    private List<EinvCancelInfoDtls_Mdl> infoDtls;
    
    public EInvCancelResponse_Mdl() {
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

    public ErrorDetails_Mdl getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(ErrorDetails_Mdl errorDetails) {
        this.errorDetails = errorDetails;
    }

    public List<EinvCancelInfoDtls_Mdl> getInfoDtls() {
        return infoDtls;
    }

    public void setInfoDtls(List<EinvCancelInfoDtls_Mdl> infoDtls) {
        this.infoDtls = infoDtls;
    }

   

    

}
