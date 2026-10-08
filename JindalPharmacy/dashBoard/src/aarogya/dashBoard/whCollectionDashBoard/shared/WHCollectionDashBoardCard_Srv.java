package aarogya.dashBoard.whCollectionDashBoard.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import java.util.List;

public interface WHCollectionDashBoardCard_Srv extends RemoteService {

    public DmResultPageing<List<WHCollectionDashBoard_Mdl>> getData(String supplierCode, DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl searchValue);

    public DmResult<KeyFieldExtended> getDefaultValue();

    public DmResult<CardDetail_Mdl> getCardDetail(String daysCount);

    public DmResult<KeyFieldExtended> exportCSV(String sessionId, String daysUpto);
}
