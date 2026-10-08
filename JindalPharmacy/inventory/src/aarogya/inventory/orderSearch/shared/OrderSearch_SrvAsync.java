package aarogya.inventory.orderSearch.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface OrderSearch_SrvAsync {

    public void getOrderDetail(String v_Type, String docId, String subCode, String v_Date, String vCat, SearchValue_Mdl searchValue_Mdl, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<GrdOrdertDetails_Mdl>>> result);

}
