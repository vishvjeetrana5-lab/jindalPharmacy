package aarogya.pharmacy.orderSearch.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface OrderSearch_SrvAsync {

    public void getOrderDetail(String docId, String subCode, String v_Date, String vCat, SearchValue_Mdl searchValue_Mdl, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<GrdOrdertDetails_Mdl>>> result);

    public void getTxtHOrderListSearch(String docId, String subCode, String v_Date, String vCat,DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>result);
    
    public void getAutoOrderDetail(String poDocIds, String vDate, AsyncCallback<DmResult<List<GrdOrdertDetails_Mdl>>>result);
}
