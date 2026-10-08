package dataman.dmToolkitR.openSourceReport.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.KeyFieldExtended;
import java.util.LinkedHashMap;

public interface OpenSourceReport_SrvAsync {

    public void execute(String reportName, String paperSize, String reportTitle, LinkedHashMap<String, FieldUDT_Mdl> filter, String regionalLanguage, DmReportProperty_Mdl reportProperty, AsyncCallback<DmResult<String>> result);

    public void getReportSetting(String v_Type, AsyncCallback<DmResult<KeyFieldExtended>> result);


}
