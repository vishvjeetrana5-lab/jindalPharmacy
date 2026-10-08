package dataman.accounts.budgetDisbursement.client;

import dataman.accounts.budgetDisbursement.client.*;
import dataman.accounts.budgetDisbursement.shared.*;
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
import dataman.accounts.budgetDisbursement.shared.BudgetDisbursement_Srv;
import dataman.accounts.budgetDisbursement.shared.BudgetDisbursement_SrvAsync;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import dataman.dmToolkit.walkThrough.client.WalkThrough;
import java.util.HashMap;
import dataman.libraryMenu.self.shared.lmAccounts;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;

public final class BudgetDisbursement extends BudgetDisbursement_Dsg implements BlurHandler, ClickHandler {

    private final ServiceInvoker<BudgetDisbursement_SrvAsync> SrvAsync;
    private final BudgetDisbursementGridEdit<BudgetDisbursement_Mdl, GrdBudgetAllocationDetail_PrpAcs> budgetEntryGridEdit = new BudgetDisbursementGridEdit(grdBudgetDisbursementDetail);
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
                return txtHDisbursementType.getTag();
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
                txtDisbursementNo.setEnabled(false);
                txtHBudgetName.setFocus(true);
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                if (DmShared.xNull(userControl.getTextBlog(approvedByBlog)).equalsIgnoreCase("")) {
                    txtHDisbursementType.setEnabled(false);
                    txtDisbursementNo.setEnabled(false);
                    txtHBudgetName.setFocus(true);
                    grdBudgetDisbursementDetail.getGrid().getStore().add(new GrdBudgetDisbursementDetail_Mdl());

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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(BudgetDisbursement.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void prnRecord(boolean printToPrinter) {

        HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
        hashMapFilter.put("docId", new FieldUDT_Mdl("Indent No.", DmShared.xNull(txtDisbursementNo.getText()), DmShared.xNull(topControl.getDocId())));
        new OpenSourceReport(txtHDisbursementType.getTag(), "", widgetName, widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
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
        txtHDisbursementType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHDisbursementType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDisbursementType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDisbursementType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDisbursementType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                if (tRow != null) {
                    txtHDisbursementType.setTag2nd(tRow.getSharedPreparedBy());
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

        budgetEntryGridEdit.addCallBack(new BudgetDisbursementGridEdit.CallBack() {
            @Override
            public void onOk(GrdBudgetDisbursementDetail_Mdl grdVoucherEntry) {
                grdBudgetDisbursementDetail.commitChanges();
                calculateTotal();
            }

            @Override
            public void onDelete() {
                grdBudgetDisbursementDetail.commitChanges();
            }
        });

        grdBudgetDisbursementDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showBudgetHeadGridEdit(event.getRowIndex());
            }

        });

        grdBudgetDisbursementDetail.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showBudgetHeadGridEdit(grdBudgetDisbursementDetail.getGrid().getStore().indexOf(grdBudgetDisbursementDetail.getGrid().getSelectionModel().getSelectedItem()));
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
        }
    }

    public BudgetDisbursement(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(BudgetDisbursement_Srv.class), BudgetDisbursement.class.getName());
        walkThrough = new WalkThrough(SrvAsync);
        this.asWidget().setTitle(widgetName);
        txtAllocationDate.addBlurHandler(this);
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
                    swi.setWidget(BudgetDisbursement.this);
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

    private BudgetDisbursement_Mdl chkValidation() {
        BudgetDisbursement_Mdl mrTemp;
        mrTemp = topControlSaveInitialize();
        BudgetDisbursement_Vld.chkSave(mrTemp);
        return mrTemp;
    }

    private BudgetDisbursement_Mdl topControlSaveInitialize() {
        BudgetDisbursement_Mdl mrTemp = new BudgetDisbursement_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
            mrTemp.getRecid().setRecIdValue(DmShared.xNull(txtDisbursementNo.getText()));
            mrTemp.getRecid().setCounter(DmShared.iNull(vNo).longValue());
            mrTemp.getRecid().setPrefix(DmShared.xNull(recIdPrefix));
        }
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtDisbursementNo.getText()));
        mrTemp.setBudgetNameText(DmShared.xNull(txtHBudgetName.getText()));
        mrTemp.setDtAllocation(DmShared.dNull(txtAllocationDate.getText()));
        mrTemp.setAllocationTypeTag(DmShared.xNull(txtHDisbursementType.getTag()));
        mrTemp.setAllocationTypeText(DmShared.xNull(txtHDisbursementType.getText()));
        mrTemp.setVoucherCategory(voucherCategory);
        mrTemp.setTotalBudget(DmShared.vNull(lblTotalValue.getText()).toString());

        grdBudgetDisbursementDetail.getGrid().getStore().commitChanges();
        mrTemp.getGrdBudgetHeadDetails().addAll(grdBudgetDisbursementDetail.getGrid().getStore().getAll());
        return mrTemp;
    }

    private void topControlSave() {
        BudgetDisbursement_Mdl mrTemp;
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
                new DmAlertMessageBox(errorMessage, txtHDisbursementType).show();
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
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<BudgetDisbursement_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<BudgetDisbursement_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<BudgetDisbursement_Mdl> result) {
        BudgetDisbursement_Mdl data;

        if (result.isStatus()) {
            data = result.getData();

            userControl.setTextBlog(preparedByBlog, data.getPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getModifiedBy());
            userControl.setTextBlog(approvedByBlog, data.getApprovedBy());

            topControl.setDocId(data.getSharedKeyFieldCode());
            txtHDisbursementType.setText(data.getAllocationTypeText());
            txtHDisbursementType.setTag(data.getAllocationTypeTag());
            txtHDisbursementType.setTag2nd(data.getVoucherCategory());
            voucherCategory=data.getVoucherCategory();
            txtDisbursementNo.setText(data.getRecid().getRecIdValue());
            txtAllocationDate.setText(data.getDtAllocation());
            txtHBudgetName.setText(data.getBudgetNameText());
            recIdPrefix = DmShared.xNull(data.getRecid().getPrefix());
            vNo = DmShared.iNull(data.getRecid().getCounter()).toString();
            recIdValue = DmShared.xNull(data.getRecid().getRecIdValue());

            grdBudgetDisbursementDetail.getGrid().getStore().clear();
            grdBudgetDisbursementDetail.getGrid().getStore().addAll(data.getGrdBudgetHeadDetails());
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
        grdBudgetDisbursementDetail.getGrid().getStore().add(new GrdBudgetDisbursementDetail_Mdl());
        txtDisbursementNo.setTag(DmClient.getVoucherPrefix());
        txtAllocationDate.setText(DmClient.getLoginDate());
        txtHDisbursementType.setTag(DmShared.xNull(dvm.getAllocationTypeTag()));
        txtHDisbursementType.setText(DmShared.xNull(dvm.getAllocationTypeText()));
        txtHDisbursementType.setTag2nd(DmShared.xNull(dvm.getVoucherCategory()));
        voucherCategory=dvm.getVoucherCategory();
    }

    private void clear() {
        grdBudgetDisbursementDetail.getGrid().getStore().clear();
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
        for (int i = 0; i < grdBudgetDisbursementDetail.getGrid().getStore().size(); i++) {
            if (DmShared.xNull(grdBudgetDisbursementDetail.getGrid().getStore().get(i).getPlusMinus()).equalsIgnoreCase("0") && 
                    DmShared.vNull(grdBudgetDisbursementDetail.getGrid().getStore().get(i).getBudget()).doubleValue()>0) {
                totBudget -= DmShared.vNull(grdBudgetDisbursementDetail.getGrid().getStore().get(i).getBudget());
            }
            if (DmShared.xNull(grdBudgetDisbursementDetail.getGrid().getStore().get(i).getPlusMinus()).equalsIgnoreCase("1") && 
                    DmShared.vNull(grdBudgetDisbursementDetail.getGrid().getStore().get(i).getBudget()).doubleValue()>0) {
                totBudget += DmShared.vNull(grdBudgetDisbursementDetail.getGrid().getStore().get(i).getBudget());
            }
        }
        lblTotalValue.setText(DmClient.vNull(totBudget, "0.00"));
    }
}
