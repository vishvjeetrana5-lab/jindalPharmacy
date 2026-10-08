package dataman.utility.voucherType.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface VoucherType_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResultPageing<List<String>> getSuggestionShortName(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<String>> getSuggestionDescription(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<VoucherType_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(VoucherType_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHModule(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHCategory(DmHelper pageing, boolean blnToEndOfFile, String NCat);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHParentVoucher(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHMenuText_Mdl>> getTxtHMenuText(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDefaultReport(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHDefaultAC_Mdl>> getTxtHDefaultAC(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHYesNO(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHNature(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHFormApplied_Mdl>> getTxtHFormApplied(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHGUITemplate(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtDocumentMailUser(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHTemplate_Mdl>> getTxtHTemplateName(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHPaymentMode_Mdl>> getTxtHPaymentMode(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHIsDefault(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<KeyFieldExtended> exportJson(String sessionId, List<String> lstVoucherType);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDocumentType(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDocumentNature(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHSite(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<List<GrdVoucherType_Mdl>> getVoucherType();

    public DmResultPageing<List<KeyFieldExtended>> getTxtHPageSize(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHItemCategory(DmHelper pageing, boolean blnToEndOfFile);
}
