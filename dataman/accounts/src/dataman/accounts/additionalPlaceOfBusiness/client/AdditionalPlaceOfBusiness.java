package dataman.accounts.additionalPlaceOfBusiness.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.accounts.additionalPlaceOfBusiness.shared.AdditionalPlaceOfBusiness_Mdl;
import dataman.accounts.additionalPlaceOfBusiness.shared.AdditionalPlaceOfBusiness_Srv;
import dataman.accounts.additionalPlaceOfBusiness.shared.AdditionalPlaceOfBusiness_SrvAsync;
import dataman.accounts.additionalPlaceOfBusiness.shared.AdditionalPlaceOfBusiness_Vld;
import dataman.accounts.additionalPlaceOfBusiness.shared.Find_Mdl;
import dataman.accounts.additionalPlaceOfBusiness.shared.TxtHCity_Mdl;
import dataman.accounts.additionalPlaceOfBusiness.shared.TxtHLinkWithAccount_Mdl;
import dataman.dmBase.self.shared.DmCallBack;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmBase.self.shared.SetWidgetInvoker;
import dataman.dmBase.self.shared.UserMenu;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.modifyReason.client.ModifyReason;
import dataman.dmToolkit.modifyReason.shared.ModifyReason_Mdl;
import java.util.HashMap;
import java.util.List;

public final class AdditionalPlaceOfBusiness extends AdditionalPlaceOfBusiness_Dsg {

    private final ServiceInvoker<AdditionalPlaceOfBusiness_SrvAsync> SrvAsync;
    private final ModifyReason modifyReason = new ModifyReason();
    private ModifyReason_Mdl modifyReasonMdl;

    public AdditionalPlaceOfBusiness(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(AdditionalPlaceOfBusiness_Srv.class), AdditionalPlaceOfBusiness.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        textHelpEvents();
        findEvents();
        swi.setWidget(AdditionalPlaceOfBusiness.this);
    }

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());

        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);

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
                if (!DmShared.xNull(topControl.getDocId()).equalsIgnoreCase("")) {
                    manageDisplay(true);
                    txtAddress1.setFocus(true);
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
                            showModifyReason();
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
                if (DmShared.xNull(topControl.getMode()).equalsIgnoreCase("Edit")) {
                    showModifyReason();
                } else {
                    topControlSave();
                }
            }
        });

        topControl.addOtherHandeller(new DmTopControl.eOther() {
            @Override
            public void otherClicked() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(AdditionalPlaceOfBusiness.this);
            }
        });

        topControl.setDisplay(true);

    }

    private void showModifyReason() {
        modifyReason.showNew(topControl.getMode(), txtHAccount.getTag(), txtHAccount.getTag2nd(), "additionalPlaceOfBusiness");
    }

    private void textHelpEvents() {

        modifyReason.addCallBack(new ModifyReason.CallBack() {

            @Override
            public void onOk(ModifyReason_Mdl modifyReason) {
                try {
                    modifyReasonMdl = modifyReason;
                    if (topControl.getMode().equalsIgnoreCase("Browse") && !DmShared.xNull(topControl.getDocId()).trim().equalsIgnoreCase("")) {
                        topControlDelete(topControl.getDocId());
                    } else {
                        topControlSave();
                    }
                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
                }
            }
        });

        txtPlace.addCallBack(new DmSuggestBox.CallBack() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getSuggestion(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<String>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtPlace).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<String>> result) {
                        if (result.isStatus()) {
                            txtPlace.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtPlace).show();
                        }
                    }
                });
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
                if (!DmShared.xNull(txtHAccount.getTag()).equalsIgnoreCase(DmShared.xNull(txtHAccount.getTag2nd()))) {
                    resetAc();
                    txtHAccount.setTag2nd(txtHAccount.getTag());
                }
                if (blnStatus && tRow != null) {
                    txtHCity.setTag(DmShared.xNull(tRow.getCityCode()));
                    txtHCity.setTag2nd(DmShared.xNull(tRow.getCityCode()));
                    txtHCity.setText(DmShared.xNull(tRow.getCityName()));
                    txtState.setText(DmShared.xNull(tRow.getStateName()));
                    txtState.setTag(tRow.getStateCode());
                    txtCountry.setText(DmShared.xNull(tRow.getCountry()));
                }
            }
        });

        txtHCity.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHCity_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHCity(txtState.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHCity_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCity).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHCity_Mdl>> result) {
                        try {
                            if (result.isStatus()) {
                                txtHCity.setData(result.getData(), result.getPaging());
                            }
                        } catch (Exception e) {
                            new DmAlertMessageBox(e.getMessage(), txtHCity).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHCity_Mdl tRow) {
                if (!DmShared.xNull(txtHCity.getTag()).equalsIgnoreCase(DmShared.xNull(txtHCity.getTag2nd()))) {
                    resetCity();
                    txtHCity.setTag2nd(txtHCity.getTag());
                }
                if (blnStatus && tRow != null) {
                    txtState.setText(DmShared.xNull(tRow.getStateName()));
                    txtState.setTag(tRow.getStateCode());
                    txtCountry.setText(DmShared.xNull(tRow.getCountryName()));
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
                    topControlMoveRec(tRow.getSharedKeyFieldCode());
                }
            }
        });
    }

    private void topControlMoveRec(String strKey) {
        try {
            SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<AdditionalPlaceOfBusiness_Mdl>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<AdditionalPlaceOfBusiness_Mdl> result) {
                    topControlMoveRecSuccess(result);
                }
            });
        } catch (Exception e) {
        }
    }

    private void topControlSaveError(int errorNo, String errorMessage) {

        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtPlace).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtHAccount).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtPinNo).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtMobile).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtEmail).show();
                break;
            case 700:
                new DmAlertMessageBox(errorMessage, txtAddress1).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHAccount).show();
                break;
        }

    }

    private void topControlMoveRecSuccess(DmResult<AdditionalPlaceOfBusiness_Mdl> result) {
        AdditionalPlaceOfBusiness_Mdl data;
        clear();
        if (result.isStatus()) {
            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());
            txtPlace.setText(data.getSharedKeyFieldName());
            txtHAccount.setTag(data.getAccountTag());
            txtHAccount.setText(data.getAccountText());
            txtHAccount.setTag2nd(data.getsNo());
            txtAddress1.setText(data.getAddress1());
            txtAddress2.setText(data.getAddress2());
            txtHCity.setTag(data.getCityCode());
            txtHCity.setTag2nd(data.getCityCode());
            txtHCity.setText(data.getCityName());
            txtState.setText(data.getStateName());
            txtState.setTag(data.getStateCode());
            txtCountry.setText(data.getCountryName());
            txtPinNo.setText(data.getPin());
            txtMobile.setText(data.getMobile());
            txtEmail.setText(data.getEmail());
            txtIsdCode.setText(data.getIsdCode());
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
    }

    private AdditionalPlaceOfBusiness_Mdl topControlSaveInitialize() {

        AdditionalPlaceOfBusiness_Mdl mrTemp = new AdditionalPlaceOfBusiness_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
            mrTemp.setsNo(txtHAccount.getTag2nd());
        }
        mrTemp.setAccountTag(DmShared.xNull(txtHAccount.getTag()));
        mrTemp.setAccountText(DmShared.xNull(txtHAccount.getText()));
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtPlace.getText()));
        mrTemp.setAddress1(DmShared.xNull(txtAddress1.getText()));
        mrTemp.setAddress2(DmShared.xNull(txtAddress2.getText()));
        mrTemp.setCityCode(DmShared.xNull(txtHCity.getTag()));
        mrTemp.setCityName(DmShared.xNull(txtHCity.getText()));
        mrTemp.setStateName(DmShared.xNull(txtState.getText()));
        mrTemp.setStateCode(DmShared.xNull(txtState.getTag()));
        mrTemp.setCountryName(DmShared.xNull(txtCountry.getText()));
        mrTemp.setPin(DmShared.xNull(txtPinNo.getText()));
        mrTemp.setEmail(DmShared.xNull(txtEmail.getText()));
        mrTemp.setMobile(DmShared.xNull(txtMobile.getText()));
        mrTemp.setIsdCode(DmShared.xNull(txtIsdCode.getText()));
        if (modifyReasonMdl != null) {
            mrTemp.setModifyReasonMdl(modifyReasonMdl);
        }
        return mrTemp;
    }

    private void topControlSave() {
        AdditionalPlaceOfBusiness_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            AdditionalPlaceOfBusiness_Vld.chkSave(mrTemp);

            SrvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHAccount).show();
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

    private void onAdd() {
        manageDisplay(true);
        txtHAccount.setFocus(true);
    }

    private void manageDisplay(boolean flag) {
        txtState.setEnabled(false);
        txtCountry.setEnabled(false);

        if (topControl.getMode().equalsIgnoreCase("Edit")) {
            txtHAccount.setEnabled(false);
        }
    }

    @Override
    public Widget asWidget() {
        return flwForm;
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
        SrvAsync.invokeRPC().deleteRecord(strKey, modifyReasonMdl, new AsyncCallback<DmResult<String>>() {

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

    private void clear() {
        modifyReasonMdl = null;
        resetCity();
        resetAc();
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    @Override
    public void action(String nature, Object object, DmCallBack callback) {

        nature = DmShared.xNull(nature).trim().toUpperCase();
        try {
            switch (nature) {
                case "ADDITIONAL_PLACE_DETAIL":
                    openWith((HashMap<String, String>) object, callback);
                    break;
                case "FOCUS_ON_ADD":
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            txtHAccount.setFocus(true);
                        }
                    });
                    break;
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    public void openWith(HashMap<String, String> param, DmCallBack callback) {
        if (!(DmShared.xNull(param.get("grdPartyCode")).equalsIgnoreCase("") && DmShared.xNull(param.get("grdsNo")).equalsIgnoreCase(""))) {
            String code = (DmShared.xNull(param.get("grdPartyCode")) + "-" + DmShared.xNull(param.get("grdsNo")));
            topControlMoveRec(code);
        } else {
            topControl.btnClick("New");
            if (param != null) {
                txtHAccount.setTag(DmShared.xNull(param.get("partyCode")));
                txtHAccount.setTag2nd(DmShared.xNull(param.get("partyCode")));
                txtHAccount.setText(DmShared.xNull(param.get("partyName")));
                txtHCity.setTag(DmShared.xNull(param.get("cityCode")));
                txtHCity.setTag2nd(DmShared.xNull(param.get("cityCode")));
                txtHCity.setText(DmShared.xNull(param.get("cityName")));
                txtState.setTag(DmShared.xNull(param.get("stateCode")));
                txtState.setText(DmShared.xNull(param.get("stateName")));
            }
        }
        if (callback != null) {
            DmResult<Object> dmrTemp = new DmResult<>();
            dmrTemp.setStatus(true);
            callback.result(dmrTemp);
        }

    }

    private void resetCity() {
        txtHCity.setTag2nd("");
        txtState.setText("");
        txtState.setTag("");
        txtState.setTag2nd("");
        txtCountry.setText("");
        txtCountry.setTag("");
        txtCountry.setTag2nd("");

    }

    private void resetAc() {
        txtHAccount.setTag2nd("");

        txtHCity.setTag("");
        txtHCity.setText("");
        resetCity();
    }
}
