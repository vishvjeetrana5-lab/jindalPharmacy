package aarogya.pharmacy.reports.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHSaleBillNo_Mdl extends KeyField implements Serializable, IsSerializable {

    public TxtHSaleBillNo_Mdl() {
    }

    //sharedKeyFieldCode=docId
    //sharedKeyFieldName=recId
    private String recIdPrefix;
    private String vNo;

    public String getRecIdPrefix() {
        return recIdPrefix;
    }

    public void setRecIdPrefix(String recIdPrefix) {
        this.recIdPrefix = recIdPrefix;
    }

    public String getVNo() {
        return vNo;
    }

    public void setVNo(String vNo) {
        this.vNo = vNo;
    }

}
