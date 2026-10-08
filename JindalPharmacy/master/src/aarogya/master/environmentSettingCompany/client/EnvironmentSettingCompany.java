package aarogya.master.environmentSettingCompany.client;

import aarogya.master.environmentSettingCompany.shared.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.HashMap;
import java.util.List;

public final class EnvironmentSettingCompany extends EnvironmentSettingCompany_Dsg implements AttachEvent.Handler, ClickHandler, DmRootEvents {

    private final ServiceInvoker<EnvironmentSettingCompany_SrvAsync> SrvAsync;
    private final PatientInfoGridEdit<GrdPatientInfo_Mdl, GridPatientInfo_PrpAcs> patientInfoGridEdit = new PatientInfoGridEdit(grdPatientInfo);
    private final PriorityGridEdit<GrdPriority_Mdl, GridPriority_PrpAcs> PriorityGridEdit = new PriorityGridEdit(grdPriority);
    private final PatientClassGridEdit<GrdPatientClass_Mdl, GridPatientClass_PrpAcs> patientClassGridEdit = new PatientClassGridEdit(grdPatientClass);

    @Override
    public void onClick(ClickEvent event) {
        try {
            if (event.getSource().equals(btnSave)) {
                topControlSave();
            } else if (event.getSource().equals(btnClose)) {
                onClose();
            } else if (event.getSource().equals(btnExport)) {
                new DmConfirmMessageBox("Are you sure? You want to export records.", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            exportJSON();
                        }
                    }
                }).show();
            }
        } catch (DmException e) {
        }

    }

    private void textHelpEvents() {
        txtHACGroupConsultant.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHACGroup(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHACGroupConsultant).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHACGroupConsultant.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHACGroupConsultant).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHACGroupDepartment.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHACGroup(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHACGroupDepartment).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHACGroupDepartment.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHACGroupDepartment).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHACGroupOrganization.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHACGroup(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHACGroupOrganization).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHACGroupOrganization.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHACGroupOrganization).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHACGroupReferred.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHACGroup(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHACGroupReferred).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHACGroupReferred.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHACGroupReferred).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHACGroupEmployee.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHACGroup(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHACGroupEmployee).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHACGroupEmployee.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHACGroupEmployee).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHACGroupPharmacySupplier.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHACGroup(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHACGroupPharmacySupplier).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHACGroupPharmacySupplier.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHACGroupPharmacySupplier).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHACGroupInventorySupplier.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHACGroup(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHACGroupInventorySupplier).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHACGroupInventorySupplier.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHACGroupInventorySupplier).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHACGroupManufacturer.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHACGroup(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHACGroupManufacturer).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHACGroupManufacturer.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHACGroupManufacturer).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHACGroupDivision.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHACGroup(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHACGroupDivision).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHACGroupDivision.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHACGroupDivision).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHACGroupCustomer.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHACGroup(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHACGroupCustomer).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHACGroupCustomer.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHACGroupCustomer).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        tabPanel.addSelectionHandler(new SelectionHandler<Integer>() {
            public void onSelection(SelectionEvent<Integer> event) {

                if (event.getSelectedItem() == 1) {
//                    fillPatientInfoGrid();
                }
            }
        });

        txtHInventoryAccountingActive.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHYesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHInventoryAccountingActive).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHInventoryAccountingActive.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHInventoryAccountingActive).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });
    }

    private void gridEvents() {
        patientInfoGridEdit.addCallBack(new PatientInfoGridEdit.CallBack() {

            @Override
            public void onOk(GrdPatientInfo_Mdl grdPatientMdl) {
                grdPatientInfo.commitChanges();

            }

            @Override
            public void onDelete() {
                grdPatientInfo.commitChanges();
            }

        });
        grdPatientInfo.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showPatientInfoGridEdit(event.getRowIndex());
            }
        });
        grdPatientInfo.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showPatientInfoGridEdit(grdPatientInfo.getGrid().getStore().indexOf(grdPatientInfo.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
        grdPatientClass.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showPatientClassGridEdit(event.getRowIndex());
            }
        });
        grdPatientClass.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showPatientClassGridEdit(grdPatientClass.getGrid().getStore().indexOf(grdPatientClass.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

        PriorityGridEdit.addCallBack(new PriorityGridEdit.CallBack() {

            @Override
            public void onOk(GrdPriority_Mdl grdPriorityMdl) {
                grdPriority.commitChanges();
            }

            @Override
            public void onDelete() {
                grdPriority.commitChanges();
            }

        });
        grdPriority.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showPriorityGridEdit(event.getRowIndex());
            }
        });
        grdPriority.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showPriorityGridEdit(grdPriority.getGrid().getStore().indexOf(grdPriority.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
    }

    private void showPatientInfoGridEdit(int currentRowIndex) {
        patientInfoGridEdit.show(currentRowIndex, true, false, false);

    }

    private void showPriorityGridEdit(int currentRowIndex) {
        PriorityGridEdit.show(currentRowIndex, true, false, false);

    }

    private void showPatientClassGridEdit(int currentRowIndex) {
        patientClassGridEdit.show(currentRowIndex, true, false, false);

    }

    public EnvironmentSettingCompany(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(EnvironmentSettingCompany_Srv.class), EnvironmentSettingCompany.class.getName());
        this.asWidget().setTitle(widgetName);
        toolContainer.setTitle(this.asWidget().getTitle());
        toolContainer.addWidget(btnSave);
        toolContainer.addWidget(btnExport);
        toolContainer.addWidget(btnClose);
        toolContainer.addGap();
        btnClose.addClickHandler(this);
        btnSave.addClickHandler(this);
        btnCopy.addClickHandler(this);
        btnExport.addClickHandler(this);
        onLoad();
        textHelpEvents();
        gridEvents();
        flwForm.addAttachHandler(this);
        swi.setWidget(this);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private EnvironmentSettingCompany_Mdl topControlSaveInitialize() {
        EnvironmentSettingCompany_Mdl mrTemp = new EnvironmentSettingCompany_Mdl();
        mrTemp.setAcGroupConsCode(DmShared.xNull(txtHACGroupConsultant.getTag()));
        mrTemp.setAcGroupDepartCode(DmShared.xNull(txtHACGroupDepartment.getTag()));
        mrTemp.setAcGroupOrgCode(DmShared.xNull(txtHACGroupOrganization.getTag()));
        mrTemp.setAcGroupOrgName(DmShared.xNull(txtHACGroupOrganization.getText()));
        mrTemp.setAcGroupRefCode(DmShared.xNull(txtHACGroupReferred.getTag()));
        mrTemp.setAcGroupRefName(DmShared.xNull(txtHACGroupReferred.getText()));
        mrTemp.setAcGroupEmpCode(DmShared.xNull(txtHACGroupEmployee.getTag()));
        mrTemp.setAcGroupEmpName(DmShared.xNull(txtHACGroupEmployee.getText()));
        mrTemp.setAcGroupPharmacySupplierCode(DmShared.xNull(txtHACGroupPharmacySupplier.getTag()));
        mrTemp.setAcGroupPharmacySupplierName(DmShared.xNull(txtHACGroupPharmacySupplier.getText()));
        mrTemp.setAcGroupInventorySupplierCode(DmShared.xNull(txtHACGroupInventorySupplier.getTag()));
        mrTemp.setAcGroupInventorySupplierName(DmShared.xNull(txtHACGroupInventorySupplier.getText()));
        mrTemp.setAcGroupManufacturerCode(DmShared.xNull(txtHACGroupManufacturer.getTag()));
        mrTemp.setAcGroupManufacturerName(DmShared.xNull(txtHACGroupManufacturer.getText()));
        mrTemp.setAcGroupDivisionCode(DmShared.xNull(txtHACGroupDivision.getTag()));
        mrTemp.setAcGroupDivisionName(DmShared.xNull(txtHACGroupDivision.getText()));
        mrTemp.setAcGroupCustomerCode(DmShared.xNull(txtHACGroupCustomer.getTag()));
        mrTemp.setAcGroupCustomerName(DmShared.xNull(txtHACGroupCustomer.getText()));
        mrTemp.setInventoryAccountingActive(DmShared.xNull(txtHInventoryAccountingActive.getTag()));
    
        mrTemp.getGrdPatientInfoList().clear();
        mrTemp.getGrdPatientInfoList().addAll(grdPatientInfo.getGrid().getStore().getAll());
        
        mrTemp.getGrdPatientClassList().clear();
        mrTemp.getGrdPatientClassList().addAll(grdPatientClass.getGrid().getStore().getAll());

        mrTemp.getGrdPriorityList().clear();
        mrTemp.getGrdPriorityList().addAll(grdPriority.getGrid().getStore().getAll());

        return mrTemp;
    }

    private void topControlSave() {
        EnvironmentSettingCompany_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            EnvironmentSettingCompany_Vld.chkSave(mrTemp);

            SrvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        onLoad();
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void topControlSaveError(int errorNo, String errorMessage) {

        switch (errorNo) {

            case 100:
                new DmAlertMessageBox(errorMessage, txtHACGroupConsultant).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtHACGroupDepartment).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtHACGroupOrganization).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtHACGroupReferred).show();
                break;
            case 600:
                new DmAlertMessageBox(errorMessage, txtHACGroupEmployee).show();
                break;
            case 700:
                new DmAlertMessageBox(errorMessage, txtHACGroupPharmacySupplier).show();
                break;
            case 800:
                new DmAlertMessageBox(errorMessage, txtHACGroupInventorySupplier).show();
                break;
            case 900:
                new DmAlertMessageBox(errorMessage, txtHACGroupManufacturer).show();
                break;
            case 1000:
                new DmAlertMessageBox(errorMessage, txtHACGroupDivision).show();
                break;
            case 1100:
                new DmAlertMessageBox(errorMessage, txtHACGroupCustomer).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, (DmTextBox) null).show();
        }

    }

    private void topControlMoveRec(String strKey) {
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<EnvironmentSettingCompany_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<EnvironmentSettingCompany_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<EnvironmentSettingCompany_Mdl> result) {
        EnvironmentSettingCompany_Mdl data;

        if (result.isStatus()) {
            EnvironmentSettingCompany_Mdl mrTemp;
            mrTemp = result.getData();
            txtHACGroupConsultant.setTag(DmShared.xNull(mrTemp.getAcGroupConsCode()));
            txtHACGroupConsultant.setText(DmShared.xNull(mrTemp.getAcGroupConsName()));
            txtHACGroupDepartment.setTag(DmShared.xNull(mrTemp.getAcGroupDepartCode()));
            txtHACGroupDepartment.setText(DmShared.xNull(mrTemp.getAcGroupDepartName()));
            txtHACGroupOrganization.setTag(DmShared.xNull(mrTemp.getAcGroupOrgCode()));
            txtHACGroupOrganization.setText(DmShared.xNull(mrTemp.getAcGroupOrgName()));
            txtHACGroupReferred.setTag(DmShared.xNull(mrTemp.getAcGroupRefCode()));
            txtHACGroupReferred.setText(DmShared.xNull(mrTemp.getAcGroupRefName()));
            txtHACGroupEmployee.setTag(DmShared.xNull(mrTemp.getAcGroupEmpCode()));
            txtHACGroupEmployee.setText(DmShared.xNull(mrTemp.getAcGroupEmpName()));
            txtHACGroupPharmacySupplier.setTag(DmShared.xNull(mrTemp.getAcGroupPharmacySupplierCode()));
            txtHACGroupPharmacySupplier.setText(DmShared.xNull(mrTemp.getAcGroupPharmacySupplierName()));
            txtHACGroupInventorySupplier.setTag(DmShared.xNull(mrTemp.getAcGroupInventorySupplierCode()));
            txtHACGroupInventorySupplier.setText(DmShared.xNull(mrTemp.getAcGroupInventorySupplierName()));
            txtHACGroupManufacturer.setTag(DmShared.xNull(mrTemp.getAcGroupManufacturerCode()));
            txtHACGroupManufacturer.setText(DmShared.xNull(mrTemp.getAcGroupManufacturerName()));
            txtHACGroupDivision.setTag(DmShared.xNull(mrTemp.getAcGroupDivisionCode()));
            txtHACGroupDivision.setText(DmShared.xNull(mrTemp.getAcGroupDivisionName()));
            txtHACGroupCustomer.setTag(DmShared.xNull(mrTemp.getAcGroupCustomerCode()));
            txtHACGroupCustomer.setText(DmShared.xNull(mrTemp.getAcGroupCustomerName()));

            if (DmShared.xNull(mrTemp.getInventoryAccountingActive()).equalsIgnoreCase("0")) {
                txtHInventoryAccountingActive.setTag("0");
                txtHInventoryAccountingActive.setText("No");
            } else if (DmShared.xNull(mrTemp.getInventoryAccountingActive()).equalsIgnoreCase("1")) {
                txtHInventoryAccountingActive.setTag("1");
                txtHInventoryAccountingActive.setText("Yes");
            }
            
            grdPatientInfo.getGrid().getStore().clear();
            grdPatientInfo.getGrid().getStore().addAll(mrTemp.getGrdPatientInfoList());

            
            grdPatientClass.getGrid().getStore().clear();
            grdPatientClass.getGrid().getStore().addAll(mrTemp.getGrdPatientClassList());

            grdPriority.getGrid().getStore().clear();
            grdPriority.getGrid().getStore().addAll(mrTemp.getGrdPriorityList());

        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {

        if (!event.isAttached()) {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            patientInfoGridEdit.hide();
        } else {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(EnvironmentSettingCompany.this);
                }
            }
        }).show();
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            onClose();
        }
    }

    private void onLoad() {
        topControlMoveRec(null);
    }

    private void exportJSON() {
        SrvAsync.invokeRPC().exportJSON(DmRequestBuilder.getSessionId(), new AsyncCallback<DmResult<KeyFieldExtended>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    KeyFieldExtended field = result.getData();
                    startDownloading(DmShared.xNull(field.getSharedKeyFieldName()), DmShared.xNull(field.getSharedKeyFieldCode()));
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), btnExport).show();
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
