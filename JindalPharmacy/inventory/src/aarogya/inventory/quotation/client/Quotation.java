package aarogya.inventory.quotation.client;

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
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
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
import java.util.List;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.menu.Item;
import dataman.dmToolkit.dmFileManager.client.DmFileManager;
import dataman.dmToolkit.dmGUI.client.DmGUI;
import dataman.dmToolkit.dmGUI.shared.DmGUIControl_Mdl;
import dataman.dmToolkit.dmGUI.shared.DmGUI_Mdl;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Mdl;
import java.util.ArrayList;
import java.util.HashMap;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.projectMenu.self.shared.pmInventory;
import aarogya.inventory.indentSearch.client.IndentSearch;
import aarogya.inventory.quotation.shared.DefaultValue_Mdl;
import aarogya.inventory.quotation.shared.Find_Mdl;
import aarogya.inventory.quotation.shared.GrdItemDetail_Mdl;
import aarogya.inventory.quotation.shared.Quotation_Mdl;
import aarogya.inventory.quotation.shared.Quotation_Srv;
import aarogya.inventory.quotation.shared.Quotation_SrvAsync;
import aarogya.inventory.quotation.shared.Quotation_Vld;
import aarogya.inventory.quotation.shared.TxtHEnquiry_Mdl;
import aarogya.inventory.quotation.shared.TxtHSupplier_Mdl;
import static aarogya.projectMenu.self.shared.ProjectRuleCode.QUOTATION;
import aarogya.selfToolkit.modifyReason.client.ModifyReason;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.serviceGrid.client.ServiceGrid;
import aarogya.selfToolkit.serviceGrid.shared.ServiceDetail_Mdl;
import aarogya.selfToolkit.stStatic.client.STClient;
import com.google.gwt.user.client.Window;
import dataman.dmBase.dmFileManager.shared.DmIFile_Mdl;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmToolkit.manageSite.client.ManageSite;

public final class Quotation extends Quotation_Dsg implements BlurHandler, AttachEvent.Handler, DmRootEvents, SelectionHandler<Item> {

    private ServiceInvoker<Quotation_SrvAsync> srvAsync;
    protected DefaultValue_Mdl defaultValue;
    final QuotationGridEdit<GrdItemDetail_Mdl, GridItemDetail_PrpAcs> itemDetailGridEdit = new QuotationGridEdit(gridItemDetail, this);
    final IndentSearch indentSearch = new IndentSearch();
    final ModifyReason modifyReason = new ModifyReason();
    private ModifyReason_Mdl modifyReasonMdl;
    private String guiTemplateCode = "", selectedQuotationType = "", selectedSupplierCode = "",
            partyType, supplierGstin, supplierGstinDate, partyTypeForEntry;
    List<KeyFieldExtended> lstIndentCard = new ArrayList<KeyFieldExtended>();
    private KeyFieldExtended enquiryCard_Mdl = null;
    private DmCallBack enquiryCardCallBack = null;
    private boolean isAttachFiles = false, isDeleteFiles = false;
    final DmFileManager fileManager = new DmFileManager(QUOTATION.getRuleCode());
    public String location, voucherCategory;
    final ManageSite manageSite = new ManageSite();

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
            public void removeCustom(DmTopControl topControl) {
            }

            @Override
            public void guiDataCopied(DmGUI_Mdl gui_Mdl, List<String> listCustomWidgetToCopy, String copyFromDocId, HashMap<String, String> param) {
            }

            @Override
            public Object addCustom(DmGUIControl_Mdl dmGUIControl_Mdl, FlowPanel flwTr, Object tabIndexAfter) {
                return null;
            }
        });

        for (String controlPermission : menuPermission.getControlPermission()) {
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(attachFileBlog)) {
                isAttachFiles = true;
            }
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(deleteFileBlog)) {
                isDeleteFiles = true;
            }
        }

        topControl.setUserPermission(menuPermission, new VoucherPermissionHandler(DmClient.getVoucherPermission(new String[]{new ProjectVoucherCategory().QUOTATION, new ProjectVoucherCategory().QUOTATION_UNREGISTERED})) {
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
                serviceDetailGrid.getServiceGrid().getGrid().getStore().add(new ServiceDetail_Mdl());
                serviceDetailGrid.initialize(setServiveDefaultValue());
                syncronizeLineService(false, true);
                txtHSupplier.setFocus(true);
            }
        });

        topControl.addDeleteHandeller(new DmTopControl.eDelete() {
            @Override
            public void deleteClicked() {
                new DmConfirmMessageBox("Are you sure? You want to delete this record.", new DialogHideEvent.DialogHideHandler() {

                    @Override

                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            topControlDelete(topControl.getDocId());
                        }
                    }
                }).show();

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
                    clearGrid();
                    selectedQuotationType = DmShared.xNull(txtHQuotationType.getTag());
                    if (blnStatus && t1Row != null) {
                        guiTemplateCode = t1Row.getSharedPreparedBy();
                        voucherCategory = DmShared.xNull(t1Row.getSharedModifiedBy());
                    } else {
                        guiTemplateCode = "";
                        voucherCategory = "";
                    }
                    gui.initializeViaTemplateCode();
                }
            }
        });

        txtHSupplier.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHSupplier_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSupplier(voucherCategory, false, txtHQuotationType.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHSupplier_Mdl>>>() {

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
                fillSupplierDetail(blnStatus, t1Row);
            }
        });

        txtHEnquiryNo.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHEnquiry_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHEnquiryNo(txtHSupplier.getTag(), DmShared.dNull(txtVendorQNDate.getText()).trim().equals("") ? DmShared.dNull(txtQuotationDate.getText()) : DmShared.dNull(txtVendorQNDate.getText()), DmShared.xNull(topControl.getMode()).equalsIgnoreCase("New") ? "" : topControl.getDocId(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHEnquiry_Mdl>>>() {

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
                    fillQuotationDetail(t1Row);
                }
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
    }

    private void fillSupplierDetail(boolean blnStatus, TxtHSupplier_Mdl t1Row) {
        if (topControl.getMode().equalsIgnoreCase("New")
                && !DmShared.xNull(selectedSupplierCode).equalsIgnoreCase(DmShared.xNull(txtHSupplier.getTag()))) {
            selectedSupplierCode = DmShared.xNull(txtHSupplier.getTag());
        }

        if (blnStatus && t1Row != null) {
            txtHSupplier.setTag2nd(DmShared.xNull(t1Row.getStateCode()));
            supplierGstin = DmShared.xNull(t1Row.getGstin());
            supplierGstinDate = DmShared.dNull(t1Row.getGstinDate());
            partyType = DmShared.xNull(t1Row.getPartyType());
            resetGstDetails();
            if (!DmShared.xNull(txtHEnquiryNo.getTag()).trim().equalsIgnoreCase("")) {
                txtHEnquiryNo.setTag("");
                txtHEnquiryNo.setText("");
                resetEnquiry(true);
            }
        }
        clearGrid();
    }

    private void fillQuotationDetail(TxtHEnquiry_Mdl t1Row) {
        resetEnquiry(false);
        txtEnquiryDate.setText(DmShared.dNull(t1Row.getEnquiryDate()));
        txtEnquiryRefNo.setText(DmShared.xNull(t1Row.getEnquiryRefNo()));
        fillItemDetails(null);
    }

    private void resetGstDetails() {

        //=====================================< Set Party Type For Entry >=================================//
        if (!DmShared.dNull(txtQuotationDate.getText()).trim().equals("")
                && !DmShared.dNull(supplierGstinDate).trim().equals("")
                && DmShared.convertDate(txtQuotationDate.getText()).getTime() < DmShared.convertDate(supplierGstinDate).getTime()) {
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

    private void clearGrid() {
        serviceDetailGrid.getServiceGrid().getGrid().getStore().clear();
        serviceDetailGrid.getServiceGrid().getGrid().getStore().add(new ServiceDetail_Mdl());
        serviceDetailGrid.initialize(setServiveDefaultValue());

        gridItemDetail.getGrid().getStore().clear();
        gridItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
        syncronizeLineService(false, true);
        calculate();
    }

    private void gridEvents() {
        itemDetailGridEdit.addCallBack(new QuotationGridEdit.CallBack() {

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
            itemDetailGridEdit.show(currentRowIndex, false, false, false);
        } else {
            itemDetailGridEdit.show(currentRowIndex, true, true, true);
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
        new OpenSourceReport(DmShared.xNull(txtHQuotationType.getTag()), "", new pmInventory().QUOTATION.caption, widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
            @Override
            public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                DmClient.localSettingPrintMap(reportProperty, moduleName, widgetName, "Print");
                return reportProperty;
            }
        }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));

    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtVendorQNDate) {
            txtVendorQNDate.setText(DmClient.returnDate(txtVendorQNDate.getText()));
            if (!DmShared.dNull(txtVendorQNDate.getText()).trim().equals("")) {
                if (STClient.isFutureDate(txtVendorQNDate.getText())) {
                    txtVendorQNDate.setText("");
                    new DmAlertMessageBox("Supplier Ref. Date should be less than or equal to current date.", txtVendorQNDate).show();
                }
            }
        } else if (event.getSource() == txtQuotationDate) {
            txtQuotationDate.setText(DmClient.returnDate(txtQuotationDate.getText()));
            if (!DmShared.dNull(txtQuotationDate.getText()).trim().equals("")) {
                if (STClient.isFutureDate(txtQuotationDate.getText())) {
                    txtQuotationDate.setText("");
                    new DmAlertMessageBox("Quotation Date should be less than or equal to current date.", txtQuotationDate).show();
                }
            }
        } else if (event.getSource() == txtEnquiryDate) {
            txtEnquiryDate.setText(DmClient.returnDate(txtEnquiryDate.getText()));
            if (!DmShared.dNull(txtEnquiryDate.getText()).trim().equals("")) {
                if (STClient.isFutureDate(txtEnquiryDate.getText())) {
                    txtEnquiryDate.setText("");
                    new DmAlertMessageBox("Enquiry Ref. Date should be less than or equal to Quotation date.", txtEnquiryDate).show();
                }
            }
        } else if (event.getSource() == txtValidUptoDate) {
            txtValidUptoDate.setText(DmClient.returnDate(txtValidUptoDate.getText()));
            if (!DmShared.dNull(txtValidUptoDate.getText()).trim().equals("")) {
                if (DmShared.convertDate(txtValidUptoDate.getText()).getTime() < DmShared.convertDate(txtQuotationDate.getText()).getTime()) {
                    txtValidUptoDate.setText("");
                    new DmAlertMessageBox("Valid Upto should be greater than or equal to Quotation date.", txtValidUptoDate).show();
                }
            }
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
        txtValidUptoDate.addBlurHandler(this);
        manageFileMenu.addSelectionHandler(this);
        mnuDownloadCsv.addSelectionHandler(this);
        btnAddIndents.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                indentSearch.show(DmShared.xNull(txtHQuotationType.getTag()), null, DmShared.dNull(txtVendorQNDate.getText()).trim().equals("") ? DmShared.dNull(txtQuotationDate.getText()) : DmShared.dNull(txtVendorQNDate.getText()), DmShared.xNull(voucherCategory), DmShared.xNull(txtHSupplier.getTag()));
            }
        });
    }

    public Quotation(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
       // menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        srvAsync = new ServiceInvoker<Quotation_SrvAsync>(GWT.create(Quotation_Srv.class), Quotation.class.getName());
        this.asWidget().setTitle(widgetName);
        flwForm.addAttachHandler(this);
        topControlEvents(menuPermission);
        manageEvent();
        textHelpEvents();
        findEvents();
        gridEvents();
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
                    guiTemplateCode = defaultValue.getVoucherType().getGuiTemplateCode();
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
        mrTemp.setTempKeyCode(fileManager.getTempKeyCode());
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtQuotationNo.getText()));
        mrTemp.setQuotationDate(DmShared.dNull(txtQuotationDate.getText()));
        mrTemp.setVoucherTypeCode(DmShared.xNull(txtHQuotationType.getTag()));
        mrTemp.setVendorTag(DmShared.xNull(txtHSupplier.getTag()));
        mrTemp.setVendorRefNo(DmShared.xNull(txtVendorRefNo.getText()));
        mrTemp.setVendorRefDate(DmShared.dNull(txtVendorQNDate.getText()));
        mrTemp.setValidUptoDate(DmShared.dNull(txtValidUptoDate.getText()));
        mrTemp.setEnquiryTag(DmShared.xNull(txtHEnquiryNo.getTag()));
        mrTemp.setEnquiryRefDate(DmShared.dNull(txtEnquiryDate.getText()));
        mrTemp.setEnquiryRefNo(DmShared.xNull(txtEnquiryRefNo.getText()));

        mrTemp.setSupplierTag(DmShared.xNull(txtHSupplier.getTag()));
        mrTemp.setSupplierGstin(DmShared.xNull(supplierGstin));
        mrTemp.setSupplierGstinDate(DmShared.xNull(supplierGstinDate));
        mrTemp.setSupplierStateCode(DmShared.xNull(txtHSupplier.getTag2nd()));
        mrTemp.setPlaceOfSupply(DmShared.xNull(defaultValue.getSiteStateCode()).trim());

        mrTemp.setRcmApplicable(txtHRCMApplicable.getTag());
        mrTemp.setPartyTypeForEntry(partyTypeForEntry);
        mrTemp.setPartyType(partyType);
        mrTemp.setLocationCode(location);

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

        grdLstPoDetail_Mdl.addAll(gridItemDetail.getGrid().getStore().getAll());

        if (gridItemDetail.getGrid().getStore().getAll().size() >= 0) {
            int i = 0;
            for (GrdItemDetail_Mdl poDeatil_Mdl : gridItemDetail.getGrid().getStore().getAll()) {
                if (DmShared.iNull(poDeatil_Mdl.getSharedKeyFieldCode()).longValue() == 0) {
                    grdLstPoDetail_Mdl.remove(i);
                    i--;
                }
                i++;
            }
            if (grdLstPoDetail_Mdl.size() < gridItemDetail.getGrid().getStore().getAll().size()) {
                gridItemDetail.getGrid().getStore().clear();
                gridItemDetail.getGrid().getStore().addAll(grdLstPoDetail_Mdl);
                gridItemDetail.commitChanges();
            }

        }

        mrTemp.getListItemDetail().clear();
        gridItemDetail.getGrid().getStore().commitChanges();
        mrTemp.getListItemDetail().addAll(gridItemDetail.getGrid().getStore().getAll());

        mrTemp.getGrdServiceDetail().clear();
        mrTemp.getGrdServiceDetail().addAll(serviceDetailGrid.getServiceGrid().getGrid().getStore().getAll());

        mrTemp.setGuiMdl(gui.getGUI_Mdl());
        mrTemp.setGuiConfigCode(gui.getConfigCode());

        if (modifyReasonMdl != null) {
            mrTemp.setModifyReasonMdl(modifyReasonMdl);
        }

        return mrTemp;
    }

    private void topControlSave() {
        Quotation_Mdl mrTemp;
        try {
            syncronizeLineService(true, false);
            defaultValue.getNewVoucherType().setSharedKeyFieldCode(txtHQuotationType.getTag());
            defaultValue.getNewVoucherType().setSharedKeyFieldName(txtHQuotationType.getText());
            defaultValue.getNewVoucherType().setVoucherDate(txtQuotationDate.getText());
            defaultValue.getNewVoucherType().setGuiTemplateCode(guiTemplateCode);
            defaultValue.getNewVoucherType().setVoucherCategory(voucherCategory);
            mrTemp = chkValidation();
            srvAsync.invokeRPC().saveRecord(mrTemp, DmClient.getVoucherPrefix(),defaultValue.isIsCommonPriceList(),defaultValue.isUpdatePriceListFromQuotation(), new AsyncCallback<DmResult<String>>() {
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
            case 700:
                new DmAlertMessageBox(errorMessage, txtValidUptoDate).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHSupplier).show();
                break;
        }
    }

    private void topControlMoveRec(String strKey) {
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

            topControl.setDocId(mrTemp.getSharedKeyFieldCode());
            txtQuotationNo.setText(mrTemp.getSharedKeyFieldName());
            txtQuotationNo.setTag(DmShared.iNull(mrTemp.getObjRecId().getCounter()).toString());
            txtQuotationNo.setTag2nd(mrTemp.getObjRecId().getPrefix());

            txtQuotationDate.setText(mrTemp.getQuotationDate());
            txtHQuotationType.setTag(mrTemp.getVoucherTypeCode());
            txtHQuotationType.setText(mrTemp.getVoucherTypeName());
            voucherCategory = DmShared.xNull(mrTemp.getVoucherCategory());
            txtHSupplier.setTag(mrTemp.getVendorTag());
            txtHSupplier.setText(mrTemp.getVendorText());
            txtVendorRefNo.setText(mrTemp.getVendorRefNo());
            txtVendorQNDate.setText(mrTemp.getVendorRefDate());
            txtHEnquiryNo.setTag(mrTemp.getEnquiryTag());
            txtHEnquiryNo.setText(mrTemp.getEnquiryText());
            txtEnquiryDate.setText(mrTemp.getEnquiryRefDate());
            txtEnquiryRefNo.setText(mrTemp.getEnquiryRefNo());
            txtValidUptoDate.setText(mrTemp.getValidUptoDate());

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

            //Set Lable SGST/UGST
            if (DmShared.vNull(mrTemp.getSgst()) > 0) {
                lblSgstUgst.setText(DmEnum.GSTS_Head.SGST.getName());
            } else if (DmShared.vNull(mrTemp.getUgst()) > 0) {
                lblSgstUgst.setText(DmEnum.GSTS_Head.UGST.getName());
            }

            //=================== ************* ===================//
            userControl.setTextBlog(preparedByBlog, mrTemp.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, mrTemp.getSharedModifiedBy());

            gridItemDetail.getGrid().getStore().clear();
            gridItemDetail.getGrid().getStore().addAll(mrTemp.getListItemDetail());

            serviceDetailGrid.getServiceGrid().getGrid().getStore().clear();
            serviceDetailGrid.getServiceGrid().getGrid().getStore().addAll(mrTemp.getGrdServiceDetail());

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
        srvAsync.invokeRPC().getItemDetail(DmShared.xNull(txtHQuotationType.getTag()), DmShared.xNull(txtHEnquiryNo.getTag()), location, defaultValue.getIsSiteStateUT(), listKF, new AsyncCallback<DmResult<List<GrdItemDetail_Mdl>>>() {
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

                        if (enquiryCardCallBack != null) {
                            DmResult<Object> dmrTemp = new DmResult<>();
                            dmrTemp.setStatus(true);
                            enquiryCardCallBack.result(dmrTemp);
                        }
                        enquiryCardCallBack = null;
                        enquiryCard_Mdl = null;

                        if (lstIndentCard != null) {
                            if (lstIndentCard.size() > 0) {
                                lstIndentCard.clear();
                                txtHSupplier.setFocus(true);
                            } else {
                                txtEnquiryRefNo.setFocus(true);
                            }
                        }
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            }
        });
    }

    private void manageDisplay(boolean flag) {
        txtQuotationNo.setEnabled(false);
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
        if (topControl.getMode().equalsIgnoreCase("Edit")) {
            txtHQuotationType.setEnabled(false);
            txtQuotationDate.setEnabled(false);
            txtHRCMApplicable.setEnabled(false);
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
        getTempKeyCode();
        clear();
        manageDisplay(true);
        serviceDetailGrid.getServiceGrid().getGrid().getStore().add(new ServiceDetail_Mdl());
        if (defaultValue.getIsContinuousNewMode()) {
            setDefaultValues();
        } else {
            setDefaultValuesWithDateReset();
        }
    }

    private void setDefaultValues() {
        txtQuotationDate.setText(defaultValue.getIsContinuousNewMode() ? DmShared.dNull(defaultValue.getNewVoucherType().getVoucherDate()) : DmShared.dNull(defaultValue.getCurrentDate()));
        txtQuotationDate.setTag(txtQuotationDate.getText());
        selectedQuotationType = defaultValue.getIsContinuousNewMode() ? defaultValue.getNewVoucherType().getSharedKeyFieldCode() : defaultValue.getVoucherType().getSharedKeyFieldCode();
        txtHQuotationType.setTag(defaultValue.getIsContinuousNewMode() ? defaultValue.getNewVoucherType().getSharedKeyFieldCode() : defaultValue.getVoucherType().getSharedKeyFieldCode());
        txtHQuotationType.setText(defaultValue.getIsContinuousNewMode() ? defaultValue.getNewVoucherType().getSharedKeyFieldName() : defaultValue.getVoucherType().getSharedKeyFieldName());
        voucherCategory = defaultValue.getIsContinuousNewMode() ? defaultValue.getNewVoucherType().getSharedModifiedBy() : defaultValue.getVoucherType().getSharedModifiedBy();
        guiTemplateCode = defaultValue.getIsContinuousNewMode() ? defaultValue.getNewVoucherType().getSharedPreparedBy() : defaultValue.getVoucherType().getSharedPreparedBy();

        txtHSupplier.setFocus(true);
        setRCM(voucherCategory);
        if (!(DmShared.iNull(defaultValue.getIsSiteStateUT()).intValue() == 1)) {
            lblSgstUgst.setText(DmEnum.GSTS_Head.SGST.getName());
        } else if ((DmShared.iNull(defaultValue.getIsSiteStateUT()).intValue() == 1)) {
            lblSgstUgst.setText(DmEnum.GSTS_Head.UGST.getName());
        }

        gui.initializeViaTemplateCode();

        if (enquiryCard_Mdl != null) {
            String vendorCode = enquiryCard_Mdl.getSharedKeyFieldName();
            getSupplierDetail(vendorCode);
        } else if (lstIndentCard != null) {
            if (lstIndentCard.size() > 0) {
                fillItemDetails(lstIndentCard);
            }
        }
    }

    private void setRCM(String vCat) {
        boolean rcmFlag = false;
        //=====================================< Set RCM >=================================//
        if (DmShared.xNull(vCat).equalsIgnoreCase(new ProjectVoucherCategory().QUOTATION_UNREGISTERED)) {
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

    @Override
    public void action(String nature, Object object, DmCallBack callback) {
        nature = DmShared.xNull(nature).trim().toUpperCase();

        try {
            switch (nature) {
                case "ADD_QUOTATION":  //TBC : satyam
                    this.lstIndentCard = (ArrayList) object;
                    topControl.btnClick("New");
                    break;
                case "ADD_RECORD":
                    this.enquiryCard_Mdl = (KeyFieldExtended) object;
                    this.enquiryCardCallBack = callback;
                    topControl.btnClick("New");
                    break;
                case "FOCUS_ON_ADD":
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            txtHSupplier.setFocus(true);
                        }
                    });
                    break;
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    private void getSupplierDetail(String vendorCode) {

        DmHelper dmHelper = new DmHelper();
        dmHelper.addSort("name", DmEnum.DmSortType.ASC);
        dmHelper.addSort("cityName", DmEnum.DmSortType.ASC);

        srvAsync.invokeRPC().getSupplierDetail(vendorCode, voucherCategory, txtHQuotationType.getTag(), dmHelper, false, new AsyncCallback<DmResult<TxtHSupplier_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHSupplier).show();
            }

            @Override
            public void onSuccess(DmResult<TxtHSupplier_Mdl> result) {
                if (result.isStatus()) {

                    txtHSupplier.setTag(result.getData().getSharedKeyFieldCode());
                    txtHSupplier.setText(result.getData().getSharedKeyFieldName());
                    fillSupplierDetail(true, result.getData());
                    getEnquiryDetail();

                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHSupplier).show();
                }
            }
        });

    }

    private void getEnquiryDetail() {
        DmHelper dmHelper = new DmHelper();
        dmHelper.addSort("enquiryDate", DmEnum.DmSortType.ASC);
        dmHelper.addSort("name", DmEnum.DmSortType.ASC);

        srvAsync.invokeRPC().getEnquiryDetail(enquiryCard_Mdl, DmShared.dNull(txtQuotationDate.getText()), "", dmHelper, false, new AsyncCallback<DmResult<List<TxtHEnquiry_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<TxtHEnquiry_Mdl>> result) {

                if (result.isStatus()) {

                    txtHEnquiryNo.setTag(result.getData().get(0).getSharedKeyFieldCode());
                    txtHEnquiryNo.setText(result.getData().get(0).getSharedKeyFieldName());
                    fillQuotationDetail(result.getData().get(0));

                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }

            }
        }
        );

    }

    private void clear() {
        guiTemplateCode = "";
        selectedQuotationType = "";
        voucherCategory = "";
        gui.clear();
        gridItemDetail.getGrid().getStore().clear();
        serviceDetailGrid.getServiceGrid().getGrid().getStore().clear();
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
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
            if (!DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                if (!detail_Mdl.isServiceRecord()) {
                    goodsGrossValue += DmShared.vNull(detail_Mdl.getAmount());
                    goodsTaxableValue += DmShared.vNull(detail_Mdl.getTaxableAmt());
                } else {
                    serviceGrossValue += DmShared.vNull(detail_Mdl.getAmount());
                    serviceTaxableValue += DmShared.vNull(detail_Mdl.getTaxableAmt());
                }
                // for common qty 
                commonUnitQty += DmShared.vNull(detail_Mdl.getQuantity()).doubleValue();

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

    @Override
    public void onSelection(SelectionEvent<Item> event) {
        if (event.getSource() == manageFileMenu) {
            fileManager.show(topControl.getMode().trim().equalsIgnoreCase("New") ? "" : topControl.getDocId(), "1", isAttachFiles, isDeleteFiles);
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

    private void getTempKeyCode() {

        srvAsync.invokeRPC().getTempKeyCode(new AsyncCallback<String>() {

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

    private void resetTaxSummary() {
        List<List<OverHeadLine_Mdl>> lstOverheadLine = new ArrayList<List<OverHeadLine_Mdl>>();
        List<OverHeadLine_Mdl> overheadLine = null;
        OverHeadLine_Mdl overheadLineRow = null;
        for (GrdItemDetail_Mdl grdMdl : gridItemDetail.getGrid().getStore().getAll()) {
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
                if (DmShared.xNull(txtHEnquiryNo.getTag()).trim().equals("")) {
                    gridItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
                }
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
                grdItemDetail_Mdl.setQuantity(DmShared.xNull(serviceDetail_Mdl.getQuantity()));
                grdItemDetail_Mdl.setPurchaseUnitCode(serviceDetail_Mdl.getUnitCode());

                grdItemDetail_Mdl.setHsnCode(serviceDetail_Mdl.getHsnCode());
                grdItemDetail_Mdl.setHsnNo(serviceDetail_Mdl.getHsn());
                grdItemDetail_Mdl.setItemGSTNature(serviceDetail_Mdl.getItemGSTNature());
                grdItemDetail_Mdl.setItsBasedOn(serviceDetail_Mdl.getItsBasedOn());
                grdItemDetail_Mdl.setGstTypeCode(serviceDetail_Mdl.getGstTypeCode());
                grdItemDetail_Mdl.setGstTypeName(serviceDetail_Mdl.getGstTypeName());
                grdItemDetail_Mdl.setManuaGstType(serviceDetail_Mdl.isManualGstType());

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

    @Override
    public void notifyForUpload(String notifier) {

    }

    @Override
    public void processAfterUploaded(DmIFile_Mdl file) {
        lblFileName.setText(file.getFileName());
        chooseFile(file.getServerDirectory(), file.getServerFileName());
    }

    private void chooseFile(String ServerDirectory, String serverFileName) {
        srvAsync.invokeRPC().getListFromCSV(ServerDirectory, serverFileName, location, defaultValue.getIsSiteStateUT(), new AsyncCallback<DmResult<Quotation_Mdl>>() {

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
                itemDetailGridEdit.displayRow(detail_Mdl);
                itemDetailGridEdit.calculate();
                itemDetailGridEdit.fillRowData(detail_Mdl);
            }
        }
        gridItemDetail.commitChanges();
        resetTaxSummary();
        calculate();
    }

    private void exportCSV(String docId) {
        srvAsync.invokeRPC().exportCSV(docId,new AsyncCallback<DmResult<String>>() {
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
}
