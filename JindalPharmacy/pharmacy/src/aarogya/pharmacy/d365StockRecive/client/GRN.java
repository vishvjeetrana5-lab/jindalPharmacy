package aarogya.pharmacy.d365StockRecive.client;

import aarogya.pharmacy.d365StockRecive.shared.*;
import aarogya.pharmacy.orderSearch.client.OrderSearch;
import aarogya.projectMenu.self.shared.ProjectRuleCode;
import aarogya.projectMenu.self.shared.pmPharmacy;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import dataman.dmToolkit.modifyReason.client.ModifyReason;
import dataman.dmToolkit.modifyReason.shared.ModifyReason_Mdl;
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
import dataman.dmToolkit.walkThrough.client.WalkThrough;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GRN extends GRN_Dsg implements AttachEvent.Handler, DmRootEvents, ClickHandler, BlurHandler, SelectionHandler<Item> {

    ServiceInvoker<GRN_SrvAsync> SrvAsync;
    protected DefaultValue_Mdl defaultValue;
    WalkThrough walkThrough;
    final OrderSearch orderSearch = new OrderSearch();
    final GRNGridEdit<GrdGrnDetail_Mdl, Grd_PrpAcs> grnDetailGridEdit = new GRNGridEdit(grdGRNDetail);
    private final DmFileManager manageFile = new DmFileManager(ProjectRuleCode.PURCHASE_CHALLAN_ENTRY.getRuleCode());
    ModifyReason_Mdl modifyReasonMdl;
    GRN_Mdl previous_Mdl = null;
    private boolean departmentEditable = false, isApprove = false, isPurchaseOrderExists = false, isAttachFile, isDeleteFile = false,
            singlePOFlowInPurchase = false;
    final ModifyReason modifyReason = new ModifyReason();
    String selectedSupplierCode, partyTypeForEntry, partyType, supplierGstin, supplierGstinDate, location;
    String rcmApplicable, wtaCode, voucherCategory, guiTemplateCode, copyGUIFromDocID, balancingAc, balancingAcName, lastTransactionDate;
    private String moduleName = new pmPharmacy().moduleName;
    final PharmacyUpdatePopup updatePopup = new PharmacyUpdatePopup();

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
        topControl.setUserPermission(menuPermission, new VoucherPermissionHandler(DmClient.getVoucherPermission(new String[]{new ProjectVoucherCategory().PHARMACY_PURCHASE_CHALLAN, new ProjectVoucherCategory().PHARMACY_PURCHASE_CHALLAN_UNREGISTERED})) {
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

                    if (DmShared.convertDate(lastTransactionDate).getTime() > DmShared.convertDate(previous_Mdl.getVoucherDate()).getTime()) {
                        new DmConfirmMessageBox("Back Date are not allowed. Are you sure? You can edit only header data.", new DialogHideEvent.DialogHideHandler() {

                            @Override
                            public void onDialogHide(DialogHideEvent event) {
                                if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                                    updatePopup.show(previous_Mdl, GRN.this);
                                } else {
                                    topControlMoveRec(topControl.getDocId());
                                }
                            }
                        }).show();
                    } else {
                        manageDisplay(true);
                        grdGRNDetail.getGrid().getStore().add(new GrdGrnDetail_Mdl());
                        txtChallanNo.setFocus(true);
                    }
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
                                showModifyReason();
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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(GRN.this);
            }
        });

        topControl.addOtherHandeller(new DmTopControl.eOther() {
            @Override
            public void otherClicked() {
                menu.show(topControl.getButton("Other"));
            }
        });
        topControl.setDisplay(true);
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
    }

    private void showModifyReason() {
        modifyReason.showNew(topControl.getMode(), topControl.getDocId(), "", "");
    }

    private void showOrderSearch(boolean isAutoShow) {
        orderSearch.show(
                topControl.getMode().equalsIgnoreCase("New") ? "" : topControl.getDocId(),
                txtHSupplier.getTag(),
                txtChallanDate.getText(),
                voucherCategory, isAutoShow, singlePOFlowInPurchase);
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
                    balancingAc = "";
                    balancingAcName = "";
                    voucherCategory = "";
                    guiTemplateCode = "";
                    if (topControl.getMode().equalsIgnoreCase("New")) {
                        txtHSupplier.setTag2nd("");
                        txtHSupplier.setTag("");
                        txtHSupplier.setText("");
                    }

                    if (blnStatus && t1Row != null) {
                        balancingAc = t1Row.getBalancingAc();
                        balancingAcName = t1Row.getBalancingAcName();
                        voucherCategory = t1Row.getVoucherCategory();
                        guiTemplateCode = t1Row.getGuiTemplateCode();
                    }
                    gui.initializeViaTemplateCode();
                }
            }
        });

        txtHSupplier.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHSupplier_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSupplier(voucherCategory, false, txtHVoucherType.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHSupplier_Mdl>>>() {

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
                if (topControl.getMode().equalsIgnoreCase("New")
                        && !DmShared.xNull(selectedSupplierCode).equalsIgnoreCase(DmShared.xNull(txtHSupplier.getTag()))) {
                    selectedSupplierCode = DmShared.xNull(txtHSupplier.getTag());
                }
                if (blnStatus && tRow != null) {
                    txtHSupplier.setTag2nd(DmShared.xNull(tRow.getStateCode()));
                    supplierGstin = DmShared.xNull(tRow.getGstin());
                    supplierGstinDate = DmShared.dNull(tRow.getGstinDate());
                    partyType = DmShared.xNull(tRow.getPartyType());
                    if (!DmShared.xNull(tRow.getGuiConfigCode()).equalsIgnoreCase("")) {
                        copyGUI(guiTemplateCode, tRow.getSharedKeyFieldCode());
                    }
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

        modifyReason.addCallBack(new ModifyReason.CallBack() {
            @Override
            public void onOk(ModifyReason_Mdl modifyReason) {
                modifyReasonMdl = modifyReason;
                if (topControl.getMode().equalsIgnoreCase("Browse") && DmShared.iNull(topControl.getDocId()).longValue() > 0) {
                    topControlDelete(topControl.getDocId(), txtVoucherNo.getText());
                } else {
                    topControlSave();
                }
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

        updatePopup.addCallBack(new PharmacyUpdatePopup.CallBack() {
            @Override
            public void onOk(GRN_Mdl mrTemp) {
                if (mrTemp != null) {
                    txtChallanNo.setText(DmShared.xNull(mrTemp.getChallanNo()));
                    txtChallanDate.setText(mrTemp.getChallanDate());
                    txtRemark.setText(mrTemp.getRemark());
                }
                topControlUpdate();
                txtChallanNo.setFocus(true);
            }

            @Override
            public void onClose() {
                topControlMoveRec(topControl.getDocId());
                txtChallanNo.setFocus(true);
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
        grnDetailGridEdit.addCallBack(new GRNGridEdit.CallBack() {

            @Override
            public void onOk(GrdGrnDetail_Mdl opdService) {
                grdGRNDetail.commitChanges();
                calculate();
            }

            @Override
            public void onDelete() {
                grdGRNDetail.commitChanges();
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
            new DmAlertMessageBox("Please select GRN Type.", txtHVoucherType).show();
            return;
        }
        if (DmShared.dNull(txtVoucherDate.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please fiil GRN Date.", txtVoucherDate).show();
            return;
        }
        if (DmShared.xNull(txtChallanNo.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please fill Ref. No.", txtChallanNo).show();
            return;
        }
        if (DmShared.dNull(txtChallanDate.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please fill Ref. Date.", txtChallanDate).show();
            return;
        }
        if (DmShared.xNull(txtHSupplier.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please select Supplier.", txtHSupplier).show();
            return;
        }
        if (DmShared.xNull(txtHDepartment.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please select Department.", txtHDepartment).show();
            return;
        }
        if ((DmShared.convertDate(txtVoucherDate.getText()).getTime() < (DmShared.convertDate(txtChallanDate.getText()).getTime()))) {
            new DmAlertMessageBox("GRN Date should be greater than or equal to Ref. Date.", txtVoucherDate).show();
            return;
        }
        String status = grdGRNDetail.getGrid().getStore().get(currentRowIndex).getIsStatus();
        String billedQty = grdGRNDetail.getGrid().getStore().get(currentRowIndex).getBilledQty();
        String mode = topControl.getMode();
        String msg = "";
        if (DmShared.iNull(status).intValue() == 1) {
            mode = "Browse";
            msg = "Record has been canceled, can't change record.";
        } else if (DmShared.iNull(status).intValue() == 2) {
            mode = "Browse";
            msg = "Record has been completed, can't change record.";
        } else if (DmShared.vNull(billedQty) > 0) {
            mode = "Browse";
            msg = "Record has been used, can't change record.";
        }
        if (mode.trim().equalsIgnoreCase("Browse")) {
            if (!topControl.getMode().equalsIgnoreCase("Browse") && !msg.equalsIgnoreCase("")) {
                new DmAlertMessageBox(msg, (DmTextBox) null).show();
            }
            grnDetailGridEdit.show(currentRowIndex, false, false, false, this, defaultValue.isInventoryAccountingActive());
        } else {
            grnDetailGridEdit.show(currentRowIndex, true, true, true, this, defaultValue.isInventoryAccountingActive());
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
            getD365Data();
        } else if (event.getSource() == btnStockIss) {
            getStockIssueData();
        }

    }

    private void getD365Data() {
        SrvAsync.invokeRPC(true).getD65DataDeatails(new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHDepartment).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (!result.getData().equalsIgnoreCase("")) {
                    new DmAlertMessageBox("GRN Stock Receive completed with errors.Failed DocId(s):'" + result.getData() + "'", (DmTextBox) null).show();
                } else if (result.getData().equalsIgnoreCase("")) {
                    new DmAlertMessageBox("GRN Stock Receive completed successfully.", (DmTextBox) null).show();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHDepartment).show();
                }
            }
        });
    }

    private void getStockIssueData() {
        SrvAsync.invokeRPC(true).getStockIssueData(new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHDepartment).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (!result.getData().equalsIgnoreCase("")) {
                    new DmAlertMessageBox("GRN Stock Consumption completed with errors.Failed DocId(s):'" + result.getData() + "'", (DmTextBox) null).show();
                } else if (result.getData().equalsIgnoreCase("")) {
                    new DmAlertMessageBox("GRN Stock Consumption completed successfully.", (DmTextBox) null).show();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHDepartment).show();
                }
            }
        });
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtVoucherDate) {
            txtVoucherDate.setText(DmClient.returnDate(txtVoucherDate.getText()));
            if (STClient.isFutureDate(txtVoucherDate.getText())) {
                txtVoucherDate.setText("");
                new DmAlertMessageBox("GRN Date should be less than or equal to current date.", txtVoucherDate).show();
                return;
            }
            if (topControl.getMode().equalsIgnoreCase("New")
                    && !DmShared.xNull(txtVoucherDate.getText()).equalsIgnoreCase(txtVoucherDate.getTag())) {
                txtVoucherDate.setTag(txtVoucherDate.getText());
            }
        } else if (event.getSource() == txtChallanDate) {
            txtChallanDate.setText(DmClient.returnDate(txtChallanDate.getText()));
            if (topControl.getMode().equalsIgnoreCase("New")
                    && !DmShared.xNull(txtChallanDate.getText()).equalsIgnoreCase(txtChallanDate.getTag())) {
                txtChallanDate.setTag(txtChallanDate.getText());
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

    public GRN(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<GRN_SrvAsync>(GWT.create(GRN_Srv.class), GRN.class.getName());
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("A", "*"));
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("E", "*"));
//        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("D", "*"));
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("S", "*"));

        walkThrough = new WalkThrough(SrvAsync);
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        findEvents();
        textHelpEvents();
        gridEvents();
        manageEvent();
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
                    gui.initializeViaTemplateCode();
                    swi.setWidget(GRN.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void topControlSave() {
        GRN_Mdl mrTemp;

        try {
            defaultValue.setNewVoucherTypeCode(txtHVoucherType.getTag());
            defaultValue.setNewVoucherTypeName(txtHVoucherType.getText());
            defaultValue.setNewVoucherTypeCategory(voucherCategory);
            defaultValue.setNewBalancingAc(balancingAc);
            defaultValue.setNewBalancingAcName(balancingAcName);
            defaultValue.setNewVoucherDate(txtVoucherDate.getText());
            defaultValue.setNewGuiTemplateCode(guiTemplateCode);
            calculate();
            mrTemp = topControlSaveInitialize();
            GRN_Vld.chkSave(mrTemp);
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

    private GRN_Mdl topControlSaveInitialize() {
        GRN_Mdl mrTemp = new GRN_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
            mrTemp.getRecIdGRN().setCounter(previous_Mdl.getRecIdGRN().getCounter());
            mrTemp.getRecIdGRN().setPrefix(previous_Mdl.getRecIdGRN().getPrefix());
            mrTemp.getRecIdGRN().setRecIdValue(previous_Mdl.getRecIdGRN().getRecIdValue());
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
        mrTemp.setChallanNo(DmShared.xNull(txtChallanNo.getText()).trim());
        mrTemp.setChallanDate(DmShared.dNull(txtChallanDate.getText()));
        mrTemp.setRemark(DmShared.xNull(txtRemark.getText()).trim());

        mrTemp.setPlaceOfSupply(DmShared.xNull(defaultValue.getSiteStateCode()).trim());
        mrTemp.setRcmApplicable(rcmApplicable);
        mrTemp.setPartyTypeForEntry(partyTypeForEntry);
        mrTemp.setPartyType(partyType);
        mrTemp.setDefaultValueMdl(defaultValue);
        mrTemp.getGrdGrnDetail().clear();
        mrTemp.getGrdGrnDetail().addAll(grdGRNDetail.getGrid().getStore().getAll());

        mrTemp.setGuiMdl(gui.getGUI_Mdl());
        mrTemp.setGuiConfigCode(gui.getConfigCode());
        mrTemp.setNetAmount(DmShared.xNull(txtNetAmount.getText()));
        if (modifyReasonMdl != null) {
            mrTemp.setModifyReasonMdl(modifyReasonMdl);
        }

        //=================== Final Values ===================//
        mrTemp.setGrossAmount(DmShared.xNull(txtGoodsAmount.getTag2nd()));
        mrTemp.setGoodsGrossAmt(DmShared.xNull(txtGoodsAmount.getText()));
        mrTemp.setServiceGrossAmt(DmShared.xNull(txtServiceAmount.getText()));
        mrTemp.setDiscount(DmShared.xNull(txtDiscount.getText()));
        mrTemp.setGoodsTaxableAmt(DmShared.xNull(txtGoodsAmount.getTag()));
        mrTemp.setServiceTaxableAmt(DmShared.xNull(txtServiceAmount.getTag()));
        mrTemp.setTaxableAmt(DmShared.xNull(txtTaxableAmt.getText()));
        mrTemp.setSgstUgst(DmShared.xNull(txtSgstUgst.getText()));
        mrTemp.setSgst(DmShared.xNull(txtSGST.getText()));
        mrTemp.setUgst(DmShared.xNull(txtUGST.getText()));
        mrTemp.setCgst(DmShared.xNull(txtCGST.getText()));
        mrTemp.setIgst(DmShared.xNull(txtIGST.getText()));
        mrTemp.setSubTotal(DmShared.xNull(txtSubTotal.getText()));
        mrTemp.setFinalDiscountPer(DmShared.xNull(txtFinalDiscountPer.getText()));
        mrTemp.setFinalDiscount(DmShared.xNull(txtFinalDiscount.getText()));
        mrTemp.setTotalAmt(DmShared.xNull(txtTotalAmount.getText()));
        mrTemp.setRoundOffAmount(DmShared.xNull(txtRoundoff.getText()));
        mrTemp.setNetAmount(DmShared.xNull(txtNetAmount.getText()));
        mrTemp.setLandedValue(DmShared.xNull(txtLandedValue.getText()));

        //Accounts               
        mrTemp.setFinalDiscountAcCode(DmShared.xNull(txtFinalDiscount.getTag()));
        mrTemp.setRoundOffAcCode(DmShared.xNull(txtRoundoff.getTag()));

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
                new DmAlertMessageBox(errorMessage, txtChallanNo).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtChallanDate).show();
                break;
            case 600:
                new DmAlertMessageBox(errorMessage, txtHDepartment).show();
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
                        ((CellSelectionModel) grdGRNDetail.getGrid().getSelectionModel()).selectCell(gridRowIndex, 1);
                    }
                }).show();
                break;
            case 800:
                new DmAlertMessageBox(errorMessage, new HideEvent.HideHandler() {
                    @Override
                    public void onHide(HideEvent event) {
                        ((CellSelectionModel) grdGRNDetail.getGrid().getSelectionModel()).selectCell(0, 1);
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
        defaultValue.setIsContinuousNewMode(false);
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<GRN_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<GRN_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<GRN_Mdl> result) {
        clear();
        if (result.isStatus()) {
            GRN_Mdl mrTemp = result.getData();
            previous_Mdl = result.getData().clone();

            topControl.setDocId(mrTemp.getSharedKeyFieldCode());
            txtVoucherNo.setText(mrTemp.getSharedKeyFieldName());
            txtVoucherNo.setTag(mrTemp.getU_EntDt_LatestLine());
            txtVoucherDate.setText(mrTemp.getVoucherDate());
            txtVoucherDate.setTag(txtVoucherDate.getText());
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
            txtChallanDate.setText(mrTemp.getChallanDate());
            txtChallanNo.setText(mrTemp.getChallanNo());
            txtRemark.setText(mrTemp.getRemark());

            selectedSupplierCode = mrTemp.getSupplierTag();
            supplierGstin = mrTemp.getSupplierGstin();
            supplierGstinDate = mrTemp.getSupplierGstinDate();
            rcmApplicable = mrTemp.getRcmApplicable();
            partyTypeForEntry = mrTemp.getPartyTypeForEntry();
            partyType = mrTemp.getPartyType();
            isPurchaseOrderExists = mrTemp.getIsPurchaseOrderExists();
            lastTransactionDate = mrTemp.getLastTransactionDate();

            userControl.setTextBlog(preparedByBlog, mrTemp.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, mrTemp.getSharedModifiedBy());
            userControl.setTextBlog(approvedByBlog, mrTemp.getApprovedBy());

            grdGRNDetail.getGrid().getStore().clear();
            grdGRNDetail.getGrid().getStore().addAll(mrTemp.getGrdGrnDetail());

            //=================== Final Values ===================//
            txtGoodsAmount.setTag2nd(DmClient.vNull(mrTemp.getGrossAmount(), "0.00"));
            txtGoodsAmount.setText(DmClient.vNull(mrTemp.getGoodsGrossAmt(), "0.00"));
            txtServiceAmount.setText(DmClient.vNull(mrTemp.getServiceGrossAmt(), "0.00"));
            txtDiscount.setText(DmClient.vNull(mrTemp.getDiscount(), "0.00"));
            txtGoodsAmount.setTag(DmClient.vNull(mrTemp.getGoodsTaxableAmt(), "0.00"));
            txtServiceAmount.setTag(DmClient.vNull(mrTemp.getServiceTaxableAmt(), "0.00"));
            txtTaxableAmt.setText(DmClient.vNull(mrTemp.getTaxableAmt(), "0.00"));
            txtSgstUgst.setText(DmClient.vNull(mrTemp.getSgstUgst(), "0.00"));
            txtSGST.setText(DmClient.vNull(mrTemp.getSgst(), "0.00"));
            txtUGST.setText(DmClient.vNull(mrTemp.getUgst(), "0.00"));
            txtCGST.setText(DmClient.vNull(mrTemp.getCgst(), "0.00"));
            txtIGST.setText(DmClient.vNull(mrTemp.getIgst(), "0.00"));
            txtSubTotal.setText(DmClient.vNull(mrTemp.getSubTotal(), "0.00"));
            txtFinalDiscountPer.setText(DmClient.vNull(mrTemp.getFinalDiscountPer(), "0.00"));
            txtFinalDiscount.setText(DmClient.vNull(mrTemp.getFinalDiscount(), "0.00"));
            txtTotalAmount.setText(DmClient.vNull(mrTemp.getTotalAmt(), "0.00"));
            txtRoundoff.setText(DmClient.vNull(mrTemp.getRoundOffAmount(), "0.00"));
            txtNetAmount.setText(DmClient.vNull(mrTemp.getNetAmount(), "0.00"));
            txtLandedValue.setText(DmClient.vNull(mrTemp.getLandedValue(), "0.00"));
            if (DmShared.vNull(mrTemp.getSgst()).doubleValue() > 0) {
                lblSgstUgst.setText(DmEnum.GSTS_Head.SGST.getName());
            } else if (DmShared.vNull(mrTemp.getUgst()).doubleValue() > 0) {
                lblSgstUgst.setText(DmEnum.GSTS_Head.UGST.getName());
            }

            //=================== ************* ===================//
            //Accounts 
            txtRoundoff.setTag(DmShared.xNull(mrTemp.getRoundOffAcCode()));
            txtFinalDiscount.setTag(DmShared.xNull(mrTemp.getFinalDiscountAcCode()));
            gui.initialize(mrTemp.getGuiMdl());
            resetGstDetails();
            resetTaxSummary();
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
        manageDisplay(false);
    }

    private void topControlDelete(String strKey, String recId) {
        SrvAsync.invokeRPC().deleteRecord(strKey, recId, gui.getConfigCode(), modifyReasonMdl, new AsyncCallback<DmResult<String>>() {

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
        }
        );
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
        SrvAsync.invokeRPC().getGrnDetail(listKF, txtHSupplier.getTag(), location, txtChallanDate.getText(), topControl.getMode().equalsIgnoreCase("New") ? "" : topControl.getDocId(), txtVoucherDate.getText(), new AsyncCallback<DmResult<List<GrdGrnDetail_Mdl>>>() {
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
                    fillMedicineDetail(result.getData());
                    txtChallanNo.setFocus(true);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void fillMedicineDetail(List<GrdGrnDetail_Mdl> listGRN) {
        for (int i = 0; i < listGRN.size(); i++) {
            GrdGrnDetail_Mdl detail_Mdl = listGRN.get(i);
            if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() > 0) {
                grdGRNDetail.getGrid().getStore().get(i).setAmount(DmClient.vNull(DmShared.vNull(grdGRNDetail.getGrid().getStore().get(i).getAcceptQty()).doubleValue() * DmShared.vNull(grdGRNDetail.getGrid().getStore().get(i).getRate()).doubleValue(), "0.00"));

                String grossAcCode = DmShared.xNull(grdGRNDetail.getGrid().getStore().get(i).getInventoryAc());
                String grossAcName = DmShared.xNull(grdGRNDetail.getGrid().getStore().get(i).getInventoryAcName());
                if (!grossAcCode.trim().equals("") && !grossAcName.trim().equals("")) {
                }
            }
        }
        grdGRNDetail.commitChanges();
        calculate();
    }

    private void manageEvent() {
        btnApprovedBy.addClickHandler(this);
        btnAddOrder.addClickHandler(this);
        btnStockIss.addClickHandler(this);
        flwForm.addAttachHandler(this);
        txtChallanDate.addBlurHandler(this);
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
        if (flag) {
            flwAddOrder.add(btnAddOrder);
        }
    }

    private void onAdd() {
        clear();
        manageDisplay(true);
        btnAddOrder.setVisible(true);
        if (defaultValue.getIsContinuousNewMode()) {
            setDefaultValues();
        } else {
            setDefaultValuesWithDateReset();
        }
    }

    private void setDefaultValues() {
        txtVoucherDate.setText(defaultValue.getIsContinuousNewMode() ? DmShared.dNull(defaultValue.getNewVoucherDate()) : DmShared.dNull(defaultValue.getCurrentDate()));
        txtVoucherDate.setTag(txtVoucherDate.getText());
        txtHVoucherType.setTag2nd(defaultValue.getIsContinuousNewMode() ? defaultValue.getNewVoucherTypeCode() : defaultValue.getVoucherTypeCode());
        txtHVoucherType.setTag(defaultValue.getIsContinuousNewMode() ? defaultValue.getNewVoucherTypeCode() : defaultValue.getVoucherTypeCode());
        txtHVoucherType.setText(defaultValue.getIsContinuousNewMode() ? defaultValue.getNewVoucherTypeName() : defaultValue.getVoucherTypeName());
        voucherCategory = defaultValue.getIsContinuousNewMode() ? defaultValue.getNewVoucherTypeCategory() : defaultValue.getVoucherCategory();
        guiTemplateCode = defaultValue.getIsContinuousNewMode() ? defaultValue.getNewGuiTemplateCode() : defaultValue.getGuiTemplateCode();
        balancingAc = defaultValue.getIsContinuousNewMode() ? defaultValue.getNewBalancingAc() : defaultValue.getBalancingAc();
        balancingAcName = defaultValue.getIsContinuousNewMode() ? defaultValue.getNewBalancingAcName() : defaultValue.getBalancingAcName();
        if (DmShared.iNull(defaultValue.getDefaultPurchaseDepartment()).intValue() == 0) {
            txtHDepartment.setTag(defaultValue.getDepartmentCode());
            txtHDepartment.setText(defaultValue.getDepartmentName());
        } else {
            txtHDepartment.setTag(defaultValue.getDefaultPurchaseDepartment());
            txtHDepartment.setText(defaultValue.getDefaultPurchaseDepartmentText());
        }
        singlePOFlowInPurchase = defaultValue.isSinglePOFlowInPurchase() ? true : false;
        gui.initializeViaTemplateCode();
        txtChallanNo.setFocus(true);
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
        guiTemplateCode = "";
        isPurchaseOrderExists = false;
        previous_Mdl = null;
        gui.clear();
        flwAddOrder.clear();
        grdGRNDetail.getGrid().getStore().clear();
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        userControl.setTextBlog(approvedByBlog, "");
        copyGUIFromDocID = "";
    }

    private void resetGstDetails() {

        //=====================================< Set Party Type For Entry >=================================//
        if (!DmShared.dNull(txtChallanDate.getText()).trim().equals("")
                && !DmShared.dNull(supplierGstinDate).trim().equals("")
                && DmShared.convertDate(txtChallanDate.getText()).getTime() < DmShared.convertDate(supplierGstinDate).getTime()) {
            partyTypeForEntry = DmEnum.DmPartyType.Unregistered.getId();
        } else {
            partyTypeForEntry = partyType;
        }

        //=====================================< Set RCM Applicable >=================================//
        if (!DmShared.dNull(defaultValue.getSiteGstinDate()).trim().equals("")
                && !DmShared.dNull(txtChallanDate.getText()).trim().equals("")
                && DmShared.convertDate(defaultValue.getSiteGstinDate()).getTime() <= DmShared.convertDate(txtChallanDate.getText()).getTime()
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

    private void calculate() {
        double netAmount = 0;
        for (GrdGrnDetail_Mdl detail_Mdl : grdGRNDetail.getGrid().getStore().getAll()) {
            if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() > 0) {
                netAmount += DmShared.vNull(detail_Mdl.getAmount());
            }
        }
        grdGRNDetail.commitChanges();
        calculateLandedValue();
        resetTaxSummary();
    }

    public void calculateLandedValue() {
        double landedValue = 0, totalGrossAmount = 0, lineGrossAmount = 0;
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

    }

    private void prnRecord(boolean printToPrinter) {
        HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
        hashMapFilter.put("docId", new FieldUDT_Mdl("GRN No.", DmShared.xNull(txtVoucherNo.getText()), DmShared.xNull(topControl.getDocId())));
        new OpenSourceReport(txtHVoucherType.getTag(), "", "Goods Receive Note", widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
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

    private void copyGUI(String guiCodeFrom, final String copyFromDocId) {
        if (DmShared.iNull(guiCodeFrom).longValue() != 0) {
            gui.copyGUI(guiCodeFrom, copyFromDocId, null);
        }
    }

    private GRN_Mdl topControlUpdate() {
        GRN_Mdl mrTemp = new GRN_Mdl();
        try {
            if (DmShared.convertDate(lastTransactionDate).getTime() > DmShared.convertDate(previous_Mdl.getVoucherDate()).getTime()) {

                if (topControl.getMode().equalsIgnoreCase("Edit")) {

                    mrTemp.setSharedKeyFieldCode(topControl.getDocId());
                    mrTemp.setChallanNo(DmShared.xNull(updatePopup.txtSupplierBillNo.getText()).trim());
                    mrTemp.setChallanDate(DmShared.dNull(updatePopup.txtSupplierBillDate.getText()));
                    mrTemp.setRemark(DmShared.xNull(updatePopup.txtRemark.getText()).trim());
                    mrTemp.setVoucherDate(DmShared.dNull(txtVoucherDate.getText()));
                    mrTemp.setLastTransactionDate(lastTransactionDate);

                    SrvAsync.invokeRPC().updateRecord(mrTemp, isApprove, new AsyncCallback<DmResult<String>>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                        }

                        @Override
                        public void onSuccess(DmResult<String> result) {
                            if (result.isStatus()) {
                                topControl.setDocId(result.getData());
                                if (topControl.getMode().equalsIgnoreCase("Edit")) {
                                    topControlMoveRec(topControl.getDocId());
                                    btnAddOrder.setVisible(false);
                                }
                            } else {
                                topControlSaveError(result.getErrorNumber(), result.getValue1(), result.getValue2(), result.getErrorMessage());
                            }
                        }
                    });
                }
            }

        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getErrorSource(), e.getErrorSourceCode(), e.getMessage());
        }
        return mrTemp;
    }
}
