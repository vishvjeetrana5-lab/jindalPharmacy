package aarogya.master.environmentService.shared;

import aarogya.selfToolkit.stStatic.shared.STEnum;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.dmFileManager.shared.DmAttachment_Mdl;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface EnvironmentService_Srv extends RemoteService {

    public DmResult<EnvironmentService_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(EnvironmentService_Mdl mrTemp);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHSite(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHConsultant(String siteCode, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<DefaultValue_Mdl> getDefaultValue();

    public DmResultPageing<List<KeyFieldExtended>> getTxtHAccountName(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getServices(DmHelper helper, boolean blnToEndOfFile, STEnum.ServiceNature[] serviceNatures, String serviceCategory);

}
