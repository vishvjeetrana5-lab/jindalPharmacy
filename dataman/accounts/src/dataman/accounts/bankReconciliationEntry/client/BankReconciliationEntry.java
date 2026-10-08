package dataman.accounts.bankReconciliationEntry.client;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import dataman.accounts.bankReconciliationEntry.shared.*;
import dataman.accounts.reports.client.RptBankReconciliation;
import dataman.accounts.reports.shared.RptBankReconciliationPrint_Mdl;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class BankReconciliationEntry extends BankReconciliationEntry_Dsg implements AttachEvent.Handler, DmRootEvents, BlurHandler, ClickHandler {

    //<editor-fold defaultstate="collapsed" desc="Search Column [Enum]">
    private enum SearchColumn {
        SR, VOUCHER_NO, DATE, AC_NAME, FAVOR_OF, CHEQUE_NO, CHEQUE_DATE, CLEAR_DATE, DEBIT, CREDIT;
    }
    //</editor-fold>

    ServiceInvoker<BankReconciliationEntry_SrvAsync> SrvAsync;
    private Setting setting = new Setting();
    private GrdBankReconciliationEntry_Mdl parentRow;
    Setting_Mdl setting_Mdl;
    private String toDate;
    private double opnAsPerBank = 0, companyBal = 0;

    private final List<GrdBankReconciliationEntry_Mdl> lstData = new ArrayList<>();
        private SearchColumn searchColumn;
    final BankReconciliationEntryGridEdit<BankReconciliationEntry, Grd_PrpAcs> bankReconciliationEntryGridEdit = new BankReconciliationEntryGridEdit(grdAcountDetail);

    private void textHelpEvents() {
        setting.addCallBack(new Setting.CallBack() {
            @Override
            public void onOk(Setting_Mdl setting_Mdl) {
                BankReconciliationEntry.this.setting_Mdl = setting_Mdl;
                fillRecords();
                if (DmShared.xNull(setting_Mdl.getStatusCode()).equalsIgnoreCase("V")) {
                    flwTableDisplaySection.setVisible(false);
                } else {
                    flwTableDisplaySection.setVisible(true);
                }
                toDate = setting_Mdl.getToDate();
                txtClearDate.setFocus(true);
            }

            @Override
            public void onClose() {
                txtClearDate.setFocus(true);
            }
        });
    }

    private void gridEvents() {
        setSearchColumn(SearchColumn.VOUCHER_NO);

        txtSearchText.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
                    ((CellSelectionModel) grdAcountDetail.getGrid().getSelectionModel()).selectCell(0, 1);
                } else {
                    fillFilteredData();
                }
            }
        });

        bankReconciliationEntryGridEdit.addCallBack(new BankReconciliationEntryGridEdit.CallBack() {
            @Override
            public void onOk(GrdBankReconciliationEntry_Mdl reconciliationEntry_Mdl) {
                grdAcountDetail.commitChanges();
                calculate();
            }

            @Override
            public void onDelete() {
                grdAcountDetail.commitChanges();
                calculate();
            }
        });

        grdAcountDetail.getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                for (SearchColumn column : SearchColumn.values()) {
                    if (column.ordinal() == event.getColumnIndex()) {
                        setSearchColumn(column);
                    }
                }
            }
        });
        grdAcountDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                if (event.getColumnIndex() != 10) {
                    showBankReconciliationEntry(event.getRowIndex());
                }
            }
        });

        grdAcountDetail.getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {
            @Override
            public void onCellClick(CellClickEvent event) {
                if (event.getCellIndex() == 10) {
                    grdCheckedValue(event.getRowIndex());
                }
            }
        });
        grdAcountDetail.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    showBankReconciliationEntry(grdAcountDetail.getGrid().getStore().indexOf(grdAcountDetail.getGrid().getSelectionModel().getSelectedItem()));
                } else if (event.getNativeKeyCode() == KeyCodes.KEY_UP) {
                    if (grdAcountDetail.getGrid().getStore().indexOf(grdAcountDetail.getGrid().getSelectionModel().getSelectedItem()) == 0) {
                        txtSearchText.setFocus(true);
                    }
                } else if (event.getNativeKeyCode() == KeyCodes.KEY_SPACE) {
                    if (grdAcountDetail.getSelectedCell().getCell() == 10) {
                        grdCheckedValue(grdAcountDetail.getSelectedCell().getRow());
                    }
                }
            }
        }, KeyDownEvent.getType());
    }

    private void setSearchColumn(SearchColumn searchColumn) {
        this.searchColumn = searchColumn;
        DmColumnConfig col = ((DmColumnConfig) grdAcountDetail.getGrid().getColumnModel().getColumn(searchColumn.ordinal()));
        for (ColumnConfig col1 : grdAcountDetail.getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        grdAcountDetail.getGrid().getView().refresh(true);
        txtSearchText.setText("");
        txtSearchText.getElement().setAttribute("placeholder", DmShared.xNull(col.getHeader().asString()));
        txtSearchText.setFocus(true);
    }

    private void grdCheckedValue(Integer currentRowIndex) {
        int checkVal = 0;
        checkVal = DmShared.iNull(grdAcountDetail.getGrid().getStore().get(currentRowIndex).getIsCleared()).intValue();
        grdAcountDetail.lockScroll();
        if (checkVal == 0) {
            if (DmShared.xNull(txtClearDate.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Please fill clear Date.", txtClearDate).show();
                return;
            }
            grdAcountDetail.getGrid().getStore().get(currentRowIndex).setIsCleared("1");
            grdAcountDetail.getGrid().getStore().get(currentRowIndex).setClearDate(DmShared.xNull(txtClearDate.getText()));
        } else if (checkVal == 1) {
            grdAcountDetail.getGrid().getStore().get(currentRowIndex).setIsCleared("0");
            grdAcountDetail.getGrid().getStore().get(currentRowIndex).setClearDate("");
        }
        grdAcountDetail.commitChanges();
        calculate();
        grdAcountDetail.releaseScroll();

    }

    public BankReconciliationEntry(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<BankReconciliationEntry_SrvAsync>(GWT.create(BankReconciliationEntry_Srv.class), BankReconciliationEntry.class.getName());
        this.asWidget().setTitle(widgetName);
        toolContainer.setTitle(this.asWidget().getTitle());
        toolContainer.addWidget(btnSave);
        toolContainer.addWidget(btnSetting);
        toolContainer.addWidget(btnRefresh);
        toolContainer.addWidget(btnPrint);
        toolContainer.addWidget(btnClose);
        toolContainer.addGap();
        manageEvent();
        textHelpEvents();
        gridEvents();
        swi.setWidget(this);

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

            @Override
            public void execute() {
                txtClearDate.setFocus(true);
            }
        });
    }

    private void manageEvent() {
        txtClearDate.addBlurHandler(this);
        btnClose.addClickHandler(this);
        btnRefresh.addClickHandler(this);
        btnSetting.addClickHandler(this);
        btnSave.addClickHandler(this);
        btnPrint.addClickHandler(this);
        flwForm.addAttachHandler(this);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private void showSetting() {
        setting.show(parentRow, true);
    }

    private void showBankReconciliationEntry(int currentRowIndex) {
        bankReconciliationEntryGridEdit.show(currentRowIndex, true, false, false);
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (!event.isAttached()) {
            bankReconciliationEntryGridEdit.hide();
            setting.hide();
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public void searchRecord(String key) {

    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            onClose();
        } else if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_S) {
            showSetting();
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtClearDate) {
            txtClearDate.setText(DmClient.returnDate(txtClearDate.getText()));
        }
        if ((DmShared.convertDate(DmClient.getLoginDate()).getTime() < (DmShared.convertDate(txtClearDate.getText())).getTime())) {
            new DmAlertMessageBox("Clear Date should be less than or equal to Current Date.", txtClearDate).show();
            return;
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnClose)) {
            onClose();
        } else if (event.getSource().equals(btnSetting)) {
            showSetting();
        } else if (event.getSource().equals(btnSave)) {
            saveRecord();
        } else if (event.getSource().equals(btnRefresh)) {
            if (DmShared.xNull(toDate).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Please select Bank Name first", btnSetting).show();
                return;
            } else {
                refresh();
            }
        } else if (event.getSource().equals(btnPrint)) {
            print();
        }
    }

    private void print() {
        LinkedHashMap<String, FieldUDT_Mdl> hashMapFilter = new LinkedHashMap();
        List<RptBankReconciliationPrint_Mdl> lstTemp = new ArrayList<>();

        for (GrdBankReconciliationEntry_Mdl prMdl : lstData) {
            if (!DmShared.xNull(prMdl.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                RptBankReconciliationPrint_Mdl lst = new RptBankReconciliationPrint_Mdl();
                lst.setvNo(DmShared.xNull(prMdl.getSharedKeyFieldName()));
                lst.setvDate(DmShared.convertDate(prMdl.getVDate()));
                lst.setAccountName(DmShared.xNull(prMdl.getAccountName()));
                lst.setFavourOf(DmShared.xNull(prMdl.getFavourOf()));
                lst.setChq(DmShared.xNull(prMdl.getChq()));
                lst.setChqDate(DmShared.convertDate(prMdl.getChqDate()));
                lst.setClearDate(DmShared.convertDate(prMdl.getClearDate()));
                lst.setDebit(DmShared.vNull(prMdl.getDebit()));
                lst.setCredit(DmShared.vNull(prMdl.getCredit()));

                lst.setBankName(DmShared.xNull(setting_Mdl.getBankName()));
                lst.setReportType(DmShared.xNull(setting_Mdl.getStatusName()));
                lst.setFromDate(DmShared.convertDate(setting_Mdl.getFromDate()));
                lst.setToDate(DmShared.convertDate(setting_Mdl.getToDate()));

                lst.setTotalAmtCredit(DmShared.xNull(lblAmtCr.getText()));
                lst.setTotalAmtDebit(DmShared.xNull(lblAmtDr.getText()));
                lst.setAmtNotReflectedCredit(DmShared.xNull(lblAmtNotClg_Cr.getText()));
                lst.setAmtNotReflectedDebit(DmShared.xNull(lblAmtNotClg_Dr.getText()));
                lst.setAmtReflectedCredit(DmShared.xNull(lblAmtClg_Cr.getText()));
                lst.setAmtReflectedDebit(DmShared.xNull(lblAmtClg_Dr.getText()));
                lst.setOpeningAsPerBank(DmShared.xNull(lblOpnAsPerBank.getText()));
                lst.setBalAsPerBank(DmShared.xNull(lblClgAmt.getText()));
                lst.setBalAsPerCompany(DmShared.xNull(lblBalCompanyBook.getText()));
                lstTemp.add(lst);
            }
        }
        RptBankReconciliation bankReconciliation = new RptBankReconciliation(lstTemp);
        bankReconciliation.setDefault(hashMapFilter, false, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));

    }

    private void refresh() {
        fillRecords();
    }

    private void fillRecords() {
        SrvAsync.invokeRPC().getFillRecords(setting_Mdl, new AsyncCallback<DmResult<BankReconciliationEntry_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtClearDate).show();
            }

            @Override
            public void onSuccess(DmResult<BankReconciliationEntry_Mdl> result) {
                BankReconciliationEntry_Mdl data = new BankReconciliationEntry_Mdl();
                if (result.isStatus()) {
                    data = result.getData();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtClearDate).show();
                }
                fillGrid(data);
            }
        });
    }

    private void fillGrid(BankReconciliationEntry_Mdl bankReconciliationEntry_Mdl) {
        lstData.clear();
        lstData.addAll(bankReconciliationEntry_Mdl.getListBr());
        opnAsPerBank = DmShared.vNull(bankReconciliationEntry_Mdl.getOpnAsPerBank()).doubleValue();
        companyBal = DmShared.vNull(bankReconciliationEntry_Mdl.getCompanyBal()).doubleValue();
        calculate();
        txtClearDate.setFocus(true);
        txtSearchText.setText("");

        List<GrdBankReconciliationEntry_Mdl> lstTemp = new ArrayList<GrdBankReconciliationEntry_Mdl>();

        if (DmShared.xNull(this.setting_Mdl.getStatusCode()).trim().equalsIgnoreCase("U")) {
            if (DmShared.xNull(this.setting_Mdl.getChkentriesCode()).trim().equalsIgnoreCase("1")) {
                for (GrdBankReconciliationEntry_Mdl gbrc_Mdl : lstData) {
                    if (!DmShared.xNull(gbrc_Mdl.getClearDate()).equalsIgnoreCase(DmShared.xNull(this.setting_Mdl.getToDate()))) {
                        lstTemp.add(gbrc_Mdl);
                    }
                }
                lstData.clear();
                lstData.addAll(lstTemp);
            }
        }
        fillFilteredData();
    }

    private void saveRecord() {
        BankReconciliationEntry_Mdl mrTemp = new BankReconciliationEntry_Mdl();
        List<GrdBankReconciliationEntry_Mdl> lstTemp = new ArrayList<GrdBankReconciliationEntry_Mdl>();
        try {
            mrTemp = topControlSaveInitialize();
            BankReconciliationEntry_Vld.chkSave(mrTemp);

            SrvAsync.invokeRPC().saveRecord(mrTemp, setting_Mdl, new AsyncCallback<DmResult<BankReconciliationEntry_Mdl>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtClearDate).show();
                }

                @Override
                public void onSuccess(DmResult<BankReconciliationEntry_Mdl> result) {
                    BankReconciliationEntry_Mdl data = new BankReconciliationEntry_Mdl();
                    if (result.isStatus()) {
                        data = result.getData();
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getValue1(), result.getValue2(), result.getErrorMessage());
                    }
                    fillGrid(data);
                }
            });

        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getErrorSource(), e.getErrorSourceCode(), e.getMessage());
        }
    }

    private void topControlSaveError(int errorNo, String errorSource, String errorSourceCode, String errorMessage) {

        switch (errorNo) {
            case 100:
                int indexTemp = DmShared.iNull(errorSourceCode).intValue();
                final int gridRowIndex = indexTemp;
                new DmAlertMessageBox(errorMessage, new HideEvent.HideHandler() {
                    @Override
                    public void onHide(HideEvent event) {
                        ((CellSelectionModel) grdAcountDetail.getGrid().getSelectionModel()).selectCell(gridRowIndex, 1);
                    }
                }).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtClearDate).show();
                break;
        }
    }

    private void clearLbl() {
        lblAmtNotClg_Dr.setText("0");
        lblAmtNotClg_Cr.setText("0");
        lblAmtClg_Dr.setText("0");
        lblAmtClg_Cr.setText("0");
        lblClgAmt.setText("0");
        lblAmtDr.setText("0");
        lblAmtCr.setText("0");
    }

    private void calculate() {
        Double dblDrAmt = 0.0;
        Double dblCrAmt = 0.0;
        Double dblCgDrAmt = 0.0;
        Double dblCgCrAmt = 0.0;
        Double dblBankFinalAmt = 0.0;
        Boolean blnClgDate = false;
        List<GrdBankReconciliationEntry_Mdl> listGrid;
        String openAsPerBankCrDr = "";
        String balAsPerCompCrDr = "";

        clearLbl();

        listGrid = lstData;

        for (int i = 0; i < listGrid.size(); i++) {
            blnClgDate = true;
            if (DmShared.dNull(listGrid.get(i).getClearDate()).trim().equals("")) {
                blnClgDate = false;
            } else if (DmShared.convertDate(DmShared.dNull(listGrid.get(i).getClearDate()).trim()).getTime() > DmShared.convertDate(DmShared.dNull(toDate)).getTime()) {
                blnClgDate = false;
            }

            if ((!(listGrid.get(i).getSharedKeyFieldCode().trim().equals(""))) && (blnClgDate == false)) {
                dblDrAmt += listGrid.get(i).getDebit();
                dblCrAmt += listGrid.get(i).getCredit();
            } else {
                dblCgDrAmt += listGrid.get(i).getDebit();
                dblCgCrAmt += listGrid.get(i).getCredit();
            }
        }

        if (DmShared.vNull(opnAsPerBank) > 0) {
            openAsPerBankCrDr = "Dr";
        } else {
            openAsPerBankCrDr = "Cr";
        }
        if (DmShared.vNull(companyBal) > 0) {
            balAsPerCompCrDr = "Dr";
        } else {
            balAsPerCompCrDr = "Cr";
        }
        lblAmtDr.setText(NumberFormat.getFormat("0.00").format(dblDrAmt + dblCgDrAmt) + " Dr");
        lblAmtCr.setText(NumberFormat.getFormat("0.00").format(dblCrAmt + dblCgCrAmt) + " Cr");
        lblAmtNotClg_Dr.setText(NumberFormat.getFormat("0.00").format(dblDrAmt) + " Dr");
        lblAmtNotClg_Cr.setText(NumberFormat.getFormat("0.00").format(dblCrAmt) + " Cr");
        lblAmtClg_Dr.setText(NumberFormat.getFormat("0.00").format(dblCgDrAmt) + " Dr");
        lblAmtClg_Cr.setText(NumberFormat.getFormat("0.00").format(dblCgCrAmt) + " Cr");

        lblOpnAsPerBank.setText(NumberFormat.getFormat("0.00").format(Math.abs(Double.parseDouble(DmShared.vNull(opnAsPerBank).toString()))) + " " + openAsPerBankCrDr);
        lblBalCompanyBook.setText(NumberFormat.getFormat("0.00").format(Math.abs(Double.parseDouble(DmShared.vNull(companyBal).toString()))) + " " + balAsPerCompCrDr);

        dblBankFinalAmt = DmShared.vNull(opnAsPerBank) + DmShared.vNull(dblCgDrAmt) - DmShared.vNull(dblCgCrAmt);

        if (dblBankFinalAmt < 0.0) {
            lblClgAmt.setText(NumberFormat.getFormat("0.00").format(Math.abs(dblBankFinalAmt)) + " Cr");
        } else {
            lblClgAmt.setText(NumberFormat.getFormat("0.00").format(Math.abs(dblBankFinalAmt)) + " Dr");
        }
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(BankReconciliationEntry.this);
                }
            }
        }).show();
    }

    private BankReconciliationEntry_Mdl topControlSaveInitialize() {
        BankReconciliationEntry_Mdl mrTemp = new BankReconciliationEntry_Mdl();
        calculate();

        mrTemp.setClearDate(DmShared.dNull(txtClearDate.getText()));
        mrTemp.setOpnAsPerBank(DmClient.vNull(opnAsPerBank, "0.00"));
        mrTemp.setCompanyBal(DmClient.vNull(companyBal, "0.00"));

        mrTemp.getListBr().clear();
        mrTemp.getListBr().addAll(lstData);

        return mrTemp;
    }
    
    private void fillFilteredData() {
        String searchText = DmShared.xNull(txtSearchText.getText()).trim().toLowerCase();
        String pattern = "^(" + searchText.replace("%", ".*") + ".*)$";
        String matchWith = "";

        if (!DmShared.xNull(searchText).trim().equals("")) {
            List<GrdBankReconciliationEntry_Mdl> lstFilteredData = new ArrayList<>();
            for (GrdBankReconciliationEntry_Mdl gMdl : lstData) {
                switch (searchColumn) {
                    case VOUCHER_NO:
                        matchWith = gMdl.getSharedKeyFieldName();
                        break;
                    case DATE:
                        matchWith = gMdl.getVDate();
                        break;
                    case AC_NAME:
                        matchWith = gMdl.getAccountName();
                        break;
                    case FAVOR_OF:
                        matchWith = gMdl.getFavourOf();
                        break;
                    case CHEQUE_NO:
                        matchWith = gMdl.getChq();
                        break;
                    case CHEQUE_DATE:
                        matchWith = gMdl.getChqDate();
                        break;
                    case CLEAR_DATE:
                        matchWith = gMdl.getClearDate();
                        break;
                    case DEBIT:
                        matchWith = gMdl.getDebitTag();
                        break;
                    case CREDIT:
                        matchWith = gMdl.getCreditTag();
                        break;
                }

                if (DmClient.regExMatches(DmShared.xNull(matchWith).trim().toLowerCase(), pattern)) {
                    lstFilteredData.add(gMdl);
                }
            }

            grdAcountDetail.getGrid().getStore().clear();
            grdAcountDetail.getGrid().getStore().addAll(lstFilteredData);
        } else {
            grdAcountDetail.getGrid().getStore().clear();
            grdAcountDetail.getGrid().getStore().addAll(lstData);
        }

    }
    
}
