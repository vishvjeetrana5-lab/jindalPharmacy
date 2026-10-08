package aarogya.inventory.requisition.client;

import aarogya.inventory.requisition.shared.Requisition_Srv;
import aarogya.inventory.requisition.shared.TxtHPriority_Mdl;
import aarogya.inventory.requisition.shared.Requisition_Vld;
import aarogya.inventory.requisition.shared.Requisition_SrvAsync;
import aarogya.inventory.requisition.shared.Requisition_Mdl;
import aarogya.inventory.requisition.shared.TxtHRequisition_Mdl;
import aarogya.inventory.requisition.shared.GrdRequisition_Mdl;
import aarogya.inventory.requisition.shared.DefaultValue_Mdl;
import aarogya.inventory.requisition.shared.Find_Mdl;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import java.util.ArrayList;
import java.util.List;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.modifyReason.client.ModifyReason;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import dataman.dmToolkit.walkThrough.client.WalkThrough;
import java.util.HashMap;

public final class Requisition extends Requisition_Dsg implements IsWidget, ClickHandler, BlurHandler, AttachEvent.Handler {

    ServiceInvoker<Requisition_SrvAsync> SrvAsync;
    WalkThrough walkThrough;
    protected DefaultValue_Mdl defaultValue;
    final RequisitionGridEdit<GrdRequisition_Mdl, Grd_PrpAcs> gridEdit = new RequisitionGridEdit(dmGrd);
    String vNo = "", recIdPrefix = "", recIdValue = "";
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
        topControl.setUserPermission(menuPermission, new VoucherPermissionHandler(DmClient.getVoucherPermission(new String[]{new ProjectVoucherCategory().INVENTORY_REQUISITION})) {
            @Override
            public String getVoucherType() {
                return txtHRequisitionType.getTag();
            }

            @Override
            public String getVoucherDate() {
                return txtVDate.getText();
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
                    dmGrd.getGrid().getStore().add(new GrdRequisition_Mdl());
                    txtHSubGroup.setFocus(true);
                    txtRequisionNo.setEnabled(false);
                    txtSiteName.setEnabled(false);
                    txtHRequisitionType.setEnabled(false);
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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(Requisition.this);
            }
        });

        topControl.addRefershHandeller(new DmTopControl.eRefersh() {
            @Override
            public void refreshClicked() {
                topControlMoveRec(topControl.getDocId());
            }
        });

        topControl.setDisplay(true);
    }

    private void prnRecord(boolean printToPrinter) {
        HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
        hashMapFilter.put("docId", new FieldUDT_Mdl("Requisition No.", DmShared.xNull(txtRequisionNo.getText()), DmShared.xNull(topControl.getDocId())));
        new OpenSourceReport(DmShared.xNull(txtHRequisitionType.getTag()), "", "Requisition", widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
            @Override
            public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                DmClient.localSettingPrintMap(reportProperty, moduleName, widgetName, "Print");
                return reportProperty;
            }
        }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
    }

    private void textHelpEvents() {
        walkThrough.addCallBack(new WalkThrough.CallBack() {
            @Override
            public void onClose() {
                topControlMoveRec(topControl.getDocId());
            }
        });
        txtHDepartmentBy.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHDepartment(helper, false, blnToEndOfFile, txtHDepartmentTo.getTag(), new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDepartmentBy).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDepartmentBy.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDepartmentBy).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
        txtHDepartmentTo.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHDepartment(helper, true, blnToEndOfFile, txtHDepartmentBy.getTag(), new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDepartmentTo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDepartmentTo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDepartmentTo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                if (blnStatus && tRow != null) {
                    txtSiteName.setText(DmShared.xNull(tRow.getSharedModifiedBy()));
                    txtSiteName.setTag(DmShared.xNull(tRow.getSharedPreparedBy()));
                } else {
                    txtSiteName.setText("");
                    txtSiteName.setTag("");
                }
            }
        });
        txtHSubGroup.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHRequisition_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSubGroup(txtHRequisitionType.getTag(),helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHRequisition_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSubGroup).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHRequisition_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHSubGroup.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSubGroup).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHRequisition_Mdl tRow) {
            }
        });
        txtHPriority.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHPriority_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHPriority(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHPriority_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPriority).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHPriority_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHPriority.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPriority).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHPriority_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
                    txtHPriority.setTag2nd(DmShared.xNull(t1Row.getPriorityIndex()));
                } else {
                    txtHPriority.setTag2nd("");
                }
            }
        });
        txtHRequisitionType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHRequisitionType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHRequisitionType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHRequisitionType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHRequisitionType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {

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

        gridEdit.addCallBack(new RequisitionGridEdit.CallBack() {
            @Override
            public void onOk(GrdRequisition_Mdl grdVoucherEntry) {
                dmGrd.commitChanges();
            }

            @Override
            public void onDelete() {
                dmGrd.commitChanges();
            }
        });

        dmGrd.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showVoucherEntryGridEdit(event.getRowIndex());
            }
        });

        dmGrd.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showVoucherEntryGridEdit(dmGrd.getGrid().getStore().indexOf(dmGrd.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
    }

    private void showModifyReason() {
        modifyReason.show(topControl.getMode(), topControl.getDocId());

        modifyReason.addCallBack(new ModifyReason.CallBack() {

            @Override
            public void onOk(ModifyReason_Mdl modifyReason) {
                try {
                    Requisition.this.modifyReasonMdl = modifyReason;
                    if (topControl.getMode().equalsIgnoreCase("Browse") && DmShared.iNull(topControl.getDocId()).longValue() > 0) {
                        topControlDelete(topControl.getDocId());
                    } else {
                        topControlSave();
                    }
                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
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

    private Requisition_Mdl chkValidation() {
        Requisition_Mdl mrTemp;
        mrTemp = topControlSaveInitialize();
        Requisition_Vld.chkSave(mrTemp);
        return mrTemp;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnApprovedBy)) {
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
        if (event.getSource().equals(txtVDate)) {
            txtVDate.setText(DmClient.returnDate(txtVDate.getText()));
            if (!DmShared.dNull(txtVDate.getText()).trim().equals("")) {
                if (CalendarUtil.getDaysBetween(DmShared.convertDate(DmClient.getLoginDate()), DmShared.convertDate(txtVDate.getText())) > 0) {
                    txtVDate.setText("");
                    new DmAlertMessageBox("Date can't be greater than Current Date", txtVDate).show();
                }
            }
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
        } else {
            gridEdit.hide();
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
                            topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                        }
                    }
                });
            }
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void showVoucherEntryGridEdit(int currentRowIndex) {
        if (DmShared.xNull(txtHDepartmentBy.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please select Godown By", txtHDepartmentBy).show();
            return;
        }
        if (DmShared.xNull(txtHDepartmentTo.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please select Godown To", txtHDepartmentTo).show();
            return;
        }
        if (topControl.getMode().equalsIgnoreCase("Browse")) {
            gridEdit.show(currentRowIndex, txtHDepartmentTo.getTag().trim(), false, false, false, this);
        } else {
            gridEdit.show(currentRowIndex, txtHDepartmentTo.getTag().trim(), true, true, true, this);
        }
    }

    public Requisition(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(Requisition_Srv.class), Requisition.class.getName());
        walkThrough = new WalkThrough(SrvAsync);
        this.asWidget().setTitle(widgetName);
        flwForm.addAttachHandler(this);
        btnApprovedBy.addClickHandler(this);
        topControlEvents(menuPermission);
        findEvents();
        textHelpEvents();
        gridEvents();
        txtVDate.addBlurHandler(this);
        getDefaultValue(swi);
    }

    public void getDefaultValue(final SetWidgetInvoker swi) {

        SrvAsync.invokeRPC().getDefaultValue(new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {

                    defaultValue = result.getData();
                    swi.setWidget(Requisition.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });

    }

    private void onAdd() {
        clear();
        txtHRequisitionType.setFocus(true);
        txtVDate.setText(DmClient.getLoginDate());
        txtRequisionNo.setEnabled(false);
        txtSiteName.setEnabled(false);
        txtHSubGroup.setTag(DmShared.xNull(defaultValue.getRequisitionByCode()));
        txtHSubGroup.setText(DmShared.xNull(defaultValue.getRequisitionByName()));
        txtHDepartmentBy.setTag(DmShared.xNull(defaultValue.getDepartmentCode()));
        txtHDepartmentBy.setText(DmShared.xNull(defaultValue.getDepartmentName()));
        txtHRequisitionType.setTag(DmShared.xNull(defaultValue.getRequisitionTypeCode()));
        txtHRequisitionType.setText(DmShared.xNull(defaultValue.getRequisitionTypeName()));
        if (DmShared.iNull(defaultValue.getDefaultPurchaseDepartment()).intValue() != 0) {
            txtHDepartmentTo.setTag(DmShared.xNull(defaultValue.getDefaultPurchaseDepartment()));
            txtHDepartmentTo.setText(DmShared.xNull(defaultValue.getDefaultPurchaseDepartmentText()));
        }
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private void topControlDelete(String strKey) {
        SrvAsync.invokeRPC().deleteRecord(strKey, new AsyncCallback<DmResult<String>>() {

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
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<Requisition_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<Requisition_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<Requisition_Mdl> result) {
        Requisition_Mdl data;

        if (result.isStatus()) {
            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());
            txtHDepartmentBy.setTag(data.getDepartmentByCode());
            txtHDepartmentBy.setText(data.getDepartmentByName());
            txtHDepartmentTo.setTag(data.getDepartmentToCode());
            txtHDepartmentTo.setText(data.getDepartmentToName());
            txtHSubGroup.setTag(data.getSubCode());
            txtHSubGroup.setText(data.getSubName());
            txtRequisionNo.setText(data.getRecid().getRecIdValue());
            txtVDate.setText(data.getDate());
            txtWTACode.setText(data.getWtaCode());
            lblUEndDTLatest.setText(data.getUserEntDtLatestLine());
            txtHPriority.setTag(data.getPriorityTypeCode());
            txtHPriority.setText(data.getPriorityTypeName());
            txtHRequisitionType.setTag(data.getRequisitionTypeCode());
            txtHRequisitionType.setText(data.getRequisitionTypeName());
            txtRemark.setText(data.getRemark());

            txtSiteName.setText(data.getLinkSiteName());
            txtSiteName.setTag(data.getLinkSiteCode());

            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());
            userControl.setTextBlog(approvedByBlog, data.getApprovedBy());

            vNo = DmShared.iNull(data.getRecid().getCounter()).toString();
            recIdValue = DmShared.xNull(data.getRecid().getRecIdValue());
            recIdPrefix = DmShared.xNull(data.getRecid().getPrefix());

            dmGrd.getGrid().getStore().clear();
            dmGrd.getGrid().getStore().addAll(data.getGrdData());
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
    }

    private void topControlSave() {
        Requisition_Mdl mrTemp;

        try {
            mrTemp = chkValidation();
            SrvAsync.invokeRPC().saveRecord(mrTemp, DmClient.getVoucherPrefix(), isApprove, new AsyncCallback<DmResult<String>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtVDate).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        topControl.setDocId(result.getData());
                        if (topControl.getMode().equalsIgnoreCase("New")) {
                            new DmConfirmMessageBox("Do you want to send print to printer?", new DialogHideEvent.DialogHideHandler() {

                                @Override
                                public void onDialogHide(DialogHideEvent event) {
                                    if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                                        prnRecord(true);
                                    }
                                    topControl.btnClick("New");
                                }
                            }).show();

                        } else {
                            topControlMoveRec(topControl.getDocId());
                        }
//                        if (topControl.getMode().equalsIgnoreCase("New")) {
//                            topControl.btnClick("New");
//                        } else {
//                            topControlMoveRec(topControl.getDocId());
//                        }
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
                new DmAlertMessageBox(errorMessage, txtHRequisitionType).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtVDate).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtHSubGroup).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtHDepartmentBy).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtHDepartmentTo).show();
                break;
            case 600:
                new DmAlertMessageBox(errorMessage, new HideEvent.HideHandler() {
                    @Override
                    public void onHide(HideEvent event) {
                        ((CellSelectionModel) dmGrd.getGrid().getSelectionModel()).selectCell(DmShared.iNull(gridRowIndex).intValue(), 1);
                    }
                }).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtRemark).show();
                break;
        }
    }

    private Requisition_Mdl topControlSaveInitialize() {
        Requisition_Mdl mrTemp = new Requisition_Mdl();
        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
            mrTemp.getRecid().setCounter(DmShared.iNull(vNo).longValue());
            mrTemp.getRecid().setPrefix(DmShared.xNull(recIdPrefix));
            mrTemp.getRecid().setRecIdValue(DmShared.xNull(recIdValue));
        }
        dmGrd.getGrid().getStore().commitChanges();
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtRequisionNo.getText()));
        mrTemp.setDepartmentByCode(DmShared.xNull(txtHDepartmentBy.getTag()));
        mrTemp.setDepartmentToCode(DmShared.xNull(txtHDepartmentTo.getTag()));
        mrTemp.setRequisitionTypeCode(DmShared.xNull(txtHRequisitionType.getTag()));
        mrTemp.setRequisitionTypeName(DmShared.xNull(txtHRequisitionType.getText()));
        mrTemp.setRemark(DmShared.xNull(txtRemark.getText()));
        mrTemp.setSubCode(DmShared.xNull(txtHSubGroup.getTag()));
        mrTemp.setDate(DmShared.xNull(txtVDate.getText()));
        mrTemp.setWtaCode(DmShared.iNull(txtWTACode.getText()).toString());
        mrTemp.setUserEntDtLatestLine(DmShared.xNull(lblUEndDTLatest.getText()));
        mrTemp.setLinkSiteCode(DmShared.xNull(txtSiteName.getTag()));
        List<GrdRequisition_Mdl> OBJList = new ArrayList<>();
        OBJList.addAll(dmGrd.getGrid().getStore().getAll());
        mrTemp.getGrdData().addAll(OBJList);
        mrTemp.setPriorityTypeCode(DmShared.xNull(txtHPriority.getTag()));
        mrTemp.setvNo(vNo);
        mrTemp.setRecIdPrefix(recIdPrefix);
        if (modifyReasonMdl != null) {
            mrTemp.setModifyReasonMdl(modifyReasonMdl);
        }
        return mrTemp;
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    private void clear() {
        vNo = "";
        recIdPrefix = "";
        recIdValue = "";
        dmGrd.getGrid().getStore().clear();
        dmGrd.getGrid().getStore().add(new GrdRequisition_Mdl());
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        userControl.setTextBlog(approvedByBlog, "");
    }

}
