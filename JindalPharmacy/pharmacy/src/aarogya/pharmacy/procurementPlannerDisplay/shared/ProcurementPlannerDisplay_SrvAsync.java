package aarogya.pharmacy.procurementPlannerDisplay.shared;

import aarogya.pharmacy.procurementPlannerDisplay.shared.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.shared.KeyFieldExtended;
import java.util.LinkedHashMap;
import java.util.List;

public interface ProcurementPlannerDisplay_SrvAsync {

    public void getFillRecords(SearchValue_Mdl searchValueMdl, AsyncCallback<DmResult<List<ProcurementPlannerDisplay_Mdl>>> result);

    public void getRequisitionDetail(String department, AsyncCallback<DmResult<ProcurementPlannerDisplay_Mdl>> result);

    public void getTxtHDepartment(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getDefaultValues(String moduleName,AsyncCallback<DmResult<KeyFieldExtended>> result);

    public void getTxtHCompany(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHGroup(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHCategory(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHMedicine(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHMedicine_Mdl>>> result);

    public void saveRecord(SearchValue_Mdl filterMdl, List<ProcurementPlannerDisplay_Mdl> gridMdl, String voucherPrefix, AsyncCallback<DmResult<KeyFieldExtended>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<Mdl_Main>> result);

    public void getNavigator(AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getFind(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getTxtHYesNo(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void execute(SearchValue_Mdl svm, List<ProcurementPlannerDisplay_Mdl> lstData, DmReportProperty_Mdl reportProperty, AsyncCallback<DmResult<String>> asyncCallback);
}
