package aarogya.master.packageDetail.shared;

import aarogya.selfToolkit.stStatic.shared.ServicePriceList_Mdl;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.util.List;

public interface PackageDetail_Srv extends RemoteService {

    public DmResult<String> saveRecord(PackageDetail_Mdl mrTemp);

    public DmResult<String> saveApplicable(String code, String fromDate, String toDate);

    public DmResult<String> deleteRecord(String strKey);

    public DmResult<PackageDetail_Mdl> getPackageDetail(String code, String packageCode, String organizationCode, String roomCategoryCode);

    public DmResult<PackageDetail_Mdl> getCopyPackage(String code);

    public DmResult<List<GrdRoomCategory_Mdl>> getRoomCategoryDetail(String packageCode, String organizationCode, String fromDate, String toDate);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHSchedule(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHActive(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHService(boolean isInvestigation, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHMedicineName_Mdl>> getTxtHItem(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHRoomCategory(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHPackage_Mdl>> getTxtHPackage(boolean isApplicable, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<List<ServicePriceList_Mdl>> getServicePriceBestFit(ServicePriceList_Mdl servicePrice_Mdl);

}
