package dataman.utility.overheadConfiguration.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface OverheadConfiguration_SrvAsync {

    public void getNavigator(AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<OverheadConfiguration_Mdl>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void saveRecord(OverheadConfiguration_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void getFind(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find>>> result);

    public void getTxtHVoucherType(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHVoucherType_Mdl>>> result);

    public void getTxtHTableAffected(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHNature(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getYesNo(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtAtRateAmount(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHRoundOff(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHDrCr(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHOverhead(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHOverhead_Mdl>>> result);

    public void getTxtHFormula(AsyncCallback<DmResult<List<TxtHFormula_Mdl>>> result);

    public void getTxtHPostAC(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHPostAC_Mdl>>> result);

    public void getTxtHContraExp(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHContraExp_Mdl>>> result);

    public void getTxtHOHSetup(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHOHSetup_Mdl>>> result);

    public void fillGridApplicableDate(String code, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void fillGrid(String code, String applicableFromDate, AsyncCallback<DmResult<List<GrdOverhead_Mdl>>> result);

    public void deleteRecordApplicable(String strKey, String applicableFromDate, AsyncCallback<DmResult<String>> result);

    public void getTxtHSite(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHAtRateSaveIn(String tableName, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHAmtSaveIn(String tableName, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHApplicableFrom(String code, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getMaxApplicableDate(String code, AsyncCallback<DmResult<KeyFieldExtended>> result);

    public void exportJson(String sessionId, OverheadConfiguration_Mdl mrTemp, AsyncCallback<DmResult<KeyFieldExtended>> result);

    public void importJson(String filePath, String fileName, AsyncCallback<DmResult<OverheadConfiguration_Mdl>> result);

    public void checkImportedValues(OverheadConfiguration_Mdl mrTemp, AsyncCallback<DmResult<OverheadConfiguration_Mdl>> result);
}
