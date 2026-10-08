package aarogya.master.organizationServiceMapping.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface OrganizationServiceMapping_SrvAsync {

    public void getNavigator(AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, String orgCode, AsyncCallback<DmResult<OrganizationServiceMapping_Mdl>> result);

    public void deleteRecord(String applicableFrom, String organizationCode, String strKey, AsyncCallback<DmResult<String>> result);

    public void saveRecord(OrganizationServiceMapping_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void getFind(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getTxtHRoomCategory(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHService(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHOrganization(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHServiceTest(String orgCode, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHServiceTest_Mdl>>> result);

    public void getOrganizationDetail(String organizationCode, AsyncCallback<DmResult<List<GrdOrganizationDetail_Mdl>>> result);

    public void getImportData(String dataFilePath, AsyncCallback<DmResult<List<GrdService_Mdl>>> result);

    public void downloadCSV(String session, AsyncCallback<DmResult<KeyFieldExtended>> result);

}
