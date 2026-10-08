package aarogya.pharmacy.stockLedger.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StockLedger_Mdl implements Serializable, IsSerializable {

    List<GrdStockDetail_Mdl> listStockDetail = new ArrayList<GrdStockDetail_Mdl>();

    public StockLedger_Mdl() {
    }

    public List<GrdStockDetail_Mdl> getListStockDetail() {
        return listStockDetail;
    }

}
