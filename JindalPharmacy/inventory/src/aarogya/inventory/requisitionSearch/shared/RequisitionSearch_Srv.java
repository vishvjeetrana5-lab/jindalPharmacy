package aarogya.inventory.requisitionSearch.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface RequisitionSearch_Srv extends RemoteService {

    public DmResultPageing<List<GrdRequisitiontDetails_Mdl>> getRequisitionDetail(String itemCode, String departmentToCode, SearchValue_Mdl searchValue_Mdl, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile);

}
