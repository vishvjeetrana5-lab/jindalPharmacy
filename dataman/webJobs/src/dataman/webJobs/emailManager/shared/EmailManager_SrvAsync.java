package dataman.webJobs.emailManager.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.dmFileManager.shared.DmIFile_Mdl;
import dataman.dmBase.self.shared.DmDynamicGridRow;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface EmailManager_SrvAsync {

    public void sendEmail(Template_Mdl templateMdl, DmIFile_Mdl file, AsyncCallback<DmResult<String>> result);

    public void getTxtHTempName(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Template_Mdl>>> result);

    public void getMetaData(Template_Mdl info_Mdl, AsyncCallback<DmResult<DynamicGrid_Mdl>> result);

    public void getCountRecords(Template_Mdl template_Mdl, AsyncCallback<DmResult<KeyFieldExtended>> result);

    public void getData(boolean isCheckedRecord, SearchValue_Mdl mdl, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<DmDynamicGridRow>>> result);

    public void updateRecord(Template_Mdl templateMdl, DmDynamicGridRow ddgr, boolean isCheckAll, boolean isCheckChange, AsyncCallback<DmResult<String>> result);

    public void getHelp(String columnName, String tableName, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);
}
