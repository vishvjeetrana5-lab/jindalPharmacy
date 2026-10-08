package aarogya.master.employeeMaster.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.master.employeeMaster.shared.*;
import aarogya.projectMenu.self.shared.ProjectRuleCode;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmSnapshot.shared.DmSnapshot_Mdl;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import dataman.dmToolkit.dmFileManager.client.DmFileManager;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmToolkit.manageSite.client.ManageSite;
import dataman.dmToolkit.self.client.SetWidgetInvokerImpl;
import dataman.libraryMenu.self.shared.lmAccounts;
import java.util.HashMap;
import java.util.List;

public final class EmployeeMaster extends EmployeeMaster_Dsg implements KeyPressHandler, ValueChangeHandler<String>, AttachEvent.Handler, BlurHandler, ClickHandler {

    private DefaultValue_Mdl defaultValue;
    private final ServiceInvoker<EmployeeMaster_SrvAsync> SrvAsync;
    private final ManageSite manageSite = new ManageSite();
    final DmFileManager fileManager = new DmFileManager(ProjectRuleCode.EMPLOYEE.getRuleCode());
    boolean isAttachFiles = false, isDeleteFiles = false;

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());

        topControl.setRefreshRequired(false);

        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);
        for (String controlPermission : menuPermission.getControlPermission()) {
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(attachFileBlog)) {
                isAttachFiles = true;
            }
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(deleteFileBlog)) {
                isDeleteFiles = true;
            }
        }
        topControl.setDisplay(true);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                clear();
                onAdd();
                txtManualCode.setEnabled(false);
                txtName.setFocus(true);
                btnCopyAddress.setVisible(true);

            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                txtManualCode.setEnabled(true);
                txtCountry.setEnabled(false);
                txtCountryTemp.setEnabled(false);
                txtHActive.setEnabled(true);
                txtName.setFocus(true);
                btnCopyAddress.setVisible(true);
                grdBankDetail.getGrid().getStore().add(new GrdBankDetail_Mdl());
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

            }
        });

        topControl.addSaveHandeller(new DmTopControl.eSave() {
            @Override
            public void saveClicked() {
                topControlSave();
            }
        });

        topControl.addOtherHandeller(new DmTopControl.eOther() {

            @Override
            public void otherClicked() {
                menu.show(topControl.getButton("Other"));
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(EmployeeMaster.this);
            }
        });
    }

    private void gridEvents() {

        grdBankDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                int r1 = event.getRowIndex();
                String subCode = grdBankDetail.getGrid().getStore().get(r1).getSharedKeyFieldCode();
                String sNo = grdBankDetail.getGrid().getStore().get(r1).getSharedKeyFieldName();
                showBankDetail(DmShared.xNull(subCode), DmShared.xNull(sNo));

            }
        });
    }

    private void textHelpEvents() {
        txtName.addCallBack(new DmSuggestBox.CallBack() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getAccountNameSuggestion(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<String>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<String>> result) {
                        if (result.isStatus()) {
                            txtName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtName).show();
                        }
                    }
                });
            }
        });
        txtHAccountNamePrefix.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAccountNamePrefix(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAccountNamePrefix).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHAccountNamePrefix.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHAccountNamePrefix).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }

        });
        txtHAccountGroup.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHAccountGroup_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAccountGroup(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHAccountGroup_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAccountGroup).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHAccountGroup_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHAccountGroup.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHAccountGroup).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHAccountGroup_Mdl tRow) {
                if (blnStatus && tRow != null) {
                    txtHNature.setTag(tRow.getNatureCode());
                    txtHNature.setText(tRow.getNatureName());
                } else {
                    txtHNature.setTag("");
                    txtHNature.setText("");
                }
            }
        });
        txtHDesignation.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHDesignation(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDesignation).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDesignation.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDesignation).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {

            }
        });
        txtHCity.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHCity_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHCity(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHCity_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCity).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHCity_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHCity.setData(result.getData(), result.getPaging());

                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCity).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHCity_Mdl tRow) {
                if (blnStatus && tRow != null) {
                    txtCountry.setTag(DmShared.xNull(tRow.getCountryCode()));
                    txtCountry.setText(DmShared.xNull(tRow.getCountryName()));
                }
            }
        });

        txtHCityTemp.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHCity_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHCity(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHCity_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCityTemp).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHCity_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHCityTemp.setData(result.getData(), result.getPaging());

                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCityTemp).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHCity_Mdl tRow) {
                txtCountryTemp.setTag(DmShared.xNull(tRow.getCountryCode()));
                txtCountryTemp.setText(DmShared.xNull(tRow.getCountryName()));
            }
        });

        txtHZone.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHZone(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHZone).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        try {
                            if (result.isStatus()) {
                                txtHZone.setData(result.getData(), result.getPaging());
                            }
                        } catch (Exception e) {
                            new DmAlertMessageBox(e.getMessage(), txtHZone).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {

            }
        });
        txtHTDSCategory.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHTDSCategory(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHTDSCategory).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        try {
                            if (result.isStatus()) {
                                txtHTDSCategory.setData(result.getData(), result.getPaging());
                            }
                        } catch (Exception e) {
                            new DmAlertMessageBox(e.getMessage(), txtHTDSCategory).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {

            }
        });
        txtHNature.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHNature(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHNature).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHNature.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHNature).show();
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
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
        txtHMaritalStatus.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHMaritalStatus(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMaritalStatus).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHMaritalStatus.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMaritalStatus).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHGender.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSex(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHGender).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHGender.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHGender).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus && t1Row != null) {

                }
            }
        });
        txtHBloodGroup.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHBloodGroup(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHBloodGroup).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHBloodGroup.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHBloodGroup).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHReligion.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHReligion(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHReligion).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHReligion.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHReligion).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHActive.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHActive(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHActive).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHActive.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHActive).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHActive.addKeyDownHandler(new DmTextHelp.TxtHelpKeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event, Object objSource) {

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
                    topControlMoveRec(tRow.getCode());
                }
            }
        });
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {

        } else {
            manageSite.hide();
        }
    }

    @Override
    public void onKeyPress(KeyPressEvent event) {

    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        if (event.getSource().equals(txtMobileNo)) {
            if (!txtMobileNo.getText().trim().equalsIgnoreCase("")) {
                txtMobileNo.setText(event.getValue());
            }
        } else if (event.getSource().equals(txtPhoneNo)) {
            if (!txtPhoneNo.getText().trim().equalsIgnoreCase("")) {
                txtPhoneNo.setText(event.getValue());
            }
        }
    }

    private void manageEvents() {
        txtMobileNo.addValueChangeHandler(this);
        txtMobileNo.addKeyPressHandler(this);
        txtPhoneNo.addValueChangeHandler(this);
        txtPhoneNo.addKeyPressHandler(this);
        txtName.addBlurHandler(this);
        txtDob.addBlurHandler(this);
        txtDOA.addBlurHandler(this);
        txtDOJ.addBlurHandler(this);
        txtDOR.addBlurHandler(this);
        txtPassportIssueDate.addBlurHandler(this);
        txtPassportExpiryDate.addBlurHandler(this);
        txtLicenseExpiry.addBlurHandler(this);
        btnCopyAddress.addClickHandler(this);
        btnCopyAddress.setVisible(false);

    }

    private void iniDefaultValue(final SetWidgetInvoker swi) {
        SrvAsync.invokeRPC().getDefaultValue(new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    defaultValue = result.getData();
                    swi.setWidget(EmployeeMaster.this);
                    txtHActive.setEnabled(false);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
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
                        topControl.setNavigator(result.getData());
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            }
        });
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

    private EmployeeMaster_Mdl topControlSaveInitialize() {
        EmployeeMaster_Mdl mrTemp = new EmployeeMaster_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }
        mrTemp.setTempKeyCode(fileManager.getTempKeyCode());
        mrTemp.setAccountNamePrefix(DmShared.xNull(txtHAccountNamePrefix.getText()));
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtName.getText()));
        mrTemp.setDisplayName(DmShared.xNull(txtDisplayName.getText()));
        mrTemp.setGenderCode(DmShared.xNull(txtHGender.getTag()));
        mrTemp.setGenderName(DmShared.xNull(txtHGender.getText()));
        mrTemp.setManualCode(DmShared.xNull(txtManualCode.getText()));
        mrTemp.setMaritalStatusName(DmShared.xNull(txtHMaritalStatus.getText()));
        mrTemp.setMaritalStatusCode(DmShared.xNull(txtHMaritalStatus.getTag()));
        mrTemp.setReligionCode(DmShared.xNull(txtHReligion.getTag()));
        mrTemp.setReligion(DmShared.xNull(txtHReligion.getText()));
        mrTemp.setZone(DmShared.xNull(txtHZone.getTag()));
        mrTemp.setAccGroup(DmShared.xNull(txtHAccountGroup.getTag()));
        mrTemp.setAccGroupName(DmShared.xNull(txtHAccountGroup.getText()));
        mrTemp.setNatureCode(DmShared.xNull(txtHNature.getTag()));
        mrTemp.setNatureName(DmShared.xNull(txtHNature.getText()));
        mrTemp.setDesignation(DmShared.xNull(txtHDesignation.getTag()));
        mrTemp.setDepartmentCode(DmShared.xNull(txtHDepartment.getTag()));
        mrTemp.setDepartmentName(DmShared.xNull(txtHDepartment.getText()));
        mrTemp.setFatherName(DmShared.xNull(txtFatherName.getText()));
        mrTemp.setPhoneNo(DmShared.xNull(txtPhoneNo.getText()));
        mrTemp.setMobileNo(DmShared.xNull(txtMobileNo.getText()));
        mrTemp.setAadhaarNo(DmShared.xNull(txtAadhaarNo.getText()));
        mrTemp.setAddress(DmShared.xNull(txtAddress.getText()));
        mrTemp.setAddress1(DmShared.xNull(txtAddress1.getText()));
        mrTemp.setCity(DmShared.xNull(txtHCity.getTag()));
        mrTemp.setSalary(DmShared.xNull(txtSalary.getText()));
//        mrTemp.setCountry(DmShared.xNull(txtHCountry.getTag()));
        mrTemp.setPin(DmShared.xNull(txtPinCode.getText()));
        mrTemp.setAddressTemp(DmShared.xNull(txtAddresstemp.getText()));
        mrTemp.setAddress1Temp(DmShared.xNull(txtAddress1temp.getText()));
        mrTemp.setCityTemp(DmShared.xNull(txtHCityTemp.getTag()));
//        mrTemp.setCountryTemp(DmShared.xNull(txtHCountryTemp.getTag()));
        mrTemp.setPinTemp(DmShared.xNull(txtPinCodetemp.getText()));
        mrTemp.setDob(DmShared.dNull(txtDob.getText()));
        mrTemp.setDoa(DmShared.dNull(txtDOA.getText()));
        mrTemp.setDoj(DmShared.dNull(txtDOJ.getText()));
        mrTemp.setDor(DmShared.dNull(txtDOR.getText()));
        mrTemp.setBloodGroupCode(DmShared.xNull(txtHBloodGroup.getTag()));
        mrTemp.setBloodGroupName(DmShared.xNull(txtHBloodGroup.getText()));
        mrTemp.setPan(DmShared.xNull(txtPAN.getText()));
        mrTemp.setIfsccode(DmShared.xNull(txtifscode.getText()));
        mrTemp.setPfAccNo(DmShared.xNull(txtPfAccNo.getText()));
        mrTemp.setBankAccNo(DmShared.xNull(txtBankAccNo.getText()));
        mrTemp.setLicenseNo(DmShared.xNull(txtLicenseNo.getText()));
        mrTemp.setLicenseExpiry(DmShared.dNull(txtLicenseExpiry.getText()));
        mrTemp.setPassportNo(DmShared.xNull(txtPassporNo.getText()));
        mrTemp.setPassportIssueDate(DmShared.dNull(txtPassportIssueDate.getText()));
        mrTemp.setPassportExpirydDate(DmShared.dNull(txtPassportExpiryDate.getText()));
        mrTemp.setPassportIssuePlace(DmShared.xNull(txtPassportIssuePlace.getText()));
        mrTemp.setEmail(DmShared.xNull(txtEMail.getText()).trim());
        mrTemp.setQualification(DmShared.xNull(txtQualification.getText()));
        mrTemp.setTdsCategory(DmShared.xNull(txtHTDSCategory.getTag()));
        mrTemp.setRemark(DmShared.xNull(txtRemark.getText()).trim());
        mrTemp.setActiveTag(DmShared.xNull(txtHActive.getTag()));
        mrTemp.setSnapshot_Mdl(snapshot.getSnapshot());
        return mrTemp;
    }

    private void topControlSave() {
        EmployeeMaster_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            EmployeeMaster_Vld.chkSave(mrTemp, DmClient.getLoginDate());

            SrvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHAccountNamePrefix).show();
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

    private void topControlMoveRec(String strKey) {
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<EmployeeMaster_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<EmployeeMaster_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<EmployeeMaster_Mdl> result) {
        try {
            EmployeeMaster_Mdl data;

            if (result.isStatus()) {
                data = result.getData();
                if (topControl.getMode().equalsIgnoreCase("Browse")) {
                    btnCopyAddress.setVisible(false);
                }

                txtHActive.setEnabled(false);
                topControl.setDocId(data.getSharedKeyFieldCode());
                txtHAccountNamePrefix.setText(data.getAccountNamePrefix());
                txtHAccountNamePrefix.setTag(data.getAccountNamePrefix());
                txtName.setText(data.getSharedKeyFieldName());
                txtDisplayName.setText(data.getDisplayName());
                txtHGender.setTag(data.getGenderCode());
                txtHGender.setText(data.getGenderName());
                txtManualCode.setText(data.getManualCode());
                txtHMaritalStatus.setTag(data.getMaritalStatusCode());
                txtHMaritalStatus.setText(data.getMaritalStatusName());
                txtHReligion.setTag(data.getReligionCode());
                txtHReligion.setText(data.getReligion());
                txtHAccountGroup.setText(data.getAccGroupName());
                txtHAccountGroup.setTag(data.getAccGroup());
                txtHNature.setText(data.getNatureName());
                txtHNature.setTag(data.getNatureCode());
                txtHZone.setText(data.getZoneName());
                txtHZone.setTag(data.getZone());
                txtHDesignation.setText(data.getDesignationName());
                txtHDesignation.setTag(data.getDesignation());
                txtHDepartment.setText(data.getDepartmentName());
                txtHDepartment.setTag(data.getDepartmentCode());
                txtFatherName.setText(data.getFatherName());
                txtPhoneNo.setText(data.getPhoneNo());
                txtMobileNo.setText(data.getMobileNo());
                txtAadhaarNo.setText(data.getAadhaarNo());
                txtAddress.setText(data.getAddress());
                txtAddress1.setText(data.getAddress1());
                txtHCity.setText(data.getCityName());
                txtHCity.setTag(data.getCity());
                txtCountry.setText(data.getCountryName());
                txtCountry.setTag(data.getCountry());
                txtPinCode.setText(data.getPin());
                txtAddresstemp.setText(data.getAddressTemp());
                txtAddress1temp.setText(data.getAddress1Temp());
                txtHCityTemp.setText(data.getCityNameTemp());
                txtHCityTemp.setTag(data.getCityTemp());
                txtCountryTemp.setText(data.getCountryNameTemp());
                txtCountryTemp.setTag(data.getCountryTemp());
                txtPinCodetemp.setText(data.getPinTemp());
                txtDob.setText(data.getDob());
                txtDOA.setText(data.getDoa());
                txtDOJ.setText(data.getDoj());
                txtDOR.setText(data.getDor());
                txtHBloodGroup.setTag(data.getBloodGroupCode());
                txtHBloodGroup.setText(data.getBloodGroupName());
                txtPAN.setText(data.getPan());
                txtifscode.setText(data.getIfsccode());
                txtPfAccNo.setText(data.getPfAccNo());
                txtBankAccNo.setText(data.getBankAccNo());
                txtLicenseNo.setText(data.getLicenseNo());
                txtLicenseExpiry.setText(data.getLicenseExpiry());
                txtPassporNo.setText(data.getPassportNo());
                txtPassportIssueDate.setText(data.getPassportIssueDate());
                txtPassportExpiryDate.setText(data.getPassportExpirydDate());
                txtPassportIssuePlace.setText(data.getPassportIssuePlace());
                txtEMail.setText(data.getEmail());
                txtQualification.setText(data.getQualification());
                txtHTDSCategory.setText(data.getTdsCategoryName());
                txtHTDSCategory.setTag(data.getTdsCategory());
                txtRemark.setText(data.getRemark());
                txtHActive.setTag(data.getActiveTag());
                txtHActive.setText(data.getActiveText());
                txtSalary.setText(data.getSalary());
                snapshot.setSnapshot(data.getSnapshot_Mdl());
                userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
                userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());

                grdBankDetail.getGrid().getStore().clear();
                grdBankDetail.getGrid().getStore().addAll(data.getGrdBankDetail());
            } else {
                new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
            }
            topControl.setDisplay(true);
        } catch (Exception ex) {
        }
    }

    private void topControlSaveError(int errorNo, String errorMessage) {

        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtName).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtDisplayName).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtHGender).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtHAccountGroup).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtHNature).show();
                break;
            case 550:
                new DmAlertMessageBox(errorMessage, txtAadhaarNo).show();
                break;
            case 600:
                new DmAlertMessageBox(errorMessage, txtPAN).show();
                break;
            case 700:
                new DmAlertMessageBox(errorMessage, txtEMail).show();
                break;
            case 800:
                new DmAlertMessageBox(errorMessage, txtMobileNo).show();
                break;
            case 1000:
                new DmAlertMessageBox(errorMessage, txtDob).show();
                break;
            case 1100:
                new DmAlertMessageBox(errorMessage, txtDOA).show();
                break;
            case 1200:
                new DmAlertMessageBox(errorMessage, txtDOJ).show();
                break;
            case 1300:
                new DmAlertMessageBox(errorMessage, txtDOJ).show();
                break;
            case 1400:
                new DmAlertMessageBox(errorMessage, txtDOJ).show();
                break;
            case 1500:
                new DmAlertMessageBox(errorMessage, txtPassportIssueDate).show();
                break;
            case 1600:
                new DmAlertMessageBox(errorMessage, txtPassportExpiryDate).show();
                break;
            case 1700:
                new DmAlertMessageBox(errorMessage, txtPassportIssueDate).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtName).show();
                break;
        }

    }

    public EmployeeMaster(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        SrvAsync = new ServiceInvoker<>(GWT.create(EmployeeMaster_Srv.class), EmployeeMaster.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        gridEvents();
        textHelpEvents();
        manageEvents();
        findEvents();
        flwForm.addAttachHandler(this);
        iniDefaultValue(swi);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    @Override
    void menuClick(SelectionEvent<MenuItem> event) {
        if (event.getSelectedItem().getText().equalsIgnoreCase(manageSiteMenu.getText())) {
            if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {
                manageSite.show("subgroupsitelist", "subcode", topControl.getDocId(), "site_code");
            } else {
                new DmAlertMessageBox("Can be used in browse mode only.", (DmTextBox) null).show();
            }
        } else if (event.getSelectedItem().getText().equalsIgnoreCase(manageFileMenu.getText())) {

            fileManager.show(topControl.getMode().trim().equalsIgnoreCase("New") ? "" : topControl.getDocId(), "1", isAttachFiles, isDeleteFiles);
        }
    }

    private void clear() {
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        DmSnapshot_Mdl dss = new DmSnapshot_Mdl();
        dss.setMongoFileLink("");
        snapshot.setSnapshot(dss);

    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtName)) {
            if (!DmShared.xNull(txtName.getText()).equalsIgnoreCase("")) {
                if (DmShared.xNull(txtDisplayName.getText()).equalsIgnoreCase("")) {
                    txtDisplayName.setText(txtName.getText());
                }
            }
        } else if (event.getSource() == txtDob) {
            returnDate(txtDob);
        } else if (event.getSource() == txtDOA) {
            returnDate(txtDOA);
        } else if (event.getSource() == txtDOJ) {
            returnDate(txtDOJ);
        } else if (event.getSource() == txtDOR) {
            returnDate(txtDOR);
        } else if (event.getSource() == txtPassportIssueDate) {
            returnDate(txtPassportIssueDate);
        } else if (event.getSource() == txtPassportExpiryDate) {
            returnDate(txtPassportExpiryDate);
        } else if (event.getSource() == txtLicenseExpiry) {
            returnDate(txtLicenseExpiry);
        }
    }

    private void returnDate(DmTextBox txtBox) {
        if (!DmShared.xNull(txtBox.getText()).trim().equalsIgnoreCase("")) {
            txtBox.setText(DmClient.returnDate(txtBox.getText()));
        }
    }

    private void onAdd() {
        tabPanel.selectTab(0);
        txtHActive.setEnabled(true);
        txtCountry.setEnabled(false);
        txtCountryTemp.setEnabled(false);
        txtHActive.setTag("1");
        txtHActive.setText("Yes");
        txtHAccountGroup.setTag(defaultValue.getGroupCode());
        txtHAccountGroup.setText(defaultValue.getGroupName());
        txtHNature.setTag(defaultValue.getNatureCode());
        txtHNature.setText(defaultValue.getNatureName());
        grdBankDetail.getGrid().getStore().clear();
        getTempKeyCode();

    }

    @Override
    public void onClick(ClickEvent event) {
//        if (topControl.getMode().equalsIgnoreCase("Browse")) {
//            btnCopyAddress.setEnabled(false);
//        }
        if (event.getSource() == btnCopyAddress) {

            txtAddresstemp.setText(DmShared.xNull(txtAddress.getText()));
            txtAddress1temp.setText(DmShared.xNull(txtAddress1.getText()));
            txtHCityTemp.setTag(DmShared.xNull(txtHCity.getTag()));
            txtHCityTemp.setText(DmShared.xNull(txtHCity.getText()));
            txtCountryTemp.setTag(DmShared.xNull(txtCountry.getTag()));
            txtCountryTemp.setText(DmShared.xNull(txtCountry.getText()));
            txtPinCodetemp.setText(DmShared.xNull(txtPinCode.getText()));

        }

    }

    private void getTempKeyCode() {

        SrvAsync.invokeRPC().getTempKeyCode(new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtName).show();
            }

            @Override
            public void onSuccess(String result) {
                fileManager.setTempKeyCode(result);
            }
        });
    }

    private void showBankDetail(String subCode, String sNo) {
        final HashMap<String, String> hm = new HashMap<>();
        hm.put("grdAccountCode", subCode);
        hm.put("grdsNo", sNo);
        hm.put("accountCode", topControl.getDocId());
        hm.put("accountName", txtName.getText());
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(new lmAccounts().moduleName, new lmAccounts().BANK_ACCOUNT_DETAIL.caption, null, null, new SetWidgetInvokerImpl() {
            @Override
            public void setWidget(final IsWidget wgtObject) {
                ((IsWidgetExtended) wgtObject).action("BANK_DETAIL", hm, new DmCallBack() {
                    @Override
                    public void result(DmResult result) {
                        if (result != null) {
                            if (result.isStatus()) {
                                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                                ((IsWidgetExtended) wgtObject).action("focus_On_Add", null, null);
                            }
                        }
                    }
                });
            }
        });
    }

}
