package aarogya.pharmacy.orderSearch.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface OrderSearch_Srv extends RemoteService {

    public DmResultPageing<List<GrdOrdertDetails_Mdl>> getOrderDetail(String docId, String subCode, String v_Date, String vCat, SearchValue_Mdl searchValue_Mdl, DmHelper helper, boolean blnToEndOfFile);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHOrderListSearch(String docId, String subCode, String v_Date, String vCat,DmHelper helper, boolean blnToEndOfFile);

    public DmResult<List<GrdOrdertDetails_Mdl>> getAutoOrderDetail(String poDocIds, String vDate);
}
