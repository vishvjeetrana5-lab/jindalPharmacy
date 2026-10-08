package aarogya.pharmacy.medicineIssue.client;

import aarogya.pharmacy.medicineIssue.shared.*;
import aarogya.projectMenu.self.shared.pmPharmacy;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.modifyReason.client.ModifyReason;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.stStatic.client.STClient;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
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
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import java.util.List;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import java.util.HashMap;

public final class MedicineIssue extends MedicineIssue_Dsg implements BlurHandler, AttachEvent.Handler, DmRootEvents, ClickHandler {

    private ServiceInvoker<MedicineIssue_SrvAsync> srvAsync;
    protected DefaultValue_Mdl defaultValue;
    final MedicineIssueGridEdit<GrdMedicineDetail_Mdl, GridMedicineDetail_PrpAcs> medicineIssueGridEdit = new MedicineIssueGridEdit(gridMedicineDetail);
    private String deptCode = "";
    MedicineIssue_Mdl previousModel = null;
    protected boolean departmentEditable = false, dateEditable = false, isAllowAddBatch = false;
    final ModifyReason modifyReason = new ModifyReason();
    private ModifyReason_Mdl modifyReasonMdl;
    private String moduleName = new pmPharmacy().moduleName;
    protected String expiryAlertBeforeDays = "";
    boolean isExpiryItems = false;

    private void topControlEvents(UserMenu menuPermission) {

        topControl.setTitle(this.asWidget().getTitle());
        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);
        for (String controlPermission : menuPermission.getControlPermission()) {
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(departmentEditableBlog)) {
                departmentEditable = true;
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(addBatchBlog)) {
                isAllowAddBatch = true;
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(dateEditableBlog)) {
                dateEditable = true;
            }

        }

        topControl.setUserPermission(menuPermission, new VoucherPermissionHandler(DmClient.getVoucherPermission(new String[]{new ProjectVoucherCategory().MEDICINE_ISSUE})) {
            @Override
            public String getVoucherType() {
                return txtHVoucherType.getTag();
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
                txtHDepartment.setFocus(true);
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                manageDisplay(true);
                gridMedicineDetail.getGrid().getStore().add(new GrdMedicineDetail_Mdl());
                txtHDepartment.setFocus(true);
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
                new DmConfirmMessageBox("Do you want to send print to printer?", new DialogHideEvent.DialogHideHandler() {
                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            prnRecord(true);
                        } else {
                            prnRecord(false);
                        }
                    }
                }).show();
            }
        });

        topControl.addSaveHandeller(new DmTopControl.eSave() {
            @Override
            public void saveClicked() {
                save();
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(MedicineIssue.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void textHelpEvents() {
        txtHVoucherType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHVoucher(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus && t1Row != null) {

                }
            }
        });

        txtHIssueTo.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHIssueTo_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHIssueTo(txtHVoucherType.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHIssueTo_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIssueTo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHIssueTo_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHIssueTo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIssueTo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHIssueTo_Mdl t1Row) {
                if (blnStatus) {
                }
            }
        });

        txtHDepartment.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                deptCode = DmShared.xNull(txtHDepartment.getTag());
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
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus) {
                    if (DmShared.iNull(deptCode).longValue() != DmShared.iNull(txtHDepartment.getTag()).longValue()) {
                        gridMedicineDetail.getGrid().getStore().clear();
                        gridMedicineDetail.getGrid().getStore().add(new GrdMedicineDetail_Mdl());
                    }
                }
            }
        });

        txtHRequisition.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHRequisitionNo_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                if (isExpiryItems) {
                    new DmAlertMessageBox("Expired Medicine can't be issued against requisition.", txtHRequisition).show();
                    return;
                }
                if (DmShared.iNull(txtHDepartment.getTag()).longValue() == 0) {
                    new DmAlertMessageBox("Please Select Department.", txtHRequisition).show();
                    return;
                }
                if (DmShared.dNull(txtVoucherDate.getText()).equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Please fill Date.", txtVoucherDate).show();
                    return;
                }
                srvAsync.invokeRPC(blnCallBackWait).getTxtHRequisitionNo(txtHDepartment.getTag(), txtVoucherDate.getText(), topControl.getMode().equalsIgnoreCase("New") ? "" : topControl.getDocId(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHRequisitionNo_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHRequisition).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHRequisitionNo_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHRequisition.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHRequisition).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHRequisitionNo_Mdl t1Row) {
                gridMedicineDetail.getGrid().getStore().clear();
                gridMedicineDetail.getGrid().getStore().add(new GrdMedicineDetail_Mdl());

                if (blnStatus && t1Row != null) {
                    if (DmShared.iNull(txtHDepartment.getTag()).longValue() == 0) {
                        txtHDepartment.setTag(DmShared.xNull(t1Row.getDepartmentToCode()));
                        txtHDepartment.setText(DmShared.xNull(t1Row.getDepartmentToName()));

                    }
                    txtHDepartment.setTag2nd(DmShared.xNull(t1Row.getDepartmentCode()));
                    txtHIssueTo.setText(DmShared.xNull(t1Row.getRequisitionBy()));
                    txtHIssueTo.setTag(DmShared.xNull(t1Row.getRequisitionByCode()));
                    fillItemDetails(t1Row.getSharedKeyFieldCode(), null);
                }
            }
        });

    }

    private void fillItemDetails(String reqDocid, final DmCallBack callback) {
        srvAsync.invokeRPC().getMedicineDetail(reqDocid, topControl.getMode().equalsIgnoreCase("New") ? "" : topControl.getDocId(), txtHDepartment.getTag(), txtVoucherDate.getText(), new AsyncCallback<DmResult<List<GrdMedicineDetail_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdMedicineDetail_Mdl>> result) {
                if (result.isStatus()) {
                    if (result.isStatus()) {
                        fillGridAuto(result.getData(), null);
                        if (callback != null) {
                            txtHIssueTo.setTag(DmShared.xNull(result.getValue1()));
                            txtHIssueTo.setText(DmShared.xNull(result.getValue2()));
                            DmResult<Object> dmrTemp = new DmResult<>();
                            dmrTemp.setStatus(true);
                            callback.result(dmrTemp);
                            txtRefNo.setFocus(true);
                        }
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            }
        }
        );
    }

    private void calculate() {
        double totalPurchaseValue = 0d;
        for (int i = 0; i < gridMedicineDetail.getGrid().getStore().size(); i++) {
            totalPurchaseValue += DmShared.vNull(gridMedicineDetail.getGrid().getStore().get(i).getPurchaseValue());
        }
        lblTotalPurchseValue.setText(DmClient.vNull(totalPurchaseValue, "0.00"));
    }

    private void gridEvents() {
        medicineIssueGridEdit.addCallBack(new MedicineIssueGridEdit.CallBack() {

            @Override
            public void onOk(GrdMedicineDetail_Mdl opdService) {
                gridMedicineDetail.commitChanges();
                calculate();
            }

            @Override
            public void onDelete() {
                gridMedicineDetail.commitChanges();
                calculate();
            }
        });

        gridMedicineDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showMedicineIssueGridEdit(event.getRowIndex());
            }
        });

        gridMedicineDetail.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showMedicineIssueGridEdit(gridMedicineDetail.getGrid().getStore().indexOf(gridMedicineDetail.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
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

    private void showMedicineIssueGridEdit(int currentRowIndex) {
        if (!DmShared.xNull(txtHDepartment.getTag()).equalsIgnoreCase("")) {
            if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {
                medicineIssueGridEdit.show(currentRowIndex, false, false, false, this);
            } else {
                medicineIssueGridEdit.show(currentRowIndex, true, true, true, this);
            }
        } else {
            new DmAlertMessageBox("Please select department.", txtHDepartment).show();
        }
    }

    private void prnRecord(boolean printToPrinter) {
        HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
        hashMapFilter.put("docId", new FieldUDT_Mdl("Issue No.", DmShared.xNull(txtVoucherNo.getText()), DmShared.xNull(topControl.getDocId())));
        new OpenSourceReport(txtHVoucherType.getTag(), "", widgetName, widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
            @Override
            public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                DmClient.localSettingPrintMap(reportProperty, moduleName, widgetName, "Print");
                return reportProperty;
            }
        }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtVoucherDate) {
            txtVoucherDate.setText(DmClient.returnDate(txtVoucherDate.getText()));
            if (STClient.isFutureDate(DmShared.dNull(txtVoucherDate.getText()))) {
                txtVoucherDate.setText("");
                new DmAlertMessageBox("Issue Date cant be greater than current date.", txtVoucherDate).show();
            }
        } else if (event.getSource() == txtRefDate) {
            txtRefDate.setText(DmClient.returnDate(txtRefDate.getText()));
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            medicineIssueGridEdit.hide();
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
        txtRefDate.addBlurHandler(this);
        chkExpiryItem.addClickHandler(this);
    }

    public MedicineIssue(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        srvAsync = new ServiceInvoker<MedicineIssue_SrvAsync>(GWT.create(MedicineIssue_Srv.class), MedicineIssue.class.getName());
        this.asWidget().setTitle(widgetName);
        flwForm.addAttachHandler(this);
        topControlEvents(menuPermission);
        manageEvent();
        textHelpEvents();
        findEvents();
        gridEvents();
        manageDisplay(false);
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
                    expiryAlertBeforeDays = result.getData().getExpiryAlertBeforeDays();
                    txtVoucherDate.setText("");
                    swi.setWidget(MedicineIssue.this);
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

    private MedicineIssue_Mdl topControlSaveInitialize() {
        MedicineIssue_Mdl mrTemp = new MedicineIssue_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
            mrTemp.getRecIdMedicineIssue().setCounter(DmShared.iNull(txtVoucherNo.getTag()).longValue());
            mrTemp.getRecIdMedicineIssue().setPrefix(DmShared.xNull(txtVoucherNo.getTag2nd()));
            mrTemp.getRecIdMedicineIssue().setRecIdValue(DmShared.xNull(txtVoucherNo.getText()));
        }
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtVoucherNo.getText()));
        mrTemp.setVoucherDate(DmShared.dNull(txtVoucherDate.getText()));
        mrTemp.setVoucherTypeCode(DmShared.xNull(txtHVoucherType.getTag()));
        mrTemp.setIssueToTag(DmShared.xNull(txtHIssueTo.getTag()));
        mrTemp.setDepartmentTag(DmShared.xNull(txtHDepartment.getTag()));
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtVoucherNo.getText()));
        if (isExpiryItems) {
            mrTemp.setIsExpiryMedicine("1");
        } else {
            mrTemp.setIsExpiryMedicine("0");
        }

        mrTemp.setRefDate(DmShared.dNull(txtRefDate.getText()));
        mrTemp.setRefNo(DmShared.xNull(txtRefNo.getText()));
        mrTemp.setRemarkHeader(DmShared.xNull(txtRemark.getText()));

        mrTemp.setVoucherTypeCode(DmShared.xNull(txtHVoucherType.getTag()));
        mrTemp.setLoginDate(DmShared.xNull(DmClient.getLoginDate()));

        mrTemp.getListMedicineDetail().clear();
        mrTemp.getListMedicineDetail().addAll(gridMedicineDetail.getGrid().getStore().getAll());
        mrTemp.setInventoryAccountingActive(defaultValue.isInventoryAccountingActive());

        // Set default Department Name
        defaultValue.setDepartmentTag(DmShared.xNull(txtHDepartment.getTag()));
        defaultValue.setDepartmentText(DmShared.xNull(txtHDepartment.getText()));

        if (modifyReasonMdl != null) {
            mrTemp.setModifyReasonMdl(modifyReasonMdl);
        }

        return mrTemp;
    }

    public void save() {
        if (topControl.getMode().equalsIgnoreCase("New")) {
            topControlSave();
        } else {
            showModifyReason();
        }

    }

    private void showModifyReason() {
        modifyReason.show(topControl.getMode(), topControl.getDocId());

        modifyReason.addCallBack(new ModifyReason.CallBack() {

            @Override
            public void onOk(ModifyReason_Mdl modifyReason) {
                try {
                    MedicineIssue.this.modifyReasonMdl = modifyReason;
                    if (topControl.getMode().equalsIgnoreCase("Browse") && DmShared.iNull(topControl.getDocId()).longValue() > 0) {
                        topControlDelete(topControl.getDocId());
                    } else {
                        topControlSave();
                    }
                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void topControlSave() {
        MedicineIssue_Mdl mrTemp;
        try {
            mrTemp = topControlSaveInitialize();
            MedicineIssue_Vld.chkSave(mrTemp);
            srvAsync.invokeRPC().saveRecord(mrTemp, previousModel, DmClient.getVoucherPrefix(), new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHVoucherType).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        topControl.setDocId(result.getData());
                        if (topControl.getMode().equalsIgnoreCase("New")) {
                            new DmConfirmMessageBox("Are you sure? You want to print this record.", new DialogHideEvent.DialogHideHandler() {
                                @Override
                                public void onDialogHide(DialogHideEvent event) {
                                    if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                                        prnRecord(true);
                                    }
                                    topControl.btnClick("New");
                                }
                            }).show();
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

    private void topControlSaveError(int errorNo, String errorSource, String errorSourceCode, String errorMessage) {
        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtHVoucherType).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtVoucherDate).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtHDepartment).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtRefDate).show();
                break;
            case 500:
                int indexTemp = DmShared.iNull(errorSourceCode).intValue();
                if (!DmShared.xNull(errorSource).equalsIgnoreCase("GridRow")) {
                    List<GrdMedicineDetail_Mdl> listTemp = gridMedicineDetail.getGrid().getStore().getAll();
                    for (GrdMedicineDetail_Mdl node : listTemp) {
                        if (DmShared.xNull(node.getRequisitionDocId()).equalsIgnoreCase(errorSource)
                                && DmShared.xNull(node.getRequisitionV_SNo()).equalsIgnoreCase(errorSourceCode)) {
                            indexTemp = DmShared.iNull(listTemp.indexOf(node)).intValue();
                            if (errorMessage != null && !errorMessage.isEmpty()) {
                                if (Character.isDigit(errorMessage.charAt(errorMessage.length() - 1))) {
                                    errorMessage += " " + DmShared.xNull(node.getSkuName());
                                }
                                errorMessage += " in line no. " + (indexTemp + 1);
                            }
                            break;
                        }
                    }
                }
                final int gridRowIndex = indexTemp;
                new DmAlertMessageBox(errorMessage, new HideEvent.HideHandler() {
                    @Override
                    public void onHide(HideEvent event) {
                        ((CellSelectionModel) gridMedicineDetail.getGrid().getSelectionModel()).selectCell(gridRowIndex, 1);
                    }
                }).show();
                break;
            case 600:
                new DmAlertMessageBox(errorMessage, (DmTextHelp) txtHRequisition).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHIssueTo).show();
                break;
        }
    }

    private void topControlMoveRec(String strKey) {
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<MedicineIssue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<MedicineIssue_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<MedicineIssue_Mdl> result) {
        clear();
        if (result.isStatus()) {
            previousModel = new MedicineIssue_Mdl();
            result.getData().copy(previousModel);

            MedicineIssue_Mdl mrTemp = result.getData();
            topControl.setDocId(mrTemp.getSharedKeyFieldCode());
            txtVoucherNo.setText(mrTemp.getSharedKeyFieldName());
            txtVoucherNo.setTag(DmShared.iNull(mrTemp.getRecIdMedicineIssue().getCounter()).toString());
            txtVoucherNo.setTag2nd(DmShared.xNull(mrTemp.getRecIdMedicineIssue().getPrefix()));
            txtVoucherDate.setText(mrTemp.getVoucherDate());

            txtRemark.setText(DmShared.xNull(mrTemp.getRemarkHeader()));
            txtRefNo.setText(DmShared.xNull(mrTemp.getRefNo()));
            txtRefDate.setText(DmShared.dNull(mrTemp.getRefDate()));
            if (DmShared.iNull(mrTemp.getIsExpiryMedicine()).intValue() == 1) {
                isExpiryItems = true;
                chkExpiryItem.setValue(true);
                expiryLabelDesign(true);
            } else {
                isExpiryItems = false;
                chkExpiryItem.setValue(false);
                expiryLabelDesign(false);
            }

            txtHVoucherType.setTag(mrTemp.getVoucherTypeCode());
            txtHVoucherType.setText(mrTemp.getVoucherTypeName());
            txtHIssueTo.setTag(mrTemp.getIssueToTag());
            txtHIssueTo.setText(mrTemp.getIssueToText());
            txtHDepartment.setTag(mrTemp.getDepartmentTag());
            txtHDepartment.setText(mrTemp.getDepartmentText());
            txtHDepartment.setTag2nd(mrTemp.getDepartmentToTag());
            txtHRequisition.setTag(DmShared.xNull(mrTemp.getRequisitionDocId()));
            txtHRequisition.setText(DmShared.xNull(mrTemp.getRequisitionNo()));

            userControl.setTextBlog(preparedByBlog, mrTemp.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, mrTemp.getSharedModifiedBy());

            gridMedicineDetail.getGrid().getStore().clear();
            gridMedicineDetail.getGrid().getStore().addAll(mrTemp.getListMedicineDetail());
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        manageDisplay(false);
        topControl.setDisplay(true);
        calculate();
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
        txtVoucherDate.setEnabled(flag);
        if (flag) {
            if (dateEditable) {
                txtVoucherDate.setEnabled(flag);
            } else {
                txtVoucherDate.setEnabled(false);
            }
        }

        if (!departmentEditable) {
            txtHDepartment.setEnabled(false);
        }

        if (topControl.getMode().equals("Edit")) {
            txtHVoucherType.setEnabled(false);
            txtHDepartment.setEnabled(false);
        }
    }

    private void expiryLabelDesign(boolean flag) {
        if (flag) {
            lblExpiryMedicine.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        } else {
            lblExpiryMedicine.getElement().getStyle().setFontWeight(Style.FontWeight.NORMAL);
        }
    }

    private void onAdd() {
        manageDisplay(true);
        clear();
        txtVoucherDate.setText(DmShared.dNull(defaultValue.getCurrentDate()));
        txtHVoucherType.setTag(defaultValue.getVoucherTypeCode());
        txtHVoucherType.setText(defaultValue.getVoucherTypeName());
        txtHDepartment.setTag(defaultValue.getDepartmentTag());
        txtHDepartment.setText(defaultValue.getDepartmentText());
        gridMedicineDetail.getGrid().getStore().add(new GrdMedicineDetail_Mdl());
    }

    private void clear() {
        gridMedicineDetail.getGrid().getStore().clear();
        isExpiryItems = false;
        chkExpiryItem.setValue(false);
        expiryLabelDesign(false);
        txtVoucherDate.setText("");
        txtVoucherDate.setTag("");
        txtVoucherDate.setTag2nd("");
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        lblTotalPurchseValue.setText("-");
    }

    private void fillGridAuto(List<GrdMedicineDetail_Mdl> grdItemDetailList, DmCallBack callback) {
        gridMedicineDetail.getGrid().getStore().clear();
        if (grdItemDetailList != null) {
            gridMedicineDetail.getGrid().getStore().addAll(grdItemDetailList);
            gridMedicineDetail.getGrid().getStore().add(new GrdMedicineDetail_Mdl());
            ((CellSelectionModel) gridMedicineDetail.getGrid().getSelectionModel()).selectCell(0, 0);
            if (callback != null) {
                DmResult<Object> dmrTemp = new DmResult<>();
                dmrTemp.setStatus(true);
                callback.result(dmrTemp);
            }
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(chkExpiryItem)) {
            if (DmShared.xNull(txtHRequisition.getTag()).trim().equalsIgnoreCase("")) {
                gridMedicineDetail.getGrid().getStore().clear();
                gridMedicineDetail.getGrid().getStore().add(new GrdMedicineDetail_Mdl());
            }
            isExpiryItems = false;
            expiryLabelDesign(false);
            if (chkExpiryItem.getValue()) {
                if (!DmShared.xNull(txtHRequisition.getTag()).trim().equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Expired Medicine can't be issued against requisition.", (DmTextBox) null).show();
                    chkExpiryItem.setValue(false);
                } else {
                    expiryLabelDesign(true);
                    isExpiryItems = true;
                }
            }
        }
    }

    @Override
    public void action(String nature, Object object, DmCallBack callback) {
        nature = DmShared.xNull(nature).trim().toUpperCase();
        try {
            switch (nature) {
                case "ADD_REQUISITION":
                    openWithRequisition((HashMap) object, callback);
                    break;
                case "FOCUS_ON_ADD":
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            if (DmShared.iNull(txtHDepartment.getTag()).longValue() > 0) {
                                txtHVoucherType.setFocus(true);
                            } else {
                                txtHDepartment.setFocus(true);
                            }
                        }
                    });
                    break;
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    public void openWithRequisition(final HashMap paramHashMap, final DmCallBack callback) {
        srvAsync.invokeRPC().getServerDateTime(new AsyncCallback<String[]>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHVoucherType).show();
            }

            @Override
            public void onSuccess(String[] result) {
                topControl.btnClick("New");
                txtVoucherDate.setText(result[0]);
                txtVoucherDate.setTag2nd(result[0]);
                txtHRequisition.setTag((String) paramHashMap.get("requisitiondocID"));
                txtHRequisition.setText((String) paramHashMap.get("requisitionNo"));
                txtHDepartment.setText((String) paramHashMap.get("fromDeptName"));
                txtHDepartment.setTag((String) paramHashMap.get("fromDeptCode"));

                fillItemDetails(txtHRequisition.getTag(), callback);
            }

        });
    }
}
