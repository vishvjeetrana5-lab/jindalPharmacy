package aarogya.nabh.reports.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import java.util.LinkedHashMap;

public interface RptIndicator10_Srv extends RemoteService {

    public DmResult<KeyFieldExtended> execute(String session, LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty);

}
