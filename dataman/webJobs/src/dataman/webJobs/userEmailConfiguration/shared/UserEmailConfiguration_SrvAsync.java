package dataman.webJobs.userEmailConfiguration.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface UserEmailConfiguration_SrvAsync {

    public void getDefaultValues(AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void saveRecord(String userName, GrdUserEmaiEdit_Mdl mrTemp, AsyncCallback<DmResult<GrdUserEmaiEdit_Mdl>> result);

    public void getTxtHUser(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHUsers_Mdl>>> result);

    public void fillGrid(String pltCode, AsyncCallback<DmResult<List<GrdUserEmaiEdit_Mdl>>> result);

}
