package aarogya.pharmacy.reports.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import java.util.LinkedHashMap;
import java.util.List;

public interface RptItemWiseProfitability_SrvAsync {

    public void getTxtHMedCat(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void execute(LinkedHashMap<String, FieldUDT_Mdl> filter, String widgetName, DmReportProperty_Mdl reportProperty, AsyncCallback<DmResult<String>> result);

    public void getTxtHCompany(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHCompany_Mdl>>> result);

    public void getTxtHMedicineName(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

}
