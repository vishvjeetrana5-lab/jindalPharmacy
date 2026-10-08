package dataman.accounts.enviroSettings.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface EnviroSettings_SrvAsync {

    public void getMoveRec(String strKey, AsyncCallback<DmResult<EnviroSettings_Mdl>> result);

    public void saveRecord(EnviroSettings_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void getTxtHSite(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHRoundOff(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHAccountName(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHStockAdjustmentVType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHStockAdjustmentVType>>> result);

    public void getDefaultValue(AsyncCallback<DmResult<KeyFieldExtended>> result);
    
    public void getTxtHTDSOn(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);
    
    public void getTxtHVouchersExcludesInTurnover(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

}
