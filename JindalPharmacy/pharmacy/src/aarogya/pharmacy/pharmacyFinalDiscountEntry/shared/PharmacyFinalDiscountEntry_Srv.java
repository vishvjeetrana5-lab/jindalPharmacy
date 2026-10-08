package aarogya.pharmacy.pharmacyFinalDiscountEntry.shared;

import aarogya.pharmacy.ledgerMaster.shared.LedgerMaster_Mdl;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface PharmacyFinalDiscountEntry_Srv extends RemoteService {

    public DmResultPageing<List<KeyFieldExtended>> getTxtHCustomerType(DmHelper helper, boolean blnToEndOfFile, boolean opdAllow, boolean ipdAllow, boolean dayCareAllow, boolean emergencyAllow, boolean walkInAllow, boolean ledgerAllow);

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission);

    public DmResult<DefaultValue_Mdl> getDefaultValues();

    public DmResult<PharmacyFinalDiscountEntry_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(UserPermission userPermission, PharmacyFinalDiscountEntry_Mdl mrTemp, String voucherPrefix);

    public DmResult<String> deleteRecord(UserPermission userPermission, String strKey, ModifyReason_Mdl modifyReasonMdl, String regDocid);

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<LedgerMaster_Mdl> getCustomerDetails(String customerType, String uniqueId, String v_Date);

    public DmResult<List<HSN_TAX_WISE_PopUp_Mdl>> getSaleRecords(String v_Date, String lmCode);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDiscountReason(DmHelper helper, boolean blnToEndOfFile);

}
