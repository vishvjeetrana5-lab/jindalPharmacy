package aarogya.pharmacy.saleBillReturn.shared;

import aarogya.pharmacy.partySearch.shared.PartySearch_Mdl;
import aarogya.pharmacy.self.shared.BarCode_Mdl;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface SaleBillReturn_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator(String voucherCategory, UserPermission userPermission);

    public DmResult<SaleBillReturn_Mdl> getMoveRec(String strKey);

    public DmResult<String> deleteRecord(String strKey, ModifyReason_Mdl reason_Mdl, String regDocid, String sbDocId, String voucherShortName);

    public DmResult<String> getBalanceSaleBill(String lmCode);

    public DmResult<String> saveRecord(String voucherCategory, SaleBillReturn_Mdl mrTemp, SaleBillReturn_Mdl prevMdl, String voucherPrefix, String returnRequestDocID, boolean saveCardInfo, boolean modifyReasonRequired);

    public DmResultPageing<List<Find_Mdl>> getFind(String voucherCategory, UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHVType_Mdl>> getTxtHVType(UserPermission userPermission, String voucherCategory, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHIdType(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHCustomerType(DmHelper helper, boolean blnToEndOfFile, boolean opdAllow, boolean ipdAllow, boolean dayCareAllow, boolean emergencyAllow, boolean walkInAllow, boolean ledgerAllow);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHSaleBillNo_Mdl>> getTxtHSaleBillNo(DmHelper helper, String paymentSource , boolean blnToEndOfFile, String departmentCode, String regDocId, String voucherDate, String docIdSaleReturn, String lmCode, String typeCode, String pharmacyReturnDays, String vTypeCode, String uhidDocID);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHConsutant(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<SaleBillReturn_Mdl> getUHIDDetails(SearchUHID_Mdl searchMdl);

    public DmResult<Default_Mdl> getDefaultVal(String voucherCategory, UserPermission userPermission);

    public String[] getServerDateTime();

    public DmResult<List<OverHeadITS_Mdl>> getITS(String itemCode, double rate, String itsBasedOn, String billDate);

    public DmResult<List<GrdMedicine_Mdl>> getSaleBillItemDetails(List<KeyFieldExtended> keyFields, String v_Date, String departmentCode);

    public DmResult<String> getRoundOffAcCode(String vDate);

    public DmResultPageing<List<TxtHCity_Mdl>> getTxtHCity(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<PartySearch_Mdl> getLedger(String manualCode, String v_Date);

    public DmResult<String> getPharmacyBalance(String lmCode);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHReason(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<BarCode_Mdl> getBarCodeSearch(String barCode, String voucherDate, String departmentCode, String siteCode,STEnum.MedicineSaleOn saleOn);
    
    public DmResultPageing<List<MedicineReturnRequest_Mdl>> getMedicineReturnRequests(String departmentCode, DmHelper helper, boolean blnToEndOfFile);
    
    public DmResult<List<KeyFieldExtended>> getReturnRequestDetail(String returnRequestDocID);
    
    public DmResult<Boolean> getrefunddocId(String refunddocId);
    
    public DmResult<Boolean> isEodExist(String entryDate);
}
