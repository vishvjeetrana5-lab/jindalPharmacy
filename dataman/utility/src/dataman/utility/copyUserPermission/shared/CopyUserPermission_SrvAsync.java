package dataman.utility.copyUserPermission.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface CopyUserPermission_SrvAsync {

    public void saveRecord(List<GrdCopyPermissionFromTo_Mdl> mrTemp, AsyncCallback<DmResult<String>> result);

    public void getTxtHCompany(DmHelper pageing, boolean blnToEndOfFile, String compCode, AsyncCallback<DmResultPageing<List<TxtHCompany_Mdl>>> result);

    public void fillGrid(String compCode, AsyncCallback<DmResult<List<GrdCopyPermissionFromTo_Mdl>>> result);

    public void getDefaultValues(AsyncCallback<DmResult<DefaultValue_Mdl>> result);

}
