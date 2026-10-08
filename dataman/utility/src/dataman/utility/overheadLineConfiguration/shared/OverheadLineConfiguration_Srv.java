package dataman.utility.overheadLineConfiguration.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface OverheadLineConfiguration_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResult<OverheadLineConfiguration_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(OverheadLineConfiguration_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHVoucherType_Mdl>> getTxtHVoucherType(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHTableAffected(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHNature(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getYesNo(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHRoundOff(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHACSave(String tableName, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHOverhead_Mdl>> getTxtHOverhead(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<List<TxtHFormula_Mdl>> getTxtHFormula();

    public DmResultPageing<List<KeyFieldExtended>> getTxtHOverheadSaveIn(String tableName, DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<List<KeyFieldExtended>> fillGridApplicableDate(String code);

    public DmResult<List<GrdOverheadLine_Mdl>> fillGrid(String code, String applicableFromDate);

    public DmResult<String> deleteRecordApplicable(String strKey, String applicableFromDate);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHSite(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHAtRateSaveIn(String tableName, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHAmtSaveIn(String tableName, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHPostAC_Mdl>> getTxtHPostAC(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHApplicableFrom(String code, DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<KeyFieldExtended> getMaxApplicableDate(String code);

    public DmResultPageing<List<KeyFieldExtended>> getTxtAtRateAmount(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<KeyFieldExtended> exportJson(String sessionId, OverheadLineConfiguration_Mdl mrTemp);

    public DmResult<OverheadLineConfiguration_Mdl> importJson(String filePath, String fileName);

    public DmResult<OverheadLineConfiguration_Mdl> checkImportedValues(OverheadLineConfiguration_Mdl mrTemp);

}
