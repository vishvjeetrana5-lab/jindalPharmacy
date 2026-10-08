package aarogya.inventory.workOrder.shared;

import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough_SrvAsync;
import java.util.List;

public interface WorkOrder_SrvAsync extends IWalkThrough_SrvAsync {

    public void getNavigator(UserPermission userPermission, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<WorkOrder_Mdl>> result);

    public void deleteRecord(String strKey, String guiKeyCode, AsyncCallback<DmResult<String>> result);

    public void saveRecord(WorkOrder_Mdl mrTemp, String voucherPrefix, boolean isApprove, AsyncCallback<DmResult<String>> result);

    public void getFind(UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getTxtHVoucherType(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHSupplierPT(boolean flagFilterByVoucherCategory, String v_Type, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHSupplierPT_Mdl>>> result);

    public void getTxtHQuotationNo(boolean isShowUsedQuotationsAlso, String supplierCode, String v_Date, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHQuotationNo_Mdl>>> result);

    public void getTxtHItem(DmHelper pageing, boolean blnToEndOfFile,boolean blnAllItemHelp, AsyncCallback<DmResultPageing<List<TxtHItemName_Mdl>>> result);

    public void getIndentDetail(List<KeyFieldExtended> keyFields, ItemProperty_Mdl ippm_Mdl, AsyncCallback<DmResult<List<GrdIndentDetail_Mdl>>> result);

    public void getItemDetail(String indentDocId, List<KeyFieldExtended> keyFields, AsyncCallback<DmResult<WorkOrder_Mdl>> result);

    public void getDefaultValues(AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void getServerDateTime(AsyncCallback<DmResult<String[]>> result);

    public void setApproval(String approvedBy, boolean isApprove, String docId, AsyncCallback<DmResult<String>> result);

}
