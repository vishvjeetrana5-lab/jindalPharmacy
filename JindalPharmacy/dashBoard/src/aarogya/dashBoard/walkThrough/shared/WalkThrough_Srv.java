package aarogya.dashBoard.walkThrough.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface WalkThrough_Srv extends RemoteService {

    public DmResult<CardDetail_Mdl> getCardDetail();

    public DmResultPageing<List<GrdWalkThrough_Mdl>> getFillRecords(SearchValue_Mdl searchValueMdl, DmHelper helper, boolean blnToEndOfFile);

}
