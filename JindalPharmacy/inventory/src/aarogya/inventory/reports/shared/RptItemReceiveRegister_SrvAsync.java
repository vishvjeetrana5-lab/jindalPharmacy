package aarogya.inventory.reports.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.self.shared.*;
import java.util.LinkedHashMap;
import java.util.List;

public interface RptItemReceiveRegister_SrvAsync {

    public void getTxtHVoucherNo(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHRecievedFrom(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHVoucherType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHItemName(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHItemName_Mdl>>> result);

    public void getTxtHItemCat(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHItemGroup(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, AsyncCallback<DmResult<String>> result);

    public void getTxtHSite(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getDefaultValue(AsyncCallback<DmResult<KeyFieldExtended>> result);

}
