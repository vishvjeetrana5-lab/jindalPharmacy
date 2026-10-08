package aarogya.inventory.grnSearch.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface GrnSearch_SrvAsync {

    public void getItemDetail(String v_Type, String docId, String subCode, String v_Date,Boolean singlePOFlowInPurchase, SearchValue_Mdl searchValue_Mdl, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<GrdGrnDetails_Mdl>>> result);

}
