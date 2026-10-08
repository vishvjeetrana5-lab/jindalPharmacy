package aarogya.pharmacy.physicalStock.client;

import aarogya.pharmacy.physicalStock.shared.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
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
import java.util.List;
import aarogya.pharmacy.physicalStock.shared.PhysicalStock_Srv;
import aarogya.pharmacy.physicalStock.shared.PhysicalStock_SrvAsync;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.modifyReason.client.ModifyReason;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.stStatic.client.STClient;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import java.util.HashMap;

public final class PhysicalStock extends PhysicalStock_Dsg implements BlurHandler, AttachEvent.Handler, DmRootEvents {

    private ServiceInvoker<PhysicalStock_SrvAsync> srvAsync;
    protected DefaultValue_Mdl defaultValue;
    final PhysicalStockGridEdit<GrdMedicineDetail_Mdl, GridMedicineDetail_PrpAcs> medicineDetailGridEdit = new PhysicalStockGridEdit(gridMedicineDetail);
    private String selectedDepartment = "", selectedFilterType = "", selectedFilterValue = "", uEntDt_LatestLine = "";
    boolean departmentEditable = false;
    final ModifyReason modifyReason = new ModifyReason();
    private ModifyReason_Mdl modifyReasonMdl;

    private void topControlEvents(UserMenu menuPermission) {

        topControl.setTitle(this.asWidget().getTitle());
        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);
        for (String controlPermission : menuPermission.getControlPermission()) {
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(departmentEditableBlog)) {
                departmentEditable = true;
            }

        }

        topControl.setUserPermission(menuPermission, new VoucherPermissionHandler(DmClient.getVoucherPermission(new String[]{new ProjectVoucherCategory().PHARMACY_PHYSICAL_STOCK})) {
            @Override
            public String getVoucherType() {
                return txtVoucherDate.getTag();
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
                manageDisplay(true);
                gridMedicineDetail.getGrid().getStore().add(new GrdMedicineDetail_Mdl());
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
                save();
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(PhysicalStock.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void textHelpEvents() {

        txtHDepartment.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                selectedDepartment = DmShared.xNull(txtHDepartment.getTag());
                srvAsync.invokeRPC(blnCallBackWait).getTxtHDepartment(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
                if (blnStatus) {
                    if (DmShared.iNull(selectedDepartment).longValue() != DmShared.iNull(txtHDepartment.getTag()).longValue()) {
                        gridMedicineDetail.getGrid().getStore().clear();
                        gridMedicineDetail.getGrid().getStore().add(new GrdMedicineDetail_Mdl());
                    }
                }
            }
        });

        txtHFilterType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                selectedFilterType = DmShared.xNull(txtHFilterType.getTag());
                srvAsync.invokeRPC(blnCallBackWait).getTxtHFilterType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHFilterType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHFilterType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHFilterType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (DmShared.iNull(selectedFilterType).longValue() != DmShared.iNull(txtHFilterType.getTag()).longValue()) {
                    txtHFilterValue.setText("");
                    txtHFilterValue.setTag("");
                    gridMedicineDetail.getGrid().getStore().clear();
                    gridMedicineDetail.getGrid().getStore().add(new GrdMedicineDetail_Mdl());
                }
            }
        });

        txtHFilterValue.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                selectedFilterValue = DmShared.xNull(txtHFilterValue.getTag());
                srvAsync.invokeRPC(blnCallBackWait).getTxtHFilterValue(txtHFilterType.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHFilterValue).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHFilterValue.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHFilterValue).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (DmShared.iNull(selectedFilterValue).longValue() != DmShared.iNull(txtHFilterValue.getTag()).longValue()) {
                    gridMedicineDetail.getGrid().getStore().clear();
                    gridMedicineDetail.getGrid().getStore().add(new GrdMedicineDetail_Mdl());
                }
            }
        });

    }

    private void gridEvents() {
        medicineDetailGridEdit.addCallBack(new PhysicalStockGridEdit.CallBack() {

            @Override
            public void onOk(GrdMedicineDetail_Mdl opdService) {
                gridMedicineDetail.commitChanges();
            }

            @Override
            public void onDelete() {
                gridMedicineDetail.commitChanges();
            }
        });

        gridMedicineDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {

                showMedicineIssueGridEdit(event.getRowIndex());

            }
        });

        gridMedicineDetail.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showMedicineIssueGridEdit(gridMedicineDetail.getGrid().getStore().indexOf(gridMedicineDetail.getGrid().getSelectionModel().getSelectedItem()));
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

    private void showMedicineIssueGridEdit(int currentRowIndex) {
        if (DmShared.xNull(txtHDepartment.getTag()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please select Department.", txtHDepartment).show();
            return;
        }
        if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {
            medicineDetailGridEdit.show(currentRowIndex, false, false, false, this);
        } else {
            medicineDetailGridEdit.show(currentRowIndex, true, true, true, this);
        }
    }

    private void prnRecord(boolean printToPrinter) {
        HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
        hashMapFilter.put("docId", new FieldUDT_Mdl("Issue No.", DmShared.xNull(txtVoucherNo.getText()), DmShared.xNull(topControl.getDocId())));
        new OpenSourceReport(txtVoucherDate.getTag(), "", widgetName, widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
            @Override
            public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                DmClient.localSettingPrintMap(reportProperty, moduleName, widgetName, "Print");
                return reportProperty;
            }
        }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtVoucherDate) {
            txtVoucherDate.setText(DmClient.returnDate(txtVoucherDate.getText()));
            if (STClient.isFutureDate(txtVoucherDate.getText())) {
                txtVoucherDate.setText("");
                new DmAlertMessageBox("Date should be less than or equal to current date.", txtVoucherDate).show();
                return;
            }
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            medicineDetailGridEdit.hide();
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getShiftKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_P) {
            rootEvent.stopPropagation();
        }
    }

    private void manageEvent() {
        txtVoucherDate.addBlurHandler(this);
    }

    public PhysicalStock(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        srvAsync = new ServiceInvoker<PhysicalStock_SrvAsync>(GWT.create(PhysicalStock_Srv.class), PhysicalStock.class.getName());
        this.asWidget().setTitle(widgetName);
        flwForm.addAttachHandler(this);
        topControlEvents(menuPermission);
        manageEvent();
        textHelpEvents();
        findEvents();
        gridEvents();
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
                    txtVoucherDate.setText("");
                    swi.setWidget(PhysicalStock.this);
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

    private PhysicalStock_Mdl topControlSaveInitialize() {
        PhysicalStock_Mdl mrTemp = new PhysicalStock_Mdl();
        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
            mrTemp.getRecIdObj().setCounter(DmShared.iNull(txtVoucherNo.getTag()).longValue());
            mrTemp.getRecIdObj().setPrefix(DmShared.xNull(txtVoucherNo.getTag2nd()));
            mrTemp.getRecIdObj().setRecIdValue(DmShared.xNull(txtVoucherNo.getText()));
        }
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtVoucherNo.getText()));
        mrTemp.setVoucherDate(DmShared.dNull(txtVoucherDate.getText()));
        mrTemp.setVoucherTypeCode(DmShared.xNull(txtVoucherDate.getTag()));
        mrTemp.setDepartmentCode(DmShared.xNull(txtHDepartment.getTag()));
        mrTemp.setDepartmentName(DmShared.xNull(txtHDepartment.getText()));
        mrTemp.setFilterTypeCode(DmShared.xNull(txtHFilterType.getTag()));
        mrTemp.setFilterTypeName(DmShared.xNull(txtHFilterType.getText()));
        mrTemp.setFilterValueCode(DmShared.xNull(txtHFilterValue.getTag()));
        mrTemp.setFilterValueName(DmShared.xNull(txtHFilterValue.getText()));
        mrTemp.setIsAllowDuplicate(cbAllowDuplicate.getValue() == true ? "1" : "0");
        mrTemp.setU_EntDt_LatestLine(DmShared.xNull(uEntDt_LatestLine));
        mrTemp.getListMedicineDetail().clear();
        mrTemp.getListMedicineDetail().addAll(gridMedicineDetail.getGrid().getStore().getAll());
        if (modifyReasonMdl != null) {
            mrTemp.setModifyReasonMdl(modifyReasonMdl);
        }
        return mrTemp;
    }

    public void save() {
        if (topControl.getMode().equalsIgnoreCase("New")) {
            topControlSave();
        } else {
            showModifyReason();
        }
    }

    private void showModifyReason() {
        modifyReason.show(topControl.getMode(), topControl.getDocId());

        modifyReason.addCallBack(new ModifyReason.CallBack() {

            @Override
            public void onOk(ModifyReason_Mdl modifyReason) {
                try {
                    PhysicalStock.this.modifyReasonMdl = modifyReason;
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

    private void topControlSave() {
        PhysicalStock_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            PhysicalStock_Vld.chkSave(mrTemp);

            srvAsync.invokeRPC().saveRecord(mrTemp, DmClient.getVoucherPrefix(), new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHDepartment).show();
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
                        topControlSaveError(result.getErrorNumber(), result.getValue1(), result.getValue2(), result.getErrorMessage());
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
                new DmAlertMessageBox(errorMessage, txtVoucherDate).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtVoucherDate).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtHDepartment).show();
                break;
            case 400:
                final int gridRowIndex = DmShared.iNull(errorSourceCode).intValue();
                new DmAlertMessageBox(errorMessage, new HideEvent.HideHandler() {
                    @Override
                    public void onHide(HideEvent event) {
                        ((CellSelectionModel) gridMedicineDetail.getGrid().getSelectionModel()).selectCell(gridRowIndex, 1);
                    }
                }).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHDepartment).show();
                break;
        }
    }

    private void topControlMoveRec(String strKey) {
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<PhysicalStock_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<PhysicalStock_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<PhysicalStock_Mdl> result) {
        clear();
        if (result.isStatus()) {
            PhysicalStock_Mdl mrTemp = result.getData();
            topControl.setDocId(mrTemp.getSharedKeyFieldCode());
            txtVoucherNo.setText(mrTemp.getSharedKeyFieldName());
            txtVoucherNo.setTag(DmShared.iNull(mrTemp.getRecIdObj().getCounter()).toString());
            txtVoucherNo.setTag2nd(DmShared.xNull(mrTemp.getRecIdObj().getPrefix()));
            txtVoucherDate.setTag(mrTemp.getVoucherTypeCode());
            txtVoucherDate.setText(mrTemp.getVoucherDate());
            txtHDepartment.setTag(mrTemp.getDepartmentCode());
            txtHDepartment.setText(mrTemp.getDepartmentName());
            txtHFilterType.setTag(mrTemp.getFilterTypeCode());
            txtHFilterType.setText(mrTemp.getFilterTypeName());
            txtHFilterValue.setTag(mrTemp.getFilterValueCode());
            txtHFilterValue.setText(mrTemp.getFilterValueName());
            cbAllowDuplicate.setValue(DmShared.iNull(mrTemp.getIsAllowDuplicate()).intValue() == 1);
            uEntDt_LatestLine = mrTemp.getU_EntDt_LatestLine();
            gridMedicineDetail.getGrid().getStore().clear();
            gridMedicineDetail.getGrid().getStore().addAll(mrTemp.getListMedicineDetail());
            userControl.setTextBlog(preparedByBlog, mrTemp.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, mrTemp.getSharedModifiedBy());
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
        manageDisplay(false);
    }

    private void topControlDelete(String strKey) {
        srvAsync.invokeRPC().deleteRecord(strKey, txtVoucherDate.getText(), txtHDepartment.getTag(), new AsyncCallback<DmResult<String>>() {

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

    private void manageDisplay(boolean flag) {
        txtVoucherNo.setEnabled(false);
        cbAllowDuplicate.setEnabled(flag);

        if (!departmentEditable) {
            txtHDepartment.setEnabled(false);
        }

        if (topControl.getMode().equals("Edit")) {
            txtHDepartment.setEnabled(false);
        }
    }

    private void onAdd() {
        clear();
        manageDisplay(true);
        txtHDepartment.setFocus(true);
        cbAllowDuplicate.setValue(true);
        txtVoucherDate.setText(DmShared.dNull(defaultValue.getCurrentDate()));
        txtVoucherDate.setTag(defaultValue.getVoucherTypeCode());
        txtHDepartment.setTag(defaultValue.getDepartmentTag());
        txtHDepartment.setText(defaultValue.getDepartmentText());
        gridMedicineDetail.getGrid().getStore().add(new GrdMedicineDetail_Mdl());
    }

    private void clear() {
        uEntDt_LatestLine = "";
        cbAllowDuplicate.setValue(false);
        gridMedicineDetail.getGrid().getStore().clear();
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
    }

}
