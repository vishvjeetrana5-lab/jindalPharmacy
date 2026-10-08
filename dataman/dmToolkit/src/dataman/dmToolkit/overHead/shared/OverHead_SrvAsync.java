package dataman.dmToolkit.overHead.shared;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface OverHead_SrvAsync {

    public void getTxtHPostingAC(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<AccountHelp_Mdl>>> result);

    public void getOverHead(String vType, String vDate, String overHeadTableName, String overHeadTableNameMA, AsyncCallback<DmResult<List<OverHead_Mdl>>> result);
}
