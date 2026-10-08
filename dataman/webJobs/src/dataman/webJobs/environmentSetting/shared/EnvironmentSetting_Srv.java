package dataman.webJobs.environmentSetting.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface EnvironmentSetting_Srv extends RemoteService {

    public DmResult<EnvironmentSetting_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(EnvironmentSetting_Mdl mrTemp);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHSite(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHSmtpSsl(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<EnvironmentSetting_Mdl> getSiteCode();

}
