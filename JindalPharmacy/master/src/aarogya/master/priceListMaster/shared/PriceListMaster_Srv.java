package aarogya.master.priceListMaster.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface PriceListMaster_Srv extends RemoteService {

    public DmResult<String> applyDiscountPercent(double percentValue, SearchValue_Mdl param);

    public DmResult<DefaultValue_Mdl> getDefaultValue();

    public DmResult<String> deleteRecord(String serviceCode, SearchValue_Mdl param);

    public DmResult<String> saveRecord(GrdItemDetail_Mdl mrTemp, SearchValue_Mdl param);

    // public DmResult<String> savePopup(PriceListMaster_Mdl mrTemp);
    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<GrdItemDetail_Mdl>> getItemDetailGrid(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param);

    public DmResultPageing<List<KeyFieldExtended>> getYesNo(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHPriceListType(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<String> getListFromCSV(String keyValue, String applicableDate, String serverDirectory, String serverFileName);

    public DmResult<String> savePriceList(PriceList_Mdl mrTemp);

    public DmResultPageing<List<PriceList_Mdl>> getTxtHPriceList(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHApplicableOn(DmHelper pageing, boolean blnToEndOfFile);
}
