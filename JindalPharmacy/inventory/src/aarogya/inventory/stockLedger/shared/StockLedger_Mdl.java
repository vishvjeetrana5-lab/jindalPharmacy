package aarogya.inventory.stockLedger.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StockLedger_Mdl implements Serializable, IsSerializable {

    public StockLedger_Mdl() {
    }
    List<GrdStockDetail_Mdl> listStockDetail = new ArrayList<GrdStockDetail_Mdl>();

    public List<GrdStockDetail_Mdl> getListStockDetail() {
        return listStockDetail;
    }

}
