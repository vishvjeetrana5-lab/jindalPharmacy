package dataman.accounts.acDisplaySetting.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface AcDisplaySetting_Srv extends RemoteService {

    public DmResultPageing<List<KeyFieldExtended>> getYesNo(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getSite(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<AcntLedger_Mdl>> getLedgerAcount(DmHelper helper, boolean blnToEndOfFile, String siteList);

    public DmResult<AcDisplaySetting_Mdl> fillData();
    
    public DmResult<AcDisplaySetting_Mdl> getVoucherPrefix(AcDisplaySetting_Mdl acDisplaySettingMdl);
}
