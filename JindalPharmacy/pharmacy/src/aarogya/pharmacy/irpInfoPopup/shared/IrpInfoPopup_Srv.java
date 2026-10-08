package aarogya.pharmacy.irpInfoPopup.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import java.util.List;

public interface IrpInfoPopup_Srv extends RemoteService {

    public DmResultPageing<List<KeyFieldExtended>> getTxtHIsActive(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHIrpType(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<IrpInfoPopup_Mdl>> getTxtHIrpName(String irpTypeCode, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<IrpInfoPopup_Mdl> getMoveRec(String irpCode);

    public DmResult<String> deleteIrpInfo(String code);

    public DmResult<String> saveIrpInfo(IrpInfoPopup_Mdl prevRecord, IrpInfoPopup_Mdl newRecord);

}
