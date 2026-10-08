package aarogya.master.poolEarningDeductionEntry.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.UserPermission;
import java.util.List;

public interface PoolEarningDeductionEntry_Srv extends RemoteService {

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<String> saveRecord(PoolEarningDeductionEntry_Mdl mrTemp, String voucherPrefix);

    public DmResult<PoolEarningDeductionEntry_Mdl> getMoveRec(String strKey);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHCName(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDescription(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHSalaryPeriod(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResultPageing<List<KeyFieldExtended>> getTxtHNature(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<List<PoolEarningDeductionEntryGridEdit_Mdl>> getInvestigationDetail();

    public DmResultPageing<List<CopyFrom_Mdl>> getTxtHCopyFrom(String employeeCode, DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<List<PoolEarningDeductionEntryGridEdit_Mdl>> getGridDetail(String key);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHType(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHyesNo(DmHelper helper, boolean blnToEndOfFile);

}
