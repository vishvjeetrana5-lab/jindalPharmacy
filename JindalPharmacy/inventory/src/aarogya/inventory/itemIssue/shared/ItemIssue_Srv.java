package aarogya.inventory.itemIssue.shared;

import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import aarogya.selfToolkit.toolTip.shared.ToolTip_Mdl;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface ItemIssue_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission);

    public DmResult<ItemIssue_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(ItemIssue_Mdl mrTempCurrent, ItemIssue_Mdl mrTempPrevious, String voucherPrefix);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucher(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHIssueTo_Mdl>> getTxtHIssueTo(String v_Type, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHItemName_Mdl>> getTxtHItemName(String v_Type, String departmentCode, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<DefaultValue_Mdl> getDefaultValues(UserPermission userPermission);

    public DmResult<List<GrdItemDetail_Mdl>> getItemDetail(String reqDocId, String itemIssueDocId, String departmentCode);

    public DmResult<String> getIbCode(ItemProperty_Mdl mdl);

    public DmResultPageing<List<TxtHRequisitionNo_Mdl>> getTxtHRequisitionNo(String voucherDate, String itemIssueDocId, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<List<ToolTip_Mdl>> getToolTipShowStock(String itemCode, String ibCode, String fromDepartment, String toDepartment);

    public DmResult<List<TxtHRequisitionNo_Mdl>> getRequisitionDetail(String reqDocid, String VoucherDate, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHProject_Mdl>> getTxtHProject(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHCustomerType(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHRegNo_Mdl>> getTxtHRegNo(String vType, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<String> getCheckExceedConsumption(String itemCode, String departmentCode, String currentQty, String allowedQty, String VoucherDate);

    public DmResult<List<GrdItemDetail_Mdl>> getNewIbCode(GrdItemDetail_Mdl mrTemp, String departmentCode);

}
