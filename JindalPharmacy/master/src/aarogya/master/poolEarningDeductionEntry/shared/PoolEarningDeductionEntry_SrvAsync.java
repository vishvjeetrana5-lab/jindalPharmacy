package aarogya.master.poolEarningDeductionEntry.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import java.util.List;

public interface PoolEarningDeductionEntry_SrvAsync {

    public void getFind(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void getNavigator(AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<PoolEarningDeductionEntry_Mdl>> asyncCallback);

    public void saveRecord(PoolEarningDeductionEntry_Mdl mrTemp, String voucherPrefix, AsyncCallback<DmResult<String>> asyncCallback);

    public void getTxtHSalaryPeriod(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);

    public void getTxtHCName(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);

    public void getTxtHDescription(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);

    public void getTxtHNature(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);

    public void getInvestigationDetail(AsyncCallback<DmResult<List<PoolEarningDeductionEntryGridEdit_Mdl>>> result);

    public void getGridDetail(String key, AsyncCallback<DmResult<List<PoolEarningDeductionEntryGridEdit_Mdl>>> asyncCallback);

    public void getTxtHCopyFrom(String xNull, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<CopyFrom_Mdl>>> asyncCallback);

    public void getTxtHType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);

    public void getTxtHyesNo(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

}
