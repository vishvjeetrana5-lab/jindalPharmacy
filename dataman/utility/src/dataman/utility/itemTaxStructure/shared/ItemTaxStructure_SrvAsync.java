package dataman.utility.itemTaxStructure.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface ItemTaxStructure_SrvAsync {

    public void getNavigator(AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<ItemTaxStructure_Mdl>> result);

    public void saveRecord(ItemTaxStructure_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void getSite(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getAccountAffected(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getOverhead(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHLocation(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getFind(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getTxtHCopyFromITS(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getMaxApplicableDate(String code, AsyncCallback<DmResult<KeyFieldExtended>> result);

    public void deleteRecordApplicable(String strKey, String applicableFromDate, AsyncCallback<DmResult<String>> result);

    public void fillGrid(String code, String applicableFromDate, AsyncCallback<DmResult<List<GrdItemTaxStructure_Mdl>>> result);

    public void fillGridApplicableDate(String code, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getITS(AsyncCallback<DmResult<List<GrdExport_Mdl>>> result);

    public void exportJson(String sessionId, List<String> lstITS, AsyncCallback<DmResult<KeyFieldExtended>> result);

    public void getNature(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);

}
