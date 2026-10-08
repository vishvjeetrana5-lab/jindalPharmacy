package aarogya.selfToolkit.patientRequisition.shared;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface PatientRequisition_SrvAsync {

    public void getDepartmentBy(DmHelper helper, boolean blnEndofFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getDepartmentTo(DmHelper helper, boolean blnEndofFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getRequisitionBy(DmHelper helper, boolean blnEndofFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getDefalutValue(AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void getTxtHMedicineName(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHMedicine_Mdl>>> result);

    public void saveRecord(PatientRequisition_Mdl mrTemp, String voucherPrefix, AsyncCallback<DmResult<String>> result);

    public void getTxtHPriority(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHPriority_Mdl>>> result);

    public void getTxtHSiteTo(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getServerDateTime(AsyncCallback<String[]> result);
}
