package aarogya.selfToolkit.iplApplicableWithOption.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;
import aarogya.selfToolkit.stStatic.shared.STEnum.PriceListEntryFrom;

public interface IPLApplicableWithOption_SrvAsync {

    public void getApplicableDateList(PriceListEntryFrom priceListEntryFrom, String keyCode, String organizationCode, String site_Code, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void deleteApplicableDate(String applicableDate, PriceListEntryFrom priceListEntryFrom, String keyCode, String organizationCode, String site_Code, AsyncCallback<DmResult<String>> result);

    public void getTxtHCopyRateDate(PriceListEntryFrom priceListEntryFrom, String keyCode, String organizationCode, String site_Code, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getDefaultValue(PriceListEntryFrom priceListEntryFrom, String site_Code, String ORGCode, AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void getTxtHSite(PriceListEntryFrom priceListEntryFrom, String organizationCode, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void downloadCSV(List<KeyFieldExtended> grdOrganizationMdl, String siteCode, AsyncCallback<DmResult<String>> result);

    public void getTxtHRateList(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<RateList_Mdl>>> result);
}
