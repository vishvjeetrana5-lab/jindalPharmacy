package aarogya.pharmacy.irpInfoPopup.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import java.util.List;

public interface IrpInfoPopup_SrvAsync {
    
    public void getTxtHIsActive(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHIrpType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHIrpName(String irpTypeCode, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<IrpInfoPopup_Mdl>>> result);   

    public void deleteIrpInfo(String code, AsyncCallback<DmResult<String>> result);
    
    public void getMoveRec(String irpCode, AsyncCallback<DmResult<IrpInfoPopup_Mdl>> result);

    public void saveIrpInfo(IrpInfoPopup_Mdl prevRecord,IrpInfoPopup_Mdl newRecord, AsyncCallback<DmResult<String>> result);

}
