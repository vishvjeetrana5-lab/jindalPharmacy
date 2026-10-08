package aarogya.master.cardRegistration.shared;

import aarogya.selfToolkit.walletSystem.shared.CardRegistration_Mdl;
import aarogya.selfToolkit.walletSystem.shared.PersonDetail_Mdl;
import aarogya.selfToolkit.walletSystem.shared.TxtHCardType_Mdl;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface CardRegistration_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission);

    public DmResult<CardRegistration_Mdl> getMoveRec(String keyCode);

    public DmResult<String> saveRecord(UserPermission userPermission, CardRegistration_Mdl mrTemp, String voucherPrefix);

    public DmResult<String> deleteRecord(UserPermission userPermission, String keyCode);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile, UserPermission userPermission, String vPrefix);

    public DmResultPageing<List<TxtHCity_Mdl>> getTxtHCity(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHPrefix(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHCardHelp(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHLinkAccount(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHCardType_Mdl>> getTxtHCardType(DmHelper pageing, boolean blnToEndOfFile, String voucherDate);

    public DmResult<PersonDetail_Mdl> getUHIDDetails(String uhidDocId, String uhidRecId);

    public DmResult<KeyFieldExtended> getDefaultValue();

    public DmResult<String> getCardNo(String cardNo);
}
