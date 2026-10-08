package aarogya.pharmacy.reports.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import java.util.LinkedHashMap;
import java.util.List;

public interface RptItemWiseProfitability_Srv extends RemoteService {

    public DmResultPageing<List<KeyFieldExtended>> getTxtHMedCat(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, String widgetName, DmReportProperty_Mdl reportProperty);

    public DmResultPageing<List<TxtHCompany_Mdl>> getTxtHCompany(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHMedicineName(DmHelper helper, boolean blnToEndOfFile);

}
