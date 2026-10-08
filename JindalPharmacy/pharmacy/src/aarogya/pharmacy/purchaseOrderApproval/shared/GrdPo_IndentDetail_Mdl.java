package aarogya.pharmacy.purchaseOrderApproval.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdPo_IndentDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    private String indDocID;
    private String indV_Sno;
    private String indOrdQty;

    public GrdPo_IndentDetail_Mdl() {
    }

    public String getIndDocID() {
        return indDocID;
    }

    public void setIndDocID(String indDocID) {
        this.indDocID = indDocID;
    }

    public String getIndV_Sno() {
        return indV_Sno;
    }

    public void setIndV_Sno(String indV_Sno) {
        this.indV_Sno = indV_Sno;
    }

    public String getIndOrdQty() {
        return indOrdQty;
    }

    public void setIndOrdQty(String indOrdQty) {
        this.indOrdQty = indOrdQty;
    }

}
