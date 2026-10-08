package dataman.utility.numberSystem.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface NumberSystem_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResult<NumberSystem_Mdl> getMoveRec(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<List<GrdNumberFormat_Mdl>> fillGrid(String vTypeCode);

    public DmResult<List<KeyFieldExtended>> fillType(String voucherType);

    public DmResult<String> saveRecord(NumberSystem_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHSite(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHIsYesNo(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHVoucherType_Mdl>> getTxtHNSVoucherType(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHVoucherType_Mdl>> getTxtHVoucherType(String vTypeCode, DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<List<KeyFieldExtended>> fillNumberFormat();

    public DmResult<List<GrdCounter_Mdl>> getCountList(String strKey);

    public DmResult<String> deleteRecordPopUp(GrdCounter_Mdl grdCounter_Mdl);

    public DmResult<String> chkNumberFormat(String numberFormat, List<KeyFieldExtended> numberFormatLst);

    public DmResult<String> InsertRecordPopUp(GrdCounter_Mdl grdCounter_Mdl);

    public DmResult<KeyFieldExtended> exportDataToCSV(String session);
}
