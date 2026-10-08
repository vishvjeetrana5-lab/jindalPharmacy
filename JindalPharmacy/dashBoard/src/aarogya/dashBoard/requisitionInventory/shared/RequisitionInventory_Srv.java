package aarogya.dashBoard.requisitionInventory.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface RequisitionInventory_Srv extends RemoteService {

    public DmResult<CardDetail_Mdl> getCardDetail();

    public DmResultPageing<List<GrdRequisitionInventory_Mdl>> getFillRecords(String departmentCode, SearchValue_Mdl searchValueMdl, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<RequisitionInventoryDetail_Mdl> getRequisitionDetail(String docId);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper pageing, boolean blnToEndOfFile);

}
