package aarogya.dashBoard.requisitionPharmacy.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface RequisitionPharmacy_SrvAsync {

    public void getCardDetail(AsyncCallback<DmResult<CardDetail_Mdl>> result);

    public void getFillRecords(String departmentCode, SearchValue_Mdl searchValueMdl, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<GrdRequisitionPharmacy_Mdl>>> result);

    public void getRequisitionDetail(String docId, AsyncCallback<DmResult<RequisitionPharmacy_Mdl>> result);

    public void getTxtHDepartment(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

}
