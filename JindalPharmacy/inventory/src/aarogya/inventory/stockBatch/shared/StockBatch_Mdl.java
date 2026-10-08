package aarogya.inventory.stockBatch.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StockBatch_Mdl implements Serializable, IsSerializable {

    List<GrdStockDetail_Mdl> listStockDetail = new ArrayList<GrdStockDetail_Mdl>();

    public StockBatch_Mdl() {
    }

    public void setListStockDetail(List<GrdStockDetail_Mdl> listStockDetail) {
        this.listStockDetail = listStockDetail;
    }

    public List<GrdStockDetail_Mdl> getListStockDetail() {
        return listStockDetail;
    }

}
