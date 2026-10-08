package aarogya.master.cardRecharge.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.selfToolkit.stStatic.shared.RegisteredWallet_Mdl;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface CardRecharge_SrvAsync {

    public void getNavigator(UserPermission userPermission, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<CardRecharge_Mdl>> result);

    public void deleteRecord(UserPermission userPermission, String strKey, String V_Date, AsyncCallback<DmResult<String>> result);

    public void saveRecord(CardRecharge_Mdl mrTemp, String voucherPrefix, UserPermission userPermission, AsyncCallback<DmResult<String>> result);

    public void getFind(UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getTxtHCardNo(String voucherDate, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<RegisteredWallet_Mdl>>> result);

    public void getDefaultValues(AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void getCardDetail(String cardNo, String physicalId, String docId, AsyncCallback<DmResult<RegisteredWallet_Mdl>> result);
}
