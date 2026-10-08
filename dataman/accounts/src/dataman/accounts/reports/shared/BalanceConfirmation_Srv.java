package dataman.accounts.reports.shared;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.LinkedHashMap;
import java.util.List;

public interface BalanceConfirmation_Srv extends RemoteService {

    public DmResultPageing<List<KeyFieldExtended>> getTxtHAccountGroup(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHSiteName(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHIndex(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHContraAccount(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHReportFor(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHIncludeOpening(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHLedgerGroup(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHHavingBalance(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<BalanceConfirmation_Model.TxtHAccountName>> getTxtHAccountName(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<BalanceConfirmation_Model.TxtHVoucherType>> getTxtHVoucherType(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<BalanceConfirmation_Model.TxtHHavingAccountName>> getTxtHHavingAccountName(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty);
    
    public DmResult<KeyFieldExtended> getDefaultValue();
}
