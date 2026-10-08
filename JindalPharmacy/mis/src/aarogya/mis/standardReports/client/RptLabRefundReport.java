package aarogya.mis.standardReports.client;

import aarogya.mis.standardReports.shared.*;
import aarogya.projectMenu.self.shared.pmMIS;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;

public class RptLabRefundReport extends DmReportLayout implements IsWidget, BlurHandler {

    public final String widgetName = new pmMIS().LAB_REFUND_REPORT.caption;
    private final ServiceInvoker<RptLabRefundReport_SrvAsync> srvAsync;
    private final DmClient.ViaList vl = new DmClient.ViaList();

    private final DmTextBox txtFromDate = new DmTextBox(11, true);
    private final DmTextBox txtToDate = new DmTextBox(11, true);

    @Override
    public void onClose() {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(this);
    }

    @Override
    public void onView() {
        srvAsync.invokeRPC(true).execute(getFilter(), reportProperty, widgetName, new AsyncCallback<DmResult<String>>() {

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

    public RptLabRefundReport(SetWidgetInvoker swi) {
        super();
        srvAsync = new ServiceInvoker<RptLabRefundReport_SrvAsync>(GWT.create(RptLabRefundReport_Srv.class), RptLabRefundReport.class.getName());
        setTitle(this.asWidget(), widgetName);
        initialize();
        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);
        swi.setWidget(this);

    }

    @Override
    public Widget asWidget() {
        return this.form;
    }

    private void initialize() {

        addControl("fromDate", "From Date", vl.add(txtFromDate));
        txtFromDate.setText(DmClient.getStartDate());

        addControl("toDate", "To Date", vl.add(txtToDate));
        txtToDate.setText(DmClient.getLoginDate());

    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtFromDate) {
            txtFromDate.setText(DmClient.returnDate(txtFromDate.getText()));
        } else if (event.getSource() == txtToDate) {
            txtToDate.setText(DmClient.returnDate(txtToDate.getText()));
        }
    }

}
