package dataman.webJobs.userEmailConfiguration.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface UserEmailConfiguration_Srv extends RemoteService {

    public DmResult<DefaultValue_Mdl> getDefaultValues();

    public DmResult<GrdUserEmaiEdit_Mdl> saveRecord(String userName, GrdUserEmaiEdit_Mdl mrTemp);

    public DmResultPageing<List<TxtHUsers_Mdl>> getTxtHUser(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<List<GrdUserEmaiEdit_Mdl>> fillGrid(String pltCode);

}
