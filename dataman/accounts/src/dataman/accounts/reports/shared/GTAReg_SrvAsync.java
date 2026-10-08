package dataman.accounts.reports.shared;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
import com.google.gwt.user.client.rpc.AsyncCallback;
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

public interface GTAReg_SrvAsync {

    public void getTxtHReportOnChoice(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHSiteName(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHIncludeOpening(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHVoucherType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHPartyName(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<GTAReg_Model.TxtHPartyName>>> result);

    public void getTxtHConsignor(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<GTAReg_Model.TxtHConsignor>>> result);

    public void getTxtHConsignee(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<GTAReg_Model.TxtHConsignee>>> result);

    public void getTxtHDebitAccount(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<GTAReg_Model.TxtHDrAccount>>> result);

    public void getTxtHCreditAccount(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<GTAReg_Model.TxtHCrAccount>>> result);

    public void execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, AsyncCallback<DmResult<String>> result);
}
