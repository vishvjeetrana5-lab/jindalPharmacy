package aarogya.mis.standardReports.client;

import aarogya.mis.standardReports.shared.RptConsultationReport_Srv;
import aarogya.mis.standardReports.shared.RptConsultationReport_SrvAsync;
import aarogya.projectMenu.self.shared.pmMIS;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.DmSortType;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextBoxTime.client.DmTextBoxTime;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class RptConsultationReport extends DmReportLayout implements IsWidget, BlurHandler {

    private final String widgetName = new pmMIS().CONSULTATION_REPORT.caption;
    private final ServiceInvoker<RptConsultationReport_SrvAsync> SrvAsync;
    private final DmClient.ViaList vl = new DmClient.ViaList();
    private final DmTextBox txtFromDate = new DmTextBox(12, true);
    private final DmTextBox txtToDate = new DmTextBox(12, true);
    private final DmTextBoxNumeric txtFromAge = new DmTextBoxNumeric(0, false, 2, false);
    private final DmTextBoxNumeric txtToAge = new DmTextBoxNumeric(0, false, 2, false);
    private final DmTextBoxTime txtToTime = new DmTextBoxTime(false);
    private final DmTextBoxTime txtFromTime = new DmTextBoxTime(false);

    //<editor-fold defaultstate="collapsed" desc="txtHType">
    private final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, true, false);

    private void iniTxtHType() {
        txtHType.getDmGrid().addTextColumn(txtHType.getDmGrid().getPropertyAccess().name(), "Name", 225, "Name");
        txtHType.getDmGrid().initializeGrid(txtHType.getDmGrid().getPropertyAccess().code(), 250, 0);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHOPDType">
    private final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHOPDType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, true, false);

    private void iniTxtHOPDType() {
        txtHOPDType.getDmGrid().addTextColumn(txtHOPDType.getDmGrid().getPropertyAccess().name(), "Name", 225, "Name");
        txtHOPDType.getDmGrid().initializeGrid(txtHOPDType.getDmGrid().getPropertyAccess().code(), 250, 0);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHTime">    
    private final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHTime = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true);

    private void iniTxtHTime() {
        txtHTime.getDmGrid().addTextColumn(txtHTime.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHTime.getDmGrid().initializeGrid(txtHTime.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHTime.getHelper().addSort("Name", DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHAge">    
    private final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHAge = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true);

    private void iniTxtHAge() {
        txtHAge.getDmGrid().addTextColumn(txtHAge.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHAge.getDmGrid().initializeGrid(txtHAge.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHAge.getHelper().addSort("Name", DmSortType.ASC);

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
        txtHType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHOPDType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHOPDType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHOPDType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHOPDType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHOPDType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHTime.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHTime(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHTime).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHTime.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHTime).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (DmShared.xNull(txtHTime.getText()).equalsIgnoreCase("Yes")) {
                    txtFromTime.setEnabled(true);
                    txtToTime.setEnabled(true);
                    txtFromTime.setFocus(true);
                } else if (DmShared.xNull(txtHTime.getText()).equalsIgnoreCase("No")) {
                    txtFromTime.setEnabled(false);
                    txtToTime.setEnabled(false);
                    txtFromTime.setText("");
                    txtToTime.setText("");
                }

            }
        });

        txtHAge.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAge(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAge).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHAge.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHAge).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (DmShared.xNull(txtHAge.getText()).equalsIgnoreCase("Yes")) {
                    txtFromAge.setEnabled(true);
                    txtToAge.setEnabled(true);
                    txtFromAge.setFocus(true);
                } else if (DmShared.xNull(txtHAge.getText()).equalsIgnoreCase("No")) {
                    txtFromAge.setEnabled(false);
                    txtToAge.setEnabled(false);
                    txtFromAge.setText("");
                    txtToAge.setText("");
                }

            }
        });
    }

    public RptConsultationReport(SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(RptConsultationReport_Srv.class), RptConsultationReport.class.getName());
        setTitle(this.asWidget(), widgetName);
        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);
        txtHelpEvents();
        initialize(swi);
        txtToTime.setEnabled(false);
        txtFromTime.setEnabled(false);
        txtToAge.setEnabled(false);
        txtFromAge.setEnabled(false);
    }

    @Override
    public Widget asWidget() {
        return this.form;
    }

    private void initialize(SetWidgetInvoker swi) {
        addControl("fromDate", "From Date", vl.add(txtFromDate));
        txtFromDate.setText(DmClient.getLoginDate());

        addControl("toDate", "To Date", vl.add(txtToDate));
        txtToDate.setText(DmClient.getLoginDate());

        iniTxtHOPDType();
        addControl("opdType", "OPD Type", vl.add(txtHOPDType));
        txtHOPDType.setTag("AL");
        txtHOPDType.setText("All");

        iniTxtHType();
        addControl("reportType", "Type", vl.add(txtHType));
        txtHType.setTag("AL");
        txtHType.setText("All");

        iniTxtHAge();
        addControl("age", "Age Wise", vl.add(txtHAge));
        txtHAge.setTag("N");
        txtHAge.setText("No");

        addControl("fromAge", "From Age", vl.add(txtFromAge));

        addControl("toAge", "To Age", vl.add(txtToAge));

        iniTxtHTime();
        addControl("time", "Time Wise", vl.add(txtHTime));
        txtHTime.setTag("N");
        txtHTime.setText("No");

        addControl("fromTime", "From Time", vl.add(txtFromTime));

        addControl("toTime", "To Time", vl.add(txtToTime));

        swi.setWidget(this);
    }

}
