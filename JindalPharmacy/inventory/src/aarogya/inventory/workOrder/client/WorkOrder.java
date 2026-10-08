package aarogya.inventory.workOrder.client;

import aarogya.inventory.workOrder.shared.*;
import aarogya.inventory.indentSearch.client.IndentSearch;
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
import dataman.dmToolkit.walkThrough.client.WalkThrough;
import java.util.HashMap;
import aarogya.inventory.workOrder.shared.WorkOrder_Srv;
import aarogya.inventory.workOrder.shared.WorkOrder_SrvAsync;

public class WorkOrder extends WorkOrder_Dsg implements BlurHandler, AttachEvent.Handler, ClickHandler, DmRootEvents {

    ServiceInvoker<WorkOrder_SrvAsync> SrvAsync;
    protected DefaultValue_Mdl defaultValue;
    final IndentSearch indentSearch = new IndentSearch();
    protected final OverHeadLineHandler<GrdIndentDetail_Mdl> ohlHandler = new OverHeadLineHandler(overHead, 300, 150);
    final WorkOrderGridEdit<GrdIndentDetail_Mdl, Grd_PrpAcs> itemDetailGridEdit = new WorkOrderGridEdit(grdItemDetail);
    WalkThrough walkThrough;
    final ModifyReason modifyReason = new ModifyReason();
    private ModifyReason_Mdl modifyReasonMdl;
    private String guiTemplateCode = "", selectedVoucherType = "", selectedQuotationNo = "";
    private RecId recId = new RecId();
    private boolean isApprove = false;

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

    private void topControlEvents(UserMenu menuPermission) {

        topControl.setTitle(this.asWidget().getTitle());

        topControl.setOtherRequired(false);

        topControl.setRefreshRequired(false);

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

        ohlHandler.addCallback(new OverHeadLineHandler.Callback<GrdItemDetail_Mdl>() {

            @Override
            public void initialized() {
                grdItemDetail.getGrid().getStore().clear();
                grdItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
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

        for (String controlPermission : menuPermission.getControlPermission()) {
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(approvedByBlog)) {
                isApprove = true;
            }
        }
        topControl.setUserPermission(menuPermission, new VoucherPermissionHandler(DmClient.getVoucherPermission(new String[]{new ProjectVoucherCategory().PURCHASE_ORDER})) {
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
                    grdItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
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
                saveValidation();
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(WorkOrder.this);
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
                reset(false);
                fillItemDetails(listKF);
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
                    reset(true);
                    if (blnStatus && tRow != null) {
                        List<KeyFieldExtended> fields = new ArrayList<>();
                        fillItemDetails(fields);
                        txtHQuotationNo.setTag2nd(tRow.getQuotationDate());
                        txtEnquiryNo.setText(tRow.getEnquiryRefNo());
                        txtEnquiryNo.setTag(tRow.getEnquiryDocId());
                        if (DmShared.iNull(tRow.getGuiConfigCode()).longValue() > 0) {
                            gui.copyGUI(tRow.getGuiConfigCode(), tRow.getSharedKeyFieldCode(), null);
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
                    reset(false);
                    if (blnStatus && t1Row != null) {
                        guiTemplateCode = t1Row.getSharedPreparedBy();
                    } else {
                        guiTemplateCode = "";
                    }
                    gui.initializeViaTemplateCode();
                }
            }
        });

        txtHSupplier.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHSupplierPT_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSupplierPT(false, txtHVoucherType.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHSupplierPT_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHQuotationNo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHSupplierPT_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHSupplier.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHQuotationNo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHSupplierPT_Mdl tRow) {
                if (blnStatus && tRow != null) {
                    if (!DmShared.xNull(txtHSupplier.getTag()).equalsIgnoreCase(txtHSupplier.getTag2nd())) {
                        txtHSupplier.setTag2nd(DmShared.xNull(txtHSupplier.getTag()));
                        setCardAcInOH();
                        reset(false);
                        if (!DmShared.xNull(tRow.getGuiConfigCode()).equalsIgnoreCase("")) {
                            copyGUI(tRow.getGuiConfigCode(), tRow.getSharedKeyFieldCode());
                        }
                    }
                }
            }
        });
    }

    private void reset(boolean isQuotation) {
        if (!isQuotation) {
            txtHQuotationNo.setTag("");
            txtHQuotationNo.setTag2nd("");
            txtHQuotationNo.setText("");
        }
        txtEnquiryNo.setTag2nd("");
        txtEnquiryNo.setTag("");
        txtEnquiryNo.setText("");
        ohlHandler.initialize();
    }

    private void fillItemDetails(List<KeyFieldExtended> keyField) {
        SrvAsync.invokeRPC().getItemDetail(txtHQuotationNo.getTag(), keyField, new AsyncCallback<DmResult<WorkOrder_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<WorkOrder_Mdl> result) {
                if (result.isStatus()) {
                    WorkOrder_Mdl entry_Mdl = result.getData();
                    List<GrdItemDetail_Mdl> grdIndentDetail_Mdl = entry_Mdl.getGrdItemDetail();
                    grdItemDetail.getGrid().getStore().clear();
                    grdItemDetail.getGrid().getStore().addAll(grdIndentDetail_Mdl);
                    grdItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
                    fillGrdItemDetail(grdIndentDetail_Mdl);
                    txtHQuotationNo.setFocus(true);
                }
            }
        });
    }

    private void fillGrdItemDetail(List<GrdItemDetail_Mdl> listGRN) {
        for (int i = 0; i < listGRN.size(); i++) {
            GrdItemDetail_Mdl detail_Mdl = listGRN.get(i);
            if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() > 0) {
                grdItemDetail.getGrid().getStore().get(i).setAmount(DmClient.vNull(DmShared.vNull(grdItemDetail.getGrid().getStore().get(i).getOrderQuantity()).doubleValue() * DmShared.vNull(grdItemDetail.getGrid().getStore().get(i).getRate()).doubleValue(), "0.00"));
                ohlHandler.setReference(detail_Mdl.getOHLListQuotation(), ohlHandler.getOverHeadList(i));
                ohlHandler.calculate(grdItemDetail.getGrid().getStore().get(i).getAmount(), i);
                grdItemDetail.getGrid().getStore().get(i).setOHLList(ohlHandler.getOverHeadList(i));
            }
        }
        grdItemDetail.commitChanges();
        calculate();
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
        itemDetailGridEdit.addCallBack(new WorkOrderGridEdit.CallBack() {

            @Override
            public void onOk(GrdItemDetail_Mdl opdService) {
                grdItemDetail.commitChanges();
                calculate();
                ohlHandler.syncronizeLineHeader();
            }

            @Override
            public void onDelete() {
                grdItemDetail.commitChanges();
                calculate();
                ohlHandler.syncronizeLineHeader();
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
        } else if (DmShared.vNull(challanedQty) != 0) {
            mode = "Browse";
            msg = "Record has been used, can't change record.";
        }

        if (mode.trim().equalsIgnoreCase("Browse")) {
            if (!topControl.getMode().equalsIgnoreCase("Browse") && !msg.equalsIgnoreCase("")) {
                new DmAlertMessageBox(msg, (DmTextBox) null).show();
            }
            itemDetailGridEdit.show(currentRowIndex, false, false, false, this, mode,cbShowStockItems.getValue());
        } else {
            itemDetailGridEdit.show(currentRowIndex, true, true, true, this, mode,cbShowStockItems.getValue());
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

    public WorkOrder(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(WorkOrder_Srv.class), WorkOrder.class.getName());
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
        overHead.setBannerVisible(false);
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
                    guiTemplateCode = defaultValue.getGuiTemplateCode();
                    gui.initializeViaTemplateCode();
                    swi.setWidget(WorkOrder.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void topControlSave() {
        WorkOrder_Mdl mrTemp;

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
                WorkOrder.this.modifyReasonMdl = modifyReason;
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

    private WorkOrder_Mdl chkValidation() {
        WorkOrder_Mdl mrTemp;
        mrTemp = topControlSaveInitialize();
        WorkOrder_Vld.chkSave(mrTemp, DmClient.getLoginDate());
        return mrTemp;
    }

    private WorkOrder_Mdl topControlSaveInitialize() {
        WorkOrder_Mdl mrTemp = new WorkOrder_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
            mrTemp.getRecIdOrder().setCounter(recId.getCounter());
            mrTemp.getRecIdOrder().setPrefix(recId.getPrefix());
            mrTemp.getRecIdOrder().setRecIdValue(recId.getRecIdValue());
        }
        mrTemp.setU_EntDt_LatestLine(DmShared.xNull(txtOrderNo.getTag()));
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtOrderNo.getText()));
        mrTemp.setVoucherDate(DmShared.xNull(txtVoucherDate.getText()));
        mrTemp.setSupplierTag(DmShared.xNull(txtHSupplier.getTag()));
        mrTemp.setEnquiryTag(DmShared.xNull(txtEnquiryNo.getTag()));
        mrTemp.setEnquiryText(DmShared.xNull(txtEnquiryNo.getText()));
        mrTemp.setQuotationDate(DmShared.xNull(txtHQuotationNo.getTag2nd()));
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
                    List<GrdItemDetail_Mdl> listTemp = grdItemDetail.getGrid().getStore().getAll();
                    for (GrdItemDetail_Mdl node : listTemp) {
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
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<WorkOrder_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<WorkOrder_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<WorkOrder_Mdl> result) {
        clear();
        if (result.isStatus()) {
            WorkOrder_Mdl mrTemp = result.getData();
            flwAddIndent.clear();
            topControl.setDocId(mrTemp.getSharedKeyFieldCode());
            recId = mrTemp.getRecIdOrder();
            txtOrderNo.setText(mrTemp.getSharedKeyFieldName());
            txtOrderNo.setTag(DmShared.iNull(mrTemp.getU_EntDt_LatestLine()).toString());
            txtVoucherDate.setText(mrTemp.getVoucherDate());
            txtVoucherDate.setTag(mrTemp.getVoucherDate());
            txtHVoucherType.setTag(mrTemp.getVoucherTypeTag());
            txtHVoucherType.setText(mrTemp.getVoucherTypeText());
            txtHSupplier.setTag(mrTemp.getSupplierTag());
            txtHSupplier.setTag2nd(mrTemp.getSupplierTag());
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

            grdItemDetail.getGrid().getStore().clear();
            grdItemDetail.getGrid().getStore().addAll(mrTemp.getGrdItemDetail());

            overHead.setOverHeadList(mrTemp.getOverHeadList(), mrTemp.getGrossAmount());
            ohlHandler.setDefault();
            gui.initialize(mrTemp.getGuiMdl());
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
        manageDisplay(false);
    }

    private void calculate() {
        double netAmount = 0;
        for (GrdItemDetail_Mdl detail_Mdl : grdItemDetail.getGrid().getStore().getAll()) {
            if (!DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                netAmount += DmShared.vNull(detail_Mdl.getAmount()).doubleValue();
            }
        }
        grdItemDetail.commitChanges();
        overHead.calculate(DmClient.vNull(netAmount, "0.00"));
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
                indentSearch.show(DmShared.xNull(txtHVoucherType.getTag()), null, !DmShared.dNull(txtRefDate.getText()).equalsIgnoreCase("") ? txtRefDate.getText() : txtVoucherDate.getText(), new ProjectVoucherCategory().PURCHASE_ORDER, "");   //PURCHASE_ORDER_UNREGISTERED //Manage Also UnRegistered PO                
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
        txtVoucherDate.setTag(defaultValue.getIsContinuousNewMode() ? DmShared.dNull(defaultValue.getNewVoucherDate()) : DmShared.dNull(defaultValue.getCurrentDate()));
        txtHVoucherType.setTag(defaultValue.getIsContinuousNewMode() ? defaultValue.getNewVoucherTypeCode() : defaultValue.getVoucherTypeCode());
        txtHVoucherType.setText(defaultValue.getIsContinuousNewMode() ? defaultValue.getNewVoucherTypeName() : defaultValue.getVoucherTypeName());
        guiTemplateCode = defaultValue.getIsContinuousNewMode() ? defaultValue.getNewGuiTemplateCode() : defaultValue.getGuiTemplateCode();
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
        selectedQuotationNo = "";
        overHead.clear();
        gui.clear();
        cbShowUsedQuotationsAlso.setValue(false);
        cbShowStockItems.setValue(false);
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
        cbShowStockItems.setEnabled(flag);
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

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnApprovedBy)) {
            if (event.getSource().equals(btnApprovedBy)) {
                if (DmShared.iNull(topControl.getDocId()).longValue() != 0) {
                    if (DmShared.xNull(userControl.getTextBlog(approvedByBlog)).equalsIgnoreCase("")) {
                        setApproved(DmShared.iNull(topControl.getDocId()).toString(), true);
                    } else {
                        setApproved(DmShared.iNull(topControl.getDocId()).toString(), false);
                    }
                }
            }
        }
    }

    private void prnRecord(boolean printToPrinter) {
        HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
        hashMapFilter.put("docId", new FieldUDT_Mdl("PO No.", DmShared.xNull(txtOrderNo.getText()), DmShared.xNull(topControl.getDocId())));
        new OpenSourceReport(DmShared.xNull(txtHVoucherType.getTag()), "", widgetName, widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
            @Override
            public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                DmClient.localSettingPrintMap(reportProperty, moduleName, widgetName, "Print");
                return reportProperty;
            }
        }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
    }
    
     private void copyGUI(String guiCodeFrom, final String copyFromDocId) {
        if (DmShared.iNull(guiCodeFrom).longValue() != 0) {
            gui.copyGUI(guiCodeFrom, copyFromDocId, null);
        }
    }
}
