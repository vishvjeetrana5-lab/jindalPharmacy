package aarogya.dashBoard.billPendingForGRN.shared;

 
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface BillPendingForGRN_SrvAsync {

    public void getData(String daysCount,DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl searchValue, AsyncCallback<DmResultPageing<List<BillPendingForGRN_Mdl>>> result);

    public void getNewDate(int value, String date, AsyncCallback<DmResult<String>> result);

    public void getDefaultValue(AsyncCallback<DmResult<KeyFieldExtended>> result);

    public void getCardDetail(String daysCount,AsyncCallback<DmResult<CardDetail_Mdl>> result);
    
    public void exportCSV(String sessionId, String daysUpto,  AsyncCallback<DmResult<KeyFieldExtended>> result);

}
