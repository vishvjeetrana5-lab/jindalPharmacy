package dataman.accounts.finalPaymentRequestEntry.client;

import dataman.accounts.finalPaymentRequestEntry.shared.DefaultValue_Mdl;
import dataman.accounts.finalPaymentRequestEntry.shared.FinalPaymentRequestEntry_Mdl;
import dataman.accounts.finalPaymentRequestEntry.shared.FinalPaymentRequestEntry_Srv;
import dataman.accounts.finalPaymentRequestEntry.shared.FinalPaymentRequestEntry_SrvAsync;
import dataman.accounts.finalPaymentRequestEntry.shared.FinalPaymentRequestEntry_Vld;
import dataman.accounts.finalPaymentRequestEntry.shared.Find_Mdl;
import dataman.accounts.finalPaymentRequestEntry.shared.TxtHSupplier_Mdl;
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
import dataman.dmToolkit.dmCheckBox.client.DmCheckBox;
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

public class FinalPaymentRequestEntry extends FinalPaymentRequestEntry_Dsg implements BlurHandler, ClickHandler, DmRootEvents, AttachEvent.Handler, KeyPressHandler {

    DefaultValue_Mdl defaultValueMdl = new DefaultValue_Mdl();
    ServiceInvoker<FinalPaymentRequestEntry_SrvAsync> SrvAsync;
    private final PBillSearch pBSearch = new PBillSearch();
    private LibraryVoucherCategory pvc = new LibraryVoucherCategory();

    public FinalPaymentRequestEntry(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(FinalPaymentRequestEntry_Srv.class), FinalPaymentRequestEntry.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        findEvents();
        manageEvents();
        textHelpEvents();
        iniDefaultValues(swi);
    }

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setRefreshRequired(false);

        topControl.setUserPermission(menuPermission, new VoucherPermissionHandler(DmClient.getVoucherPermission(new String[]{new LibraryVoucherCategory().FINAL_PAYMENT_REQUEST_ENTRY})) {

            @Override
            public String getVoucherType() {
                return defaultValueMdl.getVoucherTypeCode();

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

                topControlSave();

            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(FinalPaymentRequestEntry.this);
            }
        });

        topControl.setDisplay(true);
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
        FinalPaymentRequestEntry_Mdl mrTemp;
        try {
            mrTemp = topControlSaveInitialize();
            FinalPaymentRequestEntry_Vld.chkSave(mrTemp, DmClient.getLoginDate());
            SrvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtRemark).show();
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

    private FinalPaymentRequestEntry_Mdl topControlSaveInitialize() {
        FinalPaymentRequestEntry_Mdl mrTemp = new FinalPaymentRequestEntry_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }

        mrTemp.setpBillDocId(DmShared.xNull(txtPayBillNo.getTag()));
        mrTemp.setpBillRecId(DmShared.xNull(txtPayBillNo.getText()));

        mrTemp.setModuleTag(DmShared.xNull(txtHModuleType.getTag()).trim());
        mrTemp.setModuleText(DmShared.xNull(txtHModuleType.getText()).trim());

        mrTemp.setSupplierTag(DmShared.xNull(txtHSupplier.getTag()));
        mrTemp.setSupplierText(DmShared.xNull(txtHSupplier.getText()));

        mrTemp.setVoucherDate(DmShared.dNull(txtVoucherDate.getText()));

        mrTemp.setVoucherTypeTag(DmShared.xNull(txtVoucherType.getTag()));
        mrTemp.setVoucherTypeText(DmShared.xNull(txtVoucherType.getText()));

        mrTemp.setPaymentDate(DmShared.xNull(txtPaymnetDate.getText()));
        mrTemp.setRemark(DmShared.xNull(txtRemark.getText()));
        mrTemp.setFinalDuePayment(DmShared.xNull(txtFinalDuePay.getText()));
        mrTemp.setTotalNetBillEntry(DmShared.xNull(txtNetBillEntry.getText()));
        mrTemp.setTotalPOAmount(DmShared.xNull(txtTotalPoValue.getText()));
        mrTemp.setTotalBillAmount(DmShared.xNull(txtTotalBillAmt.getText()));
        mrTemp.setpBillAdvanceSetteled(DmShared.vNull(txtPBillAdvanceSetteled.getTag2nd()).toString());
        mrTemp.setTotalAdvNet(DmShared.vNull(txtPoAdvanceUnSetteled.getText()).toString());
        mrTemp.setTotalBillPaymnet(DmShared.vNull(txtTotalBillPayment.getText()).toString());
        mrTemp.getLstGrdAdvPaymentDetails().addAll(grdAdvPaymnetDetail.getGrid().getStore().getAll());
        
        mrTemp.setHeaderSupplierText(DmShared.xNull(txtHSupplier.getText()));
        mrTemp.setpBillSelectedSupplierText(txtSupplierName.getText());

//        if (chk.getValue()) {
//            mrTemp.setPartPay("1");
//        } else {
//            mrTemp.setPartPay("0");
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
                new DmAlertMessageBox(errorMessage, txtHSupplier).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtPayBillNo).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtFinalDuePay).show();
                break;
            case 600:
                new DmAlertMessageBox(errorMessage, txtPaymnetDate).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtPaymnetDate).show();
        }
    }

    private void topControlMoveRec(String strKey) {
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<FinalPaymentRequestEntry_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<FinalPaymentRequestEntry_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<FinalPaymentRequestEntry_Mdl> result) {
        FinalPaymentRequestEntry_Mdl data;

        if (result.isStatus()) {
            clear();
            data = result.getData();
            txtVoucherDate.setText(DmShared.xNull(data.getVoucherDate()));

            topControl.setDocId(DmShared.xNull(data.getSharedKeyFieldCode()));
            txtHModuleType.setTag(DmShared.xNull(data.getModuleTag()).trim());
            txtHModuleType.setText(DmShared.xNull(data.getModuleText()).trim());

            txtHSupplier.setTag(DmShared.xNull(data.getSupplierTag()));
            txtHSupplier.setText(DmShared.xNull(data.getSupplierText()));
            txtFinalDuePay.setText(DmShared.xNull(data.getFinalDuePayment()));
            txtPaymnetDate.setText(DmShared.xNull(data.getPaymentDate()));
            txtRemark.setText(DmShared.xNull(data.getRemark()));
            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());

            fillRecords(data);

        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
        flwPayBillSearch.clear();
        txtPayBillNo.setEnabled(false);
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
                    swi.setWidget(FinalPaymentRequestEntry.this);
                    manageDisplay();
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

                }
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
                                txtHSupplier.setFocus(true);
                            } else {
                                txtHModuleType.setFocus(true);

                            }
                        }
                    });
                }
            }
        });
        txtHSupplier.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHSupplier_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSupplier(txtVoucherType.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHSupplier_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSupplier).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHSupplier_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHSupplier.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSupplier).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHSupplier_Mdl tRow) {

                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        if (!DmShared.xNull(txtHSupplier.getTag()).trim().equalsIgnoreCase("")) {
                            txtPayBillNo.setFocus(true);
                        } else {
                            txtPayBillNo.setEnabled(false);
                            btnPayBillSearch.setFocus(true);
                        }
                    }
                });
            }
        });
        pBSearch.addCallBack(new PBillSearch.CallBack() {
            @Override
            public void onOk(String pbNo, String pbDocid, String pbSupplier) {
                getPurchaseBillDetail(pbNo, pbDocid, pbSupplier, null);
            }

            @Override
            public void onClose() {
                txtPayBillNo.setFocus(true);
            }
        });

        chk.addCallBack(new DmCheckBox.CallBack() {
            @Override
            public void valueChanged() {
                if (!DmShared.xNull(topControl.getMode()).equalsIgnoreCase("Browse")) {
                    if (DmShared.vNull(txtFinalDuePay.getText()).doubleValue() != 0) {
                        if (chk.getValue() == true) {
                            txtFinalDuePay.setEnabled(true);
                        } else {
                            txtFinalDuePay.setEnabled(false);
                        }
                    } else {
                        txtFinalDuePay.setEnabled(false);
                    }

                }

            }
        });
    }

    private void getPurchaseBillDetail(String poNo, String poDocId, String pbSupplier, final DmCallBack callback) {
        try {
            SrvAsync.invokeRPC().getPurchaseBillDetail(poNo, poDocId, DmShared.xNull(txtHModuleType.getTag()), DmShared.xNull(pbSupplier),
                    new AsyncCallback<DmResult<FinalPaymentRequestEntry_Mdl>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtPayBillNo).show();
                }

                @Override
                public void onSuccess(DmResult<FinalPaymentRequestEntry_Mdl> result) {
                    if (result.isStatus() && result.getData() != null) {
                        clear();
                        fillRecords(result.getData());
                        txtPayBillNo.setEnabled(false);
                        txtHModuleType.setEnabled(false);
                        txtHSupplier.setEnabled(false);
                        txtVoucherDate.setEnabled(false);

                        if (callback != null) {
                            DmResult<Object> dmrTemp = new DmResult<>();
                            dmrTemp.setStatus(true);
                            callback.result(dmrTemp);
                        }

                        txtFinalDuePay.setFocus(true);

                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), txtPayBillNo).show();
                    }
                }

            });
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtPayBillNo).show();
        }
    }

    private void fillRecords(FinalPaymentRequestEntry_Mdl data) {
        if (data != null) {
            if (!DmShared.xNull(data.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                txtPayReqNo.setTag(DmShared.xNull(data.getSharedKeyFieldCode()));
                txtPayReqNo.setText(DmShared.xNull(data.getSharedKeyFieldName()));
            }

            txtPayBillNo.setText(DmShared.xNull(data.getpBillRecId()));
            txtPayBillNo.setTag(DmShared.xNull(data.getpBillDocId()));

            txtPayBillDate.setText(DmShared.dNull(data.getpBillDate()));
            txtTotalBillPayment.setText(DmClient.vNull(data.getTotalBillPaymnet(), "0.00"));
            txtSupplierName.setTag(DmShared.xNull(data.getpBillSupplierTag()));
            txtSupplierName.setText(DmShared.xNull(data.getpBillSupplierText()));

            txtPoAdvanceSetteled.setText(DmClient.vNull(data.getPoAdvanceSetteled(), "0.00"));
            txtPBillAdvanceSetteled.setText(DmClient.vNull(data.getpBillAdvanceSetteled(), "0.00"));
            txtPBillAmt.setText(DmShared.xNull(data.getpBillAmount()));

            txtPayBillApprovedBy.setText(DmShared.xNull(data.getpBillApprovedBy()));
            txtPayBillApprovedDate.setText(DmShared.dNull(data.getpBillApprovedDt()));
            txtPayDueDate.setText(DmShared.dNull(data.getPayDueDate()));
            txtPayBillRemark.setText(DmShared.xNull(data.getpBillRemark()));

            txtTotalBillAmt.setText(DmClient.vNull(data.getpBillAmount(), "0.00"));
            txtPBillTDS.setText(DmClient.vNull(data.getpBillTDS(), "0.00"));
            txtNetBillEntry.setText(DmClient.vNull(data.getpBillNET(), "0.00"));

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
        double totalPoAmt = 0, totalGrnAmt = 0, totalBillAmt = 0, totalAdvBillPay = 0, totalNetBillAmt = 0, totalAdvNetPay = 0, totalAdv = 0, totalTds = 0,
                totalBillPaymnet = 0, poAdvanceUnsetteled = 0, finDuePay = 0, pBillPaymentDue = 0.0;
        for (int i = 0; i < grdPODetail.getGrid().getStore().size(); i++) {
            if (DmShared.iNull(grdPODetail.getGrid().getStore().get(i).getSharedKeyFieldCode()).longValue() != 0l) {
                totalPoAmt += DmShared.vNull(grdPODetail.getGrid().getStore().get(i).getValue()).doubleValue();
            }

        }
        txtTotalPoValue.setText(DmClient.vNull(totalPoAmt, "0.00"));

        for (int i = 0; i < grdMRNDetail.getGrid().getStore().size(); i++) {
            if (DmShared.iNull(grdMRNDetail.getGrid().getStore().get(i).getSharedKeyFieldCode()).longValue() != 0l) {
                totalGrnAmt += DmShared.vNull(grdMRNDetail.getGrid().getStore().get(i).getValue()).doubleValue();
            }
        }
        txtTotalGrn.setText(DmClient.vNull(totalGrnAmt, "0.00"));

        for (int i = 0; i < grdAdvPaymnetDetail.getGrid().getStore().size(); i++) {
            if (DmShared.iNull(grdAdvPaymnetDetail.getGrid().getStore().get(i).getSharedKeyFieldCode()).longValue() != 0l) {
                totalAdvNetPay += DmShared.vNull(grdAdvPaymnetDetail.getGrid().getStore().get(i).getNetPay()).doubleValue();
                totalAdv += DmShared.vNull(grdAdvPaymnetDetail.getGrid().getStore().get(i).getAmt()).doubleValue();
                totalTds += DmShared.vNull(grdAdvPaymnetDetail.getGrid().getStore().get(i).getTdsDeduction()).doubleValue();
            }
        }
        txtPoAdvanceNet.setText(DmClient.vNull(totalAdvNetPay, "0.00"));
        txtTotalPoAdvance.setText(DmClient.vNull(totalAdv, "0.00"));
        txtPOAdvanceTDS.setText(DmClient.vNull(totalTds, "0.00"));

        poAdvanceUnsetteled = totalAdv - DmShared.vNull(txtPoAdvanceSetteled.getText());
        txtPoAdvanceUnSetteled.setText(DmClient.vNull(poAdvanceUnsetteled, "0.00"));

        if (topControl.getMode().equalsIgnoreCase("New")) {
            if (DmShared.xNull(txtFinalDuePay.getText()).equalsIgnoreCase("")) {
                pBillPaymentDue = DmShared.vNull(DmShared.vNull(txtNetBillEntry.getText()).doubleValue()
                        - DmShared.vNull(txtTotalBillPayment.getText()).doubleValue()
                        - DmShared.vNull(txtPBillAdvanceSetteled.getText()).doubleValue()).doubleValue();

                finDuePay = pBillPaymentDue - poAdvanceUnsetteled;
                if (finDuePay <= 0.00) {
                    txtFinalDuePay.setText(DmClient.vNull(0, "0.00"));
                    txtFinalDuePay.setEnabled(false);

                } else {
                    txtFinalDuePay.setText(DmClient.vNull(finDuePay, "0.00"));
                }

                double pBillpreviousAdvanceSettle = DmShared.vNull(txtPBillAdvanceSetteled.getText());
                if (finDuePay >= 0.00) {
                    txtPBillAdvanceSetteled.setText(DmClient.vNull(poAdvanceUnsetteled + pBillpreviousAdvanceSettle, "0.00"));
                    // current payRequest Advance Settle...
                    txtPBillAdvanceSetteled.setTag2nd(DmClient.vNull(poAdvanceUnsetteled, "0.00"));

                    txtPoAdvanceUnSetteled.setText("0.00");
                } else {
                    txtPBillAdvanceSetteled.setText(DmClient.vNull(pBillpreviousAdvanceSettle
                            //                        + poAdvanceUnsetteled
                            + pBillPaymentDue, "0.00"));

                    // current payRequest Advance Settle...
                    txtPBillAdvanceSetteled.setTag2nd(DmClient.vNull(pBillPaymentDue, "0.00"));

                    poAdvanceUnsetteled = poAdvanceUnsetteled - DmShared.vNull(txtPBillAdvanceSetteled.getTag2nd());

                    txtPoAdvanceUnSetteled.setText(DmClient.vNull(poAdvanceUnsetteled, "0.00"));
                }
            }

        }

        //===============================================================================
    }

    private void onAdd() {
        clear();
        chk.setValue(false);
        manageDisplay();
        txtVoucherType.setTag(DmShared.xNull(defaultValueMdl.getVoucherTypeCode()));
        txtVoucherType.setText(DmShared.xNull(defaultValueMdl.getVoucherTypeName()));
        txtHModuleType.setTag(pvc.PURCHASE_ORDER);
        txtHModuleType.setText("Purchase");

        getServerDateTime();
    }

    private void getServerDateTime() {
        SrvAsync.invokeRPC().getServerDateTime(new AsyncCallback<String[]>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtPayBillNo).show();
            }

            @Override
            public void onSuccess(String[] result) {
                txtVoucherDate.setText(DmShared.dNull(result[0]));
                txtHModuleType.setFocus(true);

            }
        });
    }

    private void clear() {
        txtPayReqNo.setText("");
        txtPayReqNo.setTag("");
        txtPayBillNo.setText("");
        txtPayBillNo.setTag("");

        txtTotalPoValue.setText("");
        txtTotalGrn.setText("");
        txtTotalBillAmt.setText("");
        txtTotalBillPayment.setText("");

        txtPOAdvanceTDS.setText("");
        txtTotalPoAdvance.setText("");
        txtPoAdvanceNet.setText("");
        txtPaymnetDate.setText("");
        txtFinalDuePay.setText("");
        txtRemark.setText("");
        grdPODetail.getGrid().getStore().clear();
        grdAdvPaymnetDetail.getGrid().getStore().clear();
        grdBillDetail.getGrid().getStore().clear();
        grdFinalPaymnetDetail.getGrid().getStore().clear();
        grdMRNDetail.getGrid().getStore().clear();
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
    }

    private void manageDisplay() {

        txtPayReqNo.setEnabled(false);
        txtVoucherDate.setEnabled(false);
        txtPayBillDate.setEnabled(false);
        txtPBillAmt.setEnabled(false);
        txtPayBillApprovedBy.setEnabled(false);
        txtPayBillApprovedDate.setEnabled(false);
        txtPayBillRemark.setEnabled(false);
        txtSupplierName.setEnabled(false);

        txtTotalPoValue.setEnabled(false);
        txtTotalGrn.setEnabled(false);
        txtTotalBillAmt.setEnabled(false);
        txtNetBillEntry.setEnabled(false);
        txtPBillTDS.setEnabled(false);
        txtPOAdvanceTDS.setEnabled(false);
        txtTotalPoAdvance.setEnabled(false);
        txtPoAdvanceNet.setEnabled(false);
        txtTotalBillPayment.setEnabled(false);
        txtPayDueDate.setEnabled(false);
        txtPoAdvanceSetteled.setEnabled(false);
        txtPoAdvanceUnSetteled.setEnabled(false);
        txtPBillAdvanceSetteled.setEnabled(false);

        if (DmShared.xNull(topControl.getMode()).equalsIgnoreCase("Edit")) {
            txtHModuleType.setEnabled(false);
            txtHSupplier.setEnabled(false);
            txtFinalDuePay.setEnabled(false);
            chk.setValue(false);
//            if (chk.getValue()) {
//                if (DmShared.vNull(txtFinalDuePay.getText()).doubleValue() != 0) {
//                    txtFinalDuePay.setEnabled(true);
//                } else {
//                    txtFinalDuePay.setEnabled(false);
//                }
//            } else {
//                txtFinalDuePay.setEnabled(false);
//            }
        }

        if (DmShared.xNull(topControl.getMode()).equalsIgnoreCase("New")) {
            flwPayBillSearch.add(btnPayBillSearch);
            txtPayBillNo.setEnabled(true);
            txtHModuleType.setEnabled(true);
            txtHSupplier.setEnabled(true);
            txtPaymnetDate.setEnabled(true);
            txtFinalDuePay.setEnabled(false);
            chk.setValue(false);
//            if (chk.getValue()) {
//                txtFinalDuePay.setEnabled(true);
//            } else {
//                txtFinalDuePay.setEnabled(false);
//            }
        } else {
            flwPayBillSearch.clear();
            txtPayBillNo.setEnabled(false);
            txtHModuleType.setEnabled(false);
            txtVoucherDate.setEnabled(false);
            txtPaymnetDate.setEnabled(false);
            txtFinalDuePay.setEnabled(false);
            chk.setValue(false);
        }

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
        if (button == btnPayBillSearch) {
            if (!DmShared.xNull(txtHModuleType.getTag()).trim().equalsIgnoreCase("") && !DmShared.xNull(txtHSupplier.getTag()).equalsIgnoreCase("")) {
                pBSearch.show(DmShared.xNull(txtHSupplier.getTag()), DmShared.xNull(txtHModuleType.getTag()));
            } else {
                new DmAlertMessageBox("Please Select Module/Supplier", txtHModuleType).show();
            }
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getShiftKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_P) {
            btnClick(btnPayBillSearch);
            rootEvent.stopPropagation();
        }

    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void searchRecord(String key) {

    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtVoucherDate) {
            txtVoucherDate.setText(DmClient.returnDate(txtVoucherDate.getText()));
        }
        if (event.getSource() == txtPaymnetDate) {
            txtPaymnetDate.setText(DmClient.returnDate(txtPaymnetDate.getText()));
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        btnClick((Button) event.getSource());
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
        pBSearch.hide();
    }

    @Override
    public void onKeyPress(KeyPressEvent event) {
        if (event.getSource().equals(txtPayBillNo)) {
            if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
                if (!DmShared.xNull(txtPayBillNo.getText()).trim().equalsIgnoreCase("")) {
                    if (!DmShared.xNull(txtHModuleType.getTag()).trim().equalsIgnoreCase("") || !DmShared.xNull(txtHSupplier.getTag()).equalsIgnoreCase("")) {
                        getPurchaseBillDetail(DmShared.xNull(txtPayBillNo.getText()), "", DmShared.xNull(txtHSupplier.getTag()), null);
                    } else {
                        new DmAlertMessageBox("Please Select Module/Supplier", txtHSupplier).show();
                    }
                } else {
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            if ((DmShared.xNull(txtHModuleType.getTag()).equalsIgnoreCase("PO"))) {
                                btnPayBillSearch.setFocus(true);
                            }
                        }
                    });
                }
            }
        }
    }

    private void manageEvents() {
        txtPaymnetDate.addBlurHandler(this);
        btnPayBillSearch.addClickHandler(this);
        txtPayBillNo.addKeyPressHandler(this);
    }

    private void prnRecord(boolean printToPrinter) {
        HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
        hashMapFilter.put("docId", new FieldUDT_Mdl("Pay.Req. No.", DmShared.xNull(txtPayReqNo.getText()), DmShared.xNull(topControl.getDocId())));
        new OpenSourceReport(DmShared.xNull(defaultValueMdl.getVoucherTypeCode()), "", widgetName, widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
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
