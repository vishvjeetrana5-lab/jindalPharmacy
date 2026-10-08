package dataman.accounts.reports.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import java.util.LinkedHashMap;
import java.util.List;

public interface RptBillHandleRegister_Srv extends RemoteService {

    public DmResultPageing<List<KeyFieldExtended>> getTxtHBillType(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHModule(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<KeyFieldExtended> execute(String sessionId,LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty,String widgetName);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHVendor(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHLastLoc(DmHelper helper, boolean blnToEndOfFile);

}
