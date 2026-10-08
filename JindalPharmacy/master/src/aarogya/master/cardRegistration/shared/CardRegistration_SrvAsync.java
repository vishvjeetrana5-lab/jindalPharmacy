package aarogya.master.cardRegistration.shared;

import aarogya.selfToolkit.walletSystem.shared.CardRegistration_Mdl;
import aarogya.selfToolkit.walletSystem.shared.PersonDetail_Mdl;
import aarogya.selfToolkit.walletSystem.shared.TxtHCardType_Mdl;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface CardRegistration_SrvAsync {

    public void getNavigator(UserPermission userPermission, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String keyCode, AsyncCallback<DmResult<CardRegistration_Mdl>> result);

    public void deleteRecord(UserPermission userPermission, String keyCode, AsyncCallback<DmResult<String>> result);

    public void saveRecord(UserPermission userPermission, CardRegistration_Mdl mrTemp, String voucherPrefix, AsyncCallback<DmResult<String>> result);

    public void getFind(DmHelper pageing, boolean blnToEndOfFile, UserPermission userPermission, String vPrefix, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getTxtHCity(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHCity_Mdl>>> result);

    public void getTxtHPrefix(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHCardHelp(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHLinkAccount(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHCardType(DmHelper helper, boolean blnToEndOfFile, String voucherDate, AsyncCallback<DmResultPageing<List<TxtHCardType_Mdl>>> result);

    public void getUHIDDetails(String uhidDocId, String uhidRecId, AsyncCallback<DmResult<PersonDetail_Mdl>> result);

    public void getDefaultValue(AsyncCallback<DmResult<KeyFieldExtended>> result);

    public void getCardNo(String cardNo, AsyncCallback<DmResult<String>> result);

}
