package aarogya.pharmacy.reports.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHRequisitionNo_Mdl extends KeyField implements Serializable, IsSerializable {

    private String requisitionDate;
    private String requisitionVType;

    public TxtHRequisitionNo_Mdl() {
    }

    public String getRequisitionDate() {
        return requisitionDate;
    }

    public void setRequisitionDate(String requisitionDate) {
        this.requisitionDate = requisitionDate;
    }

    public String getRequisitionVType() {
        return requisitionVType;
    }

    public void setRequisitionVType(String requisitionVType) {
        this.requisitionVType = requisitionVType;
    }

}
