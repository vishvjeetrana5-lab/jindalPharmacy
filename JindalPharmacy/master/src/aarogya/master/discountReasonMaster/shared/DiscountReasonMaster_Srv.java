package aarogya.master.discountReasonMaster.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface DiscountReasonMaster_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResultPageing<List<String>> getSuggestion(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<DiscountReasonMaster_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(DiscountReasonMaster_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDiscountAccount(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

}
