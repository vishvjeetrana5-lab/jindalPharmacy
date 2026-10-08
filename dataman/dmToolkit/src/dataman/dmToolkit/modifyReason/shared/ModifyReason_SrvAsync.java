package dataman.dmToolkit.modifyReason.shared;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface ModifyReason_SrvAsync {

    public void getTxtHReason(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getEditResion(String primaryId, String tableName, long secondaryId, AsyncCallback<DmResult<List<GrdCancelReason_Mdl>>> result);

    public void getReasonDetails(String keyId_1, String keyId_2, String tableName, AsyncCallback<DmResult<List<GrdCancelReason_Mdl>>> result);
}
