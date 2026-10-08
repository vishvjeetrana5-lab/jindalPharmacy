package aarogya.pharmacy.pharmacyFinalDiscountEntry.shared;

import aarogya.pharmacy.ledgerMaster.shared.LedgerMaster_Mdl;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface PharmacyFinalDiscountEntry_SrvAsync {

    public void getTxtHCustomerType(DmHelper helper, boolean blnToEndOfFile, boolean opdAllow, boolean ipdAllow, boolean dayCareAllow, boolean emergencyAllow, boolean walkInAllow, boolean ledgerAllow, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getNavigator(UserPermission userPermission, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getDefaultValues(AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void getMoveRec(String OpdCode, AsyncCallback<DmResult<PharmacyFinalDiscountEntry_Mdl>> result);

    public void deleteRecord(UserPermission userPermission, String strKey, ModifyReason_Mdl modifyReasonMdl, String regDocid, AsyncCallback<DmResult<String>> result);

    public void saveRecord(UserPermission userPermission, PharmacyFinalDiscountEntry_Mdl mrTemp, String voucherPrefix, AsyncCallback<DmResult<String>> result);

    public void getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getCustomerDetails(String customerType, String uniqueId, String v_Date, AsyncCallback<DmResult<LedgerMaster_Mdl>> result);

    public void getSaleRecords(String v_Date, String lmCode, AsyncCallback<DmResult<List<HSN_TAX_WISE_PopUp_Mdl>>> result);

    public void getTxtHDiscountReason(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);

}
