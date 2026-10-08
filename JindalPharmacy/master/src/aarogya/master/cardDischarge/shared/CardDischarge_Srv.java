package aarogya.master.cardDischarge.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.selfToolkit.stStatic.shared.RegisteredWallet_Mdl;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface CardDischarge_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission);

    public DmResult<CardDischarge_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(CardDischarge_Mdl mrTemp, String voucherPrefix, UserPermission userPermission);

    public DmResult<String> deleteRecord(UserPermission userPermission, String strKey, String V_Date);

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<RegisteredWallet_Mdl>> getTxtHCardNo(String voucherDate, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<DefaultValue_Mdl> getDefaultValues();

    public DmResult<RegisteredWallet_Mdl> getCardDetail(String cardNo, String physicalId, String docId);
}
