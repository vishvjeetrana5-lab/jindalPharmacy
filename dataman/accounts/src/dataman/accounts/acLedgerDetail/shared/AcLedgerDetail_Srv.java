package dataman.accounts.acLedgerDetail.shared;

import com.google.gwt.user.client.rpc.RemoteService;
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

public interface AcLedgerDetail_Srv extends RemoteService {

    public DmResult<DefaultValue_Mdl> getDefault(AcDisplaySetting_Mdl acDisplaySetting, List<DisplayRoute_Mdl> displayRouteList);

    public DmResultPageing<List<AcLedgerDetail_Mdl>> getAcLedgerDetial(DmHelper helper, boolean blnToEndOfFile, KeyFieldExtended keyField);
}
