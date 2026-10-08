package dataman.webJobs.smsTemplate.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface SmsTemplate_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResult<SmsTemplate_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(SmsTemplate_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<List<GrdFieldName_Mdl>> getSqlDataSource(String functionName);

    public DmResult<List<GrdFieldName_Mdl>> getReadFile(SmsTemplate_Mdl mrTemp);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHUndTempName(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHFunctionName(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDataSource(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<List<String>> getSuggetionTemplate(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<KeyFieldExtended> exportJson(String sessionId, SmsTemplate_Mdl mrTemp);

    public DmResult<SmsTemplate_Mdl> importJson(String filePath, String fileName);

    public DmResult<SmsTemplate_Mdl> checkImportedValues(SmsTemplate_Mdl mrTemp);

}
