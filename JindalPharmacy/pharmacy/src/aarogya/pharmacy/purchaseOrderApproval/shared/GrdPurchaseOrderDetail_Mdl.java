package aarogya.pharmacy.purchaseOrderApproval.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdPurchaseOrderDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    
    private String indentV_Sno;
    private String indentName;
    private String approvedQty;
    private String orderInPurchaseOrder;
    
   

    public GrdPurchaseOrderDetail_Mdl() {
    }

    public String getApprovedQty() {
        return approvedQty;
    }

    public void setApprovedQty(String approvedQty) {
        this.approvedQty = approvedQty;
    }

    public String getIndentName() {
        return indentName;
    }

    public void setIndentName(String indentName) {
        this.indentName = indentName;
    }

    public String getOrderInPurchaseOrder() {
        return orderInPurchaseOrder;
    }

    public void setOrderInPurchaseOrder(String orderInPurchaseOrder) {
        this.orderInPurchaseOrder = orderInPurchaseOrder;
    }

    public String getIndentV_Sno() {
        return indentV_Sno;
    }

    public void setIndentV_Sno(String indentV_Sno) {
        this.indentV_Sno = indentV_Sno;
    }

    

  
}
