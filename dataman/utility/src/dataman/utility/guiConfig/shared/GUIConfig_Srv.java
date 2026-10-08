package dataman.utility.guiConfig.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface GUIConfig_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResult<GUIConfig_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(GUIConfig_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);

    public String getTempKeyCode();

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDefault(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHSite(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHTable(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getDataType(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHTemplate(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getYesNo(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getCopy(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHSaveInField(DmHelper helper, boolean blnToEndOfFile, String tableName);

    public DmResultPageing<List<KeyFieldExtended>> getViewFieldCode(DmHelper helper, boolean blnToEndOfFile, String query);

    public DmResult<KeyFieldExtended> exportJson(String sessionId, GUIConfig_Mdl mrTemp);

    public DmResult<GUIConfig_Mdl> importJson(String filePath, String fileName);
}
