package aarogya.pharmacy.saleBill.shared;

import aarogya.pharmacy.partySearch.shared.PartySearch_Mdl;
import aarogya.pharmacy.self.shared.BarCode_Mdl;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import dataman.dmToolkit.self.shared.*;
import aarogya.selfToolkit.uhidPopup.shared.UhidPopup_Mdl;
import java.util.List;
import aarogya.selfToolkit.stStatic.shared.STEnum;

public interface SaleBill_SrvAsync {

    public void getTxtHOldOpd(String uhid, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHOldOpd_Mdl>>> result);

    public void getNavigator(String voucherCategory, UserPermission userPermission, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<SaleBill_Mdl>> result);

    public void getTxtHCity(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHCity_Mdl>>> result);

    public void deleteRecord(String voucherCategory, String strKey, ModifyReason_Mdl reason_Mdl, String regDocid, AsyncCallback<DmResult<String>> result);

    public void isItemEverReturned(String itemCode, String ibCode, String regDocid, String uhid, String lmCode, String prvQty, String prvFreeQty, AsyncCallback<DmResult<String>> result);

    public void saveRecord(String voucherCategory, SaleBill_Mdl mrTemp, SaleBill_Mdl prevMdl, String voucherPrefix, AsyncCallback<DmResult<String>> result);

    public void getFind(String voucherCategory, UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getTxtHVType(UserPermission userPermission, String voucherCategory, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHVType_Mdl>>> result);

    public void getUHIDDetails(SearchUHID_Mdl searchMdl, boolean linkParentDatabase, AsyncCallback<DmResult<SaleBill_Mdl>> result);

    public void getDefaultVal(String voucherCategory, UserPermission userPermission, AsyncCallback<DmResult<Default_Mdl>> result);

    public void getServerDateTime(AsyncCallback<DmResult<String[]>> result);

//    public void getTxtHMedicine(String restrictedDepartment, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHMedicine_Mdl>>> result);
    public void getTxtHIdType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHOperation(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHMedicine(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHCustomerType(DmHelper helper, boolean blnToEndOfFile, boolean opdAllow, boolean ipdAllow, boolean dayCareAllow, boolean emergencyAllow, boolean walkInAllow, boolean ledgerAllow, boolean UhidAllow, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);
  
    public void getTxtHRemark(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHRequisition(DmHelper helper, boolean blnToEndOfFile, String departmentCode, String regDocId, String voucherDate, AsyncCallback<DmResultPageing<List<TxtHRequisition_Mdl>>> result);

    public void getTxtHEstimate(DmHelper helper, boolean blnToEndOfFile, String departmentCode, String regDocId, String voucherDate, String lmCode, String backDaysForEstimateInSaleBill, AsyncCallback<DmResultPageing<List<TxtHEstimate_Mdl>>> result);

    public void getTxtHConsutant(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getITS(String itemCode, double rate, String itsBasedOn, String billDate, AsyncCallback<DmResult<List<OverHeadITS_Mdl>>> result);

    public void getMedicineQty(List<GrdMedicine_Mdl> grdMediDetailLst, String totalDays, String vDate, String departmentCode, String pharmacyDiscountPer, STEnum.MedicineSaleOn saleOn, boolean isPharmacyDiscount, AsyncCallback<DmResult<List<GrdMedicine_Mdl>>> result);

    public void getRequisitionDetail(String docIdRequisition, String vDate, AsyncCallback<DmResult<List<GrdMedicine_Mdl>>> result);

    public void getEstimateDetail(String docIdEstimate, String vDate, String departmentCode, STEnum.MedicineSaleOn saleOn, AsyncCallback<DmResult<List<GrdMedicine_Mdl>>> result);

    public void getOperationMedicine(String operationCode, String vDate, String departmentCode, STEnum.MedicineSaleOn saleOn, String pharmacyDiscountPer, boolean isPharmacyDiscount, AsyncCallback<DmResult<List<GrdMedicine_Mdl>>> result);

    public void getMedicine(String operationCode, String vDate, String departmentCode, STEnum.MedicineSaleOn saleOn, String pharmacyDiscountPer, boolean isPharmacyDiscount, AsyncCallback<DmResult<List<GrdMedicine_Mdl>>> result);

    public void getRoundOffAcCode(String vDate, AsyncCallback<DmResult<String>> result);

    public void getPharmacyBalance(String lmCode, AsyncCallback<DmResult<String>> result);

    public void getRequisitionMedicineDetails(List<KeyFieldExtended> keyFields, String v_Date, String departmentCode, String pharmacyDiscountPer, STEnum.MedicineSaleOn saleOn, boolean isPharmacyDiscount, boolean requisitionWithoutUHID, AsyncCallback<DmResult<List<GrdMedicine_Mdl>>> result);

    public void getLedger(String manualCode, String v_Date, AsyncCallback<DmResult<PartySearch_Mdl>> result);

    public void getBarCodeSearch(String barCode, String voucherDate, String departmentCode, String siteCode, STEnum.MedicineSaleOn saleOn, AsyncCallback<DmResult<BarCode_Mdl>> result);

    public void isEodExist(String entryDate, AsyncCallback<DmResult<Boolean>> result);

    public void getSkuLandedRate(String itemCode, String ibCode, String departmentCode, String v_Date, AsyncCallback<DmResult<String>> result);

    public void getTxtHUHID(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<UhidPopup_Mdl>>> result);

    public void getPrescMedicineDetail(String opdNo, String consultantCode, AsyncCallback<DmResult<List<GrdMedicine_Mdl>>> result);
}
