package aarogya.master.D365ImportExport.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmResult;

public interface ImportExportData_SrvAsync {

    public void getIpdRevenue(String fromDate, AsyncCallback<DmResult<String>> result);

    public void exportOpdPosting(String fromDate, AsyncCallback<DmResult<String>> result);

    public void exportCancelIPDBills(String fromDate, AsyncCallback<DmResult<String>> result);

    public void exportUnBilledService(String fromDate, AsyncCallback<DmResult<String>> result);

    public void exportStockTransfer(String fromDate, AsyncCallback<DmResult<String>> result);

    public void exportStockComsumption(String fromDate, AsyncCallback<DmResult<String>> result);

    public void exportPharmacyBillStock(String fromDate, AsyncCallback<DmResult<String>> result);

    public void getItemDetails(AsyncCallback<DmResult<String>> result);

    public void getTpaSettlementBills(String fromDate,AsyncCallback<DmResult<String>> result);

    public void exportPaymentDetails(String postingFromDate, AsyncCallback<DmResult<String>> result);

    public void syncDepartmentMaster(AsyncCallback<DmResult<String>> result);

    public void updateIndentStatus(AsyncCallback<DmResult<String>> result);

}
