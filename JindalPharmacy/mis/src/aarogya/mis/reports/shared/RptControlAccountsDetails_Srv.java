package aarogya.mis.reports.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import java.util.LinkedHashMap;
import java.util.List;

public interface RptControlAccountsDetails_Srv extends RemoteService {

    public DmResultPageing<List<KeyFieldExtended>> gettxtHControlAccount(DmHelper pageing, boolean blnToEndOfFile);
 
   public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName);

}
