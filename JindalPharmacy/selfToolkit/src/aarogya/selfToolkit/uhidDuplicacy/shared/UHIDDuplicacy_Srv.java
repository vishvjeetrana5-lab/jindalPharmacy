package aarogya.selfToolkit.uhidDuplicacy.shared;

import aarogya.selfToolkit.uhidPopup.shared.UhidPopup_Mdl;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface UHIDDuplicacy_Srv extends RemoteService {

    public DmResult<List<GrdUHIDDetail_Mdl>> getDetail(UhidPopup_Mdl uhidPopup_Mdl);

    public DmResult<PatientDetail_Mdl> getUHIDDetail(String strKey);

}
