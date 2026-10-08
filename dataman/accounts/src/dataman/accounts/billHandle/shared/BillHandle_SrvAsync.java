package dataman.accounts.billHandle.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough_SrvAsync;
import java.util.List;

public interface BillHandle_SrvAsync extends IWalkThrough_SrvAsync {

    public void getNavigator(UserPermission userPermission, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<BillHandle_Mdl>> result);

    public void getDefaultValue(AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void saveRecord(BillHandle_Mdl mrTemp, String voucherPrefix, boolean isApprove, AsyncCallback<DmResult<String>> result);

    public void getFind(UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getTxtHToLocation(DmHelper pageing, boolean blnToEndOfFile, String location, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHFromLocation(DmHelper pageing, boolean blnToEndOfFile,String location, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHRequisitiontype(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getPBillByModuleLocation(String location, String fromDate, String toDate, String fromLocation, AsyncCallback<DmResult<List<GrdBillDetail_Mdl>>> asyncCallback);

    public void setApproval(String loginUserName, boolean approve, String docId, AsyncCallback<DmResult<String>> asyncCallback);

}
