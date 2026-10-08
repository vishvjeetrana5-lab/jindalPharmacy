package aarogya.pharmacy.reports.shared;

import aarogya.pharmacy.reports.shared.TxtHsupplier_Mdl;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import java.util.LinkedHashMap;
import java.util.List;

public interface RptCdConfiguration_Srv extends RemoteService {

    public DmResultPageing<List<TxtHsupplier_Mdl>> getTxtHsupplier(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHcompany(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<KeyFieldExtended> execute(String sessionID, LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName);

}
