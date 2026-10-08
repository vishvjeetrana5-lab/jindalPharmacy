package aarogya.inventory.stockSummary.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StockSummary_Mdl extends KeyField implements Serializable, IsSerializable {

    List<GrdStockSummary_Mdl> listStockDetail = new ArrayList<GrdStockSummary_Mdl>();

    public StockSummary_Mdl() {
    }

    public List<GrdStockSummary_Mdl> getListStockDetail() {
        return listStockDetail;
    }
}
