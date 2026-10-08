package aarogya.pharmacy.indentApproval.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface IndentApproval_Srv extends RemoteService {

    public DmResult<String> save(GrdMedicineDetail_Mdl approval_Mdl);

    public DmResult<String> openRecord(boolean isOpen, String docId, String v_SNo , String closedIndentApprovalRemark);

    public DmResult<IndentApproval_Mdl> fillItemGrid(String code);

    public DmResultPageing<List<GrdIndentApproval_Mdl>> getFillRecords(IndentApproval_Mdl param, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHItem(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHStatus(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHIndenter(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHIndentNo_Mdl>> getTxtHIndentNo(DmHelper pageing, boolean blnToEndOfFile);

}
