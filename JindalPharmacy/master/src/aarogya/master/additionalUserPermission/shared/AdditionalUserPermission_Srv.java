package aarogya.master.additionalUserPermission.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface AdditionalUserPermission_Srv extends RemoteService {

    public DmResult<String> saveRecord(GrdAdditionalUserPermission_Mdl mrTemp);

    public DmResult<String> updateUser(boolean isDelete, GrdManageUserRole_Mdl mrTemp);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHLinkedAc(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHNursingStation(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<GrdAdditionalUserPermission_Mdl>> getFillGrid(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHOtherDepartment(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHLocationAllowed(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHServiceCategory(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHUser(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHRole(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHService_Mdl>> getTxtHService(String categoryCode, DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<List<GrdManageUserRole_Mdl>> getFillUserDetail(String roleType);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDiscForNDS(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHPaymentMode(DmHelper pageing, boolean blnToEndOfFile,String v_Type);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucherType(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHIsDefault(DmHelper pageing, boolean blnToEndOfFile);

}
