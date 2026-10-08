package aarogya.pharmacy.reports.client;

import aarogya.pharmacy.reports.shared.TxtHsupplier_Mdl;
import aarogya.pharmacy.reports.shared.*;
import aarogya.projectMenu.self.shared.pmPharmacy;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.HashMap;
import java.util.List;

public class RptCdConfiguration extends DmReportLayout implements IsWidget, BlurHandler {

    final String widgetName = new pmPharmacy().CD_CONFIGURATION_REPORT.caption;
    ServiceInvoker<RptCdConfiguration_SrvAsync> SrvAsync;
    DmClient.ViaList vl = new DmClient.ViaList();
    DmTextBox txtFromDate = new DmTextBox(12, false);
    DmTextBox txtToDate = new DmTextBox(12, true);

    //<editor-fold defaultstate="collapsed" desc="txtHItem">
    interface GridItem_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHsupplier_Mdl> {

        @Editor.Path("Mobile")
        ValueProvider<TxtHsupplier_Mdl, String> mobile();

    }
    DmTextHelpMulti<TxtHsupplier_Mdl, GridItem_PrpAcs> txtHsupplier = new DmTextHelpMulti(GWT.create(GridItem_PrpAcs.class), 10);

    private void iniTxtHsupplier() {
        txtHsupplier.getDmGrid().addTextColumn(txtHsupplier.getDmGrid().getPropertyAccess().name(), "Supplier Name", 1, "supplierName");
        txtHsupplier.getDmGrid().addTextColumn(txtHsupplier.getDmGrid().getPropertyAccess().mobile(), "Mobile No.", 100, "mobile");
        txtHsupplier.getDmGrid().initializeGrid(txtHsupplier.getDmGrid().getPropertyAccess().fieldHashCode(), 300, 0, 2);
        txtHsupplier.getHelper().addSort("supplierName", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHcompany = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHcompany() {
        txtHcompany.getDmGrid().addTextColumn(txtHcompany.getDmGrid().getPropertyAccess().name(), "Company Name", 1, "name");
        txtHcompany.getDmGrid().initializeGrid(txtHcompany.getDmGrid().getPropertyAccess().code(), 300, 0, 2);
        txtHcompany.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    @Override
    public void onClose() {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(this);
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
    public void onView() {
        SrvAsync.invokeRPC(true).execute(DmRequestBuilder.getSessionId(), getFilter(), reportProperty, widgetName, new AsyncCallback<DmResult<KeyFieldExtended>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    KeyFieldExtended data = result.getData();
                    if (reportProperty.getExportType() == DmEnum.DmExportType.EXCEL) {
                        startDownloading(data.getSharedKeyFieldName(), data.getSharedKeyFieldCode());
                    } else {
                        getReport(data.getSharedKeyFieldCode());
                    }
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

        txtHsupplier.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<TxtHsupplier_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHsupplier(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHsupplier_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHsupplier).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHsupplier_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHsupplier.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHsupplier).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHcompany.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHcompany(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHcompany).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHcompany.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHcompany).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

    }

    public RptCdConfiguration(SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(RptCdConfiguration_Srv.class), RptCdConfiguration.class.getName());
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
//        removePDFButton();
        removeWordButton();
        addControl("fromDate", "From Date", vl.add(txtFromDate));
        txtFromDate.setText(DmClient.getStartDate());

        addControl("toDate", "To Date", vl.add(txtToDate));
        txtToDate.setText(DmClient.getLoginDate());

        iniTxtHsupplier();
        addControl("supplierCode", "Supplier", vl.add(txtHsupplier));

        iniTxtHcompany();
        addControl("companyCode", "Company", vl.add(txtHcompany));

        swi.setWidget(this);

    }

}
