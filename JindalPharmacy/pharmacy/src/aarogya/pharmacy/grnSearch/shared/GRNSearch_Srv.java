package aarogya.pharmacy.grnSearch.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface GRNSearch_Srv extends RemoteService {

    public DmResultPageing<List<GrdGrnDetails_Mdl>> getItemDetail(String docId, String subCode, String v_Date,Boolean singlePOFlowInPurchase, SearchValue_Mdl searchValue_Mdl, DmHelper helper, boolean blnToEndOfFile);

}
