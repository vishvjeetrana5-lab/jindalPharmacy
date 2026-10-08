package dataman.utility.guiConfig.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface GUIConfig_SrvAsync {

    public void getNavigator(AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<GUIConfig_Mdl>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void saveRecord(GUIConfig_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void getTempKeyCode(AsyncCallback<String> result);

    public void getFind(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getTxtHDefault(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHSite(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHTemplate(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHTable(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getDataType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getYesNo(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getCopy(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHSaveInField(DmHelper helper, boolean blnToEndOfFile, String tableName, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getViewFieldCode(DmHelper helper, boolean blnToEndOfFile, String query, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void exportJson(String sessionId, GUIConfig_Mdl mrTemp, AsyncCallback<DmResult<KeyFieldExtended>> result);

    public void importJson(String filePath, String fileName, AsyncCallback<DmResult<GUIConfig_Mdl>> result);

}
