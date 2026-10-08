package aarogya.inventory.manufacturerMaster.client;

import aarogya.inventory.manufacturerMaster.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
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

public final class ManufacturerMaster extends ManufacturerMaster_Dsg implements KeyPressHandler, ValueChangeHandler<String>, AttachEvent.Handler, BlurHandler {

    ServiceInvoker<ManufacturerMaster_SrvAsync> SrvAsync;
    final ManageSite manageSite = new ManageSite();
    final DmFileManager fileManager = new DmFileManager("MFMST");
    private boolean isAttachFiles = false, isDeleteFiles = false;
    protected DefaultValue_Mdl defaultValue = new DefaultValue_Mdl();
    private String currentDate = "";

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setRefreshRequired(false);
        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);
        topControl.setDisplay(true);

        for (String controlPermission : menuPermission.getControlPermission()) {
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(attachFileBlog)) {
                isAttachFiles = true;
            }
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(deleteFileBlog)) {
                isDeleteFiles = true;
            }
        }

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                clear();
                getTempKeyCode();
                txtManualCode.setEnabled(false);
                if (!defaultValue.isGstApplicable()) {
                    txtHPartyType.setText(DmEnum.DmPartyType.Unregistered.getValue());
                    txtHPartyType.setTag(DmEnum.DmPartyType.Unregistered.getId());
                }
                txtHAccountNamePrefix.setFocus(true);
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                txtName.setFocus(true);
                txtManualCode.setEnabled(true);
                txtState.setEnabled(false);
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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(ManufacturerMaster.this);
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
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAccountGroup(DmShared.xNull(defaultValue.getAcGroupManufacturerCode()), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHAccountGroup_Mdl>>>() {
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
                    txtHNature.setTag(tRow.getNatureCode());
                    txtHNature.setText(tRow.getNatureName());
                }
            }
        });
        txtHCostCenter.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHCostCenter(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCostCenter).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {

                            txtHCostCenter.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCostCenter).show();
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
                    txtState.setTag(tRow.getStateNumericCode());
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
        txtHReferencedTo.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHReferencedTo_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHReferencedTo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHReferencedTo_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHReferencedTo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHReferencedTo_Mdl>> result) {
                        try {
                            if (result.isStatus()) {
                                txtHReferencedTo.setData(result.getData(), result.getPaging());
                            }
                        } catch (Exception e) {
                            new DmAlertMessageBox(e.getMessage(), txtHReferencedTo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHReferencedTo_Mdl tRow) {

            }
        });
        txtHDuplicate.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHDuplicate(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDuplicate).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        try {
                            if (result.isStatus()) {
                                txtHDuplicate.setData(result.getData(), result.getPaging());
                            }
                        } catch (Exception e) {
                            new DmAlertMessageBox(e.getMessage(), txtHDuplicate).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {

            }
        });
        txtHLedgerGroup.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHLadgerGroup(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHLedgerGroup).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        try {
                            if (result.isStatus()) {
                                txtHLedgerGroup.setData(result.getData(), result.getPaging());
                            }
                        } catch (Exception e) {
                            new DmAlertMessageBox(e.getMessage(), txtHLedgerGroup).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {

            }
        });

        txtHPartyType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHPartyType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPartyType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        try {
                            if (result.isStatus()) {
                                txtHPartyType.setData(result.getData(), result.getPaging());
                            }
                        } catch (Exception e) {
                            new DmAlertMessageBox(e.getMessage(), txtHPartyType).show();
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
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHNature(DmShared.xNull(defaultValue.getAcGroupManufacturerCode()), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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

    }

    private void tabChagneEvents() {
        tabPanel.addSelectionHandler(new SelectionHandler<Integer>() {

            @Override
            public void onSelection(SelectionEvent<Integer> event) {
                if (event.getSelectedItem() == 1) {
                    txtFatherName.setFocus(true);
                }
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
    public void onValueChange(ValueChangeEvent<String> event) {
        if (event.getSource().equals(txtMobileNo)) {
            if (!txtMobileNo.getText().trim().equalsIgnoreCase("")) {
                txtMobileNo.setText(event.getValue());
            }
        } else if (event.getSource().equals(txtPhoneNo)) {
            if (!txtPhoneNo.getText().trim().equalsIgnoreCase("")) {
                txtPhoneNo.setText(event.getValue());
            }
        } else if (event.getSource().equals(txtDueDays)) {
            if (!DmShared.xNull(txtDueDays.getText()).equalsIgnoreCase("")) {
                try {
                    Short val = Short.valueOf(DmClient.vNull(txtDueDays.getText(), "0"));
                } catch (Exception exc) {
                    new DmAlertMessageBox("Due days can't be greater then 32767", txtDueDays).show();
                    txtDueDays.setText("");
                }
            }
        }
    }

    private void onLoadEvents() {

        txtMobileNo.addValueChangeHandler(this);
        txtMobileNo.addKeyPressHandler(this);
        txtPhoneNo.addValueChangeHandler(this);
        txtPhoneNo.addKeyPressHandler(this);
        txtName.addBlurHandler(this);
        txtDueDays.addValueChangeHandler(this);
        txtGstDate.addBlurHandler(this);
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

    private ManufacturerMaster_Mdl topControlSaveInitialize() {
        ManufacturerMaster_Mdl mrTemp = new ManufacturerMaster_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }
        mrTemp.setTempKeyCode(fileManager.getTempKeyCode());
        mrTemp.setAccountNamePrefix(DmShared.xNull(txtHAccountNamePrefix.getText()));
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtName.getText()));
        mrTemp.setDisplayName(DmShared.xNull(txtDisplayName.getText()));
        mrTemp.setManualCode(DmShared.xNull(txtManualCode.getText()));
        mrTemp.setAccGroup(DmShared.xNull(txtHAccountGroup.getTag()));
        mrTemp.setAccGroupName(DmShared.xNull(txtHAccountGroup.getText()));
        mrTemp.setNatureCode(DmShared.xNull(txtHNature.getTag()));
        mrTemp.setNatureName(DmShared.xNull(txtHNature.getText()));
        mrTemp.setCostCenter(DmShared.xNull(txtHCostCenter.getTag()));
        mrTemp.setFatherName(DmShared.xNull(txtFatherName.getText()));
        mrTemp.setAddress(DmShared.xNull(txtAddress.getText()));
        mrTemp.setAddress1(DmShared.xNull(txtAddress1.getText()));
        mrTemp.setCity(DmShared.xNull(txtHCity.getTag()));
        mrTemp.setStateNumericCode(DmClient.vNull(txtState.getTag(), "00"));
        mrTemp.setZone(DmShared.xNull(txtHZone.getTag()));
        mrTemp.setPin(DmShared.xNull(txtPinCode.getText()));
        mrTemp.setPhoneNo(DmShared.xNull(txtPhoneNo.getText()));
        mrTemp.setMobileNo(DmShared.xNull(txtMobileNo.getText()));
        mrTemp.setFaxNo(DmShared.xNull(txtFaxNo.getText()));
        mrTemp.setEmail(DmShared.xNull(txtEMail.getText()));
        mrTemp.setTdsCategory(DmShared.xNull(txtHTDSCategory.getTag()));
        mrTemp.setReferencedTo(DmShared.xNull(txtHReferencedTo.getTag()));
        mrTemp.setContactPerson(DmShared.xNull(txtContactPerson.getText()));
        mrTemp.setPan(DmShared.xNull(txtPAN.getText()).toUpperCase());
        mrTemp.setTin(DmShared.xNull(txtTIN.getText()));
        mrTemp.setDuplicate(DmShared.xNull(txtHDuplicate.getTag()));
        mrTemp.setCstNo(DmShared.xNull(txtCSTNo.getText()));
        mrTemp.setStno(DmShared.xNull(txtSTNo.getText()));
        mrTemp.setEccNo(DmShared.xNull(txtECCNo.getText()));
        mrTemp.setExciseNo(DmShared.xNull(txtExciseNo.getText()));
        mrTemp.setRange(DmShared.xNull(txtRange.getText()));
        mrTemp.setDivision(DmShared.xNull(txtDivision.getText()));
        mrTemp.setCreditLimit(DmClient.vNull(txtCreditLimit.getText(), "0.00"));
        mrTemp.setDueDays(DmClient.vNull(txtDueDays.getText(), "0"));
        mrTemp.setIecNo(DmShared.xNull(txtIECNo.getText()));
        mrTemp.setLedgerGroup(DmShared.xNull(txtHLedgerGroup.getTag()));
        mrTemp.setPolicyNo(DmShared.xNull(txtPolicyNo.getText()));
        mrTemp.setPartyType(DmShared.xNull(txtHPartyType.getTag()));
        mrTemp.setRemark(DmShared.xNull(txtRemark.getText()));
        mrTemp.setAadhar(DmShared.xNull(txtAadhar.getText()));
        mrTemp.setGsTINDate(DmShared.dNull(txtGstDate.getText()));
        mrTemp.setGsTin(DmShared.xNull(txtGstTin.getText()));
        mrTemp.setCurrentDate(currentDate);
        mrTemp.setGstApplicable(defaultValue.isGstApplicable());
        return mrTemp;
    }

    private void topControlSave() {
        ManufacturerMaster_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            ManufacturerMaster_Vld.chkSave(mrTemp);

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
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<ManufacturerMaster_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<ManufacturerMaster_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<ManufacturerMaster_Mdl> result) {
        try {
            ManufacturerMaster_Mdl data;

            if (result.isStatus()) {
                data = result.getData();

                topControl.setDocId(data.getSharedKeyFieldCode());
                txtHAccountNamePrefix.setText(data.getAccountNamePrefix());
                txtHAccountNamePrefix.setTag(data.getAccountNamePrefix());
                txtName.setText(data.getSharedKeyFieldName());
                txtDisplayName.setText(data.getDisplayName());
                txtManualCode.setText(data.getManualCode());
                txtHAccountGroup.setText(data.getAccGroupName());
                txtHAccountGroup.setTag(data.getAccGroup());
                txtHNature.setText(data.getNatureName());
                txtHNature.setTag(data.getNatureCode());
                txtHCostCenter.setText(data.getCostCenterName());
                txtHCostCenter.setTag(data.getCostCenter());
                txtFatherName.setText(data.getFatherName());
                txtAddress.setText(data.getAddress());
                txtAddress1.setText(data.getAddress1());
                txtHCity.setText(data.getCityName());
                txtHCity.setTag(data.getCity());
                txtState.setText(data.getStateName());
                txtState.setTag(data.getStateNumericCode());
                txtHZone.setText(data.getZoneName());
                txtHZone.setTag(data.getZone());
                txtPinCode.setText(data.getPin());
                txtPhoneNo.setText(data.getPhoneNo());
                txtMobileNo.setText(data.getMobileNo());
                txtFaxNo.setText(data.getFaxNo());
                txtEMail.setText(data.getEmail());
                txtAadhar.setText(data.getAadhar());
                txtGstDate.setText(data.getGsTINDate());
                txtGstTin.setText(data.getGsTin());

                txtHTDSCategory.setText(data.getTdsCategoryName());
                txtHTDSCategory.setTag(data.getTdsCategory());
                txtHReferencedTo.setText(data.getReferencedToName());
                txtHReferencedTo.setTag(data.getReferencedTo());
                txtContactPerson.setText(data.getContactPerson());
                txtPAN.setText(data.getPan());
                txtTIN.setText(data.getTin());
                String DuplicateName = "";
                if (DmShared.xNull(data.getDuplicate()).equalsIgnoreCase("1")) {
                    DuplicateName = "Yes";
                } else if (DmShared.xNull(data.getDuplicate()).equalsIgnoreCase("0")) {
                    DuplicateName = "No";
                }
                txtHDuplicate.setText(DuplicateName);
                txtHDuplicate.setTag(data.getDuplicate());
                txtCSTNo.setText(data.getCstNo());
                txtSTNo.setText(data.getStno());
                txtECCNo.setText(data.getEccNo());
                txtExciseNo.setText(data.getExciseNo());
                txtRange.setText(data.getRange());
                txtDivision.setText(data.getDivision());
                txtCreditLimit.setText(String.valueOf(data.getCreditLimit()));
                txtDueDays.setText(String.valueOf(data.getDueDays()));
                txtIECNo.setText(data.getIecNo());
                txtHLedgerGroup.setText(data.getLedgerGroupName());
                txtHLedgerGroup.setTag(data.getLedgerGroup());
                txtPolicyNo.setText(data.getPolicyNo());

                String PartyTypeName = "";
                if (DmShared.xNull(data.getPartyType()).equalsIgnoreCase(DmEnum.DmPartyType.Registered.getId())) {
                    PartyTypeName = DmEnum.DmPartyType.Registered.getValue();
                } else if (DmShared.xNull(data.getPartyType()).equalsIgnoreCase(DmEnum.DmPartyType.Unregistered.getId())) {
                    PartyTypeName = DmEnum.DmPartyType.Unregistered.getValue();
                } else if (DmShared.xNull(data.getPartyType()).equalsIgnoreCase(DmEnum.DmPartyType.Composite.getId())) {
                    PartyTypeName = DmEnum.DmPartyType.Composite.getValue();
                }
                txtHPartyType.setText(PartyTypeName);
                txtHPartyType.setTag(data.getPartyType());

                txtRemark.setText(data.getRemark());
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
                new DmAlertMessageBox(errorMessage, txtHAccountGroup).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtEMail).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtHNature).show();
                break;
            case 550:
                new DmAlertMessageBox(errorMessage, txtHCity).show();
                break;
            case 600:
                new DmAlertMessageBox(errorMessage, txtHPartyType).show();
                break;
            case 700:
                new DmAlertMessageBox(errorMessage, txtGstTin).show();
                break;
            case 800:
                new DmAlertMessageBox(errorMessage, txtGstDate).show();
                break;
            case 850:
                new DmAlertMessageBox(errorMessage, txtGstTin).show();
                break;
            case 900:
                new DmAlertMessageBox(errorMessage, txtAadhar).show();
                break;
            case 925:
                new DmAlertMessageBox(errorMessage, txtAadhar).show();
                break;
            case 1000:
                new DmAlertMessageBox(errorMessage, txtPAN).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtName).show();
                break;
        }

    }

    public ManufacturerMaster(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        SrvAsync = new ServiceInvoker<>(GWT.create(ManufacturerMaster_Srv.class), ManufacturerMaster.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        gridEvents();
        textHelpEvents();
        tabChagneEvents();
        onLoadEvents();
        findEvents();
        getServerTime();
        flwForm.addAttachHandler(this);
        iniDefaultValue(swi);
    }

    private void iniDefaultValue(final SetWidgetInvoker swi) {
        SrvAsync.invokeRPC().getDefaultValues(new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    defaultValue = result.getData();
                    swi.setWidget(ManufacturerMaster.this);
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
        txtHDuplicate.setTag("0");
        txtHDuplicate.setText("No");
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        tabPanel.selectTab(0);
        txtState.setEnabled(false);
        grdBankDetail.getGrid().getStore().clear();
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtName)) {
            if (!DmShared.xNull(txtName.getText()).equalsIgnoreCase("")) {
                if (DmShared.xNull(txtDisplayName.getText()).equalsIgnoreCase("")) {
                    txtDisplayName.setText(txtName.getText());
                }
            }
        } else if (event.getSource() == txtGstDate) {
            txtGstDate.setText(DmClient.returnDate(txtGstDate.getText()));

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

    private void getServerTime() {
        SrvAsync.invokeRPC().getServerDateTime(new AsyncCallback<String[]>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtGstDate).show();
            }

            @Override
            public void onSuccess(String[] result) {
                currentDate = result[0];
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
