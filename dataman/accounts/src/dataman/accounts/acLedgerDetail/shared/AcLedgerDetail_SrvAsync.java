package dataman.accounts.acLedgerDetail.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.accounts.acDisplaySetting.shared.AcDisplaySetting_Mdl;
import dataman.accounts.acDisplaySetting.shared.DisplayRoute_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface AcLedgerDetail_SrvAsync {

    public void getDefault(AcDisplaySetting_Mdl acDisplaySetting, List<DisplayRoute_Mdl> displayRouteList, AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void getAcLedgerDetial(DmHelper helper, boolean blnToEndOfFile, KeyFieldExtended keyField, AsyncCallback<DmResultPageing<List<AcLedgerDetail_Mdl>>> result);
}
