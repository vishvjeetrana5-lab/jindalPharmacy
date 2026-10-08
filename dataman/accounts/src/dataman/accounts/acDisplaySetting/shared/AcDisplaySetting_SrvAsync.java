package dataman.accounts.acDisplaySetting.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface AcDisplaySetting_SrvAsync {

    public void getYesNo(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getSite(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getLedgerAcount(DmHelper helper, boolean blnToEndOfFile, String siteList, AsyncCallback<DmResultPageing<List<AcntLedger_Mdl>>> result);

    public void fillData(AsyncCallback<DmResult<AcDisplaySetting_Mdl>> result);
    
    public void getVoucherPrefix(AcDisplaySetting_Mdl acDisplaySettingMdl,AsyncCallback<DmResult<AcDisplaySetting_Mdl>> result );
}
