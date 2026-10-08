package dataman.webJobs.smsTemplate.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface SmsTemplate_SrvAsync {

    public void getNavigator(AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<SmsTemplate_Mdl>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void saveRecord(SmsTemplate_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void getFind(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getSqlDataSource(String functionName, AsyncCallback<DmResult<List<GrdFieldName_Mdl>>> result);

    public void getReadFile(SmsTemplate_Mdl mrTemp, AsyncCallback<DmResult<List<GrdFieldName_Mdl>>> result);

    public void getTxtHUndTempName(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHFunctionName(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHDataSource(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getSuggetionTemplate(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<String>>> result);

    public void exportJson(String sessionId, SmsTemplate_Mdl mrTemp, AsyncCallback<DmResult<KeyFieldExtended>> result);

    public void importJson(String filePath, String fileName, AsyncCallback<DmResult<SmsTemplate_Mdl>> result);

    public void checkImportedValues(SmsTemplate_Mdl mrTemp, AsyncCallback<DmResult<SmsTemplate_Mdl>> result);
}
