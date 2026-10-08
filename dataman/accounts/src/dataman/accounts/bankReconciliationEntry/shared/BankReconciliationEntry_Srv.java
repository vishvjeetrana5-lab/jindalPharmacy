package dataman.accounts.bankReconciliationEntry.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface BankReconciliationEntry_Srv extends RemoteService {

    public DmResult<BankReconciliationEntry_Mdl> saveRecord(BankReconciliationEntry_Mdl mrTemp, Setting_Mdl setting_Mdl);

    public DmResult<BankReconciliationEntry_Mdl> getFillRecords(Setting_Mdl setting_Mdl);

    public DmResultPageing<List<TxtHBank_Mdl>> getTxtHBankName(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHStatus(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHHideEntry(DmHelper pageing, boolean blnToEndOfFile);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSiteName(DmHelper helper, boolean blnToEndOfFile);
    
    public DmResult<Setting_Mdl> getDefaultSetting();

}
