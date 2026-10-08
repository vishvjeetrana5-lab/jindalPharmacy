package dataman.dmToolkit.overHeadLine.shared;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface OverHeadLine_Srv extends RemoteService {

    public DmResultPageing<List<AccountHelp_Mdl>> getTxtHPostingAC(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<List<OverHeadLine_Mdl>> getOverHead(String vType, String vDate);
}
