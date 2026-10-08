package aarogya.pharmacy.saleBill.shared;

import aarogya.pharmacy.partySearch.shared.PartySearch_Mdl;
import aarogya.pharmacy.self.shared.BarCode_Mdl;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import aarogya.selfToolkit.uhidPopup.shared.UhidPopup_Mdl;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface SaleBill_Srv extends RemoteService {

    public DmResultPageing<List<TxtHOldOpd_Mdl>> getTxtHOldOpd(String uhid, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<List<KeyFieldExtended>> getNavigator(String voucherCategory, UserPermission userPermission);

    public DmResult<SaleBill_Mdl> getMoveRec(String strKey);

    public DmResult<String> deleteRecord(String voucherCategory, String strKey, ModifyReason_Mdl reason_Mdl, String regDocid);

    public DmResult<String> isItemEverReturned(String itemCode, String ibCode, String regDocid, String uhid, String lmCode, String prvQty, String prvFreeQty);

    public DmResult<String> saveRecord(String voucherCategory, SaleBill_Mdl mrTemp, SaleBill_Mdl prevMdl, String voucherPrefix);

    public DmResultPageing<List<Find_Mdl>> getFind(String voucherCategory, UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHVType_Mdl>> getTxtHVType(UserPermission userPermission, String voucherCategory, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHIdType(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHOperation(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHMedicine(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHCustomerType(DmHelper helper, boolean blnToEndOfFile, boolean opdAllow, boolean ipdAllow, boolean dayCareAllow, boolean emergencyAllow, boolean walkInAllow, boolean ledgerAllow, boolean UhidAllow);

    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHRemark(DmHelper helper, boolean blnToEndOfFile);

    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHRequisition_Mdl>> getTxtHRequisition(DmHelper helper, boolean blnToEndOfFile, String departmentCode, String regDocId, String voucherDate);

    public DmResultPageing<List<TxtHEstimate_Mdl>> getTxtHEstimate(DmHelper helper, boolean blnToEndOfFile, String departmentCode, String regDocId, String voucherDate, String lmCode, String backDaysForEstimateInSaleBill);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHConsutant(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<SaleBill_Mdl> getUHIDDetails(SearchUHID_Mdl searchMdl, boolean linkParentDatabase);

    public DmResult<Default_Mdl> getDefaultVal(String voucherCategory, UserPermission userPermission);

    public DmResult<String[]> getServerDateTime();

    public DmResultPageing<List<TxtHCity_Mdl>> getTxtHCity(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<List<OverHeadITS_Mdl>> getITS(String itemCode, double rate, String itsBasedOn, String billDate);

    public DmResult<List<GrdMedicine_Mdl>> getMedicineQty(List<GrdMedicine_Mdl> grdMediDetailLst, String totalDays, String vDate, String departmentCode, String pharmacyDiscountPer, STEnum.MedicineSaleOn saleOn, boolean isPharmacyDiscount);

    public DmResult<List<GrdMedicine_Mdl>> getRequisitionDetail(String docIdRequisition, String vDate);

    public DmResult<List<GrdMedicine_Mdl>> getEstimateDetail(String docIdEstimate, String vDate, String departmentCode, STEnum.MedicineSaleOn saleOn);

    public DmResult<List<GrdMedicine_Mdl>> getOperationMedicine(String operationCode, String vDate, String departmentCode, STEnum.MedicineSaleOn saleOn, String pharmacyDiscountPer, boolean isPharmacyDiscount);

    public DmResult<List<GrdMedicine_Mdl>> getMedicine(String operationCode, String vDate, String departmentCode, STEnum.MedicineSaleOn saleOn, String pharmacyDiscountPer, boolean isPharmacyDiscount);

//    public DmResultPageing<List<TxtHMedicine_Mdl>> getTxtHMedicine(String restrictedDepartment, DmHelper helper, boolean blnToEndOfFile);
    public DmResult<String> getRoundOffAcCode(String vDate);

    public DmResult<String> getPharmacyBalance(String lmCode);

    public DmResult<List<GrdMedicine_Mdl>> getRequisitionMedicineDetails(List<KeyFieldExtended> keyFields, String v_Date, String departmentCode, String pharmacyDiscountPer, STEnum.MedicineSaleOn saleOn, boolean isPharmacyDiscount, boolean requisitionWithoutUHID);

    public DmResult<PartySearch_Mdl> getLedger(String manualCode, String v_Date);

    public DmResult<BarCode_Mdl> getBarCodeSearch(String barCode, String voucherDate, String departmentCode, String siteCode, STEnum.MedicineSaleOn saleOn);

    public DmResult<Boolean> isEodExist(String entryDate);

    public DmResult<String> getSkuLandedRate(String itemCode, String ibCode, String departmentCode, String v_Date);

    public DmResultPageing<List<UhidPopup_Mdl>> getTxtHUHID(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<List<GrdMedicine_Mdl>> getPrescMedicineDetail(String opdNo, String consultantCode);
}
