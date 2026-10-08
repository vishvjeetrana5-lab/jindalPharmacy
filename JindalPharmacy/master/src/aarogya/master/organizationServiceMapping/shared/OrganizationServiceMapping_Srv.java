package aarogya.master.organizationServiceMapping.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface OrganizationServiceMapping_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResult<OrganizationServiceMapping_Mdl> getMoveRec(String strKey, String orgCode);

    public DmResult<String> saveRecord(OrganizationServiceMapping_Mdl mrTemp);

    public DmResult<String> deleteRecord(String applicableFrom, String organizationCode, String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHRoomCategory(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHService(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHOrganization(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHServiceTest_Mdl>> getTxtHServiceTest(String orgCode, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<List<GrdOrganizationDetail_Mdl>> getOrganizationDetail(String organizationCode);

    public DmResult<List<GrdService_Mdl>> getImportData(String dataFilePath);

    public DmResult<KeyFieldExtended> downloadCSV(String session);

}
