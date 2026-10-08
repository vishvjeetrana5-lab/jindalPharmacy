package dataman.accounts.accountMaster.client;

import dataman.dmBase.self.shared.*;
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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.accounts.accountMaster.shared.*;
import dataman.accounts.addressBook.shared.GrdContactPerson_Mdl;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
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
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmToolkit.manageSite.client.ManageSite;
import dataman.dmToolkit.self.client.SetWidgetInvokerImpl;
import dataman.libraryMenu.self.shared.lmAccounts;
import java.util.HashMap;
import java.util.List;

public final class AccountMaster extends AccountMaster_Dsg implements KeyPressHandler, ValueChangeHandler<String>, AttachEvent.Handler, BlurHandler {

    protected boolean detailEditable = false, idEditable = false, bankEditable = false;
    private ServiceInvoker<AccountMaster_SrvAsync> srvAsync;
    private final ManageSite manageSite = new ManageSite();
    protected DefaultValue_Mdl defaultValue = new DefaultValue_Mdl();
    private String natureManualCode = "";

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setRefreshRequired(false);
        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);
        topControl.setDisplay(true);
        tabPanel.remove(flwGeneral);
        tabPanel.remove(flwDetail);
        tabPanel.remove(flwDetail2);
        for (String controlPermission : menuPermission.getControlPermission()) {
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(detail)) {
                addGeneralTab();
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(detail2)) {
                addDetailTab();
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(detail3)) {
                addDetail2Tab();
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(detail4)) {
                addAdditionalDetailTab();
            }
        }

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                clear();
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
                grdContactPerson.getGrid().getStore().add(new GrdContactPerson_Mdl());
                grdAdditionalPlaceDetail.getGrid().getStore().add(new GrdAdditionalPlace_Mdl());

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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(AccountMaster.this);
            }
        });
    }

    private void textHelpEvents() {
        txtName.addCallBack(new DmSuggestBox.CallBack() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getAccountNameSuggestion(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<String>>>() {

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
                srvAsync.invokeRPC(blnCallBackWait).getTxtHAccountNamePrefix(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
                srvAsync.invokeRPC(blnCallBackWait).getTxtHAccountGroup(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHAccountGroup_Mdl>>>() {
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
                srvAsync.invokeRPC(blnCallBackWait).getTxtHCostCenter(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
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
                srvAsync.invokeRPC(blnCallBackWait).getTxtHCity(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHCity_Mdl>>>() {

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
                    txtState.setTag(DmShared.xNull(tRow.getStateNumericCode()));
                    txtState.setEnabled(false);

                } else {
                    txtState.setText("");
                    txtState.setTag("");

                }

            }
        });

        txtHZone.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHZone(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
        txtHPartyCategory.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHPartyCategory(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPartyCategory).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        try {
                            if (result.isStatus()) {
                                txtHPartyCategory.setData(result.getData(), result.getPaging());
                            }
                        } catch (Exception e) {
                            new DmAlertMessageBox(e.getMessage(), txtHPartyCategory).show();
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
                srvAsync.invokeRPC(blnCallBackWait).getTxtHTDSCategory(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
                srvAsync.invokeRPC(blnCallBackWait).getTxtHReferencedTo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHReferencedTo_Mdl>>>() {

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
                srvAsync.invokeRPC(blnCallBackWait).getTxtHDuplicate(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
                srvAsync.invokeRPC(blnCallBackWait).getTxtHLadgerGroup(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
                srvAsync.invokeRPC(blnCallBackWait).getTxtHPartyType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
                srvAsync.invokeRPC(blnCallBackWait).getTxtHNature(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
                if (!DmShared.xNull(txtHNature.getTag()).trim().equalsIgnoreCase(txtHNature.getTag2nd())) {
                    natureManualCode = "";
                    manageTransporterControls();
                }
                if (blnStatus && tRow != null) {
                    txtHNature.setTag2nd(tRow.getSharedKeyFieldCode());
                    natureManualCode = tRow.getSharedPreparedBy();
                    manageTransporterControls();
                }
            }
        });

        txtHIsSelfTransport.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHYesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIsSelfTransport).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIsSelfTransport.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIsSelfTransport).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                if (blnStatus && tRow != null) {
                }
            }
        });

    }

    private void manageTransporterControls() {
        if (DmShared.xNull(natureManualCode).equalsIgnoreCase("TP")) {
            txtTransporterId.setEnabled(true);
            txtHIsSelfTransport.setEnabled(true);
        } else {
            txtTransporterId.setEnabled(false);
            txtHIsSelfTransport.setEnabled(false);

            txtTransporterId.setText("");
            txtTransporterId.setTag("");
            txtTransporterId.setTag2nd("");

            txtHIsSelfTransport.setText("No");
            txtHIsSelfTransport.setTag("0");
            txtHIsSelfTransport.setTag2nd("");

        }
    }

    private void tabChagneEvents() {
        tabPanel.addSelectionHandler(new SelectionHandler<Integer>() {

            @Override
            public void onSelection(SelectionEvent<Integer> event) {
                if (event.getSelectedItem() == 1) {

                }
            }
        });
    }

    private void findEvents() {
        dmFind.setTitle(this.asWidget().getTitle());

        dmFind.addHelpGridCallBack(new DmFind.FindCallBack<Find_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getFind(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Find_Mdl>>>() {

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

    private void gridEvents() {

        grdContactPerson.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                int r1 = event.getRowIndex();
                String addressCode = grdContactPerson.getGrid().getStore().get(r1).getSharedKeyFieldCode();

                showAddressBook(DmShared.xNull(addressCode));

            }
        });

        grdBankDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                int r1 = event.getRowIndex();
                String subCode = grdBankDetail.getGrid().getStore().get(r1).getSharedKeyFieldCode();
                String sNo = grdBankDetail.getGrid().getStore().get(r1).getSharedKeyFieldName();
                showBankDetail(DmShared.xNull(subCode), DmShared.xNull(sNo));

            }
        });

        grdAdditionalPlaceDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                int r1 = event.getRowIndex();
                int c1 = event.getColumnIndex();
                if (c1 == 1) {
                    String subCode = grdAdditionalPlaceDetail.getGrid().getStore().get(r1).getSharedKeyFieldCode();
                    String sNo = grdAdditionalPlaceDetail.getGrid().getStore().get(r1).getSharedKeyFieldName();
                    showAdditionalPlaceDetail(DmShared.xNull(subCode), DmShared.xNull(sNo));
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
        txtGstDate.addBlurHandler(this);
        txtDueDays.addValueChangeHandler(this);
    }

    private void topControlCard() {
        srvAsync.invokeRPC().getNavigator(new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
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
        srvAsync.invokeRPC().deleteRecord(strKey, new AsyncCallback<DmResult<String>>() {

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

    private AccountMaster_Mdl topControlSaveInitialize() {
        AccountMaster_Mdl mrTemp = new AccountMaster_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }
        mrTemp.setAccountNamePrefix(DmShared.xNull(txtHAccountNamePrefix.getText()));
        mrTemp.setCurrentDate(DmClient.getLoginDate());
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtName.getText()));
        mrTemp.setDisplayName(DmShared.xNull(txtDisplayName.getText()));
        mrTemp.setManualCode(DmShared.xNull(txtManualCode.getText()));
        mrTemp.setAccGroup(DmShared.xNull(txtHAccountGroup.getTag()));
        mrTemp.setAccGroupName(DmShared.xNull(txtHAccountGroup.getText()));
        mrTemp.setNatureCode(DmShared.xNull(txtHNature.getTag()));
        mrTemp.setNatureName(DmShared.xNull(txtHNature.getText()));
        mrTemp.setCostCenter(DmShared.xNull(txtHCostCenter.getTag()));

        mrTemp.setAddress(DmShared.xNull(txtAddress.getText()));
        mrTemp.setAddress1(DmShared.xNull(txtAddress1.getText()));
        mrTemp.setCity(DmShared.xNull(txtHCity.getTag()));
        mrTemp.setStateNumericCode(DmShared.xNull(txtState.getTag()));
        mrTemp.setZone(DmShared.xNull(txtHZone.getTag()));
        mrTemp.setPin(DmShared.xNull(txtPinCode.getText()));
        mrTemp.setPhoneNo(DmShared.xNull(txtPhoneNo.getText()));
        mrTemp.setMobileNo(DmShared.xNull(txtMobileNo.getText()));
        mrTemp.setFaxNo(DmShared.xNull(txtFaxNo.getText()));
        mrTemp.setEmail(DmShared.xNull(txtEMail.getText()));
        mrTemp.setPartyCategory(DmShared.xNull(txtHPartyCategory.getTag()));

        mrTemp.setTdsCategory(DmShared.xNull(txtHTDSCategory.getTag()));
        mrTemp.setReferencedTo(DmShared.xNull(txtHReferencedTo.getTag()));

        mrTemp.setPan(DmShared.xNull(txtPAN.getText()));
        mrTemp.setTin(DmShared.xNull(txtTIN.getText()));
        mrTemp.setDuplicate(DmShared.xNull(txtHDuplicate.getTag()));

        mrTemp.setCreditLimit(DmClient.vNull(txtCreditLimit.getText(), "0.00"));
        mrTemp.setDueDays(DmClient.vNull(txtDueDays.getText(), "0"));
        mrTemp.setIecNo(DmShared.xNull(txtIECNo.getText()));
        mrTemp.setLedgerGroup(DmShared.xNull(txtHLedgerGroup.getTag()));

        mrTemp.setPartyType(DmShared.xNull(txtHPartyType.getTag()));
        mrTemp.setRemark(DmShared.xNull(txtRemark.getText()));
        mrTemp.setGsTIN(DmShared.xNull(txtgsTIN.getText()));
        mrTemp.setAadhaar(DmShared.xNull(txtAadhar.getText()));
        mrTemp.setGsTINDate(DmShared.dNull(txtGstDate.getText()));
        mrTemp.getGrdBankDetail().addAll(grdBankDetail.getGrid().getStore().getAll());
        mrTemp.getGrdAdditionalPlaceDetail().addAll(grdAdditionalPlaceDetail.getGrid().getStore().getAll());

        mrTemp.setGstApplicable(defaultValue.isGstApplicable());

        //=============================<MJ>=============================//
        mrTemp.setNatureManualCode(natureManualCode);
        mrTemp.setTransportId(DmShared.xNull(txtTransporterId.getText()));
        mrTemp.setIsSelfTransportTag(DmShared.xNull(txtHIsSelfTransport.getTag()));
        mrTemp.setIsSelfTransportText(DmShared.xNull(txtHIsSelfTransport.getText()));
        //=============================<***>=============================//
        return mrTemp;
    }

    private void topControlSave() {
        AccountMaster_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            AccountMaster_Vld.chkSave(mrTemp);

            srvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
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
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<AccountMaster_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<AccountMaster_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<AccountMaster_Mdl> result) {
        try {
            AccountMaster_Mdl data;
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
                txtHNature.setTag2nd(data.getNatureCode());
                txtHCostCenter.setText(data.getCostCenterName());
                txtHCostCenter.setTag(data.getCostCenter());
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
                txtgsTIN.setText(data.getGsTIN());

                String PartyCategoryName = "";
                if (DmShared.xNull(data.getPartyCategory()).equalsIgnoreCase("D")) {
                    PartyCategoryName = "With In District";
                } else if (DmShared.xNull(data.getPartyCategory()).equalsIgnoreCase("O")) {
                    PartyCategoryName = "Out Of District";
                }
                txtHPartyCategory.setText(PartyCategoryName);
                txtHPartyCategory.setTag(data.getPartyCategory());

                txtHTDSCategory.setText(data.getTdsCategoryName());
                txtHTDSCategory.setTag(data.getTdsCategory());
                txtHReferencedTo.setText(data.getReferencedToName());
                txtHReferencedTo.setTag(data.getReferencedTo());

                txtPAN.setText(data.getPan());
                txtTIN.setText(data.getTin());
                txtAadhar.setText(data.getAadhaar());
                txtGstDate.setText(data.getGsTINDate());
                String DuplicateName = "";
                if (DmShared.xNull(data.getDuplicate()).equalsIgnoreCase("1")) {
                    DuplicateName = "Yes";
                } else if (DmShared.xNull(data.getDuplicate()).equalsIgnoreCase("0")) {
                    DuplicateName = "No";
                }
                txtHDuplicate.setText(DuplicateName);
                txtHDuplicate.setTag(data.getDuplicate());

                txtCreditLimit.setText(String.valueOf(data.getCreditLimit()));
                txtDueDays.setText(String.valueOf(data.getDueDays()));
                txtIECNo.setText(data.getIecNo());
                txtHLedgerGroup.setText(data.getLedgerGroupName());
                txtHLedgerGroup.setTag(data.getLedgerGroup());
                grdBankDetail.getGrid().getStore().clear();
                grdBankDetail.getGrid().getStore().addAll(data.getGrdBankDetail());

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

                //=================================<MJ>=================================//
                natureManualCode = data.getNatureManualCode();
                txtTransporterId.setText(data.getTransportId());
                txtHIsSelfTransport.setTag(DmShared.xNull(data.getIsSelfTransportTag()));
                txtHIsSelfTransport.setText(DmShared.xNull(data.getIsSelfTransportText()));

                //=================================<****>=================================//
                userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
                userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());
                grdContactPerson.getGrid().getStore().clear();
                grdContactPerson.getGrid().getStore().addAll(data.getListContectPerson());

                grdAdditionalPlaceDetail.getGrid().getStore().clear();
                grdAdditionalPlaceDetail.getGrid().getStore().addAll(data.getGrdAdditionalPlaceDetail());

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
            case 600:
                new DmAlertMessageBox(errorMessage, txtHCity).show();
                break;
            case 700:
                new DmAlertMessageBox(errorMessage, txtgsTIN).show();
                break;
            case 800:
                new DmAlertMessageBox(errorMessage, txtGstDate).show();
                break;
            case 850:
                new DmAlertMessageBox(errorMessage, txtgsTIN).show();
                break;
            case 900:
                new DmAlertMessageBox(errorMessage, txtAadhar).show();
                break;
            case 925:
                new DmAlertMessageBox(errorMessage, txtAadhar).show();
                break;
            case 950:
                new DmAlertMessageBox(errorMessage, txtPAN).show();
                break;
            case 960:
                new DmAlertMessageBox(errorMessage, txtTransporterId).show();
                break;
            case 1000:
                new DmAlertMessageBox(errorMessage, txtHPartyType).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtName).show();
                break;
        }
    }

    public AccountMaster(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        srvAsync = new ServiceInvoker<>(GWT.create(AccountMaster_Srv.class), AccountMaster.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        textHelpEvents();
        tabChagneEvents();
        onLoadEvents();
        gridEvents();
        findEvents();
        flwForm.addAttachHandler(this);
        iniDefaultValue(swi);
    }

    private void iniDefaultValue(final SetWidgetInvoker swi) {
        srvAsync.invokeRPC().getDefaultValues(new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    defaultValue = result.getData();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
        swi.setWidget(this);
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
        } else if (event.getSelectedItem().getText().equalsIgnoreCase(mnuExportData.getText())) {
            if (topControl.getMode().equalsIgnoreCase("Browse")) {
                new DmConfirmMessageBox("Are you sure? You want to export records.", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            exportCSV();
                        }
                    }
                }).show();
            }
        }
    }

    private void exportCSV() {
        srvAsync.invokeRPC().exportCSV(DmRequestBuilder.getSessionId(), new AsyncCallback<DmResult<KeyFieldExtended>>() {
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
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
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

    private void clear() {
        txtHDuplicate.setTag("0");
        txtHDuplicate.setText("No");
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        txtState.setEnabled(false);
        tabPanel.selectTab(0);
        grdBankDetail.getGrid().getStore().clear();
        grdContactPerson.getGrid().getStore().clear();
        grdAdditionalPlaceDetail.getGrid().getStore().clear();
        natureManualCode = "";
        manageTransporterControls();

    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtName)) {
            if (!DmShared.xNull(txtName.getText()).equalsIgnoreCase("")) {
                if (DmShared.xNull(txtDisplayName.getText()).equalsIgnoreCase("")) {
                    txtDisplayName.setText(txtName.getText());
                }
            }
        } else if (event.getSource().equals(txtGstDate)) {
            txtGstDate.setText(DmClient.returnDate(txtGstDate.getText()));
        }
    }

    private void showAddressBook(String addressCode) {
        final HashMap<String, String> hm = new HashMap<>();
        hm.put("addressCode", addressCode);
        hm.put("orgCode", topControl.getDocId());
        hm.put("orgName", txtName.getText());
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(new lmAccounts().moduleName, new lmAccounts().ADDRESS_BOOK.caption, null, null, new SetWidgetInvokerImpl() {
            @Override
            public void setWidget(final IsWidget wgtObject) {
                ((IsWidgetExtended) wgtObject).action("ORGANIZATION", hm, new DmCallBack() {
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

    private void showAdditionalPlaceDetail(String subCode, String sNo) {
        final HashMap<String, String> hm = new HashMap<>();
        hm.put("grdPartyCode", subCode);
        hm.put("grdsNo", sNo);
        hm.put("partyCode", topControl.getDocId());
        hm.put("partyName", txtName.getText());
        hm.put("stateCode", txtState.getTag());
        hm.put("stateName", txtState.getText());
        hm.put("cityCode", txtHCity.getTag());
        hm.put("cityName", txtHCity.getText());

        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(new lmAccounts().moduleName, new lmAccounts().ADDITIONAL_PLACE_OF_BUSINESS.caption, null, null, new SetWidgetInvokerImpl() {
            @Override
            public void setWidget(final IsWidget wgtObject) {
                ((IsWidgetExtended) wgtObject).action("ADDITIONAL_PLACE_DETAIL", hm, new DmCallBack() {
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
