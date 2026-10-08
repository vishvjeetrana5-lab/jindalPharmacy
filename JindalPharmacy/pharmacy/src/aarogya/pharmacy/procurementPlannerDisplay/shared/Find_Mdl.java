package aarogya.pharmacy.procurementPlannerDisplay.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
public class Find_Mdl extends KeyField implements Serializable, IsSerializable {
    private String voucherDate;
    private String recId;
    private String prparedBy;

    public Find_Mdl() {
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getRecId() {
        return recId;
    }

    public void setRecId(String recId) {
        this.recId = recId;
    }

    public String getPrparedBy() {
        return prparedBy;
    }

    public void setPrparedBy(String prparedBy) {
        this.prparedBy = prparedBy;
    }

}
