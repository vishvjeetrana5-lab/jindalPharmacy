package aarogya.pharmacy.requisitionSearch.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface RequisitionSearch_Srv extends RemoteService {

    public DmResult<List<GrdRequisitionDetailsSearch_Mdl>> getRequisitionSearchDetail(RequsitionSearchValue_Mdl searchValue_Mdl);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartmentRequisitionSearch(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<String> openRecord(boolean isOpen, String docId, String vSNo);
    
    public DmResultPageing<List<TxtHPriority_Mdl>> getTxtHPriority(DmHelper helper, boolean blnToEndOfFile);

}
