package aarogya.pharmacy.addBatchSingle.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface AddBatchSingle_SrvAsync {

    public void getTxtHBatchNo(String itemCode, String departmentCode, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHBatch_Mdl>>> result);

    public void saveNewBatch(Batch_Mdl batchMdl, AsyncCallback<DmResult<TxtHBatch_Mdl>> result);

}
