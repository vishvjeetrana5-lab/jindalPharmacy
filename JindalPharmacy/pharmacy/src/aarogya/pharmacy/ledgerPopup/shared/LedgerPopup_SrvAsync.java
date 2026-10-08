package aarogya.pharmacy.ledgerPopup.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface LedgerPopup_SrvAsync {

    public void getTxtHAccountGroup(String acGroupCustomerCode, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHAccountGroup_Mdl>>> result);

    public void getTxtHCity(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHCity_Mdl>>> result);

    public void getTxtHNature(String acGroupCustomerCode, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHPartyType(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void saveRecord(LedgerPopup_Mdl mrTemp, AsyncCallback<DmResult<LedgerPopup_Mdl>> result);

    public void getDefaltValues(AsyncCallback<DmResult<LedgerPopup_Mdl>> result);

}
