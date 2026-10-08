package dataman.accounts.acGroupDetailCompare.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.accounts.acDisplaySetting.shared.AcDisplaySetting_Mdl;
import dataman.accounts.acDisplaySetting.shared.DisplayRoute_Mdl;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface AcGroupDetailCompare_SrvAsync {

    public void getDefault(AcDisplaySetting_Mdl acDisplaySettingComparison, List<DisplayRoute_Mdl> displayRouteList, AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void getAcGroupDetial(DmHelper helper, boolean blnToEndOfFile, KeyFieldExtended keyField, AsyncCallback<DmResultPageing<List<AcGroupDetailCompare_Mdl>>> result);
}
