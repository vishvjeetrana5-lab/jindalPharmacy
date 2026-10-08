package dataman.accounts.reports.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.accounts.balanceSheet.shared.BalanceSheetReport_Mdl;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.shared.DmResult;
import java.util.LinkedHashMap;

public interface RptBalanceSheet_SrvAsync {

    public void execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, BalanceSheetReport_Mdl balanceReport_Mdl, AsyncCallback<DmResult<String>> result);

 

}
