package dataman.accounts.reports.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import java.util.LinkedHashMap;
import java.util.List;

public interface RptSupplierMaster_SrvAsync {

    public void execute(String sessionID, LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName, AsyncCallback<DmResult<KeyFieldExtended>> result);

    public void getTxtHSupplier(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHsupplier_Mdl>>> asyncCallback);

}
