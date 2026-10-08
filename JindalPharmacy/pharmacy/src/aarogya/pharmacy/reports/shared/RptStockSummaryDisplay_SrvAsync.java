package aarogya.pharmacy.reports.shared;

import aarogya.pharmacy.stockSummary.shared.DefaultValue_Mdl;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.shared.DmResult;
import java.util.LinkedHashMap;

public interface RptStockSummaryDisplay_SrvAsync {

    public void execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, DefaultValue_Mdl dvm, AsyncCallback<DmResult<String>> result);

}
