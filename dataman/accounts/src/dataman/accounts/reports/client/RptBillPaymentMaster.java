package dataman.accounts.reports.client;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.accounts.reports.shared.RptBillPaymentMaster_Srv;
import dataman.accounts.reports.shared.RptBillPaymentMaster_SrvAsync;
import dataman.accounts.reports.shared.RptBillPaymentMaster_Vld;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmBase.self.shared.DmEnum.DmSortType;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextBoxTime.client.DmTextBoxTime;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.libraryMenu.self.shared.lmAccounts;
import java.util.HashMap;
import java.util.List;

public class RptBillPaymentMaster extends DmReportLayout implements IsWidget, BlurHandler {

    private final String widgetName = new lmAccounts().BILL_PAYMENT_MASTER.caption;
    private final ServiceInvoker<RptBillPaymentMaster_SrvAsync> srvAsync;
    private final DmClient.ViaList vl = new DmClient.ViaList();
    private final DmTextBox txtFromDate = new DmTextBox(12, true);
    private final DmTextBox txtToDate = new DmTextBox(12, true);

    //<editor-fold defaultstate="collapsed" desc="txtHBillType">
    private final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHBillType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, true, true);

    private void iniTxtHBillType() {
        txtHBillType.getDmGrid().addTextColumn(txtHBillType.getDmGrid().getPropertyAccess().name(), "Type", 1, "Type");
        txtHBillType.getDmGrid().initializeGrid(txtHBillType.getDmGrid().getPropertyAccess().code(), 250, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHModule">
    private final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHModule = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void iniTxtHModule() {
        txtHModule.getDmGrid().addTextColumn(txtHModule.getDmGrid().getPropertyAccess().name(), "Type", 1, "Type");
        txtHModule.getDmGrid().initializeGrid(txtHModule.getDmGrid().getPropertyAccess().code(), 125, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHBillStatus">
    private final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHBillStatus = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 2, true, true);

    private void initxtHBillStatus() {
        txtHBillStatus.getDmGrid().addTextColumn(txtHBillStatus.getDmGrid().getPropertyAccess().name(), "Type", 1, "Type");
        txtHBillStatus.getDmGrid().initializeGrid(txtHBillStatus.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHBillLastLocation">
    interface DmLocation_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedPreparedBy")
        ValueProvider<KeyFieldExtended, String> module();

    }
    private final DmTextHelp<KeyFieldExtended, DmLocation_PrpAcs> txtHBillLastLocation = new DmTextHelp(GWT.create(DmLocation_PrpAcs.class), 6, true, true);

    private void initxtHLastLocation() {
        txtHBillLastLocation.getDmGrid().addTextColumn(txtHBillLastLocation.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHBillLastLocation.getDmGrid().addTextColumn(txtHBillLastLocation.getDmGrid().getPropertyAccess().module(), "Module", 1, "mappMod");
        txtHBillLastLocation.getDmGrid().initializeGrid(txtHBillLastLocation.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHBillLastLocation.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHSupplier">
    interface DmSupplier_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedModifiedBy")
        ValueProvider<KeyFieldExtended, String> manualCode();

        @Editor.Path("SharedPreparedBy")
        ValueProvider<KeyFieldExtended, String> city();

    }
    final DmTextHelpMulti<KeyFieldExtended, DmSupplier_PrpAcs> txtHSupplier = new DmTextHelpMulti(GWT.create(DmSupplier_PrpAcs.class), 10);

    private void iniTxtHSupplier() {
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().manualCode(), "Manual", 80, "manualCode");
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().city(), "City", 100, "city");
        txtHSupplier.getDmGrid().initializeGrid(txtHSupplier.getDmGrid().getPropertyAccess().code(), 350, 0, 2);
        txtHSupplier.getHelper().addSort("name", DmSortType.ASC);
        txtHSupplier.getHelper().addSort("manualCode", DmSortType.ASC);
        txtHSupplier.getHelper().addSort("city", DmSortType.ASC);
    }
    //</editor-fold>

    @Override
    public void onClose() {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(this);
    }

    @Override
    public void onView() {
        srvAsync.invokeRPC(true).execute(DmRequestBuilder.getSessionId(), getFilter(), reportProperty, widgetName, new AsyncCallback<DmResult<KeyFieldExtended>>() {

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

        }
        );
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtFromDate) {
            txtFromDate.setText(DmClient.returnDate(txtFromDate.getText()));
        } else if (event.getSource() == txtToDate) {
            txtToDate.setText(DmClient.returnDate(txtToDate.getText()));
        }
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

    private void txtHelpEvents() {

        txtHModule.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHModule(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHModule).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHModule.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHModule).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });

        txtHBillType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHBillType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHBillType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHBillType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHBillType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHBillStatus.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHBillStatus(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHBillStatus).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHBillStatus.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHBillStatus).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHBillLastLocation.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHBillLastLocation(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHBillLastLocation).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHBillLastLocation.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHBillLastLocation).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHSupplier.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSupplier(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSupplier).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSupplier.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSupplier).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

    }

    public RptBillPaymentMaster(SetWidgetInvoker swi) {
        super();
        srvAsync
                = new ServiceInvoker<>(GWT.create(RptBillPaymentMaster_Srv.class
                ), RptBillPaymentMaster.class
                        .getName());

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

        iniTxtHBillType();
        addControl("billType", "Bill Type", vl.add(txtHBillType));

        initxtHBillStatus();
        addControl("billStatus", "Bill Status", vl.add(txtHBillStatus));

        initxtHLastLocation();
        addControl("lastLocation", "Last Location", vl.add(txtHBillLastLocation));

        iniTxtHModule();
        addControl("module", "Module", vl.add(txtHModule));

        iniTxtHSupplier();
        addControl("supplier", "Supplier", vl.add(txtHSupplier));

        swi.setWidget(this);

    }

}
