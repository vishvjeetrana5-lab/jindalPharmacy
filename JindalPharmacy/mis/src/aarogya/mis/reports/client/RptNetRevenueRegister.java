package aarogya.mis.reports.client;

import aarogya.mis.reports.shared.RptNetRevenueRegister_Srv;
import aarogya.mis.reports.shared.RptNetRevenueRegister_SrvAsync;
import aarogya.projectMenu.self.shared.pmMIS;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.List;
import java.util.HashMap;

public class RptNetRevenueRegister extends DmReportLayout implements IsWidget, BlurHandler {

    final String widgetName = new pmMIS().NET_REVENUE_REPORT.caption;
    ServiceInvoker<RptNetRevenueRegister_SrvAsync> SrvAsync;
    DmClient.ViaList vl = new DmClient.ViaList();
    DmTextBox txtFromDate = new DmTextBox();
    DmTextBox txtToDate = new DmTextBox();
    DmTextBox txtFromAge = new DmTextBox();

    //<editor-fold defaultstate="collapsed" desc="txtHGroupBy">
    DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHGroupBy = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniTxtHGroupBy() {
        txtHGroupBy.getDmGrid().addTextColumn(txtHGroupBy.getDmGrid().getPropertyAccess().name(), "Group By", 1, "");
        txtHGroupBy.getDmGrid().initializeGrid(txtHGroupBy.getDmGrid().getPropertyAccess().code(), 125, 0, 1);
    }
    //</editor-fold>

    @Override
    public void onClose() {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(this);
    }

    @Override
    public void onView() {
        SrvAsync.invokeRPC(true).execute(DmRequestBuilder.getSessionId(), getFilter(), reportProperty, new AsyncCallback<DmResult<KeyFieldExtended>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    KeyFieldExtended kf = result.getData();
                    startDownloading(DmShared.xNull(kf.getSharedKeyFieldName()), DmShared.xNull(kf.getSharedKeyFieldCode()));
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

    private void txtEvents() {
        txtHGroupBy.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHGroupBy(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHGroupBy).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHGroupBy.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHGroupBy).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
    }

    public RptNetRevenueRegister(SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<RptNetRevenueRegister_SrvAsync>(GWT.create(RptNetRevenueRegister_Srv.class), RptNetRevenueRegister.class.getName());
        setTitle(this.asWidget(), widgetName);
        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);
        txtFromAge.addBlurHandler(this);
        txtEvents();
        setDefaultValues();
        initialize(swi);
    }

    @Override
    public Widget asWidget() {
        return this.form;
    }

    private void initialize(SetWidgetInvoker swi) {
        removePDFButton();
        removeWordButton();

        addControl("fromDate", "From Date", vl.add(txtFromDate));
        txtFromDate.setText(DmClient.getStartDate());

        addControl("toDate", "To Date", vl.add(txtToDate));
        txtToDate.setText(DmClient.getLoginDate());

        iniTxtHGroupBy();
        addControl("groupBy", "Group By", vl.add(txtHGroupBy));

        swi.setWidget(this);
    }

    public void setDefaultValues() {
        txtHGroupBy.setTag("D");
        txtHGroupBy.setText("Date Wise");
    }
}
