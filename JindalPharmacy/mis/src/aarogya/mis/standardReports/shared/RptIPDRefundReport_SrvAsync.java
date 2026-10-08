package aarogya.mis.standardReports.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import java.util.LinkedHashMap;

public interface RptIPDRefundReport_SrvAsync {

    public void execute(LinkedHashMap<String, FieldUDT_Mdl> filter, String widgetName, DmReportProperty_Mdl reportProperty, AsyncCallback<DmResult<String>> result);

}
