package aarogya.mis.standardReports.client;

import aarogya.mis.standardReports.shared.RptDischargePatientDues_Srv;
import aarogya.mis.standardReports.shared.RptDischargePatientDues_SrvAsync;
import aarogya.projectMenu.self.shared.pmIPD;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class RptDischargePatientDues extends DmReportLayout implements IsWidget, BlurHandler {

    private final String widgetName = new pmIPD().DISCHARGE_PATIENT_DUES_REPORT.caption;
    private final ServiceInvoker<RptDischargePatientDues_SrvAsync> SrvAsync;
    private final DmClient.ViaList vl = new DmClient.ViaList();
    private final DmTextBox txtFromDate = new DmTextBox(12, true);
    private final DmTextBox txtToDate = new DmTextBox(12, true);
    private final DmTextBox txtPayRecUpTo = new DmTextBox(12, false);

    //<editor-fold defaultstate="collapsed" desc="txtHReportFor">
    private final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHReportFor = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void iniTxtHReportFor() {
        txtHReportFor.getDmGrid().addTextColumn(txtHReportFor.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHReportFor.getDmGrid().initializeGrid(txtHReportFor.getDmGrid().getPropertyAccess().code(), 250, 0, 1);
    }
    //</editor-fold>

    @Override
    public void onClose() {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(this);
    }

    @Override
    public void onView() {
        SrvAsync.invokeRPC(true).execute(getFilter(), reportProperty, widgetName, new AsyncCallback<DmResult<String>>() {

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
        if (event.getSource() == txtFromDate) {
            txtFromDate.setText(DmClient.returnDate(txtFromDate.getText()));
        } else if (event.getSource() == txtToDate) {
            txtToDate.setText(DmClient.returnDate(txtToDate.getText()));
        } else if (event.getSource() == txtPayRecUpTo) {
            txtPayRecUpTo.setText(DmClient.returnDate(txtPayRecUpTo.getText()));
        }
    }

    private void txtEvents() {

    }

    public RptDischargePatientDues(SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(RptDischargePatientDues_Srv.class), RptDischargePatientDues.class.getName());
        setTitle(this.asWidget(), widgetName);
        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);
        txtPayRecUpTo.addBlurHandler(this);
        txtEvents();
        txtHelpEvents();
        initialize(swi);
    }

    private void txtHelpEvents() {
        txtHReportFor.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHReportForType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHReportFor).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHReportFor.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHReportFor).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

    }

    @Override
    public Widget asWidget() {
        return this.form;
    }

    private void initialize(SetWidgetInvoker swi) {
        addControl("fromDate", "From Date", vl.add(txtFromDate));
        txtFromDate.setText(DmClient.getStartDate());

        addControl("toDate", "To Date", vl.add(txtToDate));
        txtToDate.setText(DmClient.getLoginDate());

        addControl("payRecUpTo", "Pay. Rec. Up To", vl.add(txtPayRecUpTo));
        txtPayRecUpTo.setText(DmClient.getLoginDate());

        iniTxtHReportFor();
        addControl("reportFor", "Report For", vl.add(txtHReportFor));

        swi.setWidget(this);
    }
}
