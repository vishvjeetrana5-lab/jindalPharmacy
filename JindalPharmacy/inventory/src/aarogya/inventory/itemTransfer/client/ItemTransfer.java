package aarogya.inventory.itemTransfer.client;

import aarogya.inventory.itemTransfer.shared.ItemTransfer_Srv;
import aarogya.inventory.itemTransfer.shared.ItemTransfer_Vld;
import aarogya.inventory.itemTransfer.shared.TxtHRequisitionNo_Mdl;
import aarogya.inventory.itemTransfer.shared.TxtHTransferTo_Mdl;
import aarogya.inventory.itemTransfer.shared.GrdItemDetail_Mdl;
import aarogya.inventory.itemTransfer.shared.Find_Mdl;
import aarogya.inventory.itemTransfer.shared.ItemTransfer_Mdl;
import aarogya.inventory.itemTransfer.shared.ItemTransfer_SrvAsync;
import aarogya.inventory.itemTransfer.shared.DefaultValue_Mdl;

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
import dataman.dmBase.dmBaseStatic.client.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import dataman.dmToolkit.self.shared.*;
import java.util.HashMap;
import java.util.List;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.modifyReason.client.ModifyReason;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.stStatic.client.STClient;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import dataman.dmToolkit.walkThrough.client.WalkThrough;

public final class ItemTransfer extends ItemTransfer_Dsg implements BlurHandler, AttachEvent.Handler, DmRootEvents, ClickHandler {

    private final ServiceInvoker<ItemTransfer_SrvAsync> srvAsync;
    protected DefaultValue_Mdl defaultValue;
    private final ItemTransferGridEdit<GrdItemDetail_Mdl, GridItemDetail_PrpAcs> itemDetailGridEdit = new ItemTransferGridEdit(gridItemDetail);
    private String deptCode = "",daysToRestrictItemTransferWithoutApproval="0",requisitionMandatoryInTransfer="0";
    private final String vCate = new ProjectVoucherCategory().ITEM_TRANSFER;
    private boolean departmentEditable = false, dateEditable = false;
    private ItemTransfer_Mdl previousMdl;
    final ModifyReason modifyReason = new ModifyReason();
    private ModifyReason_Mdl modifyReasonMdl;
    WalkThrough walkThrough;
    boolean allDepartment = false;

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);
        for (String controlPermission : menuPermission.getControlPermission()) {
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(departmentEditableBlog)) {
                departmentEditable = true;
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(dateEditableBlog)) {
                dateEditable = true;
            }
        }

        topControl.setUserPermission(menuPermission, new VoucherPermissionHandler(DmClient.getVoucherPermission(new String[]{new ProjectVoucherCategory().ITEM_TRANSFER,new ProjectVoucherCategory().SERVICE_TRANSFER})) {
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
                gridItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
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
//                new DmConfirmMessageBox("Do you want to send print to printer?", new DialogHideEvent.DialogHideHandler() {
//
//                    @Override
//                    public void onDialogHide(DialogHideEvent event) {
//                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
//                            prnRecord(true);
//                        } else {
                prnRecord(false);
//                        }
//                    }
//                }).show();
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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(ItemTransfer.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void textHelpEvents() {
        walkThrough.addCallBack(new WalkThrough.CallBack() {
            @Override
            public void onClose() {
                topControlMoveRec(topControl.getDocId());
            }
        });
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
        txtHDepartmentFrom.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHDepartment(helper, 0, blnToEndOfFile, txtHDepartmentTo.getTag(), new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
                srvAsync.invokeRPC(blnCallBackWait).getTxtHDepartment(helper, 1, blnToEndOfFile, txtHDepartmentFrom.getTag(), new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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

//                if (DmShared.iNull(deptCode).longValue() != DmShared.iNull(txtHDepartmentTo.getTag()).longValue()) {
//                    gridItemDetail.getGrid().getStore().clear();
//                }
//                if (blnStatus && t1Row != null) {
//          
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (DmShared.iNull(deptCode).longValue() != DmShared.iNull(txtHDepartmentTo.getTag()).longValue()) {
                    gridItemDetail.getGrid().getStore().clear();
                }
                if (blnStatus && t1Row != null) {
                    txtHDepartmentTo.setTag2nd(DmShared.xNull(t1Row.getSharedModifiedBy()));
                    deptCode = t1Row.getSharedKeyFieldCode();
                    gridItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
                }
                isExistpendingTransferForApproval(blnStatus, t1Row, 0);
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

        txtHRequisition.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHRequisitionNo_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {

                if (DmShared.iNull(txtHDepartmentFrom.getTag()).longValue() == 0) {
                    new DmAlertMessageBox("Please Select Godown From.", txtHDepartmentFrom).show();
                    return;
                }
                if (DmShared.iNull(txtHDepartmentTo.getTag()).longValue() == 0) {
                    new DmAlertMessageBox("Please Select Godown To.", txtHDepartmentTo).show();
                    return;
                }
                if (DmShared.dNull(txtVoucherDate.getText()).equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Please fill Date.", txtVoucherDate).show();
                    return;
                }

                srvAsync.invokeRPC(blnCallBackWait).getTxtHRequisitionNo(txtHDepartmentFrom.getTag(), txtHDepartmentTo.getTag(), txtVoucherDate.getText(), topControl.getMode().equalsIgnoreCase("New") ? "" : topControl.getDocId(),txtHVoucherType.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHRequisitionNo_Mdl>>>() {

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
                if (blnStatus && t1Row != null) {
                    if (DmShared.iNull(txtHTransferTo.getTag()).longValue() == 0) {
                        txtHTransferTo.setText(DmShared.xNull(t1Row.getRequisitionBy()));
                        txtHTransferTo.setTag(DmShared.xNull(t1Row.getRequisitionByCode()));
                    }
                    fillItemDetails(t1Row.getSharedKeyFieldCode());

                } else {
//                    gridItemDetail.getGrid().getStore().clear();
//                    gridItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
                }
            }
        });

    }

    private void fillItemDetails(String reqDocid) {
        srvAsync.invokeRPC().getItemDetail(txtVoucherDate.getText(),reqDocid, topControl.getMode().equalsIgnoreCase("New") ? "" : topControl.getDocId(),txtHDepartmentFrom.getTag(), new AsyncCallback<DmResult<List<GrdItemDetail_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdItemDetail_Mdl>> result) {
                if (result.isStatus()) {
                    if (result.isStatus()) {
                        fillGridAuto(result.getData(), null);
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            }
        });
    }

    private void fillGridAuto(List<GrdItemDetail_Mdl> grdItemDetailList, DmCallBack callback) {
        gridItemDetail.getGrid().getStore().clear();
        if (grdItemDetailList != null) {
            gridItemDetail.getGrid().getStore().addAll(grdItemDetailList);
            gridItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
            ((CellSelectionModel) gridItemDetail.getGrid().getSelectionModel()).selectCell(0, 0);
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

    private void gridEvents() {
        itemDetailGridEdit.addCallBack(new ItemTransferGridEdit.CallBack() {

            @Override
            public void onOk(GrdItemDetail_Mdl opdService) {
                gridItemDetail.commitChanges();
            }

            @Override
            public void onDelete() {
                gridItemDetail.commitChanges();
            }
        });

        gridItemDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                isExistpendingTransferForApproval(false, null, event.getRowIndex());
//                showItemTransferGridEdit(event.getRowIndex());
            }
        });

        gridItemDetail.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showItemTransferGridEdit(gridItemDetail.getGrid().getStore().indexOf(gridItemDetail.getGrid().getSelectionModel().getSelectedItem()));
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

    private void showItemTransferGridEdit(int currentRowIndex) {
        boolean flag = !topControl.getMode().equalsIgnoreCase("Browse");
//        if (flag && DmShared.iNull(txtHDepartmentFrom.getTag()).longValue() == 0 && DmShared.iNull(txtHDepartmentTo.getTag()).longValue() == 0) {
//            new DmAlertMessageBox("Please select atleast one department.", txtHDepartmentFrom).show();
//            return;
//        }
        if (flag && DmShared.iNull(txtHDepartmentFrom.getTag()).longValue() == 0) {
            new DmAlertMessageBox("Please select Godown From.", txtHDepartmentFrom).show();
            return;

        }
        if (flag && DmShared.iNull(txtHDepartmentTo.getTag()).longValue() == 0) {
            new DmAlertMessageBox("Please select Godown To.", txtHDepartmentTo).show();
            return;

        }

        itemDetailGridEdit.show(currentRowIndex, flag, flag, flag,requisitionMandatoryInTransfer,txtHVoucherType.getTag(), this);
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

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtVoucherDate) {
            txtVoucherDate.setText(DmClient.returnDate(txtVoucherDate.getText()));
            if (STClient.isFutureDate(DmShared.dNull(txtVoucherDate.getText()))) {
                txtVoucherDate.setText("");
                new DmAlertMessageBox("Transfer Date should be less than or equal to To Current Date.", txtVoucherDate).show();
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
            itemDetailGridEdit.hide();
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

    public ItemTransfer(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        srvAsync = new ServiceInvoker<ItemTransfer_SrvAsync>(GWT.create(ItemTransfer_Srv.class), ItemTransfer.class.getName());
        this.asWidget().setTitle(widgetName);
        walkThrough = new WalkThrough(srvAsync);
        flwForm.addAttachHandler(this);
        btnApprovedBy.addClickHandler(this);
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
                    manageDisplay(false);
                    swi.setWidget(ItemTransfer.this);
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

    private ItemTransfer_Mdl topControlSaveInitialize() {
        ItemTransfer_Mdl mrTemp = new ItemTransfer_Mdl();

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
        mrTemp.setDepartmentFromSite(DmShared.xNull(txtHDepartmentFrom.getTag2nd()));
        mrTemp.setDepartmentToTag(DmShared.xNull(txtHDepartmentTo.getTag()));
        mrTemp.setDepartmentToSite(DmShared.xNull(txtHDepartmentTo.getTag2nd()));
        mrTemp.getListItemDetail().clear();
        mrTemp.getListItemDetail().addAll(gridItemDetail.getGrid().getStore().getAll());

        mrTemp.setTransferToTag(DmShared.xNull(txtHTransferTo.getTag()));
        mrTemp.setTransferToText(DmShared.xNull(txtHTransferTo.getText()));
        mrTemp.setRemark(DmShared.xNull(txtRemark.getText()));
        // Set default Department Name
        defaultValue.setDepartmentTag(DmShared.xNull(txtHDepartmentFrom.getTag()));
        defaultValue.setDepartmentText(DmShared.xNull(txtHDepartmentFrom.getText()));
        defaultValue.setSharedModifiedBy(DmShared.xNull(txtHDepartmentFrom.getTag2nd()));

//        if (modifyReasonMdl != null) {
//            mrTemp.setModifyReasonMdl(modifyReasonMdl);
//        }
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
                    ItemTransfer.this.modifyReasonMdl = modifyReason;
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
        ItemTransfer_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            ItemTransfer_Vld.chkSave(mrTemp);
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
                        ((CellSelectionModel) gridItemDetail.getGrid().getSelectionModel()).selectCell(DmShared.iNull(gridRowIndex).intValue(), 1);
                    }
                }).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHDepartmentFrom).show();
                break;
        }
    }

    private void topControlMoveRec(String strKey) {
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<ItemTransfer_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<ItemTransfer_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<ItemTransfer_Mdl> result) {
        clear();
        if (result.isStatus()) {
            ItemTransfer_Mdl mrTemp = result.getData();
            previousMdl = new ItemTransfer_Mdl(mrTemp);

            topControl.setDocId(DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            txtVoucherNo.setText(DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            txtVoucherNo.setTag(DmShared.iNull(mrTemp.getRecIdTransfer().getCounter()).toString());
            txtVoucherNo.setTag2nd(DmShared.xNull(mrTemp.getRecIdTransfer().getPrefix()));
            txtVoucherDate.setText(DmShared.xNull(mrTemp.getVoucherDate()));
            txtHVoucherType.setTag(DmShared.xNull(mrTemp.getVoucherTypeCode()));
            txtHVoucherType.setText(DmShared.xNull(mrTemp.getVoucherTypeName()));
            txtRefDate.setText(DmShared.dNull(mrTemp.getRefDate()));
            txtRefNo.setText(DmShared.xNull(mrTemp.getRefNo()));
            txtWTACode.setText(DmShared.xNull(mrTemp.getWtaCode()));
            txtWTACode.setTag(DmShared.xNull(mrTemp.getWtaCode()));
            txtHDepartmentFrom.setTag2nd(DmShared.xNull(mrTemp.getDepartmentFromSite()));
            txtHDepartmentFrom.setTag(DmShared.xNull(mrTemp.getDepartmentFromTag()));
            txtHDepartmentFrom.setText(DmShared.xNull(mrTemp.getDepartmentFromText()));
            txtHDepartmentTo.setTag2nd(DmShared.xNull(mrTemp.getDepartmentToSite()));
            txtHDepartmentTo.setTag(DmShared.xNull(mrTemp.getDepartmentToTag()));
            deptCode = DmShared.xNull(mrTemp.getDepartmentToTag());
            txtHDepartmentTo.setText(DmShared.xNull(mrTemp.getDepartmentToText()));
            txtHTransferTo.setTag(DmShared.xNull(mrTemp.getTransferToTag()));
            txtHTransferTo.setText(DmShared.xNull(mrTemp.getTransferToText()));
            txtHRequisition.setTag(DmShared.xNull(mrTemp.getRequisitionDocId()));
            txtHRequisition.setText(DmShared.xNull(mrTemp.getRequisitionNo()));
            txtRemark.setText(mrTemp.getRemark());
            
            userControl.setTextBlog(preparedByBlog, DmShared.xNull(mrTemp.getSharedPreparedBy()));
            userControl.setTextBlog(modifiedByBlog, DmShared.xNull(mrTemp.getSharedModifiedBy()));
            userControl.setTextBlog(approvedByBlog, DmShared.xNull(mrTemp.getApprovedBy()));
            gridItemDetail.getGrid().getStore().clear();
            gridItemDetail.getGrid().getStore().addAll(mrTemp.getListItemDetail());
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
        txtHVoucherType.setEnabled(flag);
        txtVoucherDate.setEnabled(flag);
        if (flag) {
            if (dateEditable) {
                txtVoucherDate.setEnabled(flag);
            } else {
                txtVoucherDate.setEnabled(false);
            }
        }
        txtVoucherNo.setEnabled(false);
        if (topControl.getMode().equalsIgnoreCase("New")) {
            txtHDepartmentFrom.setEnabled(departmentEditable);
            txtHDepartmentTo.setEnabled(true);
        } else if (topControl.getMode().equalsIgnoreCase("Edit")) {
            txtHDepartmentFrom.setEnabled(false);
            txtHDepartmentTo.setEnabled(false);
        }
    }

    private void onAdd() {
        manageDisplay(true);
        clear();
        txtVoucherDate.setText(DmShared.dNull(defaultValue.getCurrentDate()));
        txtHVoucherType.setTag(defaultValue.getVoucherTypeCode());
        txtHVoucherType.setText(defaultValue.getVoucherTypeName());
        txtHDepartmentFrom.setTag(defaultValue.getDepartmentTag());
        txtHDepartmentFrom.setText(defaultValue.getDepartmentText());
        txtHDepartmentFrom.setTag2nd(defaultValue.getSharedModifiedBy());
        gridItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
        this.daysToRestrictItemTransferWithoutApproval=defaultValue.getDaysToRestrictItemTransferWithoutApproval();
        this.requisitionMandatoryInTransfer=defaultValue.getRequisitionMandatoryInTransfer();
    }

    private void clear() {
        previousMdl = null;
        txtVoucherDate.setText("");
        txtVoucherDate.setTag("");
        txtVoucherDate.setTag2nd("");
        gridItemDetail.getGrid().getStore().clear();
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
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

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnApprovedBy)) {
            if (DmShared.iNull(topControl.getDocId()).longValue() != 0) {
                if (DmShared.xNull(userControl.getTextBlog(approvedByBlog)).equalsIgnoreCase("")) {
                    setApproved(topControl.getDocId(), true);
                } else {
                    setApproved(topControl.getDocId(), false);
                }
            }
        }
    }

    private void setApproved(String docId, boolean isApprove) {
        try {
            if (DmShared.iNull(txtWTACode.getText()).longValue() != 0) {
                walkThrough.show(txtWTACode.getText());
            } else {
                srvAsync.invokeRPC().setApproval(DmClient.getLoginUserName(), isApprove, docId, new AsyncCallback<DmResult<String>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResult<String> result) {
                        if (result.isStatus()) {
                            topControlMoveRec(result.getData());
                        } else {
                            topControlSaveError(result.getErrorNumber(), "", "", result.getErrorMessage());
                        }
                    }
                });
            }
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), "", "", e.getMessage());
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
                new DmAlertMessageBox(errorMessage, txtHDepartmentFrom).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtHTransferTo).show();
                break;
            case 500:
                int indexTemp = DmShared.iNull(errorSourceCode).intValue();
//                if (!DmShared.xNull(errorSource).equalsIgnoreCase("GridRow")) {
//                    List<GrdItemDetail_Mdl> listTemp = gridItemDetail.getGrid().getStore().getAll();
//                    for (GrdItemDetail_Mdl node : listTemp) {
//                        if (DmShared.xNull(node.getRequisitionDocId()).equalsIgnoreCase(errorSource)
//                                && DmShared.xNull(node.getRequisitionVsNo()).equalsIgnoreCase(errorSourceCode)) {
//                            indexTemp = DmShared.iNull(listTemp.indexOf(node)).intValue();
//                            if (errorMessage != null && !errorMessage.isEmpty()) {
//                                if (Character.isDigit(errorMessage.charAt(errorMessage.length() - 1))) {
//                                    errorMessage += " " + DmShared.xNull(node.getUnitName());
//                                }
//                                errorMessage += " in line no. " + (indexTemp + 1);
//                            }
//                            break;
//                        }
//                    }
//                }
                final int gridRowIndex = indexTemp;
                new DmAlertMessageBox(errorMessage, new HideEvent.HideHandler() {
                    @Override
                    public void onHide(HideEvent event) {
                        ((CellSelectionModel) gridItemDetail.getGrid().getSelectionModel()).selectCell(DmShared.iNull(gridRowIndex).intValue(), 1);
                    }
                }).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHVoucherType).show();
                break;
        }
    }

    boolean tmpStatus;
    KeyFieldExtended tmpRow = new KeyFieldExtended();
    int intTempRowNo = 0;

    private void isExistpendingTransferForApproval(boolean blnStatus, KeyFieldExtended t1Row, int intRowNo) {
        tmpStatus = blnStatus;
        tmpRow = t1Row;
        intTempRowNo = intRowNo;
        srvAsync.invokeRPC().getIsExisPendingTransferForApproval(txtHVoucherType.getTag(), txtVoucherDate.getText(), txtHDepartmentTo.getTag(),daysToRestrictItemTransferWithoutApproval, new AsyncCallback<DmResult<Boolean>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<Boolean> result) {
                if (!result.isStatus()) {
                    if (DmShared.iNull(deptCode).longValue() != DmShared.iNull(txtHDepartmentTo.getTag()).longValue()) {
                        gridItemDetail.getGrid().getStore().clear();
                    }
                    if (tmpStatus && tmpRow != null) {
                        txtHDepartmentTo.setTag2nd(DmShared.xNull(tmpRow.getSharedModifiedBy()));
                        deptCode = tmpRow.getSharedKeyFieldCode();
                        gridItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
                    }
                    showItemTransferGridEdit(intTempRowNo);
                } else {
                    new DmAlertMessageBox("Some transfer of previous dates pending for approval. Can't be generate new transfer", txtHDepartmentTo).show();
                }
            }
        });
    }
}
