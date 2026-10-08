package aarogya.master.sittingLocation.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface SittingLocation_SrvAsync {

    public void saveRecord(SittingLocation_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void getTxtHLocation(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getDefaultValue(String consultantCode, AsyncCallback<DmResult<DefaultValue_Mdl>> result);
}
