package dataman.accounts.reports.shared;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.LinkedHashMap;
import java.util.List;

public interface RptLedgerExcel_SrvAsync {

    public void getTxtHAccountGroup(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHSiteName(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHIndex(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHContraAccount(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHReportFor(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHIncludeOpening(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHLedgerGroup(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHHavingBalance(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHAccountName(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<RptLedgerExcel_Model.TxtHAccountName>>> result);

    public void getTxtHVoucherType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<RptLedgerExcel_Model.TxtHVoucherType>>> result);

    public void getTxtHHavingAccountName(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<RptLedgerExcel_Model.TxtHHavingAccountName>>> result);

    public void execute(String sessionId, LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName, AsyncCallback<DmResult<KeyFieldExtended>> result);
    
    public void getDefaultValue(AsyncCallback<DmResult<KeyFieldExtended>> result);
}
