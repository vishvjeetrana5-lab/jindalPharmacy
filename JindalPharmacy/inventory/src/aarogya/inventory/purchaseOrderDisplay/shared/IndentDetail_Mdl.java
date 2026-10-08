package aarogya.inventory.purchaseOrderDisplay.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class IndentDetail_Mdl implements Serializable, IsSerializable {
    private String indentDocID;
    private String indentV_Sno;
    private String pendingQty;

    public String getIndentDocID() {
        return indentDocID;
    }

    public void setIndentDocID(String indentDocID) {
        this.indentDocID = indentDocID;
    }

    public String getIndentV_Sno() {
        return indentV_Sno;
    }

    public void setIndentV_Sno(String indentV_Sno) {
        this.indentV_Sno = indentV_Sno;
    }

    public String getPendingQty() {
        return pendingQty;
    }

    public void setPendingQty(String pendingQty) {
        this.pendingQty = pendingQty;
    }
    
    
    
}
