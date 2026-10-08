package aarogya.selfToolkit.modifyReason.shared;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface ModifyReason_SrvAsync {

    public void getTxtHReason(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getEditResion(String docId, int sNo, AsyncCallback<DmResult<List<GrdCancelReason_Mdl>>> result);
}
