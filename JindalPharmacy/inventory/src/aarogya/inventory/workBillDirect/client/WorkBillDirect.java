package aarogya.inventory.workBillDirect.client;

import aarogya.inventory.workBillDirect.shared.WorkBillDirect_Srv;
import aarogya.inventory.workBillDirect.shared.Find_Mdl;
import aarogya.inventory.workBillDirect.shared.WorkBillDirect_SrvAsync;
import aarogya.inventory.workBillDirect.shared.TxtHVoucher_Mdl;
import aarogya.inventory.workBillDirect.shared.GrdGrnDetail_Mdl;
import aarogya.inventory.workBillDirect.shared.WorkBillDirect_Vld;
import aarogya.inventory.workBillDirect.shared.WorkBillDirect_Mdl;
import aarogya.inventory.workBillDirect.shared.TxtHSupplier_Mdl;
import aarogya.inventory.workBillDirect.shared.DefaultValue_Mdl;
import aarogya.inventory.orderSearch.client.OrderSearch;
import aarogya.projectMenu.self.shared.ProjectRuleCode;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.modifyReason.client.ModifyReason;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.stStatic.client.STClient;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import dataman.dmBase.self.shared.*;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.menu.Item;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import dataman.dmToolkit.dmFileManager.client.DmFileManager;
import dataman.dmToolkit.dmGUI.client.DmGUI;
import dataman.dmToolkit.dmGUI.shared.DmGUIControl_Mdl;
import dataman.dmToolkit.dmGUI.shared.DmGUI_Mdl;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import dataman.dmToolkit.overHead.client.OverHead;
import dataman.dmToolkit.overHead.shared.OverHeadMA_Mdl;
import dataman.dmToolkit.overHead.shared.OverHead_Mdl;
import dataman.dmToolkit.overHead.shared.OverHead_Shared;
import dataman.dmToolkit.overHeadLine.client.OverHeadLineHandler;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Mdl;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Shared;
import dataman.dmToolkit.walkThrough.client.WalkThrough;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WorkBillDirect extends WorkBillDirect_Dsg implements AttachEvent.Handler, DmRootEvents, ClickHandler, BlurHandler, SelectionHandler<Item> {

    ServiceInvoker<WorkBillDirect_SrvAsync> SrvAsync;
    protected DefaultValue_Mdl defaultValue;
    WalkThrough walkThrough;
    final OrderSearch orderSearch = new OrderSearch();
    protected final OverHeadLineHandler<GrdGrnDetail_Mdl> ohlHandler = new OverHeadLineHandler(overHead, 300, 256);
    final WorkBillDirectGridEdit<GrdGrnDetail_Mdl, Grd_PrpAcs> grnDetailGridEdit = new WorkBillDirectGridEdit(grdGRNDetail);
    private final DmFileManager manageFile = new DmFileManager(ProjectRuleCode.PURCHASE_BILL_DIRECT_ENTRY.getRuleCode());
    ModifyReason_Mdl modifyReasonMdl;
    WorkBillDirect_Mdl previous_Mdl = null;
    boolean departmentEditable = false, isApprove = false, isPurchaseOrderExists = false, isAttachFile, isDeleteFile = false,
            singlePOFlowInPurchase = false;
    final ModifyReason modifyReason = new ModifyReason();
    String selectedSupplierCode, partyTypeForEntry, partyType, supplierGstin, supplierGstinDate, location,
            rcmApplicable, wtaCode, voucherCategory, guiTemplateCode;

    private void topControlEvents(UserMenu menuPermission) {

        topControl.setTitle(this.asWidget().getTitle());
        topControl.setRefreshRequired(false);
        for (String controlPermission : menuPermission.getControlPermission()) {
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(departmentEditableBlog)) {
                departmentEditable = true;
            }
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(approvedByBlog)) {
                isApprove = true;
            }
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(attachFileBlog)) {
                isAttachFile = true;
            }
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(deleteFileBlog)) {
                isDeleteFile = true;
            }
        }

        overHead.addCallback(new OverHead.Callback() {

            @Override
            public void initialized() {
                setCardAcInOH();
            }

            @Override
            public String getVoucherType() {
                return txtHVoucherType.getTag();
            }

            @Override
            public String getVoucherDate() {
                return txtVoucherDate.getText();
            }

            @Override
            public void endCalculate() {

            }
        });

        ohlHandler.addCallback(new OverHeadLineHandler.Callback<GrdGrnDetail_Mdl>() {

            @Override
            public void initialized() {
                grdGRNDetail.getGrid().getStore().clear();
                grdGRNDetail.getGrid().getStore().add(new GrdGrnDetail_Mdl());
                overHead.initialize();
            }

            @Override
            public String getVoucherType() {
                return txtHVoucherType.getTag();
            }

            @Override
            public String getVoucherDate() {
                return txtVoucherDate.getText();
            }

            @Override
            public void endCalculate() {
            }

            @Override
            public void endSyncToOH() {
            }

            @Override
            public List<GrdGrnDetail_Mdl> getRows() {
                return grdGRNDetail.getGrid().getStore().getAll();
            }
        });

        gui.addCallback(new DmGUI.Callback() {

            @Override
            public void initialized() {

            }

            @Override
            public String getGUITemplate() {
                return guiTemplateCode;
            }

            @Override
            public Object addCustom(DmGUIControl_Mdl dmGUIControl_Mdl, FlowPanel flwTr, Object tabIndexAfter) {
                return null;
            }

            @Override
            public void removeCustom(DmTopControl topControl) {

            }

            @Override
            public void guiDataCopied(DmGUI_Mdl gui_Mdl, List<String> listCustomWidgetToCopy, String copyFromDocId, HashMap<String, String> param) {
            }
        });

        topControl.setUserPermission(menuPermission, new VoucherPermissionHandler(DmClient.getVoucherPermission(new String[]{new ProjectVoucherCategory().PURCHASE_BILL_DIRECT, new ProjectVoucherCategory().PURCHASE_BILL_DIRECT_UNREGISTERED})) {
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
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                if (DmShared.xNull(userControl.getTextBlog(approvedByBlog)).equalsIgnoreCase("")) {
                    manageDisplay(true);
                    grdGRNDetail.getGrid().getStore().add(new GrdGrnDetail_Mdl());
                    txtSupplierBillNo.setFocus(true);
                } else {
                    topControl.btnClick("");
                    new DmAlertMessageBox("Record is approved. You can't modify this record.", (DmTextBox) null).show();
                }

            }
        });

        topControl.addDeleteHandeller(new DmTopControl.eDelete() {
            @Override
            public void deleteClicked() {
                if (DmShared.xNull(userControl.getTextBlog(approvedByBlog)).equalsIgnoreCase("")) {
                    new DmConfirmMessageBox("Are you sure? You want to delete this record.", new DialogHideEvent.DialogHideHandler() {

                        @Override
                        public void onDialogHide(DialogHideEvent event) {
                            if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                                topControlDelete(topControl.getDocId());
                            }
                        }
                    }).show();
                } else {
                    topControl.btnClick("");
                    new DmAlertMessageBox("Record is approved. You can't delete this record.", (DmTextBox) null).show();
                }
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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(WorkBillDirect.this);
            }
        });

        topControl.addOtherHandeller(new DmTopControl.eOther() {
            @Override
            public void otherClicked() {
                menu.show(topControl.getButton("Other"));
            }
        });

        topControl.setDisplay(true);
    }

    private void showModifyReason() {
        modifyReason.show(topControl.getMode(), topControl.getDocId());
    }

    private void showOrderSearch(boolean isAutoShow) {
        orderSearch.show(txtHVoucherType.getTag(),
                topControl.getMode().equalsIgnoreCase("New") ? "" : topControl.getDocId(),
                txtHSupplier.getTag(),
                txtSupplierBillDate.getText(),
                voucherCategory, isAutoShow,singlePOFlowInPurchase);
    }

    private void textHelpEvents() {

        txtHVoucherType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHVoucher_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHVoucher(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHVoucher_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHVoucherType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHVoucher_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHVoucherType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHVoucherType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHVoucher_Mdl t1Row) {
                if (!DmShared.xNull(txtHVoucherType.getTag()).equalsIgnoreCase(DmShared.xNull(txtHVoucherType.getTag2nd()))) {
                    txtHVoucherType.setTag2nd(txtHVoucherType.getTag());

                    //Clear Related Values
                    voucherCategory = "";
                    guiTemplateCode = "";

                    if (topControl.getMode().equalsIgnoreCase("New")) {
                        txtHSupplier.setTag2nd("");
                        txtHSupplier.setTag("");
                        txtHSupplier.setText("");
                        ohlHandler.initialize();
                    }

                    //Set Related Values
                    if (blnStatus && t1Row != null) {
                        voucherCategory = t1Row.getVoucherCategory();
                        guiTemplateCode = t1Row.getGuiTemplateCode();
                    }

                    //Reset GUI
                    gui.initializeViaTemplateCode();

                }
            }
        });

        txtHSupplier.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHSupplier_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSupplier(voucherCategory, txtHVoucherType.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHSupplier_Mdl>>>() {

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
                if (!DmShared.xNull(selectedSupplierCode).equalsIgnoreCase(DmShared.xNull(txtHSupplier.getTag()))) {
                    selectedSupplierCode = DmShared.xNull(txtHSupplier.getTag());
                    if (topControl.getMode().equalsIgnoreCase("New")) {
                        ohlHandler.initialize();
                    } else {
                        setCardAcInOH();
                    }
                }
                if (blnStatus && tRow != null) {
                    txtHSupplier.setTag2nd(DmShared.xNull(tRow.getStateCode()));
                    supplierGstin = DmShared.xNull(tRow.getGstin());
                    supplierGstinDate = DmShared.dNull(tRow.getGstinDate());
                    partyType = DmShared.xNull(tRow.getPartyType());
                    resetGstDetails();
                    showOrderSearch(true);
                }

            }
        });

        txtHDepartment.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHDepartment(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
            }
        });

        txtHCalculateOn.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHCalculateOn(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCalculateOn).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHCalculateOn.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCalculateOn).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus && t1Row != null) {
                    calcAmount();
                }
            }
        });

        modifyReason.addCallBack(new ModifyReason.CallBack() {

            @Override
            public void onOk(ModifyReason_Mdl modifyReason) {
                modifyReasonMdl = modifyReason;
                topControlSave();
            }
        });

        walkThrough.addCallBack(new WalkThrough.CallBack() {
            @Override
            public void onClose() {
                topControlMoveRec(topControl.getDocId());
            }
        });

        orderSearch.addCallBack(new OrderSearch.CallBack() {
            @Override
            public void ok(List<KeyFieldExtended> listKF) {
                fillGrnDetail(listKF);
            }

            @Override
            public void close() {
                if (departmentEditable) {
                    txtHDepartment.setFocus(true);
                } else {
                    txtRemark.setFocus(true);
                }
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
                        Window.alert(caught.getMessage());
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<Find_Mdl>> result) {
                        if (result.isStatus()) {
                            dmFind.setData(result.getData(), result.getPaging());
                        } else {
                            Window.alert(result.getErrorMessage());
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
        grnDetailGridEdit.addCallBack(new WorkBillDirectGridEdit.CallBack() {

            @Override
            public void onOk(GrdGrnDetail_Mdl opdService) {
                grdGRNDetail.commitChanges();
                ohlHandler.syncronizeLineHeader();
                calculate();
            }

            @Override
            public void onDelete() {
                grdGRNDetail.commitChanges();
                ohlHandler.syncronizeLineHeader();
                calculate();
            }
        });

        grdGRNDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showGrnEntryGridEdit(event.getRowIndex());
            }
        });

        grdGRNDetail.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showGrnEntryGridEdit(grdGRNDetail.getGrid().getStore().indexOf(grdGRNDetail.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
    }

    private void showGrnEntryGridEdit(int currentRowIndex) {
        if (DmShared.xNull(txtHVoucherType.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please select Voucher Type", txtHVoucherType).show();
            return;
        }
        if (DmShared.dNull(txtVoucherDate.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please fiil Voucher Date", txtVoucherDate).show();
            return;
        }
        if (DmShared.xNull(txtHSupplier.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please select Supplier", txtHSupplier).show();
            return;
        }
        if (DmShared.xNull(txtSupplierBillNo.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please fill Ref. No.", txtSupplierBillNo).show();
            return;
        }
        if (DmShared.dNull(txtSupplierBillDate.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please fill Ref. Date", txtSupplierBillDate).show();
            return;
        }
        if (DmShared.xNull(txtHDepartment.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please select Department", txtHDepartment).show();
            return;
        }
        if ((DmShared.convertDate(txtVoucherDate.getText()).getTime() < (DmShared.convertDate(txtSupplierBillDate.getText()).getTime()))) {
            new DmAlertMessageBox("Voucher Date should be greater than or equal to Ref. Date.", txtVoucherDate).show();
            return;
        }

        String status = grdGRNDetail.getGrid().getStore().get(currentRowIndex).getIsStatus();
        String returnedQty = grdGRNDetail.getGrid().getStore().get(currentRowIndex).getReturnedQty();
        String mode = topControl.getMode();
        String msg = "";
        if (DmShared.iNull(status).intValue() == 1) {
            mode = "Browse";
            msg = "Record has been canceled, can't change record.";
        } else if (DmShared.iNull(status).intValue() == 2) {
            mode = "Browse";
            msg = "Record has been completed, can't change record.";
        } else if (DmShared.vNull(returnedQty) != 0) {
            mode = "Browse";
            msg = "Record has been used, can't change record.";
        }
        if (mode.trim().equalsIgnoreCase("Browse")) {
            if (!topControl.getMode().equalsIgnoreCase("Browse") && !msg.equalsIgnoreCase("")) {
                new DmAlertMessageBox(msg, (DmTextBox) null).show();
            }
            grnDetailGridEdit.show(currentRowIndex, false, false, false, this,cbShowStockItems.getValue());
        } else {
            grnDetailGridEdit.show(currentRowIndex, true, true, true, this,cbShowStockItems.getValue());
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnApprovedBy) {
            if (DmShared.iNull(topControl.getDocId()).longValue() != 0) {
                if (DmShared.xNull(userControl.getTextBlog(approvedByBlog)).equalsIgnoreCase("")) {
                    setApproved(topControl.getDocId(), true);
                } else {
                    setApproved(topControl.getDocId(), false);
                }
            }
        } else if (event.getSource() == btnAddOrder) {
            if (DmShared.iNull(txtHSupplier.getTag()).longValue() == 0) {
                new DmAlertMessageBox("Please select Supplier.", txtHSupplier).show();
            } else if (DmShared.dNull(txtSupplierBillDate.getText()).trim().equals("")) {
                new DmAlertMessageBox("Please enter Ref. Date.", txtSupplierBillDate).show();
            } else {
                showOrderSearch(false);
            }
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtVoucherDate) {
            txtVoucherDate.setText(DmClient.returnDate(txtVoucherDate.getText()));
            if (STClient.isFutureDate(txtVoucherDate.getText())) {
                txtVoucherDate.setText("");
                new DmAlertMessageBox("Voucher Date should be less than or equal to current date.", txtVoucherDate).show();
                return;
            }
            if (topControl.getMode().equalsIgnoreCase("New")
                    && !DmShared.xNull(txtVoucherDate.getText()).equalsIgnoreCase(txtVoucherDate.getTag())) {
                txtVoucherDate.setTag(txtVoucherDate.getText());
                ohlHandler.initialize();
            }
        } else if (event.getSource() == txtSupplierBillDate) {
            txtSupplierBillDate.setText(DmClient.returnDate(txtSupplierBillDate.getText()));
            if (topControl.getMode().equalsIgnoreCase("New")
                    && !DmShared.xNull(txtSupplierBillDate.getText()).equalsIgnoreCase(txtSupplierBillDate.getTag())) {
                txtSupplierBillDate.setTag(txtSupplierBillDate.getText());
                ohlHandler.initialize();
            }
            resetGstDetails();
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

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            grnDetailGridEdit.hide();
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getShiftKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_P) {
            rootEvent.stopPropagation();
        }
    }

    public WorkBillDirect(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<WorkBillDirect_SrvAsync>(GWT.create(WorkBillDirect_Srv.class), WorkBillDirect.class.getName());
        walkThrough = new WalkThrough(SrvAsync);
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        findEvents();
        textHelpEvents();
        gridEvents();
        manageEvent();
        overHead.setBannerVisible(false);
        gui.setBannerVisible(false);
        iniDefaultValues(swi);
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
                    guiTemplateCode = defaultValue.getGuiTemplateCode();
                    gui.initializeViaTemplateCode();
                    taxSummary.initialize(defaultValue.isGstApplicable());
                    swi.setWidget(WorkBillDirect.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void topControlSave() {
        WorkBillDirect_Mdl mrTemp;

        try {
            ohlHandler.syncronizeLineHeader();
            defaultValue.setNewVoucherTypeCode(txtHVoucherType.getTag());
            defaultValue.setNewVoucherTypeName(txtHVoucherType.getText());
            defaultValue.setNewVoucherTypeCategory(voucherCategory);
            defaultValue.setNewVoucherDate(txtVoucherDate.getText());
            defaultValue.setNewGuiTemplateCode(guiTemplateCode);
            calculate();
            mrTemp = topControlSaveInitialize();
            WorkBillDirect_Vld.chkSave(mrTemp);
            OverHead_Shared.chkGrid(mrTemp.getOverHeadList());
            OverHeadLine_Shared.chkGrid(mrTemp.getGrdGrnDetail());
            SrvAsync.invokeRPC().saveRecord(mrTemp, previous_Mdl, DmClient.getVoucherPrefix(), isApprove, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        topControl.setDocId(result.getData());
                        if (topControl.getMode().equalsIgnoreCase("New")) {
                            defaultValue.setIsContinuousNewMode(true);
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

    private WorkBillDirect_Mdl topControlSaveInitialize() {
        WorkBillDirect_Mdl mrTemp = new WorkBillDirect_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
            mrTemp.getObjRecId().setCounter(previous_Mdl.getObjRecId().getCounter());
            mrTemp.getObjRecId().setPrefix(previous_Mdl.getObjRecId().getPrefix());
            mrTemp.getObjRecId().setRecIdValue(previous_Mdl.getObjRecId().getRecIdValue());
        }

        resetGstDetails();

        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtVoucherNo.getText()).trim());
        mrTemp.setU_EntDt_LatestLine(DmShared.xNull(txtVoucherNo.getTag()));
        mrTemp.setVoucherTypeCode(DmShared.xNull(txtHVoucherType.getTag()));
        mrTemp.setVoucherTypeName(DmShared.xNull(txtHVoucherType.getText()));
        mrTemp.setVoucherCategory(DmShared.xNull(voucherCategory));
        mrTemp.setWtaCode(DmShared.iNull(wtaCode).toString());
        mrTemp.setVoucherDate(DmShared.dNull(txtVoucherDate.getText()).trim());
        mrTemp.setSupplierStateCode(DmShared.xNull(txtHSupplier.getTag2nd()));
        mrTemp.setSupplierTag(DmShared.xNull(txtHSupplier.getTag()));
        mrTemp.setDepartmentTag(DmShared.xNull(txtHDepartment.getTag()));
        mrTemp.setDepartmentText(DmShared.xNull(txtHDepartment.getText()));
        mrTemp.setSupplierBillNo(DmShared.xNull(txtSupplierBillNo.getText()).trim());
        mrTemp.setSupplierBillDate(DmShared.dNull(txtSupplierBillDate.getText()));
        mrTemp.setCalculateOnTag(DmShared.xNull(txtHCalculateOn.getTag()));
        mrTemp.setRemark(DmShared.xNull(txtRemark.getText()).trim());

        mrTemp.setPlaceOfSupply(DmShared.xNull(defaultValue.getSiteStateCode()).trim());
        mrTemp.setRcmApplicable(rcmApplicable);
        mrTemp.setPartyTypeForEntry(partyTypeForEntry);
        mrTemp.setPartyType(partyType);
        mrTemp.setDefaultValueMdl(defaultValue);

        OverHead_Mdl ohMdl = OverHead_Shared.getOHByUniqueId("RO", overHead.getOverHeadList());
        mrTemp.setRoundOffAmount(ohMdl != null ? DmShared.vNull(ohMdl.getAmount()).toString() : "0");

        mrTemp.getGrdGrnDetail().clear();
        mrTemp.getGrdGrnDetail().addAll(grdGRNDetail.getGrid().getStore().getAll());

        mrTemp.setGuiMdl(gui.getGUI_Mdl());
        mrTemp.setGuiConfigCode(gui.getConfigCode());

        mrTemp.getOverHeadList().clear();
        mrTemp.getOverHeadList().addAll(overHead.getOverHeadList());

        if (modifyReasonMdl != null) {
            mrTemp.setModifyReasonMdl(modifyReasonMdl);
        }

        return mrTemp;
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
                new DmAlertMessageBox(errorMessage, txtHSupplier).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtSupplierBillNo).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtSupplierBillDate).show();
                break;
            case 600:
                new DmAlertMessageBox(errorMessage, txtHDepartment).show();
                break;
            case 650:
                new DmAlertMessageBox(errorMessage, txtHCalculateOn).show();
                break;
            case 700:
                int indexTemp = DmShared.iNull(errorSourceCode).intValue();
                if (!DmShared.xNull(errorSource).equalsIgnoreCase("GridRow")) {
                    List<GrdGrnDetail_Mdl> listTemp = grdGRNDetail.getGrid().getStore().getAll();
                    for (GrdGrnDetail_Mdl node : listTemp) {
                        if (DmShared.xNull(node.getPoDocId()).equalsIgnoreCase(errorSource)
                                && DmShared.xNull(node.getPoV_SNo()).equalsIgnoreCase(errorSourceCode)) {
                            indexTemp = DmShared.iNull(listTemp.indexOf(node)).intValue();
                            if (errorMessage != null && !errorMessage.isEmpty()) {
                                if (Character.isDigit(errorMessage.charAt(errorMessage.length() - 1))) {
                                    errorMessage += " " + DmShared.xNull(node.getPurchaseUnitName());
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
                        ((CellSelectionModel) grdGRNDetail.getGrid().getSelectionModel()).selectCell(DmShared.iNull(gridRowIndex).intValue(), 1);
                    }
                }).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHSupplier).show();
                break;
        }
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

    private void topControlMoveRec(String strKey) {
        ohlHandler.setDefault();
        defaultValue.setIsContinuousNewMode(false);
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<WorkBillDirect_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<WorkBillDirect_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<WorkBillDirect_Mdl> result) {
        clear();
        if (result.isStatus()) {
            WorkBillDirect_Mdl mrTemp = result.getData();
            previous_Mdl = result.getData().clone();
            topControl.setDocId(mrTemp.getSharedKeyFieldCode());
            txtVoucherNo.setText(mrTemp.getSharedKeyFieldName());
            txtVoucherNo.setTag(mrTemp.getU_EntDt_LatestLine());
            txtVoucherDate.setText(mrTemp.getVoucherDate());
            txtHVoucherType.setTag2nd(mrTemp.getVoucherTypeCode());
            txtHVoucherType.setTag(mrTemp.getVoucherTypeCode());
            txtHVoucherType.setText(mrTemp.getVoucherTypeName());
            voucherCategory = mrTemp.getVoucherCategory();
            wtaCode = mrTemp.getWtaCode();
            txtHSupplier.setTag2nd(mrTemp.getSupplierStateCode());
            txtHSupplier.setTag(mrTemp.getSupplierTag());
            txtHSupplier.setText(mrTemp.getSupplierText());
            txtHDepartment.setTag(mrTemp.getDepartmentTag());
            txtHDepartment.setText(mrTemp.getDepartmentText());
            txtSupplierBillDate.setText(mrTemp.getSupplierBillDate());
            txtSupplierBillNo.setText(mrTemp.getSupplierBillNo());
            txtHCalculateOn.setTag(mrTemp.getCalculateOnTag());
            txtHCalculateOn.setText(mrTemp.getCalculateOnText());
            txtRemark.setText(mrTemp.getRemark());

            selectedSupplierCode = mrTemp.getSupplierTag();
            supplierGstin = mrTemp.getSupplierGstin();
            supplierGstinDate = mrTemp.getSupplierGstinDate();
            rcmApplicable = mrTemp.getRcmApplicable();
            partyTypeForEntry = mrTemp.getPartyTypeForEntry();
            partyType = mrTemp.getPartyType();
            isPurchaseOrderExists = mrTemp.getIsPurchaseOrderExists();

            userControl.setTextBlog(preparedByBlog, mrTemp.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, mrTemp.getSharedModifiedBy());
            userControl.setTextBlog(approvedByBlog, mrTemp.getApprovedBy());

            grdGRNDetail.getGrid().getStore().clear();
            grdGRNDetail.getGrid().getStore().addAll(mrTemp.getGrdGrnDetail());

            overHead.setOverHeadList(mrTemp.getOverHeadList(), mrTemp.getGrossAmount());
            lblAmtount.setText(DmClient.vNull(mrTemp.getGrossAmount(), "0.00"));
            ohlHandler.setDefault();
            gui.initialize(mrTemp.getGuiMdl());
            resetGstDetails();
            resetTaxSummary();
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
        manageDisplay(false);

    }

    private void topControlDelete(String strKey) {
        SrvAsync.invokeRPC().deleteRecord(strKey, gui.getConfigCode(), new AsyncCallback<DmResult<String>>() {

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

    private void setApproved(String docId, boolean isApprove) {
        try {
            if (DmShared.iNull(wtaCode).longValue() != 0) {
                walkThrough.show(wtaCode);
            } else {
                SrvAsync.invokeRPC().setApproval(DmClient.getLoginUserName(), isApprove, docId, new AsyncCallback<DmResult<String>>() {
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

    private void fillGrnDetail(List<KeyFieldExtended> listKF) {
        SrvAsync.invokeRPC().getGrnDetail(
                listKF,
                txtHSupplier.getTag(),
                location,
                txtSupplierBillDate.getText(),
                topControl.getMode().equalsIgnoreCase("New") ? "" : topControl.getDocId(),
                txtVoucherDate.getText(),
                new AsyncCallback<DmResult<List<GrdGrnDetail_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdGrnDetail_Mdl>> result) {
                if (result.isStatus()) {
                    grdGRNDetail.getGrid().getStore().clear();
                    grdGRNDetail.getGrid().getStore().addAll(result.getData());
                    grdGRNDetail.getGrid().getStore().add(new GrdGrnDetail_Mdl());
                    fillItemDetail(result.getData());
                    txtSupplierBillNo.setFocus(true);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void fillItemDetail(List<GrdGrnDetail_Mdl> listGRN) {
        for (int i = 0; i < listGRN.size(); i++) {
            GrdGrnDetail_Mdl detail_Mdl = listGRN.get(i);
            if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() > 0) {
                grdGRNDetail.getGrid().getStore().get(i).setAmount(DmClient.vNull(DmShared.vNull(grdGRNDetail.getGrid().getStore().get(i).getAcceptQty()).doubleValue() * DmShared.vNull(grdGRNDetail.getGrid().getStore().get(i).getRate()).doubleValue(), "0.00"));
                ohlHandler.setReference(detail_Mdl.getOHLListPurchaseOrder(), ohlHandler.getOverHeadList(i));
                ohlHandler.setITS(grdGRNDetail.getGrid().getStore().get(i).getLstITS(), i);
                ohlHandler.calculate(grdGRNDetail.getGrid().getStore().get(i).getAmount(), i);
                grdGRNDetail.getGrid().getStore().get(i).setOHLList(ohlHandler.getOverHeadList(i));

                String grossAcCode = DmShared.xNull(grdGRNDetail.getGrid().getStore().get(i).getGrossAcCode());
                String grossAcName = DmShared.xNull(grdGRNDetail.getGrid().getStore().get(i).getGrossAcName());
                if (!grossAcCode.trim().equals("") && !grossAcName.trim().equals("")) {
                    // Setting account to one GS + multiple [GS] unique ids
                    List<OverHeadLine_Mdl> lstOHLMdl = OverHeadLine_Shared.getOHByUniqueIds("GS", grdGRNDetail.getGrid().getStore().get(i).getOHLList());
                    for (OverHeadLine_Mdl ohlm : lstOHLMdl) {
                        if (ohlm != null) {
                            ohlm.setAccountCode(grossAcCode);
                            ohlm.setAccountName(grossAcName);
                        }
                    }

                    ohlHandler.calculate(grdGRNDetail.getGrid().getStore().get(i).getAmount(), i);
                }
            }
        }
        grdGRNDetail.commitChanges();

        txtHCalculateOn.setTag("1");
        txtHCalculateOn.setText("Accept Quantity");

        calculate();
    }

    private void setCardAcInOH() {
        List<OverHeadMA_Mdl> overHeadMAList;
        OverHeadMA_Mdl overHeadMA;

        List<OverHead_Mdl> lstOHMdl = OverHead_Shared.getOHByUniqueIds("PT", overHead.getOverHeadList());
        for (OverHead_Mdl ohMdl : lstOHMdl) {
            if (ohMdl != null) {
                overHeadMA = new OverHeadMA_Mdl();
                overHeadMA.setSharedKeyFieldCode(DmShared.iNull(txtHSupplier.getTag()).toString());
                overHeadMA.setSharedKeyFieldName(DmShared.xNull(txtHSupplier.getText()));

                overHeadMAList = new ArrayList<OverHeadMA_Mdl>();
                overHeadMAList.add(overHeadMA);

                ohMdl.setOverHeadMA(overHeadMAList);
            }
        }

        overHead.commit();
        calculate();
    }

    private void manageEvent() {
        btnApprovedBy.addClickHandler(this);
        btnAddOrder.addClickHandler(this);
        flwForm.addAttachHandler(this);
        txtSupplierBillDate.addBlurHandler(this);
        txtVoucherDate.addBlurHandler(this);
        mnuManageFile.addSelectionHandler(this);
    }

    private void manageDisplay(boolean flag) {
        txtVoucherNo.setEnabled(false);

        if (!departmentEditable) {
            txtHDepartment.setEnabled(false);
        }
        if (topControl.getMode().equalsIgnoreCase("Edit")) {
            txtHVoucherType.setEnabled(false);
            if (isPurchaseOrderExists) {
                txtHSupplier.setEnabled(false);
            }
        }

        flwAddOrder.clear();
        if (!topControl.getMode().equalsIgnoreCase("Browse")) {
            flwAddOrder.add(btnAddOrder);
        }
        
        cbShowStockItems.setEnabled(flag);
    }

    private void onAdd() {
        clear();
        manageDisplay(true);
        if (defaultValue.getIsContinuousNewMode()) {
            setDefaultValues();
        } else {
            setDefaultValuesWithDateReset();
        }
    }

    private void setDefaultValues() {
        txtVoucherDate.setText(defaultValue.getIsContinuousNewMode() ? DmShared.dNull(defaultValue.getNewVoucherDate()) : DmShared.dNull(defaultValue.getCurrentDate()));
        txtHVoucherType.setTag2nd(defaultValue.getIsContinuousNewMode() ? defaultValue.getNewVoucherTypeCode() : defaultValue.getVoucherTypeCode());
        txtHVoucherType.setTag(defaultValue.getIsContinuousNewMode() ? defaultValue.getNewVoucherTypeCode() : defaultValue.getVoucherTypeCode());
        txtHVoucherType.setText(defaultValue.getIsContinuousNewMode() ? defaultValue.getNewVoucherTypeName() : defaultValue.getVoucherTypeName());
        voucherCategory = defaultValue.getIsContinuousNewMode() ? defaultValue.getNewVoucherTypeCategory() : defaultValue.getVoucherCategory();
        guiTemplateCode = defaultValue.getIsContinuousNewMode() ? defaultValue.getNewGuiTemplateCode() : defaultValue.getGuiTemplateCode();
        txtHDepartment.setTag(defaultValue.getDepartmentCode());
        txtHDepartment.setText(defaultValue.getDepartmentName());
        txtHCalculateOn.setTag("1");
        txtHCalculateOn.setText("Accept Qty.");
        grdGRNDetail.getGrid().getStore().add(new GrdGrnDetail_Mdl());
        ohlHandler.initialize();
        gui.initializeViaTemplateCode();
        txtSupplierBillNo.setFocus(true);
    }

    private void setDefaultValuesWithDateReset() {
        SrvAsync.invokeRPC(true).getServerDateTime(new AsyncCallback<DmResult<String[]>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String[]> result) {
                if (result.isStatus() && result.getData() != null) {
                    defaultValue.setCurrentDate(result.getData()[0]);
                    setDefaultValues();
                }
            }
        });
    }

    private void clear() {
        selectedSupplierCode = "";
        partyTypeForEntry = "";
        partyType = "";
        supplierGstin = "";
        supplierGstinDate = "";
        location = "";
        rcmApplicable = "";
        wtaCode = "";
        voucherCategory = "";
        lblAmtount.setText("0.00");
        previous_Mdl = null;
        guiTemplateCode = "";
        isPurchaseOrderExists = false;
        cbShowStockItems.setValue(false);
        overHead.clear();
        gui.clear();
        flwAddOrder.clear();
        grdGRNDetail.getGrid().getStore().clear();
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        userControl.setTextBlog(approvedByBlog, "");
    }

    private void resetGstDetails() {

        //=====================================< Set Party Type For Entry >=================================//
        if (!DmShared.dNull(txtSupplierBillDate.getText()).trim().equals("")
                && !DmShared.dNull(supplierGstinDate).trim().equals("")
                && DmShared.convertDate(txtSupplierBillDate.getText()).getTime() < DmShared.convertDate(supplierGstinDate).getTime()) {
            partyTypeForEntry = DmEnum.DmPartyType.Unregistered.getId();
        } else {
            partyTypeForEntry = partyType;
        }

        //=====================================< Set RCM Applicable >=================================//
        if (!DmShared.dNull(defaultValue.getSiteGstinDate()).trim().equals("")
                && !DmShared.dNull(txtSupplierBillDate.getText()).trim().equals("")
                && DmShared.convertDate(defaultValue.getSiteGstinDate()).getTime() <= DmShared.convertDate(txtSupplierBillDate.getText()).getTime()
                && DmShared.xNull(defaultValue.getSiteRegistrationType()).equalsIgnoreCase(DmEnum.DmPartyType.Registered.getId())
                && DmShared.xNull(partyTypeForEntry).equalsIgnoreCase(DmEnum.DmPartyType.Unregistered.getId())) {
            rcmApplicable = "1";
        } else {
            rcmApplicable = "0";
        }

        //=====================================< Set Location >=================================//
        if (DmShared.iNull(defaultValue.getSiteStateCode()).intValue() != DmShared.iNull(txtHSupplier.getTag2nd()).intValue()) {
            location = String.valueOf(DmEnum.DmLocation.InterState.getId());
        } else {
            if (DmShared.iNull(defaultValue.getIsSiteStateUT()).intValue() == 1) {
                location = String.valueOf(DmEnum.DmLocation.UnionTerritory.getId());
            } else {
                location = String.valueOf(DmEnum.DmLocation.IntraState.getId());
            }
        }
    }

    private void calcAmount() {
        for (int i = 0; i < grdGRNDetail.getGrid().getStore().getAll().size(); i++) {
            GrdGrnDetail_Mdl detail_Mdl = grdGRNDetail.getGrid().getStore().get(i);
            if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() != 0) {
                if (DmShared.iNull(txtHCalculateOn.getTag()).intValue() == 0) {
                    detail_Mdl.setAmount(DmClient.vNull(DmShared.vNull(detail_Mdl.getChallanQty()).doubleValue() * DmShared.vNull(detail_Mdl.getRate()).doubleValue(), "0.00"));
                } else {
                    detail_Mdl.setAmount(DmClient.vNull(DmShared.vNull(detail_Mdl.getAcceptQty()).doubleValue() * DmShared.vNull(detail_Mdl.getRate()).doubleValue(), "0.00"));
                }
                grdGRNDetail.getGrid().getStore().get(i).setAmount(DmClient.vNull(detail_Mdl.getAmount(), "0.00"));
                ohlHandler.calculate(grdGRNDetail.getGrid().getStore().get(i).getAmount(), i);
                grdGRNDetail.getGrid().getStore().get(i).setOHLList(ohlHandler.getOverHeadList(i));
                grdGRNDetail.commitChanges();
            }
        }
        calculate();
    }

    private void calculate() {
        double netAmount = 0;
        for (GrdGrnDetail_Mdl detail_Mdl : grdGRNDetail.getGrid().getStore().getAll()) {
            if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() > 0) {
                netAmount += DmShared.vNull(detail_Mdl.getAmount());
            }
        }
        grdGRNDetail.commitChanges();
        lblAmtount.setText(DmClient.vNull(netAmount, "0.00"));
        overHead.calculate(DmClient.vNull(netAmount, "0.00"));
        calculateLandedValue();
        resetTaxSummary();
    }

    public void calculateLandedValue() {

        double landedValue = 0, totalGrossAmount = 0, lineGrossAmount = 0;

        landedValue = overHead.getLandedValue();

        for (GrdGrnDetail_Mdl detailMdl : grdGRNDetail.getGrid().getStore().getAll()) {
            if (detailMdl != null) {
                if (!DmShared.xNull(detailMdl.getSharedKeyFieldCode()).trim().equals("")) {
                    totalGrossAmount += DmShared.vNull(detailMdl.getAmount());
                }
            }
        }

        double landedSkuValue = 0, totalLandedSkuValue = 0, landedSkuRate = 0;
        GrdGrnDetail_Mdl lastLineMdl = null;
        for (GrdGrnDetail_Mdl lineMdl : grdGRNDetail.getGrid().getStore().getAll()) {
            if (lineMdl != null) {
                if (!DmShared.xNull(lineMdl.getSharedKeyFieldCode()).trim().equals("")) {
                    lineGrossAmount = DmShared.vNull(lineMdl.getAmount());

                    landedSkuValue = OverHead_Shared.getLandedValue(landedValue, totalGrossAmount, lineGrossAmount);
                    lineMdl.setLandedSkuValue(DmClient.vNull(landedSkuValue, "0.00"));

                    landedSkuRate = DmShared.vNull(lineMdl.getLandedSkuValue()) / DmShared.vNull(lineMdl.getSkuQty());
                    lineMdl.setLandedSkuRate(DmClient.vNull(landedSkuRate, "0.0000000"));

                    totalLandedSkuValue += DmShared.vNull(lineMdl.getLandedSkuValue());
                    lastLineMdl = lineMdl;
                }
            }
        }

        if (lastLineMdl != null) {
            landedSkuValue = DmShared.vNull(DmClient.vNull(lastLineMdl.getLandedSkuValue(), "0.00")) - (DmShared.vNull(DmClient.vNull(totalLandedSkuValue, "0.00")) - DmShared.vNull(DmClient.vNull(landedValue, "0.00")));
            lastLineMdl.setLandedSkuValue(DmClient.vNull(landedSkuValue, "0.00"));

            landedSkuRate = DmShared.vNull(lastLineMdl.getLandedSkuValue()) / DmShared.vNull(lastLineMdl.getSkuQty());
            lastLineMdl.setLandedSkuRate(DmClient.vNull(landedSkuRate, "0.0000000"));
        }

        grdGRNDetail.commitChanges();

    }

    private void resetTaxSummary() {
        List<List<OverHeadLine_Mdl>> ohlList = new ArrayList<List<OverHeadLine_Mdl>>();
        for (GrdGrnDetail_Mdl grdMdl : grdGRNDetail.getGrid().getStore().getAll()) {
            if (grdMdl != null && grdMdl.getOHLList() != null) {
                ohlList.add(grdMdl.getOHLList());
            }
        }
        taxSummary.reset(ohlList);
    }

    private void prnRecord(boolean printToPrinter) {
        HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
        hashMapFilter.put("docId", new FieldUDT_Mdl("Voucher No.", DmShared.xNull(txtVoucherNo.getText()), DmShared.xNull(topControl.getDocId())));
        new OpenSourceReport(DmShared.xNull(txtHVoucherType.getTag()), "", widgetName, widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
            @Override
            public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                DmClient.localSettingPrintMap(reportProperty, moduleName, widgetName, "Print");
                return reportProperty;
            }
        }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
    }

    @Override
    public void onSelection(SelectionEvent<Item> event) {
        if (event.getSource().equals(mnuManageFile)) {
            if (!topControl.getMode().equalsIgnoreCase("New")) {
                manageFile.show(topControl.getMode().equalsIgnoreCase("New") ? "" : topControl.getDocId(), "1", isAttachFile, isDeleteFile);
            }
        }
    }
}
