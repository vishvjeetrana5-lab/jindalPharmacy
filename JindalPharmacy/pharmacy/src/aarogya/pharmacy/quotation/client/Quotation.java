package aarogya.pharmacy.quotation.client;

import aarogya.pharmacy.quotation.shared.*;
import aarogya.pharmacy.indentSearch.client.IndentSearch;
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
import java.util.List;
import aarogya.pharmacy.quotation.shared.Quotation_Srv;
import aarogya.pharmacy.quotation.shared.Quotation_SrvAsync;
import aarogya.projectMenu.self.shared.ProjectRuleCode;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.modifyReason.client.ModifyReason;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.stStatic.client.STClient;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.menu.Item;
import dataman.dmBase.dmFileManager.shared.DmIFile_Mdl;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmToolkit.dmFileManager.client.DmFileManager;
import dataman.dmToolkit.dmGUI.client.DmGUI;
import dataman.dmToolkit.dmGUI.shared.DmGUIControl_Mdl;
import dataman.dmToolkit.dmGUI.shared.DmGUI_Mdl;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Mdl;
import java.util.ArrayList;
import java.util.HashMap;

public final class Quotation extends Quotation_Dsg implements BlurHandler, AttachEvent.Handler, DmRootEvents, SelectionHandler<Item> {

    private ServiceInvoker<Quotation_SrvAsync> srvAsync;
    protected DefaultValue_Mdl defaultValue;
    final QuotationGridEdit<GrdIndentDetail_Mdl, GridItemDetail_PrpAcs> itemDetailGridEdit = new QuotationGridEdit(gridItemDetail);
    protected final OverHeadLineHandler<GrdItemDetail_Mdl> ohlHandler = new OverHeadLineHandler(overHead, 300, 110);
    private final DmFileManager manageFile = new DmFileManager(ProjectRuleCode.QUOTATION_ENTRY.getRuleCode());
    final IndentSearch indentSearch = new IndentSearch();
    final ModifyReason modifyReason = new ModifyReason();
    private ModifyReason_Mdl modifyReasonMdl;
    private boolean isAttachFile, isDeleteFile = false;
    private String guiTemplateCode = "", selectedQuotationType = "";
    private int errrorNo;
    private String path = "", fileName = "";
    protected String location = "", supplierStateCode = "";

    private void topControlEvents(UserMenu menuPermission) {

        topControl.setTitle(this.asWidget().getTitle());
        topControl.setRefreshRequired(false);

        for (String controlPermission : menuPermission.getControlPermission()) {
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
                calculate();
            }

            @Override
            public String getVoucherType() {
                return txtHQuotationType.getTag();
            }

            @Override
            public String getVoucherDate() {
                return txtQuotationDate.getText();
            }

            @Override
            public void endCalculate() {

            }
        });

        ohlHandler.addCallback(new OverHeadLineHandler.Callback<GrdItemDetail_Mdl>() {

            @Override
            public void initialized() {
                gridItemDetail.getGrid().getStore().clear();
                gridItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
                overHead.initialize();
            }

            @Override
            public String getVoucherType() {
                return txtHQuotationType.getTag();
            }

            @Override
            public String getVoucherDate() {
                return txtQuotationDate.getText();
            }

            @Override
            public void endCalculate() {
            }

            @Override
            public void endSyncToOH() {
            }

            @Override
            public List<GrdItemDetail_Mdl> getRows() {
                return gridItemDetail.getGrid().getStore().getAll();
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

        topControl.setUserPermission(menuPermission, new VoucherPermissionHandler(DmClient.getVoucherPermission(new String[]{new ProjectVoucherCategory().PHARMACY_QUOTATION})) {
            @Override
            public String getVoucherType() {
                return txtHQuotationType.getTag();
            }

            @Override
            public String getVoucherDate() {
                return txtQuotationDate.getText();
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
                txtHSupplier.setFocus(true);
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                manageDisplay(true);
                gridItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
                txtHSupplier.setFocus(true);
            }
        });

        topControl.addDeleteHandeller(new DmTopControl.eDelete() {
            @Override
            public void deleteClicked() {
//                if (DmShared.xNull(userControl.getTextBlog(approvedByBlog)).equalsIgnoreCase("")) {
                new DmConfirmMessageBox("Are you sure? You want to delete this record.", new DialogHideEvent.DialogHideHandler() {

                    @Override

                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            topControlDelete(topControl.getDocId());
                        }
                    }
                }).show();
//                }
//                else {
//                    topControl.btnClick("");
//                    new DmAlertMessageBox("Record is approved. You can't delete this record.", (DmTextBox) null).show();
//                }
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
                saveValidation();
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(Quotation.this);
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

    private void textHelpEvents() {
        indentSearch.addCallBack(new IndentSearch.CallBack() {
            @Override
            public void ok(List<KeyFieldExtended> listKF) {
                txtHEnquiryNo.setTag("");
                txtHEnquiryNo.setText("");
                resetEnquiry(true);
                fillItemDetails(listKF);
            }

            @Override
            public void close() {
                txtEnquiryRefNo.setFocus(true);
            }
        });

        txtHQuotationType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                selectedQuotationType = DmShared.xNull(txtHQuotationType.getTag());
                srvAsync.invokeRPC(blnCallBackWait).getTxtHVoucher(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHQuotationType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHQuotationType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHQuotationType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (!DmShared.xNull(selectedQuotationType).equalsIgnoreCase(DmShared.xNull(txtHQuotationType.getTag()))) {
                    ohlHandler.initialize();
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
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSupplier(txtHQuotationType.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHSupplier_Mdl>>>() {

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
            public void getSelectedRow(boolean blnStatus, TxtHSupplier_Mdl t1Row) {

                if (blnStatus) {
                    supplierStateCode = "";
                    supplierStateCode = DmShared.xNull(t1Row.getStateCode());
                    setLocation();
                    if (!DmShared.xNull(txtHEnquiryNo.getTag()).trim().equalsIgnoreCase("")) {
                        txtHEnquiryNo.setTag("");
                        txtHEnquiryNo.setText("");
                        resetEnquiry(true);

                    }
                }
            }
        });

        txtHEnquiryNo.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHEnquiry_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHEnquiryNo(DmShared.xNull(txtHSupplier.getTag()), DmShared.dNull(txtVendorQNDate.getText()).trim().equals("") ? DmShared.dNull(txtQuotationDate.getText()) : DmShared.dNull(txtVendorQNDate.getText()), DmShared.xNull(topControl.getMode()).equalsIgnoreCase("New") ? "" : topControl.getDocId(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHEnquiry_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHEnquiryNo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHEnquiry_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHEnquiryNo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHEnquiryNo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHEnquiry_Mdl t1Row) {
                resetEnquiry(true);
                if (blnStatus && t1Row != null) {
                    resetEnquiry(false);
                    txtEnquiryDate.setText(DmShared.dNull(t1Row.getEnquiryDate()));
                    txtEnquiryRefNo.setText(DmShared.xNull(t1Row.getEnquiryRefNo()));
                    fillItemDetails(null);
                }
            }
        });
    }

    private void gridEvents() {
        itemDetailGridEdit.addCallBack(new QuotationGridEdit.CallBack() {

            @Override
            public void onOk(GrdItemDetail_Mdl opdService) {
                gridItemDetail.commitChanges();
                calculate();
                ohlHandler.syncronizeLineHeader();
            }

            @Override
            public void onDelete() {
                gridItemDetail.commitChanges();
                calculate();
                ohlHandler.syncronizeLineHeader();
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
        if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {
            itemDetailGridEdit.show(currentRowIndex, false, false, false, this);
        } else {
            itemDetailGridEdit.show(currentRowIndex, true, true, true, this);
        }
    }

    private void resetEnquiry(boolean isVisible) {
        txtEnquiryDate.setEnabled(isVisible);
        txtEnquiryRefNo.setEnabled(isVisible);
        txtEnquiryDate.setText("");
        txtEnquiryRefNo.setText("");
        gridItemDetail.getGrid().getStore().clear();
        gridItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
    }

    private void prnRecord(boolean printToPrinter) {
        HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
        hashMapFilter.put("docId", new FieldUDT_Mdl("Quotation No.", DmShared.xNull(txtQuotationNo.getText()), DmShared.xNull(topControl.getDocId())));
        new OpenSourceReport(DmShared.xNull(txtHQuotationType.getTag()), "", widgetName, widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
            @Override
            public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                DmClient.localSettingPrintMap(reportProperty, moduleName, widgetName, "Print");
                return reportProperty;
            }
        }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));

    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtQuotationDate) {
            txtQuotationDate.setText(DmClient.returnDate(txtQuotationDate.getText()));
            if (!DmShared.dNull(txtQuotationDate.getText()).trim().equals("")) {
                if (STClient.isFutureDate(txtQuotationDate.getText())) {
                    txtQuotationDate.setText("");
                    new DmAlertMessageBox("Quotation Date should be less than or equal to current date.", txtQuotationDate).show();
                }
            }
        } else if (event.getSource() == txtVendorQNDate) {
            txtVendorQNDate.setText(DmClient.returnDate(txtVendorQNDate.getText()));
        } else if (event.getSource() == txtEnquiryDate) {
            txtEnquiryDate.setText(DmClient.returnDate(txtEnquiryDate.getText()));
        }
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

    private void manageEvent() {
        txtQuotationDate.addBlurHandler(this);
        txtVendorQNDate.addBlurHandler(this);
        txtEnquiryDate.addBlurHandler(this);
        mnuManageFile.addSelectionHandler(this);
        mnuDownloadCsv.addSelectionHandler(this);
        btnAddIndents.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (DmShared.dNull(txtVendorQNDate.getText()).equalsIgnoreCase("")) {
                    indentSearch.show("", txtQuotationDate.getText(), new ProjectVoucherCategory().PHARMACY_QUOTATION);
                } else {
                    indentSearch.show("", txtVendorQNDate.getText(), new ProjectVoucherCategory().PHARMACY_QUOTATION);
                }
            }
        });
    }

    public Quotation(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
//        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        srvAsync = new ServiceInvoker<Quotation_SrvAsync>(GWT.create(Quotation_Srv.class), Quotation.class.getName());
        this.asWidget().setTitle(widgetName);
        flwForm.addAttachHandler(this);
        topControlEvents(menuPermission);
        manageEvent();
        textHelpEvents();
        findEvents();
        gridEvents();
        overHead.setBannerVisible(false);
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
                    swi.setWidget(Quotation.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
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

    private void showModifyReason() {
        modifyReason.show(topControl.getMode(), topControl.getDocId());
        modifyReason.addCallBack(new ModifyReason.CallBack() {

            @Override
            public void onOk(ModifyReason_Mdl modifyReason) {
                Quotation.this.modifyReasonMdl = modifyReason;
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
            topControlSaveError(e.getErrorNo(), e.getErrorSourceCode(), e.getMessage());
        }
    }

    private Quotation_Mdl chkValidation() {
        Quotation_Mdl mrTemp;
        mrTemp = topControlSaveInitialize();
        Quotation_Vld.chkSave(mrTemp);
        return mrTemp;
    }

    private Quotation_Mdl topControlSaveInitialize() {
        Quotation_Mdl mrTemp = new Quotation_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
            mrTemp.getObjRecId().setRecIdValue(DmShared.xNull(txtQuotationNo.getText()));
            mrTemp.getObjRecId().setCounter(DmShared.iNull(txtQuotationNo.getTag()).longValue());
            mrTemp.getObjRecId().setPrefix(DmShared.xNull(txtQuotationNo.getTag2nd()));
        }
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtQuotationNo.getText()));
        mrTemp.setQuotationDate(DmShared.dNull(txtQuotationDate.getText()));
        mrTemp.setVoucherTypeCode(DmShared.xNull(txtHQuotationType.getTag()));
        mrTemp.setVendorTag(DmShared.xNull(txtHSupplier.getTag()));
        mrTemp.setVendorRefNo(DmShared.xNull(txtVendorRefNo.getText()));
        mrTemp.setVendorRefDate(DmShared.dNull(txtVendorQNDate.getText()));
        mrTemp.setLocationCode(DmShared.iNull(location).toString());
        mrTemp.setEnquiryTag(DmShared.xNull(txtHEnquiryNo.getTag()));
        mrTemp.setEnquiryRefDate(DmShared.dNull(txtEnquiryDate.getText()));
        mrTemp.setEnquiryRefNo(DmShared.xNull(txtEnquiryRefNo.getText()));
        mrTemp.getListItemDetail().clear();
        mrTemp.getListItemDetail().addAll(gridItemDetail.getGrid().getStore().getAll());
        mrTemp.setGuiMdl(gui.getGUI_Mdl());
        mrTemp.setGuiConfigCode(gui.getConfigCode());
        mrTemp.getOverHeadList().clear();
        mrTemp.getOverHeadList().addAll(overHead.getOverHeadList());
        if (modifyReasonMdl != null) {
            mrTemp.setModifyReasonMdl(modifyReasonMdl);
        }

        return mrTemp;
    }

    private void topControlSave() {
        Quotation_Mdl mrTemp;
        try {
            ohlHandler.syncronizeLineHeader();
            defaultValue.setNewVoucherTypeCode(txtHQuotationType.getTag());
            defaultValue.setNewVoucherTypeName(txtHQuotationType.getText());
            defaultValue.setNewVoucherDate(txtQuotationDate.getText());
            defaultValue.setNewGuiTemplateCode(guiTemplateCode);
            calculate();
            mrTemp = chkValidation();
            OverHead_Shared.chkGrid(mrTemp.getOverHeadList());
            OverHeadLine_Shared.chkGrid(mrTemp.getListItemDetail());
            srvAsync.invokeRPC().saveRecord(mrTemp, DmClient.getVoucherPrefix(), defaultValue.isIsCommonPriceList(), defaultValue.isUpdatePriceListFromQuotation(), new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHQuotationType).show();
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

    private void topControlSaveError(int errorNo, String errorMessage) {
        topControlSaveError(errorNo, "", errorMessage);
    }

    private void topControlSaveError(int errorNo, final String gridRowIndex, String errorMessage) {
        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtHQuotationType).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtHSupplier).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtQuotationDate).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtVendorRefNo).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtVendorQNDate).show();
                break;
            case 600:
                new DmAlertMessageBox(errorMessage, new HideEvent.HideHandler() {
                    @Override
                    public void onHide(HideEvent event) {
                        ((CellSelectionModel) gridItemDetail.getGrid().getSelectionModel()).selectCell(DmShared.iNull(gridRowIndex).intValue(), 1);
                    }
                }).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHSupplier).show();
                break;
        }
    }

    private void topControlMoveRec(String strKey) {
        ohlHandler.setDefault();
        defaultValue.setIsContinuousNewMode(false);
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<Quotation_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<Quotation_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<Quotation_Mdl> result) {
        clear();
        if (result.isStatus()) {
            Quotation_Mdl mrTemp = result.getData();

            txtQuotationDate.setEnabled(false);
            flwAddIndent.clear();
            flwImport.clear();
            topControl.setDocId(mrTemp.getSharedKeyFieldCode());
            txtQuotationNo.setText(mrTemp.getSharedKeyFieldName());
            txtQuotationNo.setTag(DmShared.iNull(mrTemp.getObjRecId().getCounter()).toString());
            txtQuotationNo.setTag2nd(mrTemp.getObjRecId().getPrefix());
            txtQuotationDate.setText(mrTemp.getQuotationDate());
            txtHQuotationType.setTag(mrTemp.getVoucherTypeCode());
            txtHQuotationType.setText(mrTemp.getVoucherTypeName());
            txtHSupplier.setTag(mrTemp.getVendorTag());
            txtHSupplier.setTag2nd(mrTemp.getVendorTag());
            txtHSupplier.setText(mrTemp.getVendorText());
            txtVendorRefNo.setText(mrTemp.getVendorRefNo());
            txtVendorQNDate.setText(mrTemp.getVendorRefDate());
            txtHEnquiryNo.setTag(mrTemp.getEnquiryTag());
            txtHEnquiryNo.setText(mrTemp.getEnquiryText());
            txtEnquiryDate.setText(mrTemp.getEnquiryRefDate());
            txtEnquiryRefNo.setText(mrTemp.getEnquiryRefNo());

            setLocation();
            userControl.setTextBlog(preparedByBlog, mrTemp.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, mrTemp.getSharedModifiedBy());

            gridItemDetail.getGrid().getStore().clear();
            gridItemDetail.getGrid().getStore().addAll(mrTemp.getListItemDetail());

            overHead.setOverHeadList(mrTemp.getOverHeadList(), mrTemp.getGrossAmount());
            ohlHandler.setDefault();
            gui.initialize(mrTemp.getGuiMdl());
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }

        topControl.setDisplay(true);
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

    private void fillItemDetails(List<KeyFieldExtended> listKF) {
        srvAsync.invokeRPC().getItemDetail(DmShared.xNull(txtHEnquiryNo.getTag()), listKF, new AsyncCallback<DmResult<List<GrdItemDetail_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdItemDetail_Mdl>> result) {
                if (result.isStatus()) {
                    if (result.isStatus()) {
                        gridItemDetail.getGrid().getStore().clear();
                        gridItemDetail.getGrid().getStore().addAll(result.getData());
                        gridItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
                        txtEnquiryRefNo.setFocus(true);
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            }
        });
    }

    private void manageDisplay(boolean flag) {
        txtQuotationNo.setEnabled(false);
        if (topControl.getMode().equalsIgnoreCase("Edit")) {
            txtHQuotationType.setEnabled(false);
            txtQuotationDate.setEnabled(false);
            if (DmShared.xNull(txtHEnquiryNo.getTag()).trim().equals("")) {
                txtEnquiryRefNo.setEnabled(true);
                txtEnquiryDate.setEnabled(true);
            } else {
                txtEnquiryRefNo.setEnabled(false);
                txtEnquiryDate.setEnabled(false);
            }
        }
        flwAddIndent.clear();
        flwImport.clear();
        if (flag) {
            flwAddIndent.add(btnAddIndents);
            flwImport.add(btnImport);
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
        txtQuotationDate.setText(defaultValue.getIsContinuousNewMode() ? DmShared.dNull(defaultValue.getNewVoucherDate()) : DmShared.dNull(defaultValue.getCurrentDate()));
        txtHQuotationType.setTag(defaultValue.getIsContinuousNewMode() ? defaultValue.getNewVoucherTypeCode() : defaultValue.getVoucherTypeCode());
        txtHQuotationType.setText(defaultValue.getIsContinuousNewMode() ? defaultValue.getNewVoucherTypeName() : defaultValue.getVoucherTypeName());
        guiTemplateCode = defaultValue.getIsContinuousNewMode() ? defaultValue.getNewGuiTemplateCode() : defaultValue.getGuiTemplateCode();
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
        location = "";
        guiTemplateCode = "";
        selectedQuotationType = "";
        overHead.clear();
        gui.clear();
        gridItemDetail.getGrid().getStore().clear();
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");

    }

    private void calculate() {
        double netAmount = 0;
        for (GrdItemDetail_Mdl detail_Mdl : gridItemDetail.getGrid().getStore().getAll()) {
            if (!DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                netAmount += DmShared.vNull(detail_Mdl.getAmount());
            }
        }
        gridItemDetail.commitChanges();
        overHead.calculate(DmClient.vNull(netAmount, "0.00"));
    }

    @Override
    public void onSelection(SelectionEvent<Item> event) {
        if (event.getSource().equals(mnuManageFile)) {
            if (!topControl.getMode().equalsIgnoreCase("New")) {
                manageFile.show(topControl.getMode().equalsIgnoreCase("New") ? "" : topControl.getDocId(), "1", isAttachFile, isDeleteFile);
            }
        } else if (event.getSource().equals(mnuDownloadCsv)) {
            new DmConfirmMessageBox("Are you sure? You want to download Csv.", new DialogHideEvent.DialogHideHandler() {

                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                        exportCSV(topControl.getDocId());
                    }
                }
            }).show();
        }
    }

    @Override
    public void notifyForUpload(String notifier) {
    }

    @Override
    public void processAfterUploaded(DmIFile_Mdl file) {
        lblFileName.setText(file.getFileName());
        chooseFile(file.getServerDirectory(), file.getServerFileName());
    }

    private void chooseFile(String ServerDirectory, String serverFileName) {
        srvAsync.invokeRPC().getListFromCSV(ServerDirectory, serverFileName, location, new AsyncCallback<DmResult<Quotation_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<Quotation_Mdl> result) {
                if (result.isStatus()) {
                    gridItemDetail.getGrid().getStore().clear();
                    gridItemDetail.getGrid().getStore().addAll(result.getData().getListItemDetail());
                    fillGrdItemDetail(result.getData().getListItemDetail());
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
                gridItemDetail.getGrid().getStore().get(i).setAmount(DmClient.vNull(DmShared.vNull(gridItemDetail.getGrid().getStore().get(i).getQuantity()).doubleValue() * DmShared.vNull(gridItemDetail.getGrid().getStore().get(i).getRate()).doubleValue(), "0.00"));

                ohlHandler.setITS(detail_Mdl.getOverHeadITSList(), i);
                gridItemDetail.getGrid().getStore().get(i).setOHLList(ohlHandler.getOverHeadList(i));
                OverHeadLine_Mdl oHLMdlA_DisAftTax = OverHeadLine_Shared.getOHByUniqueId("DS", gridItemDetail.getGrid().getStore().get(i).getOHLList());
                oHLMdlA_DisAftTax.setAtRate(DmClient.vNull(detail_Mdl.getDiscountper(), "0.00"));
                ohlHandler.calculate(gridItemDetail.getGrid().getStore().get(i).getAmount(), i);

            }
        }

        gridItemDetail.commitChanges();
        calculate();
        ohlHandler.syncronizeLineHeader();

    }

    private void exportCSV(String docId) {
        srvAsync.invokeRPC().exportCSV(docId, new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus() && result.getData() != null) {
                    startDownloading(result.getData(), result.getValue1());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtQuotationNo).show();
                }
            }
        });
    }

    private void startDownloading(String fileName, String filePath) {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("u", DmRequestBuilder.getSessionId());
            params.put("f", fileName);
            params.put("p", filePath);
            Window.open(DmClient.concatURL("dataman/dmToolkit/dmFileUploadButton/servlets/DmFileDownloader_Srvlt", params), "", "");
        } catch (Exception ex) {
        }
    }

    private void setLocation() {
        //=====================================< Set Location >=================================//
        if (DmShared.iNull(defaultValue.getSiteStateCode()).intValue() != DmShared.iNull(supplierStateCode).intValue()) {
            location = String.valueOf(DmEnum.DmLocation.InterState.getId());
        } else {
            if (DmShared.iNull(defaultValue.getIsSiteStateUT()).intValue() == 1) {
                location = String.valueOf(DmEnum.DmLocation.UnionTerritory.getId());
            } else {
                location = String.valueOf(DmEnum.DmLocation.IntraState.getId());
            }
        }
    }
}
