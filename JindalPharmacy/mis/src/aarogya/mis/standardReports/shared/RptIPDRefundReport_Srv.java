package aarogya.mis.standardReports.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import java.util.LinkedHashMap;

public interface RptIPDRefundReport_Srv extends RemoteService {

    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, String widgetName, DmReportProperty_Mdl reportProperty);

}
