package aarogya.mis.reports.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.mis.reports.shared.TxtHVType_Mdl;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.self.shared.*;
import java.util.LinkedHashMap;
import java.util.List;

public interface RptCollectionRegister_SrvAsync {

    public void getTxtHStatus(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getDefaultValue(AsyncCallback<DmResult<KeyFieldExtended>> result);

    public void getTxtHCatType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHModuleType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHVType_Mdl>>> result);

    public void getTxtHPayMode(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void execute(String sessionId,LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName, AsyncCallback<DmResult<KeyFieldExtended>> result);

    public void getTxtHDateApplicable(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHUserName(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);
    
     public void getTxtHReportView(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);


}
