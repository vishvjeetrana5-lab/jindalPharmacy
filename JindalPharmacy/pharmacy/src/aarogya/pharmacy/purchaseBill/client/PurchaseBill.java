package aarogya.pharmacy.purchaseBill.client;

import aarogya.pharmacy.purchaseBill.shared.*;
import aarogya.pharmacy.grnSearch.client.GRNSearch;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
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
import dataman.dmToolkit.overHead.client.OverHead;
import dataman.dmToolkit.overHead.shared.OverHead_Shared;
import dataman.dmToolkit.overHeadLine.client.OverHeadLineHandler;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Shared;
import dataman.dmToolkit.walkThrough.client.WalkThrough;
import java.util.List;
import aarogya.pharmacy.purchaseBill.shared.PurchaseBill_Srv;
import aarogya.pharmacy.purchaseBill.shared.PurchaseBill_SrvAsync;
import aarogya.projectMenu.self.shared.ProjectRuleCode;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.modifyReason.client.ModifyReason;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.pharmacyDiscountLineWise.client.PharmacyDiscountLineWise;
import aarogya.selfToolkit.pharmacyDiscountLineWise.shared.PharmacyDiscountLineWise_Mdl;
import aarogya.selfToolkit.pharmacyDiscountPopup.client.PharmacyDiscountPopup;
import aarogya.selfToolkit.pharmacyDiscountPopup.shared.PharmacyDiscountPopup_Mdl;
import aarogya.selfToolkit.stStatic.client.STClient;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.menu.Item;
import dataman.dmToolkit.dmFileManager.client.DmFileManager;
import dataman.dmToolkit.dmGUI.client.DmGUI;
import dataman.dmToolkit.dmGUI.shared.DmGUIControl_Mdl;
import dataman.dmToolkit.dmGUI.shared.DmGUI_Mdl;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import dataman.dmToolkit.overHead.shared.OverHeadMA_Mdl;
import dataman.dmToolkit.overHead.shared.OverHead_Mdl;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Mdl;
import dataman.dmToolkit.tdsCategoryPopup.client.TdsCategoryPopup;
import dataman.dmToolkit.tdsCategoryPopup.shared.TdsCategoryPopup_Mdl;
import java.util.ArrayList;
import java.util.HashMap;

public final class PurchaseBill extends PurchaseBill_Dsg implements BlurHandler, AttachEvent.Handler, DmRootEvents, ClickHandler, SelectionHandler<Item> {

    private ServiceInvoker<PurchaseBill_SrvAsync> srvAsync;
    protected DefaultValue_Mdl defaultValue;
    private final PurchaseBillGridEdit<GrdItemDetail_Mdl, GridItemDetail_PrpAcs> itemDetailGridEdit = new PurchaseBillGridEdit(gridMedDetail);
    protected final OverHeadLineHandler<GrdItemDetail_Mdl> ohlHandler = new OverHeadLineHandler(overHead, 300, 188);
    private final DmFileManager manageFile = new DmFileManager(ProjectRuleCode.PURCHASE_BILL_DIRECT_ENTRY.getRuleCode());
    private final GRNSearch grnSearch = new GRNSearch();
    private ModifyReason_Mdl modifyReasonMdl;
    private PurchaseBill_Mdl previous_Mdl = null;
    private final ModifyReason modifyReason = new ModifyReason();
    private WalkThrough walkThrough;
    String selectedSupplierCode, partyTypeForEntry, partyType, supplierGstin, supplierGstinDate, location,
            rcmApplicable, wtaCode, voucherCategory, guiTemplateCode, lastTransactionDate;
    private boolean isApprove = false, isAttachFile, isDeleteFile = false, singlePOFlowInPurchase = false;
    private PharmacyDiscountPopup_Mdl pharmacyDiscountPopup_Mdl;
    private final PharmacyDiscountPopup pharmacyDiscountPopup = new PharmacyDiscountPopup(true);
    private PharmacyDiscountLineWise_Mdl pharmacyDiscountLineWise_Mdl;
    private final PharmacyDiscountLineWise pharmacyDiscountLineWise = new PharmacyDiscountLineWise(true);
    final PharmacyUpdatePopup updatePopup = new PharmacyUpdatePopup();
    private final TdsCategoryPopup tdsCategoryPp = new TdsCategoryPopup();
    private TdsCategoryPopup_Mdl tdsCategoryPopup_Mdl;
    boolean saveModeActive = false;       // To restrict recreation of tds model on save event

    private void topControlEvents(UserMenu menuPermission) {

        topControl.setTitle(this.asWidget().getTitle());
        topControl.setRefreshRequired(false);
        for (String controlPermission : menuPermission.getControlPermission()) {
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

        ohlHandler.addCallback(new OverHeadLineHandler.Callback<GrdItemDetail_Mdl>() {

            @Override
            public void initialized() {
                gridMedDetail.getGrid().getStore().clear();
                gridMedDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
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
                return gridMedDetail.getGrid().getStore().getAll();
            }
        });

        topControl.setUserPermission(menuPermission, new VoucherPermissionHandler(DmClient.getVoucherPermission(new String[]{new ProjectVoucherCategory().PHARMACY_PURCHASE_BILL, new ProjectVoucherCategory().PHARMACY_PURCHASE_BILL_UNREGISTERED})) {
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
//                    txtSupplierBillNo.setFocus(true);
//                } 
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
                    showModifyReason();
                } else {
                    topControlSave();
                }
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(PurchaseBill.this);
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

        txtHVoucherType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHVoucher_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHVoucher(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHVoucher_Mdl>>>() {

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
                    if (topControl.getMode().equalsIgnoreCase("New")) {
                        txtHSupplier.setTag2nd("");
                        txtHSupplier.setTag("");
                        txtHSupplier.setText("");
                        ohlHandler.initialize();
                    }
                    if (blnStatus && t1Row != null) {
                        voucherCategory = t1Row.getVoucherCategory();
                        if (!DmShared.xNull(t1Row.getGuiTemplateCode()).equalsIgnoreCase("")) {
                            guiTemplateCode = t1Row.getGuiTemplateCode();
                            defaultValue.setBalancingAc(t1Row.getBalancingAc());
                            defaultValue.setBalancingAcName(t1Row.getBalancingAcName());
                        }
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
                    guiTemplateCode = DmShared.xNull(tRow.getSupplierGuiConfigCode());
                    txtPaymentDueDate.setText(tRow.getDueDays());
                    resetGstDetails();
                }
//gui.initializeViaTemplateCode();
                if (!DmShared.xNull(guiTemplateCode).equalsIgnoreCase("")) {
                    copyGUI(guiTemplateCode, tRow.getSharedKeyFieldCode());
                }

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

        grnSearch.addCallBack(new GRNSearch.CallBack() {

            @Override
            public void ok(List<KeyFieldExtended> listKF) {
                fillGrnDetail(listKF);
                txtSupplierBillNo.setFocus(true);
            }

            @Override
            public void close() {
                txtHSupplier.setFocus(true);
            }
        });

        pharmacyDiscountPopup.addCallBack(new PharmacyDiscountPopup.CallBack() {
            @Override
            public void onOk(PharmacyDiscountPopup_Mdl info_Mdl) {
                pharmacyDiscountPopup_Mdl = info_Mdl;
                setDiscount();
            }

            @Override
            public void onCancel() {
                txtRemark.setFocus(true);
            }

            @Override
            public void onClear() {
                if (!topControl.getMode().equalsIgnoreCase("Browse")) {
                    clearDiscount();
                    pharmacyDiscountPopup_Mdl = null;
                }
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

        pharmacyDiscountLineWise.addCallBack(new PharmacyDiscountLineWise.CallBack() {
            @Override
            public void onOk(PharmacyDiscountLineWise_Mdl pharmacyDiscountLineWise_Mdl) {

                double discPerc = 0d;
                if (DmShared.vNull(pharmacyDiscountLineWise_Mdl.getDiscount()) > 0) {

                    discPerc = (DmShared.vNull(pharmacyDiscountLineWise_Mdl.getDiscount()) / DmShared.vNull(pharmacyDiscountLineWise_Mdl.getTotalGrossAmount())) * 100;
                } else if (DmShared.vNull(pharmacyDiscountLineWise_Mdl.getDiscountPer()) > 0) {

                    discPerc = DmShared.vNull(pharmacyDiscountLineWise_Mdl.getDiscountPer());
                }

                for (int i = 0; i < gridMedDetail.getGrid().getStore().getAll().size(); i++) {

                    if (gridMedDetail.getGrid().getStore().get(i).getSharedKeyFieldCode() != null) {
                        double lineGrossAmt = DmShared.vNull(DmShared.vNull(gridMedDetail.getGrid().getStore().get(i).getAcceptQty()) * DmShared.vNull(gridMedDetail.getGrid().getStore().get(i).getRate()));
                        double lineDiscAmt = lineGrossAmt * discPerc / 100;
                        OverHeadLine_Shared.getOHByUniqueId("DS", gridMedDetail.getGrid().getStore().get(i).getOHLList()).setAmount(DmClient.vNull(lineDiscAmt, "0.00"));

                        ohlHandler.calculate(DmClient.vNull(lineGrossAmt, "0.00"), i);
                    }

                }

                gridMedDetail.commitChanges();
                ohlHandler.syncronizeLineHeader();
                overHead.commit();

                calculate();
            }

            @Override
            public void onClear() {
                for (int i = 0; i < gridMedDetail.getGrid().getStore().getAll().size(); i++) {

                    if (gridMedDetail.getGrid().getStore().get(i).getSharedKeyFieldCode() != null) {
                        double lineGrossAmt = DmShared.vNull(DmShared.vNull(gridMedDetail.getGrid().getStore().get(i).getAcceptQty()) * DmShared.vNull(gridMedDetail.getGrid().getStore().get(i).getRate()));
                        double lineDiscAmt = 0;
                        OverHeadLine_Shared.getOHByUniqueId("DS", gridMedDetail.getGrid().getStore().get(i).getOHLList()).setAmount(DmClient.vNull(lineDiscAmt, "0.00"));

                        ohlHandler.calculate(DmClient.vNull(lineGrossAmt, "0.00"), i);
                    }

                }

                gridMedDetail.commitChanges();
                ohlHandler.syncronizeLineHeader();
                overHead.commit();

                calculate();
            }

            @Override
            public void onCancel() {

            }
        }
        );

        updatePopup.addCallBack(new PharmacyUpdatePopup.CallBack() {
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

    private void clearDiscount() {
        for (int i = 0; i < gridMedDetail.getGrid().getStore().getAll().size(); i++) {
            GrdItemDetail_Mdl grdMdl = gridMedDetail.getGrid().getStore().get(i);
            if (DmShared.iNull(grdMdl.getSharedKeyFieldCode()).longValue() != 0) {
                OverHeadLine_Mdl oHLMdlA_DisAftTax = OverHeadLine_Shared.getOHByUniqueId("FD", gridMedDetail.getGrid().getStore().get(i).getOHLList());
                if (oHLMdlA_DisAftTax != null) {
                    oHLMdlA_DisAftTax.setAmount("");
                    oHLMdlA_DisAftTax.setAtRate("");
                }
                ohlHandler.calculate(gridMedDetail.getGrid().getStore().get(i).getAmount(), i);
            }
        }
        gridMedDetail.commitChanges();
        calculate();
        ohlHandler.syncronizeLineHeader();
    }

    private void setDiscount() {
        if (pharmacyDiscountPopup_Mdl != null) {
            int lastIndex = 0;
            double amt = 0, disc = 0, totalDisc = 0;
            boolean isDiscount = false;
            double amount = 0, discount = 0;

            /* ==============================<< Get total amount (and group by total amount) >>================================= */
            if ((DmShared.iNull(pharmacyDiscountPopup_Mdl.getDiscountPer()).doubleValue() == 0) && (DmShared.iNull(pharmacyDiscountPopup_Mdl.getDiscount()).doubleValue() != 0)) {
                int cnt = 0;
                for (GrdItemDetail_Mdl grdMdl : gridMedDetail.getGrid().getStore().getAll()) {
                    if (grdMdl.getSharedKeyFieldCode() != null) {
                        if ((DmShared.iNull(grdMdl.getSharedKeyFieldCode()).longValue() != 0)) {
                            OverHeadLine_Mdl oHLMdl = OverHeadLine_Shared.getOHByUniqueId("GS", gridMedDetail.getGrid().getStore().get(cnt).getOHLList());
                            if (oHLMdl != null) {
                                amt += DmShared.vNull(oHLMdl.getAmount());
                            }
                            lastIndex = cnt;
                        }
                    }
                    cnt++;
                }
                isDiscount = true;
                if (amt != 0) {
                    disc = DmShared.vNull((DmClient.vNull(DmShared.vNull(pharmacyDiscountPopup_Mdl.getDiscount()).doubleValue() / amt, "0.0000000"))).doubleValue();
                }
            }

            for (int i = 0; i < gridMedDetail.getGrid().getStore().getAll().size(); i++) {

                if (gridMedDetail.getGrid().getStore().get(i).getSharedKeyFieldCode() != null) {
                    if (DmShared.iNull(gridMedDetail.getGrid().getStore().get(i).getSharedKeyFieldCode()).longValue() != 0) {

                        OverHeadLine_Mdl oHLMdl_TaxAmt = OverHeadLine_Shared.getOHByUniqueId("GS", gridMedDetail.getGrid().getStore().get(i).getOHLList());
                        OverHeadLine_Mdl oHLMdlA_DisAftTax = OverHeadLine_Shared.getOHByUniqueId("FD", gridMedDetail.getGrid().getStore().get(i).getOHLList());
                        amount = oHLMdl_TaxAmt != null ? DmShared.vNull(oHLMdl_TaxAmt.getAmount()).doubleValue() : 0;
                        if (isDiscount) {
                            /* ==============================<< For Discount value >>================================= */
                            discount = DmShared.vNull(DmClient.vNull((disc * amount), "0.00")).doubleValue();
                            totalDisc += discount;

                            if (i == lastIndex) {
                                double adjAmt = 0;
                                if (totalDisc > DmShared.vNull(pharmacyDiscountPopup_Mdl.getDiscount()).doubleValue()) {
                                    adjAmt = DmShared.vNull(DmClient.vNull((totalDisc - DmShared.vNull(pharmacyDiscountPopup_Mdl.getDiscount()).doubleValue()), "0.00")).doubleValue();
                                    discount -= adjAmt;
                                } else if (totalDisc < DmShared.vNull(pharmacyDiscountPopup_Mdl.getDiscount()).doubleValue()) {
                                    adjAmt = DmShared.vNull(DmClient.vNull((DmShared.vNull(pharmacyDiscountPopup_Mdl.getDiscount()).doubleValue() - totalDisc), "0.00")).doubleValue();
                                    discount += adjAmt;
                                }
                            }
                        } else {
                            /* ==============================<< For Discount Percentage >>================================= */
                            discount = (amount * DmShared.vNull(DmClient.vNull(pharmacyDiscountPopup_Mdl.getDiscountPer(), "0.00")).doubleValue()) / 100;
                        }
                        if (oHLMdlA_DisAftTax != null) {
                            oHLMdlA_DisAftTax.setAmount(DmClient.vNull(discount, "0.00"));
                            oHLMdlA_DisAftTax.setAtRate(DmClient.vNull(pharmacyDiscountPopup_Mdl.getDiscountPer(), "0.00"));
                        }
                        ohlHandler.calculate(gridMedDetail.getGrid().getStore().get(i).getAmount(), i);

                    }

                }
            }
        }

        gridMedDetail.commitChanges();
        calculate();
        ohlHandler.syncronizeLineHeader();
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

    private void gridEvents() {
        itemDetailGridEdit.addCallBack(new PurchaseBillGridEdit.CallBack() {

            @Override
            public void onOk(GrdItemDetail_Mdl opdService) {
                gridMedDetail.commitChanges();
                calculate();
                ohlHandler.syncronizeLineHeader();
            }

            @Override
            public void onDelete() {
                gridMedDetail.commitChanges();
                calculate();
                ohlHandler.syncronizeLineHeader();
            }
        });

        gridMedDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showQuotationEntryGridEdit(event.getRowIndex());
            }
        });

        gridMedDetail.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showQuotationEntryGridEdit(gridMedDetail.getGrid().getStore().indexOf(gridMedDetail.getGrid().getSelectionModel().getSelectedItem()));
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

    private void showQuotationEntryGridEdit(int currentRowIndex) {
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
        if (DmShared.xNull(txtHCalculateOn.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please select Department", txtHCalculateOn).show();
            return;
        }
        if ((DmShared.convertDate(txtVoucherDate.getText()).getTime() < (DmShared.convertDate(txtSupplierBillDate.getText()).getTime()))) {
            new DmAlertMessageBox("Voucher Date should be greater than or equal to Ref. Date.", txtVoucherDate).show();
            return;
        }
        String status = gridMedDetail.getGrid().getStore().get(currentRowIndex).getIsStatus();
        String billedQty = gridMedDetail.getGrid().getStore().get(currentRowIndex).getReturnedQty();
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
            itemDetailGridEdit.show(currentRowIndex, false, false, false, this);
        } else {
            itemDetailGridEdit.show(currentRowIndex, true, true, true, this);
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
                new DmAlertMessageBox("Please enter Ref. Date.", txtSupplierBillDate).show();
            } else {
                grnSearch.show(
                        topControl.getMode().equalsIgnoreCase("New") ? "" : topControl.getDocId(),
                        txtHSupplier.getTag(),
                        txtSupplierBillDate.getText(), singlePOFlowInPurchase);
            }
        } else if (event.getSource().equals(labelCashDiscount)) {
            if (!topControl.getMode().equalsIgnoreCase("Browse")) {
                if (pharmacyDiscountPopup_Mdl == null) {
                    pharmacyDiscountPopup_Mdl = new PharmacyDiscountPopup_Mdl();
                }
                pharmacyDiscountPopup.show(pharmacyDiscountPopup_Mdl, DmShared.dNull(txtVoucherDate.getText()), DmShared.xNull(txtHVoucherType.getTag()));
            }
        } else if (event.getSource().equals(labelLineDiscount)) {

            double totalGrossAmount = 0d;
            for (GrdItemDetail_Mdl node : gridMedDetail.getGrid().getStore().getAll()) {
                totalGrossAmount += DmShared.vNull(node.getAmount());
            }

            if (!topControl.getMode().equalsIgnoreCase("Browse")) {
                if (pharmacyDiscountLineWise_Mdl == null) {
                    pharmacyDiscountLineWise_Mdl = new PharmacyDiscountLineWise_Mdl();
                }

                pharmacyDiscountLineWise_Mdl.setTotalGrossAmount(DmClient.vNull(totalGrossAmount, "0.00"));

                pharmacyDiscountLineWise.show(pharmacyDiscountLineWise_Mdl, DmShared.dNull(txtVoucherDate.getText()), DmShared.xNull(txtHVoucherType.getTag()));
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

    public PurchaseBill(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        srvAsync = new ServiceInvoker<PurchaseBill_SrvAsync>(GWT.create(PurchaseBill_Srv.class), PurchaseBill.class.getName());
        this.asWidget().setTitle(widgetName);
        walkThrough = new WalkThrough(srvAsync);
        topControlEvents(menuPermission);
        manageEvent();
        textHelpEvents();
        findEvents();
        gridEvents();
        tdsCategoryPpEvents();
        overHead.setBannerVisible(true);
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
            ohlHandler.syncronizeLineHeader();
            defaultValue.setNewVoucherTypeCode(txtHVoucherType.getTag());
            defaultValue.setNewVoucherTypeName(txtHVoucherType.getText());
            defaultValue.setNewVoucherTypeCategory(voucherCategory);
            defaultValue.setNewVoucherDate(txtVoucherDate.getText());
            defaultValue.setNewGuiTemplateCode(guiTemplateCode);
            ///*/*/*/*/*/*/*/*/*/*/*/TDS recalculation*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*
            saveModeActive = true;
            ///*/*/*/*/*/*/*/*/*/*/*/End TDS recalculation/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*
            calculate();
            mrTemp = topControlSaveInitialize();
            PurchaseBill_Vld.chkSave(mrTemp);
            OverHead_Shared.chkGrid(mrTemp.getOverHeadList());
            OverHeadLine_Shared.chkGrid(mrTemp.getListItemDetail());

            srvAsync.invokeRPC().saveRecord(mrTemp, DmClient.getVoucherPrefix(), isApprove, new AsyncCallback<DmResult<String>>() {
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
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getErrorSource(), e.getErrorSourceCode(), e.getMessage());
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
        mrTemp.setWtaCode(DmShared.iNull(wtaCode).toString());

        mrTemp.setSupplierStateCode(DmShared.xNull(txtHSupplier.getTag2nd()));
        mrTemp.setSupplierTag(DmShared.xNull(txtHSupplier.getTag()));
        mrTemp.setSupplierBillNo(DmShared.xNull(txtSupplierBillNo.getText()));
        mrTemp.setSupplierBillDate(DmShared.dNull(txtSupplierBillDate.getText()));
        mrTemp.setCalculateOnTag(DmShared.xNull(txtHCalculateOn.getTag()));
        mrTemp.setCalculateOnText(DmShared.xNull(txtHCalculateOn.getText()));
        mrTemp.setRemark(DmShared.xNull(txtRemark.getText()));
        mrTemp.setTdsAmt(DmShared.xNull(labelTDSAmount.getText()));
        mrTemp.setPlaceOfSupply(DmShared.xNull(defaultValue.getSiteStateCode()).trim());
        mrTemp.setRcmApplicable(rcmApplicable);
        mrTemp.setPartyTypeForEntry(partyTypeForEntry);
        mrTemp.setPartyType(partyType);
        mrTemp.setDefaultValueMdl(defaultValue);
        mrTemp.setSupplierGstin(DmShared.xNull(supplierGstin));
        mrTemp.setSupplierGstinDate(DmShared.xNull(supplierGstinDate));

        OverHead_Mdl ohMdl = OverHead_Shared.getOHByUniqueId("RO", overHead.getOverHeadList());
        mrTemp.setRoundOffAmount(ohMdl != null ? DmShared.vNull(ohMdl.getAmount()).toString() : "0");

        OverHead_Mdl ohMdlNa = OverHead_Shared.getOHByUniqueId("PT", overHead.getOverHeadList());
        mrTemp.setNetAmount(ohMdl != null ? DmShared.vNull(ohMdlNa.getAmount()).toString() : "0");

        mrTemp.getListItemDetail().clear();
        mrTemp.getListItemDetail().addAll(gridMedDetail.getGrid().getStore().getAll());

        mrTemp.setGuiMdl(gui.getGUI_Mdl());
        mrTemp.setGuiConfigCode(gui.getConfigCode());

        mrTemp.getOverHeadList().clear();
        mrTemp.getOverHeadList().addAll(overHead.getOverHeadList());

        if (DmShared.vNull(labelTDSAmount.getText()).doubleValue() != 0.0) {
            tdsCategoryPp.onOK();
            mrTemp.setTdsCatMdl(tdsCategoryPopup_Mdl);
        } else {
            mrTemp.setTdsCatMdl(null);
        }

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
                    List<GrdItemDetail_Mdl> listTemp = gridMedDetail.getGrid().getStore().getAll();
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
                        ((CellSelectionModel) gridMedDetail.getGrid().getSelectionModel()).selectCell(DmShared.iNull(gridRowIndex).intValue(), 1);
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
        ohlHandler.setDefault();
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
            tdsCategoryPopup_Mdl = result.getData().getTdsCatMdl();
            tdsCategoryPp.moveRec(tdsCategoryPopup_Mdl);
            topControl.setDocId(mrTemp.getSharedKeyFieldCode());
            txtVoucherNo.setText(mrTemp.getSharedKeyFieldName());
            txtVoucherNo.setTag(mrTemp.getU_EntDt_LatestLine());
            txtVoucherDate.setText(mrTemp.getVoucherDate());
            txtVoucherDate.setTag(mrTemp.getVoucherDate());
            txtPaymentDueDate.setText(mrTemp.getPaymentDueDate());
            txtHVoucherType.setTag2nd(mrTemp.getVoucherTypeCode());
            txtHVoucherType.setTag(mrTemp.getVoucherTypeCode());
            txtHVoucherType.setText(mrTemp.getVoucherTypeName());
            voucherCategory = mrTemp.getVoucherCategory();
            wtaCode = mrTemp.getWtaCode();
            txtHSupplier.setTag2nd(mrTemp.getSupplierStateCode());
            txtHSupplier.setTag(mrTemp.getSupplierTag());
            txtHSupplier.setText(mrTemp.getSupplierText());
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
            lastTransactionDate = mrTemp.getLastTransactionDate();

            userControl.setTextBlog(preparedByBlog, mrTemp.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, mrTemp.getSharedModifiedBy());
            userControl.setTextBlog(approvedByBlog, mrTemp.getApprovedBy());

            gridMedDetail.getGrid().getStore().clear();
            gridMedDetail.getGrid().getStore().addAll(mrTemp.getListItemDetail());

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

    private void fillGrnDetail(List<KeyFieldExtended> listKF) {
        srvAsync.invokeRPC().getItemDetail(
                listKF,
                txtHSupplier.getTag(),
                location,
                txtSupplierBillDate.getText(),
                topControl.getMode().equalsIgnoreCase("New") ? "" : topControl.getDocId(),
                txtVoucherDate.getText(), singlePOFlowInPurchase,
                new AsyncCallback<DmResult<List<GrdItemDetail_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdItemDetail_Mdl>> result) {
                if (result.isStatus()) {
                    gridMedDetail.getGrid().getStore().clear();
                    gridMedDetail.getGrid().getStore().addAll(result.getData());
                    gridMedDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
                    if (result.getData() != null) {
                        fillMedicineDetail(result.getData());
                    }
                    txtSupplierBillNo.setFocus(true);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void fillMedicineDetail(List<GrdItemDetail_Mdl> listGRN) {
        for (int i = 0; i < listGRN.size(); i++) {
            GrdItemDetail_Mdl detail_Mdl = listGRN.get(i);
            if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() > 0) {
                gridMedDetail.getGrid().getStore().get(i).setAmount(DmClient.vNull(DmShared.vNull(gridMedDetail.getGrid().getStore().get(i).getAcceptQty()).doubleValue() * DmShared.vNull(gridMedDetail.getGrid().getStore().get(i).getRate()).doubleValue(), "0.00"));
                ohlHandler.setReference(detail_Mdl.getOHLListGRN(), ohlHandler.getOverHeadList(i));
                ohlHandler.setITS(gridMedDetail.getGrid().getStore().get(i).getLstITS(), i);
                ohlHandler.calculate(gridMedDetail.getGrid().getStore().get(i).getAmount(), i);
                gridMedDetail.getGrid().getStore().get(i).setOHLList(ohlHandler.getOverHeadList(i));

                String grossAcCode = DmShared.xNull(gridMedDetail.getGrid().getStore().get(i).getGrossAcCode());
                String grossAcName = DmShared.xNull(gridMedDetail.getGrid().getStore().get(i).getGrossAcName());

                // Setting account to one GS + multiple [GS] unique ids
                if (!defaultValue.isInventoryAccountingActive()) {
                    if (!grossAcCode.trim().equals("") && !grossAcName.trim().equals("")) {
                        List<OverHeadLine_Mdl> lstOHLMdl = OverHeadLine_Shared.getOHByUniqueIds("GS", gridMedDetail.getGrid().getStore().get(i).getOHLList());
                        for (OverHeadLine_Mdl ohlm : lstOHLMdl) {
                            if (ohlm != null) {
                                ohlm.setAccountCode(grossAcCode);
                                ohlm.setAccountName(grossAcName);
                            }
                        }
                    }
                } else {
                    List<OverHeadLine_Mdl> lstOHLMdl = OverHeadLine_Shared.getOHByUniqueIds("IV", gridMedDetail.getGrid().getStore().get(i).getOHLList());
                    for (OverHeadLine_Mdl ohlm : lstOHLMdl) {
                        if (ohlm != null) {
                            ohlm.setAccountCode(defaultValue.getBalancingAc());
                            ohlm.setAccountName(defaultValue.getBalancingAcName());
                        }
                    }
                    ohlHandler.calculate(gridMedDetail.getGrid().getStore().get(i).getAmount(), i);
                }
            }
        }

        gridMedDetail.commitChanges();

        txtHCalculateOn.setTag(
                "1");
        txtHCalculateOn.setText(
                "Accept Quantity");

        calculate();
    }

    private void manageEvent() {
        flwForm.addAttachHandler(this);
        btnApprovedBy.addClickHandler(this);
        labelLineDiscount.addClickHandler(this);
        btnAddGRN.addClickHandler(this);
        btnAdvDetails.addClickHandler(this);
        btnBillHistory.addClickHandler(this);
        btnPaymDetails.addClickHandler(this);
        txtVoucherDate.addBlurHandler(this);
        txtPaymentDueDate.addBlurHandler(this);
        txtSupplierBillDate.addBlurHandler(this);
        labelCashDiscount.addClickHandler(this);
        labelTDS.addClickHandler(this);
        mnuManageFile.addSelectionHandler(this);
    }

    private void manageDisplay(boolean flag) {
        txtVoucherNo.setEnabled(false);

        flwAddGRN.clear();
        if (!topControl.getMode().equalsIgnoreCase("Browse")) {
            flwAddGRN.add(btnAddGRN);
        }

        if (topControl.getMode().equalsIgnoreCase("Edit")) {
            txtHVoucherType.setEnabled(false);
            txtHSupplier.setEnabled(false);
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

    private void setDefaultValues() {
        txtVoucherDate.setText(defaultValue.getIsContinuousNewMode() ? DmShared.dNull(defaultValue.getNewVoucherDate()) : DmShared.dNull(defaultValue.getCurrentDate()));
        txtVoucherDate.setTag(txtVoucherDate.getText());
        txtHVoucherType.setTag2nd(defaultValue.getIsContinuousNewMode() ? defaultValue.getNewVoucherTypeCode() : defaultValue.getVoucherTypeCode());
        txtHVoucherType.setTag(defaultValue.getIsContinuousNewMode() ? defaultValue.getNewVoucherTypeCode() : defaultValue.getVoucherTypeCode());
        txtHVoucherType.setText(defaultValue.getIsContinuousNewMode() ? defaultValue.getNewVoucherTypeName() : defaultValue.getVoucherTypeName());
        voucherCategory = defaultValue.getIsContinuousNewMode() ? defaultValue.getNewVoucherTypeCategory() : defaultValue.getVoucherCategory();
        guiTemplateCode = defaultValue.getIsContinuousNewMode() ? defaultValue.getNewGuiTemplateCode() : defaultValue.getGuiTemplateCode();
        singlePOFlowInPurchase = defaultValue.isSinglePOFlowInPurchase();
        txtHCalculateOn.setTag("1");
        txtHCalculateOn.setText("Accept Quantity");
        gridMedDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
        ohlHandler.initialize();
        gui.initializeViaTemplateCode();
        txtHSupplier.setFocus(true);
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
        previous_Mdl = null;
        overHead.clear();
        gui.clear();
        flwAddGRN.clear();

        gridMedDetail.getGrid().getStore().clear();
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        userControl.setTextBlog(approvedByBlog, "");
        tdsCategoryPp.clear();
        tdsCategoryPopup_Mdl = null;
        labelTDSAmount.setText("0.0");
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

    private void calculateGrid() {
        for (int i = 0; i < gridMedDetail.getGrid().getStore().getAll().size(); i++) {
            GrdItemDetail_Mdl detail_Mdl = gridMedDetail.getGrid().getStore().get(i);
            if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() != 0) {
                if (DmShared.iNull(txtHCalculateOn.getTag()).intValue() == 0) {
                    detail_Mdl.setAmount(DmClient.vNull(DmShared.vNull(detail_Mdl.getDocQty()).doubleValue() * DmShared.vNull(detail_Mdl.getRate()).doubleValue(), "0.00"));
                } else {
                    detail_Mdl.setAmount(DmClient.vNull(DmShared.vNull(detail_Mdl.getAcceptQty()).doubleValue() * DmShared.vNull(detail_Mdl.getRate()).doubleValue(), "0.00"));
                }
                gridMedDetail.getGrid().getStore().get(i).setAmount(DmClient.vNull(detail_Mdl.getAmount(), "0.00"));
                ohlHandler.calculate(gridMedDetail.getGrid().getStore().get(i).getAmount(), i);
                gridMedDetail.getGrid().getStore().get(i).setOHLList(ohlHandler.getOverHeadList(i));
                gridMedDetail.commitChanges();
            }
        }
        calculate();
    }

    private void calculate() {
        gridMedDetail.getGrid().getStore().commitChanges();
        double netAmount = 0;
        for (GrdItemDetail_Mdl detail_Mdl : gridMedDetail.getGrid().getStore().getAll()) {
            if (!DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                netAmount += DmShared.vNull(detail_Mdl.getAmount()).doubleValue();
            }
        }
        gridMedDetail.commitChanges();
        overHead.calculate(DmClient.vNull(netAmount, "0.00"));
        resetTaxSummary();
        setTDSCatPpFields();
    }

    private void resetTaxSummary() {
        List<List<OverHeadLine_Mdl>> ohlList = new ArrayList<List<OverHeadLine_Mdl>>();
        for (GrdItemDetail_Mdl grdMdl : gridMedDetail.getGrid().getStore().getAll()) {
            if (grdMdl != null && grdMdl.getOHLList() != null) {
                ohlList.add(grdMdl.getOHLList());
            }
        }
        taxSummary.reset(ohlList);
    }

    private void prnRecord(boolean printToPrinter) {
        HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
        hashMapFilter.put("docId", new FieldUDT_Mdl("Voucher No.", DmShared.xNull(txtVoucherNo.getText()), DmShared.xNull(topControl.getDocId())));
        new OpenSourceReport(txtHVoucherType.getTag(), "", widgetName, widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
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
                    mrTemp.setSupplierTag(DmShared.xNull(txtHSupplier.getTag()));

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
                                    btnAddGRN.setVisible(false);
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

    private void setTDSCatPpFields() {
        if (saveModeActive) {
            saveModeActive = false;
            return;
        }
        List<OverHead_Mdl> lstOHMdl;
        if (DmShared.iNull(txtHSupplier.getTag()).longValue() > 0) {
            if (tdsCategoryPopup_Mdl == null) {
                tdsCategoryPopup_Mdl = new TdsCategoryPopup_Mdl();
            }

            tdsCategoryPopup_Mdl.setTdsDeductFormCode(txtHSupplier.getTag());
            tdsCategoryPopup_Mdl.setTdsDeductFormName(txtHSupplier.getText());
            tdsCategoryPopup_Mdl.setvCategory(voucherCategory);
            if (DmShared.xNull(topControl.getMode()).equalsIgnoreCase("New")) {
                tdsCategoryPopup_Mdl.setTrnDocID("");
            } else {
                tdsCategoryPopup_Mdl.setTrnDocID(topControl.getDocId());
            }
            if (defaultValue.getTdsOn().trim().equalsIgnoreCase("0")) {
                lstOHMdl = OverHead_Shared.getOHByUniqueIds("PT", overHead.getOverHeadList());
            } else {
                lstOHMdl = OverHead_Shared.getOHByUniqueIds("TD", overHead.getOverHeadList());
            }

            for (OverHead_Mdl ohMdl : lstOHMdl) {
                if (ohMdl != null) {
                    tdsCategoryPopup_Mdl.setTdsOnAmount(ohMdl.getAmount());
                }
            }
            lstOHMdl = OverHead_Shared.getOHByUniqueIds("PT", overHead.getOverHeadList());
            for (OverHead_Mdl ohMdl : lstOHMdl) {
                if (ohMdl != null) {
                    tdsCategoryPopup_Mdl.setOrignalAmount(ohMdl.getAmount());
                }
            }
            tdsCategoryPopup_Mdl.setVoucherDate(DmShared.dNull(txtVoucherDate.getText()));
            tdsCategoryPopup_Mdl.setPoDocID(DmShared.xNull(itemDetailGridEdit.getDmGrid().getGrid().getStore().get(0).getPoDocId()).trim());
        }

        tdsCategoryPp.setPpFields(tdsCategoryPopup_Mdl);
    }

}
