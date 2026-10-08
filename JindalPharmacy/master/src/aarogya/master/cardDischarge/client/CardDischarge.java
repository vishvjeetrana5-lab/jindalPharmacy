package aarogya.master.cardDischarge.client;

import aarogya.master.cardDischarge.shared.*;
import aarogya.projectMenu.self.shared.pmMaster;
import aarogya.selfToolkit.cardPaymentPopup.client.CardPaymentPopupGridEdit;
import aarogya.selfToolkit.cardPaymentPopup.shared.GrdCardPayment_Mdl;
import aarogya.selfToolkit.stStatic.shared.RegisteredWallet_Mdl;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmHelpGrid.client.DmHelpGrid;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.util.HashMap;
import java.util.List;

public final class CardDischarge extends CardDischarge_Dsg implements BlurHandler, KeyUpHandler, KeyDownHandler, ClickHandler {

    private final ServiceInvoker<CardDischarge_SrvAsync> srvAsync;
    private final CardPaymentPopupGridEdit<GrdCardPayment_Mdl, GrdPayment_PrpAcs> cardDischargeGridEdit = new CardPaymentPopupGridEdit(grdPayment);
    protected DefaultValue_Mdl defaultValue;
    private final String vrCategory = new LibraryVoucherCategory().CARD_DISCHARGE, moduleName = new pmMaster().moduleName;

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setRefreshRequired(false);
        topControl.setOtherRequired(false);

        topControl.setUserPermission(menuPermission, new VoucherPermissionHandler(DmClient.getVoucherPermission(new String[]{vrCategory})) {
            @Override
            public String getVoucherType() {
                return defaultValue.getVoucherTag();
            }

            @Override
            public String getVoucherDate() {
                return txtValidDate.getTag();
            }

            @Override
            public String getVoucherValue() {
                return txtAmount.getText();
            }
        });

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                manageDisplay();
                clear();
                txtCardNo.setFocus(true);

            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                txtCardNo.setFocus(true);
                manageDisplay();
            }
        });

        topControl.addDeleteHandeller(new DmTopControl.eDelete() {
            @Override
            public void deleteClicked() {
                new DmConfirmMessageBox("Are you sure? You want to delete this record.", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            topControlDelete(topControl.getDocId(), txtVoucherDate.getText());
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
                topControlSave();
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(CardDischarge.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void prnRecord(boolean printToPrinter) {
        HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
        hashMapFilter.put("docId", new FieldUDT_Mdl("Card Discharge", DmShared.xNull(txtCardNo.getText()), DmShared.xNull(topControl.getDocId())));
        new OpenSourceReport(defaultValue.getVoucherTag(), "", widgetName, widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
            @Override
            public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                DmClient.localSettingPrintMap(reportProperty, moduleName, widgetName, "Print");
                return reportProperty;
            }
        }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
    }

    private void textHelpEvents() {
        txtHCardHelp.addHelpGridCallBack(new DmHelpGrid.HelpGridCallBack<RegisteredWallet_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHCardNo(txtVoucherDate.getText(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<RegisteredWallet_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), btnCardHelp).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<RegisteredWallet_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHCardHelp.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), btnCardHelp).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, RegisteredWallet_Mdl tRow) {
                if (blnStatus && tRow != null) {
                    getCardDetail(tRow.getCardNo(), "", "", false, null);
                }
            }
        });
    }

    private void clearCardDetail() {
        txtCardNo.setText("");
        txtCardNo.setTag("");
        txtCardNo.setTag2nd("");
        txtCardRegNo.setTag2nd("");
        txtCardRegNo.setTag("");
        txtCardRegNo.setText("");
        txtContactPerson.setText("");
        txtCardType.setTag("");
        txtCardType.setText("");
        txtLinkedAC.setTag("");
        txtLinkedAC.setText("");
        txtAllotmentDate.setText("");
        txtValidDate.setText("");
        lblMessage.setText("");
    }

    private void setCardDetail(RegisteredWallet_Mdl data) {
        clearCardDetail();
        String balance = "";
        if (data != null) {
            txtCardNo.setTag(DmShared.xNull(data.getSharedKeyFieldCode()));
            txtCardNo.setText(DmShared.xNull(data.getCardNo()));
            txtCardRegNo.setText(DmShared.xNull(data.getSharedKeyFieldName()));
            txtContactPerson.setText(DmShared.xNull(data.getContactPerson()));
            txtCardType.setTag(DmShared.xNull(data.getCardTypeCode()));
            txtCardType.setText(DmShared.xNull(data.getCardTypeName()));
            txtLinkedAC.setTag(DmShared.xNull(data.getLinkAccountCode()));
            txtLinkedAC.setText(DmShared.xNull(data.getLinkAccountName()));
            txtAllotmentDate.setText(DmShared.xNull(data.getAllotmentDate()));
            txtValidDate.setText(DmShared.xNull(data.getValidDate()));
            lblMessage.getElement().getStyle().setColor("green");
            balance = DmShared.vNull(data.getBalance()).doubleValue() > 0 ? (DmClient.vNull(Math.abs(data.getBalance()), "0.00") + " Dr") : (DmShared.vNull(data.getBalance()).doubleValue() < 0 ? (DmClient.vNull(Math.abs(data.getBalance()), "0.00") + " Cr") : "");
            lblMessage.setText(DmShared.vNull(data.getBalance()).doubleValue() != 0 ? ("Balance: " + balance) : "");
        }
    }

    private void findEvents() {
        dmFind.setTitle(this.asWidget().getTitle());

        dmFind.addHelpGridCallBack(new DmFind.FindCallBack<Find_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getFind(topControl.getPermissions(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Find_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<Find_Mdl>> result) {
                        if (result.isStatus()) {
                            dmFind.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
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
        cardDischargeGridEdit.addCallBack(new CardPaymentPopupGridEdit.CallBack() {
            @Override
            public void onOk(GrdCardPayment_Mdl card_Mdl) {
                grdPayment.commitChanges();
                calculateAmt();
            }

            @Override
            public void onDelete() {
                grdPayment.commitChanges();
                calculateAmt();
            }
        });

        grdPayment.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showCardGridEdit(event.getRowIndex());
            }
        });

        grdPayment.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showCardGridEdit(grdPayment.getGrid().getStore().indexOf(grdPayment.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

    }

    private void showCardGridEdit(int currentRowIndex) {
        boolean flag = !topControl.getMode().equalsIgnoreCase("Browse");
        cardDischargeGridEdit.show(currentRowIndex, flag, flag, flag, getcardTypeArray(DmShared.xNull(defaultValue.getPaymentMode())), 
                txtBalance.getText(), defaultValue.getVoucherTag(), null,"");
    }

    private String[] getcardTypeArray(String cardTypes) {
        String newCardType = cardTypes.replaceAll("\\|", ",");
        return newCardType.split(",");
    }

    public CardDischarge(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        srvAsync = new ServiceInvoker<>(GWT.create(CardDischarge_Srv.class), CardDischarge.class.getName());
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("D", "*"));
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        handlerEvents();
        findEvents();
        textHelpEvents();
        gridEvents();
        iniDefaultValues(swi);

    }

    private void handlerEvents() {
        txtVoucherDate.addBlurHandler(this);
        txtAmount.addBlurHandler(this);
        txtCollected.addKeyUpHandler(this);
        btnCardHelp.addClickHandler(this);
        txtCardNo.addKeyDownHandler(this);
        txtCardNo.addBlurHandler(this);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private void topControlDelete(String strKey, String V_Date) {
        srvAsync.invokeRPC().deleteRecord(topControl.getPermissions(), strKey, V_Date, new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    topControlCard();
                } else {
                    new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                }
            }
        });
    }

    private void topControlCard() {
        srvAsync.invokeRPC().getNavigator(topControl.getPermissions(), new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
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
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<CardDischarge_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<CardDischarge_Mdl> result) {
                if (result.isStatus()) {
                    clear();
                    topControlMoveRecSuccess(result);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<CardDischarge_Mdl> result) {
        CardDischarge_Mdl data;
        if (result.isStatus()) {
            data = result.getData();

            topControl.setDocId(data.getSharedKeyFieldCode());
            setCardDetail(data.getCardRegMdl());
            txtVoucherNo.setText(data.getSharedKeyFieldName());
            txtVoucherDate.setText(data.getVoucherDate());
            txtRemark.setText(data.getRemark());
            txtAmount.setText(data.getAmount());

            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());
            grdPayment.getGrid().getStore().clear();
            grdPayment.getGrid().getStore().addAll(data.getLstPayment());
            txtReceipt.setText(data.getNetReceipt());
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }

        topControl.setDisplay(true);
        manageDisplay();
    }

    private void topControlSave() {
        CardDischarge_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            CardDischarge_Vld.chkSave(mrTemp);
            srvAsync.invokeRPC().saveRecord(mrTemp, DmClient.getVoucherPrefix(), topControl.getPermissions(), new AsyncCallback<DmResult<String>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtAllotmentDate).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        topControl.setDocId(result.getData());
                        new DmConfirmMessageBox("Are you sure? You want to print this record.", new DialogHideEvent.DialogHideHandler() {
                            @Override
                            public void onDialogHide(DialogHideEvent event) {
                                if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                                    prnRecord(true);
                                }
                            }
                        }).show();
                        topControlMoveRec(topControl.getDocId());
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

        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtCardNo).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtCardNo).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtVoucherDate).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtAmount).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtAmount).show();
                break;
            case 600:
                new DmAlertMessageBox(errorMessage, txtAmount).show();
                break;
            case 700:
                new DmAlertMessageBox(errorMessage, txtAmount).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtCardNo).show();
                break;
        }

    }

    private CardDischarge_Mdl topControlSaveInitialize() {
        CardDischarge_Mdl mrTemp = new CardDischarge_Mdl();
        RegisteredWallet_Mdl cardMdl = new RegisteredWallet_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }
        mrTemp.setVoucherTypeCode(DmShared.xNull(defaultValue.getVoucherTag()));
        mrTemp.setVoucherDate(DmShared.dNull(txtVoucherDate.getText()));
        cardMdl.setSharedKeyFieldCode(DmShared.xNull(txtCardNo.getTag()));
        cardMdl.setSharedKeyFieldName(DmShared.xNull(txtCardRegNo.getText()));
        cardMdl.setCardNo(DmShared.xNull(txtCardNo.getText()));
        cardMdl.setContactPerson(DmShared.xNull(txtContactPerson.getText()));
        cardMdl.setLinkAccountCode(DmShared.xNull(txtLinkedAC.getTag()));
        cardMdl.setCardTypeCode(DmShared.xNull(txtCardType.getTag()));
        mrTemp.setCardRegMdl(cardMdl);
        mrTemp.setAmount(DmShared.xNull(txtAmount.getText()));
        mrTemp.setRemark(DmShared.xNull(txtRemark.getText()));
        mrTemp.getLstPayment().addAll(grdPayment.getGrid().getStore().getAll());

        return mrTemp;
    }

    private void manageDisplay() {
        btnCardHelp.setEnabled(false);
        txtCardNo.setEnabled(false);
        txtCardRegNo.setEnabled(false);
        txtCardType.setEnabled(false);
        txtContactPerson.setEnabled(false);
        txtLinkedAC.setEnabled(false);
        txtAllotmentDate.setEnabled(false);
        txtValidDate.setEnabled(false);
        txtVoucherNo.setEnabled(false);
        txtReceipt.setEnabled(false);
        txtBalance.setEnabled(false);
        txtReturn.setEnabled(false);

        if (topControl.getMode().toUpperCase().equalsIgnoreCase("NEW")) {
            btnCardHelp.setEnabled(true);
            txtCardNo.setEnabled(true);
            txtVoucherDate.setText(defaultValue.getCurrentDate());
            grdPayment.getGrid().getStore().clear();
            grdPayment.getGrid().getStore().add(defaultValue.getCardMdl());
            grdPayment.getGrid().getStore().add(new GrdCardPayment_Mdl());
        }

    }

    private void calculateAmt() {
        double grdAmount = 0;
        for (GrdCardPayment_Mdl grdMdl : grdPayment.getGrid().getStore().getAll()) {
            if (DmShared.vNull(grdMdl.getAmount()).doubleValue() > 0) {
                grdAmount += DmShared.vNull(grdMdl.getAmount()).doubleValue();
            }
        }
        txtReceipt.setText(DmClient.vNull(grdAmount, "0.00"));
        txtBalance.setText(DmClient.vNull((DmShared.vNull(txtAmount.getText()).doubleValue() - DmShared.vNull(txtReceipt.getText()).doubleValue()), "0.00"));
    }

    @Override
    public void searchRecord(String key) {

    }

    @Override
    public void action(String nature, Object object, DmCallBack callBack) {
        nature = nature.toUpperCase();
        switch (nature) {
            case "DISCHARGE":
                topControl.btnClick("New");
                getCardDetail("", "", ((String) object), true, callBack);
                break;
            case "FOCUS_ON_ADD":
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        txtAmount.setFocus(true);
                    }
                });
                break;
        }
    }

    private void iniDefaultValues(final SetWidgetInvoker swi) {
        srvAsync.invokeRPC(true).getDefaultValues(new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    defaultValue = result.getData();
                    manageDisplay();
                    swi.setWidget(CardDischarge.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtVoucherDate) {
            txtVoucherDate.setText(DmClient.returnDate(txtVoucherDate.getText()));
        } else if (event.getSource().equals(txtAmount)) {
            if (!DmShared.xNull(txtAmount.getText()).equalsIgnoreCase("")) {
                calculateAmt();
            }
        }
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (event.getSource().equals(txtCollected)) {
            txtReturn.setText(DmClient.vNull((DmShared.vNull(txtAmount.getText()).doubleValue() - DmShared.vNull(txtCollected.getText()).doubleValue()), "0.00"));
        }
    }

    private void clear() {
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        txtCardNo.setTag("");
        txtCardNo.setTag2nd("");
        txtCardNo.setText("");
        lblMessage.setText("");
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getSource().equals(txtCardNo)) {
            if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
                if (!DmShared.xNull(txtCardNo.getText()).equalsIgnoreCase("")) {
                    getCardDetail(txtCardNo.getText(), "", "", true, null);
                } else {
                    txtAmount.setFocus(true);
                }
            }
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnCardHelp)) {
            txtHCardHelp.show(btnCardHelp, "");
            txtHCardHelp.reQuery("");
        }
    }

    private void getCardDetail(String cardNo, String physicalId, String docId, final boolean isTextBoxCall, final DmCallBack callBack) {
        srvAsync.invokeRPC().getCardDetail(cardNo, physicalId, docId, new AsyncCallback<DmResult<RegisteredWallet_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                clearCardDetail();
                lblMessage.getElement().getStyle().setColor("red");
                lblMessage.setText(caught.getMessage());
                if (isTextBoxCall) {
                    txtCardNo.setFocus(true);
                } else {
                    btnCardHelp.setFocus(true);
                }
            }

            @Override
            public void onSuccess(DmResult<RegisteredWallet_Mdl> result) {
                if (result.isStatus()) {
                    setCardDetail(result.getData());
                    txtAmount.setFocus(true);
                    if (callBack != null) {
                        DmResult<Object> rs = new DmResult<>();
                        rs.setStatus(true);
                        callBack.result(rs);
                    }
                } else {
                    clearCardDetail();
                    lblMessage.getElement().getStyle().setColor("red");
                    lblMessage.setText(result.getErrorMessage());
                    if (isTextBoxCall) {
                        txtCardNo.setFocus(true);
                    } else {
                        btnCardHelp.setFocus(true);
                    }
                }
            }
        });
    }
}
