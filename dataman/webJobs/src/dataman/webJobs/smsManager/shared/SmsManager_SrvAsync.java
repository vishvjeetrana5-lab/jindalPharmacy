package dataman.webJobs.smsManager.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmDynamicGridRow;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.webJobs.emailManager.shared.Template_Mdl;
import dataman.webJobs.smsTemplate.shared.GrdFieldName_Mdl;
import java.util.List;

public interface SmsManager_SrvAsync {

    public void sendSms(Template_Mdl templateMdl, AsyncCallback<DmResult<String>> result);

    public void getTxtHTempName(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Template_Mdl>>> result);

    public void getMetaData(Template_Mdl templateMdl, AsyncCallback<DmResult<DynamicGrid_Mdl>> result);
    
    public void getMetaDataOnly(Template_Mdl templateMdl, AsyncCallback<DmResult<List<GrdFieldName_Mdl>>> result);

    public void getCountRecords(Template_Mdl template_Mdl, AsyncCallback<DmResult<KeyFieldExtended>> result);

    public void getData(boolean isCheckedRecord, SearchValue_Mdl mdl, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<DmDynamicGridRow>>> result);

    public void updateRecord(Template_Mdl templateMdl, DmDynamicGridRow ddgr, boolean isCheckAll, boolean isCheckChange, AsyncCallback<DmResult<String>> result);
}
