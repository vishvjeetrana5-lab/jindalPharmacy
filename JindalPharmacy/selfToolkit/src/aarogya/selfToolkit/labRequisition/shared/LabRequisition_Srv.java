package aarogya.selfToolkit.labRequisition.shared;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface LabRequisition_Srv extends RemoteService {

    public DmResultPageing<List<KeyFieldExtended>> getDepartment(DmHelper helper, boolean blnEndofFile);

    public DmResultPageing<List<KeyFieldExtended>> getRequisitionBy(DmHelper helper, boolean blnEndofFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHSiteTo(DmHelper helper, boolean blnEndofFile);

    public DmResult<DefaultValue_Mdl> getDefalutValue();

    public DmResultPageing<List<TxtHTest_Mdl>> getTxtHTest(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<String> saveRecord(LabRequisition_Mdl mrTemp, String voucherPrefix);

    public DmResultPageing<List<TxtHPriority_Mdl>> getTxtHPriority(DmHelper helper, boolean blnToEndOfFile);

    public String[] getServerDateTime();
}
