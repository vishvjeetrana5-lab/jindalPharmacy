package aarogya.pharmacy.operationMediMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdOperationDetails_Mdl extends KeyField implements Serializable, IsSerializable {

    private String qty;
    private String sku;
    private String skuDecimalPlaces;

    public GrdOperationDetails_Mdl() {
    }

    public String getSkuDecimalPlaces() {
        return skuDecimalPlaces;
    }

    public void setSkuDecimalPlaces(String skuDecimalPlaces) {
        this.skuDecimalPlaces = skuDecimalPlaces;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

}
