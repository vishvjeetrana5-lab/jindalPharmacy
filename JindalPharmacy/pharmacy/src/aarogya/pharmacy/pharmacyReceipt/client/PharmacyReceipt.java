package aarogya.pharmacy.pharmacyReceipt.client;

import aarogya.pharmacy.walkInSearch.client.WalkInSearch;
import aarogya.pharmacy.ledgerMaster.shared.LedgerMaster_Mdl;
import aarogya.pharmacy.partySearch.client.PartySearch;
import aarogya.pharmacy.partySearch.shared.PartySearch_Mdl;
import aarogya.pharmacy.pharmacyReceipt.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.projectMenu.self.shared.pmDayCare;
import aarogya.projectMenu.self.shared.pmOPD;
import dataman.dmBase.self.shared.*;
import aarogya.selfToolkit.cardPaymentPopup.client.*;
import aarogya.selfToolkit.cardPaymentPopup.shared.*;
import aarogya.selfToolkit.modifyReason.client.ModifyReason;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.opdSearch.client.OPDSearch;
import aarogya.selfToolkit.opdSearch.shared.SearchValue_Mdl;
import aarogya.selfToolkit.patientSearchRoomWise.client.PatientSearchRoomWise;
import aarogya.selfToolkit.patientSearchRoomWise.shared.SearchValueRoomWise_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class PharmacyReceipt extends PharmacyReceipt_Dsg implements BlurHandler, AttachEvent.Handler, DmRootEvents, ClickHandler, KeyPressHandler {

    private final ServiceInvoker<PharmacyReceipt_SrvAsync> srvAsync;
    private CardPaymentPopup_Mdl cardPayment = new CardPaymentPopup_Mdl();
    private final CardPaymentPopup cardPaymentPopup = new CardPaymentPopup();
    private final ModifyReason modifyReason = new ModifyReason();
    private DefaultValue_Mdl defaultValue;
    private ModifyReason_Mdl modifyReasonMdl;

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final OPDSearch opdSearch = new OPDSearch();
    private final PatientSearchRoomWise roomSearchIPD = new PatientSearchRoomWise(SearchValueRoomWise_Mdl.FilterType.NOT_BILLED_APPROVED, SearchValueRoomWise_Mdl.FilterType.IPD_ONLY, SearchValueRoomWise_Mdl.FilterType.ALLOW_SITE_FOR_PHARMACY);
    private final PatientSearchRoomWise roomSearchEMRG = new PatientSearchRoomWise(SearchValueRoomWise_Mdl.FilterType.NOT_DISCHARGED, SearchValueRoomWise_Mdl.FilterType.EMERGENCY_ONLY, SearchValueRoomWise_Mdl.FilterType.ALLOW_SITE_FOR_PHARMACY);
    private PatientSearchRoomWise.CallBack roomSearchCallBack;
    private final PartySearch ledgerSearch = new PartySearch();
    private final WalkInSearch walkInSearch = new WalkInSearch();
    boolean opdAllow = false, ipdAllow = false, dayCareAllow = false, emergencyAllow = false, walkInAllow = false, ledgerAllow = false;

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);
        topControl.setDeleteCaptionToDiscard();
        for (String controlPermission : menuPermission.getControlPermission()) {

            if (DmShared.xNull(controlPermission).equalsIgnoreCase(opdAllowBlog)) {
                opdAllow = true;
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(ipdAllowBlog)) {
                ipdAllow = true;
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(dayCareAllowBlog)) {
                dayCareAllow = true;
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(emergencyAllowBlog)) {
                emergencyAllow = true;
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(walkInAllowBlog)) {
                walkInAllow = true;
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(ledgerAllowBlog)) {
                ledgerAllow = true;
            }
        }

        topControl.setUserPermission(menuPermission, new VoucherPermissionHandler(DmClient.getVoucherPermission(new String[]{new ProjectVoucherCategory().PHARMACY_RECEIPT})) {
            @Override
            public String getVoucherType() {
                return defaultValue.getCardPayment().getReceiptTypeCode();
            }

            @Override
            public String getVoucherDate() {
                return txtReceiptDate.getText();
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
                if (!DmShared.xNull(cancelBy.getText()).equalsIgnoreCase("")) {
                    new DmAlertMessageBox("This Record has been discarded.You can't edit this record.", (DmTextBox) null).show();
                    topControl.btnClick("");
                    return;
                }
                manageDisplay(true);
                txtAmount.setFocus(true);
            }
        });

        topControl.addDeleteHandeller(new DmTopControl.eDelete() {
            @Override
            public void deleteClicked() {
                new DmConfirmMessageBox("Are you sure? You want to discard this record.", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            showModifyReason();
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
            public void moveRec(String fetchCode) {
                topControlMoveRec(fetchCode);
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
                showCardPayment();
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(PharmacyReceipt.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void prnRecord(boolean printToPrinter) {
        HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
        hashMapFilter.put("docId", new FieldUDT_Mdl("Receipt No.", DmShared.xNull(txtReceiptNo.getText()), DmShared.xNull(topControl.getDocId())));
        new OpenSourceReport(DmShared.xNull(txtReceiptType.getTag()), "", "Pharmacy Receipt", widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
            @Override
            public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                DmClient.localSettingPrintMap(reportProperty, moduleName, widgetName, "Print");
                return reportProperty;
            }
        }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
    }

    private void textHelpEvents() {
        opdSearch.addCallBack(new OPDSearch.CallBack() {

            @Override
            public void ok(String regRecId, String regDocId) {
                getCustomerDetails(regRecId);
            }

            @Override
            public void close() {
                txtUniqueId.setFocus(true);
            }
        });
        roomSearchCallBack = new PatientSearchRoomWise.CallBack() {
            @Override
            public void ok(String regDocId, String regRecId, String roomCode) {
                getCustomerDetails(regRecId);
            }

            @Override
            public void close() {
                txtUniqueId.setFocus(true);
            }
        };
        walkInSearch.addCallBack(new WalkInSearch.CallBack() {
            @Override
            public void ok(String lmCode, String lmManualCode) {
                getCustomerDetails(lmManualCode);
            }

            @Override
            public void close() {
                txtUniqueId.setFocus(true);
            }

        });
        ledgerSearch.addCallBack(new PartySearch.CallBack() {
            @Override
            public void ok(PartySearch_Mdl partySearchDetails) {
                getCustomerDetails(partySearchDetails.getManualCode());
            }

            @Override
            public void close(boolean isErrorClose) {
                txtUniqueId.setFocus(true);
            }
        });
        cardPaymentPopup.addCallBack(new CardPaymentPopup.CallBack() {

            @Override
            public void onOk(CardPaymentPopup_Mdl cardPaymentMdl) {
                PharmacyReceipt_Mdl mrTemp = new PharmacyReceipt_Mdl();
                cardPayment = new CardPaymentPopup_Mdl();
                cardPayment = cardPaymentMdl.clone();

                txtReceiptType.setText(DmShared.xNull(DmShared.xNull(cardPaymentMdl.getReceiptTypeName())));
                txtReceiptType.setTag(DmShared.xNull(DmShared.xNull(cardPaymentMdl.getReceiptTypeCode())));
                txtReceiptType.setTag2nd(DmShared.xNull(DmShared.xNull(cardPaymentMdl.getReceiptTypePM())));

                mrTemp = topControlSaveInitialize();
                try {
                    PharmacyReceipt_Vld.chkSave(mrTemp);
                    if (topControl.getMode().equalsIgnoreCase("Edit")) {
                        showModifyReason();
                    } else {
                        topControlSave();
                    }
                } catch (DmException e) {
                    topControlSaveError(e.getErrorNo(), e.getMessage());
                }

            }

            @Override
            public void onCancel(CardPaymentPopup_Mdl cardPaymentMdl) {
                txtAmount.setFocus(true);
            }
        });
        txtHCustomerType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHCustomerType(helper, blnToEndOfFile, opdAllow, ipdAllow, dayCareAllow, emergencyAllow, walkInAllow, ledgerAllow, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCustomerType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHCustomerType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCustomerType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (!DmShared.xNull(txtHCustomerType.getTag()).equalsIgnoreCase(DmShared.xNull(txtHCustomerType.getTag2nd()))) {
                    resetTxtHCoustomerType();
                    txtHCustomerType.setTag2nd(DmShared.xNull(txtHCustomerType.getTag()));
                }
                flwSearchBtn.clear();
                if (t1Row != null && blnStatus) {
                    manageBtnSearchRoom();
                }
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        if (!DmShared.xNull(txtHCustomerType.getTag()).trim().equalsIgnoreCase("")) {
                            txtUniqueId.setFocus(true);
                        }
                    }
                });
            }
        });
    }

    private void showModifyReason() {
        modifyReason.show(topControl.getMode(), topControl.getDocId());
        modifyReason.addCallBack(new ModifyReason.CallBack() {

            @Override
            public void onOk(ModifyReason_Mdl modifyReason) {
                PharmacyReceipt.this.modifyReasonMdl = modifyReason;
                if (topControl.getMode().equalsIgnoreCase("Browse") && DmShared.iNull(topControl.getDocId()).longValue() > 0) {
                    topControlDelete(topControl.getDocId());
                } else {
                    topControlSave();
                }
            }
        });
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtReceiptDate)) {
            txtReceiptDate.setText(DmClient.returnDate(txtReceiptDate.getText()));
        } else if (event.getSource().equals(txtAmount)) {
            if (DmShared.vNull(txtAmount.getText()) == 0) {
                txtAmount.setText(DmShared.vNull(txtAmount.getTag()).toString());
            }
        } else if (event.getSource().equals(txtUniqueId)) {
            txtUniqueId.setText(DmShared.xNull(txtUniqueId.getTag2nd()));
        }
    }

    @Override
    public void onKeyPress(KeyPressEvent event) {
        if (event.getSource().equals(txtUniqueId)) {
            if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
                if (!DmShared.xNull(txtUniqueId.getText()).trim().equalsIgnoreCase("")) {
                    if (DmShared.xNull(txtHCustomerType.getTag()).trim().equalsIgnoreCase("")) {
                        new DmAlertMessageBox("Type is mandatory.", txtHCustomerType).show();
                        return;
                    }
                    if (!DmShared.xNull(txtUniqueId.getText()).trim().equalsIgnoreCase(txtUniqueId.getTag2nd())) {
                        if (DmShared.xNull(txtHCustomerType.getTag()).trim().equalsIgnoreCase("Walk-In")) {
                            if (!DmShared.xNull(txtUniqueId.getText()).toUpperCase().contains(":W")) {
                                txtUniqueId.setText(DmShared.xNull(txtUniqueId.getText()) + ":W");
                            }
                        }
                        getCustomerDetails(txtUniqueId.getText());
                    } else {
                        txtAmount.setFocus(true);
                    }
                } else {
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            if ((DmShared.xNull(txtHCustomerType.getTag()).equalsIgnoreCase(pvc.IPD)) || (DmShared.xNull(txtHCustomerType.getTag()).equalsIgnoreCase(pvc.EMERGENCY))) {
                                btnIPDSearch.setFocus(true);
                            } else if ((DmShared.xNull(txtHCustomerType.getTag()).equalsIgnoreCase(pvc.OPD)) || (DmShared.xNull(txtHCustomerType.getTag()).equalsIgnoreCase(pvc.DAY_CARE))) {
                                btnOPDSearch.setFocus(true);
                            } else if (DmShared.xNull(txtHCustomerType.getTag()).equalsIgnoreCase("Walk-In")) {
                                btnWalkInSearch.setFocus(true);
                            } else if (DmShared.xNull(txtHCustomerType.getTag()).equalsIgnoreCase("Ledger")) {
                                btnLedgerSearch.setFocus(true);
                            }
                        }
                    });
                }
            }
        }
    }

    private void getCustomerDetails(String uniqueId) {
        srvAsync.invokeRPC().getCustomerDetails(txtHCustomerType.getTag(), uniqueId, new AsyncCallback<DmResult<LedgerMaster_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtUniqueId).show();
            }

            @Override
            public void onSuccess(DmResult<LedgerMaster_Mdl> result) {
                resetTxtHCoustomerType();
                if (result.isStatus()) {
                    fillData(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), new HideEvent.HideHandler() {
                        @Override
                        public void onHide(HideEvent event) {
                            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                                @Override
                                public void execute() {
                                    txtUniqueId.setFocus(true);
                                }
                            });
                        }
                    }).show();
                }
            }
        });
    }

    private void fillData(LedgerMaster_Mdl lmMdl) {
        if (lmMdl != null) {
            txtUniqueId.setTag(lmMdl.getSharedKeyFieldCode());
            txtUniqueId.setTag2nd(lmMdl.getManualCode());
            txtUniqueId.setText(lmMdl.getManualCode());
            txtDisplayName.setText(lmMdl.getDisplayName());
            if (DmShared.xNull(txtHCustomerType.getTag()).equalsIgnoreCase("Ledger")) {
                txtDisplayName.setTag(lmMdl.getAccountCode());
            } else if (DmShared.xNull(txtHCustomerType.getTag()).equalsIgnoreCase("Walk-In")) {
            } else {
                txtDisplayName.setTag(lmMdl.getRegDocId());
            }
            txtMobile.setText(lmMdl.getMobile());
            txtIdType.setTag(lmMdl.getIdTypeCode());
            txtIdType.setText(lmMdl.getIdTypeName());
            txtIdNo.setText(lmMdl.getIdNo());
            txtAddress.setText(lmMdl.getAddress());
            txtCity.setTag(lmMdl.getCityCode());
            txtCity.setText(lmMdl.getCityName());
            txtAmount.setTag(DmClient.vNull(lmMdl.getPharmacyBalance(), "0.00"));
            txtAmount.setText(DmClient.vNull(lmMdl.getPharmacyBalance(), "0.00"));
            lblPharmacyBalance.setText(DmShared.vNull(lmMdl.getPharmacyBalance()) != 0 ? "Pharmacy Balance: " + lmMdl.getPharmacyBalance() : "");
            txtAmount.setFocus(true);
        } else {
            new DmAlertMessageBox("No record found. Try Again.", txtUniqueId).show();
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
                if (blnStatus && tRow != null) {
                    topControlMoveRec(tRow.getSharedKeyFieldCode());
                }
            }
        });
    }

    private void handlerEvents() {
        flwForm.addAttachHandler(this);
        btnOPDSearch.addClickHandler(this);
        btnIPDSearch.addClickHandler(this);
        btnLedgerSearch.addClickHandler(this);
        btnWalkInSearch.addClickHandler(this);
        txtUniqueId.addKeyPressHandler(this);
        txtUniqueId.addBlurHandler(this);
        txtReceiptDate.addBlurHandler(this);
        txtAmount.addBlurHandler(this);
        roomSearchEMRG.addCallBack(roomSearchCallBack);
        roomSearchIPD.addCallBack(roomSearchCallBack);
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            cardPaymentPopup.hide();
            roomSearchIPD.hide();
            roomSearchEMRG.hide();
            opdSearch.hide();
            ledgerSearch.hide();
            walkInSearch.hide();
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {

    }

    public PharmacyReceipt(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        srvAsync = new ServiceInvoker<>(GWT.create(PharmacyReceipt_Srv.class), PharmacyReceipt.class.getName());
        this.asWidget().setTitle(widgetName);
        ((Button) paymentCBlog.getControlBlog()).addClickHandler(this);
        topControlEvents(menuPermission);
        textHelpEvents();
        findEvents();
        handlerEvents();
        iniDefaultValues(swi);
    }

    private PharmacyReceipt_Mdl topControlSaveInitialize() {
        PharmacyReceipt_Mdl mrTemp = new PharmacyReceipt_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
            mrTemp.getRecIdReceipt().setRecIdValue(DmShared.xNull(txtReceiptNo.getText()));
            mrTemp.getRecIdReceipt().setPrefix(DmShared.xNull(txtReceiptNo.getTag()));
            mrTemp.getRecIdReceipt().setCounter(DmShared.iNull(txtReceiptNo.getTag2nd()).longValue());
        }

        mrTemp.setSharedPreparedBy(userControl.getTextBlog("Prepared By"));
        mrTemp.setVoucherDate(DmShared.dNull(txtReceiptDate.getText()));
        ////////////////////////////////////////////////////////////////////////
        mrTemp.setLmCode(DmShared.xNull(txtUniqueId.getTag()));
//        mrTemp.setUniqueId(DmShared.xNull(txtUniqueId.getText()));
        mrTemp.setManualCode(DmShared.xNull(txtUniqueId.getText()));
        mrTemp.setDisplayName(DmShared.xNull(txtDisplayName.getText()));
        if (DmShared.xNull(txtHCustomerType.getTag()).equalsIgnoreCase("Ledger")) {
            mrTemp.setSubCode(DmShared.xNull(txtDisplayName.getTag()));
        } else if (DmShared.xNull(txtHCustomerType.getTag()).equalsIgnoreCase("Walk-In")) {
        } else {
            mrTemp.setRegDocId(DmShared.xNull(txtDisplayName.getTag()));
        }
        mrTemp.setCustomerTypeCode(DmShared.xNull(txtHCustomerType.getTag()));
        mrTemp.setCustomerTypeName(DmShared.xNull(txtHCustomerType.getText()));
        mrTemp.setAddress(DmShared.xNull(txtAddress.getText()));
        mrTemp.setCityCode(DmShared.xNull(txtCity.getTag()));
        mrTemp.setCityName(DmShared.xNull(txtCity.getText()));
        mrTemp.setMobile(DmShared.xNull(txtMobile.getText()));
        mrTemp.setIdTypeCode(DmShared.xNull(txtIdType.getTag()));
        mrTemp.setIdTypeName(DmShared.xNull(txtIdType.getText()));
        mrTemp.setIdNo(DmShared.xNull(txtIdNo.getText()));
        ////////////////////////////////////////////////////////////////////////
        mrTemp.setAmount(DmShared.vNull(txtAmount.getText()).toString());
        mrTemp.setPharmacyBalance(DmShared.vNull(txtAmount.getTag()).toString());
        mrTemp.setRemark(DmShared.xNull(txtRemark.getText()));
        mrTemp.getCardPayment().getGrdCardPaymentList().addAll(new ArrayList<GrdCardPayment_Mdl>());
        mrTemp.setCardPayment(cardPayment.clone());
        if (modifyReasonMdl != null) {
            mrTemp.setModifyReason(modifyReasonMdl);
        }
        return mrTemp;
    }

    private void topControlSave() {
        PharmacyReceipt_Mdl mrTemp;
        try {
            mrTemp = topControlSaveInitialize();
            PharmacyReceipt_Vld.chkSave(mrTemp);
            cardPaymentPopup.chkSave(mrTemp.getAmount(), mrTemp.getCardPayment());

            srvAsync.invokeRPC().saveRecord(topControl.getPermissions(), mrTemp, DmClient.getVoucherPrefix(), new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtMobile).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        topControl.setDocId(result.getData());
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
                new DmAlertMessageBox(errorMessage, txtHCustomerType).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtUniqueId).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtAmount).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHCustomerType).show();
                break;
        }
    }

    private void manageDisplay(boolean flag) {
        txtUniqueId.setEnabled(flag);
        txtReceiptType.setEnabled(false);
        txtReceiptNo.setEnabled(false);
        txtReceiptDate.setEnabled(false);
        txtDisplayName.setEnabled(false);
        txtIdType.setEnabled(false);
        txtIdNo.setEnabled(false);
        txtAddress.setEnabled(false);
        txtCity.setEnabled(false);
        txtMobile.setEnabled(false);
        flwSearchBtn.clear();
        if (topControl.getMode().trim().equalsIgnoreCase("New")) {
            manageBtnSearchRoom();
        } else if (topControl.getMode().trim().equalsIgnoreCase("Edit")) {
            txtHCustomerType.setEnabled(false);
            txtUniqueId.setEnabled(false);
        }
    }

    private void showCardPayment() {
        CardPaymentPopup_Mdl cardPaymentMdl = new CardPaymentPopup_Mdl();
        cardPaymentMdl = this.cardPayment.clone();
        cardPaymentMdl.setAmount(DmClient.vNull(txtAmount.getText(), "0.00"));
        cardPaymentMdl.setPatientName(DmShared.xNull(txtDisplayName.getText()));
//        cardPaymentMdl.setUHIDNo(DmShared.xNull(txtUHIDNo.getText()));
//        cardPaymentMdl.setUHIDDocid(DmShared.xNull(txtUHIDNo.getTag()));
//        cardPaymentMdl.setRegNo(DmShared.xNull(txtBillNo.getText()));
//        cardPaymentMdl.setRegNoDocid(DmShared.xNull(txtBillNo.getTag()));
        cardPaymentMdl.setReceiptNo(DmShared.xNull(txtReceiptNo.getText()));
        cardPaymentMdl.setReceiptTypeCode(DmShared.xNull(txtReceiptType.getTag()));
        cardPaymentMdl.setReceiptTypeName(DmShared.xNull(txtReceiptType.getText()));
        cardPaymentMdl.setReceiptTypePM(DmShared.xNull(txtReceiptType.getTag2nd()));
        cardPaymentPopup.setCTPAllowed(false);
        cardPaymentPopup.setCTRAllowed(false);
        cardPaymentPopup.setCTOAllowed(false);
        if (topControl.getMode().equalsIgnoreCase("Browse")) {
            cardPaymentPopup.show(cardPaymentMdl, DmShared.xNull(topControl.getMode()));
        } else {
            try {
                cardPaymentPopup.show(cardPaymentMdl, DmShared.xNull(topControl.getMode()));
            } catch (DmException e) {
                new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
            }
        }

    }

    @Override
    public void onClick(ClickEvent event) {
        btnClick((Button) event.getSource());
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
        if (button == paymentCBlog.getControlBlog()) {
            showCardPayment();
        } else if (button == btnIPDSearch) {
            if (DmShared.xNull(txtHCustomerType.getTag()).equalsIgnoreCase(pvc.IPD)) {
                roomSearchIPD.show();
            } else if (DmShared.xNull(txtHCustomerType.getTag()).equalsIgnoreCase(pvc.EMERGENCY)) {
                roomSearchEMRG.show();
            }
        } else if (button == btnOPDSearch) {
            if (DmShared.xNull(txtHCustomerType.getTag()).equalsIgnoreCase(pvc.OPD)) {
                opdSearch.show(new pmOPD().moduleName, SearchValue_Mdl.OPDFilterType.SERVICE_DAYS,
                        SearchValue_Mdl.OPDFilterType.OPD_ONLY, SearchValue_Mdl.OPDFilterType.ALLOW_SITE_FOR_PHARMACY);
            } else if (DmShared.xNull(txtHCustomerType.getTag()).equalsIgnoreCase(pvc.DAY_CARE)) {
                opdSearch.show(new pmDayCare().moduleName, SearchValue_Mdl.OPDFilterType.CANCEL_DAYS,
                        SearchValue_Mdl.OPDFilterType.DAYCARE_ONLY, SearchValue_Mdl.OPDFilterType.ALLOW_SITE_FOR_PHARMACY);
            }
        } else if (button == btnWalkInSearch) {
            walkInSearch.show();
        } else if (button == btnLedgerSearch) {
            ledgerSearch.show("", "");
        }
    }

    private void manageBtnSearchRoom() {
        flwSearchBtn.clear();
        if ((DmShared.xNull(txtHCustomerType.getTag()).equalsIgnoreCase(pvc.IPD)) || (DmShared.xNull(txtHCustomerType.getTag()).equalsIgnoreCase(pvc.EMERGENCY))) {
            flwSearchBtn.add(btnIPDSearch);
        } else if ((DmShared.xNull(txtHCustomerType.getTag()).equalsIgnoreCase(pvc.OPD)) || (DmShared.xNull(txtHCustomerType.getTag()).equalsIgnoreCase(pvc.DAY_CARE))) {
            flwSearchBtn.add(btnOPDSearch);
        } else if ((DmShared.xNull(txtHCustomerType.getTag()).equalsIgnoreCase("Walk-In"))) {
            flwSearchBtn.add(btnWalkInSearch);
        } else if ((DmShared.xNull(txtHCustomerType.getTag()).equalsIgnoreCase("Ledger"))) {
            flwSearchBtn.add(btnLedgerSearch);
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
                    manageDisplay(false);
                    swi.setWidget(PharmacyReceipt.this);
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
        srvAsync.invokeRPC().getNavigator(topControl.getPermissions(), new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<KeyFieldExtended>> result) {
                if (result.isStatus()) {
                    topControl.setNavigator(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void topControlMoveRec(String strKey) {
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<PharmacyReceipt_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<PharmacyReceipt_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<PharmacyReceipt_Mdl> result) {
        clear();
        if (result.isStatus()) {
            PharmacyReceipt_Mdl data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());

            txtReceiptDate.setText(data.getVoucherDate());
            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());

            txtUniqueId.setTag(data.getLmCode());
            txtUniqueId.setTag2nd(data.getManualCode());
            txtUniqueId.setText(data.getManualCode());
            txtAmount.setText(data.getAmount());
            txtAmount.setTag(data.getEntryBalance());
            txtRemark.setText(data.getRemark());
            txtHCustomerType.setTag(data.getCustomerTypeCode());
            txtHCustomerType.setTag2nd(data.getCustomerTypeCode());
            txtHCustomerType.setText(data.getCustomerTypeName());

            //Display Only fileds
            txtDisplayName.setText(data.getDisplayName());
            if (DmShared.xNull(txtHCustomerType.getTag()).equalsIgnoreCase("Ledger")) {
                txtDisplayName.setTag(data.getSubCode());
            } else if (DmShared.xNull(txtHCustomerType.getTag()).equalsIgnoreCase("Walk-In")) {
            } else {
                txtDisplayName.setTag(data.getRegDocId());
            }
            txtIdType.setText(data.getIdTypeName());
            txtIdType.setTag(data.getIdTypeCode());
            txtIdNo.setText(data.getIdNo());
            txtAddress.setText(data.getAddress());
            txtCity.setText(data.getCityName());
            txtCity.setText(data.getCityCode());
            txtMobile.setText(data.getMobile());
            lblPharmacyBalance.setText(DmShared.vNull(data.getPharmacyBalance()) != 0 ? "Pharmacy Balance: " + data.getPharmacyBalance() : "");

            // set the Receipt Details for payment popup in Moverec
            txtReceiptNo.setText(DmShared.xNull(data.getRecIdReceipt().getRecIdValue()));
            txtReceiptNo.setTag(DmShared.xNull(data.getRecIdReceipt().getPrefix()));
            txtReceiptNo.setTag2nd(DmShared.iNull(data.getRecIdReceipt().getCounter()).toString());
            txtReceiptType.setText(data.getRecVDescription());
            txtReceiptType.setTag(data.getRecVType());
            txtReceiptType.setTag2nd(data.getRecAttachPaymentMode());
            cardPayment = data.getCardPayment();
            cardPayment.getTxtHReceiptTypeVList().addAll(defaultValue.getCardPayment().getTxtHReceiptTypeVList());
            if (DmShared.iNull(data.getIsCancel()).intValue() == 1) {
                cancelBy.setText("Discarded");
            } else {
                cancelBy.setText("");
            }
            flwSearchBtn.clear();
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
        manageDisplay(false);
    }

    private void topControlDelete(String strKey) {
        srvAsync.invokeRPC().deleteRecord(topControl.getPermissions(), strKey, modifyReasonMdl, new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    topControlMoveRec(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    private void onAdd() {
        clear();
        manageDisplay(true);
        txtHCustomerType.setFocus(true);
        txtReceiptDate.setText(DmShared.dNull(defaultValue.getCurrentDate()));
    }

    private void clear() {
        txtHCustomerType.setText("");
        txtHCustomerType.setTag("");
        txtHCustomerType.setTag2nd("");

        txtReceiptDate.setText("");
        txtReceiptDate.setTag("");
        txtReceiptDate.setTag2nd("");

        resetTxtHCoustomerType();
    }

    private void resetTxtHCoustomerType() {
        txtUniqueId.setText("");
        txtUniqueId.setTag("");
        txtUniqueId.setTag2nd("");

        txtReceiptNo.setText("");
        txtReceiptNo.setTag("");
        txtReceiptNo.setTag2nd("");

        txtReceiptType.setText("");
        txtReceiptType.setTag("");
        txtReceiptType.setTag2nd("");

        txtDisplayName.setText("");
        txtDisplayName.setTag("");
        txtDisplayName.setTag2nd("");

        txtMobile.setText("");
        txtMobile.setTag("");
        txtMobile.setTag2nd("");

        txtIdType.setText("");
        txtIdType.setTag("");
        txtIdType.setTag2nd("");

        txtIdNo.setText("");
        txtIdNo.setTag("");
        txtIdNo.setTag2nd("");

        txtAddress.setText("");
        txtAddress.setTag("");
        txtAddress.setTag2nd("");

        txtCity.setText("");
        txtCity.setTag("");
        txtCity.setTag2nd("");

        txtAmount.setText("");
        txtAmount.setTag("");
        txtAmount.setTag2nd("");

        txtRemark.setText("");
        txtRemark.setTag("");
        txtRemark.setTag2nd("");

        cancelBy.setText("");
        lblPharmacyBalance.setText("");

        modifyReasonMdl = new ModifyReason_Mdl();

        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");

        cardPayment = new CardPaymentPopup_Mdl();
        cardPayment = defaultValue.getCardPayment().clone();

    }

}
