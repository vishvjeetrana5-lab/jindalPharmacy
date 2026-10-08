package aarogya.pharmacy.requisitionSearch.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface RequisitionSearch_SrvAsync {

    public void getRequisitionSearchDetail(RequsitionSearchValue_Mdl searchValue_Mdl, AsyncCallback<DmResult<List<GrdRequisitionDetailsSearch_Mdl>>> result);

    public void getTxtHDepartmentRequisitionSearch(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void openRecord(boolean isOpen, String docId, String vSNo, AsyncCallback<DmResult<String>> result);
    
    public void getTxtHPriority(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHPriority_Mdl>>> result);

}
