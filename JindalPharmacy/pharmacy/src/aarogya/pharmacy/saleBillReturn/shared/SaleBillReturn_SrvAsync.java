package aarogya.pharmacy.saleBillReturn.shared;

import aarogya.pharmacy.partySearch.shared.PartySearch_Mdl;
import aarogya.pharmacy.self.shared.BarCode_Mdl;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface SaleBillReturn_SrvAsync {

    public void getNavigator(String voucherCategory, UserPermission userPermission, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<SaleBillReturn_Mdl>> result);

    public void deleteRecord(String strKey, ModifyReason_Mdl reason_Mdl, String regDocid, String sbDocId, String voucherShortName, AsyncCallback<DmResult<String>> result);

    public void getBalanceSaleBill(String lmCode, AsyncCallback<DmResult<String>> result);

    public void saveRecord(String voucherCategory, SaleBillReturn_Mdl mrTemp, SaleBillReturn_Mdl prevMdl, String voucherPrefix,String returnRequestDocID, boolean saveCardInfo,boolean modifyReasonRequired, AsyncCallback<DmResult<String>> result);

    public void getFind(String voucherCategory, UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getTxtHVType(UserPermission userPermission, String voucherCategory, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHVType_Mdl>>> result);

    public void getUHIDDetails(SearchUHID_Mdl searchMdl, AsyncCallback<DmResult<SaleBillReturn_Mdl>> result);

    public void getDefaultVal(String voucherCategory, UserPermission userPermission, AsyncCallback<DmResult<Default_Mdl>> result);

    public void getServerDateTime(AsyncCallback<String[]> result);

    public void getTxtHIdType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHCustomerType(DmHelper helper, boolean blnToEndOfFile, boolean opdAllow, boolean ipdAllow, boolean dayCareAllow, boolean emergencyAllow, boolean walkInAllow, boolean ledgerAllow, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHSaleBillNo(DmHelper helper, String paymentSource ,boolean blnToEndOfFile, String departmentCode, String regDocId, String voucherDate, String docIdSaleReturn, String lmCode, String typeCode, String pharmacyReturnDays, String vTypeCode, String uhidDocID, AsyncCallback<DmResultPageing<List<TxtHSaleBillNo_Mdl>>> result);

    public void getTxtHConsutant(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getITS(String itemCode, double rate, String itsBasedOn, String billDate, AsyncCallback<DmResult<List<OverHeadITS_Mdl>>> result);

    public void getRoundOffAcCode(String vDate, AsyncCallback<DmResult<String>> result);

    public void getTxtHCity(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHCity_Mdl>>> result);

    public void getSaleBillItemDetails(List<KeyFieldExtended> keyFields, String v_Date, String departmentCode, AsyncCallback<DmResult<List<GrdMedicine_Mdl>>> result);

    public void getLedger(String manualCode, String v_Date, AsyncCallback<DmResult<PartySearch_Mdl>> result);

    public void getPharmacyBalance(String lmCode, AsyncCallback<DmResult<String>> result);

    public void getTxtHReason(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getBarCodeSearch(String barCode, String voucherDate, String departmentCode, String siteCode,STEnum.MedicineSaleOn saleOn, AsyncCallback<DmResult<BarCode_Mdl>> result);

    public void getMedicineReturnRequests (String departmentCode, DmHelper helper, boolean blnToEndOfFile,AsyncCallback<DmResultPageing<List<MedicineReturnRequest_Mdl>>> result) ;
    
    public void  getReturnRequestDetail(String returnRequestDocID,AsyncCallback<DmResult<List<KeyFieldExtended>>> DmResult);
    
    public void getrefunddocId(String refunddocId, AsyncCallback<DmResult<Boolean>> result);
    
    public void isEodExist(String entryDate, AsyncCallback<DmResult<Boolean>> result) ;
}
