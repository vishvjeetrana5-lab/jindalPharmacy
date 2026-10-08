package aarogya.nabh.nabhNavigation.shared;

import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface NABHNavigation_Srv extends RemoteService {

    public DmResult<String> deleteRecord(String strKey, ModifyReason_Mdl modifyReasonMdl);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHType(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<NABHNavigation_Mdl> getPatientDetail(String regDocId, String IPDNo, String voucher_Type);

}
