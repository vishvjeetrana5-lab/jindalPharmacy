package aarogya.inventory.purchaseReturn.client;

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
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.inventory.purchaseReturn.shared.DefaultValue_Mdl;
import aarogya.inventory.purchaseReturn.shared.Find_Mdl;
import aarogya.inventory.purchaseReturn.shared.GrdItemDetail_Mdl;
import aarogya.inventory.purchaseReturn.shared.PurchaseReturn_Mdl;
import aarogya.inventory.purchaseReturn.shared.PurchaseReturn_Srv;
import aarogya.inventory.purchaseReturn.shared.PurchaseReturn_SrvAsync;
import aarogya.inventory.purchaseReturn.shared.PurchaseReturn_Vld;
import aarogya.inventory.purchaseReturn.shared.TxtHBillNo_Mdl;
import aarogya.inventory.purchaseReturn.shared.TxtHGateEntry_Mdl;
import aarogya.inventory.purchaseReturn.shared.TxtHSupplier_Mdl;
import aarogya.inventory.purchaseReturn.shared.TxtHTransporter_Mdl;
import aarogya.inventory.purchaseReturn.shared.TxtHVoucherType_Mdl;
import aarogya.selfToolkit.modifyReason.client.ModifyReason;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.serviceGrid.client.ServiceGrid;
import aarogya.selfToolkit.serviceGrid.shared.ServiceDetail_Mdl;
import aarogya.selfToolkit.serviceGrid.shared.ServiceGrid_Vld;
import aarogya.selfToolkit.stStatic.client.STClient;

public class PurchaseReturn extends PurchaseReturn_Dsg implements AttachEvent.Handler, DmRootEvents, ClickHandler, BlurHandler {

    ServiceInvoker<PurchaseReturn_SrvAsync> srvAsync;
    protected DefaultValue_Mdl defaultValue;
    WalkThrough walkThrough;
//    protected final OverHeadLineHandler<GrdItemDetail_Mdl> ohlHandler = new OverHeadLineHandler(overHead, 300, 224); //<Ag> TBR
    final PurchaseReturnGridEdit<GrdItemDetail_Mdl, Grd_PrpAcs> returnGridEdit = new PurchaseReturnGridEdit(grdItemDetail, this);
    ModifyReason_Mdl modifyReasonMdl;
    PurchaseReturn_Mdl previous_Mdl;
    boolean departmentEditable = false, dateEditable = false, flagFillGrid = false, isApprove = false;
    final ModifyReason modifyReason = new ModifyReason();
    String selectedSupplierCode, selectedBillTag, partyTypeForEntry, partyType, supplierGstin, supplierGstinDate, location,
            voucherCategory, guiTemplateCode, balancingAc;
    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    double serviceTaxValue=0;

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

        topControl.setUserPermission(menuPermission, new VoucherPermissionHandler(DmClient.getVoucherPermission(new String[]{pvc.PURCHASE_RETURN, pvc.PURCHASE_RETURN_UNREGISTERED})) {
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
                    serviceDetailGrid.getServiceGrid().getGrid().getStore().add(new ServiceDetail_Mdl());
                    serviceDetailGrid.initialize(setServiveDefaultValue());
                    if (DmShared.xNull(txtHBillNo.getTag()).trim().equals("")) {
                        grdItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
                        syncronizeLineService(false, true);
                    }
                    txtHSupplier.setFocus(true);
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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(PurchaseReturn.this);
            }
        });

        topControl.addPrintHandeller(new DmTopControl.ePrint() {

            @Override
            public void printClicked() {
                prnRecord(false);
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
                    voucherCategory = "";
                    guiTemplateCode = "";
                    manageVoucherTypeChange();
                    clearGrid();

                    //Set Related Values
                    if (blnStatus && t1Row != null) {
                        voucherCategory = t1Row.getVoucherCategory();
                        guiTemplateCode = t1Row.getSharedModifiedBy();
                        balancingAc=t1Row.getBalancingAc();
                    }

                    //Reset GUI
                    gui.initializeViaTemplateCode();

                }
            }
        });

        txtHSupplier.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHSupplier_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSupplier(voucherCategory,false, txtHVoucherType.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHSupplier_Mdl>>>() {

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

        txtHGstReason.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHReason(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHGstReason).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHGstReason.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHGstReason).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHBillNo.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHBillNo_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHBillNo(txtHSupplier.getTag(), voucherCategory, txtVoucherDate.getText(), txtHBillNo.getTag2nd(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHBillNo_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHBillNo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHBillNo_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHBillNo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHBillNo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHBillNo_Mdl t1Row) {
                if (!DmShared.xNull(selectedBillTag).equalsIgnoreCase(DmShared.xNull(txtHBillNo.getTag()))) {
                    selectedBillTag = DmShared.xNull(txtHBillNo.getTag());
                    manageBillChange();
                    clearGrid();
                    flagFillGrid = true;
                }
                if (blnStatus && t1Row != null) {
                    txtBillDate.setText(DmShared.dNull(t1Row.getPbV_Date()));
                    txtSupplierBillNo.setText(DmShared.xNull(t1Row.getPbRefNo()));
                    txtSupplierBillDate.setText(DmShared.dNull(t1Row.getPbRefDate()));
                    txtSupplierBillNo.setEnabled(false);
                    txtSupplierBillDate.setEnabled(false);
                    if (flagFillGrid) {
                        fillItemGrid(txtHBillNo.getTag());
                    }
                }
            }
        });

        txtHTransporter.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHTransporter_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHTransporter(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHTransporter_Mdl>>>() {

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
                //, String returnDate, String refDate
                srvAsync.invokeRPC(blnCallBackWait).getTxtHGateEntryNo((topControl.getMode().equalsIgnoreCase("New") ? "" : topControl.getDocId()), txtHSupplier.getTag(), DmShared.dNull(txtVoucherDate.getText()), DmShared.dNull(txtSupplierBillDate.getText()), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHGateEntry_Mdl>>>() {

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
                if (!DmShared.xNull(txtHGateEntryNo.getTag2nd()).equalsIgnoreCase(DmShared.xNull(txtHGateEntryNo.getTag()))) {
                    resetGateEntry();
                    txtHGateEntryNo.setTag2nd(DmShared.xNull(txtHGateEntryNo.getTag()));
                }

                boolean flagGateEntry = (DmShared.iNull(txtHGateEntryNo.getTag()).longValue() > 0 ? true : false);
                txtHTransporter.setEnabled(!flagGateEntry);
                txtVehicleNo.setEnabled(!flagGateEntry);

                if (blnStatus && t1Row != null) {
                    txtHTransporter.setTag(DmShared.xNull(t1Row.getTransporterCode()));
                    txtHTransporter.setText(DmShared.xNull(t1Row.getTransportername()));
                    txtVehicleNo.setText(DmShared.xNull(t1Row.getVehicleNo()));
                }

            }
        });

        txtHEwayBillReq.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getEwayBillReq(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHEwayBillReq).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHEwayBillReq.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHEwayBillReq).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHTransportMode.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHTranspostMode(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHTransportMode).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHTransportMode.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHTransportMode).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {

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
    }

    private void fillSupplierDetails(boolean blnStatus, TxtHSupplier_Mdl t1Row) {
        if (!DmShared.xNull(selectedSupplierCode).equalsIgnoreCase(DmShared.xNull(txtHSupplier.getTag()))) {
            selectedSupplierCode = DmShared.xNull(txtHSupplier.getTag());
            clearGrid();
            manageSupplierChange();
        }
        if (blnStatus && t1Row != null) {
            txtHSupplier.setTag2nd(DmShared.xNull(t1Row.getStateCode()));
            supplierGstin = DmShared.xNull(t1Row.getGstin());
            supplierGstinDate = DmShared.dNull(t1Row.getGstinDate());
            partyType = DmShared.xNull(t1Row.getPartyType());
            resetGstDetails();
        }

    }

    private void clearGrid() {
        serviceDetailGrid.getServiceGrid().getGrid().getStore().clear();
        serviceDetailGrid.getServiceGrid().getGrid().getStore().add(new ServiceDetail_Mdl());
        serviceDetailGrid.initialize(setServiveDefaultValue());

        grdItemDetail.getGrid().getStore().clear();
        grdItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
        syncronizeLineService(false, true);
        calculate();
    }

    private void resetGateEntry() {
        txtHTransporter.setText("");
        txtHTransporter.setTag("");
        txtHTransporter.setTag2nd("");

        txtVehicleNo.setText("");
        txtVehicleNo.setTag("");
        txtVehicleNo.setTag2nd("");
    }

    private void manageVoucherTypeChange() {
        txtHSupplier.setText("");
        txtHSupplier.setTag("");
        txtHSupplier.setTag2nd("");
        manageSupplierChange();
    }

    private void manageSupplierChange() {
        supplierGstin = "";
        supplierGstinDate = "";
        partyType = "";

        txtHBillNo.setText("");
        txtHBillNo.setTag("");
        txtHBillNo.setTag2nd("");
        manageBillChange();
    }

    private void manageBillChange() {
        txtBillDate.setText("");
        txtBillDate.setTag("");
        txtBillDate.setTag2nd("");

        txtSupplierBillNo.setText("");
        txtSupplierBillNo.setTag("");
        txtSupplierBillNo.setTag2nd("");

        txtSupplierBillDate.setText("");
        txtSupplierBillDate.setTag("");
        txtSupplierBillDate.setTag2nd("");

        txtSupplierBillNo.setEnabled(true);
        txtSupplierBillDate.setEnabled(true);
    }

    private void fillItemGrid(String billDocId) {
        srvAsync.invokeRPC(true).getItemDetail(DmShared.xNull(txtHVoucherType.getTag()),
                billDocId,
                location,
                txtSupplierBillDate.getText(),
                topControl.getMode().equalsIgnoreCase("New") ? "" : topControl.getDocId(),
                txtHRCMApplicable.getTag(),
                defaultValue.isIncludeTaxInCost(),
                defaultValue.getIsSiteStateUT(),
                defaultValue.getPurchaseAcPostingBasedOn(),
                defaultValue.isInventoryAccountingActive(),
                new AsyncCallback<DmResult<PurchaseReturn_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHBillNo).show();
            }

            @Override
            public void onSuccess(DmResult<PurchaseReturn_Mdl> result) {
                if (result.isStatus()) {
                    PurchaseReturn_Mdl mrTemp = result.getData();
                    List<GrdItemDetail_Mdl> grdItemDetail_Mdl = mrTemp.getListItemDetail();
                    txtHRCMApplicable.setEnabled(false);
                    txtHRCMApplicable.setTag(mrTemp.getRcmApplicable());
                    if (DmShared.xNull(mrTemp.getRcmApplicable()).equalsIgnoreCase("1")) {
                        txtHRCMApplicable.setText("Yes");
                    } else if (DmShared.xNull(mrTemp.getRcmApplicable()).equalsIgnoreCase("0")) {
                        txtHRCMApplicable.setText("No");
                    }
                    grdItemDetail.getGrid().getStore().clear();
                    grdItemDetail.getGrid().getStore().addAll(grdItemDetail_Mdl);
                    grdItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());

                    serviceDetailGrid.getServiceGrid().getGrid().getStore().clear();
                    serviceDetailGrid.getServiceGrid().getGrid().getStore().addAll(mrTemp.getGrdServiceDetail());
                    serviceDetailGrid.getServiceGrid().getGrid().getStore().add(new ServiceDetail_Mdl());
                    serviceDetailGrid.getServiceGrid().commitChanges();

                    //Final Discount
                    txtFinalDiscountPer.setText(DmClient.vNull(mrTemp.getFinalDiscountPer(), "0.00"));
                    txtFinalDiscount.setText(DmClient.vNull(mrTemp.getFinalDiscount(), "0.00"));
                    setLineFinalDiscount();

                    fillGrdItemDetail(grdItemDetail_Mdl);
                    txtHGstReason.setFocus(true);
                    calculate();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void fillGrdItemDetail(List<GrdItemDetail_Mdl> listGRN) {
        for (int i = 0; i < listGRN.size(); i++) {
            GrdItemDetail_Mdl detail_Mdl = listGRN.get(i);
            if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() > 0) {
                returnGridEdit.displayRow(detail_Mdl);
                returnGridEdit.calculate();
                returnGridEdit.fillRowData(detail_Mdl);

            }
        }
        grdItemDetail.commitChanges();
        syncronizeLineService(true, true);
    }

    private void findEvents() {
        dmFind.setTitle(this.asWidget().getTitle());

        dmFind.addHelpGridCallBack(new DmFind.FindCallBack<Find_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getFind(topControl.getPermissions(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Find_Mdl>>>() {

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
        returnGridEdit.addCallBack(new PurchaseReturnGridEdit.CallBack() {

            @Override
            public void onOk(GrdItemDetail_Mdl opdService) {
                grdItemDetail.commitChanges();
                syncronizeLineService(true, true);
                calculate();
            }

            @Override
            public void onDelete() {
                grdItemDetail.commitChanges();
                syncronizeLineService(true, true);
                calculate();
            }
        });

        grdItemDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showPurchaseReturnGridEdit(event.getRowIndex());
            }
        });

        grdItemDetail.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showPurchaseReturnGridEdit(grdItemDetail.getGrid().getStore().indexOf(grdItemDetail.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

        serviceDetailGrid.addCallBack(new ServiceGrid.CallBack() {
            @Override
            public void onOk(ServiceDetail_Mdl serviceDetail_Mdl
            ) {
                serviceDetailGrid.getServiceGrid().commitChanges();
                syncronizeLineService(true, true);
                calculate();
            }

            @Override
            public void onDelete() {
                serviceDetailGrid.getServiceGrid().commitChanges();
                syncronizeLineService(true, true);
                calculate();
            }
        }
        );

    }

    private void showPurchaseReturnGridEdit(int currentRowIndex) {
        if (DmShared.xNull(txtHVoucherType.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please select Return Type", txtHVoucherType).show();
            return;
        }
        if (DmShared.dNull(txtVoucherDate.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please fill Return Date", txtVoucherDate).show();
            return;
        }
        if (DmShared.xNull(txtHSupplier.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please select Supplier", txtHSupplier).show();
            return;
        }
        if (DmShared.xNull(txtSupplierBillNo.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please select Ref. No.", txtSupplierBillNo).show();
            return;
        }
        if (DmShared.dNull(txtSupplierBillDate.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please fill Ref. Date", txtSupplierBillDate).show();
            return;
        }

        if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {
            returnGridEdit.show(currentRowIndex, false, false, false);
        } else {
            returnGridEdit.show(currentRowIndex, true, true, true);
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
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtVoucherDate) {
            txtVoucherDate.setText(DmClient.returnDate(txtVoucherDate.getText()));
            if (STClient.isFutureDate(txtVoucherDate.getText())) {
                txtVoucherDate.setText("");
                new DmAlertMessageBox("Entry Date should be less than or equal to current date.", txtVoucherDate).show();
                return;
            }
            if (topControl.getMode().equalsIgnoreCase("New")
                    && !DmShared.xNull(txtVoucherDate.getText()).equalsIgnoreCase(txtVoucherDate.getTag())) {
                txtVoucherDate.setTag(txtVoucherDate.getText());
//                ohlHandler.initialize(); //<Ag> TBR
            }
        } else if (event.getSource() == txtSupplierBillDate) {
            txtSupplierBillDate.setText(DmClient.returnDate(txtSupplierBillDate.getText()));

            if (topControl.getMode().equalsIgnoreCase("New")
                    && !DmShared.xNull(txtSupplierBillDate.getText()).equalsIgnoreCase(txtSupplierBillDate.getTag())) {
                txtSupplierBillDate.setTag(txtSupplierBillDate.getText());
//                ohlHandler.initialize(); //<Ag> TBR
            }
            resetGstDetails();
        } else if (event.getSource() == txtEwayDate) {
            txtEwayDate.setText(DmClient.returnDate(txtEwayDate.getText()));
            if (!DmShared.xNull(txtEwayDate.getText()).equalsIgnoreCase("")) {
                if ((DmShared.convertDate(txtEwayDate.getText()).getTime() > (DmShared.convertDate(DmShared.dNull(txtVoucherDate.getText())).getTime()))) {
                    new DmAlertMessageBox("E-Way Bill Date should be less than or equal to Voucher Date.", txtEwayDate).show();
                    return;
                } else if ((DmShared.convertDate(txtEwayDate.getText()).getTime() < (DmShared.convertDate(DmShared.dNull(txtSupplierBillDate.getText())).getTime()))) {
                    new DmAlertMessageBox("E-Way Bill Date should be greater than or equal to Ref. Date.", txtEwayDate).show();
                    return;
                }
            }
        } else if (event.getSource() == txtFinalDiscountPer || event.getSource() == txtFinalDiscount) {
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
            returnGridEdit.hide();
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getShiftKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_P) {
            rootEvent.stopPropagation();
        }
    }

    public PurchaseReturn(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        srvAsync = new ServiceInvoker<PurchaseReturn_SrvAsync>(GWT.create(PurchaseReturn_Srv.class), PurchaseReturn.class.getName());
        walkThrough = new WalkThrough(srvAsync);
        this.asWidget().setTitle(widgetName);
        manageEvent();
        topControlEvents(menuPermission);
        findEvents();
        textHelpEvents();
        gridEvents();
//        overHead.setBannerVisible(true); //<Ag> TBR
        gui.setBannerVisible(false);
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
                    guiTemplateCode = defaultValue.getGuiTemplateCode();
                    gui.initializeViaTemplateCode();
                    taxSummary.initialize(defaultValue.isGstApplicable());
                    swi.setWidget(PurchaseReturn.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void topControlSave() {
        PurchaseReturn_Mdl mrTemp;

        try {
            syncronizeLineService(true, false);
            defaultValue.setNewVoucherTypeCode(txtHVoucherType.getTag());
            defaultValue.setNewVoucherTypeName(txtHVoucherType.getText());
            defaultValue.setNewVoucherTypeCategory(voucherCategory);
            defaultValue.setNewVoucherDate(txtVoucherDate.getText());
            defaultValue.setNewGuiTemplateCode(guiTemplateCode);
            calculate();
            mrTemp = topControlSaveInitialize();
            PurchaseReturn_Vld.chkSave(mrTemp);
            ServiceGrid_Vld.chkSave(mrTemp.getGrdServiceDetail());
            srvAsync.invokeRPC().saveRecord(mrTemp, previous_Mdl, DmClient.getVoucherPrefix(), isApprove, new AsyncCallback<DmResult<String>>() {
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

    private PurchaseReturn_Mdl topControlSaveInitialize() {
        PurchaseReturn_Mdl mrTemp = new PurchaseReturn_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
            mrTemp.getRecIdPurchaseReturn().setCounter(previous_Mdl.getRecIdPurchaseReturn().getCounter());
            mrTemp.getRecIdPurchaseReturn().setPrefix(previous_Mdl.getRecIdPurchaseReturn().getPrefix());
            mrTemp.getRecIdPurchaseReturn().setRecIdValue(previous_Mdl.getRecIdPurchaseReturn().getRecIdValue());
        }

        resetGstDetails();

        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtVoucherNo.getText()));
        mrTemp.setU_EntDt_LatestLine(DmShared.xNull(txtVoucherNo.getTag()));  //U_EntDt_LatestLine
        mrTemp.setVoucherTypeCode(DmShared.xNull(txtHVoucherType.getTag()));
        mrTemp.setVoucherTypeName(DmShared.xNull(txtHVoucherType.getText()));
        mrTemp.setWtaCode(DmShared.iNull(txtHVoucherType.getTag2nd()).toString()); //WtaCode
        mrTemp.setVoucherCategory(DmShared.xNull(voucherCategory));
        mrTemp.setVoucherDate(DmShared.dNull(txtVoucherDate.getText()));

        mrTemp.setSupplierCode(DmShared.xNull(txtHSupplier.getTag()));
        mrTemp.setSupplierName(DmShared.xNull(txtHSupplier.getText()));
        mrTemp.setSupplierStateCode(DmShared.xNull(txtHSupplier.getTag2nd()));
        mrTemp.setSupplierGstin(DmShared.xNull(supplierGstin));
        mrTemp.setSupplierGstinDate(DmShared.xNull(supplierGstinDate));

        mrTemp.setPartyType(DmShared.xNull(partyType));
        mrTemp.setPartyTypeForEntry(DmShared.xNull(partyTypeForEntry));
        mrTemp.setRcmApplicable(DmShared.xNull(txtHRCMApplicable.getTag()));
        mrTemp.setPlaceOfSupply(DmShared.xNull(txtHSupplier.getTag2nd()));
        mrTemp.setLocationCode(location);

        mrTemp.setDefaultValueMdl(defaultValue);
        mrTemp.setGstReasonCode(DmShared.xNull(txtHGstReason.getTag()));
        mrTemp.setGstReasonName(DmShared.xNull(txtHGstReason.getText()));
        mrTemp.setBillNo(DmShared.xNull(txtHBillNo.getText()));
        mrTemp.setBillNoTag(DmShared.xNull(txtHBillNo.getTag()));
        mrTemp.setBillDate(DmShared.dNull(txtBillDate.getText()));
        mrTemp.setPartyBillNo(DmShared.xNull(txtSupplierBillNo.getText()));
        mrTemp.setPartyBillDate(DmShared.dNull(txtSupplierBillDate.getText()));
        mrTemp.setRemark(DmShared.xNull(txtRemark.getText()).trim());
        mrTemp.setGateNoTag(DmShared.xNull(txtHGateEntryNo.getTag()));
        mrTemp.setTransporterCode(DmShared.xNull(txtHTransporter.getTag()));
        mrTemp.setTransportModeCode(DmShared.xNull(txtHTransportMode.getTag()));
        mrTemp.setVehicleNo(DmShared.xNull(txtVehicleNo.getText()));
        mrTemp.setEwayNo(DmShared.xNull(txtEwayNo.getText()).trim());
        mrTemp.setEwayDate(DmShared.dNull(txtEwayDate.getText()));
        mrTemp.setEwayBillRequiredCode(DmShared.xNull(txtHEwayBillReq.getTag()));
        mrTemp.setEwayBillRequiredName(DmShared.xNull(txtHEwayBillReq.getText()));
        mrTemp.setBalancingAc(DmShared.xNull(balancingAc));

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
        mrTemp.setBalancingAc(DmShared.xNull(balancingAc));

//        OverHead_Mdl ohMdl = OverHead_Shared.getOHByUniqueId("RO", overHead.getOverHeadList()); //<Ag> TBR
//        mrTemp.setRoundOffAmount(ohMdl != null ? DmShared.vNull(ohMdl.getAmount()).toString() : "0"); //<Ag> TBR
        //----------------------------------------------------------------------------------
        //=========================(Removing Blank Row in GRID)=========================================
        List<GrdItemDetail_Mdl> grdLstItemDetail_Mdl = new ArrayList<>();

        grdLstItemDetail_Mdl.addAll(grdItemDetail.getGrid().getStore().getAll());

        if (grdItemDetail.getGrid().getStore().getAll().size() >= 0) {
            int i = 0;
            for (GrdItemDetail_Mdl grnDetail_Mdl : grdItemDetail.getGrid().getStore().getAll()) {
                if (DmShared.iNull(grnDetail_Mdl.getSharedKeyFieldCode()).longValue() == 0) {
                    grdLstItemDetail_Mdl.remove(i);
                    i--;
                }
                i++;
            }
            if (grdLstItemDetail_Mdl.size() < grdItemDetail.getGrid().getStore().getAll().size()) {
                grdItemDetail.getGrid().getStore().clear();
                grdItemDetail.getGrid().getStore().addAll(grdLstItemDetail_Mdl);
                grdItemDetail.commitChanges();
            }

        }
        mrTemp.getListItemDetail().clear();
        mrTemp.getListItemDetail().addAll(grdItemDetail.getGrid().getStore().getAll());

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
            case 500:
                new DmAlertMessageBox(errorMessage, txtHGstReason).show();
                break;
            case 600:
                new DmAlertMessageBox(errorMessage, txtSupplierBillNo).show();
                break;
            case 700:
                new DmAlertMessageBox(errorMessage, txtSupplierBillDate).show();
                break;
            case 800:
                int indexTemp = DmShared.iNull(errorSourceCode).intValue();
                if (!DmShared.xNull(errorSource).equalsIgnoreCase("GridRow")) {
                    List<GrdItemDetail_Mdl> listTemp = grdItemDetail.getGrid().getStore().getAll();
                    for (GrdItemDetail_Mdl node : listTemp) {
                        if (DmShared.xNull(node.getPbDocId()).equalsIgnoreCase(errorSource)
                                && DmShared.xNull(node.getPbV_SNo()).equalsIgnoreCase(errorSourceCode)) {
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
                        ((CellSelectionModel) grdItemDetail.getGrid().getSelectionModel()).selectCell(DmShared.iNull(gridRowIndex).intValue(), 1);
                    }
                }).show();
                break;
            case 900:
                new DmAlertMessageBox(errorMessage, txtHEwayBillReq).show();
                break;
            case 1000:
                new DmAlertMessageBox(errorMessage, txtEwayDate).show();
                break;
            case 1100:
                new DmAlertMessageBox(errorMessage, txtHRCMApplicable).show();
                break;
            case 1200:
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
                new DmAlertMessageBox(errorMessage, txtHSupplier).show();
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
//        ohlHandler.setDefault(); //<Ag> TBR
        defaultValue.setIsContinuousNewMode(false);
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<PurchaseReturn_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<PurchaseReturn_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<PurchaseReturn_Mdl> result) {
        clear();
        if (result.isStatus()) {
            PurchaseReturn_Mdl mrTemp = result.getData();
            previous_Mdl = result.getData().clone();

            topControl.setDocId(mrTemp.getSharedKeyFieldCode());

            txtVoucherNo.setText(mrTemp.getSharedKeyFieldName());
            txtVoucherNo.setTag(mrTemp.getU_EntDt_LatestLine());

            txtHVoucherType.setTag(mrTemp.getVoucherTypeCode());
            txtHVoucherType.setText(mrTemp.getVoucherTypeName());
            txtHVoucherType.setTag2nd(mrTemp.getWtaCode());

            voucherCategory = mrTemp.getVoucherCategory();

            txtVoucherDate.setText(mrTemp.getVoucherDate());
            txtVoucherDate.setTag(mrTemp.getVoucherDate());

            txtHSupplier.setTag(mrTemp.getSupplierCode());
            txtHSupplier.setText(mrTemp.getSupplierName());
            txtHSupplier.setTag2nd(mrTemp.getSupplierStateCode());
            supplierGstin = mrTemp.getSupplierGstin();
            supplierGstinDate = mrTemp.getSupplierGstinDate();
            partyType = mrTemp.getPartyType();
            partyTypeForEntry = mrTemp.getPartyTypeForEntry();
            selectedSupplierCode = mrTemp.getSupplierCode();
            location = mrTemp.getLocationCode();
            txtHRCMApplicable.setTag(DmShared.xNull(mrTemp.getRcmApplicable()));
            txtHRCMApplicable.setTag2nd(DmShared.xNull(mrTemp.getRcmApplicable()));
            if (DmShared.xNull(mrTemp.getRcmApplicable()).equalsIgnoreCase("1")) {
                txtHRCMApplicable.setText("Yes");
            } else if (DmShared.xNull(mrTemp.getRcmApplicable()).equalsIgnoreCase("0")) {
                txtHRCMApplicable.setText("No");
            }

            txtHGstReason.setTag(mrTemp.getGstReasonCode());
            txtHGstReason.setText(mrTemp.getGstReasonName());
            selectedBillTag = mrTemp.getBillNoTag();
            txtHBillNo.setText(mrTemp.getBillNo());
            txtHBillNo.setTag(mrTemp.getBillNoTag());
            txtHBillNo.setTag2nd(mrTemp.getBillNoTag());
            txtBillDate.setText(mrTemp.getBillDate());
            txtSupplierBillNo.setText(mrTemp.getPartyBillNo());
            txtSupplierBillDate.setText(mrTemp.getPartyBillDate());
            txtSupplierBillDate.setTag(mrTemp.getPartyBillDate());
            txtRemark.setText(mrTemp.getRemark());
            txtHGateEntryNo.setTag(mrTemp.getGateNoTag());
            txtHGateEntryNo.setTag2nd(mrTemp.getGateNoTag());
            txtHGateEntryNo.setText(mrTemp.getGateNoText());
            txtHTransporter.setTag(mrTemp.getTransporterCode());
            txtHTransporter.setTag2nd(mrTemp.getTransporterCode());
            txtHTransporter.setText(mrTemp.getTransporterName());
            txtHTransportMode.setTag(mrTemp.getTransportModeCode());
            txtHTransportMode.setText(mrTemp.getTransportModeName());
            txtVehicleNo.setText(mrTemp.getVehicleNo());
            txtHEwayBillReq.setTag(mrTemp.getEwayBillRequiredCode());
            txtHEwayBillReq.setText(mrTemp.getEwayBillRequiredName());
            txtEwayNo.setText(mrTemp.getEwayNo());
            txtEwayDate.setText(mrTemp.getEwayDate());

            userControl.setTextBlog(preparedByBlog, mrTemp.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, mrTemp.getSharedModifiedBy());
            userControl.setTextBlog(approvedByBlog, mrTemp.getApprovedBy());

            serviceDetailGrid.getServiceGrid().getGrid().getStore().clear();
            serviceDetailGrid.getServiceGrid().getGrid().getStore().addAll(mrTemp.getGrdServiceDetail());

            grdItemDetail.getGrid().getStore().clear();
            grdItemDetail.getGrid().getStore().addAll(mrTemp.getListItemDetail());

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
            balancingAc = DmShared.xNull(mrTemp.getBalancingAc());

            gui.initialize(mrTemp.getGuiMdl());
            //resetGstDetails();
            resetTaxSummary();
            serviceDetailGrid.initialize(setServiveDefaultValue());
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
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
            if (DmShared.iNull(txtHVoucherType.getTag2nd()).longValue() != 0) {
                walkThrough.show(txtHVoucherType.getTag2nd());
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

    private void manageEvent() {
        btnApprovedBy.addClickHandler(this);
        flwForm.addAttachHandler(this);
        txtVoucherDate.addBlurHandler(this);
        txtSupplierBillDate.addBlurHandler(this);
        txtEwayDate.addBlurHandler(this);
        txtFinalDiscount.addBlurHandler(this);
        txtFinalDiscountPer.addBlurHandler(this);
    }

    private void manageDisplay(boolean flag) {
        txtVoucherNo.setEnabled(false);
        txtBillDate.setEnabled(false);
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
            txtHRCMApplicable.setEnabled(false);
            txtHSupplier.setEnabled(false);
            if (DmShared.xNull(txtHBillNo.getTag()).trim().equals("")) {
                txtSupplierBillNo.setEnabled(true);
                txtSupplierBillDate.setEnabled(true);
            } else {
                txtSupplierBillNo.setEnabled(false);
                txtSupplierBillDate.setEnabled(false);
            }

            boolean flagGateEntry = (DmShared.iNull(txtHGateEntryNo.getTag()).longValue() > 0 ? true : false);
            txtHTransporter.setEnabled(!flagGateEntry);
            txtVehicleNo.setEnabled(!flagGateEntry);
        }

        if (flag) {
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
        txtVoucherDate.setTag(defaultValue.getIsContinuousNewMode() ? DmShared.dNull(defaultValue.getNewVoucherDate()) : DmShared.dNull(defaultValue.getCurrentDate()));
        txtVoucherDate.setText(defaultValue.getIsContinuousNewMode() ? DmShared.dNull(defaultValue.getNewVoucherDate()) : DmShared.dNull(defaultValue.getCurrentDate()));
        txtHVoucherType.setTag2nd(defaultValue.getIsContinuousNewMode() ? defaultValue.getNewVoucherTypeCode() : defaultValue.getVoucherTypeCode());
        txtHVoucherType.setTag(defaultValue.getIsContinuousNewMode() ? defaultValue.getNewVoucherTypeCode() : defaultValue.getVoucherTypeCode());
        txtHVoucherType.setText(defaultValue.getIsContinuousNewMode() ? defaultValue.getNewVoucherTypeName() : defaultValue.getVoucherTypeName());
        voucherCategory = defaultValue.getIsContinuousNewMode() ? defaultValue.getNewVoucherTypeCategory() : defaultValue.getVoucherCategory();
        guiTemplateCode = defaultValue.getIsContinuousNewMode() ? defaultValue.getNewGuiTemplateCode() : defaultValue.getGuiTemplateCode();
        balancingAc = defaultValue.getIsContinuousNewMode() ? defaultValue.getBalancingAc() : defaultValue.getBalancingAc();
        txtRoundoff.setTag(DmShared.xNull(defaultValue.getRoundOffAcCode()));
        txtFinalDiscount.setTag(DmShared.xNull(defaultValue.getFinalDicountAcCode()));
        txtHEwayBillReq.setText("Yes");
        txtHEwayBillReq.setTag("1");
        
        setRCM(voucherCategory);
        if (!(DmShared.iNull(defaultValue.getIsSiteStateUT()).intValue() == 1)) {
            lblSgstUgst.setText(DmEnum.GSTS_Head.SGST.getName());
        } else if ((DmShared.iNull(defaultValue.getIsSiteStateUT()).intValue() == 1)) {
            lblSgstUgst.setText(DmEnum.GSTS_Head.UGST.getName());
        }

        gui.initializeViaTemplateCode();
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

    private void setRCM(String vCat) {
        boolean rcmFlag = false;
        //=====================================< Set RCM >=================================//
        if (DmShared.xNull(vCat).equalsIgnoreCase(new ProjectVoucherCategory().PURCHASE_RETURN_UNREGISTERED)) {
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

    private void clear() {
        selectedSupplierCode = "";
        selectedBillTag = "";
        partyTypeForEntry = "";
        partyType = "";
        supplierGstin = "";
        supplierGstinDate = "";
        location = "";
        voucherCategory = "";
        guiTemplateCode = "";
        balancingAc = "";
        txtSGST.setText("");
        txtSGST.setTag("");
        txtSGST.setTag2nd("");

        txtUGST.setText("");
        txtUGST.setTag("");
        txtUGST.setTag2nd("");

        gui.clear();
        grdItemDetail.getGrid().getStore().clear();
        serviceDetailGrid.getServiceGrid().getGrid().getStore().clear();
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        userControl.setTextBlog(approvedByBlog, "");
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

        grdItemDetail.commitChanges();

        for (GrdItemDetail_Mdl detail_Mdl : grdItemDetail.getGrid().getStore().getAll()) {
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
        txtNetAmount.setText(DmClient.vNull(DmShared.iNull(DmShared.compute("Math.round(" + txtTotalAmount.getText() + ")")), "0.00"));
        txtRoundoff.setText(DmClient.vNull(DmShared.vNull(txtNetAmount.getText()) - DmShared.vNull(txtTotalAmount.getText()), "0.00"));

        double landedValue = 0;
        if (defaultValue.isIncludeTaxInCost()) {
            landedValue = DmShared.vNull(txtNetAmount.getText()).doubleValue();
            if (!defaultValue.isIncludeOverHeadsInCost()) {
                landedValue = DmShared.vNull(landedValue).doubleValue()
                        -DmShared.vNull(txtServiceAmount.getText()).doubleValue()
                        -DmShared.vNull(serviceTaxValue).doubleValue();
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
        grdItemDetail.commitChanges();
        calculateLandedValue();
        resetTaxSummary();
        serviceDetailGrid.initialize(setServiveDefaultValue());
    }

    private void calculateLandedValue() {

        double landedValue = 0, totalGrossAmount = 0, lineGrossAmount = 0;

        landedValue = DmShared.vNull(txtLandedValue.getText());

        for (GrdItemDetail_Mdl detailMdl : grdItemDetail.getGrid().getStore().getAll()) {
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
        for (GrdItemDetail_Mdl lineMdl : grdItemDetail.getGrid().getStore().getAll()) {
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

        grdItemDetail.commitChanges();

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
                for (GrdItemDetail_Mdl grdGrnDetail_Mdl : grdItemDetail.getGrid().getStore().getAll()) {
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

            for (int i = 0; i < grdItemDetail.getGrid().getStore().getAll().size(); i++) {
                if (grdItemDetail.getGrid().getStore().get(i).getSharedKeyFieldCode() != null) {
                    if (!grdItemDetail.getGrid().getStore().get(i).isServiceRecord()) {
                        if (DmShared.iNull(grdItemDetail.getGrid().getStore().get(i).getSharedKeyFieldCode()).longValue() != 0) {
                            flag = true;
                            if (flag) {
                                lineTaxableAmount = DmShared.vNull(grdItemDetail.getGrid().getStore().get(i).getTaxableAmt()).doubleValue();
                                lineSubTotal = DmShared.vNull(grdItemDetail.getGrid().getStore().get(i).getSubTotal()).doubleValue();
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

                                grdItemDetail.getGrid().getStore().get(i).setFinalDiscount(DmClient.vNull(lineFinalDiscount, "0.00"));
                                grdItemDetail.getGrid().getStore().get(i).setFinalDiscountPer(DmClient.vNull(txtFinalDiscountPer.getText(), "0.00"));
                                grdItemDetail.getGrid().getStore().get(i).setItemValue(DmClient.vNull(lineSubTotal - lineFinalDiscount, "0.00"));
                                flag = false;
                            }
                        }
                    }
                }
            }
        }
        grdItemDetail.commitChanges();
        calculate();
    }

    private void clearLineFinalDiscount() {
        double lineSubTotal = 0;
        for (int i = 0; i < grdItemDetail.getGrid().getStore().getAll().size(); i++) {
            GrdItemDetail_Mdl grdGrnDetails_Mdl = grdItemDetail.getGrid().getStore().get(i);
            if (!grdItemDetail.getGrid().getStore().get(i).isServiceRecord()) {
                if (DmShared.iNull(grdGrnDetails_Mdl.getSharedKeyFieldCode()).longValue() != 0) {
                    lineSubTotal = DmShared.vNull(grdItemDetail.getGrid().getStore().get(i).getSubTotal()).doubleValue();
                    grdItemDetail.getGrid().getStore().get(i).setFinalDiscount("");
                    grdItemDetail.getGrid().getStore().get(i).setFinalDiscountPer("");
                    grdItemDetail.getGrid().getStore().get(i).setItemValue(DmClient.vNull(lineSubTotal, "0.00"));
                }
            }
        }
        grdItemDetail.commitChanges();
        calculate();
    }

    private void resetTaxSummary() {
        List<List<OverHeadLine_Mdl>> lstOverheadLine = new ArrayList<List<OverHeadLine_Mdl>>();
        List<OverHeadLine_Mdl> overheadLine = null;
        OverHeadLine_Mdl overheadLineRow = null;
        for (GrdItemDetail_Mdl grdMdl : grdItemDetail.getGrid().getStore().getAll()) {
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
        hashMapFilter.put("docId", new FieldUDT_Mdl("Return No.", DmShared.xNull(txtVoucherNo.getText()), DmShared.xNull(topControl.getDocId())));
        new OpenSourceReport(DmShared.xNull(txtHVoucherType.getTag()), "", widgetName, widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
            @Override
            public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                DmClient.localSettingPrintMap(reportProperty, moduleName, widgetName, "Print");
                return reportProperty;
            }
        }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
    }

    //<editor-fold defaultstate="collapsed" desc="Service Grid">
    private HashMap setServiveDefaultValue() {
        HashMap paramHashMap = new HashMap();
        paramHashMap.put("LOCATION", location);
        paramHashMap.put("UT", DmShared.iNull(defaultValue.getIsSiteStateUT()).intValue() == 1);
        paramHashMap.put("V_DATE", txtSupplierBillDate.getText());
        paramHashMap.put("MODE", topControl.getMode());
        paramHashMap.put("IS_IN_GSTS", true);
        paramHashMap.put("GOODS_TAXABLE_VALUE", DmShared.xNull(txtGoodsAmount.getTag()));
        paramHashMap.put("COMMON_UNIT_QTY", DmShared.xNull(txtDiscount.getTag2nd()));
        paramHashMap.put("IS_RCM_APPLICABLE", DmShared.iNull(txtHRCMApplicable.getTag()).intValue() == 1);
        paramHashMap.put("IS_INCLUDE_TAX_IN_COST", defaultValue.isIncludeTaxInCost());
        paramHashMap.put("IS_MANUAL_GST_TYPE_REQUIRED", true);
        paramHashMap.put("IS_MANUAL_TAX_REQUIRED", true);
        paramHashMap.put("IS_INVENTROY_ACCOUNTING_ACTIVE", defaultValue.isInventoryAccountingActive());
        paramHashMap.put("IS_SERVICE_AC_MANDATORY", !defaultValue.isInventoryAccountingActive());
        paramHashMap.put("IS_TAX_AC_MANDATORY", true);

        return paramHashMap;

    }

    private void syncronizeLineService(boolean syncronizeLineHeader, boolean addServiceRow) {
        serviceDetailGrid.initialize(setServiveDefaultValue());
        //<editor-fold defaultstate="collapsed" desc="First Syncronize Line Service With Item Grid">
        serviceTaxValue=0;
        if (syncronizeLineHeader) {
            calculate();
        }
        List<GrdItemDetail_Mdl> grdLstItemDetail_Mdl = new ArrayList<>();

        grdLstItemDetail_Mdl.addAll(grdItemDetail.getGrid().getStore().getAll());

        int i = 0;
        for (GrdItemDetail_Mdl itemDetail_Mdl : grdItemDetail.getGrid().getStore().getAll()) {
            if (itemDetail_Mdl.isServiceRecord()) {
                grdLstItemDetail_Mdl.remove(i);
                i--;
            }
            i++;
        }

        if (grdLstItemDetail_Mdl.size() < grdItemDetail.getGrid().getStore().getAll().size()) {
            grdItemDetail.getGrid().getStore().clear();
            grdItemDetail.getGrid().getStore().addAll(grdLstItemDetail_Mdl);

            int size = grdItemDetail.getGrid().getStore().size() - 1;
            GrdItemDetail_Mdl itemDetail_Mdl = grdItemDetail.getGrid().getStore().get(size);

            if (DmShared.iNull(itemDetail_Mdl.getSharedKeyFieldCode()).longValue() > 0) {
                if (DmShared.xNull(txtHBillNo.getTag()).trim().equals("")) {
                    grdItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
                }
            }
            grdItemDetail.commitChanges();
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
                grdItemDetail_Mdl.setQuantity(DmShared.xNull(serviceDetail_Mdl.getQuantity()));
                grdItemDetail_Mdl.setPurchaseUnitCode(serviceDetail_Mdl.getUnitCode());

                grdItemDetail_Mdl.setHsnCode(serviceDetail_Mdl.getHsnCode());
                grdItemDetail_Mdl.setHsn(serviceDetail_Mdl.getHsn());
                grdItemDetail_Mdl.setItemGSTNature(serviceDetail_Mdl.getItemGSTNature());
                grdItemDetail_Mdl.setItsBasedOn(serviceDetail_Mdl.getItsBasedOn());
                grdItemDetail_Mdl.setGstTypeCode(serviceDetail_Mdl.getGstTypeCode());
                grdItemDetail_Mdl.setGstTypeName(serviceDetail_Mdl.getGstTypeName());
                grdItemDetail_Mdl.setManualGstType(serviceDetail_Mdl.isManualGstType());

                grdItemDetail_Mdl.setRate(DmShared.vNull(serviceDetail_Mdl.getRate()).toString());
                grdItemDetail_Mdl.setAmount(DmShared.vNull(serviceDetail_Mdl.getAmount()).toString());

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
                serviceTaxValue=serviceTaxValue+DmShared.vNull(serviceDetail_Mdl.getSgstAmt())
                        +DmShared.vNull(serviceDetail_Mdl.getUgstAmt())
                        +DmShared.vNull(serviceDetail_Mdl.getCgstAmt())
                        +DmShared.vNull(serviceDetail_Mdl.getIgstAmt());

                grdItemDetail.getGrid().getStore().add(grdItemDetail_Mdl);
            }
        }
        serviceDetailGrid.getServiceGrid().commitChanges();
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Add Serivice Row in Item Grid">
        if (addServiceRow) {
            GrdItemDetail_Mdl serviceRecord = new GrdItemDetail_Mdl();
            serviceRecord.setServiceRecord(true);
            grdItemDetail.getGrid().getStore().add(serviceRecord);
            int serviceRowIndex = grdItemDetail.getGrid().getStore().getAll().indexOf(serviceRecord);
            serviceDetailGrid.setServiceRowIndex(serviceRowIndex);
        }

        //</editor-fold>
        grdItemDetail.commitChanges();

        if (syncronizeLineHeader) {
            calculate();
        }

    }

    //</editor-fold>
}
