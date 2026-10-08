package dataman.accounts.addressBook.client;

import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.accounts.addressBook.shared.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmInputBox.client.DmInputBox;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import java.util.HashMap;
import java.util.List;

public final class AddressBook extends AddressBook_Dsg implements ClickHandler, KeyPressHandler, ValueChangeHandler<String> {

    ServiceInvoker<AddressBook_SrvAsync> SrvAsync;
    private String accountCode = "", accountName = "";

    final DmInputBox inputBox = new DmInputBox();

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());

        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);

        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                clear();
                txtHGroup.setFocus(true);
                txtHDefault.setEnabled(true);
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                if (!DmShared.xNull(topControl.getDocId()).equalsIgnoreCase("")) {
                    txtHGroup.setFocus(true);
                    manageDisplay(false);
                    txtHDefault.setEnabled(true);
                    txtState.setEnabled(false);
                }
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

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(AddressBook.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void textHelpEvents() {

        txtHGroup.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHCompanyGroupName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHGroup).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHGroup.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHGroup).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {

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

        txtHAccount.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHLinkWithAccount_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHLinkWithAccount(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHLinkWithAccount_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAccount).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHLinkWithAccount_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHAccount.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHAccount).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHLinkWithAccount_Mdl tRow) {

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

                    String St_Coun = DmShared.xNull(tRow.getStateName()) + " " + "-" + " " + DmShared.xNull(tRow.getCountryName());
                    txtState.setText(St_Coun);
                    txtState.setEnabled(false);

                } else {
                    txtState.setText(DmShared.xNull(""));

                }
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

        txtHDefault.addKeyDownHandler(new DmTextHelp.TxtHelpKeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event, Object objSource) {

            }
        });

        txtHDefault.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDefault).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDefault.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDefault).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });

        inputBox.addCallBack(new DmInputBox.CallBack() {

            @Override
            public void onOk(String inputValue, String parentName) {
                if (parentName.equalsIgnoreCase("NatureBox")) {
                    txtHNature.setTag(inputValue);
                    txtHNature.setText(inputValue);
                    txtHNature.setFocus(true);
                } else if (parentName.equalsIgnoreCase("GroupBox")) {
                    txtHGroup.setTag(inputValue);
                    txtHGroup.setText(inputValue);
                    txtHGroup.setFocus(true);
                } else if (parentName.equalsIgnoreCase("DesignationBox")) {
                    txtHDesignation.setTag(inputValue);
                    txtHDesignation.setText(inputValue);
                    txtHDesignation.setFocus(true);
                }
            }
        });
        txtHContactPrefix.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHContactPrefix(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHContactPrefix).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHContactPrefix.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHContactPrefix).show();
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

    public AddressBook(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        SrvAsync = new ServiceInvoker<>(GWT.create(AddressBook_Srv.class), AddressBook.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        textHelpEvents();
        findEvents();
        btnGroupInputBox.addClickHandler(this);
        btnNatureInputBox.addClickHandler(this);
        btnDesignationInputBox.addClickHandler(this);
        txtMobile.addKeyPressHandler(this);
        txtMobile.addValueChangeHandler(this);
        txtPhone.addKeyPressHandler(this);
        txtPhone.addValueChangeHandler(this);
        txtHDefault.setEnabled(false);
        swi.setWidget(this);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        if (event.getSource().equals(txtMobile)) {
            if (!txtMobile.getText().trim().equalsIgnoreCase("")) {
                txtMobile.setText(event.getValue());
            }
        }
        if (event.getSource().equals(txtPhone)) {
            if (!txtPhone.getText().trim().equalsIgnoreCase("")) {
                txtPhone.setText(event.getValue());
            }
        }
    }

    @Override
    public void onKeyPress(KeyPressEvent event) {
        int charCode = event.getCharCode();
        if (event.getSource().equals(txtMobile)) {
            if ((charCode > 31) && ((charCode < 48 || charCode > 57) && (charCode != 45 && charCode != 44))) {
                txtMobile.cancelKey();
            }
        }
        if (event.getSource().equals(txtPhone)) {
            if ((charCode > 31) && ((charCode < 48 || charCode > 57) && (charCode != 45 && charCode != 44))) {
                txtPhone.cancelKey();
            }
        }
    }

    private AddressBook_Mdl topControlSaveInitialize() {
        AddressBook_Mdl mrTemp = new AddressBook_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }

        mrTemp.setAddress(DmShared.xNull(txtAddress.getText()));
        mrTemp.setAddress1(DmShared.xNull(txtAddress1.getText()));
        mrTemp.setCityCode(DmShared.xNull(txtHCity.getTag()));
        mrTemp.setCompanyGroup(DmShared.xNull(txtHGroup.getTag()));
        mrTemp.setContactPerson(DmShared.xNull(txtContactPerson.getText()));

        mrTemp.setDesignation(DmShared.xNull(txtHDesignation.getTag()));
        mrTemp.setEmail(DmShared.xNull(txtEmail.getText()));
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtEntryNo.getText()));
        mrTemp.setFaxNo(DmShared.xNull(txtFaxNo.getText()));
        mrTemp.setLinkAccountTag(DmShared.xNull(txtHAccount.getTag()));
        mrTemp.setLinkAccountText(DmShared.xNull(txtHAccount.getText()));
        mrTemp.setMobileNo(DmShared.xNull(txtMobile.getText()));
        mrTemp.setNature(DmShared.xNull(txtHNature.getTag()));
        mrTemp.setPhoneNo(DmShared.xNull(txtPhone.getText()));
        mrTemp.setPinCode(DmShared.xNull(txtPinCode.getText()));
        mrTemp.setRemark(DmShared.xNull(txtRemark.getText()));
        mrTemp.setStateName(DmShared.xNull(txtState.getTag()));
        mrTemp.setZoneCode(DmShared.xNull(txtHZone.getTag()));
        mrTemp.setIsDefaultTag(DmShared.xNull(txtHDefault.getTag()));
        mrTemp.setInitialText(DmShared.xNull(txtHContactPrefix.getText()));
        return mrTemp;
    }

    private void topControlSave() {
        AddressBook_Mdl mrTemp;
        try {
            mrTemp = topControlSaveInitialize();
            AddressBook_Vld.chkSave(mrTemp);

            SrvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHGroup).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        topControl.setDocId(result.getData());

                        if (topControl.getMode().equalsIgnoreCase("New")) {
                            topControl.btnClick("New");
                            txtHAccount.setTag(DmShared.xNull(accountCode));
                            txtHAccount.setText(DmShared.xNull(accountName));
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
                new DmAlertMessageBox(errorMessage, txtHGroup).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtHNature).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtContactPerson).show();
                break;
            case 350:
                new DmAlertMessageBox(errorMessage, txtHDefault).show();
            case 400:
                new DmAlertMessageBox(errorMessage, txtEmail).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHGroup).show();
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
        try {
            SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<AddressBook_Mdl>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<AddressBook_Mdl> result) {
                    topControlMoveRecSuccess(result);
                }
            });
        } catch (Exception e) {
        }
    }

    private void topControlMoveRecSuccess(DmResult<AddressBook_Mdl> result) {
        AddressBook_Mdl data;

        if (result.isStatus()) {
            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());
            txtHDefault.setEnabled(false);
            txtEntryNo.setText(data.getSharedKeyFieldName());
            txtContactPerson.setText(data.getContactPerson());
            txtAddress.setText(data.getAddress());
            txtAddress1.setText(data.getAddress1());
            txtPinCode.setText(data.getPinCode());
            txtPhone.setText(data.getPhoneNo());
            txtMobile.setText(data.getMobileNo());
            txtFaxNo.setText(data.getFaxNo());
            txtEmail.setText(data.getEmail());
            txtRemark.setText(data.getRemark());
            txtHCity.setTag(data.getCityCode());
            txtHCity.setText(data.getCityName());
            txtHGroup.setTag(data.getCompanyGroup());
            txtHGroup.setText(data.getCompanyGroup());
            txtState.setText(data.getStateName());
            txtHDesignation.setTag(data.getDesignation());
            txtHDesignation.setText(data.getDesignation());

            txtHAccount.setTag(data.getLinkAccountTag());
            txtHAccount.setText(data.getLinkAccountText());

            txtHNature.setTag(data.getNature());
            txtHNature.setText(data.getNature());

            txtHZone.setTag(data.getZoneCode());
            txtHZone.setText(data.getZoneName());

            txtHDefault.setTag(data.getIsDefaultTag());
            txtHDefault.setText(data.getIsDefault());
            txtHContactPrefix.setText(data.getInitialText());

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
                    clear();
                    topControlCard();
                } else {
                    new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                }
            }
        });
    }

    private void manageDisplay(boolean blnFlag) {
        txtEntryNo.setEnabled(false);
        txtState.setEnabled(false);

    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnGroupInputBox)) {
            if (!topControl.getMode().equalsIgnoreCase("Browse")) {
                inputBox.show(btnGroupInputBox, "GroupBox", txtHGroup.getText(), 100, "Please Enter Group", "400px");
            }
        } else if (event.getSource().equals(btnNatureInputBox)) {
            if (!topControl.getMode().equalsIgnoreCase("Browse")) {
                inputBox.show(btnNatureInputBox, "NatureBox", txtHNature.getText(), 100, "Please Enter Narure", "400px");
            }
        } else if (event.getSource().equals(btnDesignationInputBox)) {
            if (!topControl.getMode().equalsIgnoreCase("Browse")) {
                inputBox.show(btnNatureInputBox, "DesignationBox", txtHNature.getText(), 100, "Please Enter Designation", "400px");
            }
        }

    }

    private void clear() {
        manageDisplay(false);
        if (topControl.getMode().equalsIgnoreCase("New")) {
            txtHDefault.setTag("1");
            txtHDefault.setText("Yes");
        }
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
    }

    @Override
    public void action(String nature, Object object, DmCallBack callback) {

        nature = DmShared.xNull(nature).trim().toUpperCase();
        try {
            switch (nature) {

                case "ORGANIZATION":
                    openWithOrg((HashMap<String, String>) object, callback);
                    break;
                case "FOCUS_ON_ADD":
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            txtHGroup.setFocus(true);
                        }
                    });
                    break;
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    //------ << FORM IS OPENING FROM ACCOUNT MASTER,ORGANIZATION MASTER >>-----
    public void openWithOrg(HashMap<String, String> param, DmCallBack callback) {
        if (DmShared.iNull(param.get("addressCode")).longValue() > 0) {
            topControlMoveRec(param.get("addressCode"));
        } else {
            topControl.btnClick("New");
            if (param != null) {
                txtHAccount.setText(DmShared.xNull(param.get("orgName")));
                txtHAccount.setTag(DmShared.xNull(param.get("orgCode")));
                accountCode = DmShared.xNull(param.get("orgCode"));
                accountName = DmShared.xNull(param.get("orgName"));
            }
        }
        if (callback != null) {
            DmResult<Object> dmrTemp = new DmResult<>();
            dmrTemp.setStatus(true);
            callback.result(dmrTemp);
        }

    }
}
