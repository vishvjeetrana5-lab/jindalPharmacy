package aarogya.master.sittingLocation.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface SittingLocation_Srv extends RemoteService {

    public DmResult<String> saveRecord(SittingLocation_Mdl mrTemp);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHLocation(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<DefaultValue_Mdl> getDefaultValue(String consultantCode);
}
