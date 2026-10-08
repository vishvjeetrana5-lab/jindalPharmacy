package aarogya.master.earningDeductionEntry.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.UserPermission;
import java.util.List;

public interface EarningDeductionEntry_Srv extends RemoteService {

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<String> saveRecord(EarningDeductionEntry_Mdl mrTemp, String voucherPrefix);

    public DmResult<EarningDeductionEntry_Mdl> getMoveRec(String strKey);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHCName(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDescription(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHSalaryPeriod(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResultPageing<List<KeyFieldExtended>> getTxtHType(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<CopyFrom_Mdl>> getTxtHCopyFrom(String employeeCode, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHNature(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<List<EarningDeductionEntryGridEdit_Mdl>> getInvestigationDetail();

    public DmResult<List<EarningDeductionEntryGridEdit_Mdl>> getGridDetail(String key);

}
