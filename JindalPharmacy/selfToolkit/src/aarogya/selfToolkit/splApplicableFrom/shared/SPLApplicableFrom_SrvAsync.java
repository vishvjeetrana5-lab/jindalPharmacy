package aarogya.selfToolkit.splApplicableFrom.shared;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import aarogya.selfToolkit.stStatic.shared.STEnum.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface SPLApplicableFrom_SrvAsync {

    public void getApplicableDateList(PriceListEntryFrom priceListEntryFrom, String keyCode, String organizationCode, String site_Code, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void deleteApplicableDate(String applicableDate, PriceListEntryFrom priceListEntryFrom, String keyCode, String organizationCode, String site_Code, AsyncCallback<DmResult<String>> result);

    public void getTxtHSite(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl svm, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHDtApplicable(DmHelper pageing, boolean blnToEndOfFile, SearchValue_Mdl svm, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getMaxApplicableDate(SearchValue_Mdl svm, AsyncCallback<DmResult<KeyFieldExtended>> result);

}
