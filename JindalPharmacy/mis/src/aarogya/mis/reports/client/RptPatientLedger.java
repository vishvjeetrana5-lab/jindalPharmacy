package aarogya.mis.reports.client;

import aarogya.mis.reports.shared.RptPatientLedger_Srv;
import aarogya.mis.reports.shared.RptPatientLedger_SrvAsync;
import aarogya.projectMenu.self.shared.pmMIS;
import aarogya.selfToolkit.patientSearch.client.PatientSearch;
import aarogya.selfToolkit.patientSearch.shared.SearchValue_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.List;

public class RptPatientLedger extends DmReportLayout implements IsWidget, BlurHandler, ClickHandler {

    private final String widgetName = new pmMIS().PATIENT_LEDGER.caption;
    private final ServiceInvoker<RptPatientLedger_SrvAsync> SrvAsync;
    private final DmTextBox txtFromDate = new DmTextBox(12, true);
    private final DmTextBox txtToDate = new DmTextBox(12, true);
    private final DmTextBox txtUHID = new DmTextBox(20, true);
    private final Button btnUHID = new Button("...");
    private final PatientSearch uhidSearch = new PatientSearch();

    //<editor-fold defaultstate="collapsed" desc="txtHDiagnosticCategory">
    private final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHDiagnosticCategory = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHDiagnosticCategory() {
        txtHDiagnosticCategory.getDmGrid().addTextColumn(txtHDiagnosticCategory.getDmGrid().getPropertyAccess().name(), "Diagnostic Category", 1, "name");
        txtHDiagnosticCategory.getDmGrid().initializeGrid(txtHDiagnosticCategory.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHDiagnosticCategory.getHelper().addSort("name", DmEnum.DmSortType.ASC);
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
        }
    }

    private void txtHelpEvents() {
        uhidSearch.addCallBack(new PatientSearch.CallBack() {
            @Override
            public void ok(String uhidRecId, String uhidDocId) {
                txtUHID.setTag(uhidDocId);
                txtUHID.setText(uhidRecId);
                txtFromDate.setFocus(true);
            }

            @Override
            public void close() {
                txtUHID.setFocus(true);
            }
        });

        txtHDiagnosticCategory.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHDiagnosticCategory(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDiagnosticCategory).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDiagnosticCategory.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDiagnosticCategory).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
    }

    public RptPatientLedger(SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(RptPatientLedger_Srv.class), RptPatientLedger.class.getName());
        setTitle(this.asWidget(), widgetName);
        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);
        btnUHID.addClickHandler(this);
        txtHelpEvents();
        initialize(swi);
    }

    @Override
    public Widget asWidget() {
        return this.form;
    }

    private void initialize(SetWidgetInvoker swi) {
        FlowPanel flwTd, flwBtn;
        HorizontalPanel hrtPanel;
        addControl("fromDate", "From Date", vl.add(txtFromDate));
        txtFromDate.setText(DmClient.getStartDate());

        addControl("toDate", "To Date", vl.add(txtToDate));
        txtToDate.setText(DmClient.getLoginDate());

        flwTd = new FlowPanel();
        flwBtn = new FlowPanel();
        flwTd.getElement().setAttribute("style", "border: 1px solid lightgray; float:left; overflow: hidden; width:100%;");
        hrtPanel = new HorizontalPanel();
        hrtPanel.setHeight("100%");
        hrtPanel.add(vl.add(txtUHID));
        hrtPanel.setCellWidth(txtUHID, "100%");
        btnUHID.getElement().setAttribute("style", "width: 20px; border:none; border-left:1px solid gray; float:left; padding-top: 0px; height:18px;");
        flwBtn.add(btnUHID);
        hrtPanel.add(flwBtn);
        hrtPanel.setCellWidth(flwBtn, "1px");
        flwTd.add(hrtPanel);

        addControl("uhid", "UHID No.", flwTd, txtUHID);

        iniTxtHDiagnosticCategory();
        addControl("diagnosticCategory", "Diagnostic Category", vl.add(txtHDiagnosticCategory));

        swi.setWidget(this);
    }

    @Override
    public void onClick(ClickEvent event) {
        super.onClick(event);
        if (event.getSource().equals(btnUHID)) {
            uhidSearch.show(true, SearchValue_Mdl.FilterType.WITHOUT_EXPIRED);
        }
    }
}
