package aarogya.inventory.serviceOverHeadMaster.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.util.List;

public interface ServiceOverHeadMaster_Srv extends RemoteService {

    public DmResultPageing<List<String>> getSuggestionItem(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<Sku_Mdl>> getSKU(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHCategory_Mdl>> getTxtHCategory(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHGroup(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHHSN_Mdl>> getTxtHHSN(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<ServiceOverHeadMaster_mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(ServiceOverHeadMaster_mdl mrTemp);

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getYesNo(DmHelper pageing, boolean blnToEndOfFile, boolean isActiveCall);

    public DmResultPageing<List<KeyFieldExtended>> getHType(DmHelper pageing, boolean blnToEndOfFile);

}
