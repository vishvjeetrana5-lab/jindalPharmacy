package dataman.accounts.wholesaleBillSettlement.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.UserPermission;
import java.util.List;

public interface WholesaleBillSettlement_SrvAsync {

    public void getDefaultValues(AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void getPendingFinalPaymnetReqDetail(String supplierCode, AsyncCallback<DmResult<List<GrdPendingFPReqDetail_Mdl>>> result);

    public void getPendingFinalCreditAdjmentRec(String supplierCode, AsyncCallback<DmResult<List<GridDrAdjustBills_Mdl>>> result);

    public void getServerDateTime(AsyncCallback<String[]> result);

    public void saveRecord(WholesaleBillSettlement_Mdl mrTemp, boolean isApprove, AsyncCallback<DmResult<String>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<WholesaleBillSettlement_Mdl>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void getNavigator(UserPermission userPermission, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getFind(UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void setApproval(String approvedBy, boolean isApprove, String docId, AsyncCallback<DmResult<String>> result);

    public void getTxtHSupplier(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHSupplier_Mdl>>> result);

    public void getTxtHVoucherType(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHVoucherType_Mdl>>> result);

    public void getTxtHAccountName(DmHelper pageing, boolean blnToEndOfFile, String v_Type, AsyncCallback<DmResultPageing<List<TxtHAccountName_Mdl>>> result);

}
