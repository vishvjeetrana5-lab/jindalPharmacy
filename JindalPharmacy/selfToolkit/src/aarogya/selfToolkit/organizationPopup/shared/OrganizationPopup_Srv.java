package aarogya.selfToolkit.organizationPopup.shared;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface OrganizationPopup_Srv extends RemoteService {

    public DmResultPageing<List<KeyFieldExtended>> getTxtHRelation(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHInsuranceCompany(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHCorporateEmployee_Mdl>> getTxtHCorporate(DmHelper helper, boolean blnToEndOfFile);
}
