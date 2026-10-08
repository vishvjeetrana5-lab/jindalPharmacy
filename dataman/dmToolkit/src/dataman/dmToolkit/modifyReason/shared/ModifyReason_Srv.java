package dataman.dmToolkit.modifyReason.shared;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface ModifyReason_Srv extends RemoteService {

    public DmResultPageing<List<KeyFieldExtended>> getTxtHReason(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<List<GrdCancelReason_Mdl>> getEditResion(String primaryId, String tableName, long secondaryId);

    public DmResult<List<GrdCancelReason_Mdl>> getReasonDetails(String keyId_1, String keyId_2, String tableName);
}
