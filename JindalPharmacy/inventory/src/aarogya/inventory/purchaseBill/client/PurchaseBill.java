package aarogya.inventory.purchaseBill.client;

import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
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
import dataman.dmToolkit.walkThrough.client.WalkThrough;
import java.util.List;

import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmBase.self.shared.DmEnum.DmSortType;
import dataman.dmToolkit.dmGUI.client.DmGUI;
import dataman.dmToolkit.dmGUI.shared.DmGUIControl_Mdl;
import dataman.dmToolkit.dmGUI.shared.DmGUI_Mdl;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import dataman.dmToolkit.overHead.shared.OverHead_Shared;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Mdl;
import java.util.ArrayList;
import java.util.HashMap;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.inventory.grnSearch.client.GrnSearch;
import aarogya.inventory.purchaseBill.shared.DefaultValue_Mdl;
import aarogya.inventory.purchaseBill.shared.Find_Mdl;
import aarogya.inventory.purchaseBill.shared.GrdBillOtherDetails_Mdl;
import aarogya.inventory.purchaseBill.shared.GrdItemDetail_Mdl;
import aarogya.inventory.purchaseBill.shared.PurchaseBill_Mdl;
import aarogya.inventory.purchaseBill.shared.PurchaseBill_Srv;
import aarogya.inventory.purchaseBill.shared.PurchaseBill_SrvAsync;
import aarogya.inventory.purchaseBill.shared.PurchaseBill_Vld;
import aarogya.inventory.purchaseBill.shared.TxtHHeaderGrnNo_Mdl;
import aarogya.inventory.purchaseBill.shared.TxtHSupplier_Mdl;
import aarogya.inventory.purchaseBill.shared.TxtHVoucherType_Mdl;
import aarogya.selfToolkit.modifyReason.client.ModifyReason;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.serviceGrid.client.ServiceGrid;
import aarogya.selfToolkit.serviceGrid.shared.ServiceDetail_Mdl;
import aarogya.selfToolkit.serviceGrid.shared.ServiceGrid_Vld;
import aarogya.selfToolkit.stStatic.client.STClient;
import com.google.gwt.user.client.ui.Button;
import dataman.dmToolkit.tdsCategoryPopup.client.TdsCategoryPopup;
import dataman.dmToolkit.tdsCategoryPopup.shared.TdsCategoryPopup_Mdl;

public final class PurchaseBill extends PurchaseBill_Dsg implements BlurHandler, AttachEvent.Handler, DmRootEvents, ClickHandler {

    private ServiceInvoker<PurchaseBill_SrvAsync> srvAsync;
    protected DefaultValue_Mdl defaultValue;
    private final PurchaseBillGridEdit<GrdItemDetail_Mdl, GridItemDetail_PrpAcs> itemDetailGridEdit = new PurchaseBillGridEdit(gridItemDetail, this);
    private final GrnSearch grnSearch = new GrnSearch();
    private ModifyReason_Mdl modifyReasonMdl;
    private PurchaseBill_Mdl previous_Mdl = null;
    private final ModifyReason modifyReason = new ModifyReason();
    private WalkThrough walkThrough;
    String selectedSupplierCode, partyTypeForEntry, partyType, supplierGstin, supplierGstinDate, location,
            wtaCode, voucherCategory, balancingAc, guiTemplateCode, lastTransactionDate;
    private boolean dateEditable = false, isApprove = false, grnDashboardFlag = false, singlePOFlowInPurchase = false;
    List<KeyFieldExtended> lstGrn = null;
    private Boolean isPrevFYData = false, updateItemPurchaseRate = false;
    final InventoryUpdatePopup updatePopup = new InventoryUpdatePopup();

    private final TdsCategoryPopup tdsCategoryPp = new TdsCategoryPopup();
    private TdsCategoryPopup_Mdl tdsCategoryPopup_Mdl;
    boolean saveModeActive = false;       // To restrict recreation of tds model on save event

    private void topControlEvents(UserMenu menuPermission) {

        topControl.setTitle(this.asWidget().getTitle());
        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);
        for (String controlPermission : menuPermission.getControlPermission()) {
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(approvedByBlog)) {
                isApprove = true;
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(dateEditableBlog)) {
                dateEditable = true;
            }
        }

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

        topControl.setUserPermission(menuPermission, new VoucherPermissionHandler(DmClient.getVoucherPermission(new String[]{new ProjectVoucherCategory().PURCHASE_BILL, new ProjectVoucherCategory().PURCHASE_BILL_UNREGISTERED})) {
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
                txtHVoucherType.setFocus(true);
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                if (DmShared.xNull(userControl.getTextBlog(approvedByBlog)).equalsIgnoreCase("")) {
//                    manageDisplay(true);
//
//                    gridItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
//                    serviceDetailGrid.getServiceGrid().getGrid().getStore().add(new ServiceDetail_Mdl());
//                    serviceDetailGrid.initialize(setServiveDefaultValue());
//                    syncronizeLineService(false, true);
//                    txtSupplierBillNo.setFocus(true);
//                } 
//                if (DmShared.xNull(userControl.getTextBlog(approvedByBlog)).equalsIgnoreCase("")) {

                    if (DmShared.convertDate(lastTransactionDate).getTime() > DmShared.convertDate(previous_Mdl.getVoucherDate()).getTime()) {
                        new DmConfirmMessageBox("Back Date are not allowed. Are you sure? You can edit only header data. ", new DialogHideEvent.DialogHideHandler() {

                            @Override
                            public void onDialogHide(DialogHideEvent event) {
                                if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                                    updatePopup.show(previous_Mdl, PurchaseBill.this);
                                } else {
                                    topControlMoveRec(topControl.getDocId());
                                }
                            }
                        }).show();

//                    txtHSupplier.setFocus(true);
                    } else {
                        manageDisplay(true);

                        gridItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
                        serviceDetailGrid.getServiceGrid().getGrid().getStore().add(new ServiceDetail_Mdl());
                        serviceDetailGrid.initialize(setServiveDefaultValue());
                        syncronizeLineService(false, true);
                        txtSupplierBillNo.setFocus(true);
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
                if (DmShared.xNull(topControl.getMode()).equalsIgnoreCase("Edit")) {
//                    new DmConfirmMessageBox("Do You want to update purchase rate in master.", new DialogHideEvent.DialogHideHandler() {
//
//                        @Override
//                        public void onDialogHide(DialogHideEvent event) {
//                            if (event.getHideButton() == Dialog.PredefinedButton.YES) {
//                                updateItemPurchaseRate=true;
//                                showModifyReason();
//                            }else{
//                                updateItemPurchaseRate=false;
//                                showModifyReason();
//                            }
//                        }
//                    }).show();
                    showModifyReason();
                } else {
                    topControlSave();
//                    updateItemPurchaseRate=true;
                }
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(PurchaseBill.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void showModifyReason() {
        modifyReason.show(topControl.getMode(), topControl.getDocId());
    }

    private void textHelpEvents() {

        txtHVoucherType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHVoucherType_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHVoucher(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHVoucherType_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHVoucherType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHVoucherType_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHVoucherType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHVoucherType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHVoucherType_Mdl t1Row) {
                if (!DmShared.xNull(txtHVoucherType.getTag()).equalsIgnoreCase(DmShared.xNull(txtHVoucherType.getTag2nd()))) {
                    txtHVoucherType.setTag2nd(txtHVoucherType.getTag());

                    //Clear Related Values
                    balancingAc = "";
                    voucherCategory = "";
                    guiTemplateCode = "";

                    txtHSupplier.setTag2nd("");
                    txtHSupplier.setTag("");
                    txtHSupplier.setText("");
                    clearGrid();

                    //Set Related Values
                    if (blnStatus && t1Row != null) {
                        balancingAc = t1Row.getBalancingAc();
                        voucherCategory = t1Row.getVoucherCategory();
                        guiTemplateCode = t1Row.getGuiTemplateCode();

                        //set RCM
                        setRCM(t1Row.getVoucherCategory());
                    }

                    //Reset GUI
                    gui.initializeViaTemplateCode();
                }
            }
        });

        txtHSupplier.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHSupplier_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSupplier(voucherCategory, txtHVoucherType.getTag(), txtVoucherDate.getText(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHSupplier_Mdl>>>() {

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
                fillSupplierDetails(blnStatus, tRow);

            }
        });

        txtHCalculateOn.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHCalcOn(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
                    calculateGrid();
                }
            }
        });

        modifyReason.addCallBack(new ModifyReason.CallBack() {

            @Override
            public void onOk(ModifyReason_Mdl modifyReason) {
                try {
                    PurchaseBill.this.modifyReasonMdl = modifyReason;
                    if (topControl.getMode().equalsIgnoreCase("Browse") && DmShared.iNull(topControl.getDocId()).longValue() > 0) {
                        topControlDelete(topControl.getDocId());
                    } else {
                        topControlSave();
                    }
                } catch (Exception ex) {
                    new DmAlertMessageBox(ex.getMessage(), (DmTextBox) null).show();
                }
            }
        });

        walkThrough.addCallBack(new WalkThrough.CallBack() {
            @Override
            public void onClose() {
                topControlMoveRec(topControl.getDocId());
            }
        });

        grnSearch.addCallBack(new GrnSearch.CallBack() {

            @Override
            public void ok(List<KeyFieldExtended> listKF) {
                clearGrid();
                fillGrnDetail(listKF, "");
                txtSupplierBillNo.setFocus(true);
            }

            @Override
            public void close() {
                txtHSupplier.setFocus(true);
            }
        });

        txtHRCMApplicable.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHRCMApplicable(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHRCMApplicable).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHRCMApplicable.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHRCMApplicable).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (!DmShared.xNull(txtHRCMApplicable.getTag2nd()).equalsIgnoreCase(txtHRCMApplicable.getTag())) {
                    txtHRCMApplicable.setTag2nd(txtHRCMApplicable.getTag());
                    clearGrid();
                }
            }
        });

        txtHGrnNo.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<TxtHHeaderGrnNo_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHHeaderGrnNo(DmShared.xNull(voucherCategory), DmShared.xNull(txtHSupplier.getTag()),
                        DmShared.xNull(txtSupplierBillDate.getText()),
                        topControl.getMode().equalsIgnoreCase("New") ? "" : topControl.getDocId(),
                        helper, blnToEndOfFile,
                        new AsyncCallback<DmResultPageing<List<TxtHHeaderGrnNo_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHGrnNo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHHeaderGrnNo_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHGrnNo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHGrnNo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
                fillGrnDetail(null, DmShared.xNull(keyCode));
            }

        });

        updatePopup.addCallBack(new InventoryUpdatePopup.CallBack() {
            @Override
            public void onOk(PurchaseBill_Mdl mrTemp) {
                if (mrTemp != null) {
                    txtSupplierBillNo.setText(DmShared.xNull(mrTemp.getSupplierBillNo()));
                    txtSupplierBillDate.setText(mrTemp.getSupplierBillDate());
                    txtPaymentDueDate.setText(mrTemp.getPaymentDueDate());
                    txtRemark.setText(mrTemp.getRemark());
                }
                topControlUpdate();
                txtSupplierBillNo.setFocus(true);
            }

            @Override
            public void onClose() {
                topControlMoveRec(topControl.getDocId());
                txtSupplierBillNo.setFocus(true);
            }
        });

    }

    private void gridEvents() {
        itemDetailGridEdit.addCallBack(new PurchaseBillGridEdit.CallBack() {

            @Override
            public void onOk(GrdItemDetail_Mdl opdService) {
                gridItemDetail.commitChanges();
                syncronizeLineService(true, true);
            }

            @Override
            public void onDelete() {
                gridItemDetail.commitChanges();
                syncronizeLineService(true, true);
            }
        });

        gridItemDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showQuotationEntryGridEdit(event.getRowIndex());
            }
        });

        gridItemDetail.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showQuotationEntryGridEdit(gridItemDetail.getGrid().getStore().indexOf(gridItemDetail.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

        serviceDetailGrid.addCallBack(new ServiceGrid.CallBack() {
            @Override
            public void onOk(ServiceDetail_Mdl serviceDetail_Mdl) {
                serviceDetailGrid.getServiceGrid().commitChanges();
                syncronizeLineService(true, true);
            }

            @Override
            public void onDelete() {
                serviceDetailGrid.getServiceGrid().commitChanges();
                syncronizeLineService(true, true);
            }
        }
        );

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

    private void showQuotationEntryGridEdit(int currentRowIndex) {
        if (DmShared.xNull(txtHVoucherType.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please select Voucher Type", txtHVoucherType).show();
            return;
        }
        if (DmShared.dNull(txtVoucherDate.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please fill Voucher Date", txtVoucherDate).show();
            return;
        }
        if (DmShared.xNull(txtHSupplier.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please select Supplier", txtHSupplier).show();
            return;
        }
        if (DmShared.xNull(txtSupplierBillNo.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please fill Bill No.", txtSupplierBillNo).show();
            return;
        }
        if (DmShared.dNull(txtSupplierBillDate.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please fill Bill Date", txtSupplierBillDate).show();
            return;
        }
        if (DmShared.xNull(txtHCalculateOn.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please select Calc. On", txtHCalculateOn).show();
            return;
        }
        if ((DmShared.convertDate(txtVoucherDate.getText()).getTime() < (DmShared.convertDate(txtSupplierBillDate.getText()).getTime()))) {
            new DmAlertMessageBox("Voucher Date should be greater than or equal to Bill Date.", txtVoucherDate).show();
            return;
        }

        String status = gridItemDetail.getGrid().getStore().get(currentRowIndex).getIsStatus();
        String returnedQty = gridItemDetail.getGrid().getStore().get(currentRowIndex).getReturnedQty();
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
            itemDetailGridEdit.show(currentRowIndex, false, false, false);
        } else {
            itemDetailGridEdit.show(currentRowIndex, true, true, true);
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnApprovedBy)) {
            if (DmShared.iNull(topControl.getDocId()).longValue() != 0) {
                if (DmShared.xNull(userControl.getTextBlog(approvedByBlog)).equalsIgnoreCase("")) {
                    setApproved(DmShared.iNull(topControl.getDocId()).toString(), true);
                } else {
                    setApproved(DmShared.iNull(topControl.getDocId()).toString(), false);
                }
            }
        } else if (event.getSource().equals(btnAddGRN)) {
            if (DmShared.iNull(txtHSupplier.getTag()).longValue() == 0) {
                new DmAlertMessageBox("Please select Supplier.", txtHSupplier).show();
            } else if (DmShared.dNull(txtSupplierBillDate.getText()).trim().equals("")) {
                new DmAlertMessageBox("Please enter Bill Date.", txtSupplierBillDate).show();
            } else {
                grnSearch.show(DmShared.xNull(txtHVoucherType.getTag()),
                        topControl.getMode().equalsIgnoreCase("New") ? "" : topControl.getDocId(),
                        txtHSupplier.getTag(),
                        txtSupplierBillDate.getText(), singlePOFlowInPurchase);
            }
        } else if (event.getSource().equals(labelTDS)) {
            if (tdsCategoryPopup_Mdl != null) {
                tdsCategoryPp.show(tdsCategoryPopup_Mdl, voucherCategory, true, true, topControl.getMode());

            } else {
                tdsCategoryPp.show();
            }

        }
        if (event.getSource().equals(btnPaymDetails)) {
            iniBillOtherDetails(btnPaymDetails.getText());
        }
        if (event.getSource().equals(btnAdvDetails)) {
            iniBillOtherDetails(btnAdvDetails.getText());
        }
        if (event.getSource().equals(btnBillHistory)) {
            iniBillOtherDetails(btnBillHistory.getText());
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
        } else if (event.getSource() == txtSupplierBillDate) {
            txtSupplierBillDate.setText(DmClient.returnDate(txtSupplierBillDate.getText()));
            if ((DmShared.convertDate(txtSupplierBillDate.getText()).getTime() > (DmShared.convertDate(DmShared.dNull(txtVoucherDate.getText())).getTime()))) {
                new DmAlertMessageBox("Bill Date should be less than or equal to Voucher Date.", txtSupplierBillDate).show();
                return;
            }

            if (grnDashboardFlag) {
                serviceDetailGrid.getServiceGrid().getGrid().getStore().clear();
                serviceDetailGrid.getServiceGrid().getGrid().getStore().add(new ServiceDetail_Mdl());
                serviceDetailGrid.initialize(setServiveDefaultValue());
                syncronizeLineService(false, true);
            }
            resetGstDetails();
        } else if (event.getSource() == txtFinalDiscountPer
                || event.getSource() == txtFinalDiscount) {
            setLineFinalDiscount();
        } else if (event.getSource() == txtPaymentDueDate) {
            txtPaymentDueDate.setText(DmClient.returnDate(txtPaymentDueDate.getText()));
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
            itemDetailGridEdit.hide();
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getShiftKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_P) {
            rootEvent.stopPropagation();
        }
    }

    public PurchaseBill(UserMenu menuPermission, HashMap paramHashMap, SetWidgetInvoker swi) {
        super();
        if (paramHashMap != null) {
            if (!paramHashMap.containsKey("isPreviousYearData")) {
                isPrevFYData = (Boolean) paramHashMap.get("isPreviousYearData");
            }
        }
        if (isPrevFYData) {
            menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("E", "*"));
            menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("D", "*"));
        }
        srvAsync = new ServiceInvoker<PurchaseBill_SrvAsync>(GWT.create(PurchaseBill_Srv.class), PurchaseBill.class.getName());
        this.asWidget().setTitle(widgetName);
        walkThrough = new WalkThrough(srvAsync);
        topControlEvents(menuPermission);
        manageEvent();
        textHelpEvents();
        findEvents();
        gridEvents();
        gui.setBannerVisible(false);
        tdsCategoryPpEvents();
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
                    guiTemplateCode = defaultValue.getVoucherType().getGuiTemplateCode();
                    gui.initializeViaTemplateCode();
                    taxSummary.initialize(defaultValue.isGstApplicable());
                    swi.setWidget(PurchaseBill.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void iniBillOtherDetails(final String btn) {
        srvAsync.invokeRPC(true).getBillOtherDetails(topControl.getDocId(), btn, new AsyncCallback<DmResult<List<GrdBillOtherDetails_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdBillOtherDetails_Mdl>> result) {
                if (result.isStatus()) {
                    flwBillOtherDetails.clear();
                    if (btn.equalsIgnoreCase(btnAdvDetails.getText())) {
                        flwBillOtherDetails.add(gridBillAdvance.getGrid());
                        gridBillAdvance.getGrid().getStore().clear();
                        gridBillAdvance.getGrid().getStore().addAll(result.getData());
                        gridBillAdvance.commitChanges();
                    }
                    if (btn.equalsIgnoreCase(btnPaymDetails.getText())) {
                        flwBillOtherDetails.add(gridBillFinalPayment.getGrid());
                        gridBillFinalPayment.getGrid().getStore().clear();
                        gridBillFinalPayment.getGrid().getStore().addAll(result.getData());
                        gridBillFinalPayment.commitChanges();
                    }
                    if (btn.equalsIgnoreCase(btnBillHistory.getText())) {
                        flwBillOtherDetails.add(gridBillHistory.getGrid());
                        gridBillHistory.getGrid().getStore().clear();
                        gridBillHistory.getGrid().getStore().addAll(result.getData());
                        gridBillHistory.commitChanges();
                    }

                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void topControlSave() {
        PurchaseBill_Mdl mrTemp;
        try {
            ///*/*/*/*/*/*/*/*/*/*/*/TDS recalculation*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*
            saveModeActive = true;
            ///*/*/*/*/*/*/*/*/*/*/*/End TDS recalculation/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*
            syncronizeLineService(true, false);
            defaultValue.getNewVoucherType().setSharedKeyFieldCode(txtHVoucherType.getTag());
            defaultValue.getNewVoucherType().setSharedKeyFieldName(txtHVoucherType.getText());
            defaultValue.getNewVoucherType().setVoucherCategory(voucherCategory);
            defaultValue.getNewVoucherType().setBalancingAc(balancingAc);
            defaultValue.setNewVoucherDate(txtVoucherDate.getText());
            defaultValue.getNewVoucherType().setGuiTemplateCode(guiTemplateCode);
            mrTemp = topControlSaveInitialize();
            ///*/*/*/*/*/*/*/*/*/*/*/TDS recalculation*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*
            saveModeActive = false;
            ///*/*/*/*/*/*/*/*/*/*/*/End TDS recalculation/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*
            PurchaseBill_Vld.chkSave(mrTemp);
            ServiceGrid_Vld.chkSave(mrTemp.getGrdServiceDetail());
            srvAsync.invokeRPC().saveRecord(mrTemp, DmClient.getVoucherPrefix(), isApprove, updateItemPurchaseRate, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHVoucherType).show();
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
                        syncronizeLineService(true, true);
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getErrorSource(), e.getErrorSourceCode(), e.getMessage());
            syncronizeLineService(true, true);
        }
    }

    private PurchaseBill_Mdl topControlSaveInitialize() {
        PurchaseBill_Mdl mrTemp = new PurchaseBill_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
            mrTemp.getObjRecId().setCounter(previous_Mdl.getObjRecId().getCounter());
            mrTemp.getObjRecId().setPrefix(previous_Mdl.getObjRecId().getPrefix());
            mrTemp.getObjRecId().setRecIdValue(previous_Mdl.getObjRecId().getRecIdValue());
        }

        resetGstDetails();

        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtVoucherNo.getText()));
        mrTemp.setU_EntDt_LatestLine(DmShared.xNull(txtVoucherNo.getTag()));
        mrTemp.setVoucherDate(DmShared.dNull(txtVoucherDate.getText()));
        mrTemp.setPaymentDueDate(DmShared.dNull(txtPaymentDueDate.getText()));
        mrTemp.setVoucherTypeCode(DmShared.xNull(txtHVoucherType.getTag()));
        mrTemp.setVoucherTypeName(DmShared.xNull(txtHVoucherType.getText()));
        mrTemp.setVoucherCategory(DmShared.xNull(voucherCategory));
        mrTemp.setBalancingAc(DmShared.xNull(balancingAc));
        mrTemp.setWtaCode(DmShared.iNull(wtaCode).toString());

        mrTemp.setSupplierStateCode(DmShared.xNull(txtHSupplier.getTag2nd()));
        mrTemp.setSupplierTag(DmShared.xNull(txtHSupplier.getTag()));
        mrTemp.setSupplierBillNo(DmShared.xNull(txtSupplierBillNo.getText()));
        mrTemp.setSupplierBillDate(DmShared.dNull(txtSupplierBillDate.getText()));
        mrTemp.setSupplierGstin(DmShared.xNull(supplierGstin));
        mrTemp.setSupplierGstinDate(DmShared.dNull(supplierGstinDate));

        mrTemp.setCalculateOnTag(DmShared.xNull(txtHCalculateOn.getTag()));
        mrTemp.setCalculateOnText(DmShared.xNull(txtHCalculateOn.getText()));
        mrTemp.setRemark(DmShared.xNull(txtRemark.getText()));

        mrTemp.setPlaceOfSupply(DmShared.xNull(defaultValue.getSiteStateCode()).trim());
        mrTemp.setRcmApplicable(DmShared.xNull(txtHRCMApplicable.getTag()));
        mrTemp.setLocationCode(location);
        mrTemp.setPartyTypeForEntry(partyTypeForEntry);
        mrTemp.setPartyType(partyType);
        mrTemp.setDefaultValueMdl(defaultValue);

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
         mrTemp.setTdsAmt(DmShared.xNull(labelTDSAmount.getText()));
        //=================== ************* ===================//

        //Accounts               
        mrTemp.setFinalDiscountAcCode(DmShared.xNull(txtFinalDiscount.getTag()));
        mrTemp.setRoundOffAcCode(DmShared.xNull(txtRoundoff.getTag()));

        //=========================(Removing Blank Row in GRID)=========================================
        List<GrdItemDetail_Mdl> grdLstGrnDetail_Mdl = new ArrayList<>();

        grdLstGrnDetail_Mdl.addAll(gridItemDetail.getGrid().getStore().getAll());

        if (gridItemDetail.getGrid().getStore().getAll().size() >= 0) {
            int i = 0;
            for (GrdItemDetail_Mdl grnDetail_Mdl : gridItemDetail.getGrid().getStore().getAll()) {
                if (DmShared.iNull(grnDetail_Mdl.getSharedKeyFieldCode()).longValue() == 0) {
                    grdLstGrnDetail_Mdl.remove(i);
                    i--;
                }
                i++;
            }
            if (grdLstGrnDetail_Mdl.size() < gridItemDetail.getGrid().getStore().getAll().size()) {
                gridItemDetail.getGrid().getStore().clear();
                gridItemDetail.getGrid().getStore().addAll(grdLstGrnDetail_Mdl);
                gridItemDetail.commitChanges();
            }

        }
        mrTemp.getListItemDetail().clear();
        mrTemp.getListItemDetail().addAll(gridItemDetail.getGrid().getStore().getAll());

        mrTemp.getGrdServiceDetail().clear();
        mrTemp.getGrdServiceDetail().addAll(serviceDetailGrid.getServiceGrid().getGrid().getStore().getAll());
        //-------------------------------------------------------------------------------------

        mrTemp.setGuiMdl(gui.getGUI_Mdl());
        mrTemp.setGuiConfigCode(gui.getConfigCode());

        if (modifyReasonMdl != null) {
            mrTemp.setModifyReasonMdl(modifyReasonMdl);
        }
        if (DmShared.vNull(labelTDSAmount.getText()).doubleValue() != 0.0) {
            tdsCategoryPp.onOK();
            mrTemp.setTdsCatMdl(tdsCategoryPopup_Mdl);
        } else {
            mrTemp.setTdsCatMdl(null);
        }

        return mrTemp;
    }

    private void topControlSaveError(int errorNo, String errorSource, String errorSourceCode, String errorMessage) {
        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtHVoucherType).show();
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
            case 540:
                new DmAlertMessageBox(errorMessage, txtPaymentDueDate).show();
                break;
            case 550:
                new DmAlertMessageBox(errorMessage, txtVoucherDate).show();
                break;
            case 600:
                new DmAlertMessageBox(errorMessage, txtHCalculateOn).show();
                break;
            case 650:
                new DmAlertMessageBox(errorMessage, txtPaymentDueDate).show();
                break;
            case 700:
                int indexTemp = DmShared.iNull(errorSourceCode).intValue();
                if (!DmShared.xNull(errorSource).equalsIgnoreCase("GridRow")) {
                    List<GrdItemDetail_Mdl> listTemp = gridItemDetail.getGrid().getStore().getAll();
                    for (GrdItemDetail_Mdl node : listTemp) {
                        if (DmShared.xNull(node.getPcDocId()).equalsIgnoreCase(errorSource)
                                && DmShared.xNull(node.getPcV_SNo()).equalsIgnoreCase(errorSourceCode)) {
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
                        ((CellSelectionModel) gridItemDetail.getGrid().getSelectionModel()).selectCell(gridRowIndex, 1);
                    }
                }).show();
                break;
            case 800:
                new DmAlertMessageBox(errorMessage, txtHRCMApplicable).show();
                break;
            case 900:
                indexTemp = DmShared.iNull(errorSourceCode).intValue();
                final int serviceRowIndex = indexTemp;
                new DmAlertMessageBox(errorMessage, new HideEvent.HideHandler() {
                    @Override
                    public void onHide(HideEvent event) {
                        ((CellSelectionModel) serviceDetailGrid.getServiceGrid().getGrid().getSelectionModel()).selectCell(serviceRowIndex, 1);
                    }
                }).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtRemark).show();
                break;
        }
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

    private void topControlMoveRec(String strKey) {
        grnDashboardFlag = false;
        lstGrn = null;
        defaultValue.setIsContinuousNewMode(false);
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<PurchaseBill_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<PurchaseBill_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<PurchaseBill_Mdl> result) {
        clear();
        if (result.isStatus()) {
            PurchaseBill_Mdl mrTemp = result.getData();
            previous_Mdl = result.getData().clone();
            topControl.setDocId(mrTemp.getSharedKeyFieldCode());
            txtVoucherNo.setText(mrTemp.getSharedKeyFieldName());
            txtVoucherNo.setTag(mrTemp.getU_EntDt_LatestLine());
            txtVoucherDate.setText(mrTemp.getVoucherDate());
            txtPaymentDueDate.setText(mrTemp.getPaymentDueDate());
            txtHVoucherType.setTag2nd(mrTemp.getVoucherTypeCode());
            txtHVoucherType.setTag(mrTemp.getVoucherTypeCode());
            txtHVoucherType.setText(mrTemp.getVoucherTypeName());
            voucherCategory = mrTemp.getVoucherCategory();
            balancingAc = mrTemp.getBalancingAc();
            wtaCode = mrTemp.getWtaCode();
            txtHSupplier.setTag2nd(mrTemp.getSupplierStateCode());
            txtHSupplier.setTag(mrTemp.getSupplierTag());
            txtHSupplier.setText(mrTemp.getSupplierText());
            txtSupplierBillDate.setText(mrTemp.getSupplierBillDate());
            txtSupplierBillNo.setText(mrTemp.getSupplierBillNo());
            txtHCalculateOn.setTag(mrTemp.getCalculateOnTag());
            txtHCalculateOn.setText(mrTemp.getCalculateOnText());
            txtRemark.setText(mrTemp.getRemark());

            lastTransactionDate = mrTemp.getLastTransactionDate();
            selectedSupplierCode = mrTemp.getSupplierTag();
            supplierGstin = mrTemp.getSupplierGstin();
            supplierGstinDate = mrTemp.getSupplierGstinDate();
            txtHRCMApplicable.setTag(DmShared.xNull(mrTemp.getRcmApplicable()));
            txtHRCMApplicable.setTag2nd(DmShared.xNull(mrTemp.getRcmApplicable()));
            if (DmShared.xNull(mrTemp.getRcmApplicable()).equalsIgnoreCase("1")) {
                txtHRCMApplicable.setText("Yes");
            } else if (DmShared.xNull(mrTemp.getRcmApplicable()).equalsIgnoreCase("0")) {
                txtHRCMApplicable.setText("No");
            }

            location = mrTemp.getLocationCode();
            partyTypeForEntry = mrTemp.getPartyTypeForEntry();
            partyType = mrTemp.getPartyType();

            userControl.setTextBlog(preparedByBlog, mrTemp.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, mrTemp.getSharedModifiedBy());
            userControl.setTextBlog(approvedByBlog, mrTemp.getApprovedBy());

            serviceDetailGrid.getServiceGrid().getGrid().getStore().clear();
            serviceDetailGrid.getServiceGrid().getGrid().getStore().addAll(mrTemp.getGrdServiceDetail());

            gridItemDetail.getGrid().getStore().clear();
            gridItemDetail.getGrid().getStore().addAll(mrTemp.getListItemDetail());

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
            serviceDetailGrid.initialize(setServiveDefaultValue());
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
        tdsCategoryPopup_Mdl = result.getData().getTdsCatMdl();
        tdsCategoryPp.moveRec(tdsCategoryPopup_Mdl);
        manageDisplay(false);
    }

    private void topControlDelete(String strKey) {

        srvAsync.invokeRPC().deleteRecord(strKey, gui.getConfigCode(), new AsyncCallback<DmResult<String>>() {

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

    private void fillGrnDetail(List<KeyFieldExtended> listKF, String grnDocId) {
        srvAsync.invokeRPC().getItemDetail(DmShared.xNull(txtHVoucherType.getTag()),
                listKF,
                grnDocId,
                defaultValue.getIsSiteStateUT(),
                txtHSupplier.getTag(),
                location,
                DmShared.xNull(txtSupplierBillDate.getText()).equalsIgnoreCase("") ? txtVoucherDate.getText() : txtSupplierBillDate.getText(),
                topControl.getMode().equalsIgnoreCase("New") ? "" : topControl.getDocId(),
                txtVoucherDate.getText(), defaultValue.isSinglePOFlowInPurchase(),
                txtHRCMApplicable.getTag(),
                defaultValue.isIncludeTaxInCost(),
                defaultValue.getIsSiteStateUT(),
                defaultValue.getPurchaseAcPostingBasedOn(),
                defaultValue.isInventoryAccountingActive(),
                new AsyncCallback<DmResult<PurchaseBill_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<PurchaseBill_Mdl> result) {
                if (result.isStatus() && result.getData() != null) {
                    PurchaseBill_Mdl mrTemp = result.getData();
                    lstGrn = null;
                    List<GrdItemDetail_Mdl> grdItemDetail_Mdl = mrTemp.getListItemDetail();
                    gridItemDetail.getGrid().getStore().clear();
                    gridItemDetail.getGrid().getStore().addAll(mrTemp.getListItemDetail());
                    gridItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
                    serviceDetailGrid.getServiceGrid().getGrid().getStore().clear();
                    serviceDetailGrid.getServiceGrid().getGrid().getStore().addAll(mrTemp.getGrdServiceDetail());
                    serviceDetailGrid.getServiceGrid().getGrid().getStore().add(new ServiceDetail_Mdl());
                    fillItemDetail(grdItemDetail_Mdl);
                    txtSupplierBillNo.setFocus(true);
                    calculate();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void fillItemDetail(List<GrdItemDetail_Mdl> listGRN) {
        for (int i = 0; i < listGRN.size(); i++) {
            GrdItemDetail_Mdl detail_Mdl = listGRN.get(i);
            if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() > 0) {
                itemDetailGridEdit.displayRow(detail_Mdl);
                itemDetailGridEdit.calculate();
                itemDetailGridEdit.fillRowData(detail_Mdl);
            }
        }
        serviceDetailGrid.getServiceGrid().commitChanges();
        gridItemDetail.commitChanges();
        syncronizeLineService(true, false);
    }

    private void manageEvent() {
        flwForm.addAttachHandler(this);
        btnApprovedBy.addClickHandler(this);
        btnAddGRN.addClickHandler(this);
        btnAdvDetails.addClickHandler(this);
        btnBillHistory.addClickHandler(this);
        btnPaymDetails.addClickHandler(this);
        txtVoucherDate.addBlurHandler(this);
        txtSupplierBillDate.addBlurHandler(this);
        txtFinalDiscountPer.addBlurHandler(this);
        txtFinalDiscount.addBlurHandler(this);
        txtPaymentDueDate.addBlurHandler(this);
        labelTDS.addClickHandler(this);
    }

    private void manageDisplay(boolean flag) {
        txtVoucherNo.setEnabled(false);
        txtVoucherDate.setEnabled(false);
        txtGoodsAmount.setEnabled(false);
        txtServiceAmount.setEnabled(false);
        txtDiscount.setEnabled(false);
        txtTaxableAmt.setEnabled(false);
        txtSgstUgst.setEnabled(false);
        txtSGST.setEnabled(false);
        txtUGST.setEnabled(false);
        txtCGST.setEnabled(false);
        txtIGST.setEnabled(false);
        txtSubTotal.setEnabled(false);
        txtTotalAmount.setEnabled(false);
        txtRoundoff.setEnabled(false);
        txtNetAmount.setEnabled(false);
        txtLandedValue.setEnabled(false);

        if (topControl.getMode().equalsIgnoreCase("Edit")) {
            txtHVoucherType.setEnabled(false);
            txtHSupplier.setEnabled(false);
            txtHRCMApplicable.setEnabled(false);

        }

        flwAddGRN.clear();
        if (flag) {
            flwAddGRN.add(btnAddGRN);
            txtVoucherDate.setEnabled(dateEditable);
        }
    }

    private void onAdd() {
        clear();
        manageDisplay(true);
        serviceDetailGrid.getServiceGrid().getGrid().getStore().add(new ServiceDetail_Mdl());
        if (defaultValue.getIsContinuousNewMode()) {
            setDefaultValues();
        } else {
            setDefaultValuesWithDateReset();
        }
        setLocation();
    }

    private void setDefaultValues() {
        txtVoucherDate.setText(defaultValue.getIsContinuousNewMode() ? DmShared.dNull(defaultValue.getNewVoucherDate()) : DmShared.dNull(defaultValue.getCurrentDate()));
        txtHVoucherType.setTag2nd(defaultValue.getIsContinuousNewMode() ? defaultValue.getNewVoucherType().getSharedKeyFieldCode() : defaultValue.getVoucherType().getSharedKeyFieldCode());
        txtHVoucherType.setTag(defaultValue.getIsContinuousNewMode() ? defaultValue.getNewVoucherType().getSharedKeyFieldCode() : defaultValue.getVoucherType().getSharedKeyFieldCode());
        txtHVoucherType.setText(defaultValue.getIsContinuousNewMode() ? defaultValue.getNewVoucherType().getSharedKeyFieldName() : defaultValue.getVoucherType().getSharedKeyFieldName());
        voucherCategory = defaultValue.getIsContinuousNewMode() ? defaultValue.getNewVoucherType().getVoucherCategory() : defaultValue.getVoucherType().getVoucherCategory();
        balancingAc = defaultValue.getIsContinuousNewMode() ? defaultValue.getNewVoucherType().getBalancingAc() : defaultValue.getVoucherType().getBalancingAc();
        guiTemplateCode = defaultValue.getIsContinuousNewMode() ? defaultValue.getNewVoucherType().getGuiTemplateCode() : defaultValue.getVoucherType().getGuiTemplateCode();
        txtHCalculateOn.setTag("0");
        txtHCalculateOn.setText("Document Quantity");
        txtRoundoff.setTag(DmShared.xNull(defaultValue.getRoundOffAcCode()));
        txtFinalDiscount.setTag(DmShared.xNull(defaultValue.getFinalDicountAcCode()));
        singlePOFlowInPurchase = defaultValue.isSinglePOFlowInPurchase();
        setRCM(voucherCategory);
        if (!(DmShared.iNull(defaultValue.getIsSiteStateUT()).intValue() == 1)) {
            lblSgstUgst.setText(DmEnum.GSTS_Head.SGST.getName());
        } else if ((DmShared.iNull(defaultValue.getIsSiteStateUT()).intValue() == 1)) {
            lblSgstUgst.setText(DmEnum.GSTS_Head.UGST.getName());
        }
        gui.initializeViaTemplateCode();
        if (lstGrn != null) {
            grnDashboardFlag = true;
            if (lstGrn.get(0) != null) {
                selectedSupplierCode = lstGrn.get(0).getSharedPreparedBy();
                getSupplierDetails(selectedSupplierCode);
            }
        }

        txtHSupplier.setFocus(true);
    }

    private void setDefaultValuesWithDateReset() {
        srvAsync.invokeRPC(true).getServerDateTime(new AsyncCallback<DmResult<String[]>>() {
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
        wtaCode = "";
        voucherCategory = "";
        balancingAc = "";
        guiTemplateCode = "";
        previous_Mdl = null;
        flwAddGRN.clear();
        gui.clear();
        gridItemDetail.getGrid().getStore().clear();
        serviceDetailGrid.getServiceGrid().getGrid().getStore().clear();
        tdsCategoryPp.clear();
        tdsCategoryPopup_Mdl = null;
        labelTDSAmount.setText("0.0");
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

        //=====================================< Set Location >=================================//
        setLocation();

    }

    private void setLocation() {
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

    private void setRCM(String vCat) {
        boolean rcmFlag = false;
        //=====================================< Set RCM >=================================//
        if (DmShared.xNull(vCat).equalsIgnoreCase(new ProjectVoucherCategory().PURCHASE_BILL_UNREGISTERED)) {
            if (!defaultValue.isIncludeTaxInCost()) {
                rcmFlag = true;
            }
        }
        if (rcmFlag) {
            txtHRCMApplicable.setTag("1");
            txtHRCMApplicable.setTag2nd("1");
            txtHRCMApplicable.setText("Yes");
            txtHRCMApplicable.setEnabled(true);
        } else {
            txtHRCMApplicable.setTag("0");
            txtHRCMApplicable.setTag2nd("0");
            txtHRCMApplicable.setText("No");
            txtHRCMApplicable.setEnabled(false);
        }
    }

    private void calculateGrid() {
        for (int i = 0; i < gridItemDetail.getGrid().getStore().getAll().size(); i++) {
            GrdItemDetail_Mdl detail_Mdl = gridItemDetail.getGrid().getStore().get(i);
            if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() != 0) {
                if (!detail_Mdl.isServiceRecord()) {
                    itemDetailGridEdit.displayRow(detail_Mdl);
                    itemDetailGridEdit.calculate();
                    itemDetailGridEdit.fillRowData(detail_Mdl);
                }
            }
        }
        gridItemDetail.commitChanges();
        syncronizeLineService(true, false);
    }

    private void calculate() {
        double goodsGrossValue = 0, serviceGrossValue = 0, goodsTaxableValue = 0, serviceTaxableValue = 0, finalDiscount = 0;
        double commonUnitQty = 0;
        String CommonUnitCode = "";
        boolean isCommonUnit = true;

        txtGoodsAmount.setText("");
        txtGoodsAmount.setTag("");
        txtGoodsAmount.setTag2nd("");
        txtServiceAmount.setText("");
        txtServiceAmount.setTag("");
        txtDiscount.setText("");
        txtDiscount.setTag2nd("");
        txtTaxableAmt.setText("");
        txtSgstUgst.setText("");
        txtSGST.setText("");
        txtUGST.setText("");
        txtCGST.setText("");
        txtIGST.setText("");
        txtSubTotal.setText("");
        txtTotalAmount.setText("");
        txtRoundoff.setText("");
        txtNetAmount.setText("");

        gridItemDetail.commitChanges();

        for (GrdItemDetail_Mdl detail_Mdl : gridItemDetail.getGrid().getStore().getAll()) {
            if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() > 0) {
                if (!detail_Mdl.isServiceRecord()) {
                    goodsGrossValue += DmShared.vNull(detail_Mdl.getAmount());
                    goodsTaxableValue += DmShared.vNull(detail_Mdl.getTaxableAmt());

                    // for common qty 
                    commonUnitQty += DmShared.vNull(detail_Mdl.getSkuQty()).doubleValue();

                    if (DmShared.xNull(CommonUnitCode).equalsIgnoreCase("")) {
                        CommonUnitCode = detail_Mdl.getPurchaseUnitCode();
                    } else if (DmShared.iNull(CommonUnitCode).longValue() != DmShared.iNull(detail_Mdl.getPurchaseUnitCode()).longValue()) {
                        isCommonUnit = false;
                    }
                } else {
                    serviceGrossValue += DmShared.vNull(detail_Mdl.getAmount());
                    serviceTaxableValue += DmShared.vNull(detail_Mdl.getTaxableAmt());
                }

                if (DmShared.vNull(txtFinalDiscountPer.getText()) > 0) {
                    finalDiscount += DmShared.vNull(detail_Mdl.getFinalDiscount());
                }

                txtDiscount.setText(DmClient.vNull(DmShared.vNull(txtDiscount.getText()) + DmShared.vNull(detail_Mdl.getDiscount()), "0.00"));
                txtSgstUgst.setText(DmClient.vNull(DmShared.vNull(txtSgstUgst.getText()) + DmShared.vNull(detail_Mdl.getSgstUgst()), "0.00"));
                txtSGST.setText(DmClient.vNull(DmShared.vNull(txtSGST.getText()) + DmShared.vNull(detail_Mdl.getSgst()), "0.00"));
                txtUGST.setText(DmClient.vNull(DmShared.vNull(txtUGST.getText()) + DmShared.vNull(detail_Mdl.getUgst()), "0.00"));
                txtCGST.setText(DmClient.vNull(DmShared.vNull(txtCGST.getText()) + DmShared.vNull(detail_Mdl.getCgst()), "0.00"));
                txtIGST.setText(DmClient.vNull(DmShared.vNull(txtIGST.getText()) + DmShared.vNull(detail_Mdl.getIgst()), "0.00"));
            }
        }

        if (isCommonUnit == false) {
            commonUnitQty = 0;
        }
        txtDiscount.setTag2nd(DmClient.vNull(commonUnitQty, "0.00"));
        txtGoodsAmount.setText(DmClient.vNull(goodsGrossValue, "0.00"));
        txtGoodsAmount.setTag(DmClient.vNull(goodsTaxableValue, "0.00"));
        txtGoodsAmount.setTag2nd(DmClient.vNull(goodsGrossValue + serviceGrossValue, "0.00"));
        txtServiceAmount.setText(DmClient.vNull(serviceGrossValue, "0.00"));
        txtServiceAmount.setTag(DmClient.vNull(serviceTaxableValue, "0.00"));
        txtTaxableAmt.setText(DmClient.vNull(goodsTaxableValue + serviceTaxableValue, "0.00"));

        if (DmShared.iNull(txtHRCMApplicable.getTag()).intValue() == 1) {
            txtSubTotal.setText(DmClient.vNull(DmShared.vNull(txtTaxableAmt.getText()), "0.00"));
        } else {
            txtSubTotal.setText(DmClient.vNull(DmShared.vNull(txtTaxableAmt.getText())
                    + DmShared.vNull(txtSgstUgst.getText())
                    + DmShared.vNull(txtCGST.getText())
                    + DmShared.vNull(txtIGST.getText()), "0.00"));
        }

        if (DmShared.vNull(txtFinalDiscountPer.getText()) > 0) {
            txtFinalDiscount.setText(DmClient.vNull(finalDiscount, "0.00"));
        }

        txtTotalAmount.setText(DmClient.vNull(DmShared.vNull(txtSubTotal.getText()) - DmShared.vNull(txtFinalDiscount.getText()), "0.00"));
//        txtNetAmount.setText(DmClient.vNull(DmShared.iNull(DmShared.compute("Math.round(" + txtTotalAmount.getText() + ")")), "0.00"));
        txtNetAmount.setText(DmClient.vNull(DmShared.iNull(Math.round(DmShared.vNull(txtTotalAmount.getText()).doubleValue())), "0.00"));
        txtRoundoff.setText(DmClient.vNull(DmShared.vNull(txtNetAmount.getText()) - DmShared.vNull(txtTotalAmount.getText()), "0.00"));

        double landedValue = 0;
        if (defaultValue.isIncludeTaxInCost()) {
            landedValue = DmShared.vNull(txtNetAmount.getText()).doubleValue();
        } else {
            if (DmShared.iNull(txtHRCMApplicable.getTag()).intValue() == 1) {
                landedValue = DmShared.vNull(txtNetAmount.getText()).doubleValue();
            } else {
                landedValue = DmShared.vNull(txtNetAmount.getText()).doubleValue()
                        - DmShared.vNull(txtSgstUgst.getText()).doubleValue()
                        - DmShared.vNull(txtCGST.getText()).doubleValue()
                        - DmShared.vNull(txtIGST.getText()).doubleValue();
            }
        }
        txtLandedValue.setText(DmClient.vNull(landedValue, "0.00"));

        calculateLandedValue();
        resetTaxSummary();
        serviceDetailGrid.initialize(setServiveDefaultValue());
        setTDSCatPpFields();
    }

    private void calculateLandedValue() {

        double landedValue = 0, totalGrossAmount = 0, lineGrossAmount = 0;

        landedValue = DmShared.vNull(txtLandedValue.getText());

        for (GrdItemDetail_Mdl detailMdl : gridItemDetail.getGrid().getStore().getAll()) {
            if (detailMdl != null) {
                if (!DmShared.xNull(detailMdl.getSharedKeyFieldCode()).trim().equals("")) {
                    if (!detailMdl.isServiceRecord()) {
                        totalGrossAmount += DmShared.vNull(detailMdl.getAmount());
                    }
                }
            }
        }

        double landedSkuValue = 0, totalLandedSkuValue = 0, landedSkuRate = 0;
        GrdItemDetail_Mdl lastLineMdl = null;
        for (GrdItemDetail_Mdl lineMdl : gridItemDetail.getGrid().getStore().getAll()) {
            if (lineMdl != null) {
                if (!DmShared.xNull(lineMdl.getSharedKeyFieldCode()).trim().equals("")) {
                    if (!lineMdl.isServiceRecord()) {
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
        }

        if (lastLineMdl != null) {
            landedSkuValue = DmShared.vNull(DmClient.vNull(lastLineMdl.getLandedSkuValue(), "0.00")) - (DmShared.vNull(DmClient.vNull(totalLandedSkuValue, "0.00")) - DmShared.vNull(DmClient.vNull(landedValue, "0.00")));
            lastLineMdl.setLandedSkuValue(DmClient.vNull(landedSkuValue, "0.00"));

            landedSkuRate = DmShared.vNull(lastLineMdl.getLandedSkuValue()) / DmShared.vNull(lastLineMdl.getSkuQty());
            lastLineMdl.setLandedSkuRate(DmClient.vNull(landedSkuRate, "0.0000000"));
        }

        gridItemDetail.commitChanges();

    }

    private void resetTaxSummary() {
        List<List<OverHeadLine_Mdl>> lstOverheadLine = new ArrayList<List<OverHeadLine_Mdl>>();
        List<OverHeadLine_Mdl> overheadLine = null;
        OverHeadLine_Mdl overheadLineRow = null;
        for (GrdItemDetail_Mdl grdMdl : gridItemDetail.getGrid().getStore().getAll()) {
            if (grdMdl != null) {
                // loop start
                overheadLine = new ArrayList<OverHeadLine_Mdl>();

                overheadLineRow = new OverHeadLine_Mdl();
                overheadLineRow.setOhManualCode(DmEnum.GSTS_Head.CGST.getShortName());
                overheadLineRow.setAtRate(grdMdl.getCgstPer());
                overheadLineRow.setAmount(grdMdl.getCgst());
                overheadLine.add(overheadLineRow);

                overheadLineRow = new OverHeadLine_Mdl();
                overheadLineRow.setOhManualCode(DmEnum.GSTS_Head.SGST.getShortName());
                overheadLineRow.setAtRate(grdMdl.getSgstPer());
                overheadLineRow.setAmount(grdMdl.getSgst());
                overheadLine.add(overheadLineRow);

                overheadLineRow = new OverHeadLine_Mdl();
                overheadLineRow.setOhManualCode(DmEnum.GSTS_Head.UGST.getShortName());
                overheadLineRow.setAtRate(grdMdl.getUgstPer());
                overheadLineRow.setAmount(grdMdl.getUgst());
                overheadLine.add(overheadLineRow);

                overheadLineRow = new OverHeadLine_Mdl();
                overheadLineRow.setOhManualCode(DmEnum.GSTS_Head.IGST.getShortName());
                overheadLineRow.setAtRate(grdMdl.getIgstPer());
                overheadLineRow.setAmount(grdMdl.getIgst());
                overheadLine.add(overheadLineRow);

                overheadLineRow = new OverHeadLine_Mdl();
                overheadLineRow.setOhManualCode("TV");
                overheadLineRow.setAmount(grdMdl.getTaxableAmt());
                overheadLine.add(overheadLineRow);

                lstOverheadLine.add(overheadLine);
                //loop end

            }
        }
        taxSummary.reset(lstOverheadLine);
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
    public void action(String nature, Object object, DmCallBack callback) {
        nature = DmShared.xNull(nature).trim().toUpperCase();
        try {
            switch (nature) {
                case "ADD_RECORD":
                    lstGrn = (ArrayList) object;
                    getDefaultVoucherType();
                    break;
                case "FOCUS_ON_ADD":
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            txtSupplierBillNo.setFocus(true);
                        }
                    });
                    break;
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    private void getSupplierDetails(String supplierCode) {

        DmHelper dmHelper = new DmHelper();
        dmHelper.addSort("name", DmSortType.ASC);
        dmHelper.addSort("cityName", DmSortType.ASC);

        srvAsync.invokeRPC().getSupplierDetails(supplierCode, voucherCategory, txtHVoucherType.getTag(), txtVoucherDate.getText(), dmHelper, false, new AsyncCallback<DmResult<TxtHSupplier_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHSupplier).show();
            }

            @Override
            public void onSuccess(DmResult<TxtHSupplier_Mdl> result) {
                if (result.isStatus()) {
                    txtHSupplier.setTag(DmShared.xNull(result.getData().getSharedKeyFieldCode()));
                    txtHSupplier.setText(DmShared.xNull(result.getData().getSharedKeyFieldName()));
                    fillSupplierDetails(true, result.getData());
//                    serviceDetailGrid.initialize(setServiveDefaultValue());  //TBC

                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHSupplier).show();
                }
            }
        });
    }

    private void fillSupplierDetails(boolean blnStatus, TxtHSupplier_Mdl t1Row) {
        if (blnStatus && t1Row != null) {
            txtHSupplier.setTag2nd(DmShared.xNull(t1Row.getStateCode()));
            supplierGstin = DmShared.xNull(t1Row.getGstin());
            supplierGstinDate = DmShared.dNull(t1Row.getGstinDate());
            partyType = DmShared.xNull(t1Row.getPartyType());
            txtPaymentDueDate.setText(t1Row.getDueDays());
            resetGstDetails();
        }

        if (!DmShared.xNull(selectedSupplierCode).equalsIgnoreCase(DmShared.xNull(txtHSupplier.getTag()))) {
            selectedSupplierCode = DmShared.xNull(txtHSupplier.getTag());
            clearGrid();
        }

        if (lstGrn != null) {
            fillGrnDetail(lstGrn, "");
        }
    }

    private void getDefaultVoucherType() {
        String partyType = lstGrn.get(0).getSharedModifiedBy();

        srvAsync.invokeRPC(true).getVoucherType(partyType, new AsyncCallback<DmResult<TxtHVoucherType_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<TxtHVoucherType_Mdl> result) {
                if (result.isStatus()) {

                    defaultValue.setVoucherType(result.getData());
                    topControl.btnClick("New");
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void clearGrid() {
        serviceDetailGrid.getServiceGrid().getGrid().getStore().clear();
        serviceDetailGrid.getServiceGrid().getGrid().getStore().add(new ServiceDetail_Mdl());
        serviceDetailGrid.initialize(setServiveDefaultValue());

        gridItemDetail.getGrid().getStore().clear();
        gridItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
        syncronizeLineService(false, true);
        calculate();
    }

    private void clearLineFinalDiscount() {
        double lineSubTotal = 0;
        for (int i = 0; i < gridItemDetail.getGrid().getStore().getAll().size(); i++) {
            GrdItemDetail_Mdl grdItemDetails_Mdl = gridItemDetail.getGrid().getStore().get(i);
            if (!gridItemDetail.getGrid().getStore().get(i).isServiceRecord()) {
                if (DmShared.iNull(grdItemDetails_Mdl.getSharedKeyFieldCode()).longValue() != 0) {
                    lineSubTotal = DmShared.vNull(gridItemDetail.getGrid().getStore().get(i).getSubTotal()).doubleValue();
                    gridItemDetail.getGrid().getStore().get(i).setFinalDiscount("");
                    gridItemDetail.getGrid().getStore().get(i).setFinalDiscountPer("");
                    gridItemDetail.getGrid().getStore().get(i).setItemValue(DmClient.vNull(lineSubTotal, "0.00"));
                }
            }
        }
        gridItemDetail.commitChanges();
        calculate();
    }

    private void setLineFinalDiscount() {
        clearLineFinalDiscount();

        if (DmShared.vNull(txtFinalDiscountPer.getText()).doubleValue() > 0 || DmShared.vNull(txtFinalDiscount.getText()).doubleValue() != 0) {
            int lastIndex = 0;
            double totalTaxableAmt = 0, discountPerRupee = 0, totalFinalDiscount = 0;
            boolean isDiscount = false, flag = false;
            double lineSubTotal = 0, lineTaxableAmount = 0, lineFinalDiscount = 0;

            /* ==============================<< Get total amount (and group by total amount) >>================================= */
            if ((DmShared.vNull(txtFinalDiscountPer.getText()).doubleValue() == 0) && (DmShared.vNull(txtFinalDiscount.getText()).doubleValue() != 0)) {
                int cnt = 0;
                for (GrdItemDetail_Mdl grdItemDetail_Mdl : gridItemDetail.getGrid().getStore().getAll()) {
                    if (grdItemDetail_Mdl.getSharedKeyFieldCode() != null) {
                        if (!grdItemDetail_Mdl.isServiceRecord()) {
                            if ((DmShared.iNull(grdItemDetail_Mdl.getSharedKeyFieldCode()).longValue() != 0)) {
                                totalTaxableAmt += DmShared.vNull(txtTaxableAmt.getText()).doubleValue();
                                lastIndex = cnt;
                            }
                        }
                    }
                    cnt++;
                }
                isDiscount = true;
                if (totalTaxableAmt != 0) {
                    discountPerRupee = DmShared.vNull((DmClient.vNull(DmShared.vNull(txtFinalDiscount.getText()).doubleValue() / totalTaxableAmt, "0.0000000"))).doubleValue();
                }
            }

            for (int i = 0; i < gridItemDetail.getGrid().getStore().getAll().size(); i++) {
                if (gridItemDetail.getGrid().getStore().get(i).getSharedKeyFieldCode() != null) {
                    if (!gridItemDetail.getGrid().getStore().get(i).isServiceRecord()) {
                        if (DmShared.iNull(gridItemDetail.getGrid().getStore().get(i).getSharedKeyFieldCode()).longValue() != 0) {
                            flag = true;
                            if (flag) {
                                lineTaxableAmount = DmShared.vNull(gridItemDetail.getGrid().getStore().get(i).getTaxableAmt()).doubleValue();
                                lineSubTotal = DmShared.vNull(gridItemDetail.getGrid().getStore().get(i).getSubTotal()).doubleValue();
                                if (isDiscount) {
                                    /* ==============================<< For Discount value >>================================= */
                                    lineFinalDiscount = DmShared.vNull(DmClient.vNull((discountPerRupee * lineTaxableAmount), "0.00")).doubleValue();
                                    totalFinalDiscount += lineFinalDiscount;

                                    if (i == lastIndex) {  //<Ag> TBC in case of service
                                        double adjAmt = 0;
                                        if (totalFinalDiscount > DmShared.vNull(txtFinalDiscount.getText()).doubleValue()) {
                                            adjAmt = DmShared.vNull(DmClient.vNull((totalFinalDiscount - DmShared.vNull(txtFinalDiscount.getText()).doubleValue()), "0.00")).doubleValue();
                                            lineFinalDiscount -= adjAmt;
                                        } else if (totalFinalDiscount < DmShared.vNull(txtFinalDiscount.getText()).doubleValue()) {
                                            adjAmt = DmShared.vNull(DmClient.vNull((DmShared.vNull(txtFinalDiscount.getText()).doubleValue() - totalFinalDiscount), "0.00")).doubleValue();
                                            lineFinalDiscount += adjAmt;
                                        }
                                    }
                                } else {
                                    /* ==============================<< For Discount Percentage >>================================= */
                                    lineFinalDiscount = (lineTaxableAmount * DmShared.vNull(DmClient.vNull(txtFinalDiscountPer.getText(), "0.00")).doubleValue()) / 100;
                                }

                                gridItemDetail.getGrid().getStore().get(i).setFinalDiscount(DmClient.vNull(lineFinalDiscount, "0.00"));
                                gridItemDetail.getGrid().getStore().get(i).setFinalDiscountPer(DmClient.vNull(txtFinalDiscountPer.getText(), "0.00"));
                                gridItemDetail.getGrid().getStore().get(i).setItemValue(DmClient.vNull(lineSubTotal - lineFinalDiscount, "0.00"));
                                flag = false;
                            }
                        }
                    }
                }
            }
        }
        gridItemDetail.commitChanges();
        calculate();
    }

    //<editor-fold defaultstate="collapsed" desc="Service Grid">
    private HashMap setServiveDefaultValue() {
        HashMap paramHashMap = new HashMap();
        paramHashMap.put("LOCATION", location);
        paramHashMap.put("UT", DmShared.iNull(defaultValue.getIsSiteStateUT()).intValue() == 1);
        paramHashMap.put("MODE", topControl.getMode());
        paramHashMap.put("IS_IN_GSTS", true);
        paramHashMap.put("GOODS_TAXABLE_VALUE", DmShared.xNull(txtGoodsAmount.getTag()));
        paramHashMap.put("COMMON_UNIT_QTY", DmShared.xNull(txtDiscount.getTag2nd()));
        paramHashMap.put("IS_RCM_APPLICABLE", DmShared.iNull(txtHRCMApplicable.getTag()).intValue() == 1);
        paramHashMap.put("IS_INCLUDE_TAX_IN_COST", defaultValue.isIncludeTaxInCost());
        paramHashMap.put("IS_MANUAL_GST_TYPE_REQUIRED", true);
        paramHashMap.put("IS_MANUAL_TAX_REQUIRED", true);
        paramHashMap.put("IS_INVENTROY_ACCOUNTING_ACTIVE", defaultValue.isInventoryAccountingActive());
        paramHashMap.put("IS_SERVICE_AC_MANDATORY", true);
        paramHashMap.put("IS_TAX_AC_MANDATORY", true);

        return paramHashMap;
    }

    private void syncronizeLineService(boolean syncronizeLineHeader, boolean addServiceRow) {
        serviceDetailGrid.initialize(setServiveDefaultValue());
        //<editor-fold defaultstate="collapsed" desc="First Syncronize Line Service With Item Grid">
        if (syncronizeLineHeader) {
            calculate();
        }

        List<GrdItemDetail_Mdl> grdLstItemDetail_Mdl = new ArrayList<>();

        grdLstItemDetail_Mdl.addAll(gridItemDetail.getGrid().getStore().getAll());

        int i = 0;
        for (GrdItemDetail_Mdl itemDetail_Mdl : gridItemDetail.getGrid().getStore().getAll()) {
            if (itemDetail_Mdl.isServiceRecord()) {
                grdLstItemDetail_Mdl.remove(i);
                i--;
            }
            i++;
        }

        if (grdLstItemDetail_Mdl.size() < gridItemDetail.getGrid().getStore().getAll().size()) {
            gridItemDetail.getGrid().getStore().clear();
            gridItemDetail.getGrid().getStore().addAll(grdLstItemDetail_Mdl);

            int size = gridItemDetail.getGrid().getStore().size() - 1;
            GrdItemDetail_Mdl itemDetail_Mdl = gridItemDetail.getGrid().getStore().get(size);

            if (DmShared.iNull(itemDetail_Mdl.getSharedKeyFieldCode()).longValue() > 0) {
//                if (DmShared.xNull(txtHGateEntryNo.getTag()).trim().equals("")) {  <Ag> Note: It Will Required in Purchase Bill Direct
                gridItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
//                }
            }
            gridItemDetail.commitChanges();
        }

        List<ServiceDetail_Mdl> serviceDetailLst_Mdls = serviceDetailGrid.getServiceGrid().getGrid().getStore().getAll();

        for (ServiceDetail_Mdl serviceDetail_Mdl : serviceDetailLst_Mdls) {
            if (DmShared.iNull(serviceDetail_Mdl.getSharedKeyFieldCode()).longValue() > 0) {
                serviceDetailGrid.calculate(serviceDetail_Mdl);

                GrdItemDetail_Mdl grdItemDetail_Mdl = new GrdItemDetail_Mdl();

                grdItemDetail_Mdl.setServiceRecord(true);
                grdItemDetail_Mdl.setSmfCode(serviceDetail_Mdl.getSmfCode());
                grdItemDetail_Mdl.setServiceEvalTypeCode(serviceDetail_Mdl.getServiceEvalTypeCode());
                grdItemDetail_Mdl.setServiceEvalTypeName(serviceDetail_Mdl.getServiceEvalTypeName());
                grdItemDetail_Mdl.setServiceEvalRate(serviceDetail_Mdl.getServiceEvalRate());

                grdItemDetail_Mdl.setSharedKeyFieldCode(serviceDetail_Mdl.getSharedKeyFieldCode());
                grdItemDetail_Mdl.setDocQty(DmShared.xNull(serviceDetail_Mdl.getQuantity()));
                grdItemDetail_Mdl.setAcceptQty(DmShared.xNull(serviceDetail_Mdl.getQuantity()));
                grdItemDetail_Mdl.setPurchaseUnitCode(serviceDetail_Mdl.getUnitCode());

                grdItemDetail_Mdl.setHsnCode(serviceDetail_Mdl.getHsnCode());
                grdItemDetail_Mdl.setHsn(serviceDetail_Mdl.getHsn());
                grdItemDetail_Mdl.setItemGSTNature(serviceDetail_Mdl.getItemGSTNature());
                grdItemDetail_Mdl.setItsBasedOn(serviceDetail_Mdl.getItsBasedOn());
                grdItemDetail_Mdl.setGstTypeCode(serviceDetail_Mdl.getGstTypeCode());
                grdItemDetail_Mdl.setGstTypeName(serviceDetail_Mdl.getGstTypeName());
                grdItemDetail_Mdl.setManualGstType(serviceDetail_Mdl.isManualGstType());

                //IAP
                grdItemDetail_Mdl.setIsubCode(serviceDetail_Mdl.getIsubCode());

                //Accounts
                grdItemDetail_Mdl.setSgstAcCode(DmShared.xNull(serviceDetail_Mdl.getSgstAcCode()));
                grdItemDetail_Mdl.setSgstRcmAcCode(DmShared.xNull(serviceDetail_Mdl.getSgstRcmAcCode()));

                grdItemDetail_Mdl.setUgstAcCode(DmShared.xNull(serviceDetail_Mdl.getUgstAcCode()));
                grdItemDetail_Mdl.setUgstRcmAcCode(DmShared.xNull(serviceDetail_Mdl.getUgstRcmAcCode()));

                grdItemDetail_Mdl.setCgstAcCode(DmShared.xNull(serviceDetail_Mdl.getCgstAcCode()));
                grdItemDetail_Mdl.setCgstRcmAcCode(DmShared.xNull(serviceDetail_Mdl.getCgstRcmAcCode()));

                grdItemDetail_Mdl.setIgstAcCode(DmShared.xNull(serviceDetail_Mdl.getIgstAcCode()));
                grdItemDetail_Mdl.setIgstRcmAcCode(DmShared.xNull(serviceDetail_Mdl.getIgstRcmAcCode()));

                //=================== Final Values ===================//
                grdItemDetail_Mdl.setRate(DmClient.vNull(serviceDetail_Mdl.getRate(), "0.00"));
                grdItemDetail_Mdl.setAmount(DmClient.vNull(serviceDetail_Mdl.getAmount(), "0.00"));
                grdItemDetail_Mdl.setDiscountPer("");
                grdItemDetail_Mdl.setDiscount("");
                grdItemDetail_Mdl.setTaxableAmt(DmClient.vNull(serviceDetail_Mdl.getTaxableAmt(), "0.00"));
                grdItemDetail_Mdl.setSgstUgstPer(DmClient.vNull(serviceDetail_Mdl.getSgstOrUgstRate(), "0.00"));
                grdItemDetail_Mdl.setSgstUgst(DmClient.vNull(serviceDetail_Mdl.getSgstOrUgstAmt(), "0.00"));
                grdItemDetail_Mdl.setSgstPer(DmClient.vNull(serviceDetail_Mdl.getSgstRate(), "0.00"));
                grdItemDetail_Mdl.setSgst(DmClient.vNull(serviceDetail_Mdl.getSgstAmt(), "0.00"));
                grdItemDetail_Mdl.setUgstPer(DmClient.vNull(serviceDetail_Mdl.getUgstRate(), "0.00"));
                grdItemDetail_Mdl.setUgst(DmClient.vNull(serviceDetail_Mdl.getUgstAmt(), "0.00"));
                grdItemDetail_Mdl.setCgstPer(DmClient.vNull(serviceDetail_Mdl.getCgstRate(), "0.00"));
                grdItemDetail_Mdl.setCgst(DmClient.vNull(serviceDetail_Mdl.getCgstAmt(), "0.00"));
                grdItemDetail_Mdl.setIgstPer(DmClient.vNull(serviceDetail_Mdl.getIgstRate(), "0.00"));
                grdItemDetail_Mdl.setIgst(DmClient.vNull(serviceDetail_Mdl.getIgstAmt(), "0.00"));
                grdItemDetail_Mdl.setSubTotal(DmClient.vNull(serviceDetail_Mdl.getSubTotal(), "0.00"));
                grdItemDetail_Mdl.setFinalDiscount("");
                grdItemDetail_Mdl.setItemValue(DmClient.vNull(serviceDetail_Mdl.getNetAmt(), "0.00"));
                //=================== ************* ===================//

                gridItemDetail.getGrid().getStore().add(grdItemDetail_Mdl);
            }
        }
        serviceDetailGrid.getServiceGrid().commitChanges();
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Add Serivice Row in Item Grid">
        if (addServiceRow) {
            GrdItemDetail_Mdl serviceRecord = new GrdItemDetail_Mdl();
            serviceRecord.setServiceRecord(true);
            gridItemDetail.getGrid().getStore().add(serviceRecord);
            int serviceRowIndex = gridItemDetail.getGrid().getStore().getAll().indexOf(serviceRecord);
            serviceDetailGrid.setServiceRowIndex(serviceRowIndex);
        }

        //</editor-fold>
        gridItemDetail.commitChanges();

        if (syncronizeLineHeader) {
            calculate();
        }

    }
//</editor-fold>    

    private PurchaseBill_Mdl topControlUpdate() {
        PurchaseBill_Mdl mrTemp = new PurchaseBill_Mdl();
        try {
            if (DmShared.convertDate(lastTransactionDate).getTime() > DmShared.convertDate(previous_Mdl.getVoucherDate()).getTime()) {

                if (topControl.getMode().equalsIgnoreCase("Edit")) {
                    mrTemp.setSharedKeyFieldCode(topControl.getDocId());
                    mrTemp.setPaymentDueDate(DmShared.dNull(updatePopup.txtPaymentDueDate.getText()));
                    mrTemp.setSupplierBillNo(DmShared.xNull(updatePopup.txtSupplierBillNo.getText()).trim());
                    mrTemp.setSupplierBillDate(DmShared.dNull(updatePopup.txtSupplierBillDate.getText()));
                    mrTemp.setRemark(DmShared.xNull(updatePopup.txtRemark.getText()).trim());
                    mrTemp.setVoucherDate(DmShared.dNull(txtVoucherDate.getText()));
                    mrTemp.setDefaultValueMdl(defaultValue);

                    srvAsync.invokeRPC().updateRecord(mrTemp, isApprove, new AsyncCallback<DmResult<String>>() {
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

    private void tdsCategoryPpEvents() {
        tdsCategoryPp.addTdsDetailCallBack(new TdsCategoryPopup.TDSDetailCallBack() {
            @Override
            public void onOk(TdsCategoryPopup_Mdl tDS_Mdl) {
                tdsCategoryPopup_Mdl = tDS_Mdl;
            }

            @Override
            public void setTDS(String tdsAmount) {
                labelTDSAmount.setText(DmClient.vNull(tdsAmount, "0.00"));
            }

            @Override
            public void onClose() {

            }
        });
    }

    private void setTDSCatPpFields() {
        if (saveModeActive) {
            return;
        }
        if (DmShared.iNull(txtHSupplier.getTag()).longValue() > 0) {
            if (tdsCategoryPopup_Mdl == null) {
                tdsCategoryPopup_Mdl = new TdsCategoryPopup_Mdl();
            }
            tdsCategoryPopup_Mdl.setTdsDeductFormCode(txtHSupplier.getTag());
            tdsCategoryPopup_Mdl.setTdsDeductFormName(txtHSupplier.getText());
            if (defaultValue.getTdsOn().trim().equalsIgnoreCase("0")){
                tdsCategoryPopup_Mdl.setTdsOnAmount(txtNetAmount.getText());
            }else {
                tdsCategoryPopup_Mdl.setTdsOnAmount(txtTaxableAmt.getText());
            }
            
            tdsCategoryPopup_Mdl.setOrignalAmount(txtNetAmount.getText());
            tdsCategoryPopup_Mdl.setVoucherDate(DmShared.dNull(txtVoucherDate.getText()));
            tdsCategoryPopup_Mdl.setvCategory(voucherCategory);
            if (DmShared.xNull(topControl.getMode()).equalsIgnoreCase("New")) {
                tdsCategoryPopup_Mdl.setTrnDocID("");
            } else {
                tdsCategoryPopup_Mdl.setTrnDocID(topControl.getDocId());
            }
            tdsCategoryPopup_Mdl.setPoDocID(DmShared.xNull(itemDetailGridEdit.getDmGrid().getGrid().getStore().get(0).getPoDocId()).trim());
        }
        tdsCategoryPp.setPpFields(tdsCategoryPopup_Mdl);
    }
}
