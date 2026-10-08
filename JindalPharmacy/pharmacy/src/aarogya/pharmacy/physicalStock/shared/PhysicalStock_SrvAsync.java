package aarogya.pharmacy.physicalStock.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface PhysicalStock_SrvAsync {

    //<editor-fold defaultstate="collapsed" desc="TextHelpEvents">
    public void getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHFilterType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHFilterValue(String filterTypeCode, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHMedicineName(String departmentCode, String filterTypeCode, String filterValueCode, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHMedicineName_Mdl>>> result);

    public void getTxtHBatchNo(String medicineCode, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHBatchNo_Mdl>>> result);
    //</editor-fold>

    public void getDefaultValues(AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void saveRecord(PhysicalStock_Mdl mrTemp, String voucherPrefix, AsyncCallback<DmResult<String>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<PhysicalStock_Mdl>> result);

    public void deleteRecord(String docId, String vDate, String departmentCode, AsyncCallback<DmResult<String>> result);

    public void getNavigator(UserPermission userPermission, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getSkuLandedRate(String itemCode, String ibCode, String departmentCode, String v_Date, AsyncCallback<DmResult<String>> result);

}
