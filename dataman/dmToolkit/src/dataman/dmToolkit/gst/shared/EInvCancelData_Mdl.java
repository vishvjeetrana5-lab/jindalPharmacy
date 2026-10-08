package dataman.dmToolkit.gst.shared;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class EInvCancelData_Mdl implements Serializable, IsSerializable {
    @JsonProperty("Irn")
    private String irn;
    @JsonProperty("CancelDate")
    private String cancelDate;

    public EInvCancelData_Mdl() {
    }

    public String getIrn() {
        return irn;
    }

    public void setIrn(String irn) {
        this.irn = irn;
    }

    public String getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(String cancelDate) {
        this.cancelDate = cancelDate;
    }    
}
