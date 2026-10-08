package aarogya.mis.standardReports.client;

import aarogya.mis.standardReports.shared.RptLabDuesReport_Srv;
import aarogya.mis.standardReports.shared.RptLabDuesReport_SrvAsync;
import aarogya.mis.standardReports.shared.TxtHUHIDNo_Mdl;
import aarogya.projectMenu.self.shared.pmMIS;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmBase.self.shared.SetWidgetInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.List;

public class RptLabDuesReport extends DmReportLayout implements IsWidget, BlurHandler {

    final String widgetName = new pmMIS().LAB_DUES_REPORT.caption;
    ServiceInvoker<RptLabDuesReport_SrvAsync> SrvAsync;
    DmClient.ViaList vl = new DmClient.ViaList();
    DmTextBox txtFromDate = new DmTextBox(12, true);
    DmTextBox txtToDate = new DmTextBox(12, true);

    //<editor-fold defaultstate="collapsed" desc="txtHSubType">
    private final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHSubType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, false);

    private void iniTxtHSubType() {
        txtHSubType.getDmGrid().addTextColumn(txtHSubType.getDmGrid().getPropertyAccess().name(), "Sub Type", 1, "Type");
        txtHSubType.getDmGrid().initializeGrid(txtHSubType.getDmGrid().getPropertyAccess().code(), 300, 0, 1);

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
        txtHUHID.getHelper().addSort("v_Date", DmEnum.DmSortType.ASC);
        txtHUHID.getHelper().addSort("short_Name", DmEnum.DmSortType.ASC);
        txtHUHID.getHelper().addSort("v_No", DmEnum.DmSortType.ASC);
        txtHUHID.getHelper().addSort("recId", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHUserName">
    private final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHUserName = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHUserName() {
        txtHUserName.getDmGrid().addTextColumn(txtHUserName.getDmGrid().getPropertyAccess().name(), "Type", 1, "Name");
        txtHUserName.getDmGrid().initializeGrid(txtHUserName.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHUserName.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    @Override
    public void onClose() {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(this);
    }

    @Override
    public void onView() {
        SrvAsync.invokeRPC(true).execute(getFilter(), widgetName, reportProperty, new AsyncCallback<DmResult<String>>() {

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
        txtHSubType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSubType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSubType).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSubType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSubType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        txtHUserName.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHUserName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHUserName).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHUserName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHUserName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
    }

    public RptLabDuesReport(SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(RptLabDuesReport_Srv.class), RptLabDuesReport.class.getName());
        setTitle(this.asWidget(), widgetName);
        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);
        txtHelpEvents();
        initialize(swi);
        setDefaultValue();
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

        iniTxtHSubType();
        addControl("subType", "Report For", vl.add(txtHSubType));

        iniTxtHUHID();
        addControl("uhid", "UHID No.", vl.add(txtHUHID));

        iniTxtHUserName();
        addControl("userName", "User Name", vl.add(txtHUserName));

        swi.setWidget(this);
    }

    private void setDefaultValue() {
        txtHSubType.setTag("A");
        txtHSubType.setText("All");
    }
}
