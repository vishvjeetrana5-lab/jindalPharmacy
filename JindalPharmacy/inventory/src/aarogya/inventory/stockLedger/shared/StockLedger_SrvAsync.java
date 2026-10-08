package aarogya.inventory.stockLedger.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface StockLedger_SrvAsync {

    public void getParticulars(KeyFieldExtended keyField, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<StockLedger_Mdl>> result);

    public void getDefault(String itemCode, Setting_Mdl setting_Mdl, AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void getTxtHItemType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHItemCateory(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHItemGroup(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHItemMake(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHItemName(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHItemName_Mdl>>> result);

    public void getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHSiteName(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHValueOn(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getYesNo(DmHelper helper, boolean blnToEndOfFile, boolean isActiveCall, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

}
