package aarogya.mis.standardReports.client;

import aarogya.mis.standardReports.shared.RptOPDServicesReport_Srv;
import aarogya.mis.standardReports.shared.RptOPDServicesReport_SrvAsync;
import aarogya.projectMenu.self.shared.pmMIS;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmBase.self.shared.SetWidgetInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxTime.client.DmTextBoxTime;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.HashMap;
import java.util.List;

public class RptOPDServicesReport extends DmReportLayout implements IsWidget, BlurHandler {

    private final String widgetName = new pmMIS().OPD_SERVICES_REPORT.caption;
    private final ServiceInvoker<RptOPDServicesReport_SrvAsync> SrvAsync;
    private final DmClient.ViaList vl = new DmClient.ViaList();
    private final DmTextBox txtFromDate = new DmTextBox(12, true);
    private final DmTextBox txtToDate = new DmTextBox(12, true);
    private final DmTextBoxTime txtToTime = new DmTextBoxTime(false);
    private final DmTextBoxTime txtFromTime = new DmTextBoxTime(false);

    //<editor-fold defaultstate="collapsed" desc="txtHTimeWise">
    private final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHTimeWise = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, true, false);

    private void initxtHTimeWise() {
        txtHTimeWise.getDmGrid().addTextColumn(txtHTimeWise.getDmGrid().getPropertyAccess().name(), "Type", 1, "Type");
        txtHTimeWise.getDmGrid().initializeGrid(txtHTimeWise.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHOnlyDiscount">
    private final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHOnlyDiscount = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, true, false);

    private void iniTxtHOnlyDiscount() {
        txtHOnlyDiscount.getDmGrid().addTextColumn(txtHOnlyDiscount.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHOnlyDiscount.getDmGrid().initializeGrid(txtHOnlyDiscount.getDmGrid().getPropertyAccess().code(), 250, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHReportGroup">
    private final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHReportGroup = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, true, true);

    private void iniTxtHSubType() {
        txtHReportGroup.getDmGrid().addTextColumn(txtHReportGroup.getDmGrid().getPropertyAccess().name(), "Name", 150, "name");
        txtHReportGroup.getDmGrid().initializeGrid(txtHReportGroup.getDmGrid().getPropertyAccess().code(), 180, 0, 0);
        txtHReportGroup.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    @Override
    public void onClose() {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(this);
    }

    @Override
    public void onView() {
        SrvAsync.invokeRPC(true).execute(DmRequestBuilder.getSessionId(),getFilter(), reportProperty, widgetName, new AsyncCallback<DmResult<KeyFieldExtended>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    if (reportProperty.getExportType() == DmEnum.DmExportType.EXCEL) {
                        KeyFieldExtended kf = result.getData();
                        startDownloading(DmShared.xNull(kf.getSharedKeyFieldName()), DmShared.xNull(kf.getSharedKeyFieldCode()));
                    } else {
                        getReport(result.getData().getSharedKeyFieldName());
                    }

                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }
  private void startDownloading(String fileName, String filePath) {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("u", DmRequestBuilder.getSessionId());
            params.put("f", fileName);
            params.put("p", filePath);
            Window.open(DmClient.concatURL("dataman/dmToolkit/dmFileUploadButton/servlets/DmFileDownloader_Srvlt", params), "", "");
        } catch (Exception ex) {
        }
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
        txtHOnlyDiscount.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHOnlyDiscount).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHOnlyDiscount.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHOnlyDiscount).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHReportGroup.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getReportGroup(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHReportGroup).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHReportGroup.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHReportGroup).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHTimeWise.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHTimeWise(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHTimeWise).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHTimeWise.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHTimeWise).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (t1Row.getSharedKeyFieldCode().equalsIgnoreCase("Y")) {
                    txtFromTime.setEnabled(true);
                    txtToTime.setEnabled(true);
                    txtFromTime.setFocus(true);

                } else {
                    txtFromTime.setEnabled(false);
                    txtToTime.setEnabled(false);
                    txtFromTime.setText("");
                    txtToTime.setText("");

                }

            }
        });
    }

    public RptOPDServicesReport(SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(RptOPDServicesReport_Srv.class), RptOPDServicesReport.class.getName());
        setTitle(this.asWidget(), widgetName);
        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);
        txtHelpEvents();
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

        iniTxtHOnlyDiscount();
        addControl("discountDisplay", "Type", vl.add(txtHOnlyDiscount));

        iniTxtHSubType();
        addControl("reportGroup", "Sub Type", vl.add(txtHReportGroup));

        initxtHTimeWise();
        addControl("timeWise", "Time Wise", vl.add(txtHTimeWise));

        addControl("fromTime", "From Time", vl.add(txtFromTime));
        txtFromDate.setText(DmClient.getStartDate());

        addControl("toTime", "To Time", vl.add(txtToTime));
        txtToDate.setText(DmClient.getLoginDate());

        txtFromTime.setEnabled(false);
        txtToTime.setEnabled(false);

        swi.setWidget(this);
        setDefault();
    }

    private void setDefault() {
//                    if (null != RptOPDServicesReport_Vld.ReportGroup.getByText(DmShared.xNull(filter.get("reportGroup").getText()).trim())) {
//
//                switch (RptOPDServicesReport_Vld.ReportGroup.getByText(DmShared.xNull(filter.get("reportGroup").getText()).trim())) {
//
//                    case Consultant_Wise:
//                      
//                        break;
//                    case User_Wise:
//                       
//                        break;
//
//                    default:
//                       
//                        break;
//                }
//            }

        txtHReportGroup.setTag("Consultant_Wise");
        txtHReportGroup.setText("Consultant Wise");
    }

}
