package aarogya.master.reports.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import java.util.LinkedHashMap;
import java.util.List;

public interface RptOrganizationMaster_Srv extends RemoteService {

    public DmResult<String> execute(String appDate, LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHKeyField(DmHelper helper, boolean blnToEndOfFile);
}
