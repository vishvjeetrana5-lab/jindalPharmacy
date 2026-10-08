package dataman.accounts.advanceRequestEntry.client;

import dataman.accounts.advanceRequestEntry.shared.AdvanceRequestEntry_Mdl;
import dataman.accounts.advanceRequestEntry.shared.AdvanceRequestEntry_Srv;
import dataman.accounts.advanceRequestEntry.shared.AdvanceRequestEntry_SrvAsync;
import dataman.accounts.advanceRequestEntry.shared.AdvanceRequestEntry_Vld;
import dataman.accounts.advanceRequestEntry.shared.DefaultValue_Mdl;
import dataman.accounts.advanceRequestEntry.shared.Find_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmCallBack;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmBase.self.shared.SetWidgetInvoker;
import dataman.dmBase.self.shared.UserMenu;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.util.HashMap;
import java.util.List;

public class AdvanceRequestEntry extends AdvanceRequestEntry_Dsg implements BlurHandler, ClickHandler, DmRootEvents, AttachEvent.Handler, KeyPressHandler {

    DefaultValue_Mdl defaultValueMdl = new DefaultValue_Mdl();
    ServiceInvoker<AdvanceRequestEntry_SrvAsync> SrvAsync;
//    final ModifyReason modifyReason = new ModifyReason();
//    private ModifyReason_Mdl modifyReasonMdl;
    private final RemarkPopup remarkPopup = new RemarkPopup();
    private final PurchaseOrderSearch poSearch = new PurchaseOrderSearch();

    public AdvanceRequestEntry(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();

        SrvAsync = new ServiceInvoker<>(GWT.create(AdvanceRequestEntry_Srv.class), AdvanceRequestEntry.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        findEvents();
        textHelpEvents();
        manageEvent();
        iniDefaultValues(swi);

    }

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setRefreshRequired(false);
        topControl.setOtherRequired(false);

        topControl.setUserPermission(menuPermission, new VoucherPermissionHandler(DmClient.getVoucherPermission(new String[]{new LibraryVoucherCategory().ADVANCE_REQUEST_ENTRY})) {

            @Override
            public String getVoucherType() {
                return defaultValueMdl.getSharedKeyFieldCode();
            }

            @Override
            public String getVoucherDate() {
                return txtVoucherDate.getText();
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

            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                //manageDisplay(true);
                if (DmShared.xNull(cancelBy.getText()).equalsIgnoreCase("Discarded")) {
                    new DmAlertMessageBox("This Record has been Discarded.You can't edit this record.", (DmTextBox) null).show();
                    topControl.btnClick("");
                    return;
                } else {
                    manageDisplay(true);

                }

            }

        });

        topControl.addDeleteHandeller(new DmTopControl.eDelete() {
            @Override
            public void deleteClicked() {
//                new DmConfirmMessageBox("Are you sure? You want to delete this record.", new DialogHideEvent.DialogHideHandler() {
//
//                    @Override
//                    public void onDialogHide(DialogHideEvent event) {
//                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
//                            topControlDelete(topControl.getDocId());
//                        }
//                    }
//                }).show();

                new DmConfirmMessageBox("Are you sure? You want to Discard this record.", new DialogHideEvent.DialogHideHandler() {
                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            remarkPopup.show();
                        }
                    }
                }).show();

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
            public void moveRec(String strKey) {
                topControlMoveRec(strKey);
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
                if (topControl.getMode().equalsIgnoreCase("Edit")) {
                    //showModifyReason();
                    topControlSave();
                } else {
                    topControlSave();
                }
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(AdvanceRequestEntry.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void iniDefaultValues(final SetWidgetInvoker swi) {
        SrvAsync.invokeRPC(true).getDefaultValues(new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    defaultValueMdl = result.getData();

                    swi.setWidget(AdvanceRequestEntry.this);
                    manageDisplay(false);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void textHelpEvents() {
        txtHModuleType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHModuleType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHModuleType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHModuleType.setData(result.getData(), result.getPaging());

                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHModuleType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (t1Row != null) {
//                    setPurchaseOrderDisplay();
//                    clear();

                }
                txtPurchaseOrder.setEnabled(true);
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        if (!DmShared.xNull(txtHModuleType.getTag()).trim().equalsIgnoreCase("")) {
                            txtPurchaseOrder.setFocus(true);
                        } else {
                            txtPurchaseOrder.setEnabled(false);
                            btnPOSearch.setFocus(true);
                        }
                    }
                });
            }
        });
        txtHModuleType.addKeyDownHandler(new DmTextHelp.TxtHelpKeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event, Object objSource) {
                if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            if (!DmShared.xNull(txtHModuleType.getTag()).trim().equalsIgnoreCase("")) {
                                txtPurchaseOrder.setFocus(true);
                            } else {
                                txtHModuleType.setFocus(true);

                            }
                        }
                    });
                }
            }
        });
        poSearch.addCallBack(new PurchaseOrderSearch.CallBack() {
            @Override
            public void onOk(String poNo, String poDocid) {
                getPurchaseOrderDetail(poNo, poDocid, null);
            }

            @Override
            public void onClose() {
                txtPurchaseOrder.setFocus(true);
            }
        });

        remarkPopup.addCallBack(new RemarkPopup.CallBack() {
            @Override
            public void onOk(String remark) {
                topControlDelete(DmShared.iNull(txtReqNo.getTag()).toString(), remark);
            }

            @Override
            public void onCancel() {
            }
        });
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void searchRecord(String key) {

    }

    @Override
    public void onClick(ClickEvent event) {

        btnClick((Button) event.getSource());
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtVoucherDate) {
            txtVoucherDate.setText(DmClient.returnDate(txtVoucherDate.getText()));
        }
        if (event.getSource() == txtDueDate) {
            txtDueDate.setText(DmClient.returnDate(txtDueDate.getText()));
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getShiftKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_P) {
            btnClick(btnPOSearch);
            rootEvent.stopPropagation();
        }

    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
        poSearch.hide();
    }

    @Override
    public void onKeyPress(KeyPressEvent event) {
        if (event.getSource().equals(txtPurchaseOrder)) {
            if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
                if (!DmShared.xNull(txtPurchaseOrder.getText()).trim().equalsIgnoreCase("")) {
                    if (!DmShared.xNull(txtHModuleType.getTag()).equalsIgnoreCase("")) {
                        getPurchaseOrderDetail(DmShared.xNull(txtPurchaseOrder.getText()), "", null);
                    } else {
                        new DmAlertMessageBox("Please Select Module", txtHModuleType).show();
                    }
                } else {
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            if ((DmShared.xNull(txtHModuleType.getTag()).equalsIgnoreCase("PO"))) {
                                btnPOSearch.setFocus(true);
                            }
                        }
                    });
                }
            }
        }
    }

    private void topControlDelete(String strKey, String remark) {
        SrvAsync.invokeRPC().deleteRecord(strKey, remark, new AsyncCallback<DmResult<String>>() {

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
                    new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                }
            }
        });
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

    private void topControlSave() {
        AdvanceRequestEntry_Mdl mrTemp;
        try {
            mrTemp = topControlSaveInitialize();
            AdvanceRequestEntry_Vld.chkSave(mrTemp, DmClient.getLoginDate());
            SrvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtAdvanceReqAmt).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        topControl.setDocId(result.getData());
                        topControlMoveRec(topControl.getDocId());

                    } else {
                        saveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            saveError(e.getErrorNo(), e.getMessage());
        }
    }

    private AdvanceRequestEntry_Mdl topControlSaveInitialize() {
        AdvanceRequestEntry_Mdl mrTemp = new AdvanceRequestEntry_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }

        mrTemp.setPoDocId(DmShared.xNull(txtPurchaseOrder.getTag()));
        mrTemp.setPoRecId(DmShared.xNull(txtPurchaseOrder.getText()));

        mrTemp.setModuleTag(DmShared.xNull(txtHModuleType.getTag()));
        mrTemp.setModuleText(DmShared.xNull(txtHModuleType.getText()));

        mrTemp.setVoucherDate(DmShared.dNull(txtVoucherDate.getText()));

        mrTemp.setVoucherTypeTag(DmShared.xNull(txtVoucherType.getTag()));
        mrTemp.setVoucherTypeText(DmShared.xNull(txtVoucherType.getText()));

        mrTemp.setDueDate(DmShared.xNull(txtDueDate.getText()));
        mrTemp.setRemark(DmShared.xNull(txtRemark.getText()));
        mrTemp.setAdvanceReqAmt(DmShared.xNull(txtAdvanceReqAmt.getText()));
        mrTemp.setTotalPoAmt(DmShared.xNull(txtTotalPoValue.getText()));
        mrTemp.setTotalMrnAmt(DmShared.xNull(txtTotalMrn.getText()));
        mrTemp.setTotaPlBillAmt(DmShared.xNull(txtTotalBill.getText()));
        mrTemp.setTotalPoAmt(DmShared.xNull(txtTotalPoValue.getText()));
        mrTemp.setTotalAdvancePaidAmt(DmShared.xNull(txtTotalAdvance.getText()));
        mrTemp.setTotalBillPaymentAmt(DmShared.xNull(txtTotalBillPay.getText()));

//        if (modifyReasonMdl != null) {
//            mrTemp.setReasonInfo_Mdl(modifyReasonMdl);
//        }
        return mrTemp;
    }

    private void saveError(int errorNo, String errorMessage) {
        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtVoucherDate).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtHModuleType).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtPurchaseOrder).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtAdvanceReqAmt).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtDueDate).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtDueDate).show();
        }
    }

    private void topControlMoveRec(String strKey) {
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<AdvanceRequestEntry_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<AdvanceRequestEntry_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<AdvanceRequestEntry_Mdl> result) {
        AdvanceRequestEntry_Mdl data;

        if (result.isStatus()) {
            clear();
            data = result.getData();
            txtVoucherDate.setText(DmShared.xNull(data.getVoucherDate()));

            topControl.setDocId(DmShared.xNull(data.getSharedKeyFieldCode()));
            txtHModuleType.setTag(DmShared.xNull(data.getModuleTag()));
            txtHModuleType.setText(DmShared.xNull(data.getModuleText()));

                if (DmShared.xNull(data.getIsStatus()).equalsIgnoreCase("1")) {
                manageDisplay(false);
            } else {
                manageDisplay(true);
            }
            if (data.getIsStatus() != null) {
                cancelBy.setText(DmShared.xNull(data.getIsStatus()).equalsIgnoreCase("1")? "Discarded" : "");
            }
            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());
            fillRecords(data);

        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
        flwPoSearch.clear();
        txtPurchaseOrder.setEnabled(false);
    }

    private void getPurchaseOrderDetail(String poNo, String poDocId, final DmCallBack callback) {
        try {
            SrvAsync.invokeRPC().getPurchaseOrderDetail(poNo, poDocId, DmShared.xNull(txtHModuleType.getTag()),
                    new AsyncCallback<DmResult<AdvanceRequestEntry_Mdl>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtPurchaseOrder).show();
                }

                @Override
                public void onSuccess(DmResult<AdvanceRequestEntry_Mdl> result) {
                    if (result.isStatus() && result.getData() != null) {
                        clear();
                        fillRecords(result.getData());
                        txtPurchaseOrder.setEnabled(false);
                        txtHModuleType.setEnabled(false);
                        txtVoucherDate.setEnabled(false);

                        if (callback != null) {
                            DmResult<Object> dmrTemp = new DmResult<>();
                            dmrTemp.setStatus(true);
                            callback.result(dmrTemp);
                        }

                        txtAdvanceReqAmt.setFocus(true);

                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), txtPurchaseOrder).show();
                    }
                }

            });
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtPurchaseOrder).show();
        }
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

    private void fillRecords(AdvanceRequestEntry_Mdl data) {
        if (data != null) {
            if (!DmShared.xNull(data.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                txtReqNo.setTag(DmShared.xNull(data.getSharedKeyFieldCode()));
                txtReqNo.setText(DmShared.xNull(data.getSharedKeyFieldName()));
            }

            txtPurchaseOrder.setText(DmShared.xNull(data.getPoRecId()));
            txtPurchaseOrder.setTag(DmShared.xNull(data.getPoDocId()));

            txtPurchaseOrderDate.setText(DmShared.dNull(data.getPoDate()));

            txtSupplierName.setTag(DmShared.xNull(data.getSupplierTag()));
            txtSupplierName.setText(DmShared.xNull(data.getSupplierText()));

            txtAmt.setText(DmShared.xNull(data.getPoAmt()));

            txtAdvanceReqAmt.setText(DmShared.xNull(data.getAdvanceReqAmt()));
            txtDueDate.setText(DmShared.dNull(data.getDueDate()));
            txtRemark.setText(DmShared.xNull(data.getRemark()));

            grdPODetail.getGrid().getStore().clear();
            grdPODetail.getGrid().getStore().addAll(data.getLstGrdPoDetail());

            grdMRNDetail.getGrid().getStore().clear();
            grdMRNDetail.getGrid().getStore().addAll(data.getLstGrdMrnDetail());

            grdBillDetail.getGrid().getStore().clear();
            grdBillDetail.getGrid().getStore().addAll(data.getLstGrdBillDetail());

            grdAdvPaymnetDetail.getGrid().getStore().clear();
            grdAdvPaymnetDetail.getGrid().getStore().addAll(data.getLstGrdAdvPaymentDetails());

            grdFinalPaymnetDetail.getGrid().getStore().clear();
            grdFinalPaymnetDetail.getGrid().getStore().addAll(data.getLstGrdFinalPaymentDetails());
            calculate();
        }

    }

    private void calculate() {
        double totalPoAmt = 0, totalGrnAmt = 0, totalBillAmt = 0, totalAdvBillPay = 0, totalAdvTdsPay = 0, totalAdvNetPay = 0, totalFinalPay = 0;
        for (int i = 0; i < grdPODetail.getGrid().getStore().size(); i++) {
            if (DmShared.iNull(grdPODetail.getGrid().getStore().get(i).getSharedKeyFieldCode()).longValue() != 0l) {
                totalPoAmt += DmShared.vNull(grdPODetail.getGrid().getStore().get(i).getAmt()).doubleValue();
            }
        }
        txtTotalPoValue.setText(DmClient.vNull(totalPoAmt, "0.00"));
        for (int i = 0; i < grdMRNDetail.getGrid().getStore().size(); i++) {
            if (DmShared.iNull(grdMRNDetail.getGrid().getStore().get(i).getSharedKeyFieldCode()).longValue() != 0l) {
                totalGrnAmt += DmShared.vNull(grdMRNDetail.getGrid().getStore().get(i).getValue()).doubleValue();
            }

        }
        txtTotalMrn.setText(DmClient.vNull(totalGrnAmt, "0.00"));
        for (int i = 0; i < grdBillDetail.getGrid().getStore().size(); i++) {
            if (DmShared.iNull(grdBillDetail.getGrid().getStore().get(i).getSharedKeyFieldCode()).longValue() != 0l) {
                totalBillAmt += DmShared.vNull(grdBillDetail.getGrid().getStore().get(i).getValue()).doubleValue();
            }

        }
        txtTotalBill.setText(DmClient.vNull(totalBillAmt, "0.00"));
        for (int i = 0; i < grdAdvPaymnetDetail.getGrid().getStore().size(); i++) {
            if (DmShared.iNull(grdAdvPaymnetDetail.getGrid().getStore().get(i).getSharedKeyFieldCode()).longValue() != 0l) {
                totalAdvBillPay += DmShared.vNull(grdAdvPaymnetDetail.getGrid().getStore().get(i).getAmt()).doubleValue();
                totalAdvNetPay += DmShared.vNull(grdAdvPaymnetDetail.getGrid().getStore().get(i).getNetPay()).doubleValue();
                totalAdvTdsPay += DmShared.vNull(grdAdvPaymnetDetail.getGrid().getStore().get(i).getTdsDeduction()).doubleValue();
            }
        }
        txtTotalAdvance.setText(DmClient.vNull(totalAdvBillPay, "0.00"));
        txtTotalAdvanceNetPay.setText(DmClient.vNull(totalAdvNetPay, "0.00"));
        txtTotalAdvanceTds.setText(DmClient.vNull(totalAdvTdsPay, "0.00"));

        for (int i = 0; i < grdFinalPaymnetDetail.getGrid().getStore().size(); i++) {
            if (DmShared.iNull(grdFinalPaymnetDetail.getGrid().getStore().get(i).getSharedKeyFieldCode()).longValue() != 0l) {
                totalFinalPay += DmShared.vNull(grdFinalPaymnetDetail.getGrid().getStore().get(i).getAmt()).doubleValue();

            }
        }
        txtTotalBillPay.setText(DmClient.vNull(totalFinalPay, "0.00"));
    }

    private void btnClick(Button button) {
        if (!button.isAttached()) {
            return;
        }
        if (!button.isVisible()) {
            return;
        }
        if (!button.isEnabled()) {
            return;
        }
        if (button == btnPOSearch) {
            if (!DmShared.xNull(txtHModuleType.getTag()).equalsIgnoreCase("")) {
                poSearch.show(txtHModuleType.getTag());
            } else {
                new DmAlertMessageBox("Please Select Module", txtHModuleType).show();
            }
        }
    }

    private void manageEvent() {
        txtVoucherDate.addBlurHandler(this);
        txtDueDate.addBlurHandler(this);
        btnPOSearch.addClickHandler(this);
        flwForm.addAttachHandler(this);
        flwPoSearch.addAttachHandler(this);
        txtPurchaseOrder.addKeyPressHandler(this);

    }

    private void onAdd() {
        clear();
        manageDisplay(true);
        txtVoucherType.setTag(DmShared.xNull(defaultValueMdl.getSharedKeyFieldCode()));
        txtVoucherType.setText(DmShared.xNull(defaultValueMdl.getSharedKeyFieldName()));
        txtHModuleType.setTag(DmShared.xNull("PO"));
        txtHModuleType.setText(DmShared.xNull("Purchase"));
        getServerDateTime();
    }

    private void getServerDateTime() {
        SrvAsync.invokeRPC().getServerDateTime(new AsyncCallback<String[]>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtPurchaseOrder).show();
            }

            @Override
            public void onSuccess(String[] result) {
                txtVoucherDate.setText(DmShared.dNull(result[0]));

                txtHModuleType.setFocus(true);

            }
        });
    }

    private void manageDisplay(boolean flag) {
        txtReqNo.setEnabled(false);
        txtVoucherDate.setEnabled(false);
        txtPurchaseOrderDate.setEnabled(false);
        txtAmt.setEnabled(false);
        txtSupplierName.setEnabled(false);
        txtTotalPoValue.setEnabled(false);
        txtTotalMrn.setEnabled(false);
        txtTotalBill.setEnabled(false);
        txtTotalAdvance.setEnabled(false);
        txtTotalAdvanceNetPay.setEnabled(false);
        txtTotalAdvanceTds.setEnabled(false);
        txtTotalBillPay.setEnabled(false);

        if (DmShared.xNull(topControl.getMode()).equalsIgnoreCase("Edit")) {
            txtHModuleType.setEnabled(false);
        }

        if (DmShared.xNull(topControl.getMode()).equalsIgnoreCase("New")) {
            flwPoSearch.add(btnPOSearch);
            txtPurchaseOrder.setEnabled(true);
            txtHModuleType.setEnabled(true);
        } else {
            flwPoSearch.clear();
            txtPurchaseOrder.setEnabled(false);
            txtHModuleType.setEnabled(false);
            txtVoucherDate.setEnabled(false);

        }

    }

    private void clear() {
        txtPurchaseOrder.setText("");
        txtPurchaseOrder.setTag("");

        txtTotalPoValue.setText("");
        txtTotalMrn.setText("");
        txtTotalBill.setText("");
        txtTotalBillPay.setText("");
        txtTotalAdvance.setText("");
        txtAdvanceReqAmt.setText("");
        txtDueDate.setText("");
        txtRemark.setText("");
        grdPODetail.getGrid().getStore().clear();
        grdAdvPaymnetDetail.getGrid().getStore().clear();
        grdBillDetail.getGrid().getStore().clear();
        grdFinalPaymnetDetail.getGrid().getStore().clear();
        grdMRNDetail.getGrid().getStore().clear();
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        cancelBy.setText("");
    }

    private void prnRecord(boolean printToPrinter) {
        HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
        hashMapFilter.put("docId", new FieldUDT_Mdl("Req. No.", DmShared.xNull(txtReqNo.getText()), DmShared.xNull(topControl.getDocId())));
        new OpenSourceReport(DmShared.xNull(defaultValueMdl.getSharedKeyFieldCode()), "", widgetName, widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
            @Override
            public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                DmClient.localSettingPrintMap(reportProperty, moduleName, widgetName, "Print");
                return reportProperty;
            }
        }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
    }

//    private void showModifyReason() {
//        modifyReason.show(topControl.getMode(), topControl.getDocId());
//        modifyReason.addCallBack(new ModifyReason.CallBack() {
//
//            @Override
//            public void onOk(ModifyReason_Mdl modifyReason) {
//                try {
//                    modifyReasonMdl = modifyReason;
//                    topControlSave();
//                } catch (Exception e) {
//                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
//                }
//            }
//        });
//    }
}
