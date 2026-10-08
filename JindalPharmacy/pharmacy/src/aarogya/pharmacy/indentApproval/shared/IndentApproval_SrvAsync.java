package aarogya.pharmacy.indentApproval.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface IndentApproval_SrvAsync {

    public void save(GrdMedicineDetail_Mdl approval_Mdl, AsyncCallback<DmResult<String>> result);

    public void openRecord(boolean isOpen, String docId, String v_SNo,String closedIndentApprovalRemark, AsyncCallback<DmResult<String>> result);

    public void getFillRecords(IndentApproval_Mdl param, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<GrdIndentApproval_Mdl>>> result);

    public void getTxtHItem(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHDepartment(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHStatus(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHIndenter(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void fillItemGrid(String code, AsyncCallback<DmResult<IndentApproval_Mdl>> result);

    public void getTxtHIndentNo(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHIndentNo_Mdl>>> result);

}
