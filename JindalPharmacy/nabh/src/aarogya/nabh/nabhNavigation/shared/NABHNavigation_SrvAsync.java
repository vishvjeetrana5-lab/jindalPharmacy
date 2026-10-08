package aarogya.nabh.nabhNavigation.shared;

import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface NABHNavigation_SrvAsync {

    public void deleteRecord(String strKey, ModifyReason_Mdl modifyReasonMdl, AsyncCallback<DmResult<String>> result);

    public void getTxtHType(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getPatientDetail(String regDocId, String IPDNo, String voucher_Type, AsyncCallback<DmResult<NABHNavigation_Mdl>> result);

}
