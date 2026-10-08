package aarogya.dashBoard.whCollectionDashBoard.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import java.util.List;

public interface WHCollectionDashBoardCard_SrvAsync {

    public void getData(String daysCount, DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl searchValue, AsyncCallback<DmResultPageing<List<WHCollectionDashBoard_Mdl>>> result);

    public void getDefaultValue(AsyncCallback<DmResult<KeyFieldExtended>> result);

    public void getCardDetail(String daysCount, AsyncCallback<DmResult<CardDetail_Mdl>> result);

    public void exportCSV(String sessionId, String daysUpto, AsyncCallback<DmResult<KeyFieldExtended>> result);

}
