package aarogya.pharmacy.reports.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import java.util.LinkedHashMap;
import java.util.List;

public interface RptSaleBillCollection_SrvAsync {

    public void execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String recIdPrefixFrom, String vNoFrom, String recIdPrefixTo, String vNoTo, AsyncCallback<DmResult<String>> result);

    public void getTxtHType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHConsultant(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHBillNo(String siteCode, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHSaleBillNo_Mdl>>> result);

    public void getTxtHSite(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHValueType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHOperation(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHBillType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getDefaultValue(AsyncCallback<DmResult<KeyFieldExtended>> result);

    public void getTxtHLMCode(String type, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHLMCode_Mdl>>> result);

    public void getTxtHPayMode(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHOrganization(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHUserName(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

}
