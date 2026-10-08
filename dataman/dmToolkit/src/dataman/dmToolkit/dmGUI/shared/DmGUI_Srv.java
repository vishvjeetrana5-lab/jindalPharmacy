package dataman.dmToolkit.dmGUI.shared;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface DmGUI_Srv extends RemoteService {

    public DmResult<DmGUI_Mdl> getGUI(String guiConfigCode, String guiTemplateCode);

    public DmResultPageing<List<KeyFieldExtended>> getHelp(byte[] helpSQLQuery, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<DmGUI_Mdl> getCopyGUIDetails(String guiCodeFrom, String docIdGUIFrom);
}
