package aarogya.pharmacy.ledgerPopup.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface LedgerPopup_Srv extends RemoteService {

    public DmResultPageing<List<TxtHAccountGroup_Mdl>> getTxtHAccountGroup(String acGroupCustomerCode, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHCity_Mdl>> getTxtHCity(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHNature(String acGroupCustomerCode, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHPartyType(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<LedgerPopup_Mdl> saveRecord(LedgerPopup_Mdl mrTemp);

    public DmResult<LedgerPopup_Mdl> getDefaltValues();
}
