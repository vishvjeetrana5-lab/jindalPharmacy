package dataman.utility.siteMaster.client;

import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import dataman.utility.siteMaster.shared.*;
import java.util.List;

public class SiteMaster extends SiteMaster_Dsg implements BlurHandler, KeyPressHandler {

    private ServiceInvoker<SiteMaster_SrvAsync> srvAsync;
    private String currentDate = "";

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);
        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                txtHBranch.setFocus(true);
                txtSiteName.setEnabled(false);
                txtCity.setEnabled(false);
                txtHHeadOffice.setTag("0");
                txtHHeadOffice.setText("No");
                clear();
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                txtHHeadOffice.setFocus(true);
                txtSiteName.setEnabled(false);
                txtCity.setEnabled(false);
                txtCode.setEnabled(false);
                txtHBranch.setEnabled(false);
                txtHProductLicenseKey.setEnabled(false);
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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(SiteMaster.this);
            }
        });

        topControl.setDisplay(true);
    }

    public SiteMaster() {
    }

    private void textHelpEvents() {

        txtHBranch.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHBranch_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHBranch(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHBranch_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHBranch).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHBranch_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHBranch.setData(result.getData(), result.getPaging());

                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHBranch).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHBranch_Mdl tRow) {
                if (blnStatus) {
                    if (tRow != null) {
                        txtCity.setText(tRow.getCityName());
                        setSiteName();
                    }
                }
            }

        });
        txtHProductLicenseKey.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHPLT_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHPLT(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHPLT_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHProductLicenseKey).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHPLT_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHProductLicenseKey.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHProductLicenseKey).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHPLT_Mdl t1Row) {
                if (blnStatus) {
                    if (t1Row != null) {
                        txtHProductLicenseKey.setTag2nd(t1Row.getShortName());
                        setSiteName();
                    }
                }

            }
        });
        txtHHeadOffice.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHHeadOffice(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHHeadOffice).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHHeadOffice.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHHeadOffice).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                if (blnStatus) {
                }
            }
        });

        txtHRegionalLanguage.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHRegionalLanguage(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHRegionalLanguage).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHRegionalLanguage.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHRegionalLanguage).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                if (blnStatus) {
                }
            }
        });

        txtHRegistrationType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHRegistrationType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHRegistrationType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHRegistrationType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHRegistrationType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {

            }
        });
        txtHState.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHState(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHState).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHState.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHState).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
    }

    private void setSiteName() {
        txtSiteName.setText(DmShared.xNull(txtHBranch.getText()) + " [" + DmShared.xNull(txtHProductLicenseKey.getTag2nd()) + "]");
        if (DmShared.xNull(txtHBranch.getText()).equalsIgnoreCase("") && DmShared.xNull(txtHProductLicenseKey.getText()).equalsIgnoreCase("")) {
            txtSiteName.setText("");
        }
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
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), dmFind.getTextBox()).show();
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

    private void textEvent() {
        txtTinNoDate.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                txtTinNoDate.setText(DmClient.returnDate(txtTinNoDate.getText()));
            }
        });

    }

    public SiteMaster(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        srvAsync = new ServiceInvoker<SiteMaster_SrvAsync>(GWT.create(SiteMaster_Srv.class), SiteMaster.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        txtPhone.addKeyPressHandler(this);
        txtGSTINDate.addBlurHandler(this);
        textHelpEvents();
        findEvents();
        textEvent();
        getServerTime();
        swi.setWidget(this);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private SiteMaster_Mdl topControlSaveInitialize() {
        SiteMaster_Mdl mrTemp = new SiteMaster_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }
        mrTemp.setBranchCode(DmShared.xNull(txtHBranch.getTag()));
        mrTemp.setBranchName(DmShared.xNull(txtHBranch.getText()));
        mrTemp.setPltCode(DmShared.xNull(txtHProductLicenseKey.getTag()));
        mrTemp.setPltName(DmShared.xNull(txtHProductLicenseKey.getText()));
        mrTemp.setSiteName(DmShared.xNull(txtSiteName.getText()));
        mrTemp.setCode(DmShared.xNull(txtCode.getText()));
        mrTemp.setRegionalLanguageCode(DmShared.xNull(txtHRegionalLanguage.getTag()));
        mrTemp.setRegionalLanguageName(DmShared.xNull(txtHRegionalLanguage.getText()));
        mrTemp.setHoCode(DmShared.xNull(txtHHeadOffice.getTag()));
        mrTemp.setHoName(DmShared.xNull(txtHHeadOffice.getText()));
        mrTemp.setManualCode(DmShared.xNull(txtManualCode.getText()));
        mrTemp.setSiteDescription(DmShared.xNull(txtSiteDescription.getText()));
        mrTemp.setAddress1(DmShared.xNull(txtAddress1.getText()));
        mrTemp.setAddress2(DmShared.xNull(txtAddress2.getText()));
        mrTemp.setCity(DmShared.xNull(txtCity.getText()));
        mrTemp.setPinNO(DmShared.xNull(txtPinNo.getText()));
        mrTemp.setCountry(DmShared.xNull(txtCountry.getText()));
        mrTemp.setGram(DmShared.xNull(txtGram.getText()));
        mrTemp.setPhone(DmShared.xNull(txtPhone.getText()));
        mrTemp.setFaxNo(DmShared.xNull(txtFaxNo.getText()));
        mrTemp.setEmail(DmShared.xNull(txtEmail.getText()));
        mrTemp.setNotificationNo(DmShared.xNull(txtNotificationNo.getText()));
        mrTemp.setTinNo(DmShared.xNull(txtTinNo.getText()));
        mrTemp.setTinDate(DmShared.dNull(txtTinNoDate.getText()));
        mrTemp.setPanNo(DmShared.xNull(txtPanNo.getText()));
        mrTemp.setGsTin(DmShared.xNull(txtGSTin.getText()));
        mrTemp.setCin(DmShared.xNull(txtCin.getText()));
        mrTemp.setGstinDate(DmShared.dNull(txtGSTINDate.getText()));
        mrTemp.setStateName(DmShared.xNull(txtHState.getText()));
        mrTemp.setStateNumericCode(DmClient.vNull(txtHState.getTag(), "00"));
        mrTemp.setRegistrationTypeCode(DmShared.xNull(txtHRegistrationType.getTag()));
        mrTemp.setCurrentDate(currentDate);
        mrTemp.setDrugLicenseNo(DmShared.xNull(txtDrugLicenseNo.getText()));
        mrTemp.setClientCode(DmShared.xNull(txtClientCode.getText()));
        mrTemp.setSerialKey(DmShared.xNull(txtSerialKey.getText()));
        mrTemp.setSiteDisplayName(DmShared.xNull(txtSiteDisplayName.getText()));
        mrTemp.setIeCode(DmShared.xNull(txtIECode.getText()));
        mrTemp.setRbiCode(DmShared.xNull(txtRBICode.getText()));

        return mrTemp;
    }

    private void topControlSave() {
        SiteMaster_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            SiteMaster_Vld.chkSave(mrTemp);

            srvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
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
                        topControlSaveError(result.getErrorNumber(), DmShared.xNull(result.getErrorMessage()));
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
                new DmAlertMessageBox(errorMessage, txtHBranch).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtHRegionalLanguage).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtHProductLicenseKey).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtSiteName).show();
                break;
            case 450:
                new DmAlertMessageBox(errorMessage, txtSiteDisplayName).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtCode).show();
                break;
            case 600:
                new DmAlertMessageBox(errorMessage, txtHHeadOffice).show();
                break;
            case 700:
                new DmAlertMessageBox(errorMessage, txtManualCode).show();
                break;
            case 750:
                new DmAlertMessageBox(errorMessage, txtHState).show();
                break;
            case 770:
                new DmAlertMessageBox(errorMessage, txtHRegistrationType).show();
                break;
            case 800:
                new DmAlertMessageBox(errorMessage, txtEmail).show();
                break;
            case 850:
                new DmAlertMessageBox(errorMessage, txtPanNo).show();
                break;
            case 900:
                new DmAlertMessageBox(errorMessage, txtTinNo).show();
                break;
            case 1000:
                new DmAlertMessageBox(errorMessage, txtGSTin).show();
                break;
            case 1100:
                new DmAlertMessageBox(errorMessage, txtGSTINDate).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, (DmTextBox) null).show();
                break;
        }

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
                        clear();
                        topControl.setNavigator(result.getData());
                    } else {
                        new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                    }
                }
            }
        });
    }

    private void topControlMoveRec(String strKey) {
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<SiteMaster_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<SiteMaster_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<SiteMaster_Mdl> result) {
        SiteMaster_Mdl data;

        if (result.isStatus()) {
            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());

            userControl.setTextBlog(preparedByBlog, data.getPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getModifiedBy());

            txtHBranch.setTag(data.getBranchCode());
            txtHBranch.setText(data.getBranchName());
            txtHRegionalLanguage.setTag(data.getRegionalLanguageCode());
            txtHRegionalLanguage.setText(data.getRegionalLanguageName());
            txtHProductLicenseKey.setText(data.getPltName());
            txtHProductLicenseKey.setTag(data.getPltCode());
            txtHProductLicenseKey.setTag2nd(data.getPltShortName());
            txtSiteName.setText(data.getSiteName());
            txtCode.setText(data.getCode());
            txtHHeadOffice.setTag(data.getHoCode());
            txtHHeadOffice.setText(data.getHoName());
            txtManualCode.setText(data.getManualCode());
            txtSiteDescription.setText(data.getSiteDescription());
            txtAddress1.setText(data.getAddress1());
            txtAddress2.setText(data.getAddress2());
            txtCity.setText(data.getCity());
            txtPinNo.setText(data.getPinNO());
            txtCountry.setText(data.getCountry());
            txtGram.setText(data.getGram());
            txtPhone.setText(data.getPhone());
            txtFaxNo.setText(data.getFaxNo());
            txtEmail.setText(data.getEmail());
            txtNotificationNo.setText(data.getNotificationNo());
            txtTinNo.setText(data.getTinNo());
            txtTinNoDate.setText(data.getTinDate());
            txtPanNo.setText(data.getPanNo());
            txtGSTin.setText(data.getGsTin());
            txtCin.setText(data.getCin());
            txtGSTINDate.setText(data.getGstinDate());
            txtHState.setText(data.getStateName());
            txtHState.setTag(data.getStateNumericCode());
            txtHRegistrationType.setText(data.getRegistrationTypeName());
            txtHRegistrationType.setTag(data.getRegistrationTypeCode());
            txtDrugLicenseNo.setText(data.getDrugLicenseNo());
            txtClientCode.setText(data.getClientCode());
            txtSerialKey.setText(data.getSerialKey());
            txtSiteDisplayName.setText(data.getSiteDisplayName());
            txtIECode.setText(data.getIeCode());
            txtRBICode.setText(data.getRbiCode());
            setSiteName();
        }
        topControl.setDisplay(true);
    }

    private void topControlDelete(String strKey) {
        srvAsync.invokeRPC().deleteRecord(strKey, DmShared.xNull(txtHProductLicenseKey.getTag()), new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    topControlCard();
                } else {
                    new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                }
            }
        });
    }

    public void clear() {
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtPhone)) {
            if (DmShared.xNull(txtPhone.getText()).equalsIgnoreCase("")) {
                txtPhone.setText(txtPhone.getText());
            }
        } else if (event.getSource().equals(txtGSTINDate)) {
            txtGSTINDate.setText(DmClient.returnDate(txtGSTINDate.getText()));

        }

    }

    @Override
    public void onKeyPress(KeyPressEvent event) {
        int charCode = event.getCharCode();
        if (event.getSource().equals(txtPhone)) {
            if ((charCode > 31) && ((charCode < 48 || charCode > 57) && (charCode != 45 && charCode != 44))) {
                txtPhone.cancelKey();
            }
        }
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    private void getServerTime() {
        srvAsync.invokeRPC().getServerDateTime(new AsyncCallback<String[]>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtGSTINDate).show();
            }

            @Override
            public void onSuccess(String[] result) {
                currentDate = result[0];
            }
        });
    }

}
