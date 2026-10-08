package dataman.dmToolkitR.openSourceReport.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.KeyFieldExtended;
import java.util.LinkedHashMap;

public interface OpenSourceReport_Srv extends RemoteService {

    public DmResult<String> execute(String reportName, String paperSize, String reportTitle, LinkedHashMap<String, FieldUDT_Mdl> filter, String regionalLanguage, DmReportProperty_Mdl reportProperty);

    public DmResult<KeyFieldExtended> getReportSetting(String v_Type);


}
