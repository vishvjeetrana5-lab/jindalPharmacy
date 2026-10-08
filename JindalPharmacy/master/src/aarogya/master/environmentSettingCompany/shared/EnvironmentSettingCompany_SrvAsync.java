package aarogya.master.environmentSettingCompany.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface EnvironmentSettingCompany_SrvAsync {

    public void getMoveRec(String strKey, AsyncCallback<DmResult<EnvironmentSettingCompany_Mdl>> result);

    public void saveRecord(EnvironmentSettingCompany_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void getTxtHACGroup(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHYesNo(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void exportJSON(String sessionId, AsyncCallback<DmResult<KeyFieldExtended>> result);

    public void getTxtHRateList(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

}
