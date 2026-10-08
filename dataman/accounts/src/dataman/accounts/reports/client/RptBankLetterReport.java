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
import dataman.accounts.reports.shared.RptBankLetterReport_SrvAsync;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmBase.self.shared.DmEnum.DmSortType;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.libraryMenu.self.shared.lmAccounts;
import java.util.HashMap;
import java.util.List;
import dataman.accounts.reports.shared.RptBankLetterReport_Srv;

public class RptBankLetterReport extends DmReportLayout implements IsWidget, BlurHandler {

    private final String widgetName = new lmAccounts().BANK_LETTER_REPORT.caption;
    private final ServiceInvoker<RptBankLetterReport_SrvAsync> srvAsync;
    private final DmClient.ViaList vl = new DmClient.ViaList();
    private final DmTextBox txtFromDate = new DmTextBox(12, true);
    private final DmTextBox txtToDate = new DmTextBox(12, true);

    //<editor-fold defaultstate="collapsed" desc="txtHBillStatus">
    private final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHBillStatus = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 2, true, true);

    private void initxtHBillStatus() {
        txtHBillStatus.getDmGrid().addTextColumn(txtHBillStatus.getDmGrid().getPropertyAccess().name(), "Type", 1, "Type");
        txtHBillStatus.getDmGrid().initializeGrid(txtHBillStatus.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
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

    //<editor-fold defaultstate="collapsed" desc="bankLetterNo">
    
    final DmTextHelpMulti<KeyFieldExtended, DmSupplier_PrpAcs> txtHbankLetterNo = new DmTextHelpMulti(GWT.create(DmSupplier_PrpAcs.class), 10);

    private void iniTxtHtxtHbankLetterNo() {
        txtHbankLetterNo.getDmGrid().addTextColumn(txtHbankLetterNo.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHbankLetterNo.getDmGrid().initializeGrid(txtHbankLetterNo.getDmGrid().getPropertyAccess().code(), 350, 0, 2);
        txtHbankLetterNo.getHelper().addSort("name", DmSortType.ASC);
      
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
        
        txtHbankLetterNo.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxttxtHbankLetterNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHbankLetterNo).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHbankLetterNo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHbankLetterNo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

    }

    public RptBankLetterReport(SetWidgetInvoker swi) {
        super();

        srvAsync = new ServiceInvoker<>(GWT.create(RptBankLetterReport_Srv.class), RptBankLetterReport.class.getName());

//          srvAsync
//                = new ServiceInvoker<>(GWT.create(RptBankLetterReport_Srv.class
//                ), RptBankLetterReport.class
//                        .getName());
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

        removePDFButton();
        removeWordButton();

        addControl("fromDate", "From Date", vl.add(txtFromDate));
        txtFromDate.setText(DmClient.getStartDate());

        addControl("toDate", "To Date", vl.add(txtToDate));
        txtToDate.setText(DmClient.getLoginDate());

        initxtHBillStatus();
        addControl("letterStatus", "Bank Letter Status", vl.add(txtHBillStatus));

        iniTxtHSupplier();
        addControl("supplier", "Supplier", vl.add(txtHSupplier));

        iniTxtHtxtHbankLetterNo();
        addControl("bankLetterNo", "Bank Letter No.", vl.add(txtHbankLetterNo));

        swi.setWidget(this);

    }

}
