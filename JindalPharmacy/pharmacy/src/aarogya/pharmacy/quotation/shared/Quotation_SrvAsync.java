package aarogya.pharmacy.quotation.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface Quotation_SrvAsync {

    public void getNavigator(UserPermission userPermission, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<Quotation_Mdl>> result);

    public void deleteRecord(String strKey, String guiConfigCode, AsyncCallback<DmResult<String>> result);

    public void saveRecord(Quotation_Mdl mrTemp, String voucherPrefix, boolean IsCommonPriceList, boolean IsUpdatePriceListFromQuotation, AsyncCallback<DmResult<String>> result);

    public void getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHSupplier(String v_Type, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHSupplier_Mdl>>> result);

    public void getTxtHEnquiryNo(String vendorCode, String v_Date, String quotationDocId, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHEnquiry_Mdl>>> result);

    public void getTxtHItemName(String location, String billDate, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHItemName_Mdl>>> result);

    public void getServerDateTime(AsyncCallback<DmResult<String[]>> result);

    public void getDefaultValues(AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void getItemDetail(String enquiryDocId, List<KeyFieldExtended> keyFields, AsyncCallback<DmResult<List<GrdItemDetail_Mdl>>> result);

    public void getIndentDetail(List<KeyFieldExtended> keyFields, String itemCode, AsyncCallback<DmResult<List<GrdIndentDetail_Mdl>>> result);

    public void getListFromCSV(String serverDirectory, String serverFileName,String location, AsyncCallback<DmResult<Quotation_Mdl>> result);

    public void exportCSV(String docid, AsyncCallback<DmResult<String>> result);

    public void getCDConfiguration(String supplierCode, String medicineCode, AsyncCallback<DmResult<String>> asyncCallback);

    public void getITS(String itemCode, String itemRate, String location, String itsBasedOn, String billDate, AsyncCallback<DmResult<List<OverHeadITS_Mdl>>> result);

}
