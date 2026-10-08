package aarogya.inventory.gateOut.shared;

import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.UserPermission;
import java.util.List;

public interface GateOut_Srv extends RemoteService {

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<GateOut_Mdl> deleteRecord(String strKey, GateOut_Mdl mrTemp, ModifyReason_Mdl modifyReasonMdl);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHBillType(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHType(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHPartyName_Mdl>> getTxtHEmployeeName(String voucherCategory, String v_Type, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHMode(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHPurpose(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHBill(String billType, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHUnit(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHAsset(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission);

    public DmResult<GateOut_Mdl> getMoveRec(String strKey);

    public DmResult<Default_Mdl> getDefaultValues();

    public DmResult<String> saveRecord(GateOut_Mdl mrTemp, String voucherPrefix);

    public DmResult<GateOut_Mdl> getSlipNoDetail(String recId);

    public DmResultPageing<List<TxtHVoucher_mdl>> getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<Default_Mdl> getDefaultDateTime();

    public DmResult<List<GateOutGridEdit_Mdl>> getItemDetail(String gateOutDocId, List<KeyFieldExtended> keyFields);

    public DmResult<Boolean> ifDocumentUsed(String strKeyCode);

    public DmResultPageing<List<KeyFieldExtended>> getHRemark(DmHelper helper, boolean blnToEndOfFile);
}
