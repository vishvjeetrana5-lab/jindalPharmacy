package aarogya.pharmacy.changePaymentSource.client;

import aarogya.pharmacy.changePaymentSource.shared.*;
import aarogya.pharmacy.saleBillSearch.client.SaleBillSearch;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import dataman.dmBase.self.shared.*;
import aarogya.selfToolkit.stStatic.shared.STEnum.PaymentSource;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import java.util.HashMap;
import java.util.List;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;

public final class ChangePaymentSource extends ChangePaymentSource_Dsg implements BlurHandler, ClickHandler, AttachEvent.Handler, DmRootEvents, KeyPressHandler {

    private final ServiceInvoker<ChangePaymentSource_SrvAsync> SrvAsync;
    private DefaultValue_Mdl defaultValue;
    private final SaleBillSearch saleBSearch = new SaleBillSearch();

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);
        topControl.setUserPermission(menuPermission, new VoucherPermissionHandler(DmClient.getVoucherPermission(new String[]{new ProjectVoucherCategory().CHANGE_PAYMENT_SOURCE})) {
            @Override
            public String getVoucherType() {
                return defaultValue.getVoucherTypeCode();
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
                manageDisplay(false);
                txtHChangePS.setFocus(true);
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
                topControlSave();
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(ChangePaymentSource.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void prnRecord(boolean printToPrinter) {
        HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
        hashMapFilter.put("docId", new FieldUDT_Mdl("Voucher No.", DmShared.xNull(txtVoucherNo.getText()), DmShared.xNull(topControl.getDocId())));
        new OpenSourceReport(defaultValue.getVoucherTypeCode(), "", widgetName, widgetName, hashMapFilter, printToPrinter, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
    }

    private void textHelpEvents() {
        saleBSearch.addCallBack(new SaleBillSearch.CallBack() {
            @Override
            public void ok(String billDocId) {
                getBillDetail(billDocId, "");
            }

            @Override
            public void close() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });

        txtHType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                if (blnStatus && tRow != null) {
                    onTypeSelection();
                }
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        txtRegNo.setFocus(true);
                    }
                });
            }
        });
        txtHChangePS.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHPaymentSource(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHChangePS).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHChangePS.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHChangePS).show();
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
                if (blnStatus && tRow != null) {
                    topControlMoveRec(tRow.getSharedKeyFieldCode());
                }
            }
        });
    }

    private void handlerEvents() {
        txtVoucherDate.addBlurHandler(this);
        txtServiceAmount.addBlurHandler(this);
        txtRegNo.addBlurHandler(this);
        txtRegNo.addKeyPressHandler(this);
        btnSaleBillSearch.addClickHandler(this);

    }

    public ChangePaymentSource(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(ChangePaymentSource_Srv.class), ChangePaymentSource.class.getName());
        this.asWidget().setTitle(widgetName);
        handlerEvents();
        topControlEvents(menuPermission);
        textHelpEvents();
        findEvents();
        flwForm.addAttachHandler(this);
        iniDefaultValues(swi);
    }

    private ChangePaymentSource_Mdl topControlSaveInitialize() {
        ChangePaymentSource_Mdl mrTemp = new ChangePaymentSource_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
            mrTemp.getRecId().setRecIdValue(DmShared.xNull(txtVoucherNo.getText()));
            mrTemp.getRecId().setPrefix(DmShared.xNull(txtVoucherNo.getTag()));
            mrTemp.getRecId().setCounter(DmShared.iNull(txtVoucherNo.getTag2nd()).longValue());
        }
        mrTemp.setVoucherDate(DmShared.dNull(txtVoucherDate.getText()));
        mrTemp.setVoucherTypeCode(DmShared.xNull(defaultValue.getVoucherTypeCode()));
        mrTemp.setChangePaymentSource(DmShared.xNull(txtHChangePS.getTag()));
        mrTemp.setRegType(DmShared.xNull(txtHType.getText()));
        mrTemp.getPatientDetail().setRegDocId(DmShared.xNull(txtPatientName.getTag()));
        mrTemp.getPatientDetail().setOrganizationCode(DmShared.xNull(txtOrganization.getTag()));
        mrTemp.getPatientDetail().setPaymentSource(DmShared.xNull(txtPaymentSource.getTag()));
        mrTemp.getPatientDetail().setRegDate(DmShared.dNull(txtVoucherDate.getTag()));
        mrTemp.getPatientDetail().setBillDocId(DmShared.xNull(txtRegNo.getTag()));

        mrTemp.setSharedPreparedBy(userControl.getTextBlog("prepared By"));

        return mrTemp;
    }

    private void topControlSave() {
        ChangePaymentSource_Mdl mrTemp;
        try {
            mrTemp = topControlSaveInitialize();
            ChangePaymentSource_Vld.chkSave(mrTemp);

            SrvAsync.invokeRPC().saveRecord(topControl.getPermissions(), mrTemp, DmClient.getVoucherPrefix(), new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtMobile).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        txtServiceAmount.setText("");
                        topControl.setDocId(result.getData());
                        if (topControl.getMode().equalsIgnoreCase("New")) {
                            new DmConfirmMessageBox("Are you sure? You want to print this record.", new DialogHideEvent.DialogHideHandler() {
                                @Override
                                public void onDialogHide(DialogHideEvent event) {
                                    if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                                        prnRecord(true);
                                    }
//                                    topControl.btnClick("New");
                                }
                            }).show();

                        } else {
//                            topControlMoveRec(topControl.getDocId());
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
                new DmAlertMessageBox(errorMessage, txtHType).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtRegNo).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtVoucherDate).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtHChangePS).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHChangePS).show();
                break;
        }
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
                    defaultValue = result.getData();
                    manageDisplay(false);
                    swi.setWidget(ChangePaymentSource.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    @Override
    public void onKeyPress(KeyPressEvent event) {
        if (event.getSource().equals(txtRegNo)) {
            if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
                if (!DmShared.xNull(txtRegNo.getText()).trim().equalsIgnoreCase("")) {
                    if (DmShared.xNull(txtHType.getTag()).trim().equalsIgnoreCase("")) {
                        new DmAlertMessageBox("Type is mandatory.", txtHType).show();
                        return;
                    }
                    getBillDetail("", DmShared.xNull(txtRegNo.getText()));
                } else {

                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            btnSaleBillSearch.setFocus(true);
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtVoucherDate)) {
            txtVoucherDate.setText(DmClient.returnDate(txtVoucherDate.getText()));
        } else if (event.getSource().equals(txtServiceAmount)) {
            txtServiceAmount.setText(DmClient.vNull(DmShared.vNull(txtServiceAmount.getText()).doubleValue(), "0.00"));
        } else if (event.getSource() == txtRegNo) {
            txtRegNo.setText(DmShared.xNull(txtRegNo.getTag2nd()));
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getShiftKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_P) {
            rootEvent.stopPropagation();
        }
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
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
                    topControl.setNavigator(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void topControlMoveRec(String strKey) {
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<ChangePaymentSource_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<ChangePaymentSource_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<ChangePaymentSource_Mdl> result) {
        ChangePaymentSource_Mdl data;
        manageDisplay(false);
        clear();
        if (result.isStatus()) {
            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());
            fillDetail(data.getPatientDetail());

            txtVoucherDate.setText(data.getVoucherDate());
            txtVoucherNo.setText(DmShared.xNull(data.getRecId().getRecIdValue()));
            txtVoucherNo.setTag(data.getRecId().getPrefix());
            txtVoucherNo.setTag2nd(DmShared.iNull(data.getRecId().getCounter()).toString());
            txtHType.setTag(DmShared.xNull(data.getRegType()));
            txtHType.setText(DmShared.xNull(data.getRegType()));
            if (!DmShared.xNull(data.getChangePaymentSource()).trim().equalsIgnoreCase("")) {
                txtHChangePS.setTag(DmShared.xNull(data.getChangePaymentSource()));
                txtHChangePS.setText(PaymentSource.getValueOf(DmShared.iNull(data.getChangePaymentSource()).intValue()).getEnumName());
            } else {
                txtHChangePS.setTag("");
                txtHChangePS.setText("");
            }
            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
    }

    private void topControlDelete(String strKey) {
        SrvAsync.invokeRPC().deleteRecord(topControl.getPermissions(), strKey, new AsyncCallback<DmResult<String>>() {
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

    private void getBillDetail(String billDocId, String billRecId) {
        SrvAsync.invokeRPC().getBillDetails(billDocId, billRecId, DmShared.xNull(txtHType.getTag()), new AsyncCallback<DmResult<PatientDetail_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtRegNo).show();
            }

            @Override
            public void onSuccess(DmResult<PatientDetail_Mdl> result) {
                if (result.isStatus()) {
                    fillDetail(result.getData());
                    if (!txtVoucherDate.isEnabled()) {
                        txtVoucherDate.setText(result.getValue1());
                    }
                    txtHChangePS.setFocus(true);
                } else {
                    txtRegNo.setText(DmShared.xNull(txtRegNo.getTag2nd()));
                    new DmAlertMessageBox(result.getErrorMessage(), txtRegNo).show();
                }
            }
        });
    }

    private void fillDetail(PatientDetail_Mdl data) {
        txtRegNo.setText(data.getBillNo());
        txtRegNo.setTag(data.getBillDocId());
        txtRegNo.setTag2nd(data.getBillNo());

        txtUHIDNo.setTag(data.getUhidDocId());
        txtUHIDNo.setText(data.getUhidNo());

        txtVoucherDate.setTag(data.getRegDate());

        txtPatientName.setTag(data.getRegDocId());
        txtPatientName.setText(data.getPatientName());
        txtGuardianName.setText(data.getGuardianName());
        txtGender.setText(data.getGender());
        txtAge.setText(data.getAge());
        txtMobile.setText(data.getMobile());
        txtAddress.setText(data.getAddress());
        txtConsultant.setText(data.getConsultantName());
        txtCase.setText(data.getCaseName());
        txtOrganization.setTag(data.getOrganizationCode());
        txtOrganization.setText(data.getOrganizationName());
        if (!DmShared.xNull(data.getPaymentSource()).trim().equalsIgnoreCase("")) {
            txtPaymentSource.setTag(DmShared.xNull(data.getPaymentSource()));
            txtPaymentSource.setText(PaymentSource.getValueOf(DmShared.iNull(data.getPaymentSource()).intValue()).getEnumName());
        } else {
            txtPaymentSource.setTag("");
            txtPaymentSource.setText("");
        }
        txtServiceAmount.setText(DmClient.vNull(data.getServiceAmount(), "0.00"));
        txtReceiptAmount.setText(DmClient.vNull(data.getReceiptAmount(), "0.00"));
    }

    private void manageDisplay(boolean flag) {
        txtRegNo.setEnabled(flag);
        txtHType.setEnabled(flag);
        txtUHIDNo.setEnabled(false);
        txtVoucherDate.setEnabled(false);
        txtVoucherNo.setEnabled(false);
        txtCase.setEnabled(false);
        txtOrganization.setEnabled(false);
        txtConsultant.setEnabled(false);
        txtGuardianName.setEnabled(false);
        txtAge.setEnabled(false);
        txtGender.setEnabled(false);
        txtAddress.setEnabled(false);
        txtPatientName.setEnabled(false);
        txtMobile.setEnabled(false);
        txtPaymentSource.setEnabled(false);
        txtServiceAmount.setEnabled(false);
        txtReceiptAmount.setEnabled(false);
    }

    private void onAdd() {
        clear();
        manageDisplay(true);
        txtVoucherDate.setText(DmShared.dNull(defaultValue.getCurrentDate()));
        txtHType.setFocus(true);
    }

    private void onTypeSelection() {
        clear();
        txtRegNo.setTag("");
        txtRegNo.setText("");
        txtUHIDNo.setTag2nd("");
        txtUHIDNo.setTag("");
        txtUHIDNo.setText("");
        txtVoucherNo.setText("");
        txtGuardianName.setText("");
        txtAddress.setText("");
        txtAge.setText("");
        txtGender.setText("");
        txtPatientName.setText("");
        txtMobile.setText("");
        txtCase.setText("");
        txtOrganization.setText("");
        txtConsultant.setText("");
        txtServiceAmount.setText("");
        txtPaymentSource.setTag("");
        txtPaymentSource.setText("");
        txtServiceAmount.setText("");
        txtReceiptAmount.setText("");
    }

    private void clear() {
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        txtRegNo.setTag2nd("");
        txtUHIDNo.setTag2nd("");
        txtRegNo.setTag("");
        txtRegNo.setText("");
        txtUHIDNo.setTag("");
        txtUHIDNo.setText("");
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnSaleBillSearch) {
            saleBSearch.show(this.txtHType.getTag());
        }
    }
}
