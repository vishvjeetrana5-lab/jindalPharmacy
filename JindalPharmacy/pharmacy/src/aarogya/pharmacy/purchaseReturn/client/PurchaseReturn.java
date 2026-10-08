package aarogya.pharmacy.purchaseReturn.client;

import aarogya.pharmacy.purchaseBillSearch.client.PurchaseBillSearch;
import aarogya.pharmacy.purchaseReturn.shared.*;
import aarogya.selfToolkit.modifyReason.client.ModifyReason;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.stStatic.client.STClient;
import aarogya.selfToolkit.taxSummary.shared.TaxSummary_Mdl;
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

public class PurchaseReturn extends PurchaseReturn_Dsg implements AttachEvent.Handler, DmRootEvents, ClickHandler, BlurHandler, SelectionHandler<Item> {

    ServiceInvoker<PurchaseReturn_SrvAsync> srvAsync;
    protected DefaultValue_Mdl defaultValue;
    WalkThrough walkThrough;
    protected final OverHeadLineHandler<GrdItemDetail_Mdl> ohlHandler = new OverHeadLineHandler(overHead, 300, 260);
    final PurchaseReturnGridEdit<GrdItemDetail_Mdl, Grd_PrpAcs> returnGridEdit = new PurchaseReturnGridEdit(grdItemDetail);
    ModifyReason_Mdl modifyReasonMdl;
    PurchaseReturn_Mdl previous_Mdl;
    boolean departmentEditable = false, flagFillGrid = false;
    final ModifyReason modifyReason = new ModifyReason();
    String selectedSupplierCode, partyTypeForEntry, partyType, supplierGstin, supplierGstinDate, location,
            rcmApplicable, voucherCategory, guiTemplateCode;
//    selectedProformaReturnBillTag
    private boolean isApprove = false;
    private boolean isIncludeTaxInCost = false, singlePBillFlowInPurchase = false;

    final PurchaseBillSearch pBillSearch = new PurchaseBillSearch();

    private void topControlEvents(UserMenu menuPermission) {

        topControl.setTitle(this.asWidget().getTitle());
        topControl.setOtherRequired(true);
        topControl.setRefreshRequired(false);

        for (String controlPermission : menuPermission.getControlPermission()) {
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(departmentEditableBlog)) {
                departmentEditable = true;
            }
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(approvedByBlog)) {
                isApprove = true;
            }
        }

        //================================<MJ>===========================//
        pBillSearch.addCallBack(new PurchaseBillSearch.CallBack() {
            @Override
            public void ok(List<KeyFieldExtended> listKF) {
                getBillDetail(listKF);
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
        //==============================<******>==========================//

        overHead.addCallback(new OverHead.Callback() {

            @Override
            public void initialized() {
                setCardAcInOH();
                if (flagFillGrid) {
                    flagFillGrid = false;
                    if (DmShared.xNull(txtHProformaBillNo.getTag()).trim().equals("")) {
                        grdItemDetail.getGrid().getStore().clear();
//                        grdItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
                    } else {
//                        fillProformaPurchaseReturnItemGrid(txtHProformaBillNo.getTag());
                    }
                }
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

        ohlHandler.addCallback(new OverHeadLineHandler.Callback<GrdItemDetail_Mdl>() {

            @Override
            public void initialized() {
                grdItemDetail.getGrid().getStore().clear();
//                grdItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
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
            public List<GrdItemDetail_Mdl> getRows() {
                return grdItemDetail.getGrid().getStore().getAll();
            }
        });

        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

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
                    if (DmShared.xNull(txtHProformaBillNo.getTag()).trim().equals("")) {
//                        grdItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(PurchaseReturn.this);
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
                if (!DmShared.xNull(txtHVoucherType.getTag()).equalsIgnoreCase(DmShared.xNull(txtHVoucherType.getTag2nd()))) {
                    txtHVoucherType.setTag2nd(txtHVoucherType.getTag());
                    if (topControl.getMode().equalsIgnoreCase("New")) {
                        manageVoucherTypeChange();
                        ohlHandler.initialize();
                    }
                    if (blnStatus && t1Row != null) {
                        voucherCategory = t1Row.getSharedPreparedBy();
                        guiTemplateCode = t1Row.getSharedModifiedBy();
                    } else {
                        guiTemplateCode = "";
                    }
                    gui.initializeViaTemplateCode();
                }
            }
        });

        txtHSupplier.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHSupplier_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSupplier(false, txtHVoucherType.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHSupplier_Mdl>>>() {

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

                flagFillGrid = true;
                ohlHandler.initialize();
                txtProformaBillDate.setText("");
                txtProformaBillDate.setTag("");
                txtProformaBillDate.setTag2nd("");
                txtHProformaBillNo.setText("");
                txtHProformaBillNo.setTag("");
                txtHGstReason.setText("");
                txtHGstReason.setTag("");
                txtHDepartment.setText("");
                txtHDepartment.setTag("");
                txtRemark.setText("");
                overHead.clear();
                gui.clear();
                grdItemDetail.getGrid().getStore().clear();

                if (!DmShared.xNull(txtHSupplier.getTag()).equalsIgnoreCase(DmShared.xNull(txtHSupplier.getTag()))) {
                    selectedSupplierCode = DmShared.xNull(txtHSupplier.getTag());
                    manageSupplierChange();
                    ohlHandler.initialize();
                }
                if (blnStatus && tRow != null) {
                    txtHSupplier.setTag2nd(DmShared.xNull(tRow.getStateCode()));
                    supplierGstin = DmShared.xNull(tRow.getGstin());
                    supplierGstinDate = DmShared.dNull(tRow.getGstinDate());
                    partyType = DmShared.xNull(tRow.getPartyType());
                    resetGstDetails();
                }
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
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHProformaBillNo.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHProformaBillNo_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHProformaBillNo(txtHSupplier.getTag(), voucherCategory, txtVoucherDate.getText(), txtHProformaBillNo.getTag2nd(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHProformaBillNo_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHProformaBillNo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHProformaBillNo_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHProformaBillNo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHProformaBillNo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHProformaBillNo_Mdl t1Row) {
//                if (!DmShared.xNull(txtHProformaBillNo.getTag()).equalsIgnoreCase(DmShared.xNull(txtHProformaBillNo.getTag()))) {
////                    selectedProformaReturnBillTag = DmShared.xNull(txtHProformaBillNo.getTag());
//                    manageProformaBillChange();
//                    flagFillGrid = true;
//                    ohlHandler.initialize();
//                }

                if (blnStatus && t1Row != null) {
                    txtProformaBillDate.setTag(DmShared.dNull(t1Row.getPfprV_Date()));
                    txtProformaBillDate.setText(DmShared.dNull(t1Row.getPfprV_Date()));

                    txtHGstReason.setTag(DmShared.xNull(t1Row.getGstReasonCode()));
                    txtHGstReason.setText(DmShared.xNull(t1Row.getGstReasonName()));
                    txtRemark.setText(DmShared.xNull(t1Row.getRemark()));

//                     manageProformaBillChange();
                    fillProformaPurchaseReturnItemGrid(txtHProformaBillNo.getTag());
//        selectedProformaReturnBillTag="";
                    // fillProformaPurchaseReturnItemGrid(DmShared.xNull(txtHProformaBillNo.getTag()));

                }
            }
        });

//        txtHBillNo.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHBillNo_Mdl>() {
//            @Override
//            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
//                srvAsync.invokeRPC(blnCallBackWait).getTxtHBillNo(txtHSupplier.getTag(), voucherCategory, txtVoucherDate.getText(), txtHBillNo.getTag2nd(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHBillNo_Mdl>>>() {
//                    @Override
//                    public void onFailure(Throwable caught) {
//                        new DmAlertMessageBox(caught.getMessage(), txtHBillNo).show();
//                    }
//
//                    @Override
//                    public void onSuccess(DmResultPageing<List<TxtHBillNo_Mdl>> result) {
//                        if (result.isStatus()) {
//                            txtHBillNo.setData(result.getData(), result.getPaging());
//                        } else {
//                            new DmAlertMessageBox(result.getErrorMessage(), txtHBillNo).show();
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void getSelectedRow(boolean blnStatus, GrdBillDetail_Mdl t1Row) {
//                if (!DmShared.xNull(selectedProformaReturnBillTag).equalsIgnoreCase(DmShared.xNull(txtHBillNo.getTag()))) {
//                    selectedProformaReturnBillTag = DmShared.xNull(txtHBillNo.getTag());
//                    manageBillChange();
//                    flagFillGrid = true;
//                    ohlHandler.initialize();
//                }
//                if (blnStatus && t1Row != null) {
//                    txtBillDate.setText(DmShared.dNull(t1Row.getPbV_Date()));
//                    txtSupplierBillNo.setText(DmShared.xNull(t1Row.getPbRefNo()));
//                    txtSupplierBillDate.setText(DmShared.dNull(t1Row.getPbRefDate()));
//                    txtSupplierBillNo.setEnabled(false);
//                    txtSupplierBillDate.setEnabled(false);
//                }
//            }
//        });
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

        txtHProformaBillNo.setText("");
        txtHProformaBillNo.setTag("");
        txtHProformaBillNo.setTag2nd("");

//        manageProformaBillChange();
    }

    private void getBillDetail(List<KeyFieldExtended> listKF) {
        srvAsync.invokeRPC(true).getItemDetail(DmShared.xNull(txtHVoucherType.getTag()), listKF, location, txtVoucherDate.getText(), topControl.getMode().equalsIgnoreCase("New") ? "" : topControl.getDocId(), new AsyncCallback<DmResult<PurchaseReturn_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<PurchaseReturn_Mdl> result) {
                if (result.isStatus() && result.getData() != null) {
                    if (result.getData().getListItemDetail().size() > 0) {
                        grdItemDetail.getGrid().getStore().clear();
                        grdItemDetail.getGrid().getStore().addAll(result.getData().getListItemDetail());
//                        grdItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
                        fillItemDetail(result.getData().getListItemDetail());

                    }
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });

    }

    private void fillItemDetail(List<GrdItemDetail_Mdl> listPBill) {
        String grossAcCode, grossAcName;
        for (int i = 0; i < listPBill.size(); i++) {
            GrdItemDetail_Mdl detail_Mdl = listPBill.get(i);
            if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() > 0) {
                grdItemDetail.getGrid().getStore().get(i).setAmount(DmClient.vNull(DmShared.vNull(grdItemDetail.getGrid().getStore().get(i).getQuantity()).doubleValue() * DmShared.vNull(grdItemDetail.getGrid().getStore().get(i).getRate()).doubleValue(), "0.00"));
                ohlHandler.setReference(detail_Mdl.getOHLListPurchaseBill(), ohlHandler.getOverHeadList(i));
                ohlHandler.setITS(grdItemDetail.getGrid().getStore().get(i).getLstITS(), i);
                ohlHandler.calculate(grdItemDetail.getGrid().getStore().get(i).getAmount(), i);
                grdItemDetail.getGrid().getStore().get(i).setOHLList(ohlHandler.getOverHeadList(i));

                if (!defaultValue.isInventoryAccountingActive()) {
                    grossAcCode = DmShared.xNull(grdItemDetail.getGrid().getStore().get(i).getGrossAcCode());
                    grossAcName = DmShared.xNull(grdItemDetail.getGrid().getStore().get(i).getGrossAcName());
                } else {
                    grossAcCode = DmShared.xNull(grdItemDetail.getGrid().getStore().get(i).getInventoryAc());
                    grossAcName = DmShared.xNull(grdItemDetail.getGrid().getStore().get(i).getInventoryAcName());
                }
                //  previousOrderNoCode = DmShared.xNull(grdItemDetail.getGrid().getStore().get(i).getPoDocId());

                if (!grossAcCode.trim().equals("") && !grossAcName.trim().equals("")) {
                    // Setting account to one GS + multiple [GS] unique ids
                    if (!defaultValue.isInventoryAccountingActive()) {
                        List<OverHeadLine_Mdl> lstOHLMdl = OverHeadLine_Shared.getOHByUniqueIds("GS", grdItemDetail.getGrid().getStore().get(i).getOHLList());
                        for (OverHeadLine_Mdl ohlm : lstOHLMdl) {
                            if (ohlm != null) {
                                ohlm.setAccountCode(grossAcCode);
                                ohlm.setAccountName(grossAcName);
                            }
                        }
                    } else {
                        List<OverHeadLine_Mdl> lstOHLMdl = OverHeadLine_Shared.getOHByUniqueIds("IV", grdItemDetail.getGrid().getStore().get(i).getOHLList());
                        for (OverHeadLine_Mdl ohlm : lstOHLMdl) {
                            if (ohlm != null) {
                                ohlm.setAccountCode(grossAcCode);
                                ohlm.setAccountName(grossAcName);
                            }
                        }
                    }
                    ohlHandler.calculate(grdItemDetail.getGrid().getStore().get(i).getAmount(), i);
                }
            }
        }
        grdItemDetail.commitChanges();

        calculate();
    }

    //=================================<MJ>==============================//
    @Override
    public void onSelection(SelectionEvent<Item> event) {

        if (event.getSource().equals(mnuAutoVoucherAdjustment)) {
            if (topControl.getMode().equalsIgnoreCase("Browse")
                    && DmShared.iNull(topControl.getDocId()).longValue() > 0) {
                new DmConfirmMessageBox("Are you sure? You want to set adjustment.", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            processAutoVoucherAdjustment();
                        }
                    }
                }).show();

            } else {
                new DmAlertMessageBox("Can be use in Browse Mode only and select a record.", txtHVoucherType).show();
            }
        }
    }

    private void processAutoVoucherAdjustment() {
        String docId = topControl.getMode().equalsIgnoreCase("New") ? "" : topControl.getDocId();
        if (DmShared.iNull(DmShared.xNull(docId)).longValue() == 0) {
            new DmAlertMessageBox("Voucher ID is required for voucher adjustment.", (DmTextBox) null).show();
            return;
        }

        srvAsync.invokeRPC(true).processAutoVoucherAdjustment(DmShared.xNull(docId), DmShared.xNull(txtHSupplier.getTag()), new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    new DmAlertMessageBox("Voucher Adjustment is processed successfully!", (DmTextBox) null).show();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

//    private void manageProformaBillChange() {
//        txtProformaBillDate.setText("");
//        txtProformaBillDate.setTag("");
//        txtProformaBillDate.setTag2nd("");
////        selectedProformaReturnBillTag="";
//        fillProformaPurchaseReturnItemGrid(DmShared.xNull(txtHProformaBillNo.getTag()));
//    }
    private void fillProformaPurchaseReturnItemGrid(String pfReturnDocId) {
        srvAsync.invokeRPC(true).getProformaPurchaseReturnItemDetail(pfReturnDocId, location, txtVoucherDate.getText(), new AsyncCallback<DmResult<List<GrdItemDetail_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHProformaBillNo).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdItemDetail_Mdl>> result) {
                if (result.isStatus() && result.getData() != null && result.getData().size() > 0) {
                    grdItemDetail.getGrid().getStore().clear();
                    grdItemDetail.getGrid().getStore().addAll(result.getData());
                    fillItemITS(result.getData());
                }
            }
        });
    }

    private void fillItemITS(List<GrdItemDetail_Mdl> listItems) {
        String grossAcCode, grossAcName;
        for (int i = 0; i < listItems.size(); i++) {
            GrdItemDetail_Mdl detail_Mdl = listItems.get(i);
            if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() > 0) {
                ohlHandler.setITS(grdItemDetail.getGrid().getStore().get(i).getLstITS(), i);
                ohlHandler.setReference(detail_Mdl.getOHLListPurchaseBill(), ohlHandler.getOverHeadList(i));
                ohlHandler.calculate(grdItemDetail.getGrid().getStore().get(i).getAmount(), i);
                OverHeadLine_Mdl ohlMdlIV = OverHeadLine_Shared.getOHByUniqueId("IV", ohlHandler.getOverHeadList(i));
                grdItemDetail.getGrid().getStore().get(i).setLandedSkuValue(DmClient.vNull(ohlMdlIV != null ? ohlMdlIV.getAmount() : 0, "0.00"));
                grdItemDetail.getGrid().getStore().get(i).setOHLList(ohlHandler.getOverHeadList(i));

                if (!defaultValue.isInventoryAccountingActive()) {
                    grossAcCode = DmShared.xNull(grdItemDetail.getGrid().getStore().get(i).getGrossAcCode());
                    grossAcName = DmShared.xNull(grdItemDetail.getGrid().getStore().get(i).getGrossAcName());
                } else {
                    grossAcCode = DmShared.xNull(grdItemDetail.getGrid().getStore().get(i).getInventoryAc());
                    grossAcName = DmShared.xNull(grdItemDetail.getGrid().getStore().get(i).getInventoryAcName());
                }
                // Setting account to one GS + multiple [GS] unique ids
                if (!DmShared.xNull(grossAcCode).trim().equalsIgnoreCase("") && !DmShared.xNull(grossAcName).trim().equalsIgnoreCase("")) {
                    if (!defaultValue.isInventoryAccountingActive()) {
                        List<OverHeadLine_Mdl> lstOHLMdl = OverHeadLine_Shared.getOHByUniqueIds("GS", grdItemDetail.getGrid().getStore().get(i).getOHLList());
                        for (OverHeadLine_Mdl ohlm : lstOHLMdl) {
                            if (ohlm != null) {
                                ohlm.setAccountCode(grossAcCode);
                                ohlm.setAccountName(grossAcName);
                            }
                        }
                    } else {
                        List<OverHeadLine_Mdl> lstOHLMdl = OverHeadLine_Shared.getOHByUniqueIds("IV", grdItemDetail.getGrid().getStore().get(i).getOHLList());
                        for (OverHeadLine_Mdl ohlm : lstOHLMdl) {
                            if (ohlm != null) {
                                ohlm.setAccountCode(grossAcCode);
                                ohlm.setAccountName(grossAcName);
                            }
                        }
                    }
                }
                ohlHandler.calculate(grdItemDetail.getGrid().getStore().get(i).getAmount(), i);
            }
        }
        grdItemDetail.commitChanges();
        calculate();
    }

    //<editor-fold defaultstate="collapsed" desc="comment">
    /*
      private void fillItemGrid(String billDocId) {
        srvAsync.invokeRPC(true).getItemDetail(billDocId, location, txtVoucherDate.getText(), topControl.getMode().equalsIgnoreCase("New") ? "" : topControl.getDocId(), new AsyncCallback<DmResult<List<GrdItemDetail_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHProformaBillNo).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdItemDetail_Mdl>> result) {
                if (result.isStatus() && result.getData() != null) {
                    grdItemDetail.getGrid().getStore().clear();
                    grdItemDetail.getGrid().getStore().addAll(result.getData());
                    fillItemITS(result.getData());
                }
            }
        });
    }

    private void fillItemITS(List<GrdItemDetail_Mdl> listItems) {
        String grossAcCode, grossAcName;
        for (int i = 0; i < listItems.size(); i++) {
            GrdItemDetail_Mdl detail_Mdl = listItems.get(i);
            if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() > 0) {
                ohlHandler.setITS(grdItemDetail.getGrid().getStore().get(i).getLstITS(), i);
                ohlHandler.setReference(detail_Mdl.getOHLListPurchaseBill(), ohlHandler.getOverHeadList(i));
                ohlHandler.calculate(grdItemDetail.getGrid().getStore().get(i).getAmount(), i);
                OverHeadLine_Mdl ohlMdlIV = OverHeadLine_Shared.getOHByUniqueId("IV", ohlHandler.getOverHeadList(i));
                grdItemDetail.getGrid().getStore().get(i).setLandedSkuValue(DmClient.vNull(ohlMdlIV != null ? ohlMdlIV.getAmount() : 0, "0.00"));
                grdItemDetail.getGrid().getStore().get(i).setOHLList(ohlHandler.getOverHeadList(i));

                if (!defaultValue.isInventoryAccountingActive()) {
                    grossAcCode = DmShared.xNull(grdItemDetail.getGrid().getStore().get(i).getGrossAcCode());
                    grossAcName = DmShared.xNull(grdItemDetail.getGrid().getStore().get(i).getGrossAcName());
                } else {
                    grossAcCode = DmShared.xNull(grdItemDetail.getGrid().getStore().get(i).getInventoryAc());
                    grossAcName = DmShared.xNull(grdItemDetail.getGrid().getStore().get(i).getInventoryAcName());
                }
                // Setting account to one GS + multiple [GS] unique ids
                if (!DmShared.xNull(grossAcCode).trim().equalsIgnoreCase("") && !DmShared.xNull(grossAcName).trim().equalsIgnoreCase("")) {
                    if (!defaultValue.isInventoryAccountingActive()) {
                        List<OverHeadLine_Mdl> lstOHLMdl = OverHeadLine_Shared.getOHByUniqueIds("GS", grdItemDetail.getGrid().getStore().get(i).getOHLList());
                        for (OverHeadLine_Mdl ohlm : lstOHLMdl) {
                            if (ohlm != null) {
                                ohlm.setAccountCode(grossAcCode);
                                ohlm.setAccountName(grossAcName);
                            }
                        }
                    } else {
                        List<OverHeadLine_Mdl> lstOHLMdl = OverHeadLine_Shared.getOHByUniqueIds("IV", grdItemDetail.getGrid().getStore().get(i).getOHLList());
                        for (OverHeadLine_Mdl ohlm : lstOHLMdl) {
                            if (ohlm != null) {
                                ohlm.setAccountCode(grossAcCode);
                                ohlm.setAccountName(grossAcName);
                            }
                        }
                    }
                }
                ohlHandler.calculate(grdItemDetail.getGrid().getStore().get(i).getAmount(), i);
            }
        }
        grdItemDetail.commitChanges();
        calculate();
    }
     */
//</editor-fold>
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
                ohlHandler.syncronizeLineHeader();
                calculate();
            }

            @Override
            public void onDelete() {
                grdItemDetail.commitChanges();
                ohlHandler.syncronizeLineHeader();
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
    }

    private void showPurchaseReturnGridEdit(int currentRowIndex) {
        if (DmShared.xNull(txtHVoucherType.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please select Voucher Type.", txtHVoucherType).show();
            return;
        }
        if (DmShared.dNull(txtVoucherDate.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please fiil Voucher Date.", txtVoucherDate).show();
            return;
        }
        if (DmShared.xNull(txtHSupplier.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please select Supplier.", txtHSupplier).show();
            return;
        }
        if (DmShared.xNull(txtHDepartment.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please select Department.", txtHDepartment).show();
            return;
        }
        if (DmShared.xNull(txtHGstReason.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please select Reason.", txtHGstReason).show();
            return;
        }
//        if (DmShared.xNull(txtSupplierBillNo.getText()).trim().equalsIgnoreCase("")) {
//            new DmAlertMessageBox("Please fill Ref. No.", txtSupplierBillNo).show();
//            return;
//        }
//        if (DmShared.dNull(txtSupplierBillDate.getText()).trim().equalsIgnoreCase("")) {
//            new DmAlertMessageBox("Please fill Ref. Date.", txtSupplierBillDate).show();
//            return;
//        }
//
//        if ((DmShared.convertDate(txtVoucherDate.getText()).getTime() < (DmShared.convertDate(txtSupplierBillDate.getText()).getTime()))) {
//            new DmAlertMessageBox("Voucher Date should be greater than or equal to Ref. Date.", txtVoucherDate).show();
//            return;
//        }

        if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {
            returnGridEdit.show(currentRowIndex, false, false, false, this, defaultValue.isInventoryAccountingActive());
        } else {
            returnGridEdit.show(currentRowIndex, true, true, false, this, defaultValue.isInventoryAccountingActive());
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
        } else if (event.getSource() == btnAddPBill) {
            if (DmShared.iNull(txtHSupplier.getTag()).longValue() == 0) {
                new DmAlertMessageBox("Please select Supplier.", txtHSupplier).show();
            } else if (DmShared.dNull(txtVoucherDate.getText()).trim().equals("")) {
                new DmAlertMessageBox("Please enter Voucher Date.", txtVoucherDate).show();
            } else {
                showPurchaseBillSearch(false);
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
                ohlHandler.initialize();
                selectedSupplierCode = "";
            }
            resetGstDetails();
        }
//        else if (event.getSource() == txtSupplierBillDate) {
//            txtSupplierBillDate.setText(DmClient.returnDate(txtSupplierBillDate.getText()));
//        }
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
        overHead.setBannerVisible(true);
        gui.setBannerVisible(false);
        iniDefaultValues(swi);

        btnAddPBill.setEnabled(false);
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
                    gui.initializeViaTemplateCode();
                    taxSummary.initialize(defaultValue.isGstApplicable());
                    isIncludeTaxInCost = defaultValue.isIncludeTaxInCost();
                    swi.setWidget(PurchaseReturn.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void chkTaxation() {
        OverHead_Mdl ohCGST = OverHead_Shared.getOHByManualCode("CGST", overHead.getOverHeadList());

        double ohCGSTVal = DmShared.vNull(ohCGST.getAmount());

        OverHead_Mdl ohSGST = OverHead_Shared.getOHByManualCode("SGST", overHead.getOverHeadList());
        double ohSGSTVal = DmShared.vNull(ohSGST.getAmount());

        OverHead_Mdl ohIGST = OverHead_Shared.getOHByManualCode("IGST", overHead.getOverHeadList());
        double ohIGSTVal = DmShared.vNull(ohIGST.getAmount());

        OverHead_Mdl ohUGST = OverHead_Shared.getOHByManualCode("UGST", overHead.getOverHeadList());
        double ohUGSTVal = DmShared.vNull(ohUGST.getAmount());

        OverHead_Mdl ohTaxable = OverHead_Shared.getOHByManualCode("TV", overHead.getOverHeadList());
        double ohTaxableVal = DmShared.vNull(ohTaxable.getAmount());

        double totalTax = ohCGSTVal + ohSGSTVal + ohIGSTVal + ohUGSTVal;

        List<List<OverHeadLine_Mdl>> ohlList = new ArrayList<List<OverHeadLine_Mdl>>();
        for (GrdItemDetail_Mdl grdMdl : grdItemDetail.getGrid().getStore().getAll()) {
            if (grdMdl != null && grdMdl.getOHLList() != null) {
                ohlList.add(grdMdl.getOHLList());
            }
        }
        TaxSummary_Mdl taxsum = new TaxSummary_Mdl();
        taxsum = STClient.verifyOHFromItemList(ohlList);

        if (DmShared.vNull(taxsum.getTaxableAmt()) != ohTaxableVal) {
            new DmAlertMessageBox("Total Taxable Amount not matched with Overhead Taxable Amount.", (DmTextBox) null).show();
            return;
        }

        if (DmShared.vNull(taxsum.getTotalAmt()) != totalTax) {
            new DmAlertMessageBox("Total Tax Amount not matched with Overhead Tax Amount.", (DmTextBox) null).show();
            return;
        }

    }

    private void topControlSave() {
        PurchaseReturn_Mdl mrTemp;

        try {

            ohlHandler.syncronizeLineHeader();
            defaultValue.setNewVoucherTypeCode(txtHVoucherType.getTag());
            defaultValue.setNewVoucherTypeName(txtHVoucherType.getText());
            defaultValue.setNewVoucherTypeCategory(voucherCategory);
            defaultValue.setNewVoucherDate(txtVoucherDate.getText());
            defaultValue.setNewGuiTemplateCode(guiTemplateCode);
            calculate();
            mrTemp = topControlSaveInitialize();
            PurchaseReturn_Vld.chkSave(mrTemp);
            OverHead_Shared.chkGrid(mrTemp.getOverHeadList());
            OverHeadLine_Shared.chkGrid(mrTemp.getListItemDetail());

            //Important
            chkTaxation();
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
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getErrorSourceCode(), e.getMessage());
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
        mrTemp.setRcmApplicable(DmShared.xNull(rcmApplicable));
        mrTemp.setPlaceOfSupply(DmShared.xNull(txtHSupplier.getTag2nd()));
        mrTemp.setDefaultValueMdl(defaultValue);
        mrTemp.setDepartmentCode(DmShared.xNull(txtHDepartment.getTag()));
        mrTemp.setDepartmentName(DmShared.xNull(txtHDepartment.getText()));
        mrTemp.setGstReasonCode(DmShared.xNull(txtHGstReason.getTag()));
        mrTemp.setGstReasonName(DmShared.xNull(txtHGstReason.getText()));
        mrTemp.setProformaBillNo(DmShared.xNull(txtHProformaBillNo.getText()));
        mrTemp.setProformaBillNoTag(DmShared.xNull(txtHProformaBillNo.getTag()));
        mrTemp.setProformaBillDate(DmShared.dNull(txtProformaBillDate.getText()));

        mrTemp.setRemark(DmShared.xNull(txtRemark.getText()).trim());

        OverHead_Mdl ohMdl = OverHead_Shared.getOHByUniqueId("RO", overHead.getOverHeadList());
        mrTemp.setRoundOffAmount(ohMdl != null ? DmShared.vNull(ohMdl.getAmount()).toString() : "0");

        OverHead_Mdl ohMdlNa = OverHead_Shared.getOHByUniqueId("PT", overHead.getOverHeadList());
        mrTemp.setNetAmount(ohMdl != null ? DmShared.vNull(ohMdlNa.getAmount()).toString() : "0");

        mrTemp.getListItemDetail().clear();
        mrTemp.getListItemDetail().addAll(grdItemDetail.getGrid().getStore().getAll());

        mrTemp.setGuiMdl(gui.getGUI_Mdl());
        mrTemp.setGuiConfigCode(gui.getConfigCode());

        mrTemp.getOverHeadList().clear();
        mrTemp.getOverHeadList().addAll(overHead.getOverHeadList());

        if (modifyReasonMdl != null) {
            mrTemp.setModifyReasonMdl(modifyReasonMdl);
        }

        return mrTemp;
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
                new DmAlertMessageBox(errorMessage, txtVoucherDate).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtHSupplier).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtHDepartment).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtHGstReason).show();
                break;
            case 600:
                new DmAlertMessageBox(errorMessage, txtHProformaBillNo).show();
                break;
            case 800:
                new DmAlertMessageBox(errorMessage, new HideEvent.HideHandler() {
                    @Override
                    public void onHide(HideEvent event) {
                        ((CellSelectionModel) grdItemDetail.getGrid().getSelectionModel()).selectCell(DmShared.iNull(gridRowIndex).intValue(), 1);
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
        ohlHandler.setDefault();
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
            txtVoucherDate.setText(mrTemp.getVoucherDate());
            txtVoucherDate.setTag(mrTemp.getVoucherDate());
            voucherCategory = mrTemp.getVoucherCategory();
            selectedSupplierCode = mrTemp.getSupplierCode();
            txtHSupplier.setTag(mrTemp.getSupplierCode());
            txtHSupplier.setText(mrTemp.getSupplierName());
            txtHSupplier.setTag2nd(mrTemp.getSupplierStateCode());
            supplierGstin = mrTemp.getSupplierGstin();
            supplierGstinDate = mrTemp.getSupplierGstinDate();
            partyType = mrTemp.getPartyType();
            partyTypeForEntry = mrTemp.getPartyTypeForEntry();
            rcmApplicable = mrTemp.getRcmApplicable();
            txtHDepartment.setTag(mrTemp.getDepartmentCode());
            txtHDepartment.setText(mrTemp.getDepartmentName());
            txtHGstReason.setTag(mrTemp.getGstReasonCode());
            txtHGstReason.setText(mrTemp.getGstReasonName());
//            selectedProformaReturnBillTag = mrTemp.getProformaBillNoTag();
            txtHProformaBillNo.setText(mrTemp.getProformaBillNo());
            txtHProformaBillNo.setTag(mrTemp.getProformaBillNoTag());
            txtHProformaBillNo.setTag2nd(mrTemp.getProformaBillNoTag());
            txtProformaBillDate.setText(mrTemp.getProformaBillDate());

            txtRemark.setText(mrTemp.getRemark());

            userControl.setTextBlog(preparedByBlog, mrTemp.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, mrTemp.getSharedModifiedBy());
            userControl.setTextBlog(approvedByBlog, mrTemp.getApprovedBy());

            grdItemDetail.getGrid().getStore().clear();
            grdItemDetail.getGrid().getStore().addAll(mrTemp.getListItemDetail());

            overHead.setOverHeadList(mrTemp.getOverHeadList(), mrTemp.getGrossAmount());
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
        srvAsync.invokeRPC().deleteRecord(strKey, gui.getConfigCode(), previous_Mdl, new AsyncCallback<DmResult<String>>() {

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
                            topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                        }
                    }
                });
            }
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
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

    private void resetGstDetails() {

        //=====================================< Set Party Type For Entry >=================================//
        if (!DmShared.dNull(txtVoucherDate.getText()).trim().equals("")
                && !DmShared.dNull(supplierGstinDate).trim().equals("")
                && DmShared.convertDate(txtVoucherDate.getText()).getTime() < DmShared.convertDate(supplierGstinDate).getTime()) {
            partyTypeForEntry = DmEnum.DmPartyType.Unregistered.getId();
        } else {
            partyTypeForEntry = partyType;
        }

        //=====================================< Set RCM Applicable >=================================//
        if (!DmShared.dNull(defaultValue.getSiteGstinDate()).trim().equals("")
                && !DmShared.dNull(txtVoucherDate.getText()).trim().equals("")
                && DmShared.convertDate(defaultValue.getSiteGstinDate()).getTime() <= DmShared.convertDate(txtVoucherDate.getText()).getTime()
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

    private void manageEvent() {
        btnAddPBill.addClickHandler(this);
        btnApprovedBy.addClickHandler(this);
        flwForm.addAttachHandler(this);
        txtVoucherDate.addBlurHandler(this);

        mnuAutoVoucherAdjustment.addSelectionHandler(this);

    }

    private void manageDisplay(boolean flag) {
        txtVoucherDate.setEnabled(false);
        txtVoucherNo.setEnabled(false);
        txtProformaBillDate.setEnabled(false);
        if (!departmentEditable) {
            txtHDepartment.setEnabled(false);
        }
        if (topControl.getMode().equalsIgnoreCase("Edit")) {
            txtHVoucherType.setEnabled(false);
            txtVoucherDate.setEnabled(false);
            txtHSupplier.setEnabled(false);

        }

        flwAddPBill.clear();
        if (flag) {
            flwAddPBill.add(btnAddPBill);
        }
    }

    private void onAdd() {
        clear();
        manageDisplay(true);
        if (defaultValue.getIsContinuousNewMode()) {
            setDefaultValues();
        } else {
            setDefaultValuesWithDateReset();
        }
        grdItemDetail.getGrid().getStore().clear();
//        grdItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
    }

    private void setDefaultValues() {
        txtVoucherDate.setText(defaultValue.getIsContinuousNewMode() ? DmShared.dNull(defaultValue.getNewVoucherDate()) : DmShared.dNull(defaultValue.getCurrentDate()));
        txtVoucherDate.setTag(txtVoucherDate.getText());
        txtHVoucherType.setTag2nd(defaultValue.getIsContinuousNewMode() ? defaultValue.getNewVoucherTypeCode() : defaultValue.getVoucherTypeCode());
        txtHVoucherType.setTag(defaultValue.getIsContinuousNewMode() ? defaultValue.getNewVoucherTypeCode() : defaultValue.getVoucherTypeCode());
        txtHVoucherType.setText(defaultValue.getIsContinuousNewMode() ? defaultValue.getNewVoucherTypeName() : defaultValue.getVoucherTypeName());
        voucherCategory = defaultValue.getIsContinuousNewMode() ? defaultValue.getNewVoucherTypeCategory() : defaultValue.getVoucherCategory();
        guiTemplateCode = defaultValue.getIsContinuousNewMode() ? defaultValue.getNewGuiTemplateCode() : defaultValue.getGuiTemplateCode();
        if (DmShared.iNull(defaultValue.getDefaultPurchaseDepartment()).intValue() != 0) {
            txtHDepartment.setTag(defaultValue.getDefaultPurchaseDepartment());
            txtHDepartment.setText(defaultValue.getDefaultPurchaseDepartmentText());
        } else {
            txtHDepartment.setTag(defaultValue.getDepartmentCode());
            txtHDepartment.setText(defaultValue.getDepartmentName());
        }

        ohlHandler.initialize();
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

    private void clear() {
        selectedSupplierCode = "";
//        selectedProformaReturnBillTag = "";
        partyTypeForEntry = "";
        partyType = "";
        supplierGstin = "";
        supplierGstinDate = "";
        voucherCategory = "";
        guiTemplateCode = "";
        location = "";
        rcmApplicable = "";
        previous_Mdl = null;
        overHead.clear();
        gui.clear();
        grdItemDetail.getGrid().getStore().clear();
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        userControl.setTextBlog(approvedByBlog, "");
    }

    private void calculate() {
        double netAmount = 0;
        for (GrdItemDetail_Mdl detail_Mdl : grdItemDetail.getGrid().getStore().getAll()) {
            if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() > 0) {
                netAmount += DmShared.vNull(detail_Mdl.getAmount()).doubleValue();
            }
        }
        grdItemDetail.commitChanges();
        overHead.calculate(DmClient.vNull(netAmount, "0.00"));
        calculateLandedValue();
//        checkLandedSkuRate();
        resetTaxSummary();
    }

    public void checkLandedSkuRate() {
        List< GrdItemDetail_Mdl> listItemDetail = grdItemDetail.getGrid().getStore().getAll();
        for (GrdItemDetail_Mdl detailMdl : listItemDetail) {
            //===================< Landed Sku Rate Check >===================================
            int a = 0, b = 0;
            a = DmShared.vNull(detailMdl.getpBillLandedSkuRate()).intValue();
            b = DmShared.vNull(detailMdl.getLandedSkuRate()).intValue();
            if (a != b) {
                new DmAlertMessageBox("LandedSkuRate is different for medicine " + DmShared.xNull(detailMdl.getSharedKeyFieldName()), (DmTextBox) null).show();
            }

        }
    }

    public void calculateLandedValue() {

        double landedValue = 0, totalGrossAmount = 0, lineGrossAmount = 0, lineDiscountAmount = 0d, staffSkuValue = 0d;
        double lineAmt[] = new double[grdItemDetail.getGrid().getStore().getAll().size()];
        landedValue = overHead.getLandedValue();

        List< GrdItemDetail_Mdl> listItemDetail = grdItemDetail.getGrid().getStore().getAll();
        for (GrdItemDetail_Mdl detailMdl : listItemDetail) {
            if (detailMdl != null) {
                if (!DmShared.xNull(detailMdl.getSharedKeyFieldCode()).trim().equals("")) {
                    if (isIncludeTaxInCost) {
                        OverHeadLine_Mdl ohMdlLn = OverHeadLine_Shared.getOHByManualCode("IV", detailMdl.getOHLList());
                        totalGrossAmount += DmShared.vNull(ohMdlLn.getAmount());
                        lineAmt[listItemDetail.indexOf(detailMdl)] = DmShared.vNull(ohMdlLn.getAmount()).doubleValue();
                    } else {
//                        OverHeadLine_Mdl ohMdlLn = OverHeadLine_Shared.getOHByManualCode("TV", detailMdl.getOHLList());
                        OverHeadLine_Mdl ohMdlLn = OverHeadLine_Shared.getOHByManualCode("IV", detailMdl.getOHLList());
                        totalGrossAmount += DmShared.vNull(ohMdlLn.getAmount());
                        lineAmt[listItemDetail.indexOf(detailMdl)] = DmShared.vNull(ohMdlLn.getAmount()).doubleValue();
                    }
                }
//                if (!DmShared.xNull(detailMdl.getSharedKeyFieldCode()).trim().equals("")) {
//                    totalGrossAmount += Math.abs(DmShared.vNull(detailMdl.getAmount()));
//                }
            }
        }

        double landedSkuValue = 0, totalLandedSkuValue = 0, landedSkuRate = 0;
        GrdItemDetail_Mdl lastLineMdl = null;
        for (GrdItemDetail_Mdl lineMdl : listItemDetail) {
            if (lineMdl != null) {
                if (!DmShared.xNull(lineMdl.getSharedKeyFieldCode()).trim().equals("")) {
//                    lineGrossAmount = Math.abs(DmShared.vNull(lineMdl.getAmount()));
                    lineGrossAmount = lineAmt[listItemDetail.indexOf(lineMdl)];

                    landedSkuValue = OverHead_Shared.getLandedValue(landedValue, totalGrossAmount, lineGrossAmount);
                    lineMdl.setLandedSkuValue(DmClient.vNull(landedSkuValue, "0.00"));

                    landedSkuRate = (DmShared.vNull(lineMdl.getLandedSkuValue())) / (DmShared.vNull(lineMdl.getSkuQty()));
//                    lineMdl.setLandedSkuRate(DmClient.vNull(landedSkuRate, "0.0000000"));
                    lineMdl.setLandedSkuRate(DmClient.vNull(landedSkuRate, "0.00"));

                    lineDiscountAmount = DmShared.vNull(OverHeadLine_Shared.getOHByManualCode("DISC", lineMdl.getOHLList()).getAmount());
                    staffSkuValue = landedSkuValue + lineDiscountAmount;
                    lineMdl.setStaffSkuValue(DmClient.vNull(staffSkuValue, "0.0000000"));

//                    totalLandedSkuValue += DmShared.vNull(lineMdl.getLandedSkuValue());
//                    lastLineMdl = lineMdl;
                }
            }
        }

//        if (lastLineMdl != null) {
//            landedSkuValue = DmShared.vNull(DmClient.vNull(lastLineMdl.getLandedSkuValue(), "0.00")) - (DmShared.vNull(DmClient.vNull(totalLandedSkuValue, "0.00")) - DmShared.vNull(DmClient.vNull(landedValue, "0.00")));
//            lastLineMdl.setLandedSkuValue(DmClient.vNull(landedSkuValue, "0.00"));
//
//            landedSkuRate = Math.abs(DmShared.vNull(lastLineMdl.getLandedSkuValue())) / Math.abs(DmShared.vNull(lastLineMdl.getSkuQty()));
//            lastLineMdl.setLandedSkuRate(DmClient.vNull(landedSkuRate, "0.0000000"));
//        }
        grdItemDetail.commitChanges();

//        double landedValue = 0, totalGrossAmount = 0, lineGrossAmount = 0;
//
//        landedValue = overHead.getLandedValue();
//
//        for (GrdItemDetail_Mdl detailMdl : grdItemDetail.getGrid().getStore().getAll()) {
//            if (detailMdl != null) {
//                if (!DmShared.xNull(detailMdl.getSharedKeyFieldCode()).trim().equals("")) {
//                    totalGrossAmount += DmShared.vNull(detailMdl.getAmount());
//                }
//            }
//        }
//
//        double landedSkuValue = 0, totalLandedSkuValue = 0, landedSkuRate = 0;
//        GrdItemDetail_Mdl lastLineMdl = null;
//        for (GrdItemDetail_Mdl lineMdl : grdItemDetail.getGrid().getStore().getAll()) {
//            if (lineMdl != null) {
//                if (!DmShared.xNull(lineMdl.getSharedKeyFieldCode()).trim().equals("")) {
//                    lineGrossAmount = DmShared.vNull(lineMdl.getAmount());
//
//                    landedSkuValue = OverHead_Shared.getLandedValue(landedValue, totalGrossAmount, lineGrossAmount);
//                    lineMdl.setLandedSkuValue(DmClient.vNull(landedSkuValue, "0.00"));
//
//                    landedSkuRate = DmShared.vNull(lineMdl.getLandedSkuValue()) / DmShared.vNull(lineMdl.getSkuQty());
//                    lineMdl.setLandedSkuRate(DmClient.vNull(landedSkuRate, "0.0000000"));
//
//                    totalLandedSkuValue += DmShared.vNull(lineMdl.getLandedSkuValue());
//                    lastLineMdl = lineMdl;
//                }
//            }
//        }
//
//        if (lastLineMdl != null) {
//            landedSkuValue = DmShared.vNull(DmClient.vNull(lastLineMdl.getLandedSkuValue(), "0.00")) - (DmShared.vNull(DmClient.vNull(totalLandedSkuValue, "0.00")) - DmShared.vNull(DmClient.vNull(landedValue, "0.00")));
//            lastLineMdl.setLandedSkuValue(DmClient.vNull(landedSkuValue, "0.00"));
//
//            landedSkuRate = DmShared.vNull(lastLineMdl.getLandedSkuValue()) / DmShared.vNull(lastLineMdl.getSkuQty());
//            lastLineMdl.setLandedSkuRate(DmClient.vNull(landedSkuRate, "0.0000000"));
//        }
//
//        grdItemDetail.commitChanges();
    }

    private void resetTaxSummary() {
        List<List<OverHeadLine_Mdl>> ohlList = new ArrayList<List<OverHeadLine_Mdl>>();
        for (GrdItemDetail_Mdl grdMdl : grdItemDetail.getGrid().getStore().getAll()) {
            if (grdMdl != null && grdMdl.getOHLList() != null) {
                ohlList.add(grdMdl.getOHLList());
            }
        }
        taxSummary.reset(ohlList);
    }

    private void prnRecord(boolean printToPrinter) {
        HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
        hashMapFilter.put("docId", new FieldUDT_Mdl("Return No.", DmShared.xNull(txtVoucherNo.getText()), DmShared.xNull(topControl.getDocId())));
        new OpenSourceReport(txtHVoucherType.getTag(), "", widgetName, widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
            @Override
            public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                DmClient.localSettingPrintMap(reportProperty, moduleName, widgetName, "Print");
                return reportProperty;
            }
        }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
    }

    private void showPurchaseBillSearch(boolean isAutoShow) {
        pBillSearch.show(
                topControl.getMode().equalsIgnoreCase("New") ? "" : topControl.getDocId(),
                txtHSupplier.getTag(),
                txtVoucherDate.getText(),
                voucherCategory, isAutoShow, singlePBillFlowInPurchase);
    }
}
