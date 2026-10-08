package aarogya.dashBoard.billPendingForGRN.shared;


import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface BillPendingForGRN_Srv extends RemoteService {

    public DmResultPageing<List<BillPendingForGRN_Mdl>> getData(String daysCount,DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl searchValue);

    public DmResult<String> getNewDate(int value, String date);

    public DmResult<KeyFieldExtended> getDefaultValue();

    public DmResult<CardDetail_Mdl> getCardDetail(String daysCount);
    
      public DmResult<KeyFieldExtended> exportCSV(String sessionId, String daysUpto );

}
