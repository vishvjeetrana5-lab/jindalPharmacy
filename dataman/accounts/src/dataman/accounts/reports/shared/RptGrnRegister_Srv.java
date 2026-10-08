package dataman.accounts.reports.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.self.shared.*;
import java.util.LinkedHashMap;
import java.util.List;

public interface RptGrnRegister_Srv extends RemoteService {

    public DmResultPageing<List<KeyFieldExtended>> getTxtHSite(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHStatus(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHGRNType(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHVendor(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHItemMedCompany_Mdl>> getTxtHItem(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHItemCat(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHItemGroup(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHPorder(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHGRNNo(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<KeyFieldExtended> execute(String sessionId,LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName);

    public DmResult<KeyFieldExtended> getDefaultValue();
}
