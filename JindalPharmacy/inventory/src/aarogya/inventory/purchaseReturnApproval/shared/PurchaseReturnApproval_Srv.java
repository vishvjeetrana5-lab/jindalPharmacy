package aarogya.inventory.purchaseReturnApproval.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface PurchaseReturnApproval_Srv extends RemoteService {

    public DmResultPageing<List<GrdPurchaseReturnApproval_Mdl>> getFillRecords(Setting_Mdl param, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHBillNo_Mdl>> getTxtHBillNo(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHStatus(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHGrnNo_Mdl>> getTxtHGrnNo(DmHelper pageing, boolean blnToEndOfFile);

}
