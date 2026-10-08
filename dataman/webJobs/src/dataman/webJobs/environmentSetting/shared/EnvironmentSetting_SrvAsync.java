package dataman.webJobs.environmentSetting.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface EnvironmentSetting_SrvAsync {

    public void getMoveRec(String strKey, AsyncCallback<DmResult<EnvironmentSetting_Mdl>> result);

    public void saveRecord(EnvironmentSetting_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void getTxtHSite(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHSmtpSsl(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getSiteCode(AsyncCallback<DmResult<EnvironmentSetting_Mdl>> result);

}
