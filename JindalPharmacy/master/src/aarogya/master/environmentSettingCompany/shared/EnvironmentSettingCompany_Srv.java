package aarogya.master.environmentSettingCompany.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface EnvironmentSettingCompany_Srv extends RemoteService {

    public DmResult<EnvironmentSettingCompany_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(EnvironmentSettingCompany_Mdl mrTemp);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHACGroup(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHYesNo(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<KeyFieldExtended> exportJSON(String sessionId);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHRateList(DmHelper helper, boolean blnToEndOfFile);

}
