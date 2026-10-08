package aarogya.dashBoard.walkThrough.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface WalkThrough_SrvAsync {

    public void getCardDetail(AsyncCallback<DmResult<CardDetail_Mdl>> result);

    public void getFillRecords(SearchValue_Mdl searchValueMdl, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<GrdWalkThrough_Mdl>>> result);

}
