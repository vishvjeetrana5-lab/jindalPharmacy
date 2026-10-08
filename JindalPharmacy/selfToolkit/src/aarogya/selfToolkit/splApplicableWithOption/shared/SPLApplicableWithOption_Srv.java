package aarogya.selfToolkit.splApplicableWithOption.shared;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import aarogya.selfToolkit.stStatic.shared.STEnum.PriceListEntryFrom;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface SPLApplicableWithOption_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getApplicableDateList(PriceListEntryFrom priceListEntryFrom, String keyCode, String organizationCode, String site_Code);

    public DmResult<String> deleteApplicableDate(String applicableDate, PriceListEntryFrom priceListEntryFrom, String keyCode, String organizationCode, String site_Code);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHCopyRateDate(PriceListEntryFrom priceListEntryFrom, String keyCode, String organizationCode, String site_Code, DmHelper pageing, boolean blnToEndOfFile);

//    public DmResultPageing<List<KeyFieldExtended>> getTxtHOrganization(String siteCode, DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<DefaultValue_Mdl> getDefaultValue(PriceListEntryFrom priceListEntryFrom, String site_Code, String ORGCode);

    public DmResult<String> orgCurrentApplicableDate(PriceListEntryFrom priceListEntryFrom, String site_Code, String ORGCode);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHSite(PriceListEntryFrom priceListEntryFrom, String organizationCode, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<String> downloadCSV(List<KeyFieldExtended> grdOrganizationMdl, String siteCode);

    public DmResultPageing<List<RateList_Mdl>> getTxtHRateList(DmHelper pageing, boolean blnToEndOfFile);
}
