package aarogya.inventory.quotation.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.gsts.shared.GSTS_Mdl;
import java.util.List;

public interface Quotation_SrvAsync {

    public void getTempKeyCode(AsyncCallback<String> result);

    public void getNavigator(UserPermission userPermission, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<Quotation_Mdl>> result);

    public void deleteRecord(String strKey, String guiConfigCode, AsyncCallback<DmResult<String>> result);

    public void saveRecord(Quotation_Mdl mrTemp, String voucherPrefix,boolean IsCommonPriceList,boolean isUpdatePriceListFromQuotation, AsyncCallback<DmResult<String>> result);

    public void getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHSupplier(String voucherCategory, boolean flagFilterByVoucherCategory, String v_Type, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHSupplier_Mdl>>> result);

    public void getSupplierDetail(String vendorCode, String voucherCategory, String v_Type, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResult<TxtHSupplier_Mdl>> result);

    public void getTxtHEnquiryNo(String vendorCode, String v_Date, String quotationDocId, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHEnquiry_Mdl>>> result);

    public void getTxtHItemName(String v_Type, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHItemName_Mdl>>> result);

    public void getServerDateTime(AsyncCallback<DmResult<String[]>> result);

    public void getDefaultValues(AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void getItemDetail(String v_Type, String enquiryDocId, String location, String isStateOrUt, List<KeyFieldExtended> keyFields, AsyncCallback<DmResult<List<GrdItemDetail_Mdl>>> result);

    public void getIndentDetail(List<KeyFieldExtended> keyFields, String itemCode, AsyncCallback<DmResult<List<GrdIndentDetail_Mdl>>> result);

    public void getEnquiryDetail(KeyFieldExtended keyField, String v_Date, String quotationDocId, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResult<List<TxtHEnquiry_Mdl>>> result);

    public void getTxtHRCMApplicable(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);

    public void getTxtHGstType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);

    public void getGSTS(String itemCode, String itemRate, String itsBasedOn, String location, String gstTypeCode, boolean manualITS, AsyncCallback<DmResult<List<GSTS_Mdl>>> asyncCallback);
    
    public void getListFromCSV(String serverDirectory, String serverFileName, String location, String isStateOrUt, AsyncCallback<DmResult<Quotation_Mdl>> result);

    public void exportCSV(String docid,AsyncCallback<DmResult<String>> result);
}
