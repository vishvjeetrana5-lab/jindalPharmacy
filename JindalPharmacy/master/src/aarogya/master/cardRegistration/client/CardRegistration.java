package aarogya.master.cardRegistration.client;

import aarogya.master.cardRegistration.shared.*;
import aarogya.projectMenu.self.shared.pmMaster;
import aarogya.selfToolkit.patientSearch.client.PatientSearch;
import aarogya.selfToolkit.patientSearch.shared.SearchValue_Mdl;
import aarogya.selfToolkit.walletSystem.shared.CardRegistration_Mdl;
import aarogya.selfToolkit.walletSystem.shared.CardRegistration_Vld;
import aarogya.selfToolkit.walletSystem.shared.PersonDetail_Mdl;
import aarogya.selfToolkit.walletSystem.shared.TxtHCardType_Mdl;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmHelpGrid.client.DmHelpGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import dataman.dmToolkit.self.client.SetWidgetInvokerImpl;
import dataman.dmToolkit.self.shared.*;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public final class CardRegistration extends CardRegistration_Dsg implements BlurHandler, ClickHandler, KeyDownHandler {

    private final ServiceInvoker<CardRegistration_SrvAsync> srvAsync;
    private KeyFieldExtended defaultMdl;
    private final PatientSearch patientSearch = new PatientSearch();
    private final String vrCategory = new LibraryVoucherCategory().CARD_REGISTRATION;
    private boolean isPhysicallyRequired = false;

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setRefreshRequired(false);
        topControl.setOtherRequired(false);

        topControl.setUserPermission(menuPermission, new VoucherPermissionHandler(DmClient.getVoucherPermission(new String[]{vrCategory})) {
            @Override
            public String getVoucherType() {
                return defaultMdl.getSharedKeyFieldCode();
            }

            @Override
            public String getVoucherDate() {
                return txtAllotmentDate.getText();
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
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        txtHCardType.setFocus(true);
                    }
                });
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                manageDisplay();
                txtExpiryDate.setFocus(true);
                txtHLinkAccount.setEnabled(false);
                txtHCardType.setEnabled(false);
            }
        });

        topControl.addDeleteHandeller(new DmTopControl.eDelete() {
            @Override
            public void deleteClicked() {
                new DmConfirmMessageBox("Are you sure? You want to delete this record.", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            topControlDelete(topControl.getDocId());
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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(CardRegistration.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void textHelpEvents() {
        patientSearch.addCallBack(new PatientSearch.CallBack() {

            @Override
            public void ok(String uhidRecId, String uhidDocId) {
                getUHIDDetail(uhidDocId, "");
            }

            @Override
            public void close() {
                txtContactPerson.setFocus(true);
            }
        });
        txtHCardHelp.addHelpGridCallBack(new DmHelpGrid.HelpGridCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHCardHelp(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), btnCardHelp).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHCardHelp.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), btnCardHelp).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                if (blnStatus && tRow != null) {
                    getCardNo(tRow.getSharedKeyFieldCode(), true);
                }
            }
        });
        txtHCity.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHCity_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHCity(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHCity_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCity).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHCity_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHCity.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCity).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHCity_Mdl t1Row) {
            }
        });
        txtHLinkAccount.addKeyDownHandler(new DmTextHelp.TxtHelpKeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event, Object objSource) {
                if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
                    txtCardNo.setFocus(true);
                }
            }
        });
        txtHLinkAccount.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHLinkAccount(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHLinkAccount).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHLinkAccount.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHLinkAccount).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                txtCardNo.setFocus(true);
            }
        });
        txtHCardType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHCardType_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHCardType(helper, blnToEndOfFile, txtAllotmentDate.getText(), new AsyncCallback<DmResultPageing<List<TxtHCardType_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCardType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHCardType_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHCardType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCardType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHCardType_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
                    setCardMdl(t1Row);
                }
            }
        });
        txtHPrefix.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHPrefix(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPrefix).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPrefix.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPrefix).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtAllotmentDate) {
            txtAllotmentDate.setText(DmClient.returnDate(txtAllotmentDate.getText()));
        } else if (event.getSource() == txtDOA) {
            txtDOA.setText(DmClient.returnDate(txtDOA.getText()));
        } else if (event.getSource() == txtDOB) {
            txtDOB.setText(DmClient.returnDate(txtDOB.getText()));
        } else if (event.getSource() == txtExpiryDate) {
            txtExpiryDate.setText(DmClient.returnDate(txtExpiryDate.getText()));
        }
    }

    private void findEvents() {
        dmFind.setTitle(this.asWidget().getTitle());

        dmFind.addHelpGridCallBack(new DmFind.FindCallBack<Find_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getFind(helper, blnToEndOfFile, topControl.getPermissions(), DmClient.getVoucherPrefix(), new AsyncCallback<DmResultPageing<List<Find_Mdl>>>() {

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

    public CardRegistration(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        srvAsync = new ServiceInvoker<>(GWT.create(CardRegistration_Srv.class), CardRegistration.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        textHelpEvents();
        findEvents();
        handlerEvents();
        iniDefaultValue(swi);
    }

    private void handlerEvents() {
        txtAllotmentDate.addBlurHandler(this);
        txtDOA.addBlurHandler(this);
        txtDOB.addBlurHandler(this);
        txtExpiryDate.addBlurHandler(this);
        btnUHID.addClickHandler(this);
        btnCardHelp.addClickHandler(this);
        btnCardRecharge.addClickHandler(this);
        btnCardDischarge.addClickHandler(this);
        btnCardDeactive.addClickHandler(this);
        txtCardNo.addKeyDownHandler(this);
    }

    private void iniDefaultValue(final SetWidgetInvoker swi) {
        srvAsync.invokeRPC().getDefaultValue(new AsyncCallback<DmResult<KeyFieldExtended>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    defaultMdl = result.getData();
                    swi.setWidget(CardRegistration.this);
                    manageDisplay();
                } else {
                    new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                }
            }
        });
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private CardRegistration_Mdl topControlSaveInitialize() {
        CardRegistration_Mdl mrTemp = new CardRegistration_Mdl();
        PersonDetail_Mdl pdm = new PersonDetail_Mdl();
        TxtHCardType_Mdl ctm = new TxtHCardType_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        } else if (topControl.getMode().equalsIgnoreCase("New") && isPhysicallyRequired) {
            mrTemp.setPhysicalId(DmShared.xNull(txtCardNo.getText()));
        }
        ctm.setSharedKeyFieldCode(DmShared.xNull(txtHCardType.getTag()));
        ctm.setSubCode(DmShared.xNull(txtHLinkAccount.getTag()));
        mrTemp.setCardMdl(ctm);
        pdm.setAddress(DmShared.xNull(txtAddress.getText()));
        pdm.setCityCode(DmShared.xNull(txtHCity.getTag()));
        pdm.setDateDOB(DmShared.dNull(txtDOB.getText()));
        pdm.setDocIdUHID(DmShared.xNull(txtUHID.getTag()));
        pdm.setEmail(DmShared.xNull(txtEmail.getText()));
        pdm.setMobile(DmShared.xNull(txtMobile.getText()));
        pdm.setPerson(DmShared.xNull(txtContactPerson.getText()));
        pdm.setInitial(DmShared.xNull(txtHPrefix.getText()));
        mrTemp.setPersonMdl(pdm);
        mrTemp.setSharedKeyFieldName(txtVrNo.getText());
        mrTemp.setVoucherType(defaultMdl.getSharedKeyFieldCode());
        mrTemp.setVoucherDate(DmShared.dNull(txtAllotmentDate.getText()));
        mrTemp.setExpiryDate(DmShared.dNull(txtExpiryDate.getText()));
        mrTemp.setRemark(DmShared.xNull(txtRemark.getText()));
        mrTemp.setCardNo(DmShared.xNull(txtCardNo.getText()));

        return mrTemp;
    }

    private void topControlSave() {
        CardRegistration_Mdl mrTemp;
        try {
            mrTemp = topControlSaveInitialize();
            CardRegistration_Vld.chkSave(mrTemp);
            srvAsync.invokeRPC().saveRecord(topControl.getPermissions(), mrTemp, DmClient.getVoucherPrefix(), new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHCardType).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        topControl.setDocId(result.getData());
                        if (topControl.getMode().equalsIgnoreCase("New")) {
                            new DmConfirmMessageBox("Do you want to recharge this wallet/card.", new DialogHideEvent.DialogHideHandler() {

                                @Override
                                public void onDialogHide(DialogHideEvent event) {
                                    if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                                        invokeWidget(true, new pmMaster().moduleName, new pmMaster().CARD_RECHARGE.caption, "RECHARGE", "FOCUS_ON_ADD", null);
                                    }
                                }
                            }).show();
                        }
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
                new DmAlertMessageBox(errorMessage, txtHCardType).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtHLinkAccount).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, btnCardHelp).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtNature).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtNature).show();
                break;
//            case 600:
//                new DmAlertMessageBox(errorMessage, txtRecDate).show();
//                break;
            case 700:
                new DmAlertMessageBox(errorMessage, txtAllotmentDate).show();
                break;
            case 800:
                new DmAlertMessageBox(errorMessage, txtContactPerson).show();
                break;
            case 900:
                new DmAlertMessageBox(errorMessage, txtMobile).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHCardType).show();
                break;

        }

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

    private void topControlMoveRec(String keyCode) {
        srvAsync.invokeRPC().getMoveRec(keyCode, new AsyncCallback<DmResult<CardRegistration_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<CardRegistration_Mdl> result) {
                clear();
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<CardRegistration_Mdl> result) {
        CardRegistration_Mdl data;
        String balance;
        if (result.isStatus()) {
            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());
            setPersonDetail(data.getPersonMdl());
            setCardMdl(data.getCardMdl());
            txtVrNo.setText(data.getSharedKeyFieldName());
            txtAllotmentDate.setText(data.getVoucherDate());
            txtExpiryDate.setText(data.getExpiryDate());
            txtCardNo.setTag(data.getPhysicalId());
            txtCardNo.setText(data.getCardNo());
            txtRemark.setText(data.getRemark());
            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());
            balance = DmShared.vNull(data.getBalance()).doubleValue() > 0 ? (DmClient.vNull(Math.abs(data.getBalance()), "0.00") + " Dr") : (DmClient.vNull(Math.abs(data.getBalance()), "0.00") + " Cr");
            lblCardBalance.setText(DmShared.vNull(data.getBalance()).doubleValue() != 0 ? ("Balance: " + balance) : "");

        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
        manageDisplay();
    }

    private void setCardMdl(TxtHCardType_Mdl ctm) {
        if (ctm != null) {
            txtHCardType.setTag(ctm.getSharedKeyFieldCode());
            txtHCardType.setText(ctm.getSharedKeyFieldName());
            txtHLinkAccount.setTag(ctm.getSubCode());
            txtHLinkAccount.setText(ctm.getLinkAccount());
            txtNature.setText(ctm.getNature());
            txtValPerPoint.setText(ctm.getValuePerPoint());
            txtPoint.setText("=" + ctm.getPoints() + " Point(s)");
            txtOnAmount.setText(ctm.getOnAmount());
            txtExpiryDate.setText(DmShared.dNull(ctm.getExpiryDate()));
            isPhysicallyRequired = ctm.isPhysicallyRequired();
        }
    }

    private void topControlDelete(String keyCode) {
        srvAsync.invokeRPC().deleteRecord(topControl.getPermissions(), keyCode, new AsyncCallback<DmResult<String>>() {

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

    private void manageDisplay() {
        txtUHID.setEnabled(false);
        txtAllotmentDate.setEnabled(false);
        btnCardHelp.setEnabled(false);
        txtCardNo.setEnabled(false);
        txtOnAmount.setEnabled(false);
        txtNature.setEnabled(false);
        txtValPerPoint.setEnabled(false);
        txtVrNo.setEnabled(false);
        txtPoint.setEnabled(false);
        flwSearchPatient.clear();
        if (topControl.getMode().equalsIgnoreCase("New")) {
            flwSearchPatient.add(btnUHID);
            btnCardHelp.setEnabled(true);
            txtCardNo.setEnabled(true);
            txtUHID.setEnabled(true);
            txtAllotmentDate.setEnabled(true);
        }

    }

    private void onAdd() {
        clear();
        txtAllotmentDate.setText(defaultMdl.getSharedKeyFieldName());
        txtHPrefix.setTag("Mr.");
        txtHPrefix.setText("Mr.");
        manageDisplay();
    }

    private void clear() {
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        txtCardNo.setTag("");
        txtCardNo.setTag2nd("");
        txtCardNo.setText("");
        isPhysicallyRequired = false;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnCardHelp)) {
            txtHCardHelp.show(btnCardHelp, "");
            txtHCardHelp.reQuery("");
        }

        btnClick((Button) event.getSource());
    }

    @Override
    public void searchRecord(String key) {
    }

    private void getUHIDDetail(String docIdUHID, String uhidRecId) {
        srvAsync.invokeRPC().getUHIDDetails(docIdUHID, uhidRecId, new AsyncCallback<DmResult<PersonDetail_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtUHID).show();

            }

            @Override
            public void onSuccess(DmResult<PersonDetail_Mdl> result) {
                if (result.isStatus()) {
                    setPersonDetail(result.getData());
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            txtRemark.setFocus(true);
                        }
                    });
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtUHID).show();
                }
            }
        });
    }

    private void setPersonDetail(PersonDetail_Mdl data) {
        if (data != null) {
            txtUHID.setText(DmShared.xNull(data.getRecIdUHID()));
            txtUHID.setTag(DmShared.xNull(data.getDocIdUHID()));
            txtHPrefix.setTag(DmShared.xNull(data.getInitial()));
            txtHPrefix.setText(DmShared.xNull(data.getInitial()));
            txtContactPerson.setText(DmShared.xNull(data.getPerson()));
            txtAddress.setText(DmShared.xNull(data.getAddress()));
            txtHCity.setText(DmShared.xNull(data.getCityName()));
            txtHCity.setTag(DmShared.xNull(data.getCityCode()));
            txtDOB.setText(DmShared.xNull(data.getDateDOB()));
            txtEmail.setText(DmShared.xNull(data.getEmail()));
            txtMobile.setText(DmShared.xNull(data.getMobile()));
        }
    }

    private void prnRecord(boolean printToPrinter) {
        HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
        hashMapFilter.put("docId", new FieldUDT_Mdl("Card Registration", DmShared.xNull(txtVrNo.getText()), DmShared.xNull(topControl.getDocId())));
        new OpenSourceReport(defaultMdl.getSharedKeyFieldCode(), "", widgetName, widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
            @Override
            public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                DmClient.localSettingPrintMap(reportProperty, new pmMaster().moduleName, widgetName, "Print");
                return reportProperty;
            }
        }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
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
        if (button.equals(btnCardRecharge)) {
            if (!topControl.getMode().equalsIgnoreCase("New")) {
                invokeWidget(true, new pmMaster().moduleName, new pmMaster().CARD_RECHARGE.caption, "RECHARGE", "FOCUS_ON_ADD", null);
            }
        } else if (button.equals(btnCardDischarge)) {
            if (!topControl.getMode().equalsIgnoreCase("New")) {
                invokeWidget(true, new pmMaster().moduleName, new pmMaster().CARD_REFUND.caption, "DISCHARGE", "FOCUS_ON_ADD", null);
            }
        } else if (button.equals(btnCardDeactive)) {
            if (!topControl.getMode().equalsIgnoreCase("New")) {
                invokeWidget(true, new pmMaster().moduleName, new pmMaster().CARD_DEACTIVE.caption, "DEACTIVE", "FOCUS_ON_ADD", null);
            }
        } else if (button.equals(btnCardHelp)) {
            txtHCardHelp.show(btnCardHelp, "");
            txtHCardHelp.reQuery("");
        } else if (button.equals(btnUHID)) {
            patientSearch.show(true, SearchValue_Mdl.FilterType.WITHOUT_EXPIRED);
        }
    }

    private void invokeWidget(final boolean isCallBack, String moduleName, String menuName, final String firstMessage, final String secondMessage, LinkedHashMap paramHashMap) {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(moduleName, menuName, paramHashMap, null, new SetWidgetInvokerImpl() {
            @Override
            public void setWidget(final IsWidget wgtObject) {
                if (isCallBack) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                    ((IsWidgetExtended) wgtObject).action(firstMessage, topControl.getDocId(), new DmCallBack() {
                        @Override
                        public void result(DmResult result) {
                            if (result != null) {
                                if (result.isStatus()) {
                                    ((IsWidgetExtended) wgtObject).action(secondMessage, null, null);
                                }
                            }
                        }
                    });
                } else {
                    ((IsWidgetExtended) wgtObject).action(firstMessage, secondMessage);
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                }
            }
        });
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getSource().equals(txtCardNo)) {
            if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
                if (!DmShared.xNull(txtCardNo.getText()).trim().equalsIgnoreCase("")) {
                    getCardNo(txtCardNo.getText(), false);
                }
                txtExpiryDate.setFocus(true);
            }
        }
    }

    private void getCardNo(String cardNo, final boolean isHelpGrid) {
        if (DmShared.iNull(txtHCardType.getTag()).longValue() == 0) {
            new DmAlertMessageBox("Card Type is mandatory to use card No.", txtHCardType).show();
            return;
        }
        srvAsync.invokeRPC().getCardNo(cardNo, new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                txtCardNo.setTag("");
                txtCardNo.setTag2nd("");
                txtCardNo.setText("");
                if (isHelpGrid) {
                    new DmAlertMessageBox(caught.getMessage(), btnCardHelp).show();
                } else {
                    new DmAlertMessageBox(caught.getMessage(), btnCardHelp).show();
                }
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                txtCardNo.setTag("");
                txtCardNo.setTag2nd("");
                txtCardNo.setText("");
                if (result.isStatus()) {
                    txtCardNo.setText(result.getData());
                    txtExpiryDate.setFocus(true);
                } else {
                    if (isHelpGrid) {
                        new DmAlertMessageBox(result.getErrorMessage(), btnCardHelp).show();
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), txtCardNo).show();
                    }
                }
            }
        });
    }
}
