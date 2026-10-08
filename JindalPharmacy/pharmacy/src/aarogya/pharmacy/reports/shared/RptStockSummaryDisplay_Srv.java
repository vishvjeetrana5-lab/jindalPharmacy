package aarogya.pharmacy.reports.shared;

import aarogya.pharmacy.stockSummary.shared.DefaultValue_Mdl;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.shared.DmResult;
import java.util.LinkedHashMap;

public interface RptStockSummaryDisplay_Srv extends RemoteService {

    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, DefaultValue_Mdl dvm);

}
