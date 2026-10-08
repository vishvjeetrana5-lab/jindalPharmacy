package aarogya.pharmacy.saleBillWholeSale.shared;

import aarogya.pharmacy.partySearch.shared.PartySearch_Mdl;
import aarogya.pharmacy.self.shared.BarCode_Mdl;
import dataman.dmToolkit.modifyReason.shared.ModifyReason_Mdl;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import java.util.List;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import dataman.dmToolkit.eGstPopup.shared.IEGstPopup_SrvAsync;
import dataman.dmToolkit.gst.shared.MasterEGst_Mdl;
import dataman.dmToolkit.partyDetailPopup.shared.PartyDetailPopup_Mdl;

public interface SaleBillWholeSale_SrvAsync extends IEGstPopup_SrvAsync {

    public void getNavigator(String voucherCategory, UserPermission userPermission, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<SaleBillWholeSale_Mdl>> result);

    public void deleteRecord(String strKey, ModifyReason_Mdl reason_Mdl, AsyncCallback<DmResult<String>> result);

    public void isItemEverReturned(String itemCode, String ibCode, String prvQty, String prvFreeQty, AsyncCallback<DmResult<String>> result);

    public void saveRecord(String voucherCategory, SaleBillWholeSale_Mdl mrTemp, SaleBillWholeSale_Mdl prevMdl, String voucherPrefix, AsyncCallback<DmResult<String>> result);

    public void getFind(String voucherCategory, UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getTxtHVType(String voucherCategory, String voucherCategory1, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHVType_Mdl>>> result);

    public void getTxtHVendor(String voucherCategory, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHVendor_Mdl>>> result);

    public void getDefaultVal(String voucherCategory, AsyncCallback<DmResult<Default_Mdl>> result);

    public void getServerDateTime(AsyncCallback<String[]> result);

    public void getTxtHOperation(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHCustomerType(DmHelper helper, boolean blnToEndOfFile, boolean vendorAllow, boolean walkInAllow, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHConsutant(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getITS(String itemCode, double rate, String itsBasedOn, String billDate, String location, AsyncCallback<DmResult<List<OverHeadITS_Mdl>>> result);

    public void getBarCodeSearch(String barCode, String voucherDate, String departmentCode, String siteCode, STEnum.MedicineSaleOn saleOn, AsyncCallback<DmResult<BarCode_Mdl>> result);

    public void getSkuLandedRate(String itemCode, String ibCode, String departmentCode, String v_Date, AsyncCallback<DmResult<String>> result);

    public void fillVendorDetails(String voucherCategory, int recordTypeCode, boolean isTransporter, AsyncCallback<DmResult<PartyDetailPopup_Mdl>> result);

    public void getEgstDetail(String strKey, String vTypeCode, AsyncCallback<DmResult<MasterEGst_Mdl>> result);

    public void getTxtHTransportMode(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHTransporter(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHVendor_Mdl>>> asyncCallback);

    public void getHYesNo(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);

    public void getHVehicleType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);

    public void getTxtHLocation(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);

    public void setApproval(String approvedBy, boolean isApprove, String gatePassDocId,String docId, AsyncCallback<DmResult<String>> result);
    
     
}
