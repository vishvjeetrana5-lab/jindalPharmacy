package aarogya.master.cardDeactive.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.selfToolkit.stStatic.shared.RegisteredWallet_Mdl;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface CardDeactivate_SrvAsync {

    public void getDefaultValues(AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void saveRecord(CardDeactivate_Mdl mrTemp, String voucherPrefix, AsyncCallback<DmResult<String>> result);

    public void getNavigator(UserPermission userPermission, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getFind(UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<CardDeactivate_Mdl>> result);

    public void getTxtHVrType(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getCardDetail(String cardNo, String physicalId, String docId, AsyncCallback<DmResult<RegisteredWallet_Mdl>> result);

    public void getTxtHCardNo(String voucherDate, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<RegisteredWallet_Mdl>>> result);

}
