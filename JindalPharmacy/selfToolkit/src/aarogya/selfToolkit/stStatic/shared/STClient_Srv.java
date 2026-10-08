package aarogya.selfToolkit.stStatic.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import java.util.LinkedHashMap;

public interface STClient_Srv extends RemoteService {

    public DmResult<STLocalSetting_Mdl> getSettingsFromJson(String jsonString);

    public DmResult<String> updatePrintStatus(String tableName, String fieldName, String docId);

    public DmResult<String> sendReportToEmail(String docId, String vType, LinkedHashMap<String, FieldUDT_Mdl> filter, String regionalLanguage, String reportTitle);

}
