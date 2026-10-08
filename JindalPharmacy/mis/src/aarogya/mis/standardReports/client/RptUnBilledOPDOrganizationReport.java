package aarogya.mis.standardReports.client;

import aarogya.mis.standardReports.shared.*;
import aarogya.projectMenu.self.shared.pmMIS;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.DmSortType;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.List;

public class RptUnBilledOPDOrganizationReport extends DmReportLayout implements IsWidget, BlurHandler {

    final String widgetName =  new pmMIS().UNBILLED_OPD_ORGANIZATION_REPORT.caption;
    ServiceInvoker<RptUnBilledOPDOrganizationReport_SrvAsync> srvAsync;
    DmClient.ViaList vl = new DmClient.ViaList();
    DmTextBox txtFromDate = new DmTextBox();
    DmTextBox txtToDate = new DmTextBox();

    //<editor-fold defaultstate="collapsed" desc="txtHOrganization">    
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHOrganization = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 8);

    private void iniTxtHOrganization() {
        txtHOrganization.getDmGrid().addTextColumn(txtHOrganization.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHOrganization.getDmGrid().initializeGrid(txtHOrganization.getDmGrid().getPropertyAccess().code(), 275, 0, 2);
        txtHOrganization.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHReportFor">    
    private final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHReportFor = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 7);

    private void iniTxtHReportFor() {
        txtHReportFor.getDmGrid().addTextColumn(txtHReportFor.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHReportFor.getDmGrid().initializeGrid(txtHReportFor.getDmGrid().getPropertyAccess().code(), 250, 0, 2);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHUHID">
    interface TxtHUHID_PrpAcs extends KeyField_T_PrpAcs<TxtHUHID_Mdl> {

        @Editor.Path("Patient")
        ValueProvider<TxtHUHID_Mdl, String> patient();

        @Editor.Path("Guardian")
        ValueProvider<TxtHUHID_Mdl, String> guardian();

        @Editor.Path("DisplayDOB")
        ValueProvider<TxtHUHID_Mdl, String> dob();

        @Editor.Path("Mobile")
        ValueProvider<TxtHUHID_Mdl, String> mobile();

        @Editor.Path("Gender")
        ValueProvider<TxtHUHID_Mdl, String> gender();
    }
    private final DmTextHelpMulti<TxtHUHID_Mdl, TxtHUHID_PrpAcs> txtHUHID = new DmTextHelpMulti(GWT.create(TxtHUHID_PrpAcs.class), 7);

    private void iniTxtHUHID() {
        txtHUHID.getDmGrid().addTextColumn(txtHUHID.getDmGrid().getPropertyAccess().name(), "UHID No.", 1, "recId");
        txtHUHID.getDmGrid().addTextColumn(txtHUHID.getDmGrid().getPropertyAccess().patient(), "Patient", 100, "patientName");
        txtHUHID.getDmGrid().addTextColumn(txtHUHID.getDmGrid().getPropertyAccess().guardian(), "Guardian", 100, "guardianName");
        txtHUHID.getDmGrid().addTextColumn(txtHUHID.getDmGrid().getPropertyAccess().mobile(), "Mobile", 80, "mobile");
        txtHUHID.getDmGrid().addTextColumn(txtHUHID.getDmGrid().getPropertyAccess().dob(), "Age", 50, "dobDisplay");
        txtHUHID.getDmGrid().addTextColumn(txtHUHID.getDmGrid().getPropertyAccess().gender(), "Gender", 70, "gender");
        txtHUHID.getDmGrid().initializeGrid(txtHUHID.getDmGrid().getPropertyAccess().code(), 550, 0, 2);
        txtHUHID.getHelper().addSort("v_Date", DmSortType.DESC);
        txtHUHID.getHelper().addSort("short_Name", DmSortType.ASC);
        txtHUHID.getHelper().addSort("v_No", DmSortType.DESC);
        txtHUHID.getHelper().addSort("recId", DmSortType.ASC);
    }
    //</editor-fold>

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

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtFromDate) {
            txtFromDate.setText(DmClient.returnDate(txtFromDate.getText()));
        } else if (event.getSource() == txtToDate) {
            txtToDate.setText(DmClient.returnDate(txtToDate.getText()));
        }

    }

    private void txtEvents() {
        txtHOrganization.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHOrganization(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHOrganization).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHOrganization.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHOrganization).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHReportFor.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHReportFor(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHUHID.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<TxtHUHID_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHUHID(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHUHID_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHUHID).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHUHID_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHUHID.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHUHID).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
    }

    public RptUnBilledOPDOrganizationReport(SetWidgetInvoker swi) {
        super();
        srvAsync = new ServiceInvoker<>(GWT.create(RptUnBilledOPDOrganizationReport_Srv.class), RptUnBilledOPDOrganizationReport.class.getName());
        setTitle(this.asWidget(), widgetName);
        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);
        setDefaultValues();
        txtEvents();
        initialize(swi);
    }

    @Override
    public Widget asWidget() {
        return this.form;
    }

    private void initialize(SetWidgetInvoker swi) {
        addControl("fromDate", "From Date", vl.add(txtFromDate));
        addControl("toDate", "To Date", vl.add(txtToDate));

        iniTxtHReportFor();
        addControl("reportFor", "Report For", vl.add(txtHReportFor));

        iniTxtHOrganization();
        addControl("organization", "Organization", vl.add(txtHOrganization));

        iniTxtHUHID();
        addControl("uhid", "UHID No.", vl.add(txtHUHID));

        swi.setWidget(this);
    }

    public void setDefaultValues() {
        txtToDate.setText(DmClient.getLoginDate());
        txtFromDate.setText(DmClient.getStartDate());

        txtHReportFor.setTag("OPD");
        txtHReportFor.setText("OPD");
    }
}
