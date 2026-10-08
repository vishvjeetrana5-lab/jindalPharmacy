package dataman.accounts.bankReconciliationEntry.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface BankReconciliationEntry_SrvAsync {

    public void saveRecord(BankReconciliationEntry_Mdl mrTemp, Setting_Mdl setting_Mdl, AsyncCallback<DmResult<BankReconciliationEntry_Mdl>> result);

    public void getFillRecords(Setting_Mdl setting_Mdl, AsyncCallback<DmResult<BankReconciliationEntry_Mdl>> result);

    public void getTxtHBankName(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHBank_Mdl>>> result);

    public void getTxtHStatus(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHHideEntry(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);
    
    public void getTxtHSiteName(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);
    
    public void getDefaultSetting(AsyncCallback<DmResult<Setting_Mdl>> result);

}
