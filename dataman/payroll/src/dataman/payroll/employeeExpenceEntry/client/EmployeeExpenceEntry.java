package dataman.payroll.employeeExpenceEntry.client;

import dataman.payroll.employeeExpenceEntry.shared.*;
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
import com.google.gwt.user.client.ui.PopupPanel;
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
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import dataman.libraryMenu.self.shared.lmPayroll;
import java.util.HashMap;
import java.util.List;

public final class EmployeeExpenceEntry extends EmployeeExpenceEntry_Dsg implements ClickHandler, BlurHandler {

    private final ServiceInvoker<EmployeeExpenceEntry_SrvAsync> SrvAsync;
    private final EmployeeExpenceEntryGridEdit<GridEmployeeExpenceEntry_Mdl, GrdTabIndex_PrpAcs> employeeExpenceEntryGridEdit = new EmployeeExpenceEntryGridEdit(grdLogDetail);
    double total = 0;
    private String docIdTag, LabNoText;
    private EmployeeExpenceEntry employeeExpenceEntry;
    private final String moduleName = new lmPayroll().moduleName;
    private final String v_Type = "";
    private boolean printRpt = false;
    private boolean blnAddEdit = false;
    private CallBack callBack;
    final PopupPanel popupPanel = new PopupPanel();
    private String vv_type = " ";

    public interface CallBack {

        public void onOk(boolean callOnSave);

    }

    private void onAdd() {

        clear();
        blnAddEdit = true;
        txtDate.setFocus(true);
        getRecords();
    }

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());

        topControl.setRefreshRequired(false);
        topControl.setOtherRequired(false);
        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                onAdd();
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                blnAddEdit = true;
                txtDate.setFocus(true);
            }
        });

        topControl.addDeleteHandeller(new DmTopControl.eDelete() {
            @Override
            public void deleteClicked() {
                new DmConfirmMessageBox("Are you sure? You want to delete this record.", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
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
            public void moveRec(String strFetchCode) {
                topControlMoveRec(strFetchCode);
            }
        });

        topControl.addPrintHandeller(new DmTopControl.ePrint() {

            @Override
            public void printClicked() {
                prnRecord(printRpt);

            }
        });

        topControl.addSaveHandeller(new DmTopControl.eSave() {
            @Override
            public void saveClicked() {
                controlSave();
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(EmployeeExpenceEntry.this);
            }
        });

        topControl.setDisplay(true);
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtDate)) {
            txtDate.setText(DmClient.returnDate(txtDate.getText()));
        }
    }

    private void textHelpEvents() {

        txtHEmployee.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<Employee_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHEmployee(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Employee_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHEmployee).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<Employee_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHEmployee.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHEmployee).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, Employee_Mdl t1Row) {

            }
        });

        txtHSalaryMethod.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<SalaryMethod_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSalaryMethod(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<SalaryMethod_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSalaryMethod).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<SalaryMethod_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHSalaryMethod.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSalaryMethod).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, SalaryMethod_Mdl t1Row) {
                txtHSalaryMethod.setTag2nd(t1Row.getFromDate());
                txtHSalaryMethod.setText(t1Row.getSalaryMethodName());
                txtHSalaryMethod.setTag(t1Row.getSharedKeyFieldCode());
            }
        });

    }

    private void findEvents() {
        dmFind.setTitle(this.asWidget().getTitle());

        dmFind.addHelpGridCallBack(new DmFind.FindCallBack<Find_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getFind(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Find_Mdl>>>() {

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

    public void calculate() {
        double addVar = 0, subVar = 0, test = 0;

        for (GridEmployeeExpenceEntry_Mdl grdEmpExpEn_Mdl : grdLogDetail.getGrid().getStore().getAll()) {
            if (!DmShared.xNull(grdEmpExpEn_Mdl.getValue()).equalsIgnoreCase("")) {
                if (DmShared.xNull(grdEmpExpEn_Mdl.getNature()).trim().equalsIgnoreCase("+")) {
                    addVar += DmShared.vNull(grdEmpExpEn_Mdl.getValue());
                } else if (DmShared.xNull(grdEmpExpEn_Mdl.getNature()).trim().equalsIgnoreCase("-")) {
                    subVar += DmShared.vNull(grdEmpExpEn_Mdl.getValue());
                }

            }
            total = addVar - subVar;
            totalVal.setText(DmClient.vNull(total, "0.00"));
        }

    }

    public EmployeeExpenceEntry(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(EmployeeExpenceEntry_Srv.class), EmployeeExpenceEntry.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        topControl.setOtherRequired(false);

        textHelpEvents();
        findEvents();
        gridEvents();
        txtDate.addBlurHandler(this);

        swi.setWidget(this);

    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private EmployeeExpenceEntry_Mdl topControlSaveInitialize() {
        EmployeeExpenceEntry_Mdl mrTemp = new EmployeeExpenceEntry_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }

        mrTemp.setSharedKeyFieldName(DmShared.xNull(DmShared.xNull(txtHEmployee.getText())));
        mrTemp.setEntryDate(DmShared.dNull(DmShared.xNull(txtDate.getText())));

        mrTemp.setEmployeeText(DmShared.xNull(txtHEmployee.getText()));
        mrTemp.setEmployeeTag(DmShared.xNull(txtHEmployee.getTag()));

        mrTemp.setSalaryPeriodText(DmShared.xNull(txtHSalaryMethod.getText()));
        mrTemp.setSalaryPeriodTag(DmShared.xNull(txtHSalaryMethod.getTag()));

        mrTemp.setSharedPreparedBy(userControl.getTextBlog("Prepared By"));
        mrTemp.setSharedPreparedBy(userControl.getTextBlog("Modified By"));

        grdLogDetail.commitChanges();
        mrTemp.getGrdDetailMdl().addAll(grdLogDetail.getGrid().getStore().getAll());

        return mrTemp;
    }

    private void topControlSave() {
        EmployeeExpenceEntry_Mdl mrTemp;
        try {
            mrTemp = topControlSaveInitialize();
            EmployeeExpenceEntry_Vld.chkSave(mrTemp);

            SrvAsync.invokeRPC().saveRecord(topControl.getPermissions(), mrTemp, DmClient.getVoucherPrefix(), new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHEmployee).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        topControl.setDocId(result.getData());

                        if (topControl.getMode().equalsIgnoreCase("New")) {
                            topControl.btnClick("New");
                            printRpt = true;
                        } else {
                            topControlMoveRec(topControl.getDocId());
                        }
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });

        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }

    }

    private void controlSave() {
        if (DmShared.iNull(totalVal.getText()).intValue() < 0) {
            new DmConfirmMessageBox("Are you sure? Do you want to continue with Negative value...", new DialogHideEvent.DialogHideHandler() {

                @Override
                public void onDialogHide(DialogHideEvent event) {

                    if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                        topControlSave();
                    }
                }
            }).show();

        } else {
            topControlSave();
        }
    }

    private void topControlSaveError(int errorNo, String errorMessage) {
        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtDate).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtHEmployee).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtHSalaryMethod).show();
                break;

            default:
                new DmAlertMessageBox(errorMessage, txtDate).show();
                break;
        }
    }

    private void topControlCard() {
        SrvAsync.invokeRPC().getNavigator(new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
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
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<EmployeeExpenceEntry_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<EmployeeExpenceEntry_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<EmployeeExpenceEntry_Mdl> result) {
        EmployeeExpenceEntry_Mdl data;

        if (result.isStatus()) {
            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());

            txtDate.setText(data.getEntryDate());

            vv_type = data.getVoucherTypeCode();

            txtHEmployee.setText(data.getEmployeeText());
            txtHEmployee.setTag(data.getEmployeeTag());

            txtHSalaryMethod.setText(data.getSalaryPeriodText());
            txtHSalaryMethod.setTag(data.getSalaryPeriodTag());

            grdLogDetail.getGrid().getStore().clear();
            grdLogDetail.getGrid().getStore().addAll(data.getGrdDetailMdl());

            calculate();

            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        blnAddEdit = false;
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

    private void getRecords() {
        SrvAsync.invokeRPC().getRecords(new AsyncCallback<DmResult<List<GridEmployeeExpenceEntry_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GridEmployeeExpenceEntry_Mdl>> result) {
                if (result.isStatus()) {
                    clear();

                    grdLogDetail.getGrid().getStore().clear();
                    grdLogDetail.getGrid().getStore().addAll(result.getData());

                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void clear() {
        grdLogDetail.getGrid().getStore().clear();
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        totalVal.setText("0.00");
    }

    @Override
    public void onClick(ClickEvent event) {
    }

    private void showValueGrid(Integer currentRowIndex) {
        employeeExpenceEntryGridEdit.show(currentRowIndex, blnAddEdit, false, false, false, this);
    }

    private void gridEvents() {
        grdLogDetail.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showValueGrid(grdLogDetail.getGrid().getStore().indexOf(grdLogDetail.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

        employeeExpenceEntryGridEdit.addCallBack(new EmployeeExpenceEntryGridEdit.CallBack() {

            @Override
            public void onOk(GridEmployeeExpenceEntry_Mdl opdService) {

                grdLogDetail.commitChanges();
                calculate();

            }

            @Override
            public void onDelete() {
                grdLogDetail.commitChanges();

            }

        }
        );

        grdLogDetail.getGrid()
                .addRowClickHandler(new RowClickEvent.RowClickHandler() {

                    @Override

                    public void onRowClick(RowClickEvent event
                    ) {
                        if (event.getRowIndex() <= grdLogDetail.getGrid().getStore().getAll().size()) {
                            if (event.getColumnIndex() == 3) {

                                showEmployeeExpenceEntryGridEdit(event.getRowIndex(), true);
                            }
                        }
                    }
                }
                );

    }

    private void showEmployeeExpenceEntryGridEdit(int currentRowIndex, boolean isDeleteFocus) {
        if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {
            employeeExpenceEntryGridEdit.show(currentRowIndex, false, false, false, isDeleteFocus, this);
        } else {
            employeeExpenceEntryGridEdit.show(currentRowIndex, true, false, false, isDeleteFocus, this);
        }
    }

    public void hide() {
        this.popupPanel.hide();
    }

    private void hide(boolean callOnSave) {
        hide();
        callBack.onOk(callOnSave);
    }

    private void prnRecord(boolean callOnSave) {

        HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
        hashMapFilter.put("docId", new FieldUDT_Mdl("", "", DmShared.xNull(topControl.getDocId())));
        new OpenSourceReport(vv_type, "", "PERKS Entry", widgetName, hashMapFilter, callOnSave, new OpenSourceReport.CallBack() {
            @Override
            public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                DmClient.localSettingPrintMap(reportProperty, moduleName, widgetName, "");
                return reportProperty;
            }
        }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));

        hide(callOnSave);
    }

}
