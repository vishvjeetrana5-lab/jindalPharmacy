package dataman.accounts.bankLetterCreation.client;

import dataman.accounts.bankLetterCreation.shared.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.event.logical.shared.AttachEvent;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import java.util.List;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.modifyReason.client.ModifyReason;
import dataman.dmToolkit.modifyReason.shared.ModifyReason_Mdl;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import dataman.dmToolkit.walkThrough.client.WalkThrough;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.util.ArrayList;
import java.util.HashMap;

public final class BankLetterCreation extends BankLetterCreation_Dsg implements IsWidget, ClickHandler, BlurHandler, AttachEvent.Handler {
    //<editor-fold defaultstate="collapsed" desc="Search Column [Enum]">

    private enum SearchColumn {

        instNo(2), payNo(3), date(4), module(5), supplier(6), paidAmt(7), payMode(8), acName(9), remark(10);

        int colIndex;

        SearchColumn(int p) {
            colIndex = p;
        }

        public int getIntValue() {
            return this.colIndex;
        }
    }
    //</editor-fold>
    ServiceInvoker<BankLetterCreation_SrvAsync> SrvAsync;

    protected DefaultValue_Mdl defaultValue;
    final BankLetterCreationGridEdit<GrdBankLetterCreation_Mdl, GrdMedicineDetail_PrpAcs> gridEdit = new BankLetterCreationGridEdit(grdBillDetail);
    WalkThrough walkThrough;
    String vNo = "", recIdPrefix = "", recIdValue = "";
    private String moduleName = "Pharmacy";
    private boolean isApprove = false;
    private boolean isCheckPermission = false;
    private SearchColumn searchColumn;
    private final List<GrdBankLetterCreation_Mdl> lstData = new ArrayList<>();
    private String selectedChequeNo = "";
    private String selectedDrAccount = "";

    private final ModifyReason modifyReason = new ModifyReason();
    private ModifyReason_Mdl modifyReasonMdl;

    List<GrdBankLetterCreation_Mdl> lstPrev;
    String chqueNo = "", bankCode = "";
    SetWidgetInvoker swi;

    private PrintPopup printPopup = new PrintPopup();

    public BankLetterCreation(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(BankLetterCreation_Srv.class), BankLetterCreation.class.getName());
        this.asWidget().setTitle(widgetName);
        walkThrough = new WalkThrough(SrvAsync);

//        menuPermission.setUserPermission(menuPermission.getUserPermission().replace("D", ""));
        topControlEvents(menuPermission);
        findEvents();
        textHelpEvents();
        gridEvents();
        blurHandler();
        setPermission(menuPermission);
        defaultValues(swi);

        this.swi = swi;
    }

    private void showModifyReason() {
        modifyReason.showNew(topControl.getMode(), topControl.getDocId(), "", "");

        modifyReason.addCallBack(new ModifyReason.CallBack() {

            @Override
            public void onOk(ModifyReason_Mdl modifyReason) {
                BankLetterCreation.this.modifyReasonMdl = modifyReason;
                if (topControl.getMode().equalsIgnoreCase("Browse") && DmShared.iNull(topControl.getDocId()).longValue() > 0) {
                    BankLetterCreation_Mdl mrTemp = topControlSaveInitialize();
                    mrTemp.setSharedKeyFieldName(DmShared.xNull(txtBankLetterNo.getText()));
                    topControlDelete(topControl.getDocId(), mrTemp);
                } else {
                    topControlSave();
                }
            }
        });
    }

    private void blurHandler() {
        flwForm.addAttachHandler(this);
        btnApprovedBy.addClickHandler(this);
        txtVDate.addBlurHandler(this);
        txtFromFinalPayment.addBlurHandler(this);
        txtToFinalPayment.addBlurHandler(this);
        txtSearchText.addBlurHandler(this);
    }

    private void topControlDelete(String strKey, BankLetterCreation_Mdl mrTemp) {
        SrvAsync.invokeRPC().deleteRecord(topControl.getPermissions(), strKey, mrTemp, new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    clear();
                    topControlCard();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);
        topControl.setDeleteCaptionToDiscard();

        for (String controlPermission : menuPermission.getControlPermission()) {
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(approvedByBlog)) {
                isApprove = true;
            }
        }

        topControl.addDeleteHandeller(new DmTopControl.eDelete() {
            @Override
            public void deleteClicked() {

                if (!DmShared.xNull(cancelBy.getText()).equalsIgnoreCase("")) {
                    new DmAlertMessageBox("This Record has been discarded.You can't edit this record.", (DmTextBox) null).show();
                    topControl.btnClick("");
                    return;
                } else {
                    new DmConfirmMessageBox("Are you sure? You want to Discards this record.", new DialogHideEvent.DialogHideHandler() {

                        @Override
                        public void onDialogHide(DialogHideEvent event) {
                            if (event.getHideButton() == Dialog.PredefinedButton.YES) {
//                            topControlDelete(topControl.getDocId());
                                showModifyReason();
                            }
                        }
                    }).show();

                }

            }
        });

        topControl.setUserPermission(menuPermission, new VoucherPermissionHandler(DmClient.getVoucherPermission(new String[]{new LibraryVoucherCategory().BILL_HANDLE})) {
            @Override
            public String getVoucherType() {
                return defaultValue.getBankLetterCreationVTypeCode();
            }

            @Override
            public String getVoucherDate() {
                return txtVDate.getText();
            }

            @Override
            public String getVoucherValue() {
                return "";
            }
        });

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                onAdd();
                txtFromFinalPayment.setFocus(true);
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {

                if (!DmShared.xNull(cancelBy.getText()).equalsIgnoreCase("")) {
                    new DmAlertMessageBox("This Record has been discarded.You can't edit this record.", (DmTextBox) null).show();
                    topControl.btnClick("");
                    return;
                }

                if (DmShared.xNull(userControl.getTextBlog(approvedByBlog)).equalsIgnoreCase("")) {
                    lstPrev = new ArrayList<>();

                    for (GrdBankLetterCreation_Mdl nodeGrd : grdBillDetail.getGrid().getStore().getAll()) {
                        lstPrev.add(nodeGrd);
                    }

                    chqueNo = lstPrev.get(0).getChqNo();
                    bankCode = lstPrev.get(0).getBankCode();

                    txtBankLetterNo.setEnabled(false);
                    txtVDate.setEnabled(false);

                } else {
                    topControl.btnClick("");
                    new DmAlertMessageBox("Record is approved. You can't modify this record.", (DmTextBox) null).show();
                }

            }
        });

        topControl.addCardHandeller(new DmTopControl.eCard() {
            @Override
            public void cardClicked() {
                topControlCard();
            }
        });

        topControl.addFindHandeller(new DmTopControl.eFind() {
            @Override
            public void findClicked() {
                flwForm.remove(vrtInnerForm);
                flwForm.add(dmFind);
            }
        });

        topControl.addMoveRecHandeller(new DmTopControl.iMoveRec() {
            @Override
            public void moveRec(String strFetchCode) {
                topControlMoveRec(strFetchCode);
            }
        });

        topControl.addPrintHandeller(new DmTopControl.ePrint() {

            @Override
            public void printClicked() {

                prnRecord(false);
            }

        });

        topControl.addSaveHandeller(new DmTopControl.eSave() {
            @Override
            public void saveClicked() {
//                topControlSave();

                if (topControl.getMode().equalsIgnoreCase("Edit")) {
                    showModifyReason();
                } else {
                    topControlSave();
                }
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(BankLetterCreation.this);
            }
        });

        topControl.addRefershHandeller(new DmTopControl.eRefersh() {
            @Override
            public void refreshClicked() {
                topControlMoveRec(topControl.getDocId());
            }
        });

        topControl.setDisplay(true);
    }

    private void setPermission(UserMenu menuPermission) {
        for (String controlPermission : menuPermission.getControlPermission()) {
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(isCheckPermissionBlog)) {
                isCheckPermission = true;
            }
        }
    }

    private void textHelpEvents() {

        walkThrough.addCallBack(new WalkThrough.CallBack() {
            @Override
            public void onClose() {
                topControlMoveRec(topControl.getDocId());
            }
        });

    }

    private void findEvents() {
        dmFind.setTitle(this.asWidget().getTitle());

        dmFind.addHelpGridCallBack(new DmFind.FindCallBack<Find_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getFind(topControl.getPermissions(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Find_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), dmFind.getTextBox()).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<Find_Mdl>> result) {
                        if (result.isStatus()) {
                            dmFind.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), dmFind.getTextBox()).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, Find_Mdl tRow) {
                flwForm.remove(dmFind);
                flwForm.add(vrtInnerForm);

                if (blnStatus) {
                    topControlMoveRec(tRow.getSharedKeyFieldCode());
                }
            }
        });
    }

    private void setSearchColumn(SearchColumn searchColumn) {
        this.searchColumn = searchColumn;
        if (this.searchColumn == null) {
            this.searchColumn = SearchColumn.payNo;
        }
        DmColumnConfig col = ((DmColumnConfig) grdBillDetail.getGrid().getColumnModel().getColumn(searchColumn.getIntValue()));
        for (ColumnConfig col1 : grdBillDetail.getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        grdBillDetail.getGrid().getView().refresh(true);
        txtSearchText.setText("");
        txtSearchText.getElement().setAttribute("placeholder", DmShared.xNull(col.getHeader().asString()));
        txtSearchText.setFocus(true);
    }

    private void fillFilteredData() {

        //     instNo(2), payNo(3), date(4), module(5), supplier(6), paidAmt(7), payMode(8), acName(9), remark(10);
        String searchText = DmShared.xNull(txtSearchText.getText()).trim().toLowerCase();
        String pattern = "^(" + searchText.replace("%", ".*") + ".*)$";
        String matchWith = "";

        if (!DmShared.xNull(searchText).trim().equals("")) {
            List<GrdBankLetterCreation_Mdl> lstFilteredData = new ArrayList<>();
            for (GrdBankLetterCreation_Mdl gMdl : lstData) {
                switch (searchColumn) {

                    case instNo:
                        matchWith = gMdl.getChqNo();
                        break;
                    case payNo:
                        matchWith = gMdl.getSharedKeyFieldName();
                        break;
                    case date:
                        matchWith = gMdl.getPayDate();
                        break;
                    case module:
                        matchWith = gMdl.getModule();
                        break;
                    case supplier:
                        matchWith = gMdl.getSupplier();
                        break;
                    case paidAmt:
                        matchWith = gMdl.getAmtPaid();
                        break;
                    case payMode:
                        matchWith = gMdl.getPayMode();
                        break;
                    case acName:
                        matchWith = gMdl.getBank();
                        break;
                    case remark:
                        matchWith = gMdl.getRemark();
                        break;

                }

                if (DmClient.regExMatches(DmShared.xNull(matchWith).trim().toLowerCase(), pattern)) {
                    lstFilteredData.add(gMdl);
                }
            }

            grdBillDetail.getGrid().getStore().clear();
            grdBillDetail.getGrid().getStore().addAll(lstFilteredData);
        } else {
            grdBillDetail.getGrid().getStore().clear();
            grdBillDetail.getGrid().getStore().addAll(lstData);
        }

        if (topControl.getMode().equalsIgnoreCase("Edit")) {

            if (grdBillDetail.getGrid().getStore().size() > 0) {
                for (GrdBankLetterCreation_Mdl nodeLstPrev : lstPrev) {
                    for (GrdBankLetterCreation_Mdl nodeGrd : grdBillDetail.getGrid().getStore().getAll()) {
                        if (DmShared.xNull(nodeLstPrev.getChqNo()).equalsIgnoreCase(nodeGrd.getChqNo()) && DmShared.xNull(nodeLstPrev.getBankCode()).equalsIgnoreCase(nodeGrd.getBankCode())
                                && DmShared.xNull(nodeLstPrev.getSharedKeyFieldName()).equalsIgnoreCase(nodeGrd.getSharedKeyFieldName())) {
                            nodeGrd.setCheck(true);
                            nodeGrd.setRemark(nodeLstPrev.getRemark());
                        }
                    }
                }
            }

            grdBillDetail.commitChanges();
        }

    }

    private void gridEvents() {
        setSearchColumn(SearchColumn.payNo);

        txtSearchText.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
                    ((CellSelectionModel) grdBillDetail.getGrid().getSelectionModel()).selectCell(0, 1);
                } else {
                    fillFilteredData();
                }
            }
        });
        gridEdit.addCallBack(new BankLetterCreationGridEdit.CallBack() {
            @Override
            public void onOk(GrdBankLetterCreation_Mdl grdVoucherEntry) {
                grdBillDetail.commitChanges();
            }

            @Override
            public void onDelete() {
                grdBillDetail.commitChanges();
            }
        });
        grdBillDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                if (event.getColumnIndex() == ColCheckPBill) {
                    grdCheckedValue(event.getRowIndex());
                } else {
                    showRequisitionGridEdit(event.getRowIndex());
                }

            }
        });
        grdBillDetail.getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {

                if (event.getColumnIndex() == 1) {
                    new DmAlertMessageBox("Search Not Available.", (DmTextBox) null).show();
                    return;
                } else {
                    for (SearchColumn column : SearchColumn.values()) {
                        if (column.getIntValue() == event.getColumnIndex()) {
                            setSearchColumn(column);
                            break;
                        }
                    }

                }
            }
        });
        grdBillDetail.getGrid().addDomHandler(new KeyDownHandler() {
            CellSelectionModel<GrdBankLetterCreation_Mdl> cellModel;

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    cellModel = (CellSelectionModel<GrdBankLetterCreation_Mdl>) grdBillDetail.getGrid().getSelectionModel();
                    if (cellModel.getSelectCell().getCell() == ColCheckPBill) {
                        grdCheckedValue(cellModel.getSelectCell().getRow());

                    } else {
                        showRequisitionGridEdit(grdBillDetail.getGrid().getStore().indexOf(grdBillDetail.getGrid().getSelectionModel().getSelectedItem()));
                    }

                }
            }
        }, KeyDownEvent.getType());
    }

    private void grdCheckedValue(int currentRowIndex) {
        GrdBankLetterCreation_Mdl gbdm;
        int scrollPos;
        if (DmShared.xNull(topControl.getMode()).equalsIgnoreCase("BROWSE")) {
            return;
        }
        if (!isCheckPermission) {
            new DmAlertMessageBox("You don't have permission for this action.", (DmTextBox) null).show();
            return;
        }
        if (!selectedChequeNo.equalsIgnoreCase("") && !selectedChequeNo.equalsIgnoreCase(grdBillDetail.getGrid().getStore().get(currentRowIndex).getChqNo()) && !grdBillDetail.getGrid().getStore().get(currentRowIndex).isCheck()) {
            new DmAlertMessageBox("Please select the records for same Inst. No.", (DmTextBox) null).show();
            return;
        }
        if (!selectedDrAccount.equalsIgnoreCase("") && !selectedDrAccount.equalsIgnoreCase(grdBillDetail.getGrid().getStore().get(currentRowIndex).getBankCode()) && !grdBillDetail.getGrid().getStore().get(currentRowIndex).isCheck()) {
            new DmAlertMessageBox("Please select the records for same Dr.Account No.", (DmTextBox) null).show();
            return;
        }

        gbdm = grdBillDetail.getGrid().getStore().get(currentRowIndex);

        grdBillDetail.getGrid().getStore().get(currentRowIndex).setCheck(gbdm.isCheck() ? false : true);

        Element scroller = grdBillDetail.getGrid().getView().getEditorParent();
        scrollPos = scroller.getScrollTop();
        grdBillDetail.commitChanges();
        grdBillDetail.getGrid().getView().getEditorParent().setScrollTop(scrollPos);

        if (grdBillDetail.getGrid().getStore().get(currentRowIndex).isCheck()) {
            selectedChequeNo = grdBillDetail.getGrid().getStore().get(currentRowIndex).getChqNo();
            selectedDrAccount = grdBillDetail.getGrid().getStore().get(currentRowIndex).getBankCode();
        } else if (!grdBillDetail.getGrid().getStore().get(currentRowIndex).isCheck()) {
            selectedChequeNo = "";
            selectedDrAccount = "";
        }

    }

    private void showRequisitionGridEdit(int currentRowIndex) {

        if (DmShared.xNull(txtFromFinalPayment.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please select From Bill Date", txtFromFinalPayment).show();
            return;
        }
        if (DmShared.xNull(txtToFinalPayment.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please select To Bill Date", txtToFinalPayment).show();
            return;
        }
        if (topControl.getMode().equalsIgnoreCase("Browse")) {
            gridEdit.show(currentRowIndex, false, false, false, this);
        } else {
            gridEdit.show(currentRowIndex, true, false, false, this);
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnApprovedBy)) {
            if (DmShared.iNull(topControl.getDocId()).longValue() != 0) {

                if (DmShared.xNull(userControl.getTextBlog(approvedByBlog)).equalsIgnoreCase("")) {
                    setApproved(topControl.getDocId(), true);
                } else {
                    setApproved(topControl.getDocId(), false);
                }

            }
        }
    }

    private void setApproved(String docId, boolean isApprove) {
        try {
            if (DmShared.iNull(txtWTACode.getText()).longValue() != 0) {
                walkThrough.show(txtWTACode.getText());
            } else {
                SrvAsync.invokeRPC().setApproval(DmClient.getLoginUserName(), isApprove, docId, new AsyncCallback<DmResult<String>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResult<String> result) {
                        if (result.isStatus()) {
                            topControlMoveRec(result.getData());
                        } else {
                            topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                        }
                    }
                });
            }
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
        } else {
//            gridEdit.hide();
        }
    }

    public void defaultValues(final SetWidgetInvoker swi) {
        SrvAsync.invokeRPC().getDefaultValue(new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    defaultValue = result.getData();
                    swi.setWidget(BankLetterCreation.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private void topControlCard() {
        SrvAsync.invokeRPC().getNavigator(topControl.getPermissions(), new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<KeyFieldExtended>> result) {
                if (result.isStatus()) {
                    if (result.isStatus()) {
                        topControl.setNavigator(result.getData());
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            }
        });
    }

    private void topControlMoveRec(String strKey) {
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<BankLetterCreation_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<BankLetterCreation_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<BankLetterCreation_Mdl> result) {
        clear();
        BankLetterCreation_Mdl data;
        if (result.isStatus()) {
            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());

            txtFromFinalPayment.setText(data.getFromFinalPaymentDate());
            txtToFinalPayment.setText(data.getToFinalPaymentDate());

            txtBankLetterNo.setText(data.getSharedKeyFieldName());
            txtVDate.setText(data.getDate());
            txtWTACode.setText(data.getWtaCode());
            userControl.setTextBlog(preparedByBlog, DmShared.xNull(data.getSharedPreparedBy()));
            userControl.setTextBlog(modifiedByBlog, DmShared.xNull(data.getSharedModifiedBy()));
            userControl.setTextBlog(approvedByBlog, DmShared.xNull(data.getApprovedBy()));
            if (DmShared.iNull(data.getIsDiscarded()).intValue() == 1) {
                cancelBy.setText("Discarded");
            } else {
                cancelBy.setText("");
            }
            fillGrid(data.getGrdData());
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
    }

    private void topControlSave() {
        BankLetterCreation_Mdl mrTemp;

        try {

            mrTemp = topControlSaveInitialize();
            BankLetterCreation_Vld.chkSave(mrTemp);

            SrvAsync.invokeRPC().saveRecord(mrTemp, DmClient.getVoucherPrefix(), isApprove, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtVDate).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        topControl.setDocId(result.getData());

                        if (topControl.getMode().equalsIgnoreCase("New")) {
                            topControl.btnClick("New");
                        } else {
                            topControlMoveRec(topControl.getDocId());
                        }
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void topControlSaveError(int errorNo, String errorMessage) {
        topControlSaveError(errorNo, "", errorMessage);
    }

    private void topControlSaveError(int errorNo, final String gridRowIndex, String errorMessage) {
        switch (errorNo) {

            case 100:
                new DmAlertMessageBox(errorMessage, txtVDate).show();
                break;

            case 200:
                new DmAlertMessageBox(errorMessage, new HideEvent.HideHandler() {
                    @Override
                    public void onHide(HideEvent event) {
                        ((CellSelectionModel) grdBillDetail.getGrid().getSelectionModel()).selectCell(DmShared.iNull(gridRowIndex).intValue(), 1);
                    }
                }).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtVDate).show();
                break;
        }
    }

    private BankLetterCreation_Mdl topControlSaveInitialize() {
        BankLetterCreation_Mdl mrTemp = new BankLetterCreation_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }

        grdBillDetail.getGrid().getStore().commitChanges();

        mrTemp.setDate(DmShared.xNull(txtVDate.getText()));
        mrTemp.setFromFinalPaymentDate(DmShared.xNull(txtFromFinalPayment.getText()));
        mrTemp.setToFinalPaymentDate(DmShared.xNull(txtToFinalPayment.getText()));
        mrTemp.setWtaCode(DmShared.xNull(txtWTACode.getText()));
        mrTemp.setvType(DmShared.xNull(defaultValue.getBankLetterCreationVTypeCode()));

        List<GrdBankLetterCreation_Mdl> lstTemp = new ArrayList<>();

        for (GrdBankLetterCreation_Mdl node : grdBillDetail.getGrid().getStore().getAll()) {
            if (node != null) {

                if (node.isCheck()) {
                    lstTemp.add(node);
                }

            }
        }

        mrTemp.getGrdData().addAll(lstTemp);

        if (modifyReasonMdl != null) {
            mrTemp.setModifyReason(modifyReasonMdl);
        }

        return mrTemp;
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtVDate)) {
            txtVDate.setText(DmClient.returnDate(txtVDate.getText()));

        }
        if (event.getSource().equals(txtFromFinalPayment)) {
            txtFromFinalPayment.setText(DmClient.returnDate(txtFromFinalPayment.getText()));
            getFinalPayment();

        }
        if (event.getSource().equals(txtToFinalPayment)) {
            txtToFinalPayment.setText(DmClient.returnDate(txtToFinalPayment.getText()));
            getFinalPayment();

        }

    }

    private void clear() {
        modifyReasonMdl = new ModifyReason_Mdl();
        grdBillDetail.getGrid().getStore().clear();
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        userControl.setTextBlog(approvedByBlog, "");
    }

    private void onAdd() {
        clear();

        txtVDate.setText(DmClient.getLoginDate());
        txtBankLetterNo.setEnabled(false);
        txtVDate.setEnabled(false);

    }

    private void getFinalPayment() {
        if (DmShared.dNull(txtFromFinalPayment.getText()).equalsIgnoreCase("")
                || DmShared.dNull(txtToFinalPayment.getText()).equalsIgnoreCase("")) {
            return;
        }
        if (DmShared.convertDate(txtFromFinalPayment.getText()).getTime() > DmShared.convertDate(txtToFinalPayment.getText()).getTime()) {
            return;
        }

        getFinalPaymentGrd();
    }

    private void getFinalPaymentGrd() {

        SrvAsync.invokeRPC().getFinalPaymentDetail(DmShared.xNull(txtFromFinalPayment.getText()), DmShared.xNull(txtToFinalPayment.getText()), topControl.getMode(), chqueNo, bankCode, new AsyncCallback<DmResult<List<GrdBankLetterCreation_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdBankLetterCreation_Mdl>> result) {
                if (result.isStatus()) {

                    fillGrid(result.getData());
                    if (grdBillDetail.getGrid().getStore().getAll().size() <= 0) {
                        new DmAlertMessageBox("No Records.", txtVDate).show();
                        return;
                    }

                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtFromFinalPayment).show();
                }
            }
        }
        );

    }

    private void fillGrid(List<GrdBankLetterCreation_Mdl> grdLst) {
        lstData.clear();
        lstData.addAll(grdLst);
        txtSearchText.setText("");
        selectedChequeNo = "";
        fillFilteredData();
    }

    private void prnRecord(boolean printToPrinter) {

        if (!DmShared.xNull(cancelBy.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("This Record has been discarded.You can't Print this record.", (DmTextBox) null).show();
            topControl.btnClick("");
            return;
        }

        if (DmShared.xNull(userControl.getTextBlog(approvedByBlog)).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Record is not Approve. You can not print this record.", txtToFinalPayment).show();

            return;
        }

        printPopup.show(this);

    }

}
