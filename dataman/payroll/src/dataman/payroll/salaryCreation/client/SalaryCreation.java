package dataman.payroll.salaryCreation.client;

import dataman.payroll.salaryCreation.shared.*;
import dataman.payroll.salaryCreation.shared.SalaryCreation_Srv;
import dataman.payroll.salaryCreation.shared.SalaryCreation_SrvAsync;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import java.util.List;
import dataman.dmToolkit.walkThrough.client.WalkThrough;
import java.util.ArrayList;

import java.util.HashMap;

public class SalaryCreation extends SalaryCreation_Dsg implements IsWidget, AttachEvent.Handler, ClickHandler, DmRootEvents {

    ServiceInvoker<SalaryCreation_SrvAsync> SrvAsync;

    WalkThrough walkThroughPBill;
    WalkThrough walkThroughPBillDirect;
    String category = "";
    SetWidgetInvoker swi;
    private List<GrdSalaryCreation_Mdl> grdSalary = new ArrayList<GrdSalaryCreation_Mdl>();
    KeyFieldExtended defaultValues = new KeyFieldExtended();

    private void textHelpEvents() {
        txtHSalaryMethod.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSalaryMethod(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSalaryMethod).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSalaryMethod.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSalaryMethod).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (t1Row != null) {
                    txtHSalaryMethod.setTag2nd(t1Row.getSharedModifiedBy());
                    txtFromToDate.setText(t1Row.getSharedModifiedBy());
                    txtFromToDate.setTag(t1Row.getSharedPreparedBy());
                }
            }
        });
        txtHDepartmentName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHDepartmentName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDepartmentName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDepartmentName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDepartmentName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });
    }

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());

        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);

        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                clear();
                txtHSalaryMethod.setFocus(true);
                onAdd();
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                txtHSalaryMethod.setFocus(true);
                manageDisplay();
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

                topControlSave();
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(SalaryCreation.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void gridEvents() {

    }

    private void getEmployeeDetail(SalaryCreation_Mdl scMdl) {

        SrvAsync.invokeRPC().getFillRecords(scMdl, new AsyncCallback<List<GrdSalaryCreation_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(List<GrdSalaryCreation_Mdl> result) {

                dmGrd.getGrid().getStore().clear();
                dmGrd.getGrid().getStore().addAll(result);

                dmGrd.commitChanges();
            }
        });

    }

    private void clear() {

        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
    }

    private SalaryCreation_Mdl topControlSaveInitialize() {
        SalaryCreation_Mdl mrTemp = new SalaryCreation_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());

        }
        mrTemp.getGrdSalaryCreationList().addAll(new ArrayList<GrdSalaryCreation_Mdl>()); // initialise the list
        mrTemp.getGrdSalaryCreationList().addAll(dmGrd.getGrid().getStore().getAll()); // filling the value into list.
        if (mrTemp.getGrdSalaryCreationList().size() == 0) {
            if (txtHSalaryMethod.getText().equalsIgnoreCase("")) {
                mrTemp.setSalaryMethodName(DmShared.xNull(null));
                mrTemp.setSalaryMethodCode(DmShared.xNull(null));
            } else {
                mrTemp.setSalaryMethodName(DmShared.xNull(txtHSalaryMethod.getText()));
                mrTemp.setSalaryMethodCode(DmShared.xNull(txtHSalaryMethod.getTag()));
            }

        } else {
            mrTemp.setSalaryMethodName(DmShared.xNull(txtHSalaryMethod.getText()));
            mrTemp.setSalaryMethodCode(DmShared.xNull(txtHSalaryMethod.getTag()));
            mrTemp.setDepartmentName(txtHDepartmentName.getText());
            mrTemp.setDepartmentCode(txtHDepartmentName.getTag());
        }
        return mrTemp;
    }

    private void topControlSaveError(int errorNo, String errorMessage) {

        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtHSalaryMethod).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtHDepartmentName).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHSalaryMethod).show();
        }
    }

    private void topControlSave() {
        SalaryCreation_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            SrvAsync.invokeRPC().saveRecord(mrTemp, DmClient.getVoucherPrefix(), new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHSalaryMethod).show();
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
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void manageDisplay() {
        flwButtonFillDetails.clear();
        if (topControl.getMode().trim().equalsIgnoreCase("New")) {
            flwButtonFillDetails.add(btnFillDetails);
        }
    }

    private void onAdd() {
        clear();
        dmGrd.getGrid().getStore().clear();
        manageDisplay();

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
                    new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                }
            }
        });
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
                        topControl.setNavigator(result.getData()); // here setNavigator set docId or update docId
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            }
        });
    }

    private void topControlMoveRec(String strKey) {
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<SalaryCreation_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<SalaryCreation_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<SalaryCreation_Mdl> result) {
        manageDisplay();
        SalaryCreation_Mdl data;
        if (result.isStatus()) {

            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());
            List<GrdSalaryCreation_Mdl> grdSalaryCreationMdlList = new ArrayList<GrdSalaryCreation_Mdl>();
            grdSalaryCreationMdlList = data.getGrdSalaryCreationList();

            dmGrd.getGrid().getStore().clear();
            dmGrd.getGrid().getStore().addAll(data.getGrdSalaryCreationList());

            txtHSalaryMethod.setText(data.getSalaryMethodName());
            txtHDepartmentName.setText(data.getDepartmentName());
            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());

        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(SalaryCreation.this);
                }
            }
        }).show();
    }

    @Override
    public void onClick(ClickEvent event) {
        SalaryCreation_Mdl scMdl = new SalaryCreation_Mdl();
        if (event.getSource() == btnFillDetails) {
            if (!DmShared.xNull(txtHSalaryMethod.getText()).equalsIgnoreCase("")) {
                scMdl.setSalaryMethodName(DmShared.xNull(txtHSalaryMethod.getText()));
                scMdl.setSalaryMethodCode(DmShared.xNull(txtHSalaryMethod.getTag()));
                scMdl.setDepartmentCode(DmShared.xNull(txtHDepartmentName.getTag()));
                scMdl.setFromDate(DmShared.xNull(txtFromToDate.getText()));
                scMdl.setToDate(DmShared.xNull(txtFromToDate.getTag()));
                getEmployeeDetail(scMdl);
            } else {
                new DmAlertMessageBox("Salary Method is Mandatory.", txtHSalaryMethod).show();
            }
        }

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

    public SalaryCreation(UserMenu menuPermission, HashMap paramHashMap, SetWidgetInvoker swi) {
        super();
        this.swi = swi;
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("E", "*"));
        SrvAsync = new ServiceInvoker<SalaryCreation_SrvAsync>(GWT.create(SalaryCreation_Srv.class), SalaryCreation.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        flwForm.addAttachHandler(this);
        btnFillDetails.addClickHandler(this);
        textHelpEvents();
        findEvents();
        iniDefaultValues(swi);
    }

    private void iniDefaultValues(final SetWidgetInvoker swi) {
        SrvAsync.invokeRPC().getDefaultValues(new AsyncCallback<DmResult<KeyFieldExtended>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHSalaryMethod).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    defaultValues = result.getData();
                    swi.setWidget(SalaryCreation.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHSalaryMethod).show();
                }
            }
        });
    }
    
    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            onClose();
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {

        if (!event.isAttached()) {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
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

    private void prnRecord(boolean printToPrinter) {
        if (DmShared.iNull(topControl.getDocId()).longValue() != 0) {
            HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
            hashMapFilter.put("docId", new FieldUDT_Mdl("Salary Creation", DmShared.xNull(txtHSalaryMethod.getText()), DmShared.xNull(topControl.getDocId())));
            new OpenSourceReport(DmShared.xNull(defaultValues.getSharedKeyFieldCode()), "SalaryCreation", widgetName, widgetName, hashMapFilter, printToPrinter, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
        } else {
            new DmAlertMessageBox("Please Select Record", txtHSalaryMethod).show();
            return;
        }
    }
}
