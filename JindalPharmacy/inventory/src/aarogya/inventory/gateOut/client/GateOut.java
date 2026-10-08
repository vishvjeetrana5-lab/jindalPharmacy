package aarogya.inventory.gateOut.client;

import aarogya.inventory.gateInSearch.client.GateInSearch;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
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
import java.util.HashMap;
import java.util.List;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.modifyReason.client.ModifyReason;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import aarogya.inventory.gateOut.shared.*;

public class GateOut extends GateOut_Dsg implements BlurHandler, ClickHandler, DmRootEvents, AttachEvent.Handler {

    private final ServiceInvoker<GateOut_SrvAsync> srvAsync;

    String voucherCategory;
    String selectedSupplierCode;
    String partyType, vNo = "", recIdPrefix = "";
    private Boolean dateTimeEditable = false;
    private DmCallBack cardCallBack = null;
    String cardActionNature = "";
    ModifyReason_Mdl modifyReasonMdl;
    final ModifyReason modifyReason = new ModifyReason();
    private boolean gateOutFlag = false, isGateOutPermission = false;
    private Default_Mdl defaultValue = new Default_Mdl();
    private final GateGridEdit<GateOut_Mdl, Grd_PrpAcs> gateGridEdit = new GateGridEdit(grdGateDetail);
    private final GateInSearch gatepassInSearch = new GateInSearch();

    public GateOut(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        this.asWidget().setTitle(widgetName);
        srvAsync = new ServiceInvoker<GateOut_SrvAsync>(GWT.create(GateOut_Srv.class), GateOut.class.getName());
        textHelpEvents();
        topControlEvents(menuPermission);
        findEvents();
        gridEvents();
        manageEvents();
        iniDefaultValues(swi);
    }

    private void onAdd() {
        clear();
        manageDisplay(true);
        setDefaultValues();
        txtHType.setFocus(true);
        grdGateDetail.getGrid().getStore().add(new GateOutGridEdit_Mdl());
    }

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());

        topControl.setOtherRequired(false);

        topControl.setRefreshRequired(false);
        topControl.setDeleteCaptionToDiscard();

        for (String controlPermission : menuPermission.getControlPermission()) {
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(dateTimeEditableBlog)) {
                dateTimeEditable = true;
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(gateOutByBlog)) {
                isGateOutPermission = true;
            }
        }

        topControl.setUserPermission(menuPermission, new VoucherPermissionHandler(DmClient.getVoucherPermission(new String[]{new ProjectVoucherCategory().GATE})) {

            @Override
            public String getVoucherType() {
                return txtHVoucherType.getTag();
            }

            @Override
            public String getVoucherDate() {
                return txtPassOutDate.getText();
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
                srvAsync.invokeRPC().ifDocumentUsed(topControl.getDocId(), new AsyncCallback<DmResult<Boolean>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public void onSuccess(DmResult<Boolean> result) {
                        if (result.getData()) {
                            topControl.btnClick("");
                            new DmAlertMessageBox("Entry has been used, can't edit/delete.", (DmTextBox) null).show();
                        } else {
                            manageDisplay(true);
                            grdGateDetail.getGrid().getStore().add(new GateOutGridEdit_Mdl());
                            txtHEmployeeName.setFocus(true);
                        }
                    }
                });

            }
        });

        topControl.addDeleteHandeller(new DmTopControl.eDelete() {
            @Override
            public void deleteClicked() {
                srvAsync.invokeRPC().ifDocumentUsed(topControl.getDocId(), new AsyncCallback<DmResult<Boolean>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public void onSuccess(DmResult<Boolean> result) {
                        if (result.getData()) {
                            topControl.btnClick("");
                            new DmAlertMessageBox("Entry has been used, can't edit/delete.", (DmTextBox) null).show();
                        } else {
                            if (DmShared.xNull(lblDiscard.getText()).equalsIgnoreCase("Discarded")) {
                                new DmAlertMessageBox("Record is already discarded.", (DmTextBox) null).show();
                                return;
                            }
                            new DmConfirmMessageBox("Are you sure? You want to discard this record.", new DialogHideEvent.DialogHideHandler() {

                                @Override
                                public void onDialogHide(DialogHideEvent event) {
                                    if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                                        //                                        topControlDelete(topControl.getDocId());
                                        showModifyReason();
                                    }
                                }
                            }).show();
                        }
                    }
                });
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
                if (DmShared.xNull(lblDiscard.getText()).equalsIgnoreCase("Discarded")) {
                    new DmAlertMessageBox("Record is already discarded.", (DmTextBox) null).show();
                    return;
                }
                if (topControl.getMode().equalsIgnoreCase("New")) {
                    topControlSave();
                } else {
                    showModifyReason();
                }
            }
        });
        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(GateOut.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void showModifyReason() {
        modifyReason.show(topControl.getMode(), topControl.getDocId());
    }

    private void resetGateIn(boolean isVisible) {

        txtEstdDate.setText("");
        grdGateDetail.getGrid().getStore().clear();
        grdGateDetail.getGrid().getStore().add(new GateOutGridEdit_Mdl());
    }

    private void fillItemDetails(List<KeyFieldExtended> listKF) {
        srvAsync.invokeRPC().getItemDetail("", listKF, new AsyncCallback<DmResult<List<GateOutGridEdit_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GateOutGridEdit_Mdl>> result) {
                if (result.isStatus()) {
                    if (result.isStatus()) {
                        grdGateDetail.getGrid().getStore().clear();
                        grdGateDetail.getGrid().getStore().addAll(result.getData());
                        grdGateDetail.getGrid().getStore().add(new GateOutGridEdit_Mdl());

                        manageItemDetails(false);

                        txtHEmployeeName.setFocus(true);

                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            }
        });
    }

    private void manageItemDetails(boolean flag) {
        txtHPurpose.setEnabled(flag);
        txtHMode.setEnabled(flag);
        txtHDepartment.setEnabled(flag);
        txtEstdDate.setEnabled(flag);
        txtHDestination.setEnabled(flag);
        txtHBill.setEnabled(false);

        txtHPurpose.setMandatory(flag);
        txtHMode.setMandatory(flag);
        txtHDepartment.setMandatory(flag);

    }

    private void textHelpEvents() {
        txtHDestination.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getHRemark(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDestination).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDestination.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDestination).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        gatepassInSearch.addCallBack(new GateInSearch.CallBack() {
            @Override
            public void ok(List<KeyFieldExtended> listKF) {
                resetGateIn(true);
                fillItemDetails(listKF);
            }

            @Override
            public void close() {

            }
        });

        txtHMode.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHMode(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMode).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHMode.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMode).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });

        txtHPurpose.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHPurpose(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPurpose).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPurpose.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPurpose).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });

        txtHVoucherType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHVoucher_mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHVoucher(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHVoucher_mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHVoucherType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHVoucher_mdl>> result) {
                        if (result.isStatus()) {
                            txtHVoucherType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHVoucherType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHVoucher_mdl t1Row) {
                if (!DmShared.xNull(txtHVoucherType.getTag()).equalsIgnoreCase(DmShared.xNull(txtHVoucherType.getTag2nd()))) {
                    txtHVoucherType.setTag2nd(txtHVoucherType.getTag());
                    if (topControl.getMode().equalsIgnoreCase("New")) {

                    }
                    if (blnStatus && t1Row != null) {
                        voucherCategory = t1Row.getVoucherCategory();

                    }

                }
            }
        });

        txtHEmployeeName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHPartyName_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHEmployeeName(voucherCategory, txtHVoucherType.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHPartyName_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHEmployeeName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHPartyName_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHEmployeeName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHEmployeeName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHPartyName_Mdl tRow) {
                if (!DmShared.xNull(selectedSupplierCode).equalsIgnoreCase(DmShared.xNull(txtHEmployeeName.getTag()))) {
                    selectedSupplierCode = DmShared.xNull(txtHEmployeeName.getTag());
                }
                if (blnStatus && tRow != null) {
                    txtHEmployeeName.setTag2nd(DmShared.xNull(tRow.getStateCode()));
                    partyType = DmShared.xNull(tRow.getPartyType());

                }
            }
        });

        txtHDepartment.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHDepartment(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDepartment).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDepartment.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDepartment).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {

                if (DmShared.xNull(STEnum.GateType.PREVIOUS_GATE_IN_RETURNABLE.getValue()).equalsIgnoreCase(DmShared.xNull(txtHType.getTag())) //                        &&(DmShared.xNull(txtHVoucherType.getTag()).equalsIgnoreCase(DmShared.xNull(txtHType.getTag())) )
                        ) {
                    txtHType.setTag2nd(DmShared.xNull(tRow.getSharedKeyFieldName()));
                }

            }
        });

        txtHType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus && t1Row != null) {
                    if (DmShared.iNull(STEnum.GateType.PREVIOUS_GATE_IN_RETURNABLE.getId()).toString().equalsIgnoreCase(DmShared.xNull(txtHType.getTag()))) {
                        gatepassInSearch.show("", DmShared.dNull(txtPassOutDate.getText()), new ProjectVoucherCategory().GATE_PASS_IN);
                        manageItemDetails(false);
                    } else {
                        grdGateDetail.getGrid().getStore().clear();
                        grdGateDetail.getGrid().getStore().add(new GateOutGridEdit_Mdl());
                        manageItemDetails(true);
                    }
                }
            }

        });

        txtHAgainstBillType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHBillType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAgainstBillType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHAgainstBillType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHAgainstBillType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (t1Row == null) {
                    txtHBill.setTag("");
                    txtHBill.setText("");
                    txtHBill.setEnabled(false);
                    txtHAgainstBillType.setFocus(true);
                }
                if (blnStatus && t1Row != null) {
                    txtHBill.setEnabled(true);

                }
            }

        });

        txtHBill.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHBill(DmShared.xNull(txtHAgainstBillType.getTag()), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHBill).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHBill.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHBill).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (t1Row == null) {
                    txtHAgainstBillType.setTag("");
                    txtHAgainstBillType.setText("");
                    txtHAgainstBillType.setFocus(true);
                }
            }
        });

        modifyReason.addCallBack(new ModifyReason.CallBack() {

            @Override
            public void onOk(ModifyReason_Mdl modifyReason) {
                modifyReasonMdl = modifyReason;
                if (topControl.getMode().equalsIgnoreCase("Browse") && DmShared.iNull(topControl.getDocId()).longValue() > 0) {
                    topControlDelete(topControl.getDocId());
                } else {
                    topControlSave();
                }
            }
        });

//        modifyReason.addCallBack(new ModifyReason.CallBack() {
//
//            @Override
//            public void onOk(ModifyReason_Mdl modifyReason) {
//                modifyReasonMdl = modifyReason;
//                topControlSave();
//            }
//        });
    }

    private void resetBill() {
        txtHAgainstBillType.setTag("");
        txtHAgainstBillType.setText("");

        txtHBill.setFocus(true);
    }

    private void manageEvents() {
        txtPassOutDate.addBlurHandler(this);
        txtOutDate.addBlurHandler(this);
        txtEstdDate.addBlurHandler(this);
        txtSlipNo.addBlurHandler(this);
        txtTareWeight.addBlurHandler(this);
        txtGrossWeight.addBlurHandler(this);
        txtNetWeight.addBlurHandler(this);
        btnClockGateEntry.addClickHandler(this);
        btnGateInDateTime.addClickHandler(this);

    }

    private void topControlSave() {
        GateOut_Mdl mrTemp;

        try {

            mrTemp = topControlSaveInitialize();
            GateOut_Vld.chkSave(mrTemp);

            srvAsync.invokeRPC().saveRecord(mrTemp, DmClient.getVoucherPrefix(), new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtEntryNo).show();
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
                        topControlSaveError(result.getErrorNumber(), result.getValue1(), result.getValue2(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getErrorSource(), e.getErrorSourceCode(), e.getMessage());
        }
    }

    private GateOut_Mdl topControlSaveInitialize() {
        GateOut_Mdl mrTemp = new GateOut_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());

            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
            mrTemp.getRecId().setRecIdValue(DmShared.xNull(txtEntryNo.getText()));
            mrTemp.getRecId().setCounter(DmShared.iNull(vNo).longValue());
            mrTemp.getRecId().setPrefix(DmShared.xNull(recIdPrefix));

        }

        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtEntryNo.getText()).trim());
        mrTemp.setU_EntDt_LatestLine(DmShared.xNull(txtEntryNo.getTag()));
        mrTemp.setVoucherTypeCode(DmShared.xNull(txtHVoucherType.getTag()));
        mrTemp.setDate(DmShared.dNull(txtPassOutDate.getText()));
        mrTemp.setTime(DmShared.xNull(txtPassOutTime.getText()));
        mrTemp.setPartyCode(DmShared.xNull(txtHEmployeeName.getTag()));
        mrTemp.setPartyName(DmShared.xNull(txtHEmployeeName.getText()));
        mrTemp.setDepartmentCode(DmShared.xNull(txtHDepartment.getText()));
        mrTemp.setPassTypeCode(DmShared.xNull(txtHType.getText()));
        mrTemp.setRemark(DmShared.xNull(txtRemark.getText()));
        mrTemp.setMode(DmShared.xNull(txtHMode.getText()));
        mrTemp.setPurposeText(DmShared.xNull(txtHPurpose.getText()));
        mrTemp.setPassTypeCode(DmShared.xNull(txtHType.getTag()));
        mrTemp.setDepartmentCode(DmShared.xNull(txtHDepartment.getTag()));
        mrTemp.setDepartmentName(DmShared.xNull(txtHDepartment.getText()));
        mrTemp.setDestination(DmShared.xNull(txtHDestination.getText()));
        mrTemp.setEstdDate(DmShared.dNull(txtEstdDate.getText()));
        mrTemp.setReturnBillDocId(DmShared.xNull(txtHBill.getTag()));
        mrTemp.setReturnBillTypeCode(DmShared.xNull(txtHAgainstBillType.getTag()));

        mrTemp.getGateDetails().clear();
        mrTemp.getGateDetails().addAll(grdGateDetail.getGrid().getStore().getAll());
        if (modifyReasonMdl != null) {
            mrTemp.setModifyReasonMdl(modifyReasonMdl);
        }
        return mrTemp;
    }

    private void topControlDelete(String strKey) {
        GateOut_Mdl mrTemp = new GateOut_Mdl();
        mrTemp.getGateDetails().addAll(grdGateDetail.getGrid().getStore().getAll());
        mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        srvAsync.invokeRPC().deleteRecord(strKey, mrTemp, modifyReasonMdl, new AsyncCallback<DmResult<String>>() {

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

    private void clear() {
        vNo = "";
        recIdPrefix = "";
        lblDiscard.setText("");
        grdGateDetail.getGrid().getStore().clear();
        modifyReasonMdl = new ModifyReason_Mdl();
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
//        if (topControl.getMode().equalsIgnoreCase("New")) {
//            topControl.setDocId("");
//        }
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

    private void topControlSaveError(int errorNo, String errorSource, String errorSourceCode, String errorMessage) {
        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtHType).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtHPurpose).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtHMode).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtHDepartment).show();
                break;
            case 600:
                new DmAlertMessageBox(errorMessage, txtPassOutDate).show();
                break;
            case 700:
                new DmAlertMessageBox(errorMessage, txtPassOutTime).show();
                break;
            case 800:
                new DmAlertMessageBox(errorMessage, txtHAgainstBillType).show();
                break;

            default:
                new DmAlertMessageBox(errorMessage, txtHDestination).show();
                break;
        }
    }

    private void topControlMoveRec(String strKey) {
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<GateOut_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                gateOutFlag = false;
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<GateOut_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<GateOut_Mdl> result) {
        GateOut_Mdl data;
        clear();

        if (result.isStatus()) {
            data = result.getData();
            topControl.setDocId(DmShared.xNull(data.getSharedKeyFieldCode()));
            txtEntryNo.setText(DmShared.xNull(data.getSharedKeyFieldName()));
            txtEntryNo.setTag(DmShared.xNull(data.getU_EntDt_LatestLine()));
            txtPassOutDate.setText(DmShared.xNull(data.getDate()));
            txtPassOutTime.setText(DmShared.xNull(data.getTime()));
            txtHVoucherType.setTag(DmShared.xNull(data.getVoucherTypeCode()));
            txtHVoucherType.setText(DmShared.xNull(data.getVoucherTypeName()));
            txtHEmployeeName.setText(DmShared.xNull(data.getPartyName()));
            txtHEmployeeName.setTag(DmShared.xNull(data.getPartyCode()));
            txtHType.setText(DmShared.xNull(data.getPassTypeName()));
            txtHType.setTag(DmShared.xNull(data.getPassTypeCode()));
            txtHDepartment.setTag(DmShared.xNull(data.getDepartmentCode()));
            txtHDepartment.setText(DmShared.xNull(data.getDepartmentName()));
            txtHDestination.setText(DmShared.xNull(data.getDestination()));
            txtHMode.setTag(DmShared.xNull(data.getMode()));
            txtHMode.setText(DmShared.xNull(data.getMode()));
            txtHPurpose.setTag(DmShared.xNull(data.getPurposeText()));
            txtHPurpose.setText(DmShared.xNull(data.getPurposeText()));
            txtEstdDate.setText(DmShared.xNull(data.getEstdDate()));
            txtRemark.setText(DmShared.xNull(data.getRemark()));
            txtGateOutDateView.setText(DmShared.dNull(data.getGateOutDate()));
            txtGateOutTimeView.setText(DmShared.xNull(data.getGateOutTime()));
            vNo = DmShared.iNull(data.getRecId().getCounter()).toString();
            recIdPrefix = DmShared.xNull(data.getRecId().getPrefix());
            txtHMode.setText(DmShared.xNull(data.getMode()));
            txtHBill.setTag(DmShared.xNull(data.getReturnBillDocId()));
            txtHBill.setText(DmShared.xNull(data.getReturnBillName()));
            txtHAgainstBillType.setText(DmShared.xNull(data.getReturnBillTypeName()));
            grdGateDetail.getGrid().getStore().clear();
            grdGateDetail.getGrid().getStore().addAll(data.getGateDetails());
            if (DmShared.iNull(data.getIsCancel()).intValue() == 1) {
                lblDiscard.setText("Discarded");
            } else {
                lblDiscard.setText("");
            }

            userControl.setTextBlog(preparedByBlog, DmShared.xNull(data.getSharedPreparedBy()));
            userControl.setTextBlog(modifiedByBlog, DmShared.xNull(data.getSharedModifiedBy()));

            if (cardCallBack != null) {
                cardAction();
            }

        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
        manageDisplay(false);
    }

    private void manageDisplay(boolean flag) {

        txtHBill.setEnabled(false);
        txtNetWeight.setEnabled(false);
        txtEntryNo.setEnabled(false);
        txtPassOutDate.setEnabled(false);
        txtPassOutTime.setEnabled(false);
        txtSlipNo.setEnabled(false);
        txtOutDate.setEnabled(false);
        txtOutTime.setEnabled(false);

        flwInDateTimeContainer.clear();
        if (flag) {
            if (!dateTimeEditable) {

            }

            if (topControl.getMode().equalsIgnoreCase("Edit")) {
                txtHVoucherType.setEnabled(false);
                if (dateTimeEditable) {
                    flwInDateTimeContainer.add(flwInDateTime);
                }
            } else {
                flwInDateTimeContainer.add(flwInDateTime);
            }
        } else { //New or Edit Mode

        }
        if (DmShared.xNull(topControl.getMode()).trim().equalsIgnoreCase("Edit")
                && DmShared.iNull(STEnum.GateType.PREVIOUS_GATE_IN_RETURNABLE.getId()).toString().equalsIgnoreCase(DmShared.xNull(txtHType.getTag()))) {
            manageItemDetails(false);
        }

    }

    private void iniDefaultValues(final SetWidgetInvoker swi) {
        srvAsync.invokeRPC().getDefaultValues(new AsyncCallback<DmResult<Default_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtPassOutDate).show();
            }

            @Override
            public void onSuccess(DmResult<Default_Mdl> result) {
                if (result.isStatus()) {
                    defaultValue = result.getData();
                    manageDisplay(false);

                    swi.setWidget(GateOut.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtPassOutDate).show();
                }
            }
        });
    }

    private void setDefaultValues() {

        txtHVoucherType.setTag(defaultValue.getvTypeCode());
        txtHVoucherType.setText(defaultValue.getvTypeName());
        getDefaultDateTime(gateOutFlag);
        txtHType.setFocus(true);
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

    private void gridEvents() {
        gateGridEdit.addCallBack(new GateGridEdit.CallBack() {

            public void onOk(GateOutGridEdit_Mdl opdService) {
                grdGateDetail.commitChanges();

            }

            public void onDelete() {
                grdGateDetail.commitChanges();

            }

        });

        grdGateDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showGrnEntryGridEdit(event.getRowIndex());
            }
        });

        grdGateDetail.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showGrnEntryGridEdit(grdGateDetail.getGrid().getStore().indexOf(grdGateDetail.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
    }

    private void showGrnEntryGridEdit(int currentRowIndex) {
        if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {
            gateGridEdit.show(currentRowIndex, false, false, false, this);
        } else {
            gateGridEdit.show(currentRowIndex, true, true, true, this);
        }
    }

    private void getDefaultDateTime(final Boolean dateTimeFlag) {
        srvAsync.invokeRPC().getDefaultDateTime(new AsyncCallback<DmResult<Default_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<Default_Mdl> result) {

                if (result.isStatus()) {
                    if (dateTimeFlag) {
                        txtOutDate.setText(result.getData().getDate());
                        txtOutTime.setText(result.getData().getTime());
                    } else {
                        txtPassOutDate.setText(result.getData().getDate());
                        txtPassOutTime.setText(result.getData().getTime());
                    }
                }
            }
        });

    }

    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtEstdDate) {
            txtEstdDate.setText(DmClient.returnDate(txtEstdDate.getText()));
        } else if (event.getSource() == txtOutDate) {
            txtOutDate.setText(DmClient.returnDate(txtOutDate.getText()));
        } else if (event.getSource() == txtSlipNo) {

            getSlipDetails(txtSlipNo.getText());
        } else if (event.getSource() == txtGrossWeight) {
            calculate();

        } else if (event.getSource() == txtTareWeight) {
            calculate();
        }

    }

    private void getSlipDetails(String slipNo) {

        srvAsync.invokeRPC().getSlipNoDetail(slipNo, new AsyncCallback<DmResult<GateOut_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<GateOut_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnClockGateEntry) {
            if (!topControl.getDocId().equals("")) {
                getDefaultDateTime(true);
            }
        } else if (event.getSource() == btnGateInDateTime) {
            if (topControl.getDocId().equals("") && topControl.getMode().equalsIgnoreCase("New")) {
                getDefaultDateTime(false);
            }
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            gateGridEdit.hide();
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getShiftKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_P) {
            rootEvent.stopPropagation();
        }
    }

    public void calculate() {

        txtGrossWeight.setText(DmShared.vNull(txtGrossWeight.getText()).toString());
        txtTareWeight.setText(DmShared.vNull(txtTareWeight.getText()).toString());
        txtNetWeight.setText(DmShared.vNull(DmShared.vNull(txtGrossWeight.getText()) - DmShared.vNull(txtTareWeight.getText())).toString());
    }

    @Override
    public void action(String nature, Object object, DmCallBack callback) {     //TBC

    }

    private void cardAction() {
        if (cardCallBack != null) {
            if (cardActionNature.equalsIgnoreCase("GATE_OUT")) {
                getDefaultDateTime(true);
            }
            DmResult<Object> dmrTemp = new DmResult<>();
            dmrTemp.setStatus(true);
            cardCallBack.result(dmrTemp);
        }
        cardCallBack = null;
        cardActionNature = "";
    }

    private void prnRecord(boolean printToPrinter) {
        HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
        hashMapFilter.put("docId", new FieldUDT_Mdl("Gate Entry", DmShared.xNull(txtEntryNo.getText()), DmShared.xNull(topControl.getDocId())));
        new OpenSourceReport(txtHVoucherType.getTag(), "", widgetName, widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
            @Override
            public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                DmClient.localSettingPrintMap(reportProperty, moduleName, widgetName, "Print");
                return reportProperty;
            }
        }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
    }

}
