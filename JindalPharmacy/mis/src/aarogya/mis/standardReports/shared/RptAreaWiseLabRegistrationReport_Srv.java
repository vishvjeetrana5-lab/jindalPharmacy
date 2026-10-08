package aarogya.mis.standardReports.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.LinkedHashMap;
import java.util.List;

public interface RptAreaWiseLabRegistrationReport_Srv extends RemoteService {

    public DmResultPageing<List<KeyFieldExtended>> getTxtHType(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHArea(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHBalance(DmHelper helper, boolean blnToEndOfFile);

}
