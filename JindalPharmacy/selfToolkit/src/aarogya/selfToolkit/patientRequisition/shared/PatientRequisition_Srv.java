package aarogya.selfToolkit.patientRequisition.shared;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface PatientRequisition_Srv extends RemoteService {

    public DmResultPageing<List<KeyFieldExtended>> getDepartmentBy(DmHelper helper, boolean blnEndofFile);

    public DmResultPageing<List<KeyFieldExtended>> getDepartmentTo(DmHelper helper, boolean blnEndofFile);

    public DmResultPageing<List<KeyFieldExtended>> getRequisitionBy(DmHelper helper, boolean blnEndofFile);

    public DmResult<DefaultValue_Mdl> getDefalutValue();

    public DmResultPageing<List<TxtHMedicine_Mdl>> getTxtHMedicineName(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<String> saveRecord(PatientRequisition_Mdl mrTemp, String voucherPrefix);

    public DmResultPageing<List<TxtHPriority_Mdl>> getTxtHPriority(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHSiteTo(DmHelper helper, boolean blnToEndOfFile);

    public String[] getServerDateTime();
}
