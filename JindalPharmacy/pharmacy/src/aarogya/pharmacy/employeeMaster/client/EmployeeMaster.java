package aarogya.pharmacy.employeeMaster.client;

import aarogya.pharmacy.employeeMaster.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import java.util.List;

public final class EmployeeMaster extends EmployeeMaster_Dsg implements ClickHandler, BlurHandler, KeyPressHandler {

    ServiceInvoker<EmployeeMaster_SrvAsync> SrvAsync;
    String nature = "";

    //<editor-fold defaultstate="collapsed" desc="events">
    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                txtName.setFocus(true);
                txtHActive.setTag("0");
                txtHActive.setText("Yes");
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                txtName.setFocus(true);

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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(EmployeeMaster.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void textHelpEvents() {
        txtName.addCallBack(new DmSuggestBox.CallBack() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getSuggestion(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<String>>>() {

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
                if (blnStatus) {
                    txtHAccountGroup.setTag2nd(tRow.getGroupNature());
                    nature = tRow.getNature();
                }
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
        txtHCity.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getCity(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCity).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHCity.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCity).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHCountry.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getCountry(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCountry).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHCountry.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCountry).show();
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
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHGender.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHGender(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
        txtHPCity.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getCity(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPCity).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPCity.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPCity).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHPCountry.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getCountry(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPCountry).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPCountry.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPCountry).show();
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

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnCopyAddress)) {
            try {
                txtPAddress1.setText(txtAddress1.getText());
                txtPAddress2.setText(txtAddress2.getText());
                txtPPinCode.setText(txtPinCode.getText());
                txtHPCity.setText(txtHCity.getText());
                txtHPCity.setTag(txtHCity.getTag());
                txtHPCountry.setText(txtHCountry.getText());
                txtHPCountry.setTag(txtHCountry.getTag());
            } catch (Exception e) {
                new DmAlertMessageBox(e.getMessage(), btnCopyAddress).show();
            }
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtDOB) {
            txtDOB.setText(DmClient.returnDate(txtDOB.getText()));
        } else if (event.getSource() == txtJoinDate) {
            txtJoinDate.setText(DmClient.returnDate(txtJoinDate.getText()));
        } else if (event.getSource() == txtAnvDate) {
            txtAnvDate.setText(DmClient.returnDate(txtAnvDate.getText()));
        } else if (event.getSource().equals(txtMobileNo)) {
            txtMobileNo.setText(txtMobileNo.getText());
        } else if (event.getSource().equals(txtPhoneNo)) {
            txtPhoneNo.setText(txtPhoneNo.getText());
        }
    }

    @Override
    public void onKeyPress(KeyPressEvent event) {
        int charCode = event.getCharCode();
        if (event.getSource().equals(txtMobileNo)) {
            if ((charCode > 31) && ((charCode < 48 || charCode > 57) && (charCode != 45 && charCode != 44))) {
                txtMobileNo.cancelKey();
            }
        } else if (event.getSource().equals(txtPhoneNo)) {
            if ((charCode > 31) && ((charCode < 48 || charCode > 57) && (charCode != 45 && charCode != 44))) {
                txtPhoneNo.cancelKey();
            }
        }
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    private void handlerEvents(Button[] btnArray, DmTextBox[] txtArray) {
        for (int i = 0; i < txtArray.length; i++) {
            txtArray[i].addBlurHandler(this);
        }
        for (int i = 0; i < btnArray.length; i++) {
            btnArray[i].addClickHandler(this);
        }
        txtPhoneNo.addKeyPressHandler(this);
        txtMobileNo.addKeyPressHandler(this);
    }
    //</editor-fold>

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    public EmployeeMaster(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(EmployeeMaster_Srv.class), EmployeeMaster.class.getName());
        this.asWidget().setTitle(widgetName);
        handlerEvents(new Button[]{btnCopyAddress}, new DmTextBox[]{txtDOB, txtJoinDate, txtAnvDate, txtPhoneNo, txtMobileNo});
        topControlEvents(menuPermission);
        findEvents();
        textHelpEvents();
        swi.setWidget(this);
    }

    private Employee_Mdl topControlSaveInitialize() {
        Employee_Mdl mrTemp = new Employee_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }
        mrTemp.setAccountGroupDesc(DmShared.xNull(txtHAccountGroup.getTag2nd()));
        mrTemp.setAccountGroupTag(DmShared.xNull(txtHAccountGroup.getTag()));
        mrTemp.setActiveTag(DmShared.xNull(txtHActive.getTag()));
        mrTemp.setAddress1(DmShared.xNull(txtAddress1.getText()));
        mrTemp.setAddress2(DmShared.xNull(txtAddress2.getText()));
        mrTemp.setAnvDate(DmShared.dNull(txtAnvDate.getText()));
        mrTemp.setCityTag(DmShared.xNull(txtHCity.getTag()));
        mrTemp.setCountryTag(DmShared.xNull(txtHCountry.getTag()));
        mrTemp.setDepartmentTag(DmShared.xNull(txtHDepartment.getTag()));
        mrTemp.setDesignationTag(DmShared.xNull(txtHDesignation.getTag()));
        mrTemp.setDob(DmShared.dNull(txtDOB.getText()));
        mrTemp.setEmailId(DmShared.xNull(txtEmailID.getText()));
        mrTemp.setEmployeeID(DmShared.xNull(txtEmpID.getText()));
        mrTemp.setGenderTag(DmShared.xNull(txtHGender.getTag()));
        mrTemp.setJoinDate(DmShared.dNull(txtJoinDate.getText()));
        mrTemp.setMaritalStatusTag(DmShared.xNull(txtHMaritalStatus.getTag()));
        mrTemp.setMobileNo(DmShared.xNull(txtMobileNo.getText()));
        mrTemp.setNature(DmShared.xNull(nature));
        mrTemp.setPaddress1(DmShared.xNull(txtPAddress1.getText()));
        mrTemp.setPaddress2(DmShared.xNull(txtPAddress2.getText()));
        mrTemp.setPanNo(DmShared.xNull(txtPanNo.getText()));
        mrTemp.setPhoneNo(DmShared.xNull(txtPhoneNo.getText()));
        mrTemp.setPpinCode(DmShared.xNull(txtPPinCode.getText()));
        mrTemp.setpCityTag(DmShared.xNull(txtHPCity.getTag()));
        mrTemp.setpCountryTag(DmShared.xNull(txtHPCountry.getTag()));
        mrTemp.setReligionTag(DmShared.xNull(txtHReligion.getTag()));
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtName.getText()));
        return mrTemp;
    }

    private void topControlSave() {
        Employee_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            EmployeeMaster_Vld.chkSave(mrTemp);

            SrvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtName).show();
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

    private void topControlSaveError(int errorNo, String errorMessage) {

        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtName).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtEmpID).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtJoinDate).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtHAccountGroup).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtHDepartment).show();
                break;
            case 600:
                new DmAlertMessageBox(errorMessage, txtHDesignation).show();
                break;
            case 700:
                new DmAlertMessageBox(errorMessage, txtHGender).show();
                break;
            case 800:
                new DmAlertMessageBox(errorMessage, txtHMaritalStatus).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtName).show();
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
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<Employee_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<Employee_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<Employee_Mdl> result) {
        Employee_Mdl data;

        if (result.isStatus()) {
            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());
            txtAddress1.setText(data.getAddress1());
            txtAddress2.setText(data.getAddress2());
            txtAnvDate.setText(data.getAnvDate());
            txtDOB.setText(data.getDob());
            txtEmailID.setText(data.getEmailId());
            txtEmpID.setText(data.getEmployeeID());
            txtHAccountGroup.setTag(data.getAccountGroupTag());
            txtHAccountGroup.setText(data.getAccountGroupText());
            txtHAccountGroup.setTag2nd(data.getAccountGroupDesc());
            txtHActive.setText(data.getActiveText());
            txtHActive.setTag(data.getActiveTag());
            txtHCity.setTag(data.getCityTag());
            txtHCity.setText(data.getCityText());
            txtHCountry.setTag(data.getCountryTag());
            txtHCountry.setText(data.getCountryText());
            txtHDepartment.setText(data.getDepartmentText());
            txtHDepartment.setTag(data.getDepartmentTag());
            txtHDesignation.setTag(data.getDesignationTag());
            txtHDesignation.setText(data.getDesignationText());
            txtHGender.setTag(data.getGenderTag());
            txtHGender.setText(data.getGenderText());
            txtHMaritalStatus.setTag(data.getMaritalStatusTag());
            txtHMaritalStatus.setText(data.getMaritalStatusText());
            txtHPCity.setTag(data.getpCityTag());
            txtHPCity.setText(data.getpCityText());
            txtHPCountry.setTag(data.getpCountryTag());
            txtHPCountry.setText(data.getpCountryText());
            txtHReligion.setTag(data.getReligionTag());
            txtHReligion.setText(data.getReligionText());
            txtJoinDate.setText(data.getJoinDate());
            txtMobileNo.setText(data.getMobileNo());
            txtName.setText(data.getSharedKeyFieldName());
            txtPAddress1.setText(data.getPaddress1());
            txtPAddress2.setText(data.getPaddress2());
            txtPPinCode.setText(data.getPpinCode());
            txtPanNo.setText(data.getPanNo());
            txtPhoneNo.setText(data.getPhoneNo());
            txtPinCode.setText(data.getPinCode());
            nature = data.getNature();

            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());
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
                    topControlCard();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

}
