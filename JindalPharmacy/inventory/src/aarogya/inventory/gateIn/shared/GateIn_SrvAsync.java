package aarogya.inventory.gateIn.shared;

import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.UserPermission;
import java.util.List;

public interface GateIn_SrvAsync {

    public void getFind(UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void deleteRecord(String strKey, GateIn_Mdl mrTemp, ModifyReason_Mdl modifyReasonMdl, AsyncCallback<DmResult<GateIn_Mdl>> result);

    public void getTxtHType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHEmployeeName(String voucherCategory, String v_Type, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHPartyName_Mdl>>> result);

    public void getTxtHMode(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHPurpose(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHDepartment(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHUnit(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHAsset(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getNavigator(UserPermission userPermission, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<GateIn_Mdl>> asyncCallback);

    public void getDefaultValues(AsyncCallback<DmResult<Default_Mdl>> asyncCallBack);

    public void saveRecord(GateIn_Mdl mrTemp, String voucherPrefix, AsyncCallback<DmResult<String>> asyncCallback);

    public void getSlipNoDetail(String recId, AsyncCallback<DmResult<GateIn_Mdl>> asyncCallback);

    public void getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHVoucher_mdl>>> result);

    public void getDefaultDateTime(AsyncCallback<DmResult<Default_Mdl>> asyncCallBack);

    public void getItemDetail(String gateOutDocId, List<KeyFieldExtended> keyFields, AsyncCallback<DmResult<List<GateInGridEdit_Mdl>>> result);

    public void ifDocumentUsed(String strKeyCode, AsyncCallback<DmResult<Boolean>> result);

    public void getHRemark(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

}
