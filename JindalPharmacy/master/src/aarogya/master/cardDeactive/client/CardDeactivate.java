package aarogya.master.cardDeactive.client;

import aarogya.master.cardDeactive.shared.*;
import aarogya.selfToolkit.stStatic.shared.RegisteredWallet_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import dataman.dmBase.self.shared.*;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmHelpGrid.client.DmHelpGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.util.List;

public final class CardDeactivate extends CardDeactivate_Dsg implements BlurHandler, KeyDownHandler, ClickHandler {

    private DefaultValue_Mdl defaultValue;
    private final ServiceInvoker<CardDeactivate_SrvAsync> srvAsync;

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());

        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);

        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                onAdd();
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                txtHVoucherType.setFocus(true);
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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(CardDeactivate.this);
            }
        });

        topControl.setDisplay(true);
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

        txtHVoucherType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHVrType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHVoucherType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHVoucherType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHVoucherType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {

            }
        });

    }

    private void findEvents() {
        dmFind.setTitle(this.asWidget().getTitle());

        dmFind.addHelpGridCallBack(new DmFind.FindCallBack<Find_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getFind(topControl.getPermissions(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Find_Mdl>>>() {

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

    public CardDeactivate(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("E", "*"));
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("D", "*"));
        srvAsync = new ServiceInvoker<>(GWT.create(CardDeactivate_Srv.class), CardDeactivate.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        findEvents();
        textHelpEvents();
        handlerEvents();
        iniDefaultValues(swi);

    }

    private void handlerEvents() {
        txtVoucherDate.addBlurHandler(this);
        btnCardHelp.addClickHandler(this);
        txtCardNo.addKeyDownHandler(this);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
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
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<CardDeactivate_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<CardDeactivate_Mdl> result) {
                clear();
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<CardDeactivate_Mdl> result) {
        CardDeactivate_Mdl data;

        if (result.isStatus()) {
            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());
            txtVoucherNo.setText(data.getSharedKeyFieldName());
            setCardDetail(data.getCardRegMdl());

            txtHVoucherType.setText(data.getVrTypeName());
            txtHVoucherType.setTag(data.getVrTypeCode());
            txtVoucherDate.setText(data.getVrDate());
            txtRemark.setText(data.getRemark());
            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());

        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }

        topControl.setDisplay(true);
        manageDisplay();
    }

    private void topControlSave() {
        CardDeactivate_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();

            CardDeactivate_Vld.chkSave(mrTemp);
            srvAsync.invokeRPC().saveRecord(mrTemp, DmClient.getVoucherPrefix(), new AsyncCallback<DmResult<String>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHVoucherType).show();
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

        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtHVoucherType).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtVoucherDate).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtCardNo).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtRemark).show();
                break;

            default:
                new DmAlertMessageBox(errorMessage, txtValiddDate).show();
                break;

        }

    }

    private CardDeactivate_Mdl topControlSaveInitialize() {
        CardDeactivate_Mdl mrTemp = new CardDeactivate_Mdl();
        RegisteredWallet_Mdl cardMdl = new RegisteredWallet_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());

        }

        mrTemp.setVrTypeCode(DmShared.xNull(txtHVoucherType.getTag()));

        mrTemp.setVrDate(DmShared.xNull(txtVoucherDate.getText()));
        mrTemp.setVrNo(DmShared.xNull(txtVoucherNo.getText()));
        mrTemp.setRemark(DmShared.xNull(txtRemark.getText()));
        cardMdl.setSharedKeyFieldCode(DmShared.xNull(txtCardNo.getTag()));
        cardMdl.setSharedKeyFieldName(DmShared.xNull(txtCardRegNo.getText()));
        cardMdl.setCardNo(DmShared.xNull(txtCardNo.getText()));
        cardMdl.setContactPerson(DmShared.xNull(txtContactPerson.getText()));
        cardMdl.setLinkAccountCode(DmShared.xNull(txtLinkedAC.getTag()));
        cardMdl.setAllotmentDate(DmShared.xNull(txtAllotmentDate.getText()));
        cardMdl.setCardTypeCode(DmShared.xNull(txtCardType.getTag()));
        mrTemp.setCardRegMdl(cardMdl);

        return mrTemp;
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
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
                    swi.setWidget(CardDeactivate.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void onAdd() {
        clear();
        manageDisplay();

        txtCardNo.setFocus(true);

        txtHVoucherType.setTag(defaultValue.getVoucherTag());
        txtHVoucherType.setText(defaultValue.getVoucherText());
        txtVoucherDate.setText(defaultValue.getCurrentDate());
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtVoucherDate) {
            txtVoucherDate.setText(DmClient.returnDate(txtVoucherDate.getText()));
            txtCardNo.setFocus(true);
        }
    }

    private void manageDisplay() {
        btnCardHelp.setEnabled(false);
        txtCardNo.setEnabled(false);
        txtCardRegNo.setEnabled(false);
        txtHVoucherType.setEnabled(false);
        txtCardType.setEnabled(false);
        txtContactPerson.setEnabled(false);
        txtLinkedAC.setEnabled(false);
        txtAllotmentDate.setEnabled(false);
        txtMobileNo.setEnabled(false);
        txtValidDate.setEnabled(false);
        txtVoucherNo.setEnabled(false);

        if (topControl.getMode().toUpperCase().equalsIgnoreCase("NEW")) {
            btnCardHelp.setEnabled(true);
            txtCardNo.setEnabled(true);
            txtHVoucherType.setEnabled(true);
            txtHVoucherType.setTag(defaultValue.getVoucherTag());;
            txtHVoucherType.setText(defaultValue.getVoucherText());
            txtVoucherDate.setText(defaultValue.getCurrentDate());

        }

    }

    @Override
    public void action(String nature, Object object, DmCallBack callBack) {
        nature = nature.toUpperCase();
        switch (nature) {
            case "DEACTIVE":
                topControl.btnClick("New");
                getCardDetail("", "", ((String) object), true, callBack);
                break;
            case "FOCUS_ON_ADD":
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        txtRemark.setFocus(true);
                    }
                });
                break;
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
                    txtRemark.setFocus(true);
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
            txtMobileNo.setText(DmShared.xNull(data.getMobile()));
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
                    txtRemark.setFocus(true);
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
