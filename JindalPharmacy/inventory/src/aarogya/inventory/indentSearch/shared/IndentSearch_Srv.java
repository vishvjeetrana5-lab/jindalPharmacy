package aarogya.inventory.indentSearch.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;

public interface IndentSearch_Srv extends RemoteService {

    public DmResultPageing<List<GrdIndentDetailsSearch_Mdl>> getIndentSearchDetail(String v_Type, ItemProperty_Mdl ipm, String v_Date, String voucherCategory, String supplierCode, IndentSearchValue_Mdl searchValue_Mdl, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartmentIndentSearch(DmHelper helper, boolean blnToEndOfFile);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHItemCategoryIndentSearch(DmHelper helper, boolean blnToEndOfFile);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHItemGroupIndentSearch(DmHelper helper, boolean blnToEndOfFile);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMakeIndentSearch(DmHelper helper, boolean blnToEndOfFile);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHItemIndentSearch(DmHelper helper, boolean blnToEndOfFile);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDivisionIndentSearch(DmHelper helper, boolean blnToEndOfFile);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMarketingIndentSearch(DmHelper helper, boolean blnToEndOfFile);

}
