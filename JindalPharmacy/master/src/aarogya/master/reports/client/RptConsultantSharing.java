package aarogya.master.reports.client;

import aarogya.master.reports.shared.RptConsultantSharing_Srv;
import aarogya.master.reports.shared.RptConsultantSharing_SrvAsync;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;

import aarogya.projectMenu.self.shared.pmMaster;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;

import java.util.HashMap;
import java.util.List;

public final class RptConsultantSharing extends DmReportLayout implements IsWidget, BlurHandler {

    private final String widgetName = new pmMaster().CONSULTANT_SHARING_REPORT.caption;
    private final ServiceInvoker<RptConsultantSharing_SrvAsync> srvAsync;
    private final DmClient.ViaList vl = new DmClient.ViaList();
    private final DmTextBox txtFromDate = new DmTextBox(11, true);
    private final DmTextBox txtToDate = new DmTextBox(11, true);

    //<editor-fold defaultstate="collapsed" desc="txtHDepartment">
    private final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHDepartment = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 5);

    private void iniTxtHVrCategory() {
        txtHDepartment.getDmGrid().addTextColumn(txtHDepartment.getDmGrid().getPropertyAccess().name(), "Department", 1, "NAME");
        txtHDepartment.getDmGrid().initializeGrid(txtHDepartment.getDmGrid().getPropertyAccess().code(), 300, 0, 2);
        txtHDepartment.getHelper().addSort("NAME", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHShowConsultant">
    private final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHConsultantType = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 3);

    private void iniTxtHConsultantType() {
        txtHConsultantType.getDmGrid().addTextColumn(txtHConsultantType.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHConsultantType.getDmGrid().initializeGrid(txtHConsultantType.getDmGrid().getPropertyAccess().code(), 150, 0, 2);
        txtHConsultantType.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    @Override
    public void onClose() {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(this);
    }

    @Override
    public void onView() {
        srvAsync.invokeRPC(true).execute(DmRequestBuilder.getSessionId(), getFilter(), reportProperty, widgetName, new AsyncCallback<DmResult<String>>() {

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

// private void startDownloading(String fileName, String filePath) {
//        try {
//            HashMap<String, String> params = new HashMap<>();
//            params.put("u", DmRequestBuilder.getSessionId());
//            params.put("f", fileName);
//            params.put("p", filePath);
//            Window.open(DmClient.concatURL("dataman/dmToolkit/dmFileUploadButton/servlets/DmFileDownloader_Srvlt", params), "", "");
//        } catch (Exception ex) {
//        }
//    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtFromDate) {
            txtFromDate.setText(DmClient.returnDate(txtFromDate.getText()));
        } else if (event.getSource() == txtToDate) {
            txtToDate.setText(DmClient.returnDate(txtToDate.getText()));
        }
    }

    public RptConsultantSharing(SetWidgetInvoker swi) {
        super();
        srvAsync = new ServiceInvoker<RptConsultantSharing_SrvAsync>(GWT.create(RptConsultantSharing_Srv.class), RptConsultantSharing.class.getName());
        setTitle(this.asWidget(), widgetName);
        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);
        initialize(swi);
        txtEvents();
        setDefaultValues();
    }

    @Override
    public Widget asWidget() {
        return this.form;
    }

    private void initialize(SetWidgetInvoker swi) {
        removePDFButton();
        removeWordButton();

        addControl("fromDate", "From Date", vl.add(txtFromDate));

        addControl("toDate", "To Date", vl.add(txtToDate));

//        iniTxtHVrCategory();
//        addControl("dapartment", "Department", vl.add(txtHDepartment));
//
////        iniTxtHGroupBy();
////        addControl("groupBy", "Group By", vl.add(txtHGroupBy));
//        iniTxtHConsultantType();
//        addControl("consultant", "Consultant", vl.add(txtHConsultantType));
//        txtHConsultantType.setEnabled(false);

        swi.setWidget(this);
    }

    private void txtEvents() {

        txtHDepartment.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHDepartment(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDepartment).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDepartment.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDepartment).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHConsultantType.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHConsultantType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHConsultantType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHConsultantType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHConsultantType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {

            }
        });
    }

    public void setDefaultValues() {
        txtFromDate.setText(DmClient.getStartDate());
        txtToDate.setText(DmClient.getLoginDate());

    }
}
