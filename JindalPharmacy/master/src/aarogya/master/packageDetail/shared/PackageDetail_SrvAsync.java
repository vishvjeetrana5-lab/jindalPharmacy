package aarogya.master.packageDetail.shared;

import aarogya.selfToolkit.stStatic.shared.ServicePriceList_Mdl;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.util.List;

public interface PackageDetail_SrvAsync {

    public void saveRecord(PackageDetail_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void saveApplicable(String code, String fromDate, String toDate, AsyncCallback<DmResult<String>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void getPackageDetail(String code, String packageCode, String organizationCode, String roomCategoryCode, AsyncCallback<DmResult<PackageDetail_Mdl>> result);

    public void getCopyPackage(String code, AsyncCallback<DmResult<PackageDetail_Mdl>> result);

    public void getRoomCategoryDetail(String packageCode, String organizationCode, String fromDate, String toDate, AsyncCallback<DmResult<List<GrdRoomCategory_Mdl>>> result);

    public void getTxtHActive(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHSchedule(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHService(boolean isInvestigation, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHItem(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHMedicineName_Mdl>>> result);

    public void getTxtHRoomCategory(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHPackage(boolean isApplicable, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHPackage_Mdl>>> result);

    public void getServicePriceBestFit(ServicePriceList_Mdl servicePrice_Mdl, AsyncCallback<DmResult<List<ServicePriceList_Mdl>>> result);

}
