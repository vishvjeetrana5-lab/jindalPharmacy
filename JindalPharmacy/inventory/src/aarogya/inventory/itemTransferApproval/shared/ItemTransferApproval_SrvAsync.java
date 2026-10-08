package aarogya.inventory.itemTransferApproval.shared;

import aarogya.inventory.itemTransferApproval.shared.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface ItemTransferApproval_SrvAsync {

    public void save(GrdItemDetail_Mdl approval_Mdl, AsyncCallback<DmResult<String>> result);

    public void openRecord(boolean isOpen, String docId, String v_SNo, AsyncCallback<DmResult<String>> result);

    public void getFillRecords(ItemTransferApproval_Mdl param, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<GrdItemTransferApproval_Mdl>>> result);

    public void getTxtHItem(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHItemName_Mdl>>> result);

    public void getTxtHDepartment(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHStatus(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHTransferTo(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void fillItemGrid(String code, AsyncCallback<DmResult<ItemTransferApproval_Mdl>> result);

    public void getTxtHTransferNo(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHTransferNo_Mdl>>> result);

}
