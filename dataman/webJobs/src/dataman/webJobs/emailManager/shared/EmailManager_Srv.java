package dataman.webJobs.emailManager.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.dmFileManager.shared.DmIFile_Mdl;
import dataman.dmBase.self.shared.DmDynamicGridRow;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface EmailManager_Srv extends RemoteService {

    public DmResult<String> sendEmail(Template_Mdl templateMdl, DmIFile_Mdl file);

    public DmResultPageing<List<Template_Mdl>> getTxtHTempName(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<DynamicGrid_Mdl> getMetaData(Template_Mdl info_Mdl);

    public DmResult<KeyFieldExtended> getCountRecords(Template_Mdl template_Mdl);

    public DmResultPageing<List<DmDynamicGridRow>> getData(boolean isCheckedRecord, SearchValue_Mdl mdl, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<String> updateRecord(Template_Mdl templateMdl, DmDynamicGridRow ddgr, boolean isCheckAll, boolean isCheckChange);

    public DmResultPageing<List<KeyFieldExtended>> getHelp(String columnName, String tableName, DmHelper helper, boolean blnToEndOfFile);
}
