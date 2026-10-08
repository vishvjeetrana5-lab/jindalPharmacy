package aarogya.mis.reports.client;

import aarogya.mis.reports.shared.RptIPD_UnbilledServiceReport_Srv;
import aarogya.mis.reports.shared.RptIPD_UnbilledServiceReport_SrvAsync;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.projectMenu.self.shared.pmMIS;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.List;

public class RptIPD_UnbilledServiceReport extends DmReportLayout implements IsWidget, BlurHandler {

    private final String widgetName = new pmMIS().UNBILLED_SERVICE_REPORT.caption;
    private final ServiceInvoker<RptIPD_UnbilledServiceReport_SrvAsync> SrvAsync;
    private final DmClient.ViaList vl = new DmClient.ViaList();
    private final DmTextBox txtFromDate = new DmTextBox(12, false);
    private final DmTextBox txtToDate = new DmTextBox(12, false);

    //<editor-fold defaultstate="collapsed" desc="txtHReportType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHReportType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniTxtHReportType() {
        txtHReportType.getDmGrid().addTextColumn(txtHReportType.getDmGrid().getPropertyAccess().name(), "Needed", 100, "");
        txtHReportType.getDmGrid().initializeGrid(txtHReportType.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHisPosted">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHisPosted = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniTxtHisPosted() {
        txtHisPosted.getDmGrid().addTextColumn(txtHisPosted.getDmGrid().getPropertyAccess().name(), "Needed", 100, "");
        txtHisPosted.getDmGrid().initializeGrid(txtHisPosted.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHOrganization">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHOrganization = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHOrganization() {
        txtHOrganization.getDmGrid().addTextColumn(txtHOrganization.getDmGrid().getPropertyAccess().name(), "Organization", 1, "name");
        txtHOrganization.getDmGrid().initializeGrid(txtHOrganization.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHOrganization.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHRoomCategory">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHRoomCategory = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHRoomCategory() {
        txtHRoomCategory.getDmGrid().addTextColumn(txtHRoomCategory.getDmGrid().getPropertyAccess().name(), "Room Category", 1, "name");
        txtHRoomCategory.getDmGrid().initializeGrid(txtHRoomCategory.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHRoomCategory.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHServiceCategory">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHServiceCategory = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHServiceCategory() {
        txtHServiceCategory.getDmGrid().addTextColumn(txtHServiceCategory.getDmGrid().getPropertyAccess().name(), "Service Category", 1, "name");
        txtHServiceCategory.getDmGrid().initializeGrid(txtHServiceCategory.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHServiceCategory.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHServiceName">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHServiceName = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHServiceName() {
        txtHServiceName.getDmGrid().addTextColumn(txtHServiceName.getDmGrid().getPropertyAccess().name(), "Service Name", 1, "name");
        txtHServiceName.getDmGrid().initializeGrid(txtHServiceName.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHServiceName.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHConsultant">
    interface TxtHConsultant_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("sharedModifiedBy")
        ValueProvider<KeyFieldExtended, String> nature();
    }
    final DmTextHelpMulti<KeyFieldExtended, TxtHConsultant_PrpAcs> txtHConsultant = new DmTextHelpMulti(GWT.create(TxtHConsultant_PrpAcs.class), 10);

    private void iniTxtHConsultant() {
        txtHConsultant.getDmGrid().addTextColumn(txtHConsultant.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHConsultant.getDmGrid().addTextColumn(txtHConsultant.getDmGrid().getPropertyAccess().nature(), "Nature", 70, "nature");
        txtHConsultant.getDmGrid().initializeGrid(txtHConsultant.getDmGrid().getPropertyAccess().code(), 250, 0, 2);
        txtHConsultant.getHelper().addSort("name", DmSortType.ASC);
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

    private void txtEvents() {

        txtHOrganization.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHOrganization(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
        txtHConsultant.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHConsultant(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHConsultant).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHConsultant.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHConsultant).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHRoomCategory.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHRoomCategory(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHRoomCategory).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHRoomCategory.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHRoomCategory).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHServiceCategory.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHServiceCategory(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHServiceCategory).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHServiceCategory.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHServiceCategory).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHServiceName.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHServiceName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHServiceName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHServiceName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHServiceName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHReportType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHReportType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
        txtHReportType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHReportType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
        txtHisPosted.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHtxtHisPosted(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHisPosted).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHisPosted.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHisPosted).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });

    }

    public RptIPD_UnbilledServiceReport(SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(RptIPD_UnbilledServiceReport_Srv.class), RptIPD_UnbilledServiceReport.class.getName());
        setTitle(this.asWidget(), widgetName);
        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);
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

//        iniTxtHReportType();
//        addControl("type", "Report Type", vl.add(txtHReportType));

//        iniTxtHisPosted();
//        addControl("isPosted", "Is posted to A/c", vl.add(txtHisPosted));

        iniTxtHOrganization();
        addControl("organization", "Organization", vl.add(txtHOrganization));

        iniTxtHRoomCategory();
        addControl("roomCategory", "Room Category", vl.add(txtHRoomCategory));

        iniTxtHServiceCategory();
        addControl("serviceCategory", "Service Category", vl.add(txtHServiceCategory));

        iniTxtHServiceName();
        addControl("serviceName", "Service Name", vl.add(txtHServiceName));

        iniTxtHConsultant();
        addControl("consultant", "Consultant", vl.add(txtHConsultant));

        swi.setWidget(this);
        setDefaultValue();
    }

    private void setDefaultValue() {
        txtFromDate.setText(DmClient.getLoginDate());
        txtToDate.setText(DmClient.getLoginDate());

        txtHReportType.setText("Unbilled IPD Service");
        txtHReportType.setTag("0");

        txtHisPosted.setText("Yes");
        txtHisPosted.setTag("1");

//        txtHReportType.setEnabled(false);

    }

}
