package aarogya.selfToolkit.uhidDuplicacy.shared;

import aarogya.selfToolkit.uhidPopup.shared.UhidPopup_Mdl;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface UHIDDuplicacy_SrvAsync {

    public void getDetail(UhidPopup_Mdl uhidPopup_Mdl, AsyncCallback<DmResult<List<GrdUHIDDetail_Mdl>>> result);

    public void getUHIDDetail(String strKey, AsyncCallback<DmResult<PatientDetail_Mdl>> result);

}
