package aarogya.selfToolkit.claimIdPopup.shared;

import aarogya.selfToolkit.organizationPopup.shared.Organization_Mdl;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmResult;

public interface ClaimIdPopup_SrvAsync {

    public void saveClaim(Organization_Mdl mrTemp, String regDocId, AsyncCallback<DmResult<String>> result);
}
