package aarogya.pharmacy.enquiry.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough_Srv;
import java.util.List;

public interface Enquiry_Srv extends RemoteService, IWalkThrough_Srv {

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission);

    public DmResult<Enquiry_Mdl> getMoveRec(String strKey);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHEnquiryType(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHSupplier_Mdl>> getTxtHSupplier(boolean flagFilterByVoucherCategory, String v_Type, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHItem_Mdl>> getTxtHItem(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<String> saveRecord(Enquiry_Mdl mrTemp, String voucherPrefix, boolean isApprove);

    public DmResult<String> deleteRecord(String strKey, String guiConfigCode);

    public DmResult<String> cancelVendor(String strKey, String subCode);

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<List<GrdIndentDetails_Mdl>> getIndentDetail(List<KeyFieldExtended> listKF, String itemCode);

    public DmResult<DefaultValue_Mdl> getDefaultValue();

    public DmResult<String> setApproval(String approvedBy, String docId);

    public DmResult<List<GrdItemDetail_Mdl>> getItemDetail(List<KeyFieldExtended> keyFields);

    public DmResult<List<GrdVendor_Mdl>> getApprovedVendors(String itemCsv, String vendorCsv);

    public DmResult<String> setApproval(String approvedBy, boolean isApprove, String docId);

    public DmResultPageing<List<GrdIndentDetailsSearch_Mdl>> getIndentSearchDetail(String itemCode, IndentSearchValue_Mdl searchValue_Mdl, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartmentIndentSearch(DmHelper helper, boolean blnToEndOfFile);
}
