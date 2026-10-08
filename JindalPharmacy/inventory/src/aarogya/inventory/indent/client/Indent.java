package aarogya.inventory.indent.client;

import aarogya.inventory.indent.shared.GrdItemDetail_Mdl;
import aarogya.inventory.indent.shared.TxtHRequisition_Mdl;
import aarogya.inventory.indent.shared.Find_Mdl;
import aarogya.inventory.indent.shared.DefaultValue_Mdl;
import aarogya.inventory.indent.shared.Indent_Vld;
import aarogya.inventory.indent.shared.Indent_Mdl;
import aarogya.inventory.indent.shared.ItemWithRequisition_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import dataman.dmBase.self.shared.*;
import java.util.List;
import aarogya.inventory.indent.shared.Indent_Srv;
import aarogya.inventory.indent.shared.Indent_SrvAsync;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.modifyReason.client.ModifyReason;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.stStatic.client.STClient;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import dataman.dmToolkit.walkThrough.client.WalkThrough;
import java.util.ArrayList;
import java.util.HashMap;
import aarogya.selfToolkit.stStatic.shared.MultiRequisition_Model;

public final class Indent extends Indent_Dsg implements BlurHandler, ClickHandler {

    ServiceInvoker<Indent_SrvAsync> SrvAsync;
    final IndentGridEdit<Indent_Mdl, GrdItemDetail_PrpAcs> IndentEntryGridEdit = new IndentGridEdit(grdItemDetail);
    WalkThrough walkThrough;
    final ModifyReason modifyReason = new ModifyReason();
    private ModifyReason_Mdl modifyReasonMdl;
    private DefaultValue_Mdl dvm;
    String recIdPrefix = "", recIdValue = "", vNo = "", isConsentForReqQtyChng = "0";
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
        topControl.setUserPermission(menuPermission, new VoucherPermissionHandler(DmClient.getVoucherPermission(new String[]{new ProjectVoucherCategory().INVENTORY_INDENT})) {
            @Override
            public String getVoucherType() {
                return txtHIndentType.getTag();
            }

            @Override
            public String getVoucherDate() {
                return txtIndentDate.getText();
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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(Indent.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void prnRecord(boolean printToPrinter) {
        HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
        hashMapFilter.put("docId", new FieldUDT_Mdl("Indent No.", DmShared.xNull(txtIndentNo.getText()), DmShared.xNull(topControl.getDocId())));
        new OpenSourceReport(txtHIndentType.getTag(), "", widgetName, widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
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
        txtHIndentType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHIndentType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIndentType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIndentType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIndentType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {

            }
        });
        txtHDepartment.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHDepartment(helper, false, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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

        txtHToDepartment.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHDepartment(helper, true, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHToDepartment).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHToDepartment.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHToDepartment).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHIndentor.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHIndentor(txtHIndentType.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIndentor).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIndentor.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIndentor).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHRequisition.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHRequisition_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHRequisition(txtIndentDate.getText(), topControl.getMode().equalsIgnoreCase("New") ? "" : DmShared.xNull(topControl.getDocId()), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHRequisition_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHRequisition).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHRequisition_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHRequisition.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHRequisition).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHRequisition_Mdl t1Row) {
                grdItemDetail.getGrid().getStore().clear();
                grdItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
                if (blnStatus && t1Row != null) {
                    fillIndentDetails(t1Row.getSharedKeyFieldCode());
                }
            }
        });
    }

    private void fillIndentDetails(String requisitionDocid) {
        SrvAsync.invokeRPC().fillIndentDetails(DmShared.xNull(txtHIndentType.getTag()), requisitionDocid, txtIndentDate.getText(), topControl.getMode().equalsIgnoreCase("New") ? "" : topControl.getDocId(), new AsyncCallback<DmResult<List<GrdItemDetail_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdItemDetail_Mdl>> result) {
                grdItemDetail.getGrid().getStore().clear();
                fillGridAuto(result.getData(), null);
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

        IndentEntryGridEdit.addCallBack(new IndentGridEdit.CallBack() {
            @Override
            public void onOk(GrdItemDetail_Mdl grdVoucherEntry) {
                grdItemDetail.commitChanges();
            }

            @Override
            public void onDelete() {
                grdItemDetail.commitChanges();
            }
        });

        grdItemDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showIndentGridEdit(event.getRowIndex());
            }

        });

        grdItemDetail.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showIndentGridEdit(grdItemDetail.getGrid().getStore().indexOf(grdItemDetail.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

    }

    private void showIndentGridEdit(int rowIndex) {
        if (DmShared.xNull(txtHIndentType.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please select Indent Type", txtHIndentType).show();
            return;
        }

        if (DmShared.dNull(txtIndentDate.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please fill  Indent Date", txtIndentDate).show();
            return;
        }
        if (DmShared.xNull(txtHDepartment.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please select Department", txtHDepartment).show();
            return;
        }

        if (DmShared.xNull(txtHIndentor.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please select  Indentor", txtHIndentor).show();
            return;
        }

        if (topControl.getMode().equalsIgnoreCase("Browse")) {
            IndentEntryGridEdit.show(DmShared.xNull(txtIndentDate.getText()), rowIndex, false, false, false, this);
        } else {
            IndentEntryGridEdit.show(DmShared.xNull(txtIndentDate.getText()), rowIndex, true, true, true, this);
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtIndentDate)) {
            txtIndentDate.setText(DmClient.returnDate(txtIndentDate.getText()));
            if (!DmShared.dNull(txtIndentDate.getText()).trim().equals("")) {
                if (STClient.isFutureDate(txtIndentDate.getText())) {
                    txtIndentDate.setText("");
                    new DmAlertMessageBox("Indent Date should be less than or equal to current date.", txtIndentDate).show();
                }
            }
        }
    }

    public Indent(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(Indent_Srv.class), Indent.class.getName());
        walkThrough = new WalkThrough(SrvAsync);
        this.asWidget().setTitle(widgetName);
        txtIndentDate.addBlurHandler(this);
        btnApprovedBy.addClickHandler(this);
        topControlEvents(menuPermission);
        findEvents();
        textHelpEvents();
        gridEvents();
        iniDefaultValue(swi);

    }

    private void iniDefaultValue(final SetWidgetInvoker swi) {
        SrvAsync.invokeRPC(true).getDefaultValues(new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {

                    dvm = result.getData();

                    swi.setWidget(Indent.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
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

    private void showModifyReason() {
        modifyReason.show(topControl.getMode(), topControl.getDocId());

        modifyReason.addCallBack(new ModifyReason.CallBack() {

            @Override
            public void onOk(ModifyReason_Mdl modifyReason) {
                try {
                    Indent.this.modifyReasonMdl = modifyReason;
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
            topControlSaveError(e.getErrorNo(), e.getErrorSource(), e.getErrorSourceCode(), e.getMessage());
        }
    }

    private Indent_Mdl chkValidation() {
        Indent_Mdl mrTemp;
        mrTemp = topControlSaveInitialize();
        Indent_Vld.chkSave(mrTemp);
        return mrTemp;
    }

    private Indent_Mdl topControlSaveInitialize() {
        Indent_Mdl mrTemp = new Indent_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
            mrTemp.getRecid().setRecIdValue(DmShared.xNull(txtIndentNo.getText()));
            mrTemp.getRecid().setCounter(DmShared.iNull(vNo).longValue());
            mrTemp.getRecid().setPrefix(DmShared.xNull(recIdPrefix));
        }
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtIndentNo.getText()));
        mrTemp.setUserEntDtLatestLine(DmShared.xNull(txtIndentNo.getTag()));
        mrTemp.setDepartmentTag(DmShared.xNull(txtHDepartment.getTag()));
        mrTemp.setDepartmentText(DmShared.dNull(txtHDepartment.getText()));
        mrTemp.setToDepartmentTag(DmShared.xNull(txtHToDepartment.getTag()));
        mrTemp.setToDepartmentText(DmShared.dNull(txtHToDepartment.getText()));
        mrTemp.setDtIndent(DmShared.xNull(txtIndentDate.getText()));
        mrTemp.setIndentTypeTag(DmShared.xNull(txtHIndentType.getTag()));
        mrTemp.setIndentTypeText(DmShared.xNull(txtHIndentType.getText()));
        mrTemp.setIndentorTag(DmShared.xNull(txtHIndentor.getTag()));
        mrTemp.setIndentorText(DmShared.xNull(txtHIndentor.getText()));
        mrTemp.setRequisitionTag(DmShared.xNull(txtHRequisition.getTag()));
        mrTemp.setRequisitionText(DmShared.xNull(txtHRequisition.getText()));
        mrTemp.setRemark(DmShared.xNull(txtRemark.getText()));
        mrTemp.setWtaCode(DmShared.iNull(txtWTACode.getText()).toString());
        mrTemp.getRecid().setPrefix(recIdPrefix);
        mrTemp.getRecid().setRecIdValue(recIdValue);
        mrTemp.setvNo(vNo);
        mrTemp.setIsConsentForReqQtyChng(DmShared.xNull(isConsentForReqQtyChng));
        if (modifyReasonMdl != null) {
            mrTemp.setModifyReasonMdl(modifyReasonMdl);
        }

        grdItemDetail.getGrid().getStore().commitChanges();
        mrTemp.getGrdItemDetails().addAll(grdItemDetail.getGrid().getStore().getAll());
        return mrTemp;
    }

    private void topControlSave() {
        Indent_Mdl mrTemp;
        try {
            mrTemp = chkValidation();
            SrvAsync.invokeRPC().saveRecord(mrTemp, DmClient.getVoucherPrefix(), isApprove, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtIndentDate).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        isConsentForReqQtyChng = "0";
                        topControl.setDocId(result.getData());
                        if (topControl.getMode().equalsIgnoreCase("New")) {
                            topControl.btnClick("New");

                        } else {
                            topControlMoveRec(topControl.getDocId());
                        }
                    } else {
                        if (result.getErrorNumber() == 12) {
                            new DmConfirmMessageBox("Requisition Quantity changed for <b>" + result.getErrorMessage() + "</b> . Do you want to proceed? ", new DialogHideEvent.DialogHideHandler() {
                                @Override
                                public void onDialogHide(DialogHideEvent event) {
                                    if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                                        isConsentForReqQtyChng = "1";
                                        topControlSave();
                                    }
                                }
                            }).show();
                        } else {
                            topControlSaveError(result.getErrorNumber(), result.getValue1(), result.getValue2(), result.getErrorMessage());
                        }
                    }
                }

            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getErrorSource(), e.getErrorSourceCode(), e.getMessage());
        }
    }

    private void topControlSaveError(int errorNo, String errorSource, String errorSourceCode, String errorMessage) {
        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtHIndentType).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtIndentDate).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtHDepartment).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtHIndentor).show();
                break;
            case 500:
                int indexTemp = DmShared.iNull(errorSourceCode).intValue();
                if (!DmShared.xNull(errorSource).equalsIgnoreCase("GridRow")) {
                    List<GrdItemDetail_Mdl> listTemp = grdItemDetail.getGrid().getStore().getAll();
                    for (GrdItemDetail_Mdl node : listTemp) {
                        if (DmShared.xNull(node.getRequisitionDocId()).equalsIgnoreCase(errorSource)
                                && DmShared.xNull(node.getRequisitionVsNo()).equalsIgnoreCase(errorSourceCode)) {
                            indexTemp = DmShared.iNull(listTemp.indexOf(node)).intValue();
                            if (errorMessage != null && !errorMessage.isEmpty()) {
                                if (Character.isDigit(errorMessage.charAt(errorMessage.length() - 1))) {
                                    errorMessage += " " + DmShared.xNull(node.getUnitName());
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
            default:
                new DmAlertMessageBox(errorMessage, txtHIndentType).show();
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
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<Indent_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<Indent_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<Indent_Mdl> result) {
        Indent_Mdl data;

        clear();
        if (result.isStatus()) {
            data = result.getData();

            userControl.setTextBlog(preparedByBlog, DmShared.xNull(data.getSharedPreparedBy()));
            userControl.setTextBlog(modifiedByBlog, DmShared.xNull(data.getSharedModifiedBy()));
            userControl.setTextBlog(approvedByBlog, data.getApprovedBy());

            topControl.setDocId(data.getSharedKeyFieldCode());
            txtHIndentType.setText(data.getIndentTypeText());
            txtHIndentType.setTag(data.getIndentTypeTag());
            txtIndentNo.setText(data.getRecid().getRecIdValue());
            txtIndentNo.setTag(DmShared.iNull(data.getUserEntDtLatestLine()).toString());
            txtIndentDate.setText(data.getDtIndent());
            txtWTACode.setText(data.getWtaCode());
            txtHDepartment.setTag(data.getDepartmentTag());
            txtHDepartment.setText(data.getDepartmentText());
            txtHToDepartment.setTag(data.getToDepartmentTag());
            txtHToDepartment.setText(data.getToDepartmentText());
            txtHIndentor.setTag(data.getIndentorTag());
            txtHIndentor.setText(data.getIndentorText());
            txtHRequisition.setTag(data.getRequisitionTag());
            txtHRequisition.setText(data.getRequisitionText());
            txtRemark.setText(data.getRemark());
            recIdPrefix = DmShared.xNull(data.getRecid().getPrefix());
            vNo = DmShared.iNull(data.getRecid().getCounter()).toString();
            recIdValue = DmShared.xNull(data.getRecid().getRecIdValue());

            grdItemDetail.getGrid().getStore().clear();
            grdItemDetail.getGrid().getStore().addAll(data.getGrdItemDetails());
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
        manageDisplay(false);
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

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnApprovedBy)) {
            if (DmShared.iNull(topControl.getDocId()).longValue() != 0) {
                if (DmShared.xNull(userControl.getTextBlog(approvedByBlog)).equalsIgnoreCase("")) {
                    setApproved(topControl.getDocId(), true);
                } else {
                    new DmAlertMessageBox("System cannot be given permission to disapprove this record.", (DmTextBox) null).show();
                }

            }
        }
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    private void onAdd() {
        try {
            clear();
            manageDisplay(true);
            grdItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
            txtIndentDate.setText(DmClient.getLoginDate());
            txtHIndentType.setTag(DmShared.xNull(dvm.getIndentTypeTag()));
            txtHIndentType.setText(DmShared.xNull(dvm.getIndentTypeText()));
            txtHDepartment.setTag(DmShared.xNull(dvm.getDepartmentTag()));
            txtHDepartment.setText(DmShared.xNull(dvm.getDepartmentText()));
            if (DmShared.iNull(dvm.getDefaultPurchaseDepartment()).intValue() != 0) {
                txtHToDepartment.setTag(DmShared.xNull(dvm.getDepartmentTag()));
                txtHToDepartment.setText(DmShared.xNull(dvm.getDepartmentText()));
            }

            txtHDepartment.setFocus(true);

        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }

    }

    private void onEdit() {
        try {
            manageDisplay(true);

            if (DmShared.xNull(userControl.getTextBlog(approvedByBlog)).equalsIgnoreCase("")) {
                txtHDepartment.setFocus(true);
                grdItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
            } else {
                topControl.btnClick("");
                new DmAlertMessageBox("Record is approved. You can't modify this record.", (DmTextBox) null).show();
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    private void clear() {
        grdItemDetail.getGrid().getStore().clear();
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        userControl.setTextBlog(approvedByBlog, "");
        recIdPrefix = "";
        recIdValue = "";
        vNo = "";
    }

    private void manageDisplay(boolean enb) {
        txtIndentNo.setEnabled(false);
        txtHProcurementPlanNo.setEnabled(false);

        if (topControl.getMode().equalsIgnoreCase("Edit")) {
            txtHIndentType.setEnabled(false);
            txtIndentNo.setEnabled(false);
        }
    }

    private void fillGridAuto(List<GrdItemDetail_Mdl> grdItemDetailList, DmCallBack callback) {
        grdItemDetail.getGrid().getStore().clear();
        if (grdItemDetailList != null) {
            grdItemDetail.getGrid().getStore().addAll(grdItemDetailList);
            grdItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
            ((CellSelectionModel) grdItemDetail.getGrid().getSelectionModel()).selectCell(0, 0);
            if (callback != null) {
                DmResult<Object> dmrTemp = new DmResult<>();
                dmrTemp.setStatus(true);
                callback.result(dmrTemp);
            }
        }
    }

    @Override
    public void action(String nature, Object object, DmCallBack callback) {
        nature = DmShared.xNull(nature).trim().toUpperCase();
        try {
            switch (nature) {
                case "ADD_REQUISITION":
                    topControl.btnClick("New");
                    openWithRequisition((String) object, callback);
                    break;
                case "FOCUS_ON_ADD":
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            txtHDepartment.setFocus(true);
                        }
                    });
                    break;
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    private void openWithRequisition(String reqDocId, final DmCallBack callback) {
        SrvAsync.invokeRPC().getRequisitionDetail(DmShared.xNull(txtHIndentType.getTag()), reqDocId, txtIndentDate.getText(), topControl.getMode().equalsIgnoreCase("New") ? "" : topControl.getDocId(), new AsyncCallback<DmResult<ItemWithRequisition_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<ItemWithRequisition_Mdl> result) {
                if (result.isStatus()) {
                    if (DmShared.iNull(result.getData().getRequisitionCode()).longValue() > 0) {
                        txtHRequisition.setText(result.getData().getRequisitionName());
                        txtHRequisition.setTag(result.getData().getRequisitionCode());
                        fillGridAuto(result.getData().getGrdItemDetailList(), callback);
                    }
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }

            }
        });

    }

    @Override
    public void action(String nature, Object object) {
        nature = DmShared.xNull(nature).trim().toUpperCase();
        try {
            switch (nature) {
                case "ADD_REQUISITION_WITH_DETAIL":
                    openRequisitionWithDetail((List<MultiRequisition_Model>) object);
                    break;
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    public void openRequisitionWithDetail(List<MultiRequisition_Model> lstDetail) {
        String itemName, itemCode, skuName = "", skuCode = "";
        double itemQty = 0;

        topControl.btnClick("New");
        List<GrdItemDetail_Mdl> tmpList = new ArrayList<>();
        GrdItemDetail_Mdl node;
        KeyFieldExtended nodeDetail;

        for (int i = 0; i < lstDetail.size(); i++) {
            node = new GrdItemDetail_Mdl();
            itemQty = 0;

            MultiRequisition_Model model = lstDetail.get(i);
            txtHDepartment.setText(DmShared.xNull(model.getSharedKeyFieldName()));
            txtHDepartment.setTag(DmShared.xNull(model.getSharedKeyFieldCode()));

            txtHProcurementPlanNo.setText(DmShared.xNull(model.getGrdItemDetailList().get(0).getSharedModifiedBy()));
            txtHProcurementPlanNo.setTag(DmShared.xNull(model.getGrdItemDetailList().get(0).getSharedPreparedBy()));
            itemName = DmShared.xNull(model.getItemName());
            itemCode = DmShared.xNull(model.getItemCode());
            for (int j = 0; j < model.getGrdItemDetailList().size(); j++) {
                nodeDetail = new KeyFieldExtended();
                skuName = DmShared.xNull(model.getGrdItemDetailList().get(j).getSkuName());
                skuCode = DmShared.xNull(model.getGrdItemDetailList().get(j).getSkuCode());
                itemQty = itemQty + DmShared.vNull(model.getGrdItemDetailList().get(j).getQuantity());
                nodeDetail.setSharedKeyFieldCode(DmShared.xNull(model.getGrdItemDetailList().get(j).getSharedKeyFieldCode()));
                nodeDetail.setSharedKeyFieldName(DmShared.xNull(model.getGrdItemDetailList().get(j).getSharedKeyFieldName()));
            }

            node.setSharedKeyFieldCode(DmShared.xNull(itemCode));
            node.setSharedKeyFieldName(DmShared.xNull(itemName));
            node.setUnitName(DmShared.xNull(skuName));
            node.setQty(DmClient.vNull(itemQty, DmShared.getNumberFormat(DmShared.iNull(model.getDecimalPlaces()).intValue())));
            node.setReqDate(DmShared.dNull(model.getRequiredDate()));
            node.setRemarks(DmShared.xNull(model.getRemark()));
            tmpList.add(node);
        }
        grdItemDetail.getGrid().getStore().clear();
        grdItemDetail.getGrid().getStore().addAll(tmpList);
        grdItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());

        txtHRequisition.setEnabled(false);
    }
}
