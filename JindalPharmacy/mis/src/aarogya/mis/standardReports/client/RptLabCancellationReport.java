package aarogya.mis.standardReports.client;

import aarogya.mis.standardReports.shared.RptLabCancellationReport_Srv;
import aarogya.mis.standardReports.shared.RptLabCancellationReport_SrvAsync;
import aarogya.projectMenu.self.shared.pmMIS;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
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
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.List;

public class RptLabCancellationReport extends DmReportLayout implements IsWidget, BlurHandler {

    final String widgetName = new pmMIS().LAB_CANCELLATION_REPORT.caption;
    ServiceInvoker<RptLabCancellationReport_SrvAsync> SrvAsync;
    DmClient.ViaList vl = new DmClient.ViaList();
    DmTextBox txtFromDate = new DmTextBox(12, true);
    DmTextBox txtToDate = new DmTextBox(12, true);

    //<editor-fold defaultstate="collapsed" desc="txtHTest">
    private final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHTest = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHTest() {
        txtHTest.getDmGrid().addTextColumn(txtHTest.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHTest.getDmGrid().initializeGrid(txtHTest.getDmGrid().getPropertyAccess().code(), 300, 0, 2);
        txtHTest.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHTestCategory">
    private final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHTestCategory = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHTestCategory() {
        txtHTestCategory.getDmGrid().addTextColumn(txtHTestCategory.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHTestCategory.getDmGrid().initializeGrid(txtHTestCategory.getDmGrid().getPropertyAccess().code(), 300, 0, 2);
        txtHTestCategory.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHConsultant">
    private final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHConsultant = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHConsultant() {
        txtHConsultant.getDmGrid().addTextColumn(txtHConsultant.getDmGrid().getPropertyAccess().name(), "Referred Name", 1, "name");
        txtHConsultant.getDmGrid().initializeGrid(txtHConsultant.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHConsultant.getHelper().addSort("name", DmEnum.DmSortType.ASC);
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

        txtHTest.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHTest(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHTest).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHTest.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHTest).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHTestCategory.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHCategory(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHTestCategory).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHTestCategory.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHTestCategory).show();
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

    }

    public RptLabCancellationReport(SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(RptLabCancellationReport_Srv.class), RptLabCancellationReport.class.getName());
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

        iniTxtHTestCategory();
        addControl("testCategory", "Investig. Category", vl.add(txtHTestCategory));

        iniTxtHTest();
        addControl("test", "Investigation", vl.add(txtHTest));

        iniTxtHConsultant();
        addControl("referred", "Referred By", vl.add(txtHConsultant));

        swi.setWidget(this);
    }

}
