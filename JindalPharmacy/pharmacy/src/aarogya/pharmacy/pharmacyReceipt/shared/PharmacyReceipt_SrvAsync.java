package aarogya.pharmacy.pharmacyReceipt.shared;

import aarogya.pharmacy.ledgerMaster.shared.LedgerMaster_Mdl;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface PharmacyReceipt_SrvAsync {

    public void getTxtHCustomerType(DmHelper helper, boolean blnToEndOfFile, boolean opdAllow, boolean ipdAllow, boolean dayCareAllow, boolean emergencyAllow, boolean walkInAllow, boolean ledgerAllow, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getNavigator(UserPermission userPermission, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getDefaultValues(AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void getMoveRec(String OpdCode, AsyncCallback<DmResult<PharmacyReceipt_Mdl>> result);

    public void deleteRecord(UserPermission userPermission, String strKey, ModifyReason_Mdl modifyReasonMdl, AsyncCallback<DmResult<String>> result);

    public void saveRecord(UserPermission userPermission, PharmacyReceipt_Mdl mrTemp, String voucherPrefix, AsyncCallback<DmResult<String>> result);

    public void getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getCustomerDetails(String customerType, String uniqueId, AsyncCallback<DmResult<LedgerMaster_Mdl>> result);

}
