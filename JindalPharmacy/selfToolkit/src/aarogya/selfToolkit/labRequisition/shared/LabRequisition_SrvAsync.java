package aarogya.selfToolkit.labRequisition.shared;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface LabRequisition_SrvAsync {

    public void getDepartment(DmHelper helper, boolean blnEndofFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getRequisitionBy(DmHelper helper, boolean blnEndofFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHSiteTo(DmHelper helper, boolean blnEndofFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getDefalutValue(AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void getTxtHTest(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHTest_Mdl>>> result);

    public void saveRecord(LabRequisition_Mdl mrTemp, String voucherPrefix, AsyncCallback<DmResult<String>> result);

    public void getTxtHPriority(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHPriority_Mdl>>> result);

    public void getServerDateTime(AsyncCallback<String[]> result);
}
