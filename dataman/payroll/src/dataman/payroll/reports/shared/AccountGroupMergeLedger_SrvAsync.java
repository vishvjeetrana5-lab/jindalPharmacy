package dataman.payroll.reports.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.LinkedHashMap;
import java.util.List;

public interface AccountGroupMergeLedger_SrvAsync {

    public void getTxtHSiteName(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHContraACNeeded(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHAccountGroup(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<AccountGroupMergeLedger_Model.TxtHAccountGroup>>> result);

    public void getTxtHVoucherTyep(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<AccountGroupMergeLedger_Model.TxtHVoucherType>>> result);

    public void execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, AsyncCallback<DmResult<String>> result);
}
