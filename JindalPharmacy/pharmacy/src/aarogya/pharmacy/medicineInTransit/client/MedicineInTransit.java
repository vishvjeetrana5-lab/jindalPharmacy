package aarogya.pharmacy.medicineInTransit.client;

import aarogya.pharmacy.medicineInTransit.shared.*;
import aarogya.projectMenu.self.shared.pmPharmacy;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.modifyReason.client.ModifyReason;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.stStatic.client.STClient;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
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
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
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
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import java.util.HashMap;
import java.util.List;

public final class MedicineInTransit extends MedicineInTransit_Dsg implements BlurHandler, AttachEvent.Handler, DmRootEvents {

    private ServiceInvoker<MedicineInTransit_SrvAsync> srvAsync;
    protected DefaultValue_Mdl defaultValue;
    final MedicineInTransitGridEdit<GrdMedicineDetail_Mdl, GridMedicineDetail_PrpAcs> medicineInTransitGridEdit = new MedicineInTransitGridEdit(gridMedicineDetail);
    private String deptCode = "";
    private final String vCate = new ProjectVoucherCategory().MEDICINE_IN_TRANSIT;
    protected boolean departmentEditable = false, dateEditable = false, isAllowAddBatch = false;
    final ModifyReason modifyReason = new ModifyReason();
    private ModifyReason_Mdl modifyReasonMdl;
    private MedicineInTransit_Mdl previousMdl;
    private String moduleName = new pmPharmacy().moduleName;

    private void topControlEvents(UserMenu menuPermission) {

        topControl.setTitle(this.asWidget().getTitle());
        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);

        for (String controlPermission : menuPermission.getControlPermission()) {
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(departmentEditableBlog)) {
                departmentEditable = true;
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(dateEditableBlog)) {
                dateEditable = true;
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(addBatchBlog)) {
                isAllowAddBatch = true;
            }
        }

        topControl.setUserPermission(menuPermission, new VoucherPermissionHandler(DmClient.getVoucherPermission(new String[]{vCate})) {
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
                if (!DmShared.xNull(txtHDepartmentTo.getText()).equalsIgnoreCase("")) {
                    txtHIssueNo.setFocus(true);
                } else {
                    txtHDepartmentTo.setFocus(true);
                }
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                manageDisplay(true);
                gridMedicineDetail.getGrid().getStore().add(new GrdMedicineDetail_Mdl());
                txtHIssueNo.setFocus(true);
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
                save();
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(MedicineInTransit.this);
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

        txtHTransferTo.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHTransferTo_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHTransferTo(txtHVoucherType.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHTransferTo_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHTransferTo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHTransferTo_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHTransferTo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHTransferTo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHTransferTo_Mdl t1Row) {
                if (blnStatus) {
                }
            }
        });

        txtHIssueNo.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHTransferFromDept_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                if (DmShared.iNull(txtHDepartmentTo.getTag()).longValue() == 0) {
                    new DmAlertMessageBox("Please Select Department To.", txtHIssueNo).show();
                    return;
                }

                srvAsync.invokeRPC(blnCallBackWait).getTxtHTransferFromDept(txtHDepartmentTo.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHTransferFromDept_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHTransferTo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHTransferFromDept_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHIssueNo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIssueNo).show();
                        }
                    }

                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHTransferFromDept_Mdl t1Row) {
                if (blnStatus && t1Row != null) {

                    if (DmShared.iNull(txtHDepartmentTo.getTag()).longValue() != 0) {
                        txtHIssueNo.setText(DmShared.xNull(t1Row.getSharedKeyFieldName()));
                        txtHIssueNo.setTag2nd(DmShared.xNull(t1Row.getFromSiteCode()));
                        txtHTransferTo.setTag(DmShared.xNull(t1Row.getSharedKeyFieldCode()));
                        txtDepartmentFrom.setText(DmShared.xNull(t1Row.getDeptName()));
                        txtDepartmentFrom.setTag(DmShared.xNull(t1Row.getDeptCode()));
                        txtHTransferTo.setText(DmShared.xNull(t1Row.getTransferToName()));
                        txtHTransferTo.setTag(DmShared.xNull(t1Row.getTransferToCode()));
                    }

                    rcdMedicineDeail(t1Row.getSharedKeyFieldCode());
                } else {
                    gridMedicineDetail.getGrid().getStore().clear();
                    gridMedicineDetail.getGrid().getStore().add(new GrdMedicineDetail_Mdl());
                }
            }
        });

        txtHDepartmentTo.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                deptCode = DmShared.xNull(txtHDepartmentTo.getTag());
                srvAsync.invokeRPC(blnCallBackWait).getTxtHDepartment(helper, blnToEndOfFile, txtHIssueNo.getTag(), false, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDepartmentTo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDepartmentTo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDepartmentTo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (DmShared.iNull(deptCode).longValue() != DmShared.iNull(txtHDepartmentTo.getTag()).longValue()) {
                    gridMedicineDetail.getGrid().getStore().clear();
                    gridMedicineDetail.getGrid().getStore().add(new GrdMedicineDetail_Mdl());
                }
                if (blnStatus && t1Row != null) {
                    txtHDepartmentTo.setTag2nd(DmShared.xNull(t1Row.getSharedModifiedBy()));
                    deptCode = t1Row.getSharedKeyFieldCode();
                }
            }
        });
    }

    private void gridEvents() {
        medicineInTransitGridEdit.addCallBack(new MedicineInTransitGridEdit.CallBack() {

            @Override
            public void onOk(GrdMedicineDetail_Mdl opdService) {
                gridMedicineDetail.commitChanges();
            }

            @Override
            public void onDelete() {
                gridMedicineDetail.commitChanges();
            }
        });

        gridMedicineDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showMedicineInTransitGridEdit(event.getRowIndex());
            }
        });

        gridMedicineDetail.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showMedicineInTransitGridEdit(gridMedicineDetail.getGrid().getStore().indexOf(gridMedicineDetail.getGrid().getSelectionModel().getSelectedItem()));
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

    private void showMedicineInTransitGridEdit(int currentRowIndex) {

        if (DmShared.xNull(txtHIssueNo.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please select Department From.", txtHIssueNo).show();
            return;
        }

        if (DmShared.xNull(txtHDepartmentTo.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please select Department To.", txtHDepartmentTo).show();
            return;
        }

        if (!DmShared.xNull(txtHIssueNo.getTag()).equalsIgnoreCase("") && !DmShared.xNull(txtHDepartmentTo.getTag()).equalsIgnoreCase("")) {

            if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {
                medicineInTransitGridEdit.show(currentRowIndex, false, false, false, this);
            } else {
                medicineInTransitGridEdit.show(currentRowIndex, true, true, true, this);
            }
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtVoucherDate) {
            txtVoucherDate.setText(DmClient.returnDate(txtVoucherDate.getText()));
            if (STClient.isFutureDate(DmShared.dNull(txtVoucherDate.getText()))) {
                txtVoucherDate.setText("");
                new DmAlertMessageBox("Voucher Date should be less than or equal to To Current Date.", txtVoucherDate).show();
                return;
            }
        }
        if (event.getSource() == txtRefDate) {
            txtRefDate.setText(DmClient.returnDate(txtRefDate.getText()));
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            medicineInTransitGridEdit.hide();
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
    }

    public MedicineInTransit(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        srvAsync = new ServiceInvoker<MedicineInTransit_SrvAsync>(GWT.create(MedicineInTransit_Srv.class), MedicineInTransit.class.getName());
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
                    txtVoucherDate.setText("");
                    txtVoucherDate.setEnabled(false);
                    swi.setWidget(MedicineInTransit.this);
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

    private MedicineInTransit_Mdl topControlSaveInitialize() {
        MedicineInTransit_Mdl mrTemp = new MedicineInTransit_Mdl();
        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
            mrTemp.getRecIdTransfer().setCounter(DmShared.iNull(txtVoucherNo.getTag()).longValue());
            mrTemp.getRecIdTransfer().setPrefix(DmShared.xNull(txtVoucherNo.getTag2nd()));
            mrTemp.getRecIdTransfer().setRecIdValue(DmShared.xNull(txtVoucherNo.getText()));
        }
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtVoucherNo.getText()));
        mrTemp.setVoucherDate(DmShared.dNull(txtVoucherDate.getText()));
        mrTemp.setVoucherTypeCode(DmShared.xNull(txtHVoucherType.getTag()));
        mrTemp.setRefDate(DmShared.dNull(txtRefDate.getText()));
        mrTemp.setRefNo(DmShared.xNull(txtRefNo.getText()));
        mrTemp.setDepartmentFromTag(DmShared.xNull(txtDepartmentFrom.getTag()));
        mrTemp.setDepartmentFromText(DmShared.xNull(txtDepartmentFrom.getText()));
        mrTemp.setinTransitDocID(DmShared.xNull(txtHIssueNo.getTag()));
        mrTemp.setDepartmentFromSite(DmShared.xNull(txtHIssueNo.getTag2nd()));
        mrTemp.setDepartmentToTag(DmShared.xNull(txtHDepartmentTo.getTag()));
        mrTemp.setDepartmentToText(DmShared.xNull(txtHDepartmentTo.getText()));
        mrTemp.setDepartmentToSite(DmShared.xNull(txtHDepartmentTo.getTag2nd()));
        mrTemp.setReceivedByTag(DmShared.xNull(txtHTransferTo.getTag()));
        mrTemp.setReceivedByText(DmShared.xNull(txtHTransferTo.getText()));
        mrTemp.getListItemDetail().clear();
        mrTemp.getListItemDetail().addAll(gridMedicineDetail.getGrid().getStore().getAll());

        // Set default Department Name
        defaultValue.setDepartmentTag(DmShared.xNull(txtHDepartmentTo.getTag()));
        defaultValue.setDepartmentText(DmShared.xNull(txtHDepartmentTo.getText()));
        defaultValue.setSharedModifiedBy(DmShared.xNull(txtHDepartmentTo.getTag2nd()));

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
                    MedicineInTransit.this.modifyReasonMdl = modifyReason;
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
        MedicineInTransit_Mdl mrTemp;
        try {
            mrTemp = topControlSaveInitialize();
            MedicineInTransit_Vld.chkSave(mrTemp);
            srvAsync.invokeRPC().saveRecord(topControl.getPermissions(), mrTemp, previousMdl, DmClient.getVoucherPrefix(), new AsyncCallback<DmResult<String>>() {
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
            topControlSaveError(e.getErrorNo(), e.getErrorSourceCode(), e.getMessage());
        }
    }

    private void topControlSaveError(int errorNo, String errorMessage) {
        topControlSaveError(errorNo, "", errorMessage);
    }

    private void topControlSaveError(int errorNo, final String gridRowIndex, String errorMessage) {
        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtHVoucherType).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtHIssueNo).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtHDepartmentTo).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtVoucherDate).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtRefDate).show();
                break;
            case 600:
                new DmAlertMessageBox(errorMessage, new HideEvent.HideHandler() {
                    @Override
                    public void onHide(HideEvent event) {
                        ((CellSelectionModel) gridMedicineDetail.getGrid().getSelectionModel()).selectCell(DmShared.iNull(gridRowIndex).intValue(), 1);
                    }
                }).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHIssueNo).show();
                break;
        }
    }

    private void topControlMoveRec(String strKey) {
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<MedicineInTransit_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<MedicineInTransit_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<MedicineInTransit_Mdl> result) {
        clear();
        if (result.isStatus()) {
            MedicineInTransit_Mdl mrTemp = result.getData();
            previousMdl = new MedicineInTransit_Mdl(mrTemp);

            topControl.setDocId(mrTemp.getSharedKeyFieldCode());
            txtVoucherNo.setText(mrTemp.getSharedKeyFieldName());
            txtVoucherNo.setTag(DmShared.iNull(mrTemp.getRecIdTransfer().getCounter()).toString());
            txtVoucherNo.setTag2nd(DmShared.xNull(mrTemp.getRecIdTransfer().getPrefix()));
            txtVoucherDate.setText(mrTemp.getVoucherDate());
            txtHVoucherType.setTag(mrTemp.getVoucherTypeCode());
            txtHVoucherType.setText(mrTemp.getVoucherTypeName());
            txtRefDate.setText(mrTemp.getRefDate());
            txtRefNo.setText(mrTemp.getRefNo());
            txtHIssueNo.setTag2nd(mrTemp.getDepartmentFromSite());
            txtHIssueNo.setTag(mrTemp.getInTransitDocID());
            txtHIssueNo.setText(mrTemp.getinTransitRecID());
            txtDepartmentFrom.setTag(mrTemp.getDepartmentFromTag());
            txtDepartmentFrom.setText(mrTemp.getDepartmentFromText());
            txtHDepartmentTo.setTag2nd(mrTemp.getDepartmentToSite());
            txtHDepartmentTo.setTag(mrTemp.getDepartmentToTag());
            txtHDepartmentTo.setText(mrTemp.getDepartmentToText());

            txtHTransferTo.setTag(DmShared.xNull(mrTemp.getReceivedByTag()));
            txtHTransferTo.setText(DmShared.xNull(mrTemp.getReceivedByText()));

            userControl.setTextBlog(preparedByBlog, mrTemp.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, mrTemp.getSharedModifiedBy());

            gridMedicineDetail.getGrid().getStore().clear();
            gridMedicineDetail.getGrid().getStore().addAll(mrTemp.getListItemDetail());
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        manageDisplay(false);
        topControl.setDisplay(true);
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
        txtDepartmentFrom.setEnabled(false);
        txtHTransferTo.setEnabled(false);
        txtVoucherDate.setEnabled(flag);
        if (flag) {
            if (dateEditable) {
                txtVoucherDate.setEnabled(flag);
            } else {
                txtVoucherDate.setEnabled(false);
            }
        }
        if (topControl.getMode().equalsIgnoreCase("New")) {
            txtHIssueNo.setEnabled(departmentEditable);
            txtHDepartmentTo.setEnabled(true);
        } else if (topControl.getMode().equalsIgnoreCase("Edit")) {
            txtHIssueNo.setEnabled(false);
            txtHDepartmentTo.setEnabled(false);
            txtHVoucherType.setEnabled(false);
        }
    }

    private void onAdd() {
        manageDisplay(true);
        clear();
        txtVoucherDate.setText(DmShared.dNull(defaultValue.getCurrentDate()));
        txtHVoucherType.setTag(defaultValue.getVoucherTypeCode());
        txtHVoucherType.setText(defaultValue.getVoucherTypeName());
        txtHDepartmentTo.setTag(defaultValue.getDepartmentTag());
        txtHDepartmentTo.setTag2nd(defaultValue.getSharedModifiedBy());
        txtHDepartmentTo.setText(defaultValue.getDepartmentText());
        gridMedicineDetail.getGrid().getStore().add(new GrdMedicineDetail_Mdl());
    }

    private void clear() {
        previousMdl = null;
        txtVoucherDate.setText("");
        txtVoucherDate.setTag("");
        txtVoucherDate.setTag2nd("");
        gridMedicineDetail.getGrid().getStore().clear();
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
    }

    private void prnRecord(boolean printToPrinter) {
        HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
        hashMapFilter.put("docId", new FieldUDT_Mdl("Transfer No.", DmShared.xNull(txtVoucherNo.getText()), DmShared.xNull(topControl.getDocId())));
        new OpenSourceReport(txtHVoucherType.getTag(), "", widgetName, widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
            @Override
            public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                DmClient.localSettingPrintMap(reportProperty, moduleName, widgetName, "Print");
                return reportProperty;
            }
        }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
    }

    private void rcdMedicineDeail(String strKey) {
        srvAsync.invokeRPC().getRcdMedicineDetail(strKey, DmShared.dNull(txtVoucherDate.getText()), new AsyncCallback<DmResult<MedicineInTransit_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<MedicineInTransit_Mdl> result) {
                fillGridAuto(result);
            }
        });
    }

    private void fillGridAuto(DmResult<MedicineInTransit_Mdl> result) {
//        clear();
        if (result.isStatus()) {
            MedicineInTransit_Mdl mrTemp = result.getData();
            previousMdl = new MedicineInTransit_Mdl(mrTemp);

            gridMedicineDetail.getGrid().getStore().clear();
            gridMedicineDetail.getGrid().getStore().addAll(mrTemp.getListItemDetail());
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
    }
}
