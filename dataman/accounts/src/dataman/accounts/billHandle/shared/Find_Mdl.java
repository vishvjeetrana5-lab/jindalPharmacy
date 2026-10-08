package dataman.accounts.billHandle.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {


    private String date;
    private String fromLocationName;
    private String toLocationName;
    private String fromBilldate;
    private String toBilldate;
    private String module;
    private String approvedBy;
    public Find_Mdl() {
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFromLocationName() {
        return fromLocationName;
    }

    public void setFromLocationName(String fromLocationName) {
        this.fromLocationName = fromLocationName;
    }

    public String getToLocationName() {
        return toLocationName;
    }

    public void setToLocationName(String toLocationName) {
        this.toLocationName = toLocationName;
    }

    public String getFromBilldate() {
        return fromBilldate;
    }

    public void setFromBilldate(String fromBilldate) {
        this.fromBilldate = fromBilldate;
    }

    public String getToBilldate() {
        return toBilldate;
    }

    public void setToBilldate(String toBilldate) {
        this.toBilldate = toBilldate;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

}
