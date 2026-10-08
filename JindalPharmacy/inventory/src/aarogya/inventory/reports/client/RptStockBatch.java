package aarogya.inventory.reports.client;

import aarogya.inventory.reports.shared.RptStockBatch_SrvAsync;
import aarogya.inventory.reports.shared.RptStockBatch_Srv;
import aarogya.inventory.stockBatch.shared.DefaultValue_Mdl;
import aarogya.projectMenu.self.shared.pmInventory;
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

public class RptStockBatch extends DmReportLayout implements IsWidget, BlurHandler {

    final String widgetName = new pmInventory().STOCK_BATCH_DISPLAY.caption;
    ServiceInvoker<RptStockBatch_SrvAsync> SrvAsync;
    DefaultValue_Mdl dvm;

    final DmTextBox txtFromDate = new DmTextBox(12, false);
    final DmTextBox txtToDate = new DmTextBox(12, false);
    final DmTextBox txtItemType = new DmTextBox();
    final DmTextBox txtItemCategory = new DmTextBox();
    final DmTextBox txtItemGroup = new DmTextBox();
    final DmTextBox txtItemMake = new DmTextBox();
    final DmTextBox txtItemName = new DmTextBox();
    final DmTextBox txtDepartmentName = new DmTextBox();
    final DmTextBox txtSiteName = new DmTextBox();
    final DmTextBox txtStatus = new DmTextBox();
    final DmTextBox txtValueOn = new DmTextBox(20, false);
    final DmTextBox txtParticulars = new DmTextBox(100, false);
    final DmTextBox txtNarcotics = new DmTextBox();
    final DmTextBox txtConsignmentItem = new DmTextBox();
    final DmTextBox txtFlowThrough = new DmTextBox();
    final DmTextBox txtHighValue = new DmTextBox();
    final DmTextBox txtBilledItem = new DmTextBox();
    final DmTextBox txtLifeSaving = new DmTextBox();

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
                    getReport(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    @Override
    public void onBlur(BlurEvent event) {

    }

    public RptStockBatch(DefaultValue_Mdl defaultValue_Mdl) {
        super();
        this.dvm = defaultValue_Mdl;
        SrvAsync = new ServiceInvoker<RptStockBatch_SrvAsync>(GWT.create(RptStockBatch_Srv.class), RptStockBatch.class.getName());
        setTitle(this.asWidget(), widgetName);
        setDefaultValue();
        initialize();
        txtEvents();

    }

    @Override
    public Widget asWidget() {
        return this.form;
    }

    private void initialize() {
        addControl("fromDate", "From Date", txtFromDate);
        addControl("toDate", "To Date", txtToDate);
        addControl("itemType", "Item Type", txtItemType);
        addControl("itemCat", "Item Category", txtItemCategory);
        addControl("itemGroup", "Item Group", txtItemGroup);
        addControl("itemMake", "Item Make", txtItemMake);
        addControl("item", "Item Name", txtItemName);
        addControl("department", "Department", txtDepartmentName);
        addControl("siteName", "Site Name", txtSiteName);
        addControl("status", "Status", txtStatus);
        addControl("valueOn", "Value On", txtValueOn);
        addControl("particulars", "Particulars", txtParticulars);
        addControl("narcotics", "Narcotics", txtNarcotics);
        addControl("consignmentItem", "Consignment Item", txtConsignmentItem);
        addControl("highValue", "High Value", txtHighValue);
        addControl("billedItem", "Billed Item", txtBilledItem);
        addControl("flowThrough", "Flow Through", txtFlowThrough);
        addControl("lifeSaving", "Life Saving", txtLifeSaving);
    }

    public void setDefault(LinkedHashMap<String, FieldUDT_Mdl> hashMapFilter, boolean printToPrinter, SetWidgetInvoker swi) {
        if (!printToPrinter) {
            swi.setWidget(this);
        }
        onView();
    }

    private void txtEvents() {

    }

    private void setDefaultValue() {
        txtFromDate.setEnabled(false);
        txtToDate.setEnabled(false);
        txtItemType.setEnabled(false);
        txtItemCategory.setEnabled(false);
        txtItemGroup.setEnabled(false);
        txtItemMake.setEnabled(false);
        txtItemName.setEnabled(false);
        txtDepartmentName.setEnabled(false);
        txtSiteName.setEnabled(false);
        txtStatus.setEnabled(false);
        txtValueOn.setEnabled(false);
        txtParticulars.setEnabled(false);
        txtNarcotics.setEnabled(false);
        txtConsignmentItem.setEnabled(false);
        txtHighValue.setEnabled(false);
        txtBilledItem.setEnabled(false);
        txtFlowThrough.setEnabled(false);
        txtLifeSaving.setEnabled(false);

        txtFromDate.setText(DmShared.xNull(dvm.getSetting_Mdl().getFromDate()));
        txtToDate.setText(DmShared.xNull(dvm.getSetting_Mdl().getToDate()));
        txtItemType.setText(DmShared.xNull(dvm.getSetting_Mdl().getItemTypeName()));
        txtItemCategory.setText(DmShared.xNull(dvm.getSetting_Mdl().getItemCatName()));
        txtItemGroup.setText(DmShared.xNull(dvm.getSetting_Mdl().getItemGroupName()));
        txtItemMake.setText(DmShared.xNull(dvm.getSetting_Mdl().getItemMakeName()));
        txtItemName.setText(DmShared.xNull(dvm.getSetting_Mdl().getItemName()));
        txtDepartmentName.setText(DmShared.xNull(dvm.getSetting_Mdl().getDepartmentName()));
        txtSiteName.setText(DmShared.xNull(dvm.getSetting_Mdl().getSiteName()));
        txtStatus.setText(DmShared.xNull(dvm.getSetting_Mdl().getStatus()));
        txtValueOn.setText(DmShared.xNull(dvm.getSetting_Mdl().getValueOn()));
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
