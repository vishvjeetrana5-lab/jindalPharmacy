package aarogya.pharmacy.reports.shared;

import aarogya.pharmacy.reports.shared.TxtHsupplier_Mdl;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import java.util.LinkedHashMap;
import java.util.List;

public interface RptCdConfiguration_SrvAsync {

    public void getTxtHsupplier(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHsupplier_Mdl>>> result);

    public void getTxtHcompany(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void execute(String sessionID, LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName, AsyncCallback<DmResult<KeyFieldExtended>> result);

}
