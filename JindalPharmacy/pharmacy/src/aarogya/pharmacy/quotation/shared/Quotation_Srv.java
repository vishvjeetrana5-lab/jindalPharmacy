package aarogya.pharmacy.quotation.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface Quotation_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission);

    public DmResult<Quotation_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(Quotation_Mdl mrTemp, String voucherPrefix, boolean IsCommonPriceList, boolean IsUpdatePriceListFromQuotation);

    public DmResult<String> deleteRecord(String strKey, String guiConfigCode);

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHSupplier_Mdl>> getTxtHSupplier(String v_Type, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHEnquiry_Mdl>> getTxtHEnquiryNo(String vendorCode, String v_Date, String quotationDocId, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHItemName_Mdl>> getTxtHItemName(String location, String billDate, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<String[]> getServerDateTime();

    public DmResult<DefaultValue_Mdl> getDefaultValues();

    public DmResult<List<GrdItemDetail_Mdl>> getItemDetail(String enquiryDocId, List<KeyFieldExtended> keyFields);

    public DmResult<List<GrdIndentDetail_Mdl>> getIndentDetail(List<KeyFieldExtended> keyFields, String itemCode);

    public DmResult<Quotation_Mdl> getListFromCSV(String serverDirectory, String serverFileName,String location);

    public DmResult<String> exportCSV(String docid);

    public DmResult<String> getCDConfiguration(String supplierCode, String medicineCode);

    public DmResult<List<OverHeadITS_Mdl>> getITS(String itemCode, String itemRate, String location, String itsBasedOn, String billDate);

}
