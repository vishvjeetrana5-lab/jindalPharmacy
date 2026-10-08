package aarogya.pharmacy.saleBillWholeSale.shared;

import aarogya.pharmacy.partySearch.shared.PartySearch_Mdl;
import aarogya.pharmacy.self.shared.BarCode_Mdl;
import dataman.dmToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.eGstPopup.shared.IEGstPopup_Srv;
import dataman.dmToolkit.gst.shared.MasterEGst_Mdl;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import dataman.dmToolkit.partyDetailPopup.shared.PartyDetailPopup_Mdl;
import java.util.List;

public interface SaleBillWholeSale_Srv extends RemoteService, IEGstPopup_Srv {

    public DmResult<List<KeyFieldExtended>> getNavigator(String voucherCategory, UserPermission userPermission);

    public DmResult<SaleBillWholeSale_Mdl> getMoveRec(String strKey);

    public DmResult<String> deleteRecord(String strKey, ModifyReason_Mdl reason_Mdl);

    public DmResult<String> isItemEverReturned(String itemCode, String ibCode, String prvQty, String prvFreeQty);

    public DmResult<String> saveRecord(String voucherCategory, SaleBillWholeSale_Mdl mrTemp, SaleBillWholeSale_Mdl prevMdl, String voucherPrefix);

    public DmResultPageing<List<Find_Mdl>> getFind(String voucherCategory, UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHVType_Mdl>> getTxtHVType(String voucherCategory, String voucherCategory1, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHVendor_Mdl>> getTxtHVendor(String voucherCategory, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHOperation(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHCustomerType(DmHelper helper, boolean blnToEndOfFile, boolean emergencyAllow, boolean walkInAllow);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHConsutant(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<Default_Mdl> getDefaultVal(String voucherCategory);

    public String[] getServerDateTime();

    public DmResult<List<OverHeadITS_Mdl>> getITS(String itemCode, double rate, String itsBasedOn, String billDate, String location);

    public DmResult<BarCode_Mdl> getBarCodeSearch(String barCode, String voucherDate, String departmentCode, String siteCode, STEnum.MedicineSaleOn saleOn);

    public DmResult<String> getSkuLandedRate(String itemCode, String ibCode, String departmentCode, String v_Date);

    public DmResult<PartyDetailPopup_Mdl> fillVendorDetails(String voucherCategory, int recordTypeCode, boolean isTransporter);

    public DmResult<MasterEGst_Mdl> getEgstDetail(String strKey, String vTypeCode);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHTransportMode(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHVendor_Mdl>> getTxtHTransporter(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getHYesNo(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHLocation(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getHVehicleType(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<String> setApproval(String approvedBy, boolean isApprove, String gatePassDocId, String docId);

    
}
