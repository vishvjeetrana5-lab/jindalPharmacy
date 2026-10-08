package aarogya.dashBoard.requisitionInventory.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface RequisitionInventory_SrvAsync {

    public void getCardDetail(AsyncCallback<DmResult<CardDetail_Mdl>> result);

    public void getFillRecords(String departmentCode, SearchValue_Mdl searchValueMdl, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<GrdRequisitionInventory_Mdl>>> result);

    public void getRequisitionDetail(String docId, AsyncCallback<DmResult<RequisitionInventoryDetail_Mdl>> result);

    public void getTxtHDepartment(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

}
