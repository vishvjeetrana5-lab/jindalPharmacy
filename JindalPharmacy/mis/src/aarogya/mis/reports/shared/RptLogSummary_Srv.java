package aarogya.mis.reports.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.LinkedHashMap;
import java.util.List;

public interface RptLogSummary_Srv extends RemoteService {

    public DmResultPageing<List<KeyFieldExtended>> getTxtHEntry(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucherNo(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<String> execute(String widgetName, LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty);
}
