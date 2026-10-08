package aarogya.inventory.itemIssue.shared;

import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import aarogya.selfToolkit.toolTip.shared.ToolTip_Mdl;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface ItemIssue_SrvAsync {

    public void getNavigator(UserPermission userPermission, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<ItemIssue_Mdl>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void saveRecord(ItemIssue_Mdl mrTempCurrent, ItemIssue_Mdl mrTempPrevious, String voucherPrefix, AsyncCallback<DmResult<String>> result);

    public void getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getTxtHVoucher(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHIssueTo(String v_Type, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHIssueTo_Mdl>>> result);

    public void getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHItemName(String v_Type, String departmentCode, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHItemName_Mdl>>> result);

    public void getDefaultValues(UserPermission userPermission, AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void getItemDetail(String reqDocId, String itemIssueDocId, String departmentCode, AsyncCallback<DmResult<List<GrdItemDetail_Mdl>>> result);

    public void getIbCode(ItemProperty_Mdl mdl, AsyncCallback<DmResult<String>> result);

    public void getTxtHRequisitionNo(String voucherDate, String itemIssueDocId, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHRequisitionNo_Mdl>>> result);

    public void getToolTipShowStock(String itemCode, String ibCode, String fromDepartment, String toDepartment, AsyncCallback<DmResult<List<ToolTip_Mdl>>> result);

    public void getRequisitionDetail(String reqDocid, String VoucherDate, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResult<List<TxtHRequisitionNo_Mdl>>> result);

    public void getTxtHProject(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHProject_Mdl>>> result);

    public void getTxtHCustomerType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHRegNo(String vType, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHRegNo_Mdl>>> result);

    public void getCheckExceedConsumption(String itemCode, String departmentCode, String currentQty, String allowedQty, String VoucherDate, AsyncCallback<DmResult<String>> result);

    public void getNewIbCode(GrdItemDetail_Mdl mrTemp, String departmentCode, AsyncCallback<DmResult<List<GrdItemDetail_Mdl>>> result);

}
