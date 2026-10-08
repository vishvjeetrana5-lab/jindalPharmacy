package aarogya.inventory.purchaseReturn.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHBillNo_Mdl extends KeyField implements Serializable, IsSerializable {

    /*
     * SharedKeyfieldCode = pbDocId
     * SharedKeyfieldName = pbRecId
     */
    private String pbV_Date;
    private String pbRefNo;
    private String pbRefDate;

    public TxtHBillNo_Mdl() {
    }

    public String getPbV_Date() {
        return pbV_Date;
    }

    public void setPbV_Date(String pbV_Date) {
        this.pbV_Date = pbV_Date;
    }

    public String getPbRefNo() {
        return pbRefNo;
    }

    public void setPbRefNo(String pbRefNo) {
        this.pbRefNo = pbRefNo;
    }

    public String getPbRefDate() {
        return pbRefDate;
    }

    public void setPbRefDate(String pbRefDate) {
        this.pbRefDate = pbRefDate;
    }

}
