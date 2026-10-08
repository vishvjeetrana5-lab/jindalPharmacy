package aarogya.selfToolkit.stockDetailPopup.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;

public interface StockDetailPopup_Srv extends RemoteService {

    public DmResult<List<StockDetailGrdEdit_Mdl>> getShowStock(String itemCode, ItemProperty_Mdl propertyPopup_Mdl);

}
