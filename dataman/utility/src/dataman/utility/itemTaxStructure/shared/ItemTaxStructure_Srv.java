package dataman.utility.itemTaxStructure.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface ItemTaxStructure_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResult<ItemTaxStructure_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(ItemTaxStructure_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<KeyFieldExtended>> getSite(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getAccountAffected(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getOverhead(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHLocation(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHCopyFromITS(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<KeyFieldExtended> getMaxApplicableDate(String code);

    public DmResult<String> deleteRecordApplicable(String strKey, String applicableFromDate);

    public DmResult<List<GrdItemTaxStructure_Mdl>> fillGrid(String code, String applicableFromDate);

    public DmResult<List<KeyFieldExtended>> fillGridApplicableDate(String code);

    public DmResult<KeyFieldExtended> exportJson(String sessionId, List<String> lstITS);

    public DmResult<List<GrdExport_Mdl>> getITS();

    public DmResultPageing<List<KeyFieldExtended>> getNature(DmHelper pageing, boolean blnToEndOfFile);

}
