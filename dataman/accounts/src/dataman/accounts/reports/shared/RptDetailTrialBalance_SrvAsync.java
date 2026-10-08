package dataman.accounts.reports.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.accounts.detailTrialBalance.shared.DetailTrialBalanceReport_Mdl;

import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import java.util.LinkedHashMap;
import java.util.List;

public interface RptDetailTrialBalance_SrvAsync {

    public void execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, DetailTrialBalanceReport_Mdl balanceReport_Mdl, AsyncCallback<DmResult<String>> result);

    public void getYesNo(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getSite(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

//    public void getLedgerAcount(DmHelper helper, boolean blnToEndOfFile, String siteList, AsyncCallback<DmResultPageing<List<RptTrialBalance_Mdl>>> result);

}
