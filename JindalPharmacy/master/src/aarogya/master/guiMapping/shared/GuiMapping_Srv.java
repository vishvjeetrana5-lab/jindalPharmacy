package aarogya.master.guiMapping.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface GuiMapping_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResult<String> saveRecord(GuiMapping_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<GuiMapping_Mdl> getMoveRec(String strKey);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHGuiTemplate(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHEntry(DmHelper helper, boolean blnToEndOfFile);
}
