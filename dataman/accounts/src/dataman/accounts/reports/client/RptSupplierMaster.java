package dataman.accounts.reports.client;

import dataman.accounts.reports.shared.*;
import dataman.libraryMenu.self.shared.lmAccounts;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
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
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.HashMap;
import java.util.List;

public class RptSupplierMaster extends DmReportLayout implements IsWidget {

    final String widgetName = new lmAccounts().SUPPLIER_MASTER_REPORT.caption;
    ServiceInvoker<RptSupplierMaster_SrvAsync> SrvAsync;
    DmClient.ViaList vl = new DmClient.ViaList();

    //<editor-fold defaultstate="collapsed" desc="txtHSupplier">
    interface TxtHSupplier_PrpAcs extends KeyField_T_PrpAcs<TxtHsupplier_Mdl> {

        @Editor.Path("Mobile")
        ValueProvider<TxtHsupplier_Mdl, String> mobile();

    }
    DmTextHelp<TxtHsupplier_Mdl, TxtHSupplier_PrpAcs> txtHSupplier = new DmTextHelp(GWT.create(TxtHSupplier_PrpAcs.class), 10, false);

    private void iniTxtHSupplier() {
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().name(), "Supplier Name", 155, "supplierName");
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().mobile(), "Mobile", 100, "mobile");
        txtHSupplier.getDmGrid().initializeGrid(txtHSupplier.getDmGrid().getPropertyAccess().code(), 280, 0); //244

        txtHSupplier.getHelper().addSort("supplierName", DmEnum.DmSortType.ASC); //244
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

    private void txtHelpEvents() {

        txtHSupplier.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHsupplier_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSupplier(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHsupplier_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSupplier).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHsupplier_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHSupplier.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSupplier).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHsupplier_Mdl tRow) {
            }
        });

    }

    public RptSupplierMaster(SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(RptSupplierMaster_Srv.class), RptSupplierMaster.class.getName());
        setTitle(this.asWidget(), widgetName);
        txtHelpEvents();

        initialize(swi);

    }

    @Override
    public Widget asWidget() {
        return this.form;
    }

    private void initialize(SetWidgetInvoker swi) {
        removePDFButton();
        removeWordButton();

        iniTxtHSupplier();
        addControl("supplier", "Supplier", vl.add(txtHSupplier));

        swi.setWidget(this);

    }

}
