package dataman.accounts.enviroSettings.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHStockAdjustmentVType extends KeyField implements Serializable, IsSerializable {

    private String decription;
    private String category;

    public TxtHStockAdjustmentVType() {
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
