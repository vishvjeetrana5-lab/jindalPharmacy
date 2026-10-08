
package aarogya.pharmacy.wholeSalePaymentRecipt.client;

import aarogya.pharmacy.wholeSalePaymentRecipt.shared.DefaultValue_Mdl;
import aarogya.pharmacy.wholeSalePaymentRecipt.shared.Find_Mdl;
import aarogya.pharmacy.wholeSalePaymentRecipt.shared.TxtHDepositAc_Mdl;
import aarogya.pharmacy.wholeSalePaymentRecipt.shared.WholeSalePaymentRecipt_Mdl;
import aarogya.pharmacy.wholeSalePaymentRecipt.shared.WholeSalePaymentRecipt_Srv;
import aarogya.pharmacy.wholeSalePaymentRecipt.shared.WholeSalePaymentRecipt_SrvAsync;
import aarogya.projectMenu.self.shared.pmPharmacy;
import aarogya.selfToolkit.remarkPopup.client.RemarkPopup;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.RecId;
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
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import dataman.dmToolkit.self.client.SetWidgetInvokerImpl;
import java.util.HashMap;
import java.util.List;


public final class WholeSalePaymentRecipt extends WholeSalePaymentRecipt_Dsg implements BlurHandler, AttachEvent.Handler, DmRootEvents {
    
    private final ServiceInvoker<WholeSalePaymentRecipt_SrvAsync> srvAsync;
    protected DefaultValue_Mdl defaultValue;
    final RemarkPopup remarkPopup = new RemarkPopup();
    private final String moduleName = new pmPharmacy().moduleName;
    
    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);

        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                onAdd();
                manageInstrumentDisplay(false);
                txtHDebtorsName.setFocus(true);
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                manageDisplay(false);
                if (DmShared.xNull(txtHDepositAC.getTag2nd()).trim().equalsIgnoreCase("Cash")) {
                    txtHInstrument.setTag("0");
                    txtHInstrument.setText("None");
                    txtHInstrument.setEnabled(false);
                } else {
                    txtHInstrument.setEnabled(true);
                }
                if (DmShared.xNull(txtHInstrument.getText()).trim().equalsIgnoreCase("None")) {
                    manageInstrumentDisplay(false);
                } else {
                    manageInstrumentDisplay(true);
                }
                txtHDepositAC.setFocus(true);
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
//                checkAmount();
                topControlSave();
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(WholeSalePaymentRecipt.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void prnRecord(boolean printToPrinter) {
        HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
        hashMapFilter.put("docId", new FieldUDT_Mdl("Receipt No.", DmShared.xNull(txtVoucherNo.getText()), DmShared.xNull(topControl.getDocId())));
        new OpenSourceReport(txtHVoucher.getTag(), "", widgetName, widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
            @Override
            public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                DmClient.localSettingPrintMap(reportProperty, moduleName, widgetName, "Print");
                return reportProperty;
            }
        }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
    }

    private void textHelpEvents() {
        remarkPopup.addCallBack(new RemarkPopup.CallBack() {
            @Override
            public void onOk(String textVal) {
                if (!DmShared.xNull(topControl.getMode()).trim().equalsIgnoreCase("Browse")) {
                    txtRemark.setText(DmShared.xNull(textVal).trim());
                    txtRemark.setFocus(true);
                }
            }
        });

        txtHVoucher.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHVoucher(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHVoucher).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHVoucher.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHVoucher).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus && t1Row != null) {

                }
            }
        });
        txtHDebtorsName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHDebtorsName(DmShared.xNull(txtHVoucher.getTag()), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDebtorsName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDebtorsName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDebtorsName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
//                setDefaultTDSAc();
//                if (blnStatus && t1Row != null) {
//                    txtHTDSAccount.setTag(DmShared.xNull(t1Row.getSharedModifiedBy()));
//                    txtHTDSAccount.setText(DmShared.xNull(t1Row.getSharedPreparedBy()));
//                } else {
//                    txtHTDSAccount.setTag("");
//                    txtHTDSAccount.setText("");
//                }
            }
        });
        txtHTDSAccount.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHTDSAccount(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHTDSAccount).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHTDSAccount.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHTDSAccount).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });
        txtHDepositAC.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHDepositAc_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHDepositIn(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHDepositAc_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDepositAC).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHDepositAc_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHDepositAC.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDepositAC).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHDepositAc_Mdl t1Row) {
                txtInstrumentDate.setText("");
                txtInstrumentNo.setText("");
                if (blnStatus && t1Row != null) {
                    if (DmShared.xNull(t1Row.getNature()).trim().equalsIgnoreCase("Cash")) {
                        txtHInstrument.setTag("0");
                        txtHInstrument.setText("None");
                        manageInstrumentDisplay(false);
                        txtHInstrument.setEnabled(false);
                    } else {
                        if (DmShared.iNull(txtHInstrument.getTag()).intValue() == 0) {
                            manageInstrumentDisplay(false);
                        } else {
                            manageInstrumentDisplay(true);
                        }
                        txtHInstrument.setEnabled(true);
                    }
                    txtHDepositAC.setTag2nd(DmShared.xNull(t1Row.getNature()));
                } else {
                    txtHDepositAC.setTag2nd("");
                }
            }
        });
        txtHInstrument.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHInstrument(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHInstrument).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHInstrument.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHInstrument).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                if (blnStatus && tRow != null) {
                    if (DmShared.xNull(tRow.getSharedKeyFieldName()).trim().equalsIgnoreCase("None")) {
                        txtInstrumentDate.setText("");
                        txtInstrumentNo.setText("");
                        manageInstrumentDisplay(false);
                    } else {
                        manageInstrumentDisplay(true);
                    }
                }
            }
        });
        txtHDisallowType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHDisallowType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDisallowType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDisallowType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDisallowType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
    }

    private void manageInstrumentDisplay(boolean isVisible) {
        txtInstrumentDate.setEnabled(isVisible);
        txtInstrumentNo.setEnabled(isVisible);
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

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtVoucherDate) {
            txtVoucherDate.setText(DmClient.returnDate(txtVoucherDate.getText()));
        } else if (event.getSource() == txtInstrumentDate) {
            txtInstrumentDate.setText(DmClient.returnDate(txtInstrumentDate.getText()));
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            remarkPopup.hide();
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getShiftKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_P) {
            rootEvent.stopPropagation();
        }
    }

    private void manageEvent() {
        txtVoucherDate.addBlurHandler(this);
        txtInstrumentDate.addBlurHandler(this);

        lblUnsettled.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (DmShared.xNull(txtVoucherNo.getText()).trim().equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Without Voucher No. you can not open  Bill Settlement.", txtHDebtorsName).show();
                } else {
                    showBillSettlement();
                }
            }
        });

        btnRemark.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                remarkPopup.show(DmShared.xNull(txtRemark.getText()).trim());
            }
        });
    }

    private void showBillSettlement() {
        if (!DmShared.xNull(txtVoucherNo.getText()).trim().equalsIgnoreCase("")) {
            DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(new pmPharmacy().moduleName, new pmPharmacy().WHOLE_SALE_BILL_SETTELEMEMT.caption, null, null, new SetWidgetInvokerImpl() {
                @Override
                public void setWidget(final IsWidget wgtObject) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                    ((IsWidgetExtended) wgtObject).searchRecord(topControl.getDocId());
                }
            });
        }
    }

    public WholeSalePaymentRecipt(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
//        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        srvAsync = new ServiceInvoker<WholeSalePaymentRecipt_SrvAsync>(GWT.create(WholeSalePaymentRecipt_Srv.class), WholeSalePaymentRecipt.class.getName());
        this.asWidget().setTitle(widgetName);
        flwForm.addAttachHandler(this);
        topControlEvents(menuPermission);
        manageEvent();
        textHelpEvents();
        findEvents();
        iniDefaultValues(swi);
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
                    swi.setWidget(WholeSalePaymentRecipt.this);

                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void setDefaultTDSAc() {
        srvAsync.invokeRPC().getDefaultTDSAc(new AsyncCallback<String[]>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(String[] result) {
                txtHTDSAccount.setText("");
                txtHTDSAccount.setTag("");
                if (DmShared.iNull(result[0]).intValue() != 0) {
                    txtHTDSAccount.setTag(result[0]);
                    txtHTDSAccount.setText(result[1]);
//                    txtHTDSAccount.setEnabled(false);

                } else {
//                    txtHTDSAccount.setEnabled(true);
                }

            }
        });
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

    private WholeSalePaymentRecipt_Mdl topControlSaveInitialize() {
        WholeSalePaymentRecipt_Mdl mrTemp = new WholeSalePaymentRecipt_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
            mrTemp.setObjRecId(new RecId());
            mrTemp.getObjRecId().setRecIdValue(DmShared.xNull(txtVoucherNo.getText()));
            mrTemp.getObjRecId().setPrefix(DmShared.xNull(txtVoucherNo.getTag()));
            mrTemp.getObjRecId().setCounter(DmShared.iNull(txtVoucherNo.getTag2nd()).longValue());
        }

        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtVoucherNo.getText()));
        mrTemp.setVoucherDate(DmShared.dNull(txtVoucherDate.getText()));
        mrTemp.setVoucherTypeCode(DmShared.xNull(txtHVoucher.getTag()));
        mrTemp.setPartyCode(DmShared.xNull(txtHDebtorsName.getTag()));
        mrTemp.setPartyName(DmShared.xNull(txtHDebtorsName.getText()));
        mrTemp.setTdsAcCode(DmShared.xNull(txtHTDSAccount.getTag()));
        mrTemp.setTdsAcName(DmShared.xNull(txtHTDSAccount.getText()));
        mrTemp.setDepositInCode(DmShared.xNull(txtHDepositAC.getTag()));
        mrTemp.setDepositInName(DmShared.xNull(txtHDepositAC.getText()));
        mrTemp.setBankCashType(DmShared.xNull(txtHDepositAC.getTag2nd()));
        mrTemp.setInstrumentCode(DmShared.xNull(txtHInstrument.getTag()));
        mrTemp.setInstrumentName(DmShared.xNull(txtHInstrument.getText()));
        mrTemp.setInstrumentNo(DmShared.xNull(txtInstrumentNo.getText()));
        mrTemp.setInstrumentDate(DmShared.dNull(txtInstrumentDate.getText()));
        mrTemp.setRemark(DmShared.xNull(txtRemark.getText()));
        mrTemp.setDisallowTypeCode(DmShared.xNull(txtHDisallowType.getTag()));
        mrTemp.setDisallowTypeName(DmShared.xNull(txtHDisallowType.getText()));
        mrTemp.setReceivedAmt(DmClient.vNull(txtReceivedAmt.getText(), "0.00"));

        return mrTemp;
    }

    private void topControlSave() {
        WholeSalePaymentRecipt_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
//            WholeSalePaymentRecipt_Mdl.chkSave(mrTemp);

            srvAsync.invokeRPC().saveRecord(mrTemp, DmClient.getVoucherPrefix(), new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHDebtorsName).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        topControl.setDocId(result.getData());

                        if (topControl.getMode().equalsIgnoreCase("New")) {
//                            topControl.btnClick("New");
                            new DmConfirmMessageBox("Are you sure? You want to print this record.", new DialogHideEvent.DialogHideHandler() {
                                @Override
                                public void onDialogHide(DialogHideEvent event) {
                                    if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                                        prnRecord(false);
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
                new DmAlertMessageBox(errorMessage, txtHVoucher).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtHDebtorsName).show();
                break;
            case 250:
                new DmAlertMessageBox(errorMessage, txtHTDSAccount).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtHDepositAC).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtVoucherDate).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtHInstrument).show();
                break;
            case 600:
                new DmAlertMessageBox(errorMessage, txtInstrumentNo).show();
                break;
            case 700:
                new DmAlertMessageBox(errorMessage, txtInstrumentDate).show();
                break;
            case 800:
                new DmAlertMessageBox(errorMessage, txtReceivedAmt).show();
                break;
            case 900:
                new DmAlertMessageBox(errorMessage, txtHDisallowType).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHDepositAC).show();
                break;
        }
    }

    private void topControlMoveRec(String strKey) {
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<WholeSalePaymentRecipt_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<WholeSalePaymentRecipt_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<WholeSalePaymentRecipt_Mdl> result) {
        clear();
        if (result.isStatus()) {
            WholeSalePaymentRecipt_Mdl mrTemp = result.getData();

            topControl.setDocId(mrTemp.getSharedKeyFieldCode());
            txtVoucherNo.setText(mrTemp.getSharedKeyFieldName());
            txtVoucherNo.setTag(mrTemp.getObjRecId().getPrefix());
            txtVoucherNo.setTag2nd(DmShared.iNull(mrTemp.getObjRecId().getCounter()).toString());
            txtVoucherDate.setText(mrTemp.getVoucherDate());
            txtHVoucher.setTag(mrTemp.getVoucherTypeCode());
            txtHVoucher.setText(mrTemp.getVoucherTypeName());
            txtHDebtorsName.setTag(mrTemp.getPartyCode());
            txtHDebtorsName.setText(mrTemp.getPartyName());
            txtHTDSAccount.setTag(mrTemp.getTdsAcCode());
            txtHTDSAccount.setText(mrTemp.getTdsAcName());
            txtHDepositAC.setTag(mrTemp.getDepositInCode());
            txtHDepositAC.setText(mrTemp.getDepositInName());
            txtHDepositAC.setTag2nd(mrTemp.getBankCashType());
            txtHInstrument.setText(mrTemp.getInstrumentName());
            txtHInstrument.setTag(mrTemp.getInstrumentCode());
            txtInstrumentNo.setText(mrTemp.getInstrumentNo());
            txtInstrumentDate.setText(mrTemp.getInstrumentDate());
            txtRemark.setText(mrTemp.getRemark());
            txtHDisallowType.setTag(DmShared.xNull(mrTemp.getDisallowTypeCode()));
            txtHDisallowType.setText(DmShared.xNull(mrTemp.getDisallowTypeName()));
            txtReceivedAmt.setText(DmClient.vNull(mrTemp.getReceivedAmt(), "0.00"));
            txtTDSAmt.setText(DmClient.vNull(mrTemp.getTdsAmt(), "0.00"));
            txtUnsettledAmt.setText(DmClient.vNull(mrTemp.getUnsettledAmt(), "0.00"));
            txtsettledAmt.setText(DmClient.vNull(mrTemp.getSettledAmt(), "0.00"));
            userControl.setTextBlog(preparedByBlog, mrTemp.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, mrTemp.getSharedModifiedBy());
            setNetAmount();
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }

        topControl.setDisplay(true);
    }

    private void setNetAmount() {
        txtNetAmt.setText(DmClient.vNull(DmShared.vNull(txtReceivedAmt.getText()).doubleValue() + DmShared.vNull(txtTDSAmt.getText()).doubleValue(), "0.00"));
    }

    private void topControlDelete(String strKey) {
        srvAsync.invokeRPC().deleteRecord(strKey, new AsyncCallback<DmResult<String>>() {

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

    private void manageDisplay(boolean flag) {
        txtVoucherNo.setEnabled(false);
        txtHVoucher.setEnabled(flag);
        txtTDSAmt.setEnabled(false);
        txtNetAmt.setEnabled(false);
        txtUnsettledAmt.setEnabled(false);
        txtsettledAmt.setEnabled(false);
    }

    private void onAdd() {
        clear();
        manageDisplay(true);
        txtVoucherDate.setText(DmShared.dNull(defaultValue.getCurrentDate()));
        txtHVoucher.setTag(DmShared.xNull(defaultValue.getVoucherTypeCode()));
        txtHVoucher.setText(DmShared.xNull(defaultValue.getVoucherTypeName()));
        txtHDisallowType.setTag(DmShared.iNull(defaultValue.getDisallowType()).toString());
        txtHDisallowType.setText(DmShared.iNull(defaultValue.getDisallowType()).intValue() == 1 ? "Disallow (Reg. wise)" : "Disallow (Service wise)");
        txtHInstrument.setTag("0");
        txtHInstrument.setText("None");
//        setDefaultTDSAc();
    }

    private void clear() {
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
    }
    
}
