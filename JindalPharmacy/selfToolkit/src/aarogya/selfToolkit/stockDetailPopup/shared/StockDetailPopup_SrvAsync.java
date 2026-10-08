package aarogya.selfToolkit.stockDetailPopup.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;

public interface StockDetailPopup_SrvAsync {

    public void getShowStock(String itemCode, ItemProperty_Mdl propertyPopup_Mdl, AsyncCallback<DmResult<List<StockDetailGrdEdit_Mdl>>> result);

}
