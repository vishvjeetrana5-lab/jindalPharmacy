package aarogya.selfToolkit.stStatic.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class ConvertedQuantity implements Serializable, IsSerializable {

    String displayQty, secondryPartQty, skuPartQty;

    public String getDisplayQty() {
        return displayQty;
    }

    public void setDisplayQty(String displayQty) {
        this.displayQty = displayQty;
    }

    public String getSecondryPartQty() {
        return secondryPartQty;
    }

    public void setSecondryPartQty(String secondryPartQty) {
        this.secondryPartQty = secondryPartQty;
    }

    public String getSkuPartQty() {
        return skuPartQty;
    }

    public void setSkuPartQty(String skuPartQty) {
        this.skuPartQty = skuPartQty;
    }

}
