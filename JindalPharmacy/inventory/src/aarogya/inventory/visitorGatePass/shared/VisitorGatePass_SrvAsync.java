package aarogya.inventory.visitorGatePass.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.UserPermission;
import java.util.List;

public interface VisitorGatePass_SrvAsync {

    public void getNavigator(UserPermission userPermission, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> asyncCallback);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<VisitorGatePass_Mdl>> asyncCallback);

    public void getSlipNoDetail(String recId, AsyncCallback<DmResult<VisitorGatePass_Mdl>> asyncCallback);

    public void setGateOut(VisitorGatePass_Mdl mrTemp, boolean isGateOut, AsyncCallback<DmResult<String>> result);

    public void saveRecord(VisitorGatePass_Mdl mrTemp, String voucherPrefix, AsyncCallback<DmResult<String>> asyncCallback);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void getDefaultValues(AsyncCallback<DmResult<Default_Mdl>> asyncCallBack);

    public void getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtCity(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHCity_Mdl>>> result);

    public void getHVehicleType(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getDefaultDateTime(AsyncCallback<DmResult<Default_Mdl>> asyncCallBack);

    public void getTxtHGateNo(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);

    public void getVisitorDetails(String mobioleNo, AsyncCallback<DmResult<VisitorGatePass_Mdl>> asyncCallback);
}
