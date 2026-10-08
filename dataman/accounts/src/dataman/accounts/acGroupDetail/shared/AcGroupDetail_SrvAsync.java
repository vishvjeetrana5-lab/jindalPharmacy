package dataman.accounts.acGroupDetail.shared;

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

public interface AcGroupDetail_SrvAsync {

    public void getDefault(AcDisplaySetting_Mdl acDisplaySetting, List<DisplayRoute_Mdl> displayRouteList, AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void getAcGroupDetial(DmHelper helper, boolean blnToEndOfFile, KeyFieldExtended keyField, AsyncCallback<DmResultPageing<List<AcGroupDetail_Mdl>>> result);
}
