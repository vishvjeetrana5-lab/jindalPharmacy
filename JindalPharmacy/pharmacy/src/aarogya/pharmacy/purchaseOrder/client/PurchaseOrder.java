package aarogya.pharmacy.purchaseOrder.client;

import aarogya.pharmacy.purchaseOrder.shared.*;
import aarogya.pharmacy.indentSearch.client.IndentSearch;
import com.google.gwt.core.client.GWT;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import dataman.dmToolkit.overHead.client.OverHead;
import dataman.dmToolkit.overHead.shared.OverHeadMA_Mdl;
import dataman.dmToolkit.overHead.shared.OverHead_Mdl;
import dataman.dmToolkit.overHead.shared.OverHead_Shared;
import dataman.dmToolkit.overHeadLine.client.OverHeadLineHandler;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Shared;
import java.util.ArrayList;
import java.util.List;
import aarogya.pharmacy.purchaseOrder.shared.PurchaseOrder_Srv;
import aarogya.pharmacy.purchaseOrder.shared.PurchaseOrder_SrvAsync;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.modifyReason.client.ModifyReason;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.stStatic.client.STClient;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmGUI.client.DmGUI;
import dataman.dmToolkit.dmGUI.shared.DmGUIControl_Mdl;
import dataman.dmToolkit.dmGUI.shared.DmGUI_Mdl;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Mdl;
import dataman.dmToolkit.walkThrough.client.WalkThrough;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class PurchaseOrder extends PurchaseOrder_Dsg implements BlurHandler, AttachEvent.Handler, ClickHandler, DmRootEvents {

    ServiceInvoker<PurchaseOrder_SrvAsync> SrvAsync;
    protected DefaultValue_Mdl defaultValue;
    final IndentSearch indentSearch = new IndentSearch();
    protected final OverHeadLineHandler<GrdIndentDetail_Mdl> ohlHandler = new OverHeadLineHandler(overHead, 300, 125);
    final PurchaseOrderGridEdit<GrdIndentDetail_Mdl, Grd_PrpAcs> itemDetailGridEdit = new PurchaseOrderGridEdit(grdItemDetail);
    private final WalkThrough walkThrough;
    final ModifyReason modifyReason = new ModifyReason();
    private ModifyReason_Mdl modifyReasonMdl;
    private String guiTemplateCode = "", selectedVoucherType = "", selectedQuotationNo = "", indentRequired = "";
    String selectedSupplierCode, partyTypeForEntry, partyType, supplierGstin, supplierGstinDate, location;
    String rcmApplicable, wtaCode, voucherCategory, balancingAc, balancingAcName, preparedBy = "";
    private RecId recId = new RecId();
    private boolean isApprove = false, IsUnApprove = false;

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);
        for (String controlPermission : menuPermission.getControlPermission()) {
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(approvedByBlog)) {
                isApprove = true;
            }
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(unApprovedByBlog)) {
                IsUnApprove = true;
            }
        }

        overHead.addCallback(new OverHead.Callback() {

            @Override
            public void initialized() {
                calculate();
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

        ohlHandler.addCallback(new OverHeadLineHandler.Callback<GrdMedicineDetail_Mdl>() {

            @Override
            public void initialized() {
                grdItemDetail.getGrid().getStore().clear();
                grdItemDetail.getGrid().getStore().add(new GrdMedicineDetail_Mdl());
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
            public List<GrdMedicineDetail_Mdl> getRows() {
                return grdItemDetail.getGrid().getStore().getAll();
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
                if (gui_Mdl != null) {
                    gui.initialize(gui_Mdl);
                }
            }
        });

        topControl.setUserPermission(menuPermission, new VoucherPermissionHandler(DmClient.getVoucherPermission(new String[]{new ProjectVoucherCategory().PHARMACY_PURCHASE_ORDER})) {
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
                    if (defaultValue.getIsSystemAdminUser().equalsIgnoreCase("1")) {
                        manageDisplay(true);
                        grdItemDetail.getGrid().getStore().add(new GrdMedicineDetail_Mdl());
                        txtHSupplier.setFocus(true);
                    } else if (DmShared.xNull(preparedBy).equalsIgnoreCase(defaultValue.getUserName())) {
                        manageDisplay(true);
                        grdItemDetail.getGrid().getStore().add(new GrdMedicineDetail_Mdl());
                        txtHSupplier.setFocus(true);
                    } else {
                        topControl.btnClick("");
                        new DmAlertMessageBox("You do not have permission to modify this record.", (DmTextBox) null).show();
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
            public void moveRec(String strFetchCode) {
                topControlMoveRec(strFetchCode);
            }
        });

        topControl.addPrintHandeller(new DmTopControl.ePrint() {

            @Override
            public void printClicked() {
                printRecord(false);
            }
        });

        topControl.addSaveHandeller(new DmTopControl.eSave() {
            @Override
            public void saveClicked() {
                saveValidation();
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(PurchaseOrder.this);
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
        indentSearch.addCallBack(new IndentSearch.CallBack() {
            @Override
            public void ok(List<KeyFieldExtended> listKF) {
                reset(false, false);
                fillItemDetails(listKF, null, null, false);
            }

            @Override
            public void close() {
                txtHSupplier.setFocus(true);
            }
        });

        txtHQuotationNo.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHQuotationNo_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                selectedQuotationNo = DmShared.xNull(txtHQuotationNo.getTag());
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHQuotationNo(cbShowUsedQuotationsAlso.getValue(), txtHSupplier.getTag(), !DmShared.dNull(txtRefDate.getText()).trim().equals("") ? txtRefDate.getText() : txtVoucherDate.getText(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHQuotationNo_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHQuotationNo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHQuotationNo_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHQuotationNo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHQuotationNo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHQuotationNo_Mdl tRow) {
                if (!DmShared.xNull(selectedQuotationNo).equalsIgnoreCase(DmShared.xNull(txtHQuotationNo.getTag()))) {
                    reset(true, false);

                    if (blnStatus && tRow != null) {
                        List<KeyFieldExtended> fields = new ArrayList<>();
                        txtHQuotationNo.setTag2nd(tRow.getQuotationDate());
                        txtEnquiryNo.setText(tRow.getEnquiryRefNo());
                        txtEnquiryNo.setTag(tRow.getEnquiryDocId());
                        if (!DmShared.xNull(tRow.getGuiConfigCode()).equalsIgnoreCase("")) {
                            fillItemDetails(fields, tRow.getGuiConfigCode(), tRow.getSharedKeyFieldCode(), true);
                        }
                    }
                }
            }
        });

        txtHVoucherType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                selectedVoucherType = DmShared.xNull(txtHVoucherType.getTag());
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHVoucherType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
                if (!DmShared.xNull(selectedVoucherType).equalsIgnoreCase(DmShared.xNull(txtHVoucherType.getTag()))) {
                    reset(false, true);

                    if (blnStatus && t1Row != null) {
                        guiTemplateCode = t1Row.getSharedPreparedBy();
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
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSupplierPT(false, txtHVoucherType.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHSupplier_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHQuotationNo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHSupplier_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHSupplier.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHQuotationNo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHSupplier_Mdl tRow) {
                if (topControl.getMode().equalsIgnoreCase("New")
                        && !DmShared.xNull(selectedSupplierCode).equalsIgnoreCase(DmShared.xNull(txtHSupplier.getTag()))) {
                    selectedSupplierCode = DmShared.xNull(txtHSupplier.getTag());
                    ohlHandler.initialize();
                }
                if (blnStatus && tRow != null) {
                    if (!DmShared.xNull(txtHSupplier.getTag()).equalsIgnoreCase(txtHSupplier.getTag2nd())) {
                        txtHSupplier.setTag2nd(DmShared.xNull(tRow.getStateCode()));
                        supplierGstin = DmShared.xNull(tRow.getGstin());
                        supplierGstinDate = DmShared.dNull(tRow.getGstinDate());
                        partyType = DmShared.xNull(tRow.getPartyType());
                        setCardAcInOH();
                        reset(false, false);
                        if (!DmShared.xNull(tRow.getGuiConfigCode()).equalsIgnoreCase("")) {
                            copyGUI(tRow.getGuiConfigCode(), tRow.getSharedKeyFieldCode());
                        }
                        resetGstDetails();
                    }
                }
            }
        });
    }

    private void reset(boolean isQuotation, boolean isOhlInitialize) {
        if (!isQuotation) {
            txtHQuotationNo.setTag("");
            txtHQuotationNo.setTag2nd("");
            txtHQuotationNo.setText("");
        }
        txtEnquiryNo.setTag2nd("");
        txtEnquiryNo.setTag("");
        txtEnquiryNo.setText("");

        if (isOhlInitialize) {
            ohlHandler.initialize();
        } else {
            grdItemDetail.getGrid().getStore().clear();
            grdItemDetail.getGrid().getStore().add(new GrdMedicineDetail_Mdl());
            grdItemDetail.commitChanges();
            calculate();
        }
    }

    private void fillItemDetails(List<KeyFieldExtended> keyField, final String guiCodeFrom, final String copyFromDocId, final boolean isGUICopyRequired) {
        SrvAsync.invokeRPC().getItemDetail(DmShared.xNull(txtHQuotationNo.getTag()), keyField, txtHSupplier.getTag(), location, txtVoucherDate.getText(), txtVoucherDate.getText(), new AsyncCallback<DmResult<PurchaseOrder_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<PurchaseOrder_Mdl> result) {
                if (result.isStatus()) {
                    PurchaseOrder_Mdl entry_Mdl = result.getData();
                    List<GrdMedicineDetail_Mdl> grdIndentDetail_Mdl = entry_Mdl.getGrdItemDetail();
                    grdItemDetail.getGrid().getStore().clear();
                    grdItemDetail.getGrid().getStore().addAll(grdIndentDetail_Mdl);
                    grdItemDetail.getGrid().getStore().add(new GrdMedicineDetail_Mdl());
                    fillGrdItemDetail(grdIndentDetail_Mdl);
                    if (isGUICopyRequired) {
                        copyGUI(guiCodeFrom, copyFromDocId);
                    }
                    txtHQuotationNo.setFocus(true);
                }
            }
        });
    }

    private void copyGUI(String guiCodeFrom, final String copyFromDocId) {
        if (DmShared.iNull(guiCodeFrom).longValue() != 0) {
            gui.copyGUI(guiCodeFrom, copyFromDocId, null);
        }
    }

    private void fillGrdItemDetail(List<GrdMedicineDetail_Mdl> listGRN) {
        for (int i = 0; i < listGRN.size(); i++) {
            GrdMedicineDetail_Mdl detail_Mdl = listGRN.get(i);
            if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() > 0) {
                grdItemDetail.getGrid().getStore().get(i).setAmount(DmClient.vNull(DmShared.vNull(grdItemDetail.getGrid().getStore().get(i).getOrderQuantity()).doubleValue() * DmShared.vNull(grdItemDetail.getGrid().getStore().get(i).getRate()).doubleValue(), "0.00"));
                if (!detail_Mdl.getQuotationDocId().equalsIgnoreCase("")) {
                    ohlHandler.setReference(detail_Mdl.getOHLListQuotation(), ohlHandler.getOverHeadList(i));
                } else {
                    OverHeadLine_Mdl ohlDiscount = OverHeadLine_Shared.getOHByUniqueId("DS", ohlHandler.getOverHeadList(i));
                    if (!defaultValue.getRequiredLastBillDiscountInPO().trim().equalsIgnoreCase("0")) {
                        ohlDiscount.setAtRate(DmClient.vNull(detail_Mdl.gretlastPurchaseDiscount(), "0.00"));
                    }
                    ohlDiscount.setAmount("");
                    List<OverHeadLine_Mdl> lstOhl = new ArrayList<>();
                    lstOhl.add(ohlDiscount);
                    ohlHandler.setReference(detail_Mdl.getOHLListQuotation(), ohlHandler.getOverHeadList(i));
                }
                ohlHandler.setITS(grdItemDetail.getGrid().getStore().get(i).getLstITS(), i);
                ohlHandler.calculate(grdItemDetail.getGrid().getStore().get(i).getAmount(), i);
                grdItemDetail.getGrid().getStore().get(i).setOHLList(ohlHandler.getOverHeadList(i));
            }
        }
        grdItemDetail.commitChanges();
        calculate();
        getCDdetailForIndent();
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
        itemDetailGridEdit.addCallBack(new PurchaseOrderGridEdit.CallBack() {

            @Override
            public void onOk(GrdMedicineDetail_Mdl opdService) {
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
                showPurchaseOrderEntryGridEdit(event.getRowIndex());
            }
        });

        grdItemDetail.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    showPurchaseOrderEntryGridEdit(grdItemDetail.getGrid().getStore().indexOf(grdItemDetail.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
    }

    private void showPurchaseOrderEntryGridEdit(int currentRowIndex) {
        if (DmShared.xNull(txtHVoucherType.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please select PO Type.", txtHVoucherType).show();
            return;
        }
        if (DmShared.xNull(txtHSupplier.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please select Supplier.", txtHSupplier).show();
            return;
        }
        if (DmShared.xNull(txtVoucherDate.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please fiil  Date.", txtVoucherDate).show();
            return;
        }

        String status = grdItemDetail.getGrid().getStore().get(currentRowIndex).getIsStatus();
        String challanedQty = grdItemDetail.getGrid().getStore().get(currentRowIndex).getChallanQyt();
        String mode = topControl.getMode();
        String msg = "";
        if (DmShared.iNull(status).intValue() == 1) {
            mode = "Browse";
            msg = "Record has been canceled, can't change record.";
        } else if (DmShared.iNull(status).intValue() == 2) {
            mode = "Browse";
            msg = "Record has been completed, can't change record.";
        } else if (DmShared.vNull(challanedQty) > 0) {
            mode = "Browse";
            msg = "Record has been used, can't change record.";
        }
        if (mode.trim().equalsIgnoreCase("Browse")) {
            if (!topControl.getMode().equalsIgnoreCase("Browse") && !msg.equalsIgnoreCase("")) {
                new DmAlertMessageBox(msg, (DmTextBox) null).show();
            }
            itemDetailGridEdit.show(currentRowIndex, false, false, false, this, mode, indentRequired);
        } else {
            itemDetailGridEdit.show(currentRowIndex, true, true, true, this, mode, indentRequired);
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
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtVoucherDate) {
            txtVoucherDate.setText(DmClient.returnDate(txtVoucherDate.getText()));
            if (STClient.isFutureDate(txtVoucherDate.getText())) {
                txtVoucherDate.setText("");
                new DmAlertMessageBox("PO Date should be less than or equal to current date.", txtVoucherDate).show();
            }
        } else if (event.getSource() == txtRefDate) {
            txtRefDate.setText(DmClient.returnDate(txtRefDate.getText()));
        }
    }

    public PurchaseOrder(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(PurchaseOrder_Srv.class), PurchaseOrder.class.getName());
        this.asWidget().setTitle(widgetName);
        walkThrough = new WalkThrough(SrvAsync);
        txtVoucherDate.addBlurHandler(this);
        txtRefDate.addBlurHandler(this);
        btnApprovedBy.addClickHandler(this);
        flwForm.addAttachHandler(this);
        topControlEvents(menuPermission);
        findEvents();
        textHelpEvents();
        gridEvents();
        showIndentSearch();
        overHead.setBannerVisible(true);
        iniDefaultValues(swi);
        gui.setBannerVisible(false);
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
                    taxSummary.initialize(defaultValue.isGstApplicable());
                    swi.setWidget(PurchaseOrder.this);
                    gui.initializeViaTemplateCode();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void topControlSave() {
        PurchaseOrder_Mdl mrTemp;

        try {
            ohlHandler.syncronizeLineHeader();
            defaultValue.setNewVoucherTypeCode(txtHVoucherType.getTag());
            defaultValue.setNewVoucherTypeName(txtHVoucherType.getText());
            defaultValue.setNewVoucherDate(txtVoucherDate.getText());
            defaultValue.setNewGuiTemplateCode(guiTemplateCode);
            calculate();
            mrTemp = chkValidation();
            OverHead_Shared.chkGrid(mrTemp.getOverHeadList());
            OverHeadLine_Shared.chkGrid(mrTemp.getGrdItemDetail());
            SrvAsync.invokeRPC().saveRecord(mrTemp, DmClient.getVoucherPrefix(), isApprove, new AsyncCallback<DmResult<String>>() {
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

    private void showModifyReason() {
        modifyReason.show(topControl.getMode(), topControl.getDocId());
        modifyReason.addCallBack(new ModifyReason.CallBack() {

            @Override
            public void onOk(ModifyReason_Mdl modifyReason) {
                PurchaseOrder.this.modifyReasonMdl = modifyReason;
                if (topControl.getMode().equalsIgnoreCase("Browse") && DmShared.iNull(topControl.getDocId()).longValue() > 0) {
                    topControlDelete(topControl.getDocId());
                } else {
                    topControlSave();
                }

            }
        });
    }

    public void saveModifyReason() {
        if (topControl.getMode().equalsIgnoreCase("New")) {
            topControlSave();
        } else {
            showModifyReason();
        }

    }

    private void saveValidation() {
        try {
            chkValidation();
            saveModifyReason();
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getErrorSource(), e.getErrorSourceCode(), e.getMessage());
        }
    }

    private PurchaseOrder_Mdl chkValidation() {
        PurchaseOrder_Mdl mrTemp;
        mrTemp = topControlSaveInitialize();
        PurchaseOrder_Vld.chkSave(mrTemp, DmClient.getLoginDate());
        return mrTemp;
    }

    private PurchaseOrder_Mdl topControlSaveInitialize() {
        PurchaseOrder_Mdl mrTemp = new PurchaseOrder_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
            mrTemp.getRecIdPurchaseOrder().setCounter(recId.getCounter());
            mrTemp.getRecIdPurchaseOrder().setPrefix(recId.getPrefix());
            mrTemp.getRecIdPurchaseOrder().setRecIdValue(recId.getRecIdValue());
        }
        mrTemp.setU_EntDt_LatestLine(DmShared.xNull(txtOrderNo.getTag()));
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtOrderNo.getText()));
        mrTemp.setVoucherDate(DmShared.xNull(txtVoucherDate.getText()));
        mrTemp.setSupplierTag(DmShared.xNull(txtHSupplier.getTag()));
        mrTemp.setEnquiryTag(DmShared.xNull(txtEnquiryNo.getTag()));
        mrTemp.setEnquiryText(DmShared.xNull(txtEnquiryNo.getText()));
        mrTemp.setQuotationNoTag(DmShared.xNull(txtHQuotationNo.getTag()));
        mrTemp.setQuotationNoText(DmShared.xNull(txtHQuotationNo.getText()));
        mrTemp.setRefNo(DmShared.xNull(txtRefNo.getText()));
        mrTemp.setRefDate(DmShared.xNull(txtRefDate.getText()));
        mrTemp.setVoucherTypeTag(DmShared.xNull(txtHVoucherType.getTag()));
        mrTemp.setVoucherTypeText(DmShared.xNull(txtHVoucherType.getText()));
        mrTemp.setWtaCode(DmShared.xNull(txtWTACode.getText()));
        mrTemp.setIsShowUsedQuotationsAlso(cbShowUsedQuotationsAlso.getValue() ? "1" : "0");
        mrTemp.setGuiMdl(gui.getGUI_Mdl());
        mrTemp.setGuiConfigCode(gui.getConfigCode());
        mrTemp.getGrdItemDetail().clear();
        grdItemDetail.getGrid().getStore().commitChanges();
        mrTemp.getGrdItemDetail().addAll(grdItemDetail.getGrid().getStore().getAll());
        if (modifyReasonMdl != null) {
            mrTemp.setModifyReasonMdl(modifyReasonMdl);
        }

        mrTemp.getOverHeadList().clear();
        mrTemp.getOverHeadList().addAll(overHead.getOverHeadList());
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
                new DmAlertMessageBox(errorMessage, txtHQuotationNo).show();
                break;
            case 500:
                int indexTemp = DmShared.iNull(errorSourceCode).intValue();
                if (!DmShared.xNull(errorSource).equalsIgnoreCase("GridRow")) {
                    List<GrdMedicineDetail_Mdl> listTemp = grdItemDetail.getGrid().getStore().getAll();
                    for (GrdMedicineDetail_Mdl node : listTemp) {
                        for (GrdIndentDetail_Mdl idm : node.getListIndentDetail()) {
                            if (DmShared.xNull(idm.getSharedKeyFieldCode()).equalsIgnoreCase(errorSource)
                                    && DmShared.xNull(idm.getIndentVSNo()).equalsIgnoreCase(errorSourceCode)) {
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
                }
                final int gridRowIndex = indexTemp;
                new DmAlertMessageBox(errorMessage, new HideEvent.HideHandler() {
                    @Override
                    public void onHide(HideEvent event) {
                        ((CellSelectionModel) grdItemDetail.getGrid().getSelectionModel()).selectCell(gridRowIndex, 1);
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
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<PurchaseOrder_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<PurchaseOrder_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<PurchaseOrder_Mdl> result) {
        clear();
        if (result.isStatus()) {
            PurchaseOrder_Mdl mrTemp = result.getData();

            flwAddIndent.clear();
            topControl.setDocId(mrTemp.getSharedKeyFieldCode());
            recId = mrTemp.getRecIdPurchaseOrder();
            txtOrderNo.setText(mrTemp.getSharedKeyFieldName());
            txtOrderNo.setTag(DmShared.iNull(mrTemp.getU_EntDt_LatestLine()).toString());
            txtVoucherDate.setText(mrTemp.getVoucherDate());
            txtVoucherDate.setTag(mrTemp.getVoucherDate());
            txtHVoucherType.setTag(mrTemp.getVoucherTypeTag());
            txtHVoucherType.setText(mrTemp.getVoucherTypeText());
            txtHSupplier.setTag2nd(mrTemp.getSupplierStateCode());
            txtHSupplier.setTag(mrTemp.getSupplierTag());
            txtHSupplier.setText(mrTemp.getSupplierText());
            txtHQuotationNo.setTag2nd(mrTemp.getQuotationDate());
            txtHQuotationNo.setTag(mrTemp.getQuotationNoTag());
            txtHQuotationNo.setText(mrTemp.getQuotationNoText());
            txtEnquiryNo.setTag(mrTemp.getEnquiryTag());
            txtEnquiryNo.setText(mrTemp.getEnquiryText());

            txtRefDate.setText(mrTemp.getRefDate());
            txtRefNo.setText(mrTemp.getRefNo());

            txtWTACode.setText(mrTemp.getWtaCode());

            if (DmShared.xNull(mrTemp.getIsShowUsedQuotationsAlso()).equalsIgnoreCase("1")) {
                cbShowUsedQuotationsAlso.setValue(true);
            }

            userControl.setTextBlog(preparedByBlog, mrTemp.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, mrTemp.getSharedModifiedBy());
            userControl.setTextBlog(approvedByBlog, mrTemp.getApprovedBy());

            preparedBy = "";
            preparedBy = DmShared.xNull(mrTemp.getSharedPreparedBy());
            grdItemDetail.getGrid().getStore().clear();
            grdItemDetail.getGrid().getStore().addAll(mrTemp.getGrdItemDetail());

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

    private void calculate() {
        double netAmount = 0;
        for (GrdMedicineDetail_Mdl detail_Mdl : grdItemDetail.getGrid().getStore().getAll()) {
            if (!DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                netAmount += DmShared.vNull(detail_Mdl.getAmount()).doubleValue();
            }
        }
        grdItemDetail.commitChanges();
        overHead.calculate(DmClient.vNull(netAmount, "0.00"));
        resetTaxSummary();
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

    private void showIndentSearch() {
        btnAddIndent.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                indentSearch.show("", !DmShared.dNull(txtRefDate.getText()).equalsIgnoreCase("") ? txtRefDate.getText() : txtVoucherDate.getText(), new ProjectVoucherCategory().PHARMACY_PURCHASE_ORDER);
            }
        });
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
        txtVoucherDate.setTag(DmShared.dNull(defaultValue.getCurrentDate()));
        txtHVoucherType.setTag(defaultValue.getIsContinuousNewMode() ? defaultValue.getNewVoucherTypeCode() : defaultValue.getVoucherTypeCode());
        txtHVoucherType.setText(defaultValue.getIsContinuousNewMode() ? defaultValue.getNewVoucherTypeName() : defaultValue.getVoucherTypeName());
        guiTemplateCode = defaultValue.getIsContinuousNewMode() ? defaultValue.getNewGuiTemplateCode() : defaultValue.getGuiTemplateCode();
        indentRequired = defaultValue.getIndentRequired();
        ohlHandler.initialize();
        gui.initializeViaTemplateCode();
        txtHSupplier.setFocus(true);
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
        guiTemplateCode = "";
        selectedVoucherType = "";
        overHead.clear();
        gui.clear();
        cbShowUsedQuotationsAlso.setValue(false);
        grdItemDetail.getGrid().getStore().clear();
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        userControl.setTextBlog(approvedByBlog, "");
        recId = new RecId();
    }

    private void manageDisplay(boolean flag) {
        txtOrderNo.setEnabled(false);
        txtEnquiryNo.setEnabled(false);
        cbShowUsedQuotationsAlso.setEnabled(flag);
        if (DmShared.xNull(topControl.getMode()).equalsIgnoreCase("Edit")) {
            txtHVoucherType.setEnabled(false);
        }
        flwAddIndent.clear();
        if (!DmShared.xNull(topControl.getMode()).equalsIgnoreCase("Browse")) {
            flwAddIndent.add(btnAddIndent);
        }
    }

    private void setApproved(String docId, boolean isApprove) {
        try {
            if (DmShared.iNull(txtWTACode.getText()).longValue() != 0) {
                walkThrough.show(txtWTACode.getText());
            } else {
                SrvAsync.invokeRPC().setApproval(DmClient.getLoginUserName(), isApprove, IsUnApprove, docId, new AsyncCallback<DmResult<String>>() {
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

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnApprovedBy)) {
            if (event.getSource().equals(btnApprovedBy)) {
                if (DmShared.iNull(topControl.getDocId()).longValue() != 0) {
                    if (DmShared.xNull(userControl.getTextBlog(approvedByBlog)).equalsIgnoreCase("")) {
                        setApproved(DmShared.iNull(topControl.getDocId()).toString(), true);
                        sendMail();
                    } else {
                        setApproved(DmShared.iNull(topControl.getDocId()).toString(), false);
                    }

                }
            }
        }
    }

    private void printRecord(boolean printToPrinter) {

        HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
        hashMapFilter.put("docId", new FieldUDT_Mdl("PO No.", DmShared.xNull(txtOrderNo.getText()), DmShared.xNull(topControl.getDocId())));
        new OpenSourceReport(DmShared.xNull(defaultValue.getVoucherTypeCode()), "", "Purchase Order ", widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
            @Override
            public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                DmClient.localSettingPrintMap(reportProperty, moduleName, widgetName, "Print");
                return reportProperty;
            }
        }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
    }

    private void sendMail() {

        LinkedHashMap<String, FieldUDT_Mdl> hashMapFilter = new LinkedHashMap<>();
        hashMapFilter.put("docId", new FieldUDT_Mdl("PO No.", DmShared.xNull(txtOrderNo.getText()), DmShared.xNull(topControl.getDocId())));

        STClient.sendReportToEmail(DmShared.xNull(topControl.getDocId()), DmShared.xNull(defaultValue.getVoucherTypeCode()), hashMapFilter, widgetName);

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

    private void resetTaxSummary() {
        List<List<OverHeadLine_Mdl>> ohlList = new ArrayList<List<OverHeadLine_Mdl>>();
        for (GrdMedicineDetail_Mdl grdMdl : grdItemDetail.getGrid().getStore().getAll()) {
            if (grdMdl != null && grdMdl.getOHLList() != null) {
                ohlList.add(grdMdl.getOHLList());
            }
        }
        taxSummary.reset(ohlList);
    }

    private void getCDdetailForIndent() {
        CDdetail_Mdl cdDetailNode;

        List<CDdetail_Mdl> lstCdDetail = new ArrayList<CDdetail_Mdl>();

        for (int i = 0; i < grdItemDetail.getGrid().getStore().getAll().size(); i++) {
            cdDetailNode = new CDdetail_Mdl();
            cdDetailNode.setMedicineCode(DmShared.iNull(grdItemDetail.getGrid().getStore().get(i).getSharedKeyFieldCode()).toString());
            cdDetailNode.setSupplierCode(DmShared.iNull(txtHSupplier.getTag()).toString());
            lstCdDetail.add(cdDetailNode);
        }

        SrvAsync.invokeRPC().getCDdetailForIndent(lstCdDetail, new AsyncCallback<DmResult<List<CDdetail_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<CDdetail_Mdl>> result) {
                if (result.isStatus()) {
                    if (result.isStatus()) {
                        setCDdetailForIndent(result.getData());
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            }
        });
    }

    private void setCDdetailForIndent(List<CDdetail_Mdl> lstCdDetail) {

        for (CDdetail_Mdl lstCDNode : lstCdDetail) {

            if (lstCdDetail != null) {
                for (int i = 0; i < grdItemDetail.getGrid().getStore().getAll().size(); i++) {
                    if (grdItemDetail.getGrid().getStore().get(i).getSharedKeyFieldCode() != null && DmShared.iNull(grdItemDetail.getGrid().getStore().get(i).getSharedKeyFieldCode()).longValue() > 0) {

                        if (DmShared.xNull(lstCDNode.getMedicineCode()).equalsIgnoreCase(DmShared.xNull(grdItemDetail.getGrid().getStore().get(i).getSharedKeyFieldCode()))) {

                            OverHeadLine_Mdl lineOH = OverHeadLine_Shared.getOHByUniqueId("DS", grdItemDetail.getGrid().getStore().get(i).getOHLList());

                            lineOH.setAtRate(DmClient.vNull(lstCDNode.getDiscountPercent(), "0.00"));
                            ohlHandler.calculate(grdItemDetail.getGrid().getStore().get(i).getAmount(), i);

                            if (DmShared.vNull(lstCDNode.getDiscountPercent()) > 0) {
                                lineOH.setAtRateEditable("0");
                                lineOH.setAmountEditable("0");
                            } else {
                                lineOH.setAtRateEditable("1");
                                lineOH.setAmountEditable("1");
                            }

                        }
                    }

                }
            }

        }

        grdItemDetail.commitChanges();
        calculate();
        ohlHandler.syncronizeLineHeader();

    }
}
