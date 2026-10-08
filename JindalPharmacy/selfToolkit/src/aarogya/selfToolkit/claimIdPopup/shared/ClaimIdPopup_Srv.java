package aarogya.selfToolkit.claimIdPopup.shared;

import aarogya.selfToolkit.organizationPopup.shared.Organization_Mdl;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmResult;

public interface ClaimIdPopup_Srv extends RemoteService {

    public DmResult<String> saveClaim(Organization_Mdl mrTemp, String regDocId);
}
