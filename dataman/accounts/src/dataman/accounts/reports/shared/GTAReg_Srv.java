package dataman.accounts.reports.shared;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public interface GTAReg_Srv extends RemoteService {

    public DmResultPageing<List<KeyFieldExtended>> getTxtHReportOnChoice(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHSiteName(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHIncludeOpening(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucherType(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<GTAReg_Model.TxtHPartyName>> getTxtHPartyName(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<GTAReg_Model.TxtHConsignor>> getTxtHConsignor(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<GTAReg_Model.TxtHConsignee>> getTxtHConsignee(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<GTAReg_Model.TxtHDrAccount>> getTxtHDebitAccount(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<GTAReg_Model.TxtHCrAccount>> getTxtHCreditAccount(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty);
}
