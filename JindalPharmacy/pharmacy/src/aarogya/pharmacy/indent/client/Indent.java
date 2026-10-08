package aarogya.pharmacy.indent.client;

import aarogya.pharmacy.indent.shared.*;
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
import dataman.dmToolkit.self.shared.*;
import java.util.List;
import aarogya.pharmacy.indent.shared.Indent_Srv;
import aarogya.pharmacy.indent.shared.Indent_SrvAsync;
import aarogya.pharmacy.indent.shared.MedicineWithRequisition_Mdl;
import aarogya.selfToolkit.stStatic.shared.MultiRequisition_Model;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.projectMenu.self.shared.pmPharmacy;
import dataman.dmToolkit.modifyReason.client.ModifyReason;
import dataman.dmToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.stStatic.client.STClient;
import aarogya.selfToolkit.toolTip.client.ToolTip;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import dataman.dmToolkit.walkThrough.client.WalkThrough;
import java.util.ArrayList;
import java.util.HashMap;

public final class Indent extends Indent_Dsg implements BlurHandler, ClickHandler {

    private final ServiceInvoker<Indent_SrvAsync> SrvAsync;
    private final IndentGridEdit<Indent_Mdl, Indent_Dsg.GrdMedicineDetail_PrpAcs> IndentEntryGridEdit = new IndentGridEdit(grdMedicineDetail);
    private final WalkThrough walkThrough;
    private final ModifyReason modifyReason = new ModifyReason();
    private ModifyReason_Mdl modifyReasonMdl;
    private DefaultValue_Mdl dvm;
    private final ToolTip toolTip = new ToolTip();
    private String recIdPrefix = "", recIdValue = "", vNo = "", isConsentForReqQtyChng = "0";
    private final String voucherCategory = new ProjectVoucherCategory().PHARMACY_INDENT;
    private String moduleName = new pmPharmacy().moduleName;
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
        topControl.setUserPermission(menuPermission, new VoucherPermissionHandler(DmClient.getVoucherPermission(new String[]{voucherCategory})) {
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
                txtIndentNo.setEnabled(false);
                txtHRequisition.setEnabled(true);
                txtHProcurementPlanNo.setEnabled(false);
                txtHDepartment.setFocus(true);
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                if (DmShared.xNull(userControl.getTextBlog(approvedByBlog)).equalsIgnoreCase("")) {
                    txtHIndentType.setEnabled(false);
                    txtIndentNo.setEnabled(false);
                    txtHDepartment.setFocus(true);
                    grdMedicineDetail.getGrid().getStore().add(new GrdMedicineDetail_Mdl());

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
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHRequisition(DmShared.xNull(txtIndentDate.getText()), topControl.getMode().equalsIgnoreCase("New") ? "" : DmShared.xNull(topControl.getDocId()), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHRequisition_Mdl>>>() {

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
                if (blnStatus && t1Row != null) {
                    fillIndentDetails(t1Row.getSharedKeyFieldCode());
                }
            }
        });
    }

    private void fillIndentDetails(String requisitionDocid) {
        SrvAsync.invokeRPC().fillIndentDetails(requisitionDocid, DmShared.xNull(txtIndentDate.getText()), topControl.getMode().equalsIgnoreCase("New") ? "" : DmShared.xNull(topControl.getDocId()), new AsyncCallback<DmResult<List<GrdMedicineDetail_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdMedicineDetail_Mdl>> result) {
                grdMedicineDetail.getGrid().getStore().clear();
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
            public void onOk(GrdMedicineDetail_Mdl grdVoucherEntry) {
                grdMedicineDetail.commitChanges();
            }

            @Override
            public void onDelete() {
                grdMedicineDetail.commitChanges();
            }
        });

        grdMedicineDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                if (event.getColumnIndex() == rowIndexRequisitionFor) {
                    if (!grdMedicineDetail.getGrid().getStore().get(event.getRowIndex()).getData().isEmpty()) {
                        toolTip.show(grdMedicineDetail.getGrid().getStore().get(event.getRowIndex()).getData(), grdMedicineDetail.getGrid());
                    }
                } else {
                    if (DmShared.xNull(txtHProcurementPlanNo.getTag()).trim().equalsIgnoreCase("")) {
                        showIndentGridEdit(event.getRowIndex());
                    } else {
                        new DmAlertMessageBox("Detail not edtable in Indent against Procurement Plan", txtHProcurementPlanNo).show();
                        return;
                    }
                }
            }

        });

        grdMedicineDetail.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    if (DmShared.xNull(txtHProcurementPlanNo.getTag()).trim().equalsIgnoreCase("")) {
                        showIndentGridEdit(grdMedicineDetail.getGrid().getStore().indexOf(grdMedicineDetail.getGrid().getSelectionModel().getSelectedItem()));
                    } else {
                        new DmAlertMessageBox("Detail not edtable in Indent against Procurement Plan", txtHProcurementPlanNo).show();
                        return;
                    }
                }
            }
        }, KeyDownEvent.getType());
    }

    private void showIndentGridEdit(int rowIndex) {
        if (topControl.getMode().equalsIgnoreCase("Browse")) {
            IndentEntryGridEdit.show(DmShared.xNull(txtIndentDate.getText()), rowIndex, false, false, false);
        } else {
            IndentEntryGridEdit.show(DmShared.xNull(txtIndentDate.getText()), rowIndex, true, true, true);
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtIndentDate)) {
            txtIndentDate.setText(DmClient.returnDate(txtIndentDate.getText()));
            if (STClient.isFutureDate(DmShared.dNull(txtIndentDate.getText()))) {
                txtIndentDate.setText("");
                new DmAlertMessageBox("Indent Date cannot be greater than current date.", txtIndentDate).show();
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

    private void showModifyReason() {
        modifyReason.showNew(topControl.getMode(), topControl.getDocId(), "", "");

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
        mrTemp.setDepartmentText(DmShared.xNull(txtHDepartment.getText()));
        mrTemp.setDtIndent(DmShared.xNull(txtIndentDate.getText()));
        mrTemp.setWtaCode(DmShared.xNull(txtWTACode.getText()));
        mrTemp.setIndentTypeTag(DmShared.xNull(txtHIndentType.getTag()));
        mrTemp.setIndentTypeText(DmShared.xNull(txtHIndentType.getText()));
        mrTemp.setIndentorTag(DmShared.xNull(txtHIndentor.getTag()));
        mrTemp.setIndentorText(DmShared.xNull(txtHIndentor.getText()));
        mrTemp.setRequisitionTag(DmShared.xNull(txtHRequisition.getTag()));
        mrTemp.setRequisitionText(DmShared.xNull(txtHRequisition.getText()));
        mrTemp.setRemark(DmShared.xNull(txtRemark.getText()));
        mrTemp.setIsConsentForReqQtyChng(DmShared.xNull(isConsentForReqQtyChng));

        mrTemp.setProcurementPlanRecID(DmShared.xNull(txtHProcurementPlanNo.getText()));
        mrTemp.setProcurementPlanDocID(DmShared.xNull(txtHProcurementPlanNo.getTag()));

        if (modifyReasonMdl != null) {
            mrTemp.setModifyReasonMdl(modifyReasonMdl);
        }

        grdMedicineDetail.getGrid().getStore().commitChanges();
        mrTemp.getGrdItemDetails().addAll(grdMedicineDetail.getGrid().getStore().getAll());
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
                    List<GrdMedicineDetail_Mdl> listTemp = grdMedicineDetail.getGrid().getStore().getAll();
                    for (GrdMedicineDetail_Mdl node : listTemp) {
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
                        ((CellSelectionModel) grdMedicineDetail.getGrid().getSelectionModel()).selectCell(DmShared.iNull(gridRowIndex).intValue(), 1);
                    }
                }).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtRemark).show();
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

        if (result.isStatus()) {
            data = result.getData();

            userControl.setTextBlog(preparedByBlog, data.getPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getModifiedBy());
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
            txtHIndentor.setTag(data.getIndentorTag());
            txtHIndentor.setText(data.getIndentorText());
            txtHRequisition.setTag(data.getRequisitionTag());
            txtHRequisition.setText(data.getRequisitionText());
            txtRemark.setText(data.getRemark());

            recIdPrefix = DmShared.xNull(data.getRecid().getPrefix());
            vNo = DmShared.iNull(data.getRecid().getCounter()).toString();
            recIdValue = DmShared.xNull(data.getRecid().getRecIdValue());

            txtHProcurementPlanNo.setTag(data.getProcurementPlanDocID());
            txtHProcurementPlanNo.setText(data.getProcurementPlanRecID());

            grdMedicineDetail.getGrid().getStore().clear();
            grdMedicineDetail.getGrid().getStore().addAll(data.getGrdItemDetails());
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);

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
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    private void onAdd() {
        clear();
        grdMedicineDetail.getGrid().getStore().add(new GrdMedicineDetail_Mdl());
        txtIndentNo.setTag(DmClient.getVoucherPrefix());
        txtIndentDate.setText(DmClient.getLoginDate());
        txtHIndentType.setTag(DmShared.xNull(dvm.getIndentTypeTag()));
        txtHIndentType.setText(DmShared.xNull(dvm.getIndentTypeText()));
        txtHDepartment.setTag(DmShared.xNull(dvm.getDepartmentTag()));
        txtHDepartment.setText(DmShared.xNull(dvm.getDepartmentText()));
    }

    private void clear() {
        grdMedicineDetail.getGrid().getStore().clear();
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        userControl.setTextBlog(approvedByBlog, "");
        recIdPrefix = "";
        recIdValue = "";
        vNo = "";
    }

    private void fillGridAuto(List<GrdMedicineDetail_Mdl> grdItemDetailList, DmCallBack callback) {
        grdMedicineDetail.getGrid().getStore().clear();
        if (grdItemDetailList != null) {
            grdMedicineDetail.getGrid().getStore().addAll(grdItemDetailList);
            grdMedicineDetail.getGrid().getStore().add(new GrdMedicineDetail_Mdl());
            ((CellSelectionModel) grdMedicineDetail.getGrid().getSelectionModel()).selectCell(0, 0);
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
                    openWithRequisition((String) object, callback);
                case "FOCUS_ON_ADD":
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            txtHDepartment.setFocus(true);
                        }
                    });
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
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

    public void openWithRequisition(String reqDocId, final DmCallBack callback) {
        topControl.btnClick("New");

        SrvAsync.invokeRPC().getRequisitionDetail(reqDocId, DmShared.xNull(txtIndentDate.getText()), topControl.getMode().equalsIgnoreCase("New") ? "" : DmShared.xNull(topControl.getDocId()), new AsyncCallback<DmResult<MedicineWithRequisition_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<MedicineWithRequisition_Mdl> result) {

                if (result.isStatus()) {

                    txtHRequisition.setText(result.getData().getRequisitionName());
                    txtHRequisition.setTag(result.getData().getRequisitionCode());

                    fillGridAuto(result.getData().getGrdItemDetailList(), callback);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }

            }
        });

    }

    public void openRequisitionWithDetail(List<MultiRequisition_Model> lstDetail) {
        String itemName, itemCode, skuName = "", skuCode = "";
        double itemQty = 0;

        topControl.btnClick("New");
        List<GrdMedicineDetail_Mdl> tmpList = new ArrayList<>();
        GrdMedicineDetail_Mdl node;
        KeyFieldExtended nodeDetail;

        for (int i = 0; i < lstDetail.size(); i++) {
            node = new GrdMedicineDetail_Mdl();
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
                node.getLstOldRequisition().add(nodeDetail);
            }

            node.setSharedKeyFieldCode(DmShared.xNull(itemCode));
            node.setSharedKeyFieldName(DmShared.xNull(itemName));
            node.setUnitName(DmShared.xNull(skuName));
            node.setQty(DmClient.vNull(itemQty, DmShared.getNumberFormat(DmShared.iNull(model.getDecimalPlaces()).intValue())));
            node.setReqDate(DmShared.dNull(model.getRequiredDate()));
            node.setRemarks(DmShared.xNull(model.getRemark()));
            tmpList.add(node);
        }
        grdMedicineDetail.getGrid().getStore().clear();
        grdMedicineDetail.getGrid().getStore().addAll(tmpList);
        grdMedicineDetail.getGrid().getStore().add(new GrdMedicineDetail_Mdl());

        txtHRequisition.setEnabled(false);
    }
}
