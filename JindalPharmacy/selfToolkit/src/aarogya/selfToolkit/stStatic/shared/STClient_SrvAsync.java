package aarogya.selfToolkit.stStatic.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import java.util.LinkedHashMap;

public interface STClient_SrvAsync {

    public void getSettingsFromJson(String jsonString, AsyncCallback<DmResult<STLocalSetting_Mdl>> callback);

    public void updatePrintStatus(String tableName, String fieldName, String docId, AsyncCallback<DmResult<String>> asyncCallback);

    public void sendReportToEmail(String docId, String vType, LinkedHashMap<String, FieldUDT_Mdl> filter, String regionalLanguage, String reportTitle, AsyncCallback<DmResult<String>> asyncCallback);

}
