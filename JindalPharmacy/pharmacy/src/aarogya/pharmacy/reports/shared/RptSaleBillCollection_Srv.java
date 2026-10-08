package aarogya.pharmacy.reports.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import java.util.LinkedHashMap;
import java.util.List;

public interface RptSaleBillCollection_Srv extends RemoteService {

    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String recIdPrefixFrom, String vNoFrom, String recIdPrefixTo, String vNoTo);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHType(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHConsultant(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHSaleBillNo_Mdl>> getTxtHBillNo(String siteCode, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHSite(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHValueType(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHOperation(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHBillType(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<KeyFieldExtended> getDefaultValue();

    public DmResultPageing<List<TxtHLMCode_Mdl>> getTxtHLMCode(String type, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHPayMode(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHOrganization(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHUserName(DmHelper helper, boolean blnToEndOfFile);

}
