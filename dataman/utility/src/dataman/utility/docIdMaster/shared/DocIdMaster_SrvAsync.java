package dataman.utility.docIdMaster.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface DocIdMaster_SrvAsync {

    public void getNavigator(AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<DocIdMaster_Mdl>> result);

    public void getTxtHSite(DmHelper pageing, boolean blnToEndOfFile, String strCode, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getFind(String vPrefix, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void fillGrid(String siteCode, String vpRefix, AsyncCallback<DmResult<List<GrdDocIdMaster_Mdl>>> result);
}
