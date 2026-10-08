package aarogya.inventory.purchaseOrderDisplay.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import java.util.List;

public interface PurchaseOrderDisplay_SrvAsync {

    public void getFillRecords(SearchValue_Mdl searchValueMdl, AsyncCallback<DmResult<List<PurchaseOrderDisplay_Mdl>>> result);

    public void getTxtHDepartment(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getDefaultValues(AsyncCallback<DmResult<KeyFieldExtended>> result);

    public void getTxtHCompany(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHGroup(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHCategory(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHMedicine(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHItem_Mdl>>> result);

    public void getTxtHIndent(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHIndent_Mdl>>> result);

    public void getShortOnSupplier(List<PurchaseOrderDisplay_Mdl> lstTemp, AsyncCallback<DmResult<List<PurchaseOrderDisplay_Mdl>>> result);

    public void getApprovedVendors(String itemCode, AsyncCallback<DmResult<List<ApprovedVendors_Mdl>>> result);

    public void getPrevGrn(String itemCode, DmHelper helper, boolean blnToEndOfFile, String param, AsyncCallback<DmResultPageing<List<GrdGrnPurchaseHistory_Mdl>>> result);

    public void getTxtHSupplierPT(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHSupplier_Mdl>>> result);
}
