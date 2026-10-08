package aarogya.mis.standardReports.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.LinkedHashMap;
import java.util.List;

public interface RptConsultantWiseConsultationReport_SrvAsync {

    public void getTxtHOPDType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHTime(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHReferred(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHConsultant(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHConsultant_Mdl>>> result);

    public void getTxtHUser(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);
    
     public void getTxtHReportView(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName, AsyncCallback<DmResult<String>> result);
}
