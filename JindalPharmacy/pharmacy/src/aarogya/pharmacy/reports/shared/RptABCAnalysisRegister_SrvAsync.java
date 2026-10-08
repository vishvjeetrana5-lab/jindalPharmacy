package aarogya.pharmacy.reports.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import java.util.LinkedHashMap;
import java.util.List;

public interface RptABCAnalysisRegister_SrvAsync {

    public void getTxtHMedicine(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHMedicine_Mdl>>> result);

    public void getTxtHMedCat(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHMedGroup(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHAnalysisOn(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, AsyncCallback<DmResult<String>> result);

    public void getTxtHSite(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getDefaultValue(AsyncCallback<DmResult<KeyFieldExtended>> result);

    public void getTxtHCompany(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHCompany_Mdl>>> result);

    public void getTxtHValueOn(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHGroupBy(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

}
