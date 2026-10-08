package aarogya.master.additionalUserPermission.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface AdditionalUserPermission_SrvAsync {

    public void saveRecord(GrdAdditionalUserPermission_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void updateUser(boolean isDelete, GrdManageUserRole_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHLinkedAc(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHNursingStation(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getFillGrid(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param, AsyncCallback<DmResultPageing<List<GrdAdditionalUserPermission_Mdl>>> result);

    public void getTxtHOtherDepartment(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHServiceCategory(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHUser(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHRole(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHService(String categoryCode, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHService_Mdl>>> result);

    public void getFillUserDetail(String roleType, AsyncCallback<DmResult<List<GrdManageUserRole_Mdl>>> result);

    public void getTxtHDiscForNDS(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHLocationAllowed(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);

    public void getTxtHPaymentMode(DmHelper pageing, boolean blnToEndOfFile,String v_Type, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHVoucherType(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHIsDefault(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

}
