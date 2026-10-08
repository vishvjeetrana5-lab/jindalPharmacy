package aarogya.pharmacy.pharmacyReceipt.shared;

import aarogya.pharmacy.ledgerMaster.shared.LedgerMaster_Mdl;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface PharmacyReceipt_Srv extends RemoteService {

    public DmResultPageing<List<KeyFieldExtended>> getTxtHCustomerType(DmHelper helper, boolean blnToEndOfFile, boolean opdAllow, boolean ipdAllow, boolean dayCareAllow, boolean emergencyAllow, boolean walkInAllow, boolean ledgerAllow);

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission);

    public DmResult<DefaultValue_Mdl> getDefaultValues();

    public DmResult<PharmacyReceipt_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(UserPermission userPermission, PharmacyReceipt_Mdl mrTemp, String voucherPrefix);

    public DmResult<String> deleteRecord(UserPermission userPermission, String strKey, ModifyReason_Mdl modifyReasonMdl);

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<LedgerMaster_Mdl> getCustomerDetails(String customerType, String uniqueId);

}
