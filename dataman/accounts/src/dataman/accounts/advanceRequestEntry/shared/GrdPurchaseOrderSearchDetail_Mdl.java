package dataman.accounts.advanceRequestEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class GrdPurchaseOrderSearchDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    String poDate, supplierName, poAmt, preparedBy,v_Sno,finalAmt;

    public GrdPurchaseOrderSearchDetail_Mdl() {

    }

    public String getPoDate() {
        return poDate;
    }

    public void setPoDate(String poDate) {
        this.poDate = poDate;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getPoAmt() {
        return poAmt;
    }

    public void setPoAmt(String poAmt) {
        this.poAmt = poAmt;
    }

    public String getPreparedBy() {
        return preparedBy;
    }

    public void setPreparedBy(String preparedBy) {
        this.preparedBy = preparedBy;
    }

    public String getV_Sno() {
        return v_Sno;
    }

    public void setV_Sno(String v_Sno) {
        this.v_Sno = v_Sno;
    }

    public String getFinalAmt() {
        return finalAmt;
    }

    public void setFinalAmt(String finalAmt) {
        this.finalAmt = finalAmt;
    }

}
