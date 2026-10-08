package aarogya.inventory.grn.client;

import aarogya.inventory.grn.shared.GrdGrnDetail_Mdl;
import aarogya.inventory.grn.shared.*;
import aarogya.inventory.orderSearch.client.OrderSearch;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.modifyReason.client.ModifyReason;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.stStatic.client.STClient;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import dataman.dmBase.self.shared.*;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.i18n.client.NumberFormat;
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
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmEnum.DmSortType;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import dataman.dmToolkit.dmGUI.client.DmGUI;
import dataman.dmToolkit.dmGUI.shared.DmGUIControl_Mdl;
import dataman.dmToolkit.dmGUI.shared.DmGUI_Mdl;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import dataman.dmToolkit.overHead.shared.OverHead_Shared;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Mdl;
import dataman.dmToolkit.walkThrough.client.WalkThrough;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import aarogya.inventory.purchaseBill.shared.GrdItemDetail_Mdl;
import aarogya.selfToolkit.serviceGrid.client.ServiceGrid;
import aarogya.selfToolkit.serviceGrid.shared.ServiceDetail_Mdl;
import aarogya.selfToolkit.serviceGrid.shared.ServiceGrid_Vld;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import aarogya.selfToolkit.stStatic.shared.STEnum.LabOverhead;

public class Grn extends Grn_Dsg implements AttachEvent.Handler, DmRootEvents, ClickHandler, BlurHandler {

    ServiceInvoker<Grn_SrvAsync> SrvAsync;
    protected DefaultValue_Mdl defaultValue;
    WalkThrough walkThrough;
    final OrderSearch orderSearch = new OrderSearch();
    final InventoryUpdatePopup updatePopup = new InventoryUpdatePopup();
    final GrnGridEdit<GrdGrnDetail_Mdl, Grd_PrpAcs> grnDetailGridEdit = new GrnGridEdit(grdGRNDetail, this);
    ModifyReason_Mdl modifyReasonMdl;
    Grn_Mdl previous_Mdl = null;
    boolean departmentEditable = false, dateEditable = false, isApprove = false, isPurchaseOrderExists = false, poDashboardFlag = false,
            singlePOFlowInPurchase = false;
    final ModifyReason modifyReason = new ModifyReason();
    String selectedSupplierCode, partyTypeForEntry, partyType, supplierGstin, supplierGstinDate,
            location, wtaCode, balancingAc, voucherCategory, guiTemplateCode, lastTransactionDate;
    List<KeyFieldExtended> lstPo = null;
    double serviceTaxValue = 0;

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);
        for (String controlPermission : menuPermission.getControlPermission()) {
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(departmentEditableBlog)) {
                departmentEditable = true;
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(approvedByBlog)) {
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

        topControl.setUserPermission(menuPermission, new VoucherPermissionHandler(DmClient.getVoucherPermission(new String[]{new ProjectVoucherCategory().PURCHASE_CHALLAN, new ProjectVoucherCategory().PURCHASE_CHALLAN_UNREGISTERED})) {
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
                if (!DmShared.xNull(userControl.getTextBlog(approvedByBlog)).equalsIgnoreCase("")) {
                    topControl.btnClick("");
                    new DmAlertMessageBox("Record is approved. You can't modify this record.", (DmTextBox) null).show();
                } else {
                    if (DmShared.convertDate(lastTransactionDate).getTime() > DmShared.convertDate(previous_Mdl.getVoucherDate()).getTime()) {
                        new DmConfirmMessageBox("Back Date are not allowed. Are you sure? You can edit only header data.", new DialogHideEvent.DialogHideHandler() {

                            @Override
                            public void onDialogHide(DialogHideEvent event) {
                                if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                                    updatePopup.show(previous_Mdl, Grn.this);
                                } else {
                                    topControlMoveRec(topControl.getDocId());
                                }
                            }
                        }).show();

//                    txtHSupplier.setFocus(true);
                    } else {
                        manageDisplay(true);
                        serviceDetailGrid.getServiceGrid().getGrid().getStore().add(new ServiceDetail_Mdl());
                        serviceDetailGrid.initialize(setServiveDefaultValue());
                        if (DmShared.xNull(txtHGateEntryNo.getTag()).trim().equals("")) {
                            grdGRNDetail.getGrid().getStore().add(new GrdGrnDetail_Mdl());
                            syncronizeLineService(false, true);
                        }
                        txtHSupplier.setFocus(true);
                    }
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
                    checkMaybeLabData();
                } else {
                    showModifyReason();
                }
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(Grn.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void showModifyReason() {
        modifyReason.show(topControl.getMode(), topControl.getDocId());
    }

    private void showOrderSearch(boolean isAutoShow) {
        orderSearch.show(DmShared.xNull(txtHVoucherType.getTag()),
                topControl.getMode().equalsIgnoreCase("New") ? "" : topControl.getDocId(),
                txtHSupplier.getTag(),
                txtChallanDate.getText(),
                voucherCategory, isAutoShow, singlePOFlowInPurchase);
    }

    private void textHelpEvents() {

        txtHVoucherType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHVoucherType_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHVoucher(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHVoucherType_Mdl>>>() {

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
                selectedSupplierCode = DmShared.xNull(txtHSupplier.getTag());
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
                fillSupplierDetails(blnStatus, tRow);
            }
        });

        modifyReason.addCallBack(new ModifyReason.CallBack() {

            @Override
            public void onOk(ModifyReason_Mdl modifyReason) {
                modifyReasonMdl = modifyReason;
                checkMaybeLabData();
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

                } else {
                    txtRemark.setFocus(true);
                }
            }
        });

        txtHTransporter.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHTransporter_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHTransporter(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHTransporter_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHTransporter).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHTransporter_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHTransporter.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHTransporter).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHTransporter_Mdl tRow) {
                if (blnStatus && tRow != null) {

                }
            }
        });

        txtHGateEntryNo.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHGateEntry_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHGateEntryNo((topControl.getMode().equalsIgnoreCase("New") ? "" : topControl.getDocId()), txtHSupplier.getTag(), txtVoucherDate.getText(), txtChallanDate.getText(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHGateEntry_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHGateEntryNo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHGateEntry_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHGateEntryNo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHGateEntryNo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHGateEntry_Mdl t1Row) {
                boolean flagFillGrid = false;
                if (!DmShared.xNull(txtHGateEntryNo.getTag2nd()).equalsIgnoreCase(DmShared.xNull(txtHGateEntryNo.getTag()))) {
                    txtHTransporter.setTag("");
                    txtHTransporter.setText("");
                    txtHTransporter.setTag2nd("");
                    flagFillGrid = true;
                    txtHGateEntryNo.setTag2nd(DmShared.xNull(txtHGateEntryNo.getTag()));
                    clearGrid();
                }

                if (DmShared.iNull(txtHGateEntryNo.getTag()).longValue() > 0) {
                    txtHTransporter.setEnabled(false);
                } else {
                    txtHTransporter.setEnabled(true);
                }

                if (blnStatus && t1Row != null) {
                    txtHTransporter.setTag(DmShared.xNull(t1Row.getTransporterCode()));
                    txtHTransporter.setText(DmShared.xNull(t1Row.getTransportername()));
                    if (flagFillGrid) {
                        fillGateEntryDetail(DmShared.xNull(txtHGateEntryNo.getTag()), location);
                    }
                }

            }
        });

        txtHRCMApplicable.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHRCMApplicable(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
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

        updatePopup.addCallBack(new InventoryUpdatePopup.CallBack() {
            @Override
            public void onOk(Grn_Mdl mrTemp) {
                if (mrTemp != null) {
                    txtChallanNo.setText(DmShared.xNull(mrTemp.getChallanNo()));
                    txtChallanDate.setText(mrTemp.getChallanDate());
                    txtHTransporter.setTag(mrTemp.getTransporterCode());
                    txtHTransporter.setTag2nd(mrTemp.getTransporterCode());
                    txtHTransporter.setText(mrTemp.getTransporterName());
                    txtEwayNo.setText(mrTemp.getEwayNo());
                    txtEwayDate.setText(mrTemp.getEwayDate());
                    txtLRNo.setText(mrTemp.getLrNo());
                    txtLRDate.setText(mrTemp.getLrDate());
                    txtRemark.setText(mrTemp.getRemark());
                }

                if (DmShared.xNull(mrTemp.getChallanNo()).equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Ref. No. is mandatory.", txtChallanNo).show();
                    return;
                }
                if (DmShared.xNull(mrTemp.getChallanDate()).equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Ref. Date is mandatory.", txtChallanDate).show();
                    return;
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

    private void fillGateEntryDetail(String gateDocId, String location) {
        SrvAsync.invokeRPC(true).fillGateEntryDetail(DmShared.xNull(txtHVoucherType.getTag()), gateDocId, txtHSupplier.getTag(), location, txtChallanDate.getText(), txtVoucherDate.getText(), defaultValue.getIsSiteStateUT(), new AsyncCallback<DmResult<List<GrdGrnDetail_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHGateEntryNo).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdGrnDetail_Mdl>> result) {
                if (result.isStatus()) {
                    grdGRNDetail.getGrid().getStore().clear();
                    grdGRNDetail.getGrid().getStore().addAll(result.getData());
                    fillItemDetail(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHGateEntryNo).show();
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
        grnDetailGridEdit.addCallBack(new GrnGridEdit.CallBack() {

            @Override
            public void onOk(GrdGrnDetail_Mdl grnDetail) {
                grdGRNDetail.commitChanges();
                syncronizeLineService(true, true);
            }

            @Override
            public void onDelete() {
                grdGRNDetail.commitChanges();
                syncronizeLineService(true, true);
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

        serviceDetailGrid.addCallBack(new ServiceGrid.CallBack() {
            @Override
            public void onOk(ServiceDetail_Mdl serviceDetail_Mdl
            ) {
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

    private void showGrnEntryGridEdit(int currentRowIndex) {
        if (DmShared.xNull(txtHVoucherType.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please select GRN Type", txtHVoucherType).show();
            return;
        }
        if (DmShared.dNull(txtVoucherDate.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please fill GRN Date", txtVoucherDate).show();
            return;
        }
        if (DmShared.xNull(txtHSupplier.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please select Supplier", txtHSupplier).show();
            return;
        }
        if (DmShared.xNull(txtChallanNo.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please fill Ref. No.", txtChallanNo).show();
            return;
        }
        if (DmShared.dNull(txtChallanDate.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please fill Ref. Date", txtChallanDate).show();
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
        } else if (DmShared.vNull(billedQty) != 0) {
            mode = "Browse";
            msg = "Record has been used, can't change record.";
        }
        if (mode.trim().equalsIgnoreCase("Browse")) {
            if (!topControl.getMode().equalsIgnoreCase("Browse") && !msg.equalsIgnoreCase("")) {
                new DmAlertMessageBox(msg, (DmTextBox) null).show();
            }
            grnDetailGridEdit.show(currentRowIndex, false, false, false, voucherCategory);
        } else {
            grnDetailGridEdit.show(currentRowIndex, true, true, true, voucherCategory);
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
            } else if (DmShared.dNull(txtChallanDate.getText()).trim().equals("")) {
                new DmAlertMessageBox("Please enter Ref. Date.", txtChallanDate).show();
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
                new DmAlertMessageBox("GRN Date should be less than or equal to current date.", txtVoucherDate).show();
                return;
            }
        } else if (event.getSource() == txtChallanDate) {
            txtChallanDate.setText(DmClient.returnDate(txtChallanDate.getText()));
            if ((DmShared.convertDate(txtChallanDate.getText()).getTime() > (DmShared.convertDate(DmShared.dNull(txtVoucherDate.getText())).getTime()))) {
                new DmAlertMessageBox("Ref. Date should be less than or equal to Voucher Date.", txtChallanDate).show();
                return;
            }
            if (poDashboardFlag) {      //<Ag> TBC
                serviceDetailGrid.getServiceGrid().getGrid().getStore().clear();
                serviceDetailGrid.getServiceGrid().getGrid().getStore().add(new ServiceDetail_Mdl());
                serviceDetailGrid.initialize(setServiveDefaultValue());
                syncronizeLineService(false, true);
            }
            resetGstDetails();
        } else if (event.getSource() == txtEwayDate) {
            txtEwayDate.setText(DmClient.returnDate(txtEwayDate.getText()));
        } else if (event.getSource() == txtLRDate) {
            txtLRDate.setText(DmClient.returnDate(txtLRDate.getText()));
        } else if (event.getSource() == txtFinalDiscountPer
                || event.getSource() == txtFinalDiscount) {
            setLineFinalDiscount();
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

    public Grn(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<Grn_SrvAsync>(GWT.create(Grn_Srv.class), Grn.class.getName());
        walkThrough = new WalkThrough(SrvAsync);
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        findEvents();
        textHelpEvents();
        gridEvents();

        gui.setBannerVisible(false);
        iniDefaultValues(swi);
        manageEvent();
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
                    guiTemplateCode = defaultValue.getVoucherType().getGuiTemplateCode();
                    gui.initializeViaTemplateCode();
                    taxSummary.initialize(defaultValue.isGstApplicable());

                    tabPanel2.clear();
                    tabPanel2.add(vl.add(new ServiceGrid(625, 120, defaultValue.isIsServiceGridActiveInGrn())), "Service");
                    tabPanel2.add(vl.add(taxSummary), "Tax Summary");

                    if (!defaultValue.isIsServiceGridActiveInGrn()) {
                        txtFinalDiscountPer.setEnabled(false);
                        txtFinalDiscount.setEnabled(false);
                    }

                    swi.setWidget(Grn.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void topControlSave() {
        Grn_Mdl mrTemp;
        try {
            syncronizeLineService(true, false);
            defaultValue.getNewVoucherType().setSharedKeyFieldCode(txtHVoucherType.getTag());
            defaultValue.getNewVoucherType().setSharedKeyFieldName(txtHVoucherType.getText());
            defaultValue.getNewVoucherType().setVoucherCategory(voucherCategory);
            defaultValue.getNewVoucherType().setBalancingAc(balancingAc);
            defaultValue.getNewVoucherType().setGuiTemplateCode(guiTemplateCode);
            defaultValue.setNewVoucherDate(txtVoucherDate.getText());
            mrTemp = topControlSaveInitialize();
            Grn_Vld.chkSave(mrTemp);
            ServiceGrid_Vld.chkSave(mrTemp.getGrdServiceDetail());
            SrvAsync.invokeRPC().saveRecord(mrTemp, previous_Mdl, DmClient.getVoucherPrefix(), isApprove, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        poDashboardFlag = false;
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

    private void checkMaybeLabData() {
        String message = "", lineNo = "";
        try {
            for (int i = 0; i < grdGRNDetail.getGrid().getStore().getAll().size(); i++) {
                GrdGrnDetail_Mdl detail_Mdl = grdGRNDetail.getGrid().getStore().getAll().get(i);
                if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() > 0) {
                    if (DmShared.iNull(detail_Mdl.getIsLabRequired()).intValue() == STEnum.LabRequired.MAY_BE.getId()) {
                        lineNo += (i + 1);
                    }
                }
            }

            if (!DmShared.xNull(lineNo).equalsIgnoreCase("")) {
                String[] str = lineNo.split("");
                lineNo = "";
                lineNo += str[0];
                for (int i = 1; i < str.length; i++) {
                    lineNo += ", " + str[i];
                }

                message = "<div style='float:right; margin-right: 5px;'>";
                message += "Lab data not found for following items. Do you want to continue? - <br>";
                message += "<b>Line No. : " + lineNo + "</b>";
                message += "</div>";
            }

            if (!DmShared.xNull(message).trim().equalsIgnoreCase("")) {
                new DmConfirmMessageBox(message, new DialogHideEvent.DialogHideHandler() {
                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.NO) {
                            throw new DmException(800, "");
                        } else {
                            topControlSave();
                        }
                    }
                }).show();
            } else {
                topControlSave();
            }
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getErrorSource(), e.getErrorSourceCode(), e.getMessage());
        }
    }

    private Grn_Mdl topControlSaveInitialize() {
        Grn_Mdl mrTemp = new Grn_Mdl();

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
        mrTemp.setBalancingAc(DmShared.xNull(balancingAc));
        mrTemp.setWtaCode(DmShared.iNull(wtaCode).toString());
        mrTemp.setVoucherDate(DmShared.dNull(txtVoucherDate.getText()).trim());
        mrTemp.setSupplierStateCode(DmShared.xNull(txtHSupplier.getTag2nd()));
        mrTemp.setSupplierTag(DmShared.xNull(txtHSupplier.getTag()));
        mrTemp.setSupplierGstin(DmShared.xNull(supplierGstin));
        mrTemp.setSupplierGstinDate(DmShared.xNull(supplierGstinDate));

        mrTemp.setChallanNo(DmShared.xNull(txtChallanNo.getText()).trim());
        mrTemp.setChallanDate(DmShared.dNull(txtChallanDate.getText()));
        mrTemp.setRemark(DmShared.xNull(txtRemark.getText()).trim());

        mrTemp.setGateNoTag(DmShared.xNull(txtHGateEntryNo.getTag()));
        mrTemp.setTransporterCode(DmShared.xNull(txtHTransporter.getTag()));
        mrTemp.setEwayNo(DmShared.xNull(txtEwayNo.getText()).trim());
        mrTemp.setEwayDate(DmShared.dNull(txtEwayDate.getText()));
        mrTemp.setLrNo(DmShared.xNull(txtLRNo.getText()).trim());
        mrTemp.setLrDate(DmShared.dNull(txtLRDate.getText()));

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
        //=================== ************* ===================//

        //Accounts               
        mrTemp.setFinalDiscountAcCode(DmShared.xNull(txtFinalDiscount.getTag()));
        mrTemp.setRoundOffAcCode(DmShared.xNull(txtRoundoff.getTag()));

        //=========================(Removing Blank Row in GRID)=========================================
        List<GrdGrnDetail_Mdl> grdLstGrnDetail_Mdl = new ArrayList<>();

        grdLstGrnDetail_Mdl.addAll(grdGRNDetail.getGrid().getStore().getAll());

        if (grdGRNDetail.getGrid().getStore().getAll().size() >= 0) {
            int i = 0;
            for (GrdGrnDetail_Mdl grnDetail_Mdl : grdGRNDetail.getGrid().getStore().getAll()) {
                if (DmShared.iNull(grnDetail_Mdl.getSharedKeyFieldCode()).longValue() == 0) {
                    grdLstGrnDetail_Mdl.remove(i);
                    i--;
                }
                i++;
            }
            if (grdLstGrnDetail_Mdl.size() < grdGRNDetail.getGrid().getStore().getAll().size()) {
                grdGRNDetail.getGrid().getStore().clear();
                grdGRNDetail.getGrid().getStore().addAll(grdLstGrnDetail_Mdl);
                grdGRNDetail.commitChanges();
            }

        }
        mrTemp.getGrdGrnDetail().clear();
        mrTemp.getGrdGrnDetail().addAll(grdGRNDetail.getGrid().getStore().getAll());

        mrTemp.getGrdServiceDetail().clear();
        mrTemp.getGrdServiceDetail().addAll(serviceDetailGrid.getServiceGrid().getGrid().getStore().getAll());

        mrTemp.setInventoryAccountingActive(defaultValue.isInventoryAccountingActive());

        //-------------------------------------------------------------------------------------
        mrTemp.setGuiMdl(gui.getGUI_Mdl());
        mrTemp.setGuiConfigCode(gui.getConfigCode());

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
                new DmAlertMessageBox(errorMessage, txtChallanNo).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtChallanDate).show();
                break;
            case 600:
                new DmAlertMessageBox(errorMessage, txtHRCMApplicable).show();
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
                new DmAlertMessageBox(errorMessage, txtEwayNo).show();
                break;
            case 900:
                new DmAlertMessageBox(errorMessage, txtEwayDate).show();
                break;
            case 1000:
                new DmAlertMessageBox(errorMessage, txtLRNo).show();
                break;
            case 1100:
                new DmAlertMessageBox(errorMessage, txtLRDate).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtRemark).show();
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
        poDashboardFlag = false;
        lstPo = null;
        defaultValue.setIsContinuousNewMode(false);
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<Grn_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<Grn_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<Grn_Mdl> result) {
        clear();
        if (result.isStatus()) {
            Grn_Mdl mrTemp = result.getData();
            previous_Mdl = result.getData().clone();

            topControl.setDocId(mrTemp.getSharedKeyFieldCode());
            txtVoucherNo.setText(mrTemp.getSharedKeyFieldName());
            txtVoucherNo.setTag(mrTemp.getU_EntDt_LatestLine());
            txtVoucherDate.setText(mrTemp.getVoucherDate());
            txtVoucherDate.setTag(mrTemp.getVoucherDate());
            txtHVoucherType.setTag2nd(mrTemp.getVoucherTypeCode());
            txtHVoucherType.setTag(mrTemp.getVoucherTypeCode());
            txtHVoucherType.setText(mrTemp.getVoucherTypeName());
            voucherCategory = mrTemp.getVoucherCategory();
            balancingAc = mrTemp.getBalancingAc();
            wtaCode = mrTemp.getWtaCode();
            txtHSupplier.setTag2nd(mrTemp.getSupplierStateCode());
            txtHSupplier.setTag(mrTemp.getSupplierTag());
            txtHSupplier.setText(mrTemp.getSupplierText());
            txtChallanDate.setText(mrTemp.getChallanDate());
            txtChallanNo.setText(mrTemp.getChallanNo());
            txtRemark.setText(mrTemp.getRemark());
            lastTransactionDate = mrTemp.getLastTransactionDate();

            txtHGateEntryNo.setTag(mrTemp.getGateNoTag());
            txtHGateEntryNo.setTag2nd(mrTemp.getGateNoTag());
            txtHGateEntryNo.setText(mrTemp.getGateNoText());
            txtHTransporter.setTag(mrTemp.getTransporterCode());
            txtHTransporter.setTag2nd(mrTemp.getTransporterCode());
            txtHTransporter.setText(mrTemp.getTransporterName());
            txtEwayNo.setText(mrTemp.getEwayNo());
            txtEwayDate.setText(mrTemp.getEwayDate());
            txtLRNo.setText(mrTemp.getLrNo());
            txtLRDate.setText(mrTemp.getLrDate());
            txtHRCMApplicable.setTag(DmShared.xNull(mrTemp.getRcmApplicable()));
            txtHRCMApplicable.setTag2nd(DmShared.xNull(mrTemp.getRcmApplicable()));
            if (DmShared.xNull(mrTemp.getRcmApplicable()).equalsIgnoreCase("1")) {
                txtHRCMApplicable.setText("Yes");
            } else if (DmShared.xNull(mrTemp.getRcmApplicable()).equalsIgnoreCase("0")) {
                txtHRCMApplicable.setText("No");
            }

            selectedSupplierCode = mrTemp.getSupplierTag();
            supplierGstin = mrTemp.getSupplierGstin();
            supplierGstinDate = mrTemp.getSupplierGstinDate();
            partyTypeForEntry = mrTemp.getPartyTypeForEntry();
            partyType = mrTemp.getPartyType();
            location = mrTemp.getLocationCode();
            isPurchaseOrderExists = mrTemp.getIsPurchaseOrderExists();

            userControl.setTextBlog(preparedByBlog, mrTemp.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, mrTemp.getSharedModifiedBy());
            userControl.setTextBlog(approvedByBlog, mrTemp.getApprovedBy());

            serviceDetailGrid.getServiceGrid().getGrid().getStore().clear();
            serviceDetailGrid.getServiceGrid().getGrid().getStore().addAll(mrTemp.getGrdServiceDetail());

            //==============================================================================
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
            resetTaxSummary();

            serviceDetailGrid.initialize(setServiveDefaultValue());
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
        clearGrid();
        SrvAsync.invokeRPC().getGrnDetail(DmShared.xNull(txtHVoucherType.getTag()),
                listKF,
                txtHSupplier.getTag(),
                location,
                DmShared.xNull(txtChallanDate.getText()).equalsIgnoreCase("") ? txtVoucherDate.getText() : txtChallanDate.getText(),
                topControl.getMode().equalsIgnoreCase("New") ? "" : topControl.getDocId(),
                txtVoucherDate.getText(),
                defaultValue.getIsSiteStateUT(),
                new AsyncCallback<DmResult<List<GrdGrnDetail_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdGrnDetail_Mdl>> result) {
                if (result.isStatus()) {
                    lstPo = null;
                    grdGRNDetail.getGrid().getStore().clear();
                    grdGRNDetail.getGrid().getStore().addAll(result.getData());
                    if (DmShared.xNull(txtHGateEntryNo.getTag()).trim().equals("")) {
                        grdGRNDetail.getGrid().getStore().add(new GrdGrnDetail_Mdl());
                    }
                    fillItemDetail(result.getData());
                    txtChallanNo.setFocus(true);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void fillItemDetail(List<GrdGrnDetail_Mdl> listGRN) {
        String receiveQty = "", acceptQty = "", rate = "";
        String labRejectedQty = "", labRejectedAmt = "";
        int purchaseUnitDecimalPlaces = 4, skuDecimalPlaces = 7;
        for (int i = 0; i < listGRN.size(); i++) {
            GrdGrnDetail_Mdl detail_Mdl = listGRN.get(i);
            if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() > 0) {

                //<editor-fold defaultstate="collapsed" desc="If lab test detail exists then cal. following">
                receiveQty = DmShared.xNull(grdGRNDetail.getGrid().getStore().get(i).getRecQty());
                rate = DmShared.xNull(grdGRNDetail.getGrid().getStore().get(i).getRate());
                purchaseUnitDecimalPlaces = DmShared.iNull(grdGRNDetail.getGrid().getStore().get(i).getPurchaseUnitDecimalPlaces()).intValue();
                skuDecimalPlaces = DmShared.iNull(grdGRNDetail.getGrid().getStore().get(i).getSkuDecimalPlaces()).intValue();

                //cal. Lab rejected qty
                labRejectedQty = calculateFormulaForQty(grdGRNDetail.getGrid().getStore().get(i).getLstLabEntry(), receiveQty, rate, purchaseUnitDecimalPlaces);
                grdGRNDetail.getGrid().getStore().get(i).setLabRejectedQty(labRejectedQty);

                //cal. total rejected qty
                grdGRNDetail.getGrid().getStore().get(i).setRejQty(labRejectedQty);

                //cal. accept qty
                acceptQty = DmClient.vNull((DmShared.vNull(receiveQty).doubleValue() - DmShared.vNull(labRejectedQty).doubleValue()), DmShared.getNumberFormat(purchaseUnitDecimalPlaces));
                grdGRNDetail.getGrid().getStore().get(i).setAcceptQty(acceptQty);

                //cal. DrCrNONote Amt
                labRejectedAmt = calculateFormulaForAmt(grdGRNDetail.getGrid().getStore().get(i).getLstLabEntry(), receiveQty, acceptQty, rate);
                grdGRNDetail.getGrid().getStore().get(i).setDrCrNoteAmt(labRejectedAmt);

                //cal. sku qty.
                if (DmShared.vNull(labRejectedQty).doubleValue() != 0) {
                    double tempSkuQty = DmShared.vNull(acceptQty).doubleValue() * DmShared.vNull(grdGRNDetail.getGrid().getStore().get(i).getPurchaseUnitConversionRate()).doubleValue();
                    grdGRNDetail.getGrid().getStore().get(i).setSkuDifferenceQty("0");
                    grdGRNDetail.getGrid().getStore().get(i).setSkuQty(DmClient.vNull(tempSkuQty, DmShared.getNumberFormat(skuDecimalPlaces)));
                }

                //</editor-fold>
                grdGRNDetail.commitChanges();
                grnDetailGridEdit.displayRow(detail_Mdl);
                grnDetailGridEdit.calculate();
                grnDetailGridEdit.fillRowData(detail_Mdl);
            }
        }
        grdGRNDetail.commitChanges();
        calculate();
    }

    private void manageEvent() {
        btnApprovedBy.addClickHandler(this);
        btnAddOrder.addClickHandler(this);
        flwForm.addAttachHandler(this);
        txtChallanDate.addBlurHandler(this);
        txtVoucherDate.addBlurHandler(this);
        txtEwayDate.addBlurHandler(this);
        txtLRDate.addBlurHandler(this);
        txtFinalDiscountPer.addBlurHandler(this);
        txtFinalDiscount.addBlurHandler(this);

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
        txtFinalDiscount.setEnabled(false);
        txtFinalDiscountPer.setEnabled(false);

        if (topControl.getMode().equalsIgnoreCase("Edit")) {
            txtHVoucherType.setEnabled(false);
            txtHRCMApplicable.setEnabled(false);
            if (isPurchaseOrderExists || DmShared.iNull(txtHGateEntryNo.getTag()).longValue() != 0) {
                txtHSupplier.setEnabled(false);
            }
            if (DmShared.iNull(txtHGateEntryNo.getTag()).longValue() != 0) {
                txtHTransporter.setEnabled(false);
            }
        }
        flwAddOrder.clear();
        if (flag) {
            flwAddOrder.add(btnAddOrder);
            if (!defaultValue.isInventoryAccountingActive()) {
                txtVoucherDate.setEnabled(dateEditable);
            }
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
        txtVoucherDate.setTag(txtVoucherDate.getText());
        txtHVoucherType.setTag2nd(defaultValue.getIsContinuousNewMode() ? defaultValue.getNewVoucherType().getSharedKeyFieldCode() : defaultValue.getVoucherType().getSharedKeyFieldCode());
        txtHVoucherType.setTag(defaultValue.getIsContinuousNewMode() ? defaultValue.getNewVoucherType().getSharedKeyFieldCode() : defaultValue.getVoucherType().getSharedKeyFieldCode());
        txtHVoucherType.setText(defaultValue.getIsContinuousNewMode() ? defaultValue.getNewVoucherType().getSharedKeyFieldName() : defaultValue.getVoucherType().getSharedKeyFieldName());
        voucherCategory = defaultValue.getIsContinuousNewMode() ? defaultValue.getNewVoucherType().getVoucherCategory() : defaultValue.getVoucherType().getVoucherCategory();
        balancingAc = defaultValue.getIsContinuousNewMode() ? defaultValue.getNewVoucherType().getBalancingAc() : defaultValue.getVoucherType().getBalancingAc();
        guiTemplateCode = defaultValue.getIsContinuousNewMode() ? defaultValue.getNewVoucherType().getGuiTemplateCode() : defaultValue.getVoucherType().getGuiTemplateCode();
        txtRoundoff.setTag(DmShared.xNull(defaultValue.getRoundOffAcCode()));
        txtFinalDiscount.setTag(DmShared.xNull(defaultValue.getFinalDicountAcCode()));
        singlePOFlowInPurchase = defaultValue.isSinglePOFlowInPurchase() ? true : false;
        setRCM(voucherCategory);
        if (!(DmShared.iNull(defaultValue.getIsSiteStateUT()).intValue() == 1)) {
            lblSgstUgst.setText(DmEnum.GSTS_Head.SGST.getName());
        } else if ((DmShared.iNull(defaultValue.getIsSiteStateUT()).intValue() == 1)) {
            lblSgstUgst.setText(DmEnum.GSTS_Head.UGST.getName());
        }

        gui.initializeViaTemplateCode();
        if (lstPo != null) {
            poDashboardFlag = true;
            if (lstPo.get(0) != null) {
                selectedSupplierCode = lstPo.get(0).getSharedPreparedBy();
                getSupplierDetails(selectedSupplierCode);
            }
        }
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
        partyType = "";
        partyTypeForEntry = "";
        supplierGstin = "";
        supplierGstinDate = "";
        location = "";
        wtaCode = "";
        voucherCategory = "";
        balancingAc = "";
        guiTemplateCode = "";
        isPurchaseOrderExists = false;

        txtSGST.setText("");
        txtSGST.setTag("");
        txtSGST.setTag2nd("");

        txtUGST.setText("");
        txtUGST.setTag("");
        txtUGST.setTag2nd("");

        gui.clear();
        previous_Mdl = null;
        grdGRNDetail.getGrid().getStore().clear();
        serviceDetailGrid.getServiceGrid().getGrid().getStore().clear();
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        userControl.setTextBlog(approvedByBlog, "");
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
        if (DmShared.xNull(vCat).equalsIgnoreCase(new ProjectVoucherCategory().PURCHASE_CHALLAN_UNREGISTERED)) {
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

        grdGRNDetail.commitChanges();

        for (GrdGrnDetail_Mdl detail_Mdl : grdGRNDetail.getGrid().getStore().getAll()) {
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
            if (!defaultValue.isIncludeOverHeadsInCost()) {
                landedValue = DmShared.vNull(landedValue).doubleValue()
                        - DmShared.vNull(txtServiceAmount.getText()).doubleValue()
                        - DmShared.vNull(serviceTaxValue).doubleValue();
            }
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
    }

    private void calculateLandedValue() {

        double landedValue = 0, totalGrossAmount = 0, lineGrossAmount = 0;

        landedValue = DmShared.vNull(txtLandedValue.getText());

        for (GrdGrnDetail_Mdl detailMdl : grdGRNDetail.getGrid().getStore().getAll()) {
            if (detailMdl != null) {
                if (!DmShared.xNull(detailMdl.getSharedKeyFieldCode()).trim().equals("")) {
                    if (!detailMdl.isServiceRecord()) {
                        totalGrossAmount += DmShared.vNull(detailMdl.getAmount());
                    }
                }
            }
        }

        double landedSkuValue = 0, totalLandedSkuValue = 0, landedSkuRate = 0;
        GrdGrnDetail_Mdl lastLineMdl = null;
        for (GrdGrnDetail_Mdl lineMdl : grdGRNDetail.getGrid().getStore().getAll()) {
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

        grdGRNDetail.commitChanges();

    }

    private void resetTaxSummary() {
        List<List<OverHeadLine_Mdl>> lstOverheadLine = new ArrayList<List<OverHeadLine_Mdl>>();
        List<OverHeadLine_Mdl> overheadLine = null;
        OverHeadLine_Mdl overheadLineRow = null;

        for (GrdGrnDetail_Mdl grdMdl : grdGRNDetail.getGrid().getStore().getAll()) {
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
        hashMapFilter.put("docId", new FieldUDT_Mdl("GRN No.", DmShared.xNull(txtVoucherNo.getText()), DmShared.xNull(topControl.getDocId())));
        new OpenSourceReport(DmShared.xNull(txtHVoucherType.getTag()), "", "Goods Receive Note", widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
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
                    lstPo = (ArrayList) object;
                    getDefaultVoucherType();
                    break;
                case "FOCUS_ON_ADD":
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            txtChallanNo.setFocus(true);
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

        SrvAsync.invokeRPC().getSupplierDetails(supplierCode, voucherCategory, false, txtHVoucherType.getTag(), dmHelper, false, new AsyncCallback<DmResult<TxtHSupplier_Mdl>>() {

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
            resetGstDetails();
        }

        if (!DmShared.xNull(selectedSupplierCode).equalsIgnoreCase(DmShared.xNull(txtHSupplier.getTag()))) {

            txtHGateEntryNo.setTag("");
            txtHGateEntryNo.setText("");
            txtHGateEntryNo.setTag2nd("");

            txtHTransporter.setTag("");
            txtHTransporter.setText("");
            txtHTransporter.setTag2nd("");

            selectedSupplierCode = DmShared.xNull(txtHSupplier.getTag());
            clearGrid();
            if (lstPo != null) {
                fillGrnDetail(lstPo);
            }
        }
    }

    private void getDefaultVoucherType() {
        String partyType = lstPo.get(0).getSharedModifiedBy();

        SrvAsync.invokeRPC(true).getVoucherType(partyType, new AsyncCallback<DmResult<TxtHVoucherType_Mdl>>() {

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

    //<editor-fold defaultstate="collapsed" desc="formulaForQty">
//    protected void calculateFormulaForQty(GrdLabEntry_Mdl impurityMdl) {
    protected String calculateFormulaForQty(List<GrdLabEntry_Mdl> listGrid, String receiveQty, String rate, int purchaseUnitDecimalPlaces) {
//        List<GrdLabEntry_Mdl> listGrid = dmGrdLab.getGrid().getStore().getAll();
        String orignalFormula = "", tempFormula = "", convertedFormula = "", impurityManualCode = "", formulaValue = "";
        boolean flag = false;
        double tempValue, totalValue = 0;

        for (int i = 0; i < listGrid.size(); i++) {
            orignalFormula = DmShared.xNull(listGrid.get(i).getFormulaForQty()).trim().toUpperCase();
            convertedFormula = "";
            if (!orignalFormula.trim().equalsIgnoreCase("")) {
                for (int j = 0; (j < orignalFormula.length()); j++) {
                    tempFormula = orignalFormula.substring(j, j + 1);
                    if (flag) {
                        if (tempFormula.equalsIgnoreCase("]") || tempFormula.equalsIgnoreCase("}")) {
                            flag = false;

                            convertedFormula = convertedFormula + NumberFormat.getFormat("0.00").format(getLabOhValue(impurityManualCode, listGrid.get(i), receiveQty, "0", rate));

                        } else {
                            impurityManualCode = impurityManualCode + tempFormula;
                        }
                    } else {
                        if (tempFormula.equalsIgnoreCase("[") || tempFormula.equalsIgnoreCase("{")) {
                            impurityManualCode = "";
                            flag = true;
                        } else {
                            convertedFormula = convertedFormula + tempFormula;
                        }
                    }
                }

                if (convertedFormula.trim().equalsIgnoreCase("")) {
                    convertedFormula = "0";
                }

                tempValue = DmShared.compute(convertedFormula);

                if (Double.isInfinite(tempValue)
                        || Double.isNaN(tempValue)) {
                    tempValue = 0d;
                }

                listGrid.get(i).setLabQty(DmClient.vNull(tempValue, DmShared.getNumberFormat(purchaseUnitDecimalPlaces)));
                totalValue = totalValue + DmShared.vNull(listGrid.get(i).getLabQty());
            }
        }

        formulaValue = DmClient.vNull(totalValue, DmShared.getNumberFormat(purchaseUnitDecimalPlaces));
//        dmGrdLab.commitChanges();
        return formulaValue;
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="formulaForAmt">
//    protected void calculateFormulaForAmt(GrdLabEntry_Mdl impurityMdl) {
    protected String calculateFormulaForAmt(List<GrdLabEntry_Mdl> listGrid, String receiveQty, String acceptQty, String rate) {
//        List<GrdLabEntry_Mdl> listGrid = dmGrdLab.getGrid().getStore().getAll();
        String orignalFormula = "", tempFormula = "", convertedFormula = "", labOhManualCode = "", formulaValue = "";
        boolean flag = false;
        double tempValue, totalValue = 0;

        for (int i = 0; i < listGrid.size(); i++) {
            orignalFormula = DmShared.xNull(listGrid.get(i).getFormulaForAmt()).trim().toUpperCase();
            convertedFormula = "";
            if (!orignalFormula.trim().equalsIgnoreCase("")) {
                for (int j = 0; (j < orignalFormula.length()); j++) {
                    tempFormula = orignalFormula.substring(j, j + 1);
                    if (flag) {
                        if (tempFormula.equalsIgnoreCase("]") || tempFormula.equalsIgnoreCase("}")) {
                            flag = false;

                            convertedFormula = convertedFormula + NumberFormat.getFormat("0.00").format(getLabOhValue(labOhManualCode, listGrid.get(i), receiveQty, acceptQty, rate));

                        } else {
                            labOhManualCode = labOhManualCode + tempFormula;
                        }
                    } else {
                        if (tempFormula.equalsIgnoreCase("[") || tempFormula.equalsIgnoreCase("{")) {
                            labOhManualCode = "";
                            flag = true;
                        } else {
                            convertedFormula = convertedFormula + tempFormula;
                        }
                    }
                }

                if (convertedFormula.trim().equalsIgnoreCase("")) {
                    convertedFormula = "0";
                }

                tempValue = DmShared.compute(convertedFormula);

                if (Double.isInfinite(tempValue)
                        || Double.isNaN(tempValue)) {
                    tempValue = 0d;
                }

                listGrid.get(i).setLabAmt(String.valueOf(tempValue));
                totalValue = totalValue + DmShared.vNull(listGrid.get(i).getLabAmt());
            }
        }
        formulaValue = DmClient.vNull(totalValue, "0.00");
//        impurityMdl.setLabAmt(formulaValue);
//        dmGrdLab.commitChanges();
        return formulaValue;
    }
//</editor-fold>

    private double getLabOhValue(String labOhManualCode, GrdLabEntry_Mdl impurityMdl, String receiveQty, String acceptQty, String rate) {
        double rtnValue = 0;

        if (null != LabOverhead.getViaKey(labOhManualCode)) {
            switch (LabOverhead.getViaKey(labOhManualCode)) {
                case RANGE1:
                    rtnValue = DmShared.vNull(impurityMdl.getRange1());
                    break;
                case RANGE2:
                    rtnValue = DmShared.vNull(impurityMdl.getRange2());
                    break;
                case FOUND_VALUE:
                    rtnValue = DmShared.vNull(impurityMdl.getMainFoundValue());
                    break;
                case RECIEVE_QTY:
                    rtnValue = DmShared.vNull(receiveQty);
                    break;
                case SUB_RESULT_QTY:
                    rtnValue = 0;
                    break;
                case SUB_RESULT_AMOUNT:
                    rtnValue = 0;
                    break;
                case RATE:
                    rtnValue = DmShared.vNull(rate);
                    break;
                case ACCEPT_QTY:
                    rtnValue = DmShared.vNull(acceptQty);
                    break;
                default:
                    break;
            }
        }
        return rtnValue;
    }

    private void clearGrid() {
        serviceDetailGrid.getServiceGrid().getGrid().getStore().clear();
        serviceDetailGrid.getServiceGrid().getGrid().getStore().add(new ServiceDetail_Mdl());
        serviceDetailGrid.initialize(setServiveDefaultValue());

        grdGRNDetail.getGrid().getStore().clear();
        grdGRNDetail.getGrid().getStore().add(new GrdGrnDetail_Mdl());
        syncronizeLineService(false, true);
        calculate();
    }

    private void clearLineFinalDiscount() {
        double lineSubTotal = 0;
        for (int i = 0; i < grdGRNDetail.getGrid().getStore().getAll().size(); i++) {
            GrdGrnDetail_Mdl grdGrnDetails_Mdl = grdGRNDetail.getGrid().getStore().get(i);
            if (!grdGRNDetail.getGrid().getStore().get(i).isServiceRecord()) {
                if (DmShared.iNull(grdGrnDetails_Mdl.getSharedKeyFieldCode()).longValue() != 0) {
                    lineSubTotal = DmShared.vNull(grdGRNDetail.getGrid().getStore().get(i).getSubTotal()).doubleValue();
                    grdGRNDetail.getGrid().getStore().get(i).setFinalDiscount("");
                    grdGRNDetail.getGrid().getStore().get(i).setFinalDiscountPer("");
                    grdGRNDetail.getGrid().getStore().get(i).setItemValue(DmClient.vNull(lineSubTotal, "0.00"));
                }
            }
        }
        grdGRNDetail.commitChanges();
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
                for (GrdGrnDetail_Mdl grdGrnDetail_Mdl : grdGRNDetail.getGrid().getStore().getAll()) {
                    if (grdGrnDetail_Mdl.getSharedKeyFieldCode() != null) {
                        if (!grdGrnDetail_Mdl.isServiceRecord()) {
                            if ((DmShared.iNull(grdGrnDetail_Mdl.getSharedKeyFieldCode()).longValue() != 0)) {
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

            for (int i = 0; i < grdGRNDetail.getGrid().getStore().getAll().size(); i++) {
                if (grdGRNDetail.getGrid().getStore().get(i).getSharedKeyFieldCode() != null) {
                    if (!grdGRNDetail.getGrid().getStore().get(i).isServiceRecord()) {
                        if (DmShared.iNull(grdGRNDetail.getGrid().getStore().get(i).getSharedKeyFieldCode()).longValue() != 0) {
                            flag = true;
                            if (flag) {
                                lineTaxableAmount = DmShared.vNull(grdGRNDetail.getGrid().getStore().get(i).getTaxableAmt()).doubleValue();
                                lineSubTotal = DmShared.vNull(grdGRNDetail.getGrid().getStore().get(i).getSubTotal()).doubleValue();
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

                                grdGRNDetail.getGrid().getStore().get(i).setFinalDiscount(DmClient.vNull(lineFinalDiscount, "0.00"));
                                grdGRNDetail.getGrid().getStore().get(i).setFinalDiscountPer(DmClient.vNull(txtFinalDiscountPer.getText(), "0.00"));
                                grdGRNDetail.getGrid().getStore().get(i).setItemValue(DmClient.vNull(lineSubTotal - lineFinalDiscount, "0.00"));
                                flag = false;
                            }
                        }
                    }
                }
            }
        }
        grdGRNDetail.commitChanges();
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
        paramHashMap.put("IS_SERVICE_AC_MANDATORY", defaultValue.isInventoryAccountingActive());
        paramHashMap.put("IS_TAX_AC_MANDATORY", false);

        return paramHashMap;
    }

    private void syncronizeLineService(boolean syncronizeLineHeader, boolean addServiceRow) {
        serviceDetailGrid.initialize(setServiveDefaultValue());
        //<editor-fold defaultstate="collapsed" desc="First Syncronize Line Service With Item Grid">
        serviceTaxValue = 0;
        if (syncronizeLineHeader) {
            calculate();
        }

        List<GrdGrnDetail_Mdl> grdLstItemDetail_Mdl = new ArrayList<>();

        grdLstItemDetail_Mdl.addAll(grdGRNDetail.getGrid().getStore().getAll());

        int i = 0;
        for (GrdGrnDetail_Mdl itemDetail_Mdl : grdGRNDetail.getGrid().getStore().getAll()) {
            if (itemDetail_Mdl.isServiceRecord()) {
                grdLstItemDetail_Mdl.remove(i);
                i--;
            }
            i++;
        }

        if (grdLstItemDetail_Mdl.size() < grdGRNDetail.getGrid().getStore().getAll().size()) {
            grdGRNDetail.getGrid().getStore().clear();
            grdGRNDetail.getGrid().getStore().addAll(grdLstItemDetail_Mdl);

            int size = grdGRNDetail.getGrid().getStore().size() - 1;
            GrdGrnDetail_Mdl itemDetail_Mdl = grdGRNDetail.getGrid().getStore().get(size);

            if (DmShared.iNull(itemDetail_Mdl.getSharedKeyFieldCode()).longValue() > 0) {
                if (DmShared.xNull(txtHGateEntryNo.getTag()).trim().equals("")) {
                    grdGRNDetail.getGrid().getStore().add(new GrdGrnDetail_Mdl());
                }
            }
            grdGRNDetail.commitChanges();
        }

        List<ServiceDetail_Mdl> serviceDetailLst_Mdls = serviceDetailGrid.getServiceGrid().getGrid().getStore().getAll();

        for (ServiceDetail_Mdl serviceDetail_Mdl : serviceDetailLst_Mdls) {
            if (DmShared.iNull(serviceDetail_Mdl.getSharedKeyFieldCode()).longValue() > 0) {
                serviceDetailGrid.calculate(serviceDetail_Mdl);

                GrdGrnDetail_Mdl grdGrnDetail_Mdl = new GrdGrnDetail_Mdl();

                grdGrnDetail_Mdl.setServiceRecord(true);
                grdGrnDetail_Mdl.setSmfCode(serviceDetail_Mdl.getSmfCode());
                grdGrnDetail_Mdl.setServiceEvalTypeCode(serviceDetail_Mdl.getServiceEvalTypeCode());
                grdGrnDetail_Mdl.setServiceEvalTypeName(serviceDetail_Mdl.getServiceEvalTypeName());
                grdGrnDetail_Mdl.setServiceEvalRate(serviceDetail_Mdl.getServiceEvalRate());

                grdGrnDetail_Mdl.setSharedKeyFieldCode(serviceDetail_Mdl.getSharedKeyFieldCode());
                grdGrnDetail_Mdl.setChallanQty(DmShared.xNull(serviceDetail_Mdl.getQuantity()));
                grdGrnDetail_Mdl.setAcceptQty(DmShared.xNull(serviceDetail_Mdl.getQuantity()));
                grdGrnDetail_Mdl.setPurchaseUnitCode(serviceDetail_Mdl.getUnitCode());

                grdGrnDetail_Mdl.setHsnCode(serviceDetail_Mdl.getHsnCode());
                grdGrnDetail_Mdl.setHsn(serviceDetail_Mdl.getHsn());
                grdGrnDetail_Mdl.setItemGSTNature(serviceDetail_Mdl.getItemGSTNature());
                grdGrnDetail_Mdl.setItsBasedOn(serviceDetail_Mdl.getItsBasedOn());
                grdGrnDetail_Mdl.setGstTypeCode(serviceDetail_Mdl.getGstTypeCode());
                grdGrnDetail_Mdl.setGstTypeName(serviceDetail_Mdl.getGstTypeName());
                grdGrnDetail_Mdl.setManualGstType(serviceDetail_Mdl.isManualGstType());

                //IAP
                grdGrnDetail_Mdl.setIsubCode(serviceDetail_Mdl.getIsubCode());

                //=================== Final Values ===================//
                grdGrnDetail_Mdl.setRate(DmClient.vNull(serviceDetail_Mdl.getRate(), "0.00"));
                grdGrnDetail_Mdl.setAmount(DmClient.vNull(serviceDetail_Mdl.getAmount(), "0.00"));
                grdGrnDetail_Mdl.setDiscountPer("");
                grdGrnDetail_Mdl.setDiscount("");
                grdGrnDetail_Mdl.setTaxableAmt(DmClient.vNull(serviceDetail_Mdl.getTaxableAmt(), "0.00"));
                grdGrnDetail_Mdl.setSgstUgstPer(DmClient.vNull(serviceDetail_Mdl.getSgstOrUgstRate(), "0.00"));
                grdGrnDetail_Mdl.setSgstUgst(DmClient.vNull(serviceDetail_Mdl.getSgstOrUgstAmt(), "0.00"));
                grdGrnDetail_Mdl.setSgstPer(DmClient.vNull(serviceDetail_Mdl.getSgstRate(), "0.00"));
                grdGrnDetail_Mdl.setSgst(DmClient.vNull(serviceDetail_Mdl.getSgstAmt(), "0.00"));
                grdGrnDetail_Mdl.setUgstPer(DmClient.vNull(serviceDetail_Mdl.getUgstRate(), "0.00"));
                grdGrnDetail_Mdl.setUgst(DmClient.vNull(serviceDetail_Mdl.getUgstAmt(), "0.00"));
                grdGrnDetail_Mdl.setCgstPer(DmClient.vNull(serviceDetail_Mdl.getCgstRate(), "0.00"));
                grdGrnDetail_Mdl.setCgst(DmClient.vNull(serviceDetail_Mdl.getCgstAmt(), "0.00"));
                grdGrnDetail_Mdl.setIgstPer(DmClient.vNull(serviceDetail_Mdl.getIgstRate(), "0.00"));
                grdGrnDetail_Mdl.setIgst(DmClient.vNull(serviceDetail_Mdl.getIgstAmt(), "0.00"));
                grdGrnDetail_Mdl.setSubTotal(DmClient.vNull(serviceDetail_Mdl.getSubTotal(), "0.00"));
                grdGrnDetail_Mdl.setFinalDiscount("");
                grdGrnDetail_Mdl.setItemValue(DmClient.vNull(serviceDetail_Mdl.getNetAmt(), "0.00"));
                //=================== ************* ===================//
                serviceTaxValue = serviceTaxValue + DmShared.vNull(serviceDetail_Mdl.getSgstAmt())
                        + DmShared.vNull(serviceDetail_Mdl.getUgstAmt())
                        + DmShared.vNull(serviceDetail_Mdl.getCgstAmt())
                        + DmShared.vNull(serviceDetail_Mdl.getIgstAmt());

                grdGRNDetail.getGrid().getStore().add(grdGrnDetail_Mdl);
            }
        }
        serviceDetailGrid.getServiceGrid().commitChanges();
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Add Serivice Row in Item Grid">
        if (addServiceRow) {
            GrdGrnDetail_Mdl serviceRecord = new GrdGrnDetail_Mdl();
            serviceRecord.setServiceRecord(true);
            grdGRNDetail.getGrid().getStore().add(serviceRecord);
            int serviceRowIndex = grdGRNDetail.getGrid().getStore().getAll().indexOf(serviceRecord);
            serviceDetailGrid.setServiceRowIndex(serviceRowIndex);
        }

        //</editor-fold>
        grdGRNDetail.commitChanges();

        if (syncronizeLineHeader) {
            calculate();
        }

    }
//</editor-fold>

    private Grn_Mdl topControlUpdate() {
        Grn_Mdl mrTemp = new Grn_Mdl();
        try {
            if (DmShared.convertDate(lastTransactionDate).getTime() > DmShared.convertDate(previous_Mdl.getVoucherDate()).getTime()) {

                if (topControl.getMode().equalsIgnoreCase("Edit")) {
                    mrTemp.setSharedKeyFieldCode(topControl.getDocId());
//                    mrTemp.setChallanDate(DmShared.dNull(updatePopup.txtPaymentDueDate.getText()));
                    mrTemp.setChallanNo(DmShared.xNull(updatePopup.txtSupplierBillNo.getText()).trim());
                    mrTemp.setChallanDate(DmShared.xNull(updatePopup.txtSupplierBillDate.getText()).trim());
//                    mrTemp.setSupplierBillDate(DmShared.dNull(updatePopup.txtSupplierBillDate.getText()));
                    mrTemp.setRemark(DmShared.xNull(updatePopup.txtRemark.getText()).trim());
                    mrTemp.setTransporterCode(DmShared.xNull(updatePopup.txtHTransporter.getTag()));
                    mrTemp.setEwayNo(DmShared.xNull(updatePopup.txtEwayNo.getText()).trim());
                    mrTemp.setEwayDate(DmShared.dNull(updatePopup.txtEwayDate.getText()));
                    mrTemp.setLrNo(DmShared.xNull(updatePopup.txtLRNo.getText()).trim());
                    mrTemp.setLrDate(DmShared.dNull(updatePopup.txtLRDate.getText()));
                    mrTemp.setVoucherDate(DmShared.dNull(txtVoucherDate.getText()));
                    mrTemp.setDefaultValueMdl(defaultValue);

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
