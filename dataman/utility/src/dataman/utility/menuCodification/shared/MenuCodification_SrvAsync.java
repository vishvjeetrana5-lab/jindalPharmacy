package dataman.utility.menuCodification.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface MenuCodification_SrvAsync {

    public void getTxtHMenuName(String moduleName, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHMenuName_Mdl>>> result);

    public void saveRecord(GrdDescription_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void deleteRecord(GrdDescription_Mdl strKey, AsyncCallback<DmResult<String>> result);

    public void getModuleList(String moduleType, AsyncCallback<DmResult<List<GrdModuleMenu_Mdl>>> result);

    public void getMenuList(String sorting, String moduleName, AsyncCallback<DmResult<List<GrdDescription_Mdl>>> result);

    public void exportCSV(String sessionId, AsyncCallback<DmResult<KeyFieldExtended>> result);

}
