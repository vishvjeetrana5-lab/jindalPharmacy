package aarogya.inventory.enquiry.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import dataman.dmBase.self.shared.*;
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
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import dataman.dmToolkit.dmGUI.client.DmGUI;
import dataman.dmToolkit.dmGUI.shared.DmGUIControl_Mdl;
import dataman.dmToolkit.dmGUI.shared.DmGUI_Mdl;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import dataman.dmToolkit.walkThrough.client.WalkThrough;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.inventory.enquiry.shared.DefaultValue_Mdl;
import aarogya.inventory.enquiry.shared.Enquiry_Mdl;
import aarogya.inventory.enquiry.shared.Enquiry_Srv;
import aarogya.inventory.enquiry.shared.Enquiry_SrvAsync;
import aarogya.inventory.enquiry.shared.Enquiry_Vld;
import aarogya.inventory.enquiry.shared.Find_Mdl;
import aarogya.inventory.enquiry.shared.GrdItemDetail_Mdl;
import aarogya.inventory.enquiry.shared.GrdVendor_Mdl;
import aarogya.inventory.indentSearch.client.IndentSearch;
import aarogya.selfToolkit.modifyReason.client.ModifyReason;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.stStatic.client.STClient;

public final class Enquiry extends Enquiry_Dsg implements BlurHandler, ClickHandler {

    ServiceInvoker<Enquiry_SrvAsync> SrvAsync;
    final VendorsGridEdit<GrdVendor_Mdl, GridVendor_PrpAcs> venGridEdit = new VendorsGridEdit(gridVendor);
    final ItemDetailsGridEdit<GrdItemDetail_Mdl, GridVendor_PrpAcs> itemGridEdit = new ItemDetailsGridEdit(gridItemDetail);
    protected DefaultValue_Mdl defaultValue;
    WalkThrough walkThrough;
    final IndentSearch indentSearch = new IndentSearch();
    final ModifyReason modifyReason = new ModifyReason();
    private ModifyReason_Mdl modifyReasonMdl;
    private boolean isApprove = false;

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);
        for (String controlPermission : menuPermission.getControlPermission()) {
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(approvedByBlog)) {
                isApprove = true;
            }
        }
        topControl.setUserPermission(menuPermission, new VoucherPermissionHandler(DmClient.getVoucherPermission(new String[]{new ProjectVoucherCategory().INVENTORY_ENQUIRY})) {
            @Override
            public String getVoucherType() {
                return txtHEnquiryType.getTag();
            }

            @Override
            public String getVoucherDate() {
                return txtDtEnquiry.getText();
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
                onEdit();
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

                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(Enquiry.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void textHelpEvents() {
        gui.addCallback(new DmGUI.Callback() {

            @Override
            public void initialized() {

            }

            @Override
            public String getGUITemplate() {
                return defaultValue.getGuiTemplate();
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
        walkThrough.addCallBack(new WalkThrough.CallBack() {
            @Override
            public void onClose() {
                topControlMoveRec(topControl.getDocId());
            }
        });
        indentSearch.addCallBack(new IndentSearch.CallBack() {
            @Override
            public void ok(List<KeyFieldExtended> listKF) {
                resetEnquiry();
                fillItemDetails(listKF, true);
            }

            @Override
            public void close() {
                ((CellSelectionModel) itemGridEdit.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 0);
            }
        });
        txtHEnquiryType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHEnquiryType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHEnquiryType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHEnquiryType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHEnquiryType).show();
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
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHDepartment(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
                if (blnStatus && t1Row != null) {
                    txtHDepartment.setTag2nd(DmShared.xNull(t1Row.getSharedModifiedBy()));
                }
            }
        });
    }

    private void showModifyReason() {
        modifyReason.show(topControl.getMode(), topControl.getDocId());
        modifyReason.addCallBack(new ModifyReason.CallBack() {

            @Override
            public void onOk(ModifyReason_Mdl modifyReason) {
                Enquiry.this.modifyReasonMdl = modifyReason;
                if (topControl.getMode().equalsIgnoreCase("Browse") && DmShared.iNull(topControl.getDocId()).longValue() > 0) {
                    topControlDelete(topControl.getDocId());
                } else {
                    topControlSave();
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

    private void gridEvents() {

        venGridEdit.addCallBack(new VendorsGridEdit.CallBack() {

            @Override
            public void onOk(GrdVendor_Mdl gridVendor_Mdl) {
                gridVendor.commitChanges();
            }

            @Override
            public void onDelete() {
                gridVendor.commitChanges();
            }

        });

        gridVendor.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showVendorGridEdit(event.getRowIndex());
            }
        });

        gridVendor.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showVendorGridEdit(gridVendor.getGrid().getStore().indexOf(gridVendor.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

        itemGridEdit.addCallBack(new ItemDetailsGridEdit.CallBack() {

            @Override
            public void onOk(GrdItemDetail_Mdl gridItem_Mdl) {
                gridItemDetail.commitChanges();
                fillApprovedVendors(DmShared.xNull(gridItem_Mdl.getSharedKeyFieldCode()), false);
            }

            @Override
            public void onDelete() {
                gridItemDetail.commitChanges();
            }

        });

        gridItemDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showItemGridEdit(event.getRowIndex());
            }
        });

        gridItemDetail.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showItemGridEdit(gridItemDetail.getGrid().getStore().indexOf(gridItemDetail.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

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
                            topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                        }
                    }
                });
            }
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void showVendorGridEdit(int currentRowIndex) {
        if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {
            venGridEdit.show(currentRowIndex, false, false, false, this);
        } else {
            venGridEdit.show(currentRowIndex, true, true, true, this);
        }
    }

    private void showItemGridEdit(int currentRowIndex) {
        if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {
            itemGridEdit.show(currentRowIndex, false, false, false, this);
        } else {
            itemGridEdit.show(currentRowIndex, true, true, true, this);
        }
    }

    public Enquiry(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(Enquiry_Srv.class), Enquiry.class.getName());
        this.asWidget().setTitle(widgetName);
        walkThrough = new WalkThrough(SrvAsync);
        topControlEvents(menuPermission);
        findEvents();
        textHelpEvents();
        addHandler();
        manageDisplay(false);
        gui.setBannerVisible(false);
        gridEvents();
        DefaultValues(swi);
    }

    private void addHandler() {
        txtDtEnquiry.addBlurHandler(this);
        btnApprovedBy.addClickHandler(this);
        btnAddInd.addClickHandler(this);
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

    private void topControlSave() {
        Enquiry_Mdl mrTemp;
        try {
            mrTemp = chkValidation();
            SrvAsync.invokeRPC().saveRecord(mrTemp, DmClient.getVoucherPrefix(), isApprove, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHEnquiryType).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        topControl.setDocId(result.getData());

                        if (topControl.getMode().equalsIgnoreCase("New")) {
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

    private Enquiry_Mdl chkValidation() {
        Enquiry_Mdl mrTemp;
        mrTemp = topControlSaveInitialize();
        Enquiry_Vld.chkSave(mrTemp);
        return mrTemp;
    }

    private Enquiry_Mdl topControlSaveInitialize() {
        Enquiry_Mdl mrTemp = new Enquiry_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
            mrTemp.getObjRecid().setRecIdValue(DmShared.xNull(txtEnquiryNo.getText()));
            mrTemp.getObjRecid().setCounter(DmShared.iNull(txtEnquiryNo.getTag()).longValue());
            mrTemp.getObjRecid().setRecIdValue(DmShared.xNull(txtEnquiryNo.getText()));
        }
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtEnquiryNo.getText()));
        mrTemp.setUserEntDtLatestLine(DmShared.xNull(txtHEnquiryType.getTag2nd()));
        mrTemp.setEnquiryTypeCode(DmShared.xNull(txtHEnquiryType.getTag()));
        mrTemp.setEnquiryTypeName(DmShared.xNull(txtHEnquiryType.getText()));
        mrTemp.setReferenceNo(DmShared.xNull(txtRefNo.getText()));
        mrTemp.setEnquiryDt(DmShared.dNull(txtDtEnquiry.getText()));
        mrTemp.setRemark(DmShared.xNull(txtRemark.getText()));
        mrTemp.setWtaCode(DmShared.xNull(txtWTACode.getText()));
        mrTemp.setDepartmentTag(DmShared.xNull(txtHDepartment.getTag()));
        mrTemp.setGuiMdl(gui.getGUI_Mdl());
        mrTemp.setGuiConfigCode(gui.getConfigCode());

        if (modifyReasonMdl != null) {
            mrTemp.setModifyReasonMdl(modifyReasonMdl);
        }

        List<GrdItemDetail_Mdl> OBJListItem = new ArrayList<>();
        OBJListItem.addAll(gridItemDetail.getGrid().getStore().getAll());
        mrTemp.getGrdItem_Mdl().addAll(OBJListItem);

        List<GrdVendor_Mdl> OBJListVendor = new ArrayList<>();
        OBJListVendor.addAll(gridVendor.getGrid().getStore().getAll());
        mrTemp.getGrdVen_Mdl().addAll(OBJListVendor);

        return mrTemp;
    }

    private void topControlSaveError(int errorNo, String errorMessage) {
        topControlSaveError(errorNo, "", errorMessage);
    }

    private void topControlSaveError(int errorNo, final String gridRowIndex, String errorMessage) {
        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtHEnquiryType).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtDtEnquiry).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, new HideEvent.HideHandler() {
                    @Override
                    public void onHide(HideEvent event) {
                        ((CellSelectionModel) gridItemDetail.getGrid().getSelectionModel()).selectCell(DmShared.iNull(gridRowIndex).intValue(), 1);
                    }
                }).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, new HideEvent.HideHandler() {
                    @Override
                    public void onHide(HideEvent event) {
                        ((CellSelectionModel) gridVendor.getGrid().getSelectionModel()).selectCell(DmShared.iNull(gridRowIndex).intValue(), 1);
                    }
                }).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtHDepartment).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtRemark).show();
                break;
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtDtEnquiry)) {
            txtDtEnquiry.setText(DmClient.returnDate(txtDtEnquiry.getText()));
            if (!DmShared.dNull(txtDtEnquiry.getText()).trim().equals("")) {
                if (STClient.isFutureDate(txtDtEnquiry.getText())) {
                    txtDtEnquiry.setText("");
                    new DmAlertMessageBox("Enquiry Date should be less than or equal to current date.", txtDtEnquiry).show();
                }
            }
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
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<Enquiry_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<Enquiry_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });

    }

    private void topControlMoveRecSuccess(DmResult<Enquiry_Mdl> result) {
        Enquiry_Mdl data;

        clear();
        if (result.isStatus()) {
            data = result.getData();

            userControl.setTextBlog(preparedByBlog, data.getPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getModifiedBy());
            userControl.setTextBlog(approvedByBlog, data.getApprovedBy());

            topControl.setDocId(data.getSharedKeyFieldCode());
            txtHEnquiryType.setText(data.getEnquiryTypeName());
            txtHEnquiryType.setTag(data.getEnquiryTypeCode());
            txtHEnquiryType.setTag2nd(DmShared.iNull(data.getUserEntDtLatestLine()).toString());
            txtEnquiryNo.setText(data.getEnquiryNo());
            txtEnquiryNo.setTag(DmShared.iNull(data.getObjRecid().getCounter()).toString());
            txtEnquiryNo.setTag2nd(DmShared.xNull(data.getObjRecid().getPrefix()));

            txtDtEnquiry.setText(data.getEnquiryDt());
            txtRemark.setText(data.getRemark());
            txtRefNo.setText(data.getReferenceNo());
            txtWTACode.setText(data.getWtaCode());

            txtHDepartment.setTag(data.getDepartmentTag());
            txtHDepartment.setText(data.getDepartmentText());

            gridItemDetail.getGrid().getStore().clear();
            gridItemDetail.getGrid().getStore().addAll(data.getGrdItem_Mdl());

            gridVendor.getGrid().getStore().clear();
            gridVendor.getGrid().getStore().addAll(data.getGrdVen_Mdl());

            gui.initialize(data.getGuiMdl());
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
                    topControlCard();
                    clear();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void onAdd() {
        clear();
        manageDisplay(true);
        setDefaultValues();
        txtHEnquiryType.setFocus(true);
        gui.initializeViaTemplateCode();
    }

    private void onEdit() {
        if (DmShared.xNull(userControl.getTextBlog(approvedByBlog)).equalsIgnoreCase("")) {
            manageDisplay(true);

            gridItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
            gridVendor.getGrid().getStore().add(new GrdVendor_Mdl());
        } else {
            topControl.btnClick("");
            new DmAlertMessageBox("Record is approved. You can't modify this record.", (DmTextBox) null).show();
        }

    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private void clear() {
        gui.clear();
        gridVendor.getGrid().getStore().clear();
        gridItemDetail.getGrid().getStore().clear();

        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        userControl.setTextBlog(approvedByBlog, "");
    }

    public void DefaultValues(final SetWidgetInvoker swi) {

        SrvAsync.invokeRPC().getDefaultValue(new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {

                    defaultValue = result.getData();
                    swi.setWidget(Enquiry.this);
                    gui.initializeViaTemplateCode();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });

    }

    private void setDefaultValues() {
        txtDtEnquiry.setText(DmShared.dNull(defaultValue.getCurrentDate()));
        txtHEnquiryType.setTag(defaultValue.getEnquiryTypeCode());
        txtHEnquiryType.setText(defaultValue.getEnquiryTypeName());

        txtHDepartment.setTag(defaultValue.getDepartmentCode());
        txtHDepartment.setText(defaultValue.getDepartmentName());

        gridItemDetail.getGrid().getStore().clear();
        gridItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());

        gridVendor.getGrid().getStore().clear();
        gridVendor.getGrid().getStore().add(new GrdVendor_Mdl());
    }

    private void manageDisplay(boolean flag) {
        txtHEnquiryType.setEnabled(flag);
        if (!flag) {
            btnAddInd.removeFromParent();
        } else {
            flwIndentBtn.clear();
            flwIndentBtn.add(btnAddInd);
        }

        txtEnquiryNo.setEnabled(false);
        if (topControl.getMode().equalsIgnoreCase("Edit")) {
            txtHEnquiryType.setEnabled(false);
        }
    }

    @Override
    public void action(String nature, Object object, DmCallBack callback) {
        nature = DmShared.xNull(nature).trim().toUpperCase();
        List<KeyFieldExtended> lst = (ArrayList) object;
        try {
            switch (nature) {
                case "ADD_ENQUIRY":
                    topControl.btnClick("New");
                    fillItemDetails(lst, true);
                    break;
                case "FOCUS_ON_ADD":
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
//                            txtHDepartment.setFocus(true);
                        }
                    });
                    break;
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    private void fillItemDetails(List<KeyFieldExtended> listKF, final boolean flagReset) {
        SrvAsync.invokeRPC().getItemDetail(DmShared.xNull(txtHEnquiryType.getTag()), listKF, new AsyncCallback<DmResult<List<GrdItemDetail_Mdl>>>() {
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

                        String itemCsv = "";
                        for (GrdItemDetail_Mdl gim : result.getData()) {
                            if (!DmShared.xNull(gim.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                                if (!itemCsv.trim().equalsIgnoreCase("")) {
                                    itemCsv += ",";
                                }
                                itemCsv += gim.getSharedKeyFieldCode();
                            }
                        }
                        fillApprovedVendors(itemCsv, flagReset);
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            }
        });
    }

    private void fillApprovedVendors(String itemCsv, final boolean flagReset) {
        String vendorCsv = "";

        try {

            List<GrdVendor_Mdl> gvms = new ArrayList<GrdVendor_Mdl>();
            gvms.addAll(gridVendor.getGrid().getStore().getAll());
            for (GrdVendor_Mdl gvm : gvms) {
                if (!DmShared.xNull(gvm.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                    if (!vendorCsv.trim().equalsIgnoreCase("")) {
                        vendorCsv += ",";
                    }
                    vendorCsv += gvm.getSharedKeyFieldCode();
                }
            }

            SrvAsync.invokeRPC().getApprovedVendors(itemCsv, vendorCsv, new AsyncCallback<DmResult<List<GrdVendor_Mdl>>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<List<GrdVendor_Mdl>> result) {
                    if (result.isStatus()) {
                        if (flagReset) {
                            gridVendor.getGrid().getStore().clear();
                            gridVendor.getGrid().getStore().addAll(result.getData());
                            gridVendor.getGrid().getStore().add(new GrdVendor_Mdl());
                        } else {
                            if (result.getData() != null) {
                                List<GrdVendor_Mdl> lstVendorMdl = new ArrayList<>();
                                for (GrdVendor_Mdl gvm : gridVendor.getGrid().getStore().getAll()) {
                                    if (!DmShared.xNull(gvm.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                                        lstVendorMdl.add(gvm);
                                    }
                                }
                                for (GrdVendor_Mdl gvm : result.getData()) {
                                    lstVendorMdl.add(gvm);
                                }
                                lstVendorMdl.add(new GrdVendor_Mdl());
                                gridVendor.getGrid().getStore().clear();
                                gridVendor.getGrid().getStore().addAll(lstVendorMdl);
                            }
                        }
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }

                }
            });
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
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
        } else if (event.getSource().equals(btnAddInd)) {
            indentSearch.show(DmShared.xNull(txtHEnquiryType.getTag()), null, DmShared.dNull(txtDtEnquiry.getText()), new ProjectVoucherCategory().INVENTORY_ENQUIRY);
        }

    }

    private void resetEnquiry() {

        gridItemDetail.getGrid().getStore().clear();
        gridItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());

        gridVendor.getGrid().getStore().clear();
    }

    private void prnRecord(boolean printToPrinter) {
        HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
        hashMapFilter.put("docId", new FieldUDT_Mdl("Enquiry No.", DmShared.xNull(txtEnquiryNo.getText()), DmShared.xNull(topControl.getDocId())));
        new OpenSourceReport(DmShared.xNull(defaultValue.getEnquiryTypeCode()), "", "REQUEST FOR QUOTATION", widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
            @Override
            public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                DmClient.localSettingPrintMap(reportProperty, moduleName, widgetName, "Print");
                return reportProperty;
            }
        }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
    }

}
