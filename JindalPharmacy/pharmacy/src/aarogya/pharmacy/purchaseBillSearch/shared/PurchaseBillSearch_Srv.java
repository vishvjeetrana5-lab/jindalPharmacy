package aarogya.pharmacy.purchaseBillSearch.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface PurchaseBillSearch_Srv extends RemoteService {

    public DmResultPageing<List<GrdBillDetails_Mdl>> getBillDetail(String docId, String subCode, String v_Date, String vCat, SearchValue_Mdl searchValue_Mdl, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHBillListSearch(String docId, String subCode, String v_Date, String vCat, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<List<GrdBillDetails_Mdl>> getAutoBillDetail(String pbDocId, String vDate);
}
