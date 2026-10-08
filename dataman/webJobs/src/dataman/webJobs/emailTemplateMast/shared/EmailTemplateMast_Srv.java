package dataman.webJobs.emailTemplateMast.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface EmailTemplateMast_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResult<EmailTemplateMast_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(EmailTemplateMast_Mdl mrTemp);

    public DmResult<String> updateImageContentId(String code);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<List<GrdFieldName_Mdl>> getSqlDataSource(String functionName);

    public DmResult<List<GrdFieldName_Mdl>> getReadFile(EmailTemplateMast_Mdl mrTemp);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHUndTempName(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHFunctionName(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDataSource(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<List<String>> getSuggetionTemplate(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<KeyFieldExtended> exportJson(String sessionId, EmailTemplateMast_Mdl mrTemp);

    public DmResult<EmailTemplateMast_Mdl> importJson(String filePath, String fileName);

    public DmResult<EmailTemplateMast_Mdl> checkImportedValues(EmailTemplateMast_Mdl mrTemp);

}
