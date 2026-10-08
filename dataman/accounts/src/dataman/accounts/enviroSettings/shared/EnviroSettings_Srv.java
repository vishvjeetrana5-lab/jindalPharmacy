package dataman.accounts.enviroSettings.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface EnviroSettings_Srv extends RemoteService {

    public DmResult<EnviroSettings_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(EnviroSettings_Mdl mrTemp);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHSite(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHRoundOff(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHAccountName(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHStockAdjustmentVType>> getTxtHStockAdjustmentVType(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<KeyFieldExtended> getDefaultValue();
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHTDSOn(DmHelper helper, boolean blnToEndOfFile);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHVouchersExcludesInTurnover(DmHelper helper, boolean blnToEndOfFile);

}
