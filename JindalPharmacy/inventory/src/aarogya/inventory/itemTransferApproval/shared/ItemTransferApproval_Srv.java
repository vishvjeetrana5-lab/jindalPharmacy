package aarogya.inventory.itemTransferApproval.shared;

import aarogya.inventory.itemTransferApproval.shared.*;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface ItemTransferApproval_Srv extends RemoteService {

    public DmResult<String> save(GrdItemDetail_Mdl approval_Mdl);

    public DmResult<String> openRecord(boolean isOpen, String docId, String v_SNo);

    public DmResult<ItemTransferApproval_Mdl> fillItemGrid(String code);

    public DmResultPageing<List<GrdItemTransferApproval_Mdl>> getFillRecords(ItemTransferApproval_Mdl param, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHItemName_Mdl>> getTxtHItem(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHStatus(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHTransferTo(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHTransferNo_Mdl>> getTxtHTransferNo(DmHelper pageing, boolean blnToEndOfFile);

}
