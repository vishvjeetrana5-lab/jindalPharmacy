package aarogya.pharmacy.wholeSaleBillReturn.shared;

import aarogya.pharmacy.partySearch.shared.PartySearch_Mdl;
import aarogya.pharmacy.self.shared.BarCode_Mdl;
import dataman.dmToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmToolkit.eGstPopup.shared.IEGstPopup_Srv;
import dataman.dmToolkit.gst.shared.MasterEGst_Mdl;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import dataman.dmToolkit.partyDetailPopup.shared.PartyDetailPopup_Mdl;
import java.util.List;

public interface WholeSaleBillReturn_Srv extends RemoteService, IEGstPopup_Srv {

    public DmResult<List<KeyFieldExtended>> getNavigator(String voucherCategory, UserPermission userPermission);

    public DmResult<WholeSaleBillReturn_Mdl> getMoveRec(String strKey);

    public DmResult<String> deleteRecord(String strKey, ModifyReason_Mdl reason_Mdl, String sbDocId, String voucherShortName);

    public DmResult<String> getBalanceSaleBill(String lmCode);

    public DmResult<String> saveRecord(String voucherCategory, WholeSaleBillReturn_Mdl mrTemp, WholeSaleBillReturn_Mdl prevMdl, String voucherPrefix, String returnRequestDocID, boolean saveCardInfo, boolean modifyReasonRequired);

    public DmResultPageing<List<Find_Mdl>> getFind(String voucherCategory, UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHVType_Mdl>> getTxtHVType(UserPermission userPermission, String voucherCategory, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHSaleBillNo_Mdl>> getTxtHSaleBillNo(DmHelper helper, String paymentSource, String partySubcode, boolean blnToEndOfFile, String departmentCode, String voucherDate, String docIdSaleReturn, String pharmacyReturnDays, String vTypeCode);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHConsutant(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<WholeSaleBillReturn_Mdl> getUHIDDetails(SearchUHID_Mdl searchMdl);

    public DmResult<Default_Mdl> getDefaultVal(String voucherCategory, UserPermission userPermission);

    public String[] getServerDateTime();

    public DmResult<List<OverHeadITS_Mdl>> getITS(String itemCode, double rate, String itsBasedOn, String billDate);

    public DmResult<List<GrdMedicine_Mdl>> getSaleBillItemDetails(List<KeyFieldExtended> keyFields, String v_Date, String departmentCode);

    public DmResult<String> getRoundOffAcCode(String vDate);

    public DmResult<PartySearch_Mdl> getLedger(String manualCode, String v_Date);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHReason(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<BarCode_Mdl> getBarCodeSearch(String barCode, String voucherDate, String departmentCode, String siteCode, STEnum.MedicineSaleOn saleOn);

    public DmResultPageing<List<MedicineReturnRequest_Mdl>> getMedicineReturnRequests(String departmentCode, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<List<KeyFieldExtended>> getReturnRequestDetail(String returnRequestDocID);

    public DmResult<Boolean> getrefunddocId(String refunddocId);

    public DmResult<Boolean> isEodExist(String entryDate);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDebtorsName(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<MasterEGst_Mdl> getEgstDetail(String strKey, String vTypeCode);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHTransportMode(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHVendor_Mdl>> getTxtHTransporter(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHLocation(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getHVehicleType(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<PartyDetailPopup_Mdl> fillVendorDetails(String voucherCategory, int recordTypeCode);

    public DmResult<String> processAutoVoucherAdjustment(String strKey, String partyCode);

}
