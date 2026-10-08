package aarogya.master.cardDeactive.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.selfToolkit.stStatic.shared.RegisteredWallet_Mdl;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface CardDeactivate_Srv extends RemoteService {

    public DmResult<DefaultValue_Mdl> getDefaultValues();

    public DmResult<String> saveRecord(CardDeactivate_Mdl mrTemp, String voucherPrefix);

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission);

    public DmResult<CardDeactivate_Mdl> getMoveRec(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHVrType(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<RegisteredWallet_Mdl> getCardDetail(String cardNo, String physicalId, String docId);

    public DmResultPageing<List<RegisteredWallet_Mdl>> getTxtHCardNo(String voucherDate, DmHelper helper, boolean blnToEndOfFile);

}
