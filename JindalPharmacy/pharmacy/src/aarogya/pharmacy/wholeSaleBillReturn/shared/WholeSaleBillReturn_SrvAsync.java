
package aarogya.pharmacy.wholeSaleBillReturn.shared;

import aarogya.pharmacy.partySearch.shared.PartySearch_Mdl;
import aarogya.pharmacy.self.shared.BarCode_Mdl;
import dataman.dmToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmToolkit.eGstPopup.shared.IEGstPopup_SrvAsync;
import dataman.dmToolkit.gst.shared.MasterEGst_Mdl;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import dataman.dmToolkit.partyDetailPopup.shared.PartyDetailPopup_Mdl;
import java.util.List;


public interface WholeSaleBillReturn_SrvAsync extends IEGstPopup_SrvAsync {
    
    public void getNavigator(String voucherCategory, UserPermission userPermission, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<WholeSaleBillReturn_Mdl>> result);

    public void deleteRecord(String strKey, ModifyReason_Mdl reason_Mdl, String sbDocId, String voucherShortName, AsyncCallback<DmResult<String>> result);

    public void getBalanceSaleBill(String lmCode, AsyncCallback<DmResult<String>> result);

    public void saveRecord(String voucherCategory, WholeSaleBillReturn_Mdl mrTemp, WholeSaleBillReturn_Mdl prevMdl, String voucherPrefix,String returnRequestDocID, boolean saveCardInfo,boolean modifyReasonRequired, AsyncCallback<DmResult<String>> result);

    public void getFind(String voucherCategory, UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getTxtHVType(UserPermission userPermission, String voucherCategory, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHVType_Mdl>>> result);

    public void getUHIDDetails(SearchUHID_Mdl searchMdl, AsyncCallback<DmResult<WholeSaleBillReturn_Mdl>> result);

    public void getDefaultVal(String voucherCategory, UserPermission userPermission, AsyncCallback<DmResult<Default_Mdl>> result);

    public void getServerDateTime(AsyncCallback<String[]> result);

    public void getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHSaleBillNo(DmHelper helper, String paymentSource ,String partySubcode,boolean blnToEndOfFile, String departmentCode, String voucherDate, String docIdSaleReturn, String pharmacyReturnDays, String vTypeCode, AsyncCallback<DmResultPageing<List<TxtHSaleBillNo_Mdl>>> result);

    public void getTxtHConsutant(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getITS(String itemCode, double rate, String itsBasedOn, String billDate, AsyncCallback<DmResult<List<OverHeadITS_Mdl>>> result);

    public void getRoundOffAcCode(String vDate, AsyncCallback<DmResult<String>> result);

    public void getSaleBillItemDetails(List<KeyFieldExtended> keyFields, String v_Date, String departmentCode, AsyncCallback<DmResult<List<GrdMedicine_Mdl>>> result);

    public void getLedger(String manualCode, String v_Date, AsyncCallback<DmResult<PartySearch_Mdl>> result);

    public void getTxtHReason(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getBarCodeSearch(String barCode, String voucherDate, String departmentCode, String siteCode,STEnum.MedicineSaleOn saleOn, AsyncCallback<DmResult<BarCode_Mdl>> result);

    public void getMedicineReturnRequests (String departmentCode, DmHelper helper, boolean blnToEndOfFile,AsyncCallback<DmResultPageing<List<MedicineReturnRequest_Mdl>>> result) ;
    
    public void  getReturnRequestDetail(String returnRequestDocID,AsyncCallback<DmResult<List<KeyFieldExtended>>> DmResult);
    
    public void getrefunddocId(String refunddocId, AsyncCallback<DmResult<Boolean>> result);
    
    public void isEodExist(String entryDate, AsyncCallback<DmResult<Boolean>> result) ;
    
    public void getTxtHDebtorsName(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getEgstDetail(String strKey, String vTypeCode, AsyncCallback<DmResult<MasterEGst_Mdl>> result);

    public void getTxtHTransportMode(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHTransporter(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHVendor_Mdl>>> asyncCallback);

    public void getHVehicleType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);

    public void getTxtHLocation(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);

    public void fillVendorDetails(String voucherCategory, int recordTypeCode, AsyncCallback<DmResult<PartyDetailPopup_Mdl>> result);

    public void processAutoVoucherAdjustment(String strKey, String partyCode, AsyncCallback<DmResult<String>> result);
    
}
