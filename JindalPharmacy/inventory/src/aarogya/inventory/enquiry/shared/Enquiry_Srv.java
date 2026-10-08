package aarogya.inventory.enquiry.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough_Srv;
import java.util.List;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;

public interface Enquiry_Srv extends RemoteService, IWalkThrough_Srv {

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission);

    public DmResult<Enquiry_Mdl> getMoveRec(String strKey);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHEnquiryType(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHSupplier_Mdl>> getTxtHSupplier(DmHelper helper, boolean blnToEndOfFile, String v_Type, boolean isFromEnquiry);

    public DmResultPageing<List<TxtHItem_Mdl>> getTxtHItem(String v_Type, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<String> saveRecord(Enquiry_Mdl mrTemp, String voucherPrefix, boolean isApprove);

    public DmResult<String> deleteRecord(String strKey, String guiConfigCode);

    public DmResult<String> cancelVendor(String strKey, String subCode);

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<List<GrdIndentDetails_Mdl>> getIndentDetail(List<KeyFieldExtended> listKF, ItemProperty_Mdl ippm);

    public DmResult<DefaultValue_Mdl> getDefaultValue();

    public DmResult<String> setApproval(String approvedBy, String docId);

    public DmResult<List<GrdItemDetail_Mdl>> getItemDetail(String v_Type, List<KeyFieldExtended> keyFields);

    public DmResult<List<GrdVendor_Mdl>> getApprovedVendors(String itemCsv, String vendorCsv);

    public DmResult<String> setApproval(String approvedBy, boolean isApprove, String docId);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartmentIndentSearch(DmHelper helper, boolean blnToEndOfFile);
}
