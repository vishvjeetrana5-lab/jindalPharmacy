package aarogya.pharmacy.purchaseReturn.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHProformaBillNo_Mdl extends KeyField implements Serializable, IsSerializable {

    /*
     * SharedKeyfieldCode = pfprDocId
     * SharedKeyfieldName = pfprRecId
     */
    private String remark, gstReasonCode, gstReasonName;
    private String pfprV_Date;

    public TxtHProformaBillNo_Mdl() {
    }

    public String getPfprV_Date() {
        return pfprV_Date;
    }

    public void setPfprV_Date(String pfprV_Date) {
        this.pfprV_Date = pfprV_Date;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getGstReasonCode() {
        return gstReasonCode;
    }

    public void setGstReasonCode(String gstReasonCode) {
        this.gstReasonCode = gstReasonCode;
    }

    public String getGstReasonName() {
        return gstReasonName;
    }

    public void setGstReasonName(String gstReasonName) {
        this.gstReasonName = gstReasonName;
    }

}
