package dataman.dmToolkit.dmGUI.shared;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface DmGUI_SrvAsync {

    public void getGUI(String guiConfigCode, String guiTemplateCode, AsyncCallback<DmResult<DmGUI_Mdl>> result);

    public void getHelp(byte[] helpSQLQuery, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getCopyGUIDetails(String guiCodeFrom, String docIdGUIFrom, AsyncCallback<DmResult<DmGUI_Mdl>> result);
}
