package dataman.utility.numberSystem.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface NumberSystem_SrvAsync {

    public void getNavigator(AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<NumberSystem_Mdl>> result);

    public void getFind(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void fillGrid(String vTypeCode, AsyncCallback<DmResult<List<GrdNumberFormat_Mdl>>> result);

    public void fillType(String voucherType, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void saveRecord(NumberSystem_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void getTxtHSite(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHNSVoucherType(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHVoucherType_Mdl>>> result);

    public void getTxtHVoucherType(String vTypeCode, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHVoucherType_Mdl>>> result);

    public void fillNumberFormat(AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getTxtHIsYesNo(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getCountList(String strKey, AsyncCallback<DmResult<List<GrdCounter_Mdl>>> result);

    public void deleteRecordPopUp(GrdCounter_Mdl grdCounter_Mdl, AsyncCallback<DmResult<String>> result);

    public void chkNumberFormat(String numberFormat, List<KeyFieldExtended> numberFormatLst, AsyncCallback<DmResult<String>> result);

    public void InsertRecordPopUp(GrdCounter_Mdl grdCounter_Mdl, AsyncCallback<DmResult<String>> result);

    public void exportDataToCSV(String session, AsyncCallback<DmResult<KeyFieldExtended>> result);
}
