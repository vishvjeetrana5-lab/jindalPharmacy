package dataman.dmToolkit.overHeadLine.shared;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface OverHeadLine_SrvAsync {

    public void getTxtHPostingAC(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<AccountHelp_Mdl>>> result);

    public void getOverHead(String vType, String vDate, AsyncCallback<DmResult<List<OverHeadLine_Mdl>>> result);
}
