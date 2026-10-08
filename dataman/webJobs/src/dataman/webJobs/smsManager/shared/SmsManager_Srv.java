package dataman.webJobs.smsManager.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmDynamicGridRow;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.webJobs.emailManager.shared.Template_Mdl;
import dataman.webJobs.smsTemplate.shared.GrdFieldName_Mdl;
import java.util.List;

public interface SmsManager_Srv extends RemoteService {

    public DmResult<String> sendSms(Template_Mdl templateMdl);

    public DmResultPageing<List<Template_Mdl>> getTxtHTempName(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<DynamicGrid_Mdl> getMetaData(Template_Mdl templateMdl);
    
    public DmResult<List<GrdFieldName_Mdl>> getMetaDataOnly(Template_Mdl templateMdl);

    public DmResultPageing<List<DmDynamicGridRow>> getData(boolean isCheckedRecord, SearchValue_Mdl mdl, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<String> updateRecord(Template_Mdl templateMdl, DmDynamicGridRow ddgr, boolean isCheckAll, boolean isCheckChange);

    public DmResult<KeyFieldExtended> getCountRecords(Template_Mdl template_Mdl);
}
