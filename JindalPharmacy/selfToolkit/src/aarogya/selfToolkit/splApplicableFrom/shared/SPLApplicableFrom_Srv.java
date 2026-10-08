package aarogya.selfToolkit.splApplicableFrom.shared;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import aarogya.selfToolkit.stStatic.shared.STEnum.*;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface SPLApplicableFrom_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getApplicableDateList(PriceListEntryFrom priceListEntryFrom, String keyCode, String organizationCode, String site_Code);

    public DmResult<String> deleteApplicableDate(String applicableDate, PriceListEntryFrom priceListEntryFrom, String keyCode, String organizationCode, String site_Code);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHSite(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl svm);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDtApplicable(DmHelper pageing, boolean blnToEndOfFile, SearchValue_Mdl svm);

    public DmResult<KeyFieldExtended> getMaxApplicableDate(SearchValue_Mdl svm);

}
