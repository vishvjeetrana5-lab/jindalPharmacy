package aarogya.inventory.purchaseOrder.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import dataman.dmBase.self.shared.*;
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
import dataman.dmToolkit.overHead.shared.OverHead_Shared;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Shared;
import java.util.ArrayList;
import java.util.List;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.menu.Item;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmFileManager.client.DmFileManager;
import dataman.dmToolkit.dmGUI.client.DmGUI;
import dataman.dmToolkit.dmGUI.shared.DmGUIControl_Mdl;
import dataman.dmToolkit.dmGUI.shared.DmGUI_Mdl;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Mdl;
import dataman.dmToolkit.walkThrough.client.WalkThrough;
import java.util.HashMap;
import static aarogya.projectMenu.self.shared.ProjectRuleCode.PURCHASE_ORDER;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.inventory.indentSearch.client.IndentSearch;
import aarogya.inventory.purchaseOrder.shared.DefaultValue_Mdl;
import aarogya.inventory.purchaseOrder.shared.Find_Mdl;
import aarogya.inventory.purchaseOrder.shared.GrdIndentDetail_Mdl;
import aarogya.inventory.purchaseOrder.shared.GrdItemDetail_Mdl;
import aarogya.inventory.purchaseOrder.shared.PurchaseOrder_Mdl;
import aarogya.inventory.purchaseOrder.shared.PurchaseOrder_Srv;
import aarogya.inventory.purchaseOrder.shared.PurchaseOrder_SrvAsync;
import aarogya.inventory.purchaseOrder.shared.PurchaseOrder_Vld;
import aarogya.inventory.purchaseOrder.shared.TxtHAmendmentOrder_Mdl;
import aarogya.inventory.purchaseOrder.shared.TxtHQuotationNo_Mdl;
import aarogya.inventory.purchaseOrder.shared.TxtHSupplierPT_Mdl;
import aarogya.inventory.purchaseOrder.shared.TxtHVoucherType_Mdl;
import aarogya.selfToolkit.modifyReason.client.ModifyReason;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.serviceGrid.client.ServiceGrid;
import aarogya.selfToolkit.serviceGrid.shared.ServiceDetail_Mdl;
import aarogya.selfToolkit.stStatic.client.STClient;
import aarogya.inventory.purchaseOrderDisplay.shared.PurchaseOrderDisplay_Mdl;
import java.util.LinkedHashMap;

public class PurchaseOrder extends PurchaseOrder_Dsg implements BlurHandler, AttachEvent.Handler, ClickHandler, DmRootEvents, SelectionHandler<Item> {

    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    ServiceInvoker<PurchaseOrder_SrvAsync> SrvAsync;
    protected DefaultValue_Mdl defaultValue;
    final IndentSearch indentSearch = new IndentSearch();
    final PurchaseOrderGridEdit<GrdItemDetail_Mdl, Grd_PrpAcs> itemDetailGridEdit = new PurchaseOrderGridEdit(grdItemDetail, this);
    WalkThrough walkThrough;
    final ModifyReason modifyReason = new ModifyReason();
    private ModifyReason_Mdl modifyReasonMdl;
    protected String selectedVoucherType = "", selectedQuotationNo = "", voucherCategory = "", guiTemplateCode = "",
            location, selectedSupplierCode, partyType, supplierGstin, supplierGstinDate, partyTypeForEntry;
    private RecId recId = new RecId();
    private boolean isApprove = false, isUnApprove = false;
    final DmFileManager fileManager = new DmFileManager(PURCHASE_ORDER.getRuleCode());
    private boolean isAttachFiles = false, isDeleteFiles = false;
    private String strAllowValueUpto = "", preparedBy = "";

    List<KeyFieldExtended> lstIndentCard = new ArrayList<KeyFieldExtended>();
    private KeyFieldExtended quotationCard_Mdl = null;
    private DmCallBack quotationCardCallBack = null;
    private boolean indentCardFlag = false;

    private void topControlEvents(UserMenu menuPermission) {

        topControl.setTitle(this.asWidget().getTitle());

        topControl.setOtherRequired(true);

        topControl.setRefreshRequired(false);

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
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(attachFileBlog)) {
                isAttachFiles = true;
            }
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(deleteFileBlog)) {
                isDeleteFiles = true;
            }
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(unApprovedByBlog)) {
                isUnApprove = true;
            }
        }

        topControl.setUserPermission(menuPermission, new VoucherPermissionHandler(DmClient.getVoucherPermission(new String[]{new ProjectVoucherCategory().PURCHASE_ORDER, new ProjectVoucherCategory().PURCHASE_ORDER_UNREGISTERED, new ProjectVoucherCategory().SERVICE_ORDER, new ProjectVoucherCategory().SERVICE_ORDER_UNREGISTERED})) {
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
                        grdItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
                        serviceDetailGrid.getServiceGrid().getGrid().getStore().add(new ServiceDetail_Mdl());
                        serviceDetailGrid.initialize(setServiveDefaultValue());
                        syncronizeLineService(false, true);
                        txtHSupplier.setFocus(true);
                    } else if (DmShared.xNull(preparedBy).equalsIgnoreCase(defaultValue.getUserName())) {
                        manageDisplay(true);
                        grdItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
                        serviceDetailGrid.getServiceGrid().getGrid().getStore().add(new ServiceDetail_Mdl());
                        serviceDetailGrid.initialize(setServiveDefaultValue());
                        syncronizeLineService(false, true);
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

        topControl.addDeleteHandeller(
                new DmTopControl.eDelete() {
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
        }
        );

        topControl.addCardHandeller(
                new DmTopControl.eCard() {
            @Override
            public void cardClicked() {
                topControlCard();
            }
        }
        );

        topControl.addFindHandeller(
                new DmTopControl.eFind() {
            @Override
            public void findClicked() {
                flwForm.remove(vrtInnerForm);
                flwForm.add(dmFind);
            }
        }
        );

        topControl.addMoveRecHandeller(
                new DmTopControl.iMoveRec() {
            @Override
            public void moveRec(String strFetchCode
            ) {
                topControlMoveRec(strFetchCode);
            }
        }
        );

        topControl.addPrintHandeller(
                new DmTopControl.ePrint() {

            @Override
            public void printClicked() {
                prnRecord(false);
            }
        }
        );

        topControl.addSaveHandeller(
                new DmTopControl.eSave() {
            @Override
            public void saveClicked() {
                saveValidation();
            }
        }
        );

        topControl.addCloseHandeller(
                new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(PurchaseOrder.this);
            }
        }
        );

        topControl.addOtherHandeller(
                new DmTopControl.eOther() {
            @Override
            public void otherClicked() {
                menu.show(topControl.getButton("Other"));
            }
        }
        );

        topControl.setDisplay(
                true);
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
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHQuotationNo(cbShowUsedQuotationsAlso.getValue(), txtHSupplier.getTag(), !DmShared.dNull(txtRefDate.getText()).trim().equals("") ? txtRefDate.getText() : txtVoucherDate.getText(), voucherCategory, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHQuotationNo_Mdl>>>() {

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
                fillQuotationDetail(blnStatus, tRow);
            }
        });

        txtHVoucherType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHVoucherType_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHVoucherType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHVoucherType_Mdl>>>() {

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
                    if (topControl.getMode().equalsIgnoreCase("New")) {
                        txtHSupplier.setTag2nd("");
                        txtHSupplier.setTag("");
                        txtHSupplier.setText("");
                    }

                    //Set Related Values
                    if (blnStatus && t1Row != null) {
                        voucherCategory = t1Row.getVoucherCategory();
                        guiTemplateCode = t1Row.getGuiTemplateCode();
                    }

                    //Reset GUI
                    gui.initializeViaTemplateCode();

                    //set RCM
                    setRCM(t1Row.getVoucherCategory());
                    if (DmShared.xNull(voucherCategory).trim().equalsIgnoreCase(pvc.SERVICE_ORDER)
                            || DmShared.xNull(voucherCategory).trim().equalsIgnoreCase(pvc.SERVICE_ORDER_UNREGISTERED)) {
                        flwAddIndent.clear();
//                        flwBlank.clear();
                    } else {
                        flwAddIndent.add(btnAddIndent);
//                        flwBlank.add(vl.add(quotation()));
                    }
                }
            }
        });

        txtHSupplier.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHSupplierPT_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSupplierPT(voucherCategory, false, txtHVoucherType.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHSupplierPT_Mdl>>>() {

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
                fillSupplierDetail(blnStatus, tRow);
            }
        });

        txtHAmendmentOrder.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHAmendmentOrder_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                if (DmShared.xNull(txtHQuotationNo.getTag()).equalsIgnoreCase("")) {
                    SrvAsync.invokeRPC(blnCallBackWait).getTxtHAmendmentOrder(
                            DmShared.dNull(txtVoucherDate.getText()),
                            DmShared.xNull(txtHSupplier.getTag()),
                            DmShared.xNull(txtHAmendmentOrder.getTag()),
                            helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHAmendmentOrder_Mdl>>>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            new DmAlertMessageBox(caught.getMessage(), txtHAmendmentOrder).show();
                        }

                        @Override
                        public void onSuccess(DmResultPageing<List<TxtHAmendmentOrder_Mdl>> result) {
                            if (result.isStatus()) {
                                txtHAmendmentOrder.setData(result.getData(), result.getPaging());
                            } else {
                                new DmAlertMessageBox(result.getErrorMessage(), txtHAmendmentOrder).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHAmendmentOrder_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
                    if (!DmShared.xNull(txtHAmendmentOrder.getTag2nd()).equalsIgnoreCase(DmShared.xNull(txtHAmendmentOrder.getTag()))) {
                        txtAmendmentDate.setText(DmShared.xNull(t1Row.getSharedPreparedBy()));
                        txtHAmendmentOrder.setTag2nd(DmShared.xNull(txtHAmendmentOrder.getTag()));
                        fillAmendmentGridDetail(t1Row.getSharedKeyFieldCode());
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

    private void fillSupplierDetail(boolean blnStatus, TxtHSupplierPT_Mdl t1Row) {
        if (topControl.getMode().equalsIgnoreCase("New")
                && !DmShared.xNull(selectedSupplierCode).equalsIgnoreCase(DmShared.xNull(txtHSupplier.getTag()))) {

            reset(false, true);
            selectedSupplierCode = DmShared.xNull(txtHSupplier.getTag());
        }

        if (blnStatus && t1Row != null) {
            txtHSupplier.setTag2nd(DmShared.xNull(t1Row.getStateCode()));
            supplierGstin = DmShared.xNull(t1Row.getGstin());
            supplierGstinDate = DmShared.dNull(t1Row.getGstinDate());
            partyType = DmShared.xNull(t1Row.getPartyType());
            resetGstDetails();
        }
        clearGrid();
    }

    private void fillQuotationDetail(boolean blnStatus, TxtHQuotationNo_Mdl tRow) {
        if (!DmShared.xNull(selectedQuotationNo).equalsIgnoreCase(DmShared.xNull(txtHQuotationNo.getTag()))) {
            reset(true, false);
            if (blnStatus && tRow != null) {
                List<KeyFieldExtended> fields = new ArrayList<>();
                fillItemDetails(fields);
                txtHQuotationNo.setTag2nd(tRow.getQuotationDate());
                txtEnquiryNo.setText(tRow.getEnquiryRefNo());
                txtEnquiryNo.setTag(tRow.getEnquiryDocId());
                if (DmShared.iNull(tRow.getGuiConfigCode()).longValue() != 0) {
                    gui.copyGUI(tRow.getGuiConfigCode(), tRow.getSharedKeyFieldCode(), null);
                }
            }
        }
    }

    private void reset(boolean isQuotation, boolean isSupplier) {

        if (!isQuotation) {
            txtHQuotationNo.setTag("");
            txtHQuotationNo.setTag2nd("");
            txtHQuotationNo.setText("");
        }
        txtEnquiryNo.setTag2nd("");
        txtEnquiryNo.setTag("");
        txtEnquiryNo.setText("");

        txtHAmendmentOrder.setTag("");
        txtHAmendmentOrder.setTag2nd("");
        txtHAmendmentOrder.setText("");

        txtAmendmentDate.setTag("");
        txtAmendmentDate.setTag2nd("");
        txtAmendmentDate.setText("");

        if (isSupplier && indentCardFlag) {
            indentCardFlag = false;
        } else {
            indentCardFlag = false;
        }
    }

    private void fillItemDetails(List<KeyFieldExtended> keyField) {
        SrvAsync.invokeRPC().getItemDetail(DmShared.xNull(txtHVoucherType.getTag()), txtHQuotationNo.getTag(), voucherCategory, location, txtVoucherDate.getText(), defaultValue.getIsSiteStateUT(), keyField, new AsyncCallback<DmResult<PurchaseOrder_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<PurchaseOrder_Mdl> result) {
                if (result.isStatus()) {
                    PurchaseOrder_Mdl entry_Mdl = result.getData();
                    List<GrdItemDetail_Mdl> grdIndentDetail_Mdl = entry_Mdl.getGrdItemDetail();
                    grdItemDetail.getGrid().getStore().clear();
                    grdItemDetail.getGrid().getStore().addAll(grdIndentDetail_Mdl);
                    grdItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
                    serviceDetailGrid.getServiceGrid().getGrid().getStore().clear();
                    serviceDetailGrid.getServiceGrid().getGrid().getStore().addAll(entry_Mdl.getGrdServiceDetail());
                    serviceDetailGrid.getServiceGrid().getGrid().getStore().add(new ServiceDetail_Mdl());
                    fillGrdItemDetail(grdIndentDetail_Mdl);
                    txtHSupplier.setFocus(true);

                    if (quotationCardCallBack != null) {
                        DmResult<Object> dmrTemp = new DmResult<>();
                        dmrTemp.setStatus(true);
                        quotationCardCallBack.result(dmrTemp);
                        grdItemDetail.commitChanges();
//                        ohlHandler.syncronizeLineHeader(); //<Ag> TBR
                        syncronizeLineService(false, true);
                        calculate();
                    }
                    quotationCardCallBack = null;
                    quotationCard_Mdl = null;
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
                /*  //<Ag> TBR
                grdItemDetail.getGrid().getStore().get(i).setAmount(DmClient.vNull(DmShared.vNull(grdItemDetail.getGrid().getStore().get(i).getOrderQuantity()).doubleValue() * DmShared.vNull(grdItemDetail.getGrid().getStore().get(i).getRate()).doubleValue(), "0.00"));
                ohlHandler.setReference(detail_Mdl.getOHLListQuotation(), ohlHandler.getOverHeadList(i));
                ohlHandler.setGSTS(grdItemDetail.getGrid().getStore().get(i).getLstITS(), i);
                ohlHandler.calculate(grdItemDetail.getGrid().getStore().get(i).getAmount(), i);
                grdItemDetail.getGrid().getStore().get(i).setOHLList(ohlHandler.getOverHeadList(i)); 
                 */  //<Ag> TBR
                itemDetailGridEdit.displayRow(detail_Mdl);
                itemDetailGridEdit.calculate();
                itemDetailGridEdit.fillRowData(detail_Mdl);
            }
        }
        serviceDetailGrid.getServiceGrid().commitChanges();
        grdItemDetail.commitChanges();
        syncronizeLineService(true, true);
//        ohlHandler.syncronizeLineHeader(); //<Ag> TBR
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
            public void onOk(GrdItemDetail_Mdl opdService) {
                grdItemDetail.commitChanges();
                serviceDetailGrid.initialize(setServiveDefaultValue());
                syncronizeLineService(true, true);
                serviceDetailGrid.getServiceGrid().commitChanges();
            }

            @Override
            public void onDelete() {
                grdItemDetail.commitChanges();
                serviceDetailGrid.getServiceGrid().commitChanges();
                syncronizeLineService(true, true);
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
        });
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
            itemDetailGridEdit.show(currentRowIndex, false, false, false, mode);
        } else {
            itemDetailGridEdit.show(currentRowIndex, true, true, true, mode);
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
            if (!DmShared.xNull(txtRefDate.getText()).equalsIgnoreCase("")) {
                if ((DmShared.convertDate(txtVoucherDate.getText()).getTime() < (DmShared.convertDate(DmShared.dNull(txtRefDate.getText())).getTime()))) {
                    new DmAlertMessageBox("Ref. Date should be less than or equal to Voucher Date.", txtRefDate).show();
                }
            }
        }
    }

    public PurchaseOrder(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<PurchaseOrder_SrvAsync>(GWT.create(PurchaseOrder_Srv.class), PurchaseOrder.class.getName());
        this.asWidget().setTitle(widgetName);
        walkThrough = new WalkThrough(SrvAsync);
        txtVoucherDate.addBlurHandler(this);
        txtRefDate.addBlurHandler(this);
        btnApprovedBy.addClickHandler(this);
        manageFileMenu.addSelectionHandler(this);
        flwForm.addAttachHandler(this);
        topControlEvents(menuPermission);
        findEvents();
        textHelpEvents();
        gridEvents();
        showIndentSearch();
//        overHead.setBannerVisible(true); //<Ag> TBR
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
                    guiTemplateCode = defaultValue.getVoucherType().getGuiTemplateCode();
                    gui.initializeViaTemplateCode();
                    taxSummary.initialize(defaultValue.isGstApplicable());
                    swi.setWidget(PurchaseOrder.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void topControlSave() {
        PurchaseOrder_Mdl mrTemp;

        try {
            syncronizeLineService(true, false);
            defaultValue.setNewVoucherTypeCode(txtHVoucherType.getTag());
            defaultValue.setNewVoucherTypeName(txtHVoucherType.getText());
            defaultValue.setNewVoucherDate(txtVoucherDate.getText());
            defaultValue.setNewGuiTemplateCode(guiTemplateCode);
            mrTemp = chkValidation();
//            OverHead_Shared.chkGrid(mrTemp.getOverHeadList());
//            OverHeadLine_Shared.chkGrid(mrTemp.getGrdItemDetail());
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
            mrTemp.getRecIdOrder().setCounter(recId.getCounter());
            mrTemp.getRecIdOrder().setPrefix(recId.getPrefix());
            mrTemp.getRecIdOrder().setRecIdValue(recId.getRecIdValue());
        }
        mrTemp.setTempKeyCode(fileManager.getTempKeyCode());
        mrTemp.setU_EntDt_LatestLine(DmShared.xNull(txtOrderNo.getTag()));
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtOrderNo.getText()));
        mrTemp.setVoucherDate(DmShared.xNull(txtVoucherDate.getText()));

        mrTemp.setSupplierTag(DmShared.xNull(txtHSupplier.getTag()));
        mrTemp.setSupplierGstin(DmShared.xNull(supplierGstin));
        mrTemp.setSupplierGstinDate(DmShared.xNull(supplierGstinDate));
        mrTemp.setSupplierStateCode(DmShared.xNull(txtHSupplier.getTag2nd()));
        mrTemp.setPlaceOfSupply(DmShared.xNull(defaultValue.getSiteStateCode()).trim());

        mrTemp.setRcmApplicable(txtHRCMApplicable.getTag());
        mrTemp.setPartyTypeForEntry(partyTypeForEntry);
        mrTemp.setPartyType(partyType);
        mrTemp.setLocationCode(location);

        mrTemp.setAmdOrdDocId(DmShared.xNull(txtHAmendmentOrder.getTag()));
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
        mrTemp.setTotalAmt(DmShared.xNull(txtTotalAmount.getText()));
        mrTemp.setRoundOffAmount(DmShared.xNull(txtRoundoff.getText()));
        mrTemp.setNetAmount(DmShared.xNull(txtNetAmount.getText()));
        //=================== ************* ===================//

        //----------------------------------------------------------------------------------
        List<GrdItemDetail_Mdl> grdLstPoDetail_Mdl = new ArrayList<>();

        grdLstPoDetail_Mdl.addAll(grdItemDetail.getGrid().getStore().getAll());

        if (grdItemDetail.getGrid().getStore().getAll().size() >= 0) {
            int i = 0;
            for (GrdItemDetail_Mdl poDeatil_Mdl : grdItemDetail.getGrid().getStore().getAll()) {
                if (DmShared.iNull(poDeatil_Mdl.getSharedKeyFieldCode()).longValue() == 0) {
                    grdLstPoDetail_Mdl.remove(i);
                    i--;
                }
                i++;
            }
            if (grdLstPoDetail_Mdl.size() < grdItemDetail.getGrid().getStore().getAll().size()) {
                grdItemDetail.getGrid().getStore().clear();
                grdItemDetail.getGrid().getStore().addAll(grdLstPoDetail_Mdl);
                grdItemDetail.commitChanges();
            }

        }

        mrTemp.getGrdItemDetail().clear();
        grdItemDetail.getGrid().getStore().commitChanges();
        mrTemp.getGrdItemDetail().addAll(grdItemDetail.getGrid().getStore().getAll());

        mrTemp.getGrdServiceDetail().clear();
        mrTemp.getGrdServiceDetail().addAll(serviceDetailGrid.getServiceGrid().getGrid().getStore().getAll());

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
            case 600:
                new DmAlertMessageBox(errorMessage, txtHRCMApplicable).show();
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
//        ohlHandler.setDefault(); //<Ag> TBR
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

            txtHAmendmentOrder.setTag(mrTemp.getAmdOrdDocId());
            txtHAmendmentOrder.setText(mrTemp.getAmdOrdNo());
            txtAmendmentDate.setText(mrTemp.getAmdOrdDate());

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
            partyTypeForEntry = mrTemp.getPartyTypeForEntry();
            partyType = mrTemp.getPartyType();
            location = DmShared.xNull(mrTemp.getLocationCode());

            txtRefDate.setText(mrTemp.getRefDate());
            txtRefNo.setText(mrTemp.getRefNo());

            txtWTACode.setText(mrTemp.getWtaCode());
            voucherCategory = mrTemp.getVoucherCategory();

            if (DmShared.xNull(mrTemp.getIsShowUsedQuotationsAlso()).equalsIgnoreCase("1")) {
                cbShowUsedQuotationsAlso.setValue(true);
            }

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
            txtTotalAmount.setText(DmClient.vNull(mrTemp.getTotalAmt(), "0.00"));
            txtRoundoff.setText(DmClient.vNull(mrTemp.getRoundOffAmount(), "0.00"));
            txtNetAmount.setText(DmClient.vNull(mrTemp.getNetAmount(), "0.00"));
            //=================== ************* ===================//

            //Set Lable SGST/UGST
            if (DmShared.vNull(mrTemp.getSgst()) > 0) {
                lblSgstUgst.setText(DmEnum.GSTS_Head.SGST.getName());
            } else if (DmShared.vNull(mrTemp.getUgst()) > 0) {
                lblSgstUgst.setText(DmEnum.GSTS_Head.UGST.getName());
            }

            grdItemDetail.getGrid().getStore().clear();
            grdItemDetail.getGrid().getStore().addAll(mrTemp.getGrdItemDetail());

            serviceDetailGrid.getServiceGrid().getGrid().getStore().clear();
            serviceDetailGrid.getServiceGrid().getGrid().getStore().addAll(mrTemp.getGrdServiceDetail());

            userControl.setTextBlog(preparedByBlog, mrTemp.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, mrTemp.getSharedModifiedBy());
            userControl.setTextBlog(approvedByBlog, mrTemp.getApprovedBy());

            preparedBy = "";
            String st = DmShared.xNull(mrTemp.getSharedPreparedBy());
            preparedBy = st.split(" - ")[0];

            gui.initialize(mrTemp.getGuiMdl());
            resetTaxSummary();
            serviceDetailGrid.initialize(setServiveDefaultValue());
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
        manageDisplay(false);
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
            if (!DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                if (!detail_Mdl.isServiceRecord()) {
                    goodsGrossValue += DmShared.vNull(detail_Mdl.getAmount());
                    goodsTaxableValue += DmShared.vNull(detail_Mdl.getTaxableAmt());
                } else {
                    serviceGrossValue += DmShared.vNull(detail_Mdl.getAmount());
                    serviceTaxableValue += DmShared.vNull(detail_Mdl.getTaxableAmt());
                }
                // for common qty 
                commonUnitQty += DmShared.vNull(detail_Mdl.getOrderQuantity()).doubleValue();

                //temp
                if (!detail_Mdl.isServiceRecord()) {
                    if (DmShared.xNull(CommonUnitCode).equalsIgnoreCase("")) {
                        CommonUnitCode = detail_Mdl.getPurchaseUnitCode();
                    } else if (DmShared.iNull(CommonUnitCode).longValue() != DmShared.iNull(detail_Mdl.getPurchaseUnitCode()).longValue()) {
                        isCommonUnit = false;
                    }
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

        txtTotalAmount.setText(DmClient.vNull(DmShared.vNull(txtSubTotal.getText()), "0.00"));
//        txtNetAmount.setText(DmClient.vNull(DmShared.iNull(DmShared.compute("Math.round(" + txtTotalAmount.getText() + ")")), "0.00"));
        txtNetAmount.setText(DmClient.vNull(DmShared.iNull(Math.round(DmShared.vNull(txtTotalAmount.getText()).doubleValue())), "0.00"));
        txtRoundoff.setText(DmClient.vNull(DmShared.vNull(txtNetAmount.getText()) - DmShared.vNull(txtTotalAmount.getText()), "0.00"));

        serviceDetailGrid.initialize(setServiveDefaultValue());

        resetTaxSummary();
    }

    private void resetTaxSummary() {
        List<List<OverHeadLine_Mdl>> lstOverheadLine = new ArrayList<List<OverHeadLine_Mdl>>();
        List<OverHeadLine_Mdl> overheadLine = null;
        OverHeadLine_Mdl overheadLineRow = null;
        for (GrdItemDetail_Mdl grdMdl : grdItemDetail.getGrid().getStore().getAll()) {
            if (grdMdl != null) {
                // loop start
                overheadLine = new ArrayList<OverHeadLine_Mdl>(); //1 line overhead

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

    private void topControlDelete(String strKey) {
        SrvAsync.invokeRPC().deleteRecord(strKey, DmShared.xNull(txtHAmendmentOrder.getTag()), gui.getConfigCode(), new AsyncCallback<DmResult<String>>() {

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
                indentSearch.show(DmShared.xNull(txtHVoucherType.getTag()), null, !DmShared.dNull(txtRefDate.getText()).equalsIgnoreCase("") ? txtRefDate.getText() : txtVoucherDate.getText(), voucherCategory, "");   //PURCHASE_ORDER_UNREGISTERED //Manage Also UnRegistered PO                
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
        getTempKeyCode();
        clear();
        manageDisplay(true);
        serviceDetailGrid.getServiceGrid().getGrid().getStore().add(new ServiceDetail_Mdl());
        if (defaultValue.isIsContinuousNewMode()) {
            setDefaultValues();
        } else {
            setDefaultValuesWithDateReset();
        }
        setLocation();
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

    private void setDefaultValues() {
        txtVoucherDate.setText(defaultValue.isIsContinuousNewMode() ? DmShared.dNull(defaultValue.getNewVoucherDate()) : DmShared.dNull(defaultValue.getCurrentDate()));
        txtVoucherDate.setTag(defaultValue.isIsContinuousNewMode() ? DmShared.dNull(defaultValue.getNewVoucherDate()) : DmShared.dNull(defaultValue.getCurrentDate()));
        txtHVoucherType.setTag(defaultValue.isIsContinuousNewMode() ? defaultValue.getNewVoucherTypeCode() : defaultValue.getVoucherType().getSharedKeyFieldCode());
        txtHVoucherType.setText(defaultValue.isIsContinuousNewMode() ? defaultValue.getNewVoucherTypeName() : defaultValue.getVoucherType().getSharedKeyFieldName());
        txtHVoucherType.setTag2nd(defaultValue.isIsContinuousNewMode() ? defaultValue.getNewVoucherType().getSharedKeyFieldCode() : defaultValue.getVoucherType().getSharedKeyFieldCode());
        guiTemplateCode = defaultValue.isIsContinuousNewMode() ? defaultValue.getNewGuiTemplateCode() : defaultValue.getVoucherType().getGuiTemplateCode();
        voucherCategory = defaultValue.isIsContinuousNewMode() ? defaultValue.getNewVoucherType().getVoucherCategory() : defaultValue.getVoucherType().getVoucherCategory();
        guiTemplateCode = defaultValue.isIsContinuousNewMode() ? defaultValue.getNewVoucherType().getGuiTemplateCode() : defaultValue.getVoucherType().getGuiTemplateCode();

        setRCM(voucherCategory);

        if (!(DmShared.iNull(defaultValue.getIsSiteStateUT()).intValue() == 1)) {
            lblSgstUgst.setText(DmEnum.GSTS_Head.SGST.getName());
        } else if ((DmShared.iNull(defaultValue.getIsSiteStateUT()).intValue() == 1)) {
            lblSgstUgst.setText(DmEnum.GSTS_Head.UGST.getName());
        }

        gui.initializeViaTemplateCode();
        txtHSupplier.setFocus(true);
        if (quotationCard_Mdl != null) {
            getSupplierDetail(DmShared.xNull(quotationCard_Mdl.getSharedKeyFieldName()));
        } else if (lstIndentCard != null) {
            if (lstIndentCard.size() > 0) {
                fillItemDetails(lstIndentCard);
            }
            lstIndentCard.clear();
        }

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
        txtHRCMApplicable.setTag("");
        txtHRCMApplicable.setText("");
        guiTemplateCode = "";
        selectedVoucherType = "";
        selectedQuotationNo = "";
//        overHead.clear(); //<Ag> TBR

        txtSGST.setText("");
        txtSGST.setTag("");
        txtSGST.setTag2nd("");

        txtUGST.setText("");
        txtUGST.setTag("");
        txtUGST.setTag2nd("");

        gui.clear();
        cbShowUsedQuotationsAlso.setValue(false);
        serviceDetailGrid.getServiceGrid().getGrid().getStore().clear();
        grdItemDetail.getGrid().getStore().clear();

        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        userControl.setTextBlog(approvedByBlog, "");
        recId = new RecId();
        txtVoucherDate.setTag("");
        txtVoucherDate.setTag2nd("");
        txtVoucherDate.setText("");

    }

    private void manageDisplay(boolean flag) {
        txtOrderNo.setEnabled(false);
        txtEnquiryNo.setEnabled(false);
        txtAmendmentDate.setEnabled(false);
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
        cbShowUsedQuotationsAlso.setEnabled(flag);
        txtVoucherDate.setEnabled(flag);
        if (DmShared.xNull(topControl.getMode()).equalsIgnoreCase("Edit")) {
            txtHVoucherType.setEnabled(false);
            txtHRCMApplicable.setEnabled(false);
        }
        flwAddIndent.clear();
        if (!DmShared.xNull(topControl.getMode()).equalsIgnoreCase("Browse")) {
            flwAddIndent.add(btnAddIndent);
        }
    }

    private void setRCM(String vCat) {
        boolean rcmFlag = false;
        //=====================================< Set RCM >=================================//
        if (DmShared.xNull(vCat).equalsIgnoreCase(new ProjectVoucherCategory().PURCHASE_ORDER_UNREGISTERED)
                || DmShared.xNull(vCat).equalsIgnoreCase(new ProjectVoucherCategory().SERVICE_ORDER_UNREGISTERED)) {
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

    private void setApproved(String docId, boolean isApprove) {
        try {
            if (DmShared.iNull(txtWTACode.getText()).longValue() != 0) {
                walkThrough.show(txtWTACode.getText());
            } else {
                SrvAsync.invokeRPC().setApproval(DmClient.getLoginUserName(), isApprove, isUnApprove, docId, new AsyncCallback<DmResult<String>>() {
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

    private void sendMail() {

        LinkedHashMap<String, FieldUDT_Mdl> hashMapFilter = new LinkedHashMap<>();
        hashMapFilter.put("docId", new FieldUDT_Mdl("PO No.", DmShared.xNull(txtOrderNo.getText()), DmShared.xNull(topControl.getDocId())));

        STClient.sendReportToEmail(DmShared.xNull(topControl.getDocId()), DmShared.xNull(defaultValue.getVoucherType().getSharedKeyFieldCode()), hashMapFilter, widgetName);
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

    private void getTempKeyCode() {

        SrvAsync.invokeRPC().getTempKeyCode(new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(String result) {
                fileManager.setTempKeyCode(result);
            }
        });
    }

    @Override
    public void onSelection(SelectionEvent<Item> event) {
        if (event.getSource() == manageFileMenu) {
            fileManager.show(topControl.getMode().trim().equalsIgnoreCase("New") ? "" : topControl.getDocId(), "1", isAttachFiles, isDeleteFiles);
        }
    }

    @Override
    public void action(String nature, Object object, DmCallBack callback) {
        nature = DmShared.xNull(nature).trim().toUpperCase();
        try {
            switch (nature) {
                case "ADD_PURCHASEORDER":
                    this.lstIndentCard = (ArrayList) object;
                    indentCardFlag = true;
                    topControl.btnClick("New");
                    break;
                case "ADD_QUOTATION":
                    this.quotationCard_Mdl = (KeyFieldExtended) object;
                    this.quotationCardCallBack = callback;
                    topControl.btnClick("New");
                    break;
                case "FOCUS_ON_ADD":
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            txtRefNo.setFocus(true);
                        }
                    });
                    break;
                case "ADD_ORDER_WITH_PENDING_INDENT":
                    openIndentWithDetail((List<PurchaseOrderDisplay_Mdl>) object, callback);
                    break;
//                case "ADD_ORDER_WITH_CONSIGNMENT_BILL":
//                    openWithConsignment((List<MultiRequisition_Model>) object);
//                    break;
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    public void openWithQuotation(String supplierCode) {
//        topControl.btnClick("New");
        DmHelper dmHelper = new DmHelper();
        dmHelper.addSort("recId", DmEnum.DmSortType.ASC);

        SrvAsync.invokeRPC().getQuotationDetail(quotationCard_Mdl, supplierCode, txtVoucherDate.getText(), voucherCategory, dmHelper, false, new AsyncCallback<DmResult<List<TxtHQuotationNo_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<TxtHQuotationNo_Mdl>> result) {
                if (result.isStatus()) {
                    txtHQuotationNo.setTag(result.getData().get(0).getSharedKeyFieldCode());
                    txtHQuotationNo.setText(result.getData().get(0).getSharedKeyFieldName());
                    fillQuotationDetail(true, result.getData().get(0));
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void getSupplierDetail(String supplierCode) {
        DmHelper dmHelper = new DmHelper();
        dmHelper.addSort("name", DmEnum.DmSortType.ASC);
        dmHelper.addSort("cityName", DmEnum.DmSortType.ASC);

        SrvAsync.invokeRPC().getSupplierDetail(supplierCode, voucherCategory, false, txtHVoucherType.getTag(), dmHelper, false, new AsyncCallback<DmResult<TxtHSupplierPT_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHSupplier).show();
            }

            @Override
            public void onSuccess(DmResult<TxtHSupplierPT_Mdl> result) {
                if (result.isStatus()) {
                    txtHSupplier.setTag(result.getData().getSharedKeyFieldCode());
                    txtHSupplier.setText(result.getData().getSharedKeyFieldName());
                    fillSupplierDetail(true, result.getData());
                    openWithQuotation(result.getData().getSharedKeyFieldCode());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHSupplier).show();
                }
            }
        });

    }

    private void fillAmendmentGridDetail(String amdOrdDocId) {
        SrvAsync.invokeRPC().fillAmendmentGridDetail(amdOrdDocId, new AsyncCallback<DmResult<List<GrdItemDetail_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdItemDetail_Mdl>> result) {
                if (result.isStatus()) {
                    List<GrdItemDetail_Mdl> grdItemDetail_Mdl = result.getData();
                    grdItemDetail.getGrid().getStore().clear();
                    grdItemDetail.getGrid().getStore().addAll(grdItemDetail_Mdl);
                    fillGrdItemDetail(grdItemDetail_Mdl);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
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
        paramHashMap.put("IS_SERVICE_AC_MANDATORY", false);
        paramHashMap.put("IS_TAX_AC_MANDATORY", false);

        return paramHashMap;

    }

    private void syncronizeLineService(boolean syncronizeLineHeader, boolean addServiceRow) {
        serviceDetailGrid.initialize(setServiveDefaultValue());

        //<editor-fold defaultstate="collapsed" desc="First Syncronize Line Service With Item Grid">
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
                if (DmShared.xNull(txtHQuotationNo.getTag()).trim().equals("")) {
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
                grdItemDetail_Mdl.setOrderQuantity(DmShared.xNull(serviceDetail_Mdl.getQuantity()));
                grdItemDetail_Mdl.setPurchaseUnitCode(serviceDetail_Mdl.getUnitCode());

                grdItemDetail_Mdl.setHsnCode(serviceDetail_Mdl.getHsnCode());
                grdItemDetail_Mdl.setHsnNo(serviceDetail_Mdl.getHsn());
                grdItemDetail_Mdl.setItemGSTNature(serviceDetail_Mdl.getItemGSTNature());
                grdItemDetail_Mdl.setItsBasedOn(serviceDetail_Mdl.getItsBasedOn());
                grdItemDetail_Mdl.setGstTypeCode(serviceDetail_Mdl.getGstTypeCode());
                grdItemDetail_Mdl.setGstTypeName(serviceDetail_Mdl.getGstTypeName());
                grdItemDetail_Mdl.setManualGstType(serviceDetail_Mdl.isManualGstType());

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
                grdItemDetail_Mdl.setItemValue(DmClient.vNull(serviceDetail_Mdl.getNetAmt(), "0.00"));
                //=================== ************* ===================//

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

    public void openIndentWithDetail(List<PurchaseOrderDisplay_Mdl> lstDetail, final DmCallBack callback) {
        String itemName, itemCode;

        topControl.btnClick("New");
        setDefaultValues();
//        List<GrdMedicineDetail_Mdl> tmpList = new ArrayList<>();
        List<GrdItemDetail_Mdl> tmpList = new ArrayList<>();
        GrdItemDetail_Mdl node;
        KeyFieldExtended nodeDetail;

        List<KeyFieldExtended> lstIndent = new ArrayList<>();

        for (int i = 0; i < lstDetail.size(); i++) {
            node = new GrdItemDetail_Mdl();

            PurchaseOrderDisplay_Mdl model = lstDetail.get(i);
            txtHSupplier.setText(DmShared.xNull(model.getLastOrderSupplier()));
            txtHSupplier.setTag(DmShared.xNull(model.getLastOrderSupplierCode()));
            itemName = DmShared.xNull(model.getItemName());
            itemCode = DmShared.xNull(model.getItemCode());
            node.setSharedKeyFieldCode(DmShared.xNull(itemCode));
            node.setSharedKeyFieldName(DmShared.xNull(itemName));
            node.setOrderQuantity(DmClient.vNull(model.getPendingOrderQty(), "0.00"));
            node.setSkuQty(DmClient.vNull(model.getPendingOrderQty(), "0.00"));
            node.setRate(DmClient.vNull(model.getLastOrderRate(), "0.00"));

            node.setPurchaseUnitCode(DmShared.xNull(model.getItemPurchaseunitCode()));
            node.setPurchaseUnitDecimalPlaces(DmShared.xNull(model.getUnitDecimalPlaces()));
            node.setSkuName(DmShared.xNull(model.getSkuName()));

            for (int j = 0; j < model.getGrdItemDetailList().size(); j++) {
                nodeDetail = new KeyFieldExtended();
                nodeDetail.setSharedKeyFieldCode(DmShared.xNull(model.getGrdItemDetailList().get(j).getIndentDocID()));
                nodeDetail.setSharedKeyFieldName(DmShared.xNull(model.getGrdItemDetailList().get(j).getIndentV_Sno()));
                nodeDetail.setSharedModifiedBy(DmShared.xNull(model.getLastOrderRate()));
                lstIndent.add(nodeDetail);
            }

            tmpList.add(node);
        }
        fillItemDetails(lstIndent);
        if (callback != null) {
            DmResult<Object> dmrTemp = new DmResult<>();
            dmrTemp.setStatus(true);
            callback.result(dmrTemp);
        }
    }

    private void fillItemDetails(List<KeyFieldExtended> keyField, final String guiCodeFrom, final String copyFromDocId, final boolean isGUICopyRequired) {
        SrvAsync.invokeRPC().getItemDetail(DmShared.xNull(txtHVoucherType.getTag()), txtHQuotationNo.getTag(), voucherCategory, location, txtVoucherDate.getText(), defaultValue.getIsSiteStateUT(), keyField, new AsyncCallback<DmResult<PurchaseOrder_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<PurchaseOrder_Mdl> result) {
                if (result.isStatus()) {
                    PurchaseOrder_Mdl entry_Mdl = result.getData();
                    List<GrdItemDetail_Mdl> grdIndentDetail_Mdl = entry_Mdl.getGrdItemDetail();
                    grdItemDetail.getGrid().getStore().clear();
                    grdItemDetail.getGrid().getStore().addAll(grdIndentDetail_Mdl);
                    grdItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
                    fillGrdItemDetail(grdIndentDetail_Mdl);
//                    if (isGUICopyRequired) {
//                        copyGUI(guiCodeFrom, copyFromDocId);
//                    }
                }
            }
        });
    }
}
