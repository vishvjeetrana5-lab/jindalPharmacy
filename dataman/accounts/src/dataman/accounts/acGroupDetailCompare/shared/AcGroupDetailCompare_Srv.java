package dataman.accounts.acGroupDetailCompare.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.accounts.acDisplaySetting.shared.AcDisplaySetting_Mdl;
import dataman.accounts.acDisplaySetting.shared.DisplayRoute_Mdl;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface AcGroupDetailCompare_Srv extends RemoteService {

    public DmResult<DefaultValue_Mdl> getDefault(AcDisplaySetting_Mdl acDisplaySettingComparison, List<DisplayRoute_Mdl> displayRouteList);

    public DmResultPageing<List<AcGroupDetailCompare_Mdl>> getAcGroupDetial(DmHelper helper, boolean blnToEndOfFile, KeyFieldExtended keyField);
}
