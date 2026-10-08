package aarogya.master.corporateEmployeeMaster.client;

import aarogya.master.corporateEmployeeMaster.shared.*;
import aarogya.selfToolkit.stStatic.client.STClient;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.util.List;

public final class CorporateEmployeeMaster extends CorporateEmployeeMaster_Dsg implements BlurHandler, KeyUpHandler {

    private final ServiceInvoker<CorporateEmployeeMaster_SrvAsync> SrvAsync;
    private KeyFieldExtended defaultValue;

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());

        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);

        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                clearBlogs();
                onAdd();
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                txtHCorporate.setFocus(true);
                txtEmpCode.setEnabled(true);
                txtState.setEnabled(false);

                if (!DmShared.dNull(txtDob.getText()).equalsIgnoreCase("")) {
                    manageAgeBlocks(false);
                } else {
                    manageAgeBlocks(true);
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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(CorporateEmployeeMaster.this);
            }
        });

        topControl.setDisplay(true);

    }

    private void textHelpEvents() {

        txtHCorporate.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHCorporate(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCorporate).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHCorporate.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCorporate).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
//                corporateCode = txtHCorporate.getTag();
            }
        });
        txtEmpName.addCallBack(new DmSuggestBox.CallBack() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getSuggestion(helper, blnToEndOfFile, txtHCorporate.getTag(), new AsyncCallback<DmResultPageing<List<String>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtEmpName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<String>> result) {
                        if (result.isStatus()) {
                            txtEmpName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtEmpName).show();
                        }
                    }
                });
            }
        });
        txtHRelationship.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTextHRelationship(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHRelationship).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHRelationship.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHRelationship).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });
        txtHBloodGroup.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getHL7Mast(helper, blnToEndOfFile, "bloodGroup", new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
        txtHGender.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getHL7Mast(helper, blnToEndOfFile, "administrativeSex", new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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

    private void topControlMoveRec(String strKey) {
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<CorporateEmployeeMaster_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<CorporateEmployeeMaster_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private CorporateEmployeeMaster_Mdl topControlSaveInitialize() {

        CorporateEmployeeMaster_Mdl mrTemp = new CorporateEmployeeMaster_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }

        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtEmpName.getText()).trim());
        mrTemp.setManualCode(DmShared.xNull(txtEmpCode.getText()).trim());
        mrTemp.setCorporateCode(DmShared.xNull(txtHCorporate.getTag()).trim());
        mrTemp.setCorporateName(DmShared.xNull(txtHCorporate.getText()).trim());
        mrTemp.setPolicyNo(DmShared.xNull(txtPolicyNo.getText()).trim());
        mrTemp.setCardHolderName(DmShared.xNull(txtCardHolderName.getText()).trim());
        mrTemp.setRelationship(DmShared.xNull(txtHRelationship.getText()).trim());
        mrTemp.setRank(DmShared.xNull(txtRank.getText()).trim());
        mrTemp.setAddress1(DmShared.xNull(txtAddress1.getText()));
        mrTemp.setAddress2(DmShared.xNull(txtAddress2.getText()));
        mrTemp.setCityTag(DmShared.xNull(txtHCity.getTag()));
        mrTemp.setPin(DmShared.xNull(txtPinCode.getText()));
        mrTemp.setZoneTag(DmShared.xNull(txtHZone.getTag()));
        mrTemp.setMobile(DmShared.xNull(txtMobile.getText()));
        mrTemp.setDob(DmShared.dNull(txtDob.getText()));
        mrTemp.setGenderTag(DmShared.xNull(txtHGender.getTag()));
        mrTemp.setBloodGroupTag(DmShared.xNull(txtHBloodGroup.getTag()));
        mrTemp.setAgeYear(DmClient.vNull(txtAgeYear.getText(), "0.00"));
        mrTemp.setAgeMonth(DmClient.vNull(txtAgeMonth.getText(), "0.00"));
        mrTemp.setAgeDay(DmClient.vNull(txtAgeDay.getText(), "0.00"));
        mrTemp.setShowDDUptoMonth(DmShared.xNull(defaultValue.getSharedKeyFieldName()));
        mrTemp.setShowMMUptoYear(DmShared.xNull(defaultValue.getSharedPreparedBy()));
        mrTemp.setDtVoucher(DmShared.xNull(defaultValue.getSharedKeyFieldCode()));
        manageAgeBlocks(true);

        return mrTemp;
    }

    private void topControlSave() {
        CorporateEmployeeMaster_Mdl mrTemp;
        try {
            mrTemp = topControlSaveInitialize();
            CorporateEmployeeMaster_Vld.chkSave(mrTemp);
            SrvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {

                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        topControl.setDocId(result.getData());

                        if (topControl.getMode().equalsIgnoreCase("New")) {

                            topControl.btnClick("New");

                            txtEmpName.setFocus(true);

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

    private void topControlMoveRecSuccess(DmResult<CorporateEmployeeMaster_Mdl> result) {

        CorporateEmployeeMaster_Mdl data;

        if (result.isStatus()) {
            data = result.getData();
            topControl.setDocId(DmShared.xNull(data.getSharedKeyFieldCode()));
            txtEmpName.setText(data.getSharedKeyFieldName());
            txtEmpCode.setText(data.getManualCode());
            txtHCorporate.setText(data.getCorporateName());
            txtHCorporate.setTag(data.getCorporateCode());
            txtPolicyNo.setText(data.getPolicyNo());
            txtCardHolderName.setText(data.getCardHolderName());
            txtHRelationship.setText(data.getRelationship());
            txtHRelationship.setTag(data.getRelationship());
            txtRank.setText(data.getRank());
            txtAddress1.setText(data.getAddress1());
            txtAddress2.setText(data.getAddress2());
            txtDob.setText(data.getDob());
            txtDob.setTag(data.getTempDOB());
            txtHBloodGroup.setText(data.getBloodGroupText());
            txtHBloodGroup.setTag(data.getBloodGroupTag());
            txtHGender.setTag(data.getGenderTag());
            txtHGender.setText(data.getGenderText());
            txtHCity.setText(data.getCityText());
            txtHCity.setTag(data.getCityTag());
            txtState.setText(data.getState());
            txtHZone.setText(data.getZoneText());
            txtHZone.setTag(data.getZoneTag());
            txtPinCode.setText(data.getPin());
            txtAgeDay.setText("");
            txtAgeMonth.setText("");
            txtAgeYear.setText("");
            fillDob(txtDob.getTag());
            txtMobile.setText(data.getMobile());
            userControl.setTextBlog(preparedByBlog, DmShared.xNull(data.getSharedPreparedBy()));
            userControl.setTextBlog(modifiedByBlog, DmShared.xNull(data.getSharedModifiedBy()));
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
    }

    private void topControlSaveError(int errorNo, String errorMessage) {
        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtHCorporate).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtEmpName).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtCardHolderName).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtHRelationship).show();
                break;
            case 600:
                new DmAlertMessageBox(errorMessage, txtAgeYear).show();
                break;
            case 601:
                new DmAlertMessageBox(errorMessage, txtAgeYear).show();
                break;
            case 602:
                new DmAlertMessageBox(errorMessage, txtAgeMonth).show();
                break;
            case 603:
                new DmAlertMessageBox(errorMessage, txtAgeDay).show();
            case 700:
                new DmAlertMessageBox(errorMessage, txtHGender).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtEmpName).show();
                break;
        }
    }

    public CorporateEmployeeMaster(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        SrvAsync = new ServiceInvoker<>(GWT.create(CorporateEmployeeMaster_Srv.class), CorporateEmployeeMaster.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        handlerEvents();
        textHelpEvents();
        findEvents();
        iniDefaultValues(swi);
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
                    clearBlogs();
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
                        topControl.setNavigator(result.getData());
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            }
        });
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (event.getSource().equals(txtDob)) {
            if (DmShared.xNull(txtDob.getText()).trim().equalsIgnoreCase("")) {
                manageAgeBlocks(true);
            } else {
                manageAgeBlocks(false);
            }
        }
    }

    public void onAdd() {
        txtHCorporate.setFocus(true);
        txtEmpCode.setEnabled(false);
        txtState.setEnabled(false);
    }

    public void clearBlogs() {
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtEmpName)) {
            if (!DmShared.xNull(txtEmpName.getText()).equalsIgnoreCase("")) {
                if (DmShared.xNull(txtCardHolderName.getText()).equalsIgnoreCase("")) {
                    txtCardHolderName.setText(txtEmpName.getText());
                }
            }
        } else if (event.getSource().equals(txtDob)) {
            if (!DmShared.xNull(txtDob.getText()).trim().equalsIgnoreCase("")) {
                txtDob.setText(DmClient.returnDate(txtDob.getText()));
                fillDob(DmShared.xNull(txtDob.getText()));
                if ((DmShared.convertDate(txtDob.getText()).getTime() > (DmShared.convertDate(DmClient.getLoginDate())).getTime())) {
                    new DmAlertMessageBox("DOB should be less than or equal to Current Date.", txtDob).show();
                    return;
                }
            }
        }
    }

    private void handlerEvents() {
        txtEmpName.addBlurHandler(this);
        txtDob.addBlurHandler(this);
        txtDob.addKeyUpHandler(this);
    }

    @Override
    public void searchRecord(String key) {
    }

    private void iniDefaultValues(final SetWidgetInvoker swi) {
        SrvAsync.invokeRPC(true).getDefaultValues(new AsyncCallback<DmResult<KeyFieldExtended>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    defaultValue = result.getData();
                    swi.setWidget(CorporateEmployeeMaster.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void fillDob(String DOB) {
        if (!DmShared.xNull(DOB).trim().equalsIgnoreCase("")) {
            STClient.Age age = STClient.getAge(defaultValue.getSharedKeyFieldCode(), DOB, DmShared.iNull(defaultValue.getSharedKeyFieldName()).intValue(), DmShared.iNull(defaultValue.getSharedPreparedBy()).intValue());
            txtAgeDay.setText(DmClient.vNull(age.getDay(), "00"));
            txtAgeMonth.setText(DmClient.vNull(age.getMonth(), "00"));
            txtAgeYear.setText(DmClient.vNull(age.getYear(), "00"));
        }
    }

    private void manageAgeBlocks(boolean flag) {
        txtAgeDay.setEnabled(flag);
        txtAgeMonth.setEnabled(flag);
        txtAgeYear.setEnabled(flag);
    }

}
