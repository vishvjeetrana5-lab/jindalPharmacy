package aarogya.master.earningDeductionEntry.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.UserPermission;
import java.util.List;

public interface EarningDeductionEntry_SrvAsync {

    public void getFind(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void getNavigator(AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<EarningDeductionEntry_Mdl>> asyncCallback);

    public void saveRecord(EarningDeductionEntry_Mdl mrTemp, String voucherPrefix, AsyncCallback<DmResult<String>> asyncCallback);

    public void getTxtHSalaryPeriod(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);

    public void getTxtHCName(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);

    public void getTxtHDescription(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);

    public void getTxtHType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);

    public void getTxtHNature(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);

    public void getInvestigationDetail(AsyncCallback<DmResult<List<EarningDeductionEntryGridEdit_Mdl>>> result);

    public void getTxtHCopyFrom(String employeeCode, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<CopyFrom_Mdl>>> asyncCallback);

    public void getGridDetail(String key, AsyncCallback<DmResult<List<EarningDeductionEntryGridEdit_Mdl>>> asyncCallback);

}
