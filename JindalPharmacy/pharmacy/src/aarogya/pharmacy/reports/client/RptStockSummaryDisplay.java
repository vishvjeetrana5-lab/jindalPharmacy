package aarogya.pharmacy.reports.client;

import aarogya.pharmacy.reports.shared.RptStockSummaryDisplay_Srv;
import aarogya.pharmacy.reports.shared.RptStockSummaryDisplay_SrvAsync;
import aarogya.pharmacy.stockSummary.shared.DefaultValue_Mdl;
import aarogya.projectMenu.self.shared.pmPharmacy;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.LinkedHashMap;

public class RptStockSummaryDisplay extends DmReportLayout implements IsWidget, BlurHandler {

    final String widgetName = new pmPharmacy().STOCK_SUMMARY_DISPLAY.caption;
    ServiceInvoker<RptStockSummaryDisplay_SrvAsync> SrvAsync;
    DmClient.ViaList vl = new DmClient.ViaList();
    private DefaultValue_Mdl dvm;
    final DmTextBox txtSorting = new DmTextBox();
    final DmTextBox txtValueOn = new DmTextBox();
    final DmTextBox txtMedicineCategory = new DmTextBox();
    final DmTextBox txtMedicineGroup = new DmTextBox();
    final DmTextBox txtMedicineMake = new DmTextBox();
    final DmTextBox txtMedicineName = new DmTextBox();
    final DmTextBox txtDepartmentName = new DmTextBox();
    final DmTextBox txtSiteName = new DmTextBox();
    final DmTextBox txtStatus = new DmTextBox();
    final DmTextBox txtGroupBy = new DmTextBox();
    final DmTextBox txtFromDate = new DmTextBox();
    final DmTextBox txtToDate = new DmTextBox();
    final DmTextBox txtParticulars = new DmTextBox(100, false);
    final DmTextBox txtNarcotics = new DmTextBox();
    final DmTextBox txtConsignmentItem = new DmTextBox();
    final DmTextBox txtFlowThrough = new DmTextBox();
    final DmTextBox txtHighValue = new DmTextBox();
    final DmTextBox txtBilledItem = new DmTextBox();
    final DmTextBox txtLifeSaving = new DmTextBox();
    boolean flagOpenAsPopup = false;

    @Override
    public void onClose() {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(this);
    }

    @Override
    public void onView() {
        SrvAsync.invokeRPC(true).execute(getFilter(), reportProperty, dvm, new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    if (flagOpenAsPopup) {
                        getReport(result.getData(), flagOpenAsPopup);
                    } else {
                        getReport(result.getData());
                    }
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    @Override
    public void onBlur(BlurEvent event) {

    }

    public RptStockSummaryDisplay(DefaultValue_Mdl dvm, boolean flagOpenAsPopup) {
        super();
        this.dvm = dvm;
        this.flagOpenAsPopup = flagOpenAsPopup;
        SrvAsync = new ServiceInvoker<RptStockSummaryDisplay_SrvAsync>(GWT.create(RptStockSummaryDisplay_Srv.class), RptStockSummaryDisplay.class.getName());
        setTitle(this.asWidget(), widgetName);
        initialize();
        setDefaultValue();
        txtEvents();
    }

    @Override
    public Widget asWidget() {
        return this.form;
    }

    private void initialize() {
        addControl("fromDate", "From Date", txtFromDate);
        addControl("toDate", "to Date", txtToDate);
        addControl("medCat", "Medicine Category", txtMedicineCategory);
        addControl("medGroup", "Medicine Group", txtMedicineGroup);
        addControl("medMake", "Medicine Company", txtMedicineMake);
        addControl("departmentName", "Department Name", txtDepartmentName);
        addControl("med", "Medicine Name", txtMedicineName);
        addControl("siteName", "Site Name", txtSiteName);
        addControl("status", "Status", txtStatus);
        addControl("groupBy", "Group By", txtGroupBy);
        addControl("valueOn", "Value On", txtValueOn);
        addControl("sorting", "Sorting", txtSorting);
        addControl("particulars", "Particulars", txtParticulars);
        addControl("narcotics", "Narcotics", txtNarcotics);
        addControl("consignmentItem", "Consignment Item", txtConsignmentItem);
        addControl("highValue", "High Value", txtHighValue);
        addControl("billedItem", "Billed Item", txtBilledItem);
        addControl("flowThrough", "Flow Through", txtFlowThrough);
        addControl("lifeSaving", "Life Saving", txtLifeSaving);
    }

    public void setDefault(LinkedHashMap<String, FieldUDT_Mdl> hashMapFilter, boolean printToPrinter, SetWidgetInvoker swi) {
        if (!printToPrinter && !flagOpenAsPopup) {
            swi.setWidget(this);
        }
        onView();
    }

    private void txtEvents() {

    }

    private void setDefaultValue() {
        txtFromDate.setEnabled(false);
        txtToDate.setEnabled(false);
        txtValueOn.setEnabled(false);
        txtSorting.setEnabled(false);
        txtMedicineCategory.setEnabled(false);
        txtMedicineGroup.setEnabled(false);
        txtMedicineMake.setEnabled(false);
        txtMedicineName.setEnabled(false);
        txtDepartmentName.setEnabled(false);
        txtSiteName.setEnabled(false);
        txtStatus.setEnabled(false);
        txtGroupBy.setEnabled(false);
        txtParticulars.setEnabled(false);

        txtNarcotics.setEnabled(false);
        txtConsignmentItem.setEnabled(false);
        txtHighValue.setEnabled(false);
        txtBilledItem.setEnabled(false);
        txtFlowThrough.setEnabled(false);
        txtLifeSaving.setEnabled(false);

        txtFromDate.setText(DmShared.xNull(dvm.getSetting_Mdl().getFromDate()));
        txtToDate.setText(DmShared.xNull(dvm.getSetting_Mdl().getToDate()));
        txtMedicineGroup.setTag(DmShared.xNull(dvm.getSetting_Mdl().getMedicineGroupCode()));
        txtMedicineGroup.setText(DmShared.xNull(dvm.getSetting_Mdl().getMedicineGroupName()));
        txtMedicineMake.setTag(DmShared.xNull(dvm.getSetting_Mdl().getMedicineMakeCode()));
        txtMedicineMake.setText(DmShared.xNull(dvm.getSetting_Mdl().getMedicineMakeName()));
        txtMedicineCategory.setTag(DmShared.xNull(dvm.getSetting_Mdl().getMedicineCatCode()));
        txtMedicineCategory.setText(DmShared.xNull(dvm.getSetting_Mdl().getMedicineCatName()));
        txtMedicineName.setTag(DmShared.xNull(dvm.getSetting_Mdl().getMedicineCode()));
        txtMedicineName.setText(DmShared.xNull(dvm.getSetting_Mdl().getMedicineName()));
        txtDepartmentName.setText(DmShared.xNull(dvm.getSetting_Mdl().getDepartmentCode()));
        txtDepartmentName.setText(DmShared.xNull(dvm.getSetting_Mdl().getDepartmentName()));
        txtSiteName.setTag(DmShared.xNull(dvm.getSetting_Mdl().getSiteCode()));
        txtSiteName.setText(DmShared.xNull(dvm.getSetting_Mdl().getSiteName()));
        txtValueOn.setText(DmShared.xNull(dvm.getSetting_Mdl().getValueOn()));
        txtSorting.setText(DmShared.xNull(dvm.getSetting_Mdl().getSorting()));
        txtGroupBy.setText(DmShared.xNull(dvm.getSetting_Mdl().getGroupBy()));
        txtStatus.setText(DmShared.xNull(dvm.getSetting_Mdl().getStockStatus()));
        txtParticulars.setText(DmShared.xNull(dvm.getSetting_Mdl().getTitleName()));

        txtNarcotics.setTag(DmShared.xNull(dvm.getSetting_Mdl().getNarcoticsCode()));
        txtNarcotics.setText(DmShared.xNull(dvm.getSetting_Mdl().getNarcoticsName()));
        txtConsignmentItem.setTag(DmShared.xNull(dvm.getSetting_Mdl().getConsignmentItemCode()));
        txtConsignmentItem.setText(DmShared.xNull(dvm.getSetting_Mdl().getConsignmentItemName()));
        txtHighValue.setTag(DmShared.xNull(dvm.getSetting_Mdl().getHighValueCode()));
        txtHighValue.setText(DmShared.xNull(dvm.getSetting_Mdl().getHighValueName()));
        txtBilledItem.setTag(DmShared.xNull(dvm.getSetting_Mdl().getBilledItemCode()));
        txtBilledItem.setText(DmShared.xNull(dvm.getSetting_Mdl().getBilledItemName()));
        txtFlowThrough.setTag(DmShared.xNull(dvm.getSetting_Mdl().getFlowThroughCode()));
        txtFlowThrough.setText(DmShared.xNull(dvm.getSetting_Mdl().getFlowThroughName()));
        txtLifeSaving.setTag(DmShared.xNull(dvm.getSetting_Mdl().getLifeSavingCode()));
        txtLifeSaving.setText(DmShared.xNull(dvm.getSetting_Mdl().getLifeSavingName()));
    }
}
