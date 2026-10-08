package aarogya.pharmacy.medicineTransferWholeSale.client;

import aarogya.pharmacy.medicineTransferWholeSale.shared.*;
import aarogya.projectMenu.self.shared.pmPharmacy;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.modifyReason.client.ModifyReason;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.stStatic.client.STClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
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

public final class MedicineTransferWholeSale extends MedicineTransferWholeSale_Dsg implements BlurHandler, AttachEvent.Handler, DmRootEvents {

    private ServiceInvoker<MedicineTransferWholeSale_SrvAsync> srvAsync;
    protected DefaultValue_Mdl defaultValue;
    final MedicineTransferGridEdit<GrdMedicineDetail_Mdl, GridMedicineDetail_PrpAcs> medicineTransferGridEdit = new MedicineTransferGridEdit(gridMedicineDetail);
    private String deptCode = "";
    private final String vCate = new ProjectVoucherCategory().MEDICINE_TRANSFER_WHOLE_SALE;
    protected boolean departmentEditable = false, dateEditable = false, isAllowAddBatch = false;
    final ModifyReason modifyReason = new ModifyReason();
    private ModifyReason_Mdl modifyReasonMdl;
    private MedicineTransferWholeSale_Mdl previousMdl;
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
                txtHDepartmentFrom.setFocus(true);
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                manageDisplay(true);
                gridMedicineDetail.getGrid().getStore().add(new GrdMedicineDetail_Mdl());
                txtHDepartmentFrom.setFocus(true);
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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(MedicineTransferWholeSale.this);
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

        txtHDepartmentFrom.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHDepartment(helper, blnToEndOfFile, txtHDepartmentTo.getTag(), true, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDepartmentFrom).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDepartmentFrom.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDepartmentFrom).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus && t1Row != null) {
                    txtHDepartmentFrom.setTag2nd(DmShared.xNull(t1Row.getSharedModifiedBy()));
                }
            }
        });

        txtHDepartmentTo.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                deptCode = DmShared.xNull(txtHDepartmentTo.getTag());
                srvAsync.invokeRPC(blnCallBackWait).getTxtHDepartment(helper, blnToEndOfFile, txtHDepartmentFrom.getTag(), false, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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

    private void fillItemDetails(String reqDocid) {
        srvAsync.invokeRPC().getMedicineDetail(reqDocid, topControl.getMode().equalsIgnoreCase("New") ? "" : topControl.getDocId(), txtHDepartmentFrom.getTag(), txtVoucherDate.getText(), new AsyncCallback<DmResult<List<GrdMedicineDetail_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdMedicineDetail_Mdl>> result) {
                if (result.isStatus()) {
                    if (result.isStatus()) {
                        fillGridAuto(result.getData(), null);
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            }
        }
        );
    }

    private void gridEvents() {
        medicineTransferGridEdit.addCallBack(new MedicineTransferGridEdit.CallBack() {

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
                showMedicineTransferGridEdit(event.getRowIndex());
            }
        });

        gridMedicineDetail.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showMedicineTransferGridEdit(gridMedicineDetail.getGrid().getStore().indexOf(gridMedicineDetail.getGrid().getSelectionModel().getSelectedItem()));
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

    private void showMedicineTransferGridEdit(int currentRowIndex) {

        if (DmShared.xNull(txtHDepartmentFrom.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please select Department From.", txtHDepartmentFrom).show();
            return;
        }

        if (DmShared.xNull(txtHDepartmentTo.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please select Department To.", txtHDepartmentTo).show();
            return;
        }

        if (!DmShared.xNull(txtHDepartmentFrom.getTag()).equalsIgnoreCase("") && !DmShared.xNull(txtHDepartmentTo.getTag()).equalsIgnoreCase("")) {

            if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {
                medicineTransferGridEdit.show(currentRowIndex, false, false, false, this);
            } else {
                medicineTransferGridEdit.show(currentRowIndex, true, true, true, this);
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
            medicineTransferGridEdit.hide();
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

    public MedicineTransferWholeSale(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        srvAsync = new ServiceInvoker<MedicineTransferWholeSale_SrvAsync>(GWT.create(MedicineTransferWholeSale_Srv.class), MedicineTransferWholeSale.class.getName());
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
                    swi.setWidget(MedicineTransferWholeSale.this);
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

    private MedicineTransferWholeSale_Mdl topControlSaveInitialize() {
        MedicineTransferWholeSale_Mdl mrTemp = new MedicineTransferWholeSale_Mdl();
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
        mrTemp.setDepartmentFromTag(DmShared.xNull(txtHDepartmentFrom.getTag()));
        mrTemp.setDepartmentFromText(DmShared.xNull(txtHDepartmentFrom.getText()));
        mrTemp.setDepartmentFromSite(DmShared.xNull(txtHDepartmentFrom.getTag2nd()));
        mrTemp.setDepartmentToTag(DmShared.xNull(txtHDepartmentTo.getTag()));
        mrTemp.setDepartmentToText(DmShared.xNull(txtHDepartmentTo.getText()));
        mrTemp.setDepartmentToSite(DmShared.xNull(txtHDepartmentTo.getTag2nd()));
        mrTemp.setTransferToTag(DmShared.xNull(txtHTransferTo.getTag()));
        mrTemp.setTransferToText(DmShared.xNull(txtHTransferTo.getText()));
        mrTemp.getListItemDetail().clear();
        mrTemp.getListItemDetail().addAll(gridMedicineDetail.getGrid().getStore().getAll());

        // Set default Department Name
        defaultValue.setDepartmentTag(DmShared.xNull(txtHDepartmentFrom.getTag()));
        defaultValue.setDepartmentText(DmShared.xNull(txtHDepartmentFrom.getText()));
        defaultValue.setSharedModifiedBy(DmShared.xNull(txtHDepartmentFrom.getTag2nd()));

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
                    MedicineTransferWholeSale.this.modifyReasonMdl = modifyReason;
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
        MedicineTransferWholeSale_Mdl mrTemp;
        try {
            mrTemp = topControlSaveInitialize();
            MedicineTransferWholeSale_Vld.chkSave(mrTemp);
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
                new DmAlertMessageBox(errorMessage, txtHDepartmentFrom).show();
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
                new DmAlertMessageBox(errorMessage, txtHDepartmentFrom).show();
                break;
        }
    }

    private void topControlMoveRec(String strKey) {
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<MedicineTransferWholeSale_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<MedicineTransferWholeSale_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<MedicineTransferWholeSale_Mdl> result) {
        clear();
        if (result.isStatus()) {
            MedicineTransferWholeSale_Mdl mrTemp = result.getData();
            previousMdl = new MedicineTransferWholeSale_Mdl(mrTemp);

            topControl.setDocId(mrTemp.getSharedKeyFieldCode());
            txtVoucherNo.setText(mrTemp.getSharedKeyFieldName());
            txtVoucherNo.setTag(DmShared.iNull(mrTemp.getRecIdTransfer().getCounter()).toString());
            txtVoucherNo.setTag2nd(DmShared.xNull(mrTemp.getRecIdTransfer().getPrefix()));
            txtVoucherDate.setText(mrTemp.getVoucherDate());
            txtHVoucherType.setTag(mrTemp.getVoucherTypeCode());
            txtHVoucherType.setText(mrTemp.getVoucherTypeName());
            txtRefDate.setText(mrTemp.getRefDate());
            txtRefNo.setText(mrTemp.getRefNo());
            txtHDepartmentFrom.setTag2nd(mrTemp.getDepartmentFromSite());
            txtHDepartmentFrom.setTag(mrTemp.getDepartmentFromTag());
            txtHDepartmentFrom.setText(mrTemp.getDepartmentFromText());
            txtHDepartmentTo.setTag2nd(mrTemp.getDepartmentToSite());
            txtHDepartmentTo.setTag(mrTemp.getDepartmentToTag());
            txtHDepartmentTo.setText(mrTemp.getDepartmentToText());

            txtHTransferTo.setTag(DmShared.xNull(mrTemp.getTransferToTag()));
            txtHTransferTo.setText(DmShared.xNull(mrTemp.getTransferToText()));

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
        txtVoucherDate.setEnabled(flag);
        if (flag) {
            if (dateEditable) {
                txtVoucherDate.setEnabled(flag);
            } else {
                txtVoucherDate.setEnabled(false);
            }
        }
        if (topControl.getMode().equalsIgnoreCase("New")) {
            txtHDepartmentFrom.setEnabled(departmentEditable);
            txtHDepartmentTo.setEnabled(true);
        } else if (topControl.getMode().equalsIgnoreCase("Edit")) {
            txtHDepartmentFrom.setEnabled(false);
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
        txtHDepartmentFrom.setTag(defaultValue.getDepartmentTag());
        txtHDepartmentFrom.setTag2nd(defaultValue.getSharedModifiedBy());
        txtHDepartmentFrom.setText(defaultValue.getDepartmentText());
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

    private void fillGridAuto(List<GrdMedicineDetail_Mdl> grdItemDetailList, DmCallBack callback) {
        gridMedicineDetail.getGrid().getStore().clear();
        if (grdItemDetailList != null) {
            gridMedicineDetail.getGrid().getStore().addAll(grdItemDetailList);
            gridMedicineDetail.getGrid().getStore().add(new GrdMedicineDetail_Mdl());
            ((CellSelectionModel) gridMedicineDetail.getGrid().getSelectionModel()).selectCell(0, 0);
            manageRequisitionDepartment(true);
            if (callback != null) {
                DmResult<Object> dmrTemp = new DmResult<>();
                dmrTemp.setStatus(true);
                callback.result(dmrTemp);
            }
        } else {
            manageRequisitionDepartment(false);
        }
    }

    private void manageRequisitionDepartment(boolean requisitionDataExist) {
        if (requisitionDataExist) {
            txtHDepartmentFrom.setEnabled(false);
            txtHDepartmentTo.setEnabled(false);
        } else {
            if (topControl.getMode().equalsIgnoreCase("New")) {
                txtHDepartmentFrom.setEnabled(departmentEditable);
                txtHDepartmentTo.setEnabled(true);
            }
        }
    }
}
