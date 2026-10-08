package aarogya.pharmacy.purchaseBillSearch.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface PurchaseBillSearch_SrvAsync {

    public void getBillDetail(String docId, String subCode, String v_Date, String vCat, SearchValue_Mdl searchValue_Mdl, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<GrdBillDetails_Mdl>>> result);

    public void getTxtHBillListSearch(String docId, String subCode, String v_Date, String vCat, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getAutoBillDetail(String pbDocId, String vDate, AsyncCallback<DmResult<List<GrdBillDetails_Mdl>>> result);
}
