package aarogya.inventory.purchaseOrderDisplay.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import java.util.List;

public interface PurchaseOrderDisplay_Srv extends RemoteService {

    public DmResult<List<PurchaseOrderDisplay_Mdl>> getFillRecords(SearchValue_Mdl searchValueMdl);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper pageing, boolean blnToEndOfFile);
    
    public DmResult<KeyFieldExtended> getDefaultValues();
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCompany(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHGroup(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHCategory(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHItem_Mdl>> getTxtHMedicine(DmHelper helper, boolean blnToEndOfFile);
    
    public DmResultPageing<List<TxtHIndent_Mdl>> getTxtHIndent(DmHelper helper, boolean blnToEndOfFile);
    
    public DmResult<List<PurchaseOrderDisplay_Mdl>> getShortOnSupplier(List<PurchaseOrderDisplay_Mdl> lstTemp);
    
    public DmResult<List<ApprovedVendors_Mdl>> getApprovedVendors(String itemCode);
    
    public DmResultPageing<List<GrdGrnPurchaseHistory_Mdl>> getPrevGrn(String itemCode, DmHelper helper, boolean blnToEndOfFile, String param);

    public DmResultPageing<List<TxtHSupplier_Mdl>> getTxtHSupplierPT(DmHelper pageing, boolean blnToEndOfFile);
}
