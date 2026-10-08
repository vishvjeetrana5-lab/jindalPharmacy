package dataman.accounts.reports.client;

import dataman.libraryMenu.self.shared.lmAccounts;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.accounts.reports.shared.Ledger_Model;
import dataman.accounts.reports.shared.Ledger_Srv;
import dataman.accounts.reports.shared.Ledger_SrvAsync;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.HashMap;
import java.util.List;

public class Ledger extends DmReportLayout implements IsWidget, BlurHandler {

    ServiceInvoker<Ledger_SrvAsync> SrvAsync;
    final String widgetName = new lmAccounts().LEDGER.caption;
    DmClient.ViaList vl = new DmClient.ViaList();
    DmTextBox txtFromDate = new DmTextBox();
    DmTextBox txtToDate = new DmTextBox();
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHAccountGroup = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHSiteName = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);
    DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIndex = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 2);

    DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHContraAccount = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 2);

    DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHReportFor = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3);
    DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIncludeOpening = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 2);
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHLedgerGroup = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);
    DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHHavingBalance = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5);

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtFromDate) {
            txtFromDate.setText(DmClient.returnDate(txtFromDate.getText()));
        } else if (event.getSource() == txtToDate) {
            txtToDate.setText(DmClient.returnDate(txtToDate.getText()));
        }
    }

    //<editor-fold defaultstate="collapsed" desc="TxtHAccountName">
    interface TxtHAccountName_PrpAcs extends KeyField_T_PrpAcs<Ledger_Model.TxtHAccountName> {

        @Editor.Path("City")
        ValueProvider<Ledger_Model.TxtHAccountName, String> city();

        @Editor.Path("GroupName")
        ValueProvider<Ledger_Model.TxtHAccountName, String> groupName();

        @Editor.Path("ManualCode")
        ValueProvider<Ledger_Model.TxtHAccountName, String> manualCode();
    }
    DmTextHelpMulti<Ledger_Model.TxtHAccountName, TxtHAccountName_PrpAcs> txtHAccountName = new DmTextHelpMulti(GWT.create(TxtHAccountName_PrpAcs.class), 10);

    private void iniTxtHAccountName() {
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().city(), "City", 100, "City");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "ManualCode");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().groupName(), "Group Name", 150, "GroupName");
        txtHAccountName.getDmGrid().initializeGrid(txtHAccountName.getDmGrid().getPropertyAccess().code(), 500, 0, 2);
        txtHAccountName.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
        txtHAccountName.getHelper().addSort("City", DmEnum.DmSortType.ASC);
        txtHAccountName.getHelper().addSort("ManualCode", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHVocherType">

    interface TxtHVoucherType_PrpAcs extends KeyField_T_PrpAcs<Ledger_Model.TxtHVoucherType> {

        @Editor.Path("Type")
        ValueProvider<Ledger_Model.TxtHVoucherType, String> type();
    }
    DmTextHelpMulti<Ledger_Model.TxtHVoucherType, TxtHVoucherType_PrpAcs> txtHVoucherType = new DmTextHelpMulti(GWT.create(TxtHVoucherType_PrpAcs.class), 10);

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHHavingAccountName">
    interface TxtHHavingAccountName_PrpAcs extends KeyField_T_PrpAcs<Ledger_Model.TxtHHavingAccountName> {

        @Editor.Path("City")
        ValueProvider<Ledger_Model.TxtHHavingAccountName, String> city();

        @Editor.Path("GroupName")
        ValueProvider<Ledger_Model.TxtHHavingAccountName, String> groupName();

        @Editor.Path("ManualCode")
        ValueProvider<Ledger_Model.TxtHHavingAccountName, String> manualCode();
    }
    DmTextHelpMulti<Ledger_Model.TxtHHavingAccountName, TxtHHavingAccountName_PrpAcs> txtHHavingAccountName = new DmTextHelpMulti(GWT.create(TxtHHavingAccountName_PrpAcs.class), 10);
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

    private void txtEvents() {
        txtHAccountGroup.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAccountGroup(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAccountGroup).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHAccountGroup.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHAccountGroup).show();
                        }

                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHAccountName.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<Ledger_Model.TxtHAccountName>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAccountName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Ledger_Model.TxtHAccountName>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAccountName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<Ledger_Model.TxtHAccountName>> result) {
                        if (result.isStatus()) {
                            txtHAccountName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHAccountName).show();
                        }

                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHContraAccount.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHContraAccount(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHContraAccount).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHContraAccount.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHContraAccount).show();
                        }

                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHHavingAccountName.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<Ledger_Model.TxtHHavingAccountName>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHHavingAccountName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Ledger_Model.TxtHHavingAccountName>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHHavingAccountName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<Ledger_Model.TxtHHavingAccountName>> result) {
                        if (result.isStatus()) {
                            txtHHavingAccountName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHHavingAccountName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHHavingBalance.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHHavingBalance(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHHavingBalance).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHHavingBalance.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHHavingBalance).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });
        txtHIncludeOpening.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHIncludeOpening(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIncludeOpening).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIncludeOpening.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIncludeOpening).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHIndex.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHIndex(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIndex).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIndex.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIndex).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHLedgerGroup.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHLedgerGroup(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHLedgerGroup).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHLedgerGroup.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHLedgerGroup).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHReportFor.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHReportFor(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHReportFor).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHReportFor.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHReportFor).show();
                        }

                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHSiteName.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSiteName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSiteName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSiteName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSiteName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHVoucherType.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<Ledger_Model.TxtHVoucherType>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHVoucherType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Ledger_Model.TxtHVoucherType>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHVoucherType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<Ledger_Model.TxtHVoucherType>> result) {
                        if (result.isStatus()) {
                            txtHVoucherType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHVoucherType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
    }

    public Ledger(SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(Ledger_Srv.class), Ledger.class.getName());
        setTitle(this.asWidget(), widgetName);
        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);
        txtEvents();
        initialize(swi);
        setDefaultValue();
    }

    @Override
    public Widget asWidget() {
        return this.form;
    }

    private void setDefaultValue() {

        SrvAsync.invokeRPC().getDefaultValue(new AsyncCallback<DmResult<KeyFieldExtended>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    KeyFieldExtended keyField = result.getData();
                    txtHSiteName.setTag(keyField.getSharedKeyFieldCode());
                    txtHSiteName.setText(keyField.getSharedKeyFieldName());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });

    }

    private void initialize(SetWidgetInvoker swi) {
        txtFromDate.setMandatory(true);
        addControl("fromDate", "From Date", vl.add(txtFromDate));
        txtFromDate.setText(DmClient.getStartDate());
        addControl("toDate", "To Date", vl.add(txtToDate));
        txtToDate.setText(DmClient.getLoginDate());

        iniTxtHAccountGroup();
        addControl("accountGroup", "Account Group", vl.add(txtHAccountGroup));

        iniTxtHAccountName();
        addControl("accountName", "Account Name", vl.add(txtHAccountName));

        iniTxtHVoucherType();
        addControl("voucherType", "Voucher Type", vl.add(txtHVoucherType));

        iniTxtHSiteName();
        addControl("siteName", "Site Name", vl.add(txtHSiteName));

        iniTxtHIndex();
        txtHIndex.setMandatory(true);
        addControl("indexneeded", "Index Needed", vl.add(txtHIndex));

        iniTxtHContraAccount();
        txtHContraAccount.setMandatory(true);
        addControl("contraAccount", "Contra A/c Needed", vl.add(txtHContraAccount));

        iniTxtHReportFor();
        txtHReportFor.setMandatory(true);
        addControl("reportFor", "Report For", vl.add(txtHReportFor));

        iniTxtHIncludeOpening();
        txtHIncludeOpening.setMandatory(true);
        addControl("includeOpening", "Include Opening", vl.add(txtHIncludeOpening));

        iniTxtHHavingAccountName();
        addControl("havingAccount", "Having Account Name", vl.add(txtHHavingAccountName));

        iniTxtHLedgerGroup();
        addControl("ledgerGroup", "Ledger Group", vl.add(txtHLedgerGroup));

        iniTxtHHavingBalance();
        txtHHavingBalance.setMandatory(true);
        addControl("havingBalance", "Having Balance", vl.add(txtHHavingBalance));

        defaultValue();
        swi.setWidget(this);
    }

    private void iniTxtHAccountGroup() {
        txtHAccountGroup.getDmGrid().addTextColumn(txtHAccountGroup.getDmGrid().getPropertyAccess().name(), "Name", 0, "GroupName");
        txtHAccountGroup.getDmGrid().initializeGrid(txtHAccountGroup.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHAccountGroup.getHelper().addSort("GroupName", DmEnum.DmSortType.ASC);
    }

    private void iniTxtHContraAccount() {
        txtHContraAccount.getDmGrid().addTextColumn(txtHContraAccount.getDmGrid().getPropertyAccess().name(), "Name", 1, "NAME");
        txtHContraAccount.getDmGrid().initializeGrid(txtHContraAccount.getDmGrid().getPropertyAccess().code(), 75, 0, 1);
        txtHContraAccount.getHelper().addSort("NAME", DmEnum.DmSortType.ASC);
    }

    private void iniTxtHHavingAccountName() {
        txtHHavingAccountName.getDmGrid().addTextColumn(txtHHavingAccountName.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHHavingAccountName.getDmGrid().addTextColumn(txtHHavingAccountName.getDmGrid().getPropertyAccess().city(), "City", 100, "City");
        txtHHavingAccountName.getDmGrid().addTextColumn(txtHHavingAccountName.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "ManualCode");
        txtHHavingAccountName.getDmGrid().addTextColumn(txtHHavingAccountName.getDmGrid().getPropertyAccess().groupName(), "Group Name", 150, "GroupName");
        txtHHavingAccountName.getDmGrid().initializeGrid(txtHHavingAccountName.getDmGrid().getPropertyAccess().code(), 500, 0, 2);
        txtHHavingAccountName.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
        txtHHavingAccountName.getHelper().addSort("City", DmEnum.DmSortType.ASC);
        txtHHavingAccountName.getHelper().addSort("ManualCode", DmEnum.DmSortType.ASC);
    }

    private void iniTxtHHavingBalance() {
        txtHHavingBalance.getDmGrid().addTextColumn(txtHHavingBalance.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHHavingBalance.getDmGrid().initializeGrid(txtHHavingBalance.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
        txtHHavingBalance.getHelper().addSort("NAME", DmEnum.DmSortType.ASC);
    }

    private void iniTxtHIncludeOpening() {
        txtHIncludeOpening.getDmGrid().addTextColumn(txtHIncludeOpening.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHIncludeOpening.getDmGrid().initializeGrid(txtHIncludeOpening.getDmGrid().getPropertyAccess().code(), 75, 0, 1);
        txtHIncludeOpening.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }

    private void iniTxtHIndex() {
        txtHIndex.getDmGrid().addTextColumn(txtHIndex.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHIndex.getDmGrid().initializeGrid(txtHIndex.getDmGrid().getPropertyAccess().code(), 75, 0, 1);
        txtHIndex.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }

    private void iniTxtHLedgerGroup() {
        txtHLedgerGroup.getDmGrid().addTextColumn(txtHLedgerGroup.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHLedgerGroup.getDmGrid().initializeGrid(txtHLedgerGroup.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHLedgerGroup.getHelper().addSort("NAME", DmEnum.DmSortType.ASC);
    }

    private void iniTxtHReportFor() {
        txtHReportFor.getDmGrid().addTextColumn(txtHReportFor.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHReportFor.getDmGrid().initializeGrid(txtHReportFor.getDmGrid().getPropertyAccess().code(), 75, 0, 1);
        txtHReportFor.getHelper().addSort("NAME", DmEnum.DmSortType.ASC);
    }

    private void iniTxtHSiteName() {
        txtHSiteName.getDmGrid().addTextColumn(txtHSiteName.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHSiteName.getDmGrid().initializeGrid(txtHSiteName.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHSiteName.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }

    private void iniTxtHVoucherType() {
        txtHVoucherType.getDmGrid().addTextColumn(txtHVoucherType.getDmGrid().getPropertyAccess().name(), "Name", 1, "Description");
        txtHVoucherType.getDmGrid().addTextColumn(txtHVoucherType.getDmGrid().getPropertyAccess().type(), "Type", 50, "V_Type");
        txtHVoucherType.getDmGrid().initializeGrid(txtHVoucherType.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHVoucherType.getHelper().addSort("Description", DmEnum.DmSortType.ASC);
    }

    private void defaultValue() {
        txtHIndex.setTag("N");
        txtHIndex.setText("No");

        txtHContraAccount.setTag("N");
        txtHContraAccount.setText("No");

        txtHIncludeOpening.setTag("Y");
        txtHIncludeOpening.setText("Yes");

        txtHReportFor.setTag("B");
        txtHReportFor.setText("Both");

        txtHHavingBalance.setTag("N");
        txtHHavingBalance.setText("Not Applicable");
    }
}
