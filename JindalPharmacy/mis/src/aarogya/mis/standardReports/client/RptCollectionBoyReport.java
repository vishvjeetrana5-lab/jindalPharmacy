package aarogya.mis.standardReports.client;


import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
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
import dataman.dmBase.self.shared.DmEnum.DmSortType;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.List;
import aarogya.mis.standardReports.shared.TxtHUHIDNo_Mdl;
import com.google.gwt.editor.client.Editor;
import com.sencha.gxt.core.client.ValueProvider;
import aarogya.mis.standardReports.shared.RptCollectionBoyReport_Srv;
import aarogya.mis.standardReports.shared.RptCollectionBoyReport_SrvAsync;
import aarogya.projectMenu.self.shared.pmMIS;

public class RptCollectionBoyReport extends DmReportLayout implements IsWidget, BlurHandler {

    private final String widgetName = new pmMIS().COLLECTION_BOY_REPORT.caption;
    private final ServiceInvoker<RptCollectionBoyReport_SrvAsync> SrvAsync;
    private final DmClient.ViaList vl = new DmClient.ViaList();
    private final DmTextBox txtFromDate = new DmTextBox(11, true);
    private final DmTextBox txtToDate = new DmTextBox(11, true);

    //<editor-fold defaultstate="collapsed" desc="txtHReportType">
    private final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHReportType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniTxtHReportType() {
        txtHReportType.getDmGrid().addTextColumn(txtHReportType.getDmGrid().getPropertyAccess().name(), "Type", 1, "Name");
        txtHReportType.getDmGrid().initializeGrid(txtHReportType.getDmGrid().getPropertyAccess().code(), 75, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHUHID">
    interface txtHUHID_PrpAcs extends KeyField_T_PrpAcs<TxtHUHIDNo_Mdl> {

        @Editor.Path("Patient")
        ValueProvider<TxtHUHIDNo_Mdl, String> patient();

        @Editor.Path("Guardian")
        ValueProvider<TxtHUHIDNo_Mdl, String> guardian();

        @Editor.Path("DobDisplay")
        ValueProvider<TxtHUHIDNo_Mdl, String> age();

        @Editor.Path("Gender")
        ValueProvider<TxtHUHIDNo_Mdl, String> gender();

        @Editor.Path("Organization")
        ValueProvider<TxtHUHIDNo_Mdl, String> organization();
    }
    private final DmTextHelpMulti<TxtHUHIDNo_Mdl, txtHUHID_PrpAcs> txtHUHID = new DmTextHelpMulti(GWT.create(txtHUHID_PrpAcs.class), 10);

    private void iniTxtHUHID() {
        txtHUHID.getDmGrid().addTextColumn(txtHUHID.getDmGrid().getPropertyAccess().name(), "UHID No.", 1, "recId");
        txtHUHID.getDmGrid().addTextColumn(txtHUHID.getDmGrid().getPropertyAccess().patient(), "Patient Name", 125, "patientName");
        txtHUHID.getDmGrid().addTextColumn(txtHUHID.getDmGrid().getPropertyAccess().guardian(), "Guardian Name", 125, "guardianName");
        txtHUHID.getDmGrid().addTextColumn(txtHUHID.getDmGrid().getPropertyAccess().age(), "Age", 50, "dobDisplay");
        txtHUHID.getDmGrid().addTextColumn(txtHUHID.getDmGrid().getPropertyAccess().gender(), "Gender", 75, "gender");
        txtHUHID.getDmGrid().addTextColumn(txtHUHID.getDmGrid().getPropertyAccess().organization(), "Organization", 125, "organization");
        txtHUHID.getDmGrid().initializeGrid(txtHUHID.getDmGrid().getPropertyAccess().code(), 650, 0, 2);
        txtHUHID.getHelper().addSort("v_Date", DmSortType.ASC);
        txtHUHID.getHelper().addSort("short_Name", DmSortType.ASC);
        txtHUHID.getHelper().addSort("v_No", DmSortType.ASC);
        txtHUHID.getHelper().addSort("recId", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHCollectionBoy">
    private final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHCollectionBoy = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHCollectionBoy() {
        txtHCollectionBoy.getDmGrid().addTextColumn(txtHCollectionBoy.getDmGrid().getPropertyAccess().name(), "Name", 300, "name");
        txtHCollectionBoy.getDmGrid().initializeGrid(txtHCollectionBoy.getDmGrid().getPropertyAccess().code(), 300, 0, 2);
        txtHCollectionBoy.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHArea">
    private final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHArea = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHArea() {
        txtHArea.getDmGrid().addTextColumn(txtHCollectionBoy.getDmGrid().getPropertyAccess().name(), "Name", 300, "name");
        txtHArea.getDmGrid().initializeGrid(txtHCollectionBoy.getDmGrid().getPropertyAccess().code(), 300, 0, 2);
        txtHArea.getHelper().addSort("name", DmEnum.DmSortType.ASC);
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
        txtHReportType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getReportType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHReportType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHReportType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHReportType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
        txtHUHID.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getUHIDNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHUHIDNo_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHUHID).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHUHIDNo_Mdl>> result) {
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
        txtHCollectionBoy.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getCollectionBoy(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCollectionBoy).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHCollectionBoy.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCollectionBoy).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHArea.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getArea(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHArea).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHArea.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHArea).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

    }

    public RptCollectionBoyReport(SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<RptCollectionBoyReport_SrvAsync>(GWT.create(RptCollectionBoyReport_Srv.class), RptCollectionBoyReport.class.getName());
        setTitle(this.asWidget(), widgetName);
        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);
        txtHelpEvents();
        setDefaultValue();
        initialize(swi);
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

        iniTxtHReportType();
        addControl("reportType", "Report Type", vl.add(txtHReportType));

        iniTxtHCollectionBoy();
        addControl("collectionBoy", "Collection Boy", vl.add(txtHCollectionBoy));

        iniTxtHArea();
        addControl("area", "Area Wise", vl.add(txtHArea));

        iniTxtHUHID();
        addControl("uhid", "UHID No.", vl.add(txtHUHID));

        swi.setWidget(this);

    }

    private void setDefaultValue() {
        txtHReportType.setTag("S");
        txtHReportType.setText("Summary");
    }

}
