package dataman.utility.menuCodification.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface MenuCodification_Srv extends RemoteService {

    public DmResultPageing<List<TxtHMenuName_Mdl>> getTxtHMenuName(String moduleName, DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<String> saveRecord(GrdDescription_Mdl mrTemp);

    public DmResult<String> deleteRecord(GrdDescription_Mdl strKey);

    public DmResult<List<GrdModuleMenu_Mdl>> getModuleList(String moduleType);

    public DmResult<List<GrdDescription_Mdl>> getMenuList(String sorting, String moduleName);

    public DmResult<KeyFieldExtended> exportCSV(String sessionId);
}
