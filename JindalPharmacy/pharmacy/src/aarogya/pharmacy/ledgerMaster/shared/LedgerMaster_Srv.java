package aarogya.pharmacy.ledgerMaster.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface LedgerMaster_Srv extends RemoteService {

    public DmResultPageing<List<TxtHCity_Mdl>> getTxtHCity(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHIdType(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHInitial(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHIsActive(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<LedgerMaster_Mdl>> getFind(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResult<LedgerMaster_Mdl> saveRecord(LedgerMaster_Mdl mrTemp);

    public DmResult<LedgerMaster_Mdl> getMoveRec(String strKey);
}
