package dataman.accounts.budgetAllocationEntry.client;

import dataman.accounts.budgetAllocationEntry.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import dataman.dmBase.self.shared.*;
import java.util.List;
import dataman.accounts.budgetAllocationEntry.shared.BudgetAllocationEntry_Srv;
import dataman.accounts.budgetAllocationEntry.shared.BudgetAllocationEntry_SrvAsync;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import dataman.dmToolkit.walkThrough.client.WalkThrough;
import java.util.HashMap;
import dataman.libraryMenu.self.shared.lmAccounts;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;

public final class BudgetAllocationEntry extends BudgetAllocationEntry_Dsg implements BlurHandler, ClickHandler {

    private final ServiceInvoker<BudgetAllocationEntry_SrvAsync> SrvAsync;
    private final BudgetAllocationEntryGridEdit<BudgetAllocationEntry_Mdl, BudgetAllocationEntry_Dsg.GrdBudgetAllocationDetail_PrpAcs> budgetEntryGridEdit = new BudgetAllocationEntryGridEdit(grdBudgetAllocationDetail);
    private final WalkThrough walkThrough;
    private DefaultValue_Mdl dvm;
    private String recIdPrefix = "", recIdValue = "", vNo = "";
    private LibraryVoucherCategory pvc = new LibraryVoucherCategory();
    private String voucherCategory = "";
    private String moduleName = new lmAccounts().moduleName;
    private boolean isApprove = false;

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);
        for (String controlPermission : menuPermission.getControlPermission()) {
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(approvedByBlog)) {
                isApprove = true;
            }
        }
        topControl.setUserPermission(menuPermission, new VoucherPermissionHandler(DmClient.getVoucherPermission(new String[]{voucherCategory})) {
            @Override
            public String getVoucherType() {
                return txtHAllocationType.getTag();
            }

            @Override
            public String getVoucherDate() {
                return txtAllocationDate.getText();
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
                txtAllocationNo.setEnabled(false);
                txtHBudgetName.setFocus(true);
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                if (DmShared.xNull(userControl.getTextBlog(approvedByBlog)).equalsIgnoreCase("")) {
                    txtHAllocationType.setEnabled(false);
                    txtAllocationNo.setEnabled(false);
                    txtHBudgetName.setFocus(true);
                    grdBudgetAllocationDetail.getGrid().getStore().add(new GrdBudgetAllocationDetail_Mdl());

                } else {
                    topControl.btnClick("");
                    new DmAlertMessageBox("Record is approved. You can't modify this record.", (DmTextBox) null).show();
                }

            }
        });

        topControl.addDeleteHandeller(new DmTopControl.eDelete() {
            @Override
            public void deleteClicked() {
                if (DmShared.xNull(userControl.getTextBlog(approvedByBlog)).equalsIgnoreCase("")) {
                    new DmConfirmMessageBox("Are you sure? You want to delete this record.", new DialogHideEvent.DialogHideHandler() {

                        @Override
                        public void onDialogHide(DialogHideEvent event) {
                            if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                                topControlDelete(topControl.getDocId());
                            }
                        }
                    }).show();
                } else {
                    topControl.btnClick("");
                    new DmAlertMessageBox("Record is approved. You can't delete this record.", (DmTextBox) null).show();
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
                saveValidation();
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(BudgetAllocationEntry.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void prnRecord(boolean printToPrinter) {

        HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
        hashMapFilter.put("docId", new FieldUDT_Mdl("Indent No.", DmShared.xNull(txtAllocationNo.getText()), DmShared.xNull(topControl.getDocId())));
        new OpenSourceReport(txtHAllocationType.getTag(), "", widgetName, widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
            @Override
            public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                DmClient.localSettingPrintMap(reportProperty, moduleName, widgetName, "Print");
                return reportProperty;
            }
        }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
    }

    private void textHelpEvents() {
        walkThrough.addCallBack(new WalkThrough.CallBack() {
            @Override
            public void onClose() {
                topControlMoveRec(topControl.getDocId());
            }
        });
        txtHAllocationType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAllocationType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAllocationType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHAllocationType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHAllocationType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                if (tRow != null) {
                    txtHAllocationType.setTag2nd(tRow.getSharedPreparedBy());
                    voucherCategory=tRow.getSharedPreparedBy();
                }
            }
        });

        txtHBudgetName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHBudgetName(voucherCategory,helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHBudgetName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHBudgetName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHBudgetName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
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

    private void gridEvents() {

        budgetEntryGridEdit.addCallBack(new BudgetAllocationEntryGridEdit.CallBack() {
            @Override
            public void onOk(GrdBudgetAllocationDetail_Mdl grdVoucherEntry) {
                grdBudgetAllocationDetail.commitChanges();
                calculateTotal();
            }

            @Override
            public void onDelete() {
                grdBudgetAllocationDetail.commitChanges();
            }
        });

        grdBudgetAllocationDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showBudgetHeadGridEdit(event.getRowIndex());
            }

        });

        grdBudgetAllocationDetail.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showBudgetHeadGridEdit(grdBudgetAllocationDetail.getGrid().getStore().indexOf(grdBudgetAllocationDetail.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
    }

    private void showBudgetHeadGridEdit(int rowIndex) {

        if (topControl.getMode().equalsIgnoreCase("Browse")) {
            budgetEntryGridEdit.show(DmShared.xNull(txtHBudgetName.getText()), voucherCategory, rowIndex, false, false, false);
        } else {
            budgetEntryGridEdit.show(DmShared.xNull(txtHBudgetName.getText()), voucherCategory, rowIndex, true, true, true);
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtAllocationDate)) {
            txtAllocationDate.setText(DmClient.returnDate(txtAllocationDate.getText()));
        } else if (event.getSource().equals(txtFromDate)) {
            txtFromDate.setText(DmClient.returnDate(txtFromDate.getText()));
        } else if (event.getSource().equals(txtToDate)) {
            txtToDate.setText(DmClient.returnDate(txtToDate.getText()));
        }
    }

    public BudgetAllocationEntry(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(BudgetAllocationEntry_Srv.class), BudgetAllocationEntry.class.getName());
        walkThrough = new WalkThrough(SrvAsync);
        this.asWidget().setTitle(widgetName);
        txtAllocationDate.addBlurHandler(this);
        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);
        btnApprovedBy.addClickHandler(this);
        topControlEvents(menuPermission);
        findEvents();
        textHelpEvents();
        gridEvents();
        iniDefaultValue(swi);
    }

    private void iniDefaultValue(final SetWidgetInvoker swi) {
        SrvAsync.invokeRPC(true).getDefaultValues(new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    dvm = result.getData();
                    voucherCategory = dvm.getVoucherCategory();
                    swi.setWidget(BudgetAllocationEntry.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    public void saveModifyReason() {
        if (topControl.getMode().equalsIgnoreCase("New")) {
            topControlSave();
        } else {
//            showModifyReason();
        }

    }

    private void saveValidation() {
        try {
            chkValidation();
            saveModifyReason();
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getErrorSource(), e.getErrorSourceCode(), e.getMessage());
        }
    }

    private BudgetAllocationEntry_Mdl chkValidation() {
        BudgetAllocationEntry_Mdl mrTemp;
        mrTemp = topControlSaveInitialize();
        BudgetAllocationEntry_Vld.chkSave(mrTemp);
        return mrTemp;
    }

    private BudgetAllocationEntry_Mdl topControlSaveInitialize() {
        BudgetAllocationEntry_Mdl mrTemp = new BudgetAllocationEntry_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
            mrTemp.getRecid().setRecIdValue(DmShared.xNull(txtAllocationNo.getText()));
            mrTemp.getRecid().setCounter(DmShared.iNull(vNo).longValue());
            mrTemp.getRecid().setPrefix(DmShared.xNull(recIdPrefix));
        }
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtAllocationNo.getText()));
        mrTemp.setBudgetNameText(DmShared.xNull(txtHBudgetName.getText()));
        mrTemp.setDtAllocation(DmShared.dNull(txtAllocationDate.getText()));
        mrTemp.setAllocationTypeTag(DmShared.xNull(txtHAllocationType.getTag()));
        mrTemp.setAllocationTypeText(DmShared.xNull(txtHAllocationType.getText()));
        mrTemp.setFromDate(DmShared.dNull(txtFromDate.getText()));
        mrTemp.setToDate(DmShared.dNull(txtToDate.getText()));
        mrTemp.setVoucherCategory(voucherCategory);
        mrTemp.setTotalBudget(DmShared.vNull(lblTotalValue.getText()).toString());

        grdBudgetAllocationDetail.getGrid().getStore().commitChanges();
        mrTemp.getGrdBudgetHeadDetails().addAll(grdBudgetAllocationDetail.getGrid().getStore().getAll());
        return mrTemp;
    }

    private void topControlSave() {
        BudgetAllocationEntry_Mdl mrTemp;
        try {
            mrTemp = chkValidation();
            SrvAsync.invokeRPC().saveRecord(mrTemp, DmClient.getVoucherPrefix(), isApprove, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtAllocationDate).show();
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
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getErrorSource(), e.getErrorSourceCode(), e.getMessage());
        }
    }

    private void topControlSaveError(int errorNo, String errorSource, String errorSourceCode, String errorMessage) {
        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtHAllocationType).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtAllocationDate).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtHBudgetName).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHBudgetName).show();
                break;
        }
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
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<BudgetAllocationEntry_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<BudgetAllocationEntry_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<BudgetAllocationEntry_Mdl> result) {
        BudgetAllocationEntry_Mdl data;

        if (result.isStatus()) {
            data = result.getData();

            userControl.setTextBlog(preparedByBlog, data.getPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getModifiedBy());
            userControl.setTextBlog(approvedByBlog, data.getApprovedBy());

            topControl.setDocId(data.getSharedKeyFieldCode());
            txtHAllocationType.setText(data.getAllocationTypeText());
            txtHAllocationType.setTag(data.getAllocationTypeTag());
            txtHAllocationType.setTag2nd(data.getVoucherCategory());
            voucherCategory=data.getVoucherCategory();
            txtAllocationNo.setText(data.getRecid().getRecIdValue());
            txtAllocationDate.setText(data.getDtAllocation());
            txtHBudgetName.setText(data.getBudgetNameText());
            txtFromDate.setText(data.getFromDate());
            txtToDate.setText(data.getToDate());
            recIdPrefix = DmShared.xNull(data.getRecid().getPrefix());
            vNo = DmShared.iNull(data.getRecid().getCounter()).toString();
            recIdValue = DmShared.xNull(data.getRecid().getRecIdValue());

            grdBudgetAllocationDetail.getGrid().getStore().clear();
            grdBudgetAllocationDetail.getGrid().getStore().addAll(data.getGrdBudgetHeadDetails());
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        calculateTotal();
        topControl.setDisplay(true);
    }

    private void topControlDelete(String strKey) {
        SrvAsync.invokeRPC().deleteRecord(strKey, new AsyncCallback<DmResult<String>>() {

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

    @Override
    public Widget asWidget() {
        return flwForm;
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
                            topControlSaveError(result.getErrorNumber(), "", "", result.getErrorMessage());
                        }
                    }
                });
            }
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), "", "", e.getMessage());
        }
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    private void onAdd() {
        clear();
        grdBudgetAllocationDetail.getGrid().getStore().add(new GrdBudgetAllocationDetail_Mdl());
        txtAllocationNo.setTag(DmClient.getVoucherPrefix());
        txtAllocationDate.setText(DmClient.getLoginDate());
        txtHAllocationType.setTag(DmShared.xNull(dvm.getAllocationTypeTag()));
        txtHAllocationType.setText(DmShared.xNull(dvm.getAllocationTypeText()));
        txtHAllocationType.setTag2nd(DmShared.xNull(dvm.getVoucherCategory()));
        voucherCategory=dvm.getVoucherCategory();
    }

    private void clear() {
        grdBudgetAllocationDetail.getGrid().getStore().clear();
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        userControl.setTextBlog(approvedByBlog, "");
        lblTotalValue.setText("0.00");
        recIdPrefix = "";
        recIdValue = "";
        vNo = "";
    }

    private void calculateTotal() {
        double totBudget = 0;
        for (int i = 0; i < grdBudgetAllocationDetail.getGrid().getStore().size(); i++) {
            if (DmShared.xNull(grdBudgetAllocationDetail.getGrid().getStore().get(i).getPlusMinus()).equalsIgnoreCase("0") && 
                    DmShared.vNull(grdBudgetAllocationDetail.getGrid().getStore().get(i).getBudget()).doubleValue()>0) {
                totBudget -= DmShared.vNull(grdBudgetAllocationDetail.getGrid().getStore().get(i).getBudget());
            }
            if (DmShared.xNull(grdBudgetAllocationDetail.getGrid().getStore().get(i).getPlusMinus()).equalsIgnoreCase("1") && 
                    DmShared.vNull(grdBudgetAllocationDetail.getGrid().getStore().get(i).getBudget()).doubleValue()>0) {
                totBudget += DmShared.vNull(grdBudgetAllocationDetail.getGrid().getStore().get(i).getBudget());
            }
        }
        lblTotalValue.setText(DmClient.vNull(totBudget, "0.00"));
    }
}
