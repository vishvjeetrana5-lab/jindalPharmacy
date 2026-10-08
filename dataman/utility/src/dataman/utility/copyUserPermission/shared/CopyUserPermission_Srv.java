package dataman.utility.copyUserPermission.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface CopyUserPermission_Srv extends RemoteService {

    public DmResult<String> saveRecord(List<GrdCopyPermissionFromTo_Mdl>  mrTemp);

    public DmResultPageing<List<TxtHCompany_Mdl>> getTxtHCompany(DmHelper pageing, boolean blnToEndOfFile, String compCode);

    public DmResult<List<GrdCopyPermissionFromTo_Mdl>> fillGrid(String compCode);

    public DmResult<DefaultValue_Mdl> getDefaultValues();

}
