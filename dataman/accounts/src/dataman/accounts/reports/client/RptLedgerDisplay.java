package dataman.accounts.reports.client;

import dataman.libraryMenu.self.shared.lmAccounts;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.accounts.acLedgerDetail.shared.AcLedgerDetailReport_Mdl;
import dataman.accounts.reports.shared.RptLedgerDisplay_Mdl;
import dataman.accounts.reports.shared.RptLedgerDisplay_Srv;
import dataman.accounts.reports.shared.RptLedgerDisplay_SrvAsync;
import dataman.accounts.acDisplaySetting.shared.AcDisplaySetting_Mdl;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.LinkedHashMap;
import java.util.List;

public class RptLedgerDisplay extends DmReportLayout implements IsWidget, BlurHandler {

    final String widgetName = new lmAccounts().LEDGER_DISPLAY.caption;
    ServiceInvoker<RptLedgerDisplay_SrvAsync> SrvAsync;
    DmClient.ViaList vl = new DmClient.ViaList();
    AcLedgerDetailReport_Mdl billLedgerPrint_Mdl;
    DmTextBox txtFromDate = new DmTextBox(12, false);
    DmTextBox txtToDate = new DmTextBox(12, false);
    final DmTextBoxNumeric txtClosing = new DmTextBoxNumeric(0, true, 15, false);

    //<editor-fold defaultstate="collapsed" desc="txtHYN">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHZeroValue = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, false);
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDetail = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, false);
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHNarration = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, false);
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHOpening = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, false);

    private void iniTxtHYN() {
        txtHZeroValue.getDmGrid().addTextColumn(txtHZeroValue.getDmGrid().getPropertyAccess().name(), "Name", 100, "");
        txtHZeroValue.getDmGrid().initializeGrid(txtHZeroValue.getDmGrid().getPropertyAccess().code(), 150, 0, 1);

        txtHDetail.getDmGrid().addTextColumn(txtHDetail.getDmGrid().getPropertyAccess().name(), "Name", 100, "");
        txtHDetail.getDmGrid().initializeGrid(txtHDetail.getDmGrid().getPropertyAccess().code(), 150, 0, 1);

        txtHNarration.getDmGrid().addTextColumn(txtHNarration.getDmGrid().getPropertyAccess().name(), "Name", 100, "");
        txtHNarration.getDmGrid().initializeGrid(txtHNarration.getDmGrid().getPropertyAccess().code(), 150, 0, 1);

        txtHOpening.getDmGrid().addTextColumn(txtHOpening.getDmGrid().getPropertyAccess().name(), "Name", 100, "");
        txtHOpening.getDmGrid().initializeGrid(txtHOpening.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHLedger">
    interface TxtHLedger_PrpAcs extends KeyField_T_PrpAcs<RptLedgerDisplay_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<RptLedgerDisplay_Mdl, String> manualCode();

        @Editor.Path("City")
        ValueProvider<RptLedgerDisplay_Mdl, String> city();
    }
    final DmTextHelpMulti<RptLedgerDisplay_Mdl, TxtHLedger_PrpAcs> txtHLedger = new DmTextHelpMulti(GWT.create(TxtHLedger_PrpAcs.class), 7);

    private void iniTxtHLedger() {
        txtHLedger.getDmGrid().addTextColumn(txtHLedger.getDmGrid().getPropertyAccess().name(), "Name", 150, "name");
        txtHLedger.getDmGrid().addTextColumn(txtHLedger.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 50, "manualCode");
        txtHLedger.getDmGrid().addTextColumn(txtHLedger.getDmGrid().getPropertyAccess().city(), "City", 100, "cityName");
        txtHLedger.getDmGrid().initializeGrid(txtHLedger.getDmGrid().getPropertyAccess().code(), 350, 0, 2);
        txtHLedger.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHSite">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHSite = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 7);

    private void iniTxtHSite() {
        txtHSite.getDmGrid().addTextColumn(txtHSite.getDmGrid().getPropertyAccess().name(), "Name", 150, "name");
        txtHSite.getDmGrid().initializeGrid(txtHSite.getDmGrid().getPropertyAccess().code(), 350, 0, 2);
        txtHSite.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    @Override
    public void onClose() {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(this);
    }

    @Override
    public void onView() {
        SrvAsync.invokeRPC(true).execute(getFilter(), reportProperty, billLedgerPrint_Mdl, new AsyncCallback<DmResult<String>>() {

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

    }

    public RptLedgerDisplay(AcLedgerDetailReport_Mdl billLedgerPrint_Mdl,AcDisplaySetting_Mdl acDisplaySettingMdl) {
        super();
        this.billLedgerPrint_Mdl = billLedgerPrint_Mdl;
        SrvAsync = new ServiceInvoker<RptLedgerDisplay_SrvAsync>(GWT.create(RptLedgerDisplay_Srv.class), RptLedgerDisplay.class.getName());
        setTitle(this.asWidget(), widgetName);

        textHelpEvents();
        txtEvents();
        initialize(acDisplaySettingMdl);
    }

    @Override
    public Widget asWidget() {
        return this.form;
    }

    private void initialize(AcDisplaySetting_Mdl acDisplaySettingMdl) {
        
        addControl("fromDate", "From Date", vl.add(txtFromDate));
//        txtFromDate.setText(DmClient.getStartDate());
        txtFromDate.setText(acDisplaySettingMdl.getDateFrom());

        addControl("toDate", "To Date", vl.add(txtToDate));
//        txtToDate.setText(DmClient.getLoginDate());
        txtToDate.setText(acDisplaySettingMdl.getDateTo());

        iniTxtHSite();
        addControl("name", "Site Name", vl.add(txtHSite));
        txtHSite.setText(acDisplaySettingMdl.getSiteNameList());
        txtHSite.setTag(acDisplaySettingMdl.getSiteCodeList());
        
        iniTxtHYN();
        addControl("value4", "Show Zero Value A/c", vl.add(txtHZeroValue));
        addControl("detail", "Show Detail", vl.add(txtHDetail));
        addControl("showNarration", "Show Narration", vl.add(txtHNarration));
        addControl("IncludeOpening", "IncludeOpening", vl.add(txtHOpening));

        iniTxtHLedger();
        addControl("ledger", "A/c Ledger", vl.add(txtHLedger));
        txtHLedger.setText(acDisplaySettingMdl.getLedgerAcNameList());
        
        addControl("stock", "Closing Stock", vl.add(txtClosing));
        txtClosing.setText(acDisplaySettingMdl.getClosingStock());
        /*
            
        */
    }

    public void setDefault(LinkedHashMap<String, FieldUDT_Mdl> hashMapFilter, boolean printToPrinter, SetWidgetInvoker swi) {
        if (!printToPrinter) {
            swi.setWidget(this);
        }
        onView();
    }

    private void txtEvents() {

    }

    private void textHelpEvents() {

        txtHDetail.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDetail).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDetail.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDetail).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHLedger.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<RptLedgerDisplay_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getLedgerAcount(helper, blnToEndOfFile, DmShared.xNull(txtHSite.getTag()), new AsyncCallback<DmResultPageing<List<RptLedgerDisplay_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHLedger).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<RptLedgerDisplay_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHLedger.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHLedger).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHNarration.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHNarration).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHNarration.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHNarration).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHOpening.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHOpening).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHOpening.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHOpening).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHZeroValue.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHZeroValue).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHZeroValue.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHZeroValue).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHSite.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getSite(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSite).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSite.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSite).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
    }
}
