package aarogya.inventory.orderSearch.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface OrderSearch_Srv extends RemoteService {

    public DmResultPageing<List<GrdOrdertDetails_Mdl>> getOrderDetail(String v_Type, String docId, String subCode, String v_Date, String vCat, SearchValue_Mdl searchValue_Mdl, DmHelper helper, boolean blnToEndOfFile);

}
