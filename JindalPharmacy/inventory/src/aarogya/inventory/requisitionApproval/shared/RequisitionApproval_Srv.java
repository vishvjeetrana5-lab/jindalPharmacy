package aarogya.inventory.requisitionApproval.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface RequisitionApproval_Srv extends RemoteService {

    public DmResult<String> openRecord(boolean isOpen, String docId, String v_SNo,String closedRequisitionRemark);

    public DmResult<RequisitionApproval_Mdl> fillItemGrid(String code);

    public DmResultPageing<List<GrdRequisitionApproval_Mdl>> getFillRecords(RequisitionApproval_Mdl param, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHItem(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHBrand(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHSKU(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHStatus(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHIndenter(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHRequisitionNo_Mdl>> getTxtHRequisitionNo(DmHelper pageing, boolean blnToEndOfFile);
    
    public DmResult<Boolean> getLinkedRecords(String docId, String vSNo);

}
