package aarogya.pharmacy.addBatchSingle.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface AddBatchSingle_Srv extends RemoteService {

    public DmResultPageing<List<TxtHBatch_Mdl>> getTxtHBatchNo(String itemCode, String departmentCode, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<TxtHBatch_Mdl> saveNewBatch(Batch_Mdl batchMdl);

}
