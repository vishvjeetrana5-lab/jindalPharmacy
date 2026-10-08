package dataman.utility.overheadConfiguration.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface OverheadConfiguration_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResult<OverheadConfiguration_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(OverheadConfiguration_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<Find>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHVoucherType_Mdl>> getTxtHVoucherType(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHTableAffected(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHNature(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getYesNo(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtAtRateAmount(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHRoundOff(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDrCr(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHOverhead_Mdl>> getTxtHOverhead(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<List<TxtHFormula_Mdl>> getTxtHFormula();

    public DmResultPageing<List<TxtHPostAC_Mdl>> getTxtHPostAC(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHContraExp_Mdl>> getTxtHContraExp(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHOHSetup_Mdl>> getTxtHOHSetup(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<List<KeyFieldExtended>> fillGridApplicableDate(String code);

    public DmResult<List<GrdOverhead_Mdl>> fillGrid(String code, String applicableFromDate);

    public DmResult<String> deleteRecordApplicable(String strKey, String applicableFromDate);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHSite(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHAtRateSaveIn(String tableName, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHAmtSaveIn(String tableName, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHApplicableFrom(String code, DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<KeyFieldExtended> getMaxApplicableDate(String code);

    public DmResult<KeyFieldExtended> exportJson(String sessionId, OverheadConfiguration_Mdl mrTemp);

    public DmResult<OverheadConfiguration_Mdl> importJson(String filePath, String fileName);

    public DmResult<OverheadConfiguration_Mdl> checkImportedValues(OverheadConfiguration_Mdl mrTemp);
}
