package dataman.dmToolkit.gst.shared;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.List;

public class EWayViaIrnResponse_Mdl implements Serializable, IsSerializable {
    @JsonProperty("Status") 
    public String status;
    @JsonProperty("Data") 
    public String data;
    @JsonProperty("ErrorDetails") 
    public List<ErrorDetails_Mdl> errorDetails;
    @JsonProperty("InfoDtls") 
    public List<EinvCancelInfoDtls_Mdl> infoDtls;


    public EWayViaIrnResponse_Mdl() {
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
