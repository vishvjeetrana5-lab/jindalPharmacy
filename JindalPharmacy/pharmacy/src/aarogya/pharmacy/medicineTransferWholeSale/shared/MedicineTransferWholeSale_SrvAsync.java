package aarogya.pharmacy.medicineTransferWholeSale.shared;

import aarogya.pharmacy.self.shared.BarCode_Mdl;
import aarogya.selfToolkit.toolTip.shared.ToolTip_Mdl;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface MedicineTransferWholeSale_SrvAsync {

    public void getNavigator(UserPermission userPermission, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<MedicineTransferWholeSale_Mdl>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void saveRecord(UserPermission userPermission, MedicineTransferWholeSale_Mdl mrTemp, MedicineTransferWholeSale_Mdl prevMdl, String voucherPrefix, AsyncCallback<DmResult<String>> result);

    public void getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile, String department, boolean flagFromDepartment, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getDefaultValues(AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void getTxtHTransferTo(String v_Type, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHTransferTo_Mdl>>> result);

    public void getMedicineDetail(String reqDocId, String itemIssueDocId, String departmentCode, String v_Date, AsyncCallback<DmResult<List<GrdMedicineDetail_Mdl>>> result);

    public void getToolTipShowStock(String medCode, String ibCode, String fromDepartment, String toDepartment, AsyncCallback<DmResult<List<ToolTip_Mdl>>> result);

    public void getBarCodeSearch(String barCode, String voucherDate, String departmentCode, String siteCode, AsyncCallback<DmResult<BarCode_Mdl>> result);
    
    public void getSkuLandedRate(String itemCode, String ibCode, String departmentCode, String v_Date, AsyncCallback<DmResult<String>> result);

}
