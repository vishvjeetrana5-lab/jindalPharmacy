package aarogya.inventory.requisitionSearch.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface RequisitionSearch_SrvAsync {

    public void getRequisitionDetail(String itemCode, String departmentToCode, SearchValue_Mdl searchValue_Mdl, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<GrdRequisitiontDetails_Mdl>>> result);

    public void getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

}
