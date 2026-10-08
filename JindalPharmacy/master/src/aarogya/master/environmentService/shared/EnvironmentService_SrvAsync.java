package aarogya.master.environmentService.shared;

import aarogya.selfToolkit.stStatic.shared.STEnum;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.dmFileManager.shared.DmAttachment_Mdl;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface EnvironmentService_SrvAsync {

    public void getMoveRec(String strKey, AsyncCallback<DmResult<EnvironmentService_Mdl>> result);

    public void saveRecord(EnvironmentService_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void getTxtHSite(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHConsultant(String siteCode, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getDefaultValue(AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void getTxtHAccountName(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getServices(DmHelper helper, boolean blnToEndOfFile, STEnum.ServiceNature[] serviceNatures, String serviceCategory, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

}
