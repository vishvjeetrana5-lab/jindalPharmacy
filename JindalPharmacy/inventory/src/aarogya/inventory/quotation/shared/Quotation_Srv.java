package aarogya.inventory.quotation.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.gsts.shared.GSTS_Mdl;
import java.util.List;

public interface Quotation_Srv extends RemoteService {

    public String getTempKeyCode();

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission);

    public DmResult<Quotation_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(Quotation_Mdl mrTemp, String voucherPrefix,boolean IsCommonPriceList,boolean isUpdatePriceListFromQuotation);

    public DmResult<String> deleteRecord(String strKey, String guiConfigCode);

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHSupplier_Mdl>> getTxtHSupplier(String voucherCategory, boolean flagFilterByVoucherCategory, String v_Type, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<TxtHSupplier_Mdl> getSupplierDetail(String vendorCode, String voucherCategory, String v_Type, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHEnquiry_Mdl>> getTxtHEnquiryNo(String vendorCode, String v_Date, String quotationDocId, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHItemName_Mdl>> getTxtHItemName(String v_Type, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<String[]> getServerDateTime();

    public DmResult<DefaultValue_Mdl> getDefaultValues();

    public DmResult<List<GrdItemDetail_Mdl>> getItemDetail(String v_Type, String enquiryDocId,String location, String isStateOrUt, List<KeyFieldExtended> keyFields);

    public DmResult<List<GrdIndentDetail_Mdl>> getIndentDetail(List<KeyFieldExtended> keyFields, String itemCode);

    public DmResult<List<TxtHEnquiry_Mdl>> getEnquiryDetail(KeyFieldExtended keyField, String v_Date, String quotationDocId, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHRCMApplicable(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHGstType(DmHelper pageing, boolean blnToEndOfFile);
    
    public DmResult<List<GSTS_Mdl>> getGSTS(String itemCode, String itemRate, String itsBasedOn, String locationCode, String gstTypeCode, boolean isManualGst);
    
    public DmResult<Quotation_Mdl> getListFromCSV(String serverDirectory, String serverFileName, String location, String isStateOrUt);

    public DmResult<String> exportCSV(String docid);

}
