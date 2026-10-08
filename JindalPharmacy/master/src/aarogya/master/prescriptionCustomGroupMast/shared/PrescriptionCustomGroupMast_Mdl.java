package aarogya.master.prescriptionCustomGroupMast.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class PrescriptionCustomGroupMast_Mdl extends KeyField implements Serializable, IsSerializable {

    private String natureCode;
    private String natureName;
    private String prnSrNo;

    public PrescriptionCustomGroupMast_Mdl() {
    }

    public String getNatureCode() {
        return natureCode;
    }

    public void setNatureCode(String natureCode) {
        this.natureCode = natureCode;
    }

    public String getNatureName() {
        return natureName;
    }

    public void setNatureName(String natureName) {
        this.natureName = natureName;
    }

    public String getPrnSrNo() {
        return prnSrNo;
    }

    public void setPrnSrNo(String prnSrNo) {
        this.prnSrNo = prnSrNo;
    }

}
