package aarogya.selfToolkit.modifyReason.shared;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface ModifyReason_Srv extends RemoteService {

    public DmResultPageing<List<KeyFieldExtended>> getTxtHReason(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<List<GrdCancelReason_Mdl>> getEditResion(String docId, int sNo);
}
