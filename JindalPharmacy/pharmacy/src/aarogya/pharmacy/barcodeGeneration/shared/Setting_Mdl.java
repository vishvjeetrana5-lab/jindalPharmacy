package aarogya.pharmacy.barcodeGeneration.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class Setting_Mdl implements Serializable, IsSerializable {

    private String fromDate;
    private String toDate;
    private String vPrefix;
    private String department;
    private String barCodeDocid;
    private String barCodeRecid;

    public Setting_Mdl() {
    }

    public String getBarCodeDocid() {
        return barCodeDocid;
    }

    public void setBarCodeDocid(String barCodeDocid) {
        this.barCodeDocid = barCodeDocid;
    }

    public String getBarCodeRecid() {
        return barCodeRecid;
    }

    public void setBarCodeRecid(String barCodeRecid) {
        this.barCodeRecid = barCodeRecid;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getvPrefix() {
        return vPrefix;
    }

    public void setvPrefix(String vPrefix) {
        this.vPrefix = vPrefix;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

}
