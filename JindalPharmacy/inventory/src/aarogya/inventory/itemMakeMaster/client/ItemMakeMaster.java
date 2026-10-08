package aarogya.inventory.itemMakeMaster.client;

import aarogya.inventory.itemMakeMaster.shared.*;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import java.util.List;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.VoucherPermissionHandler;

public final class ItemMakeMaster extends ItemMakeMaster_Dsg implements KeyPressHandler, BlurHandler {

    ServiceInvoker<ItemMakeMaster_SrvAsync> SrvAsync;
int entryType=0;
    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);

        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                clear();
                txtCompanyName.setFocus(true);
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                txtCompanyName.setFocus(true);
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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(ItemMakeMaster.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void textHelpEvents() {
        txtCompanyName.addCallBack(new DmSuggestBox.CallBack() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getSuggestionCompanyName(helper, blnToEndOfFile,entryType, new AsyncCallback<DmResultPageing<List<String>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtCompanyName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<String>> result) {
                        if (result.isStatus()) {
                            txtCompanyName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtCompanyName).show();
                        }
                    }
                });
            }
        });
        txtShortName.addCallBack(new DmSuggestBox.CallBack() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getSuggestionShortName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<String>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtShortName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<String>> result) {
                        if (result.isStatus()) {
                            txtShortName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtShortName).show();
                        }
                    }
                });
            }
        });

    }

    private void findEvents() {
        dmFind.setTitle(this.asWidget().getTitle());

        dmFind.addHelpGridCallBack(new DmFind.FindCallBack<Find_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getFind(helper, blnToEndOfFile,entryType, new AsyncCallback<DmResultPageing<List<Find_Mdl>>>() {

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

    public ItemMakeMaster(UserMenu menuPermission, SetWidgetInvoker swi, int entryType) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        SrvAsync = new ServiceInvoker<>(GWT.create(ItemMakeMaster_Srv.class), ItemMakeMaster.class.getName());
        this.entryType=entryType;
        if (STEnum.ITEM_MAKE_ENTRY_TYPE.MAKE.getId()==entryType){
            lblEntrytype.setText("Make Name");
            this.asWidget().setTitle("Item Make Master");
        }else if(STEnum.ITEM_MAKE_ENTRY_TYPE.MARKETING.getId()==entryType){
            lblEntrytype.setText("Company Name");
            this.asWidget().setTitle("Marketing Company Master");
        }
        
        topControlEvents(menuPermission);
        handlerEvents();
        textHelpEvents();
        findEvents();
        
        swi.setWidget(this);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private ItemMakeMaster_Mdl topControlSaveInitialize() {
        ItemMakeMaster_Mdl mrTemp = new ItemMakeMaster_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }
        mrTemp.setEntryType(DmShared.iNull(entryType).toString());
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtCompanyName.getText()));
        mrTemp.setShortName(DmShared.xNull(txtShortName.getText()));
        mrTemp.setLclPerson(DmShared.xNull(txtLclPerson.getText()));
        mrTemp.setLclAddress(DmShared.xNull(txtLclAddress.getText()));
        mrTemp.setLclAddress1(DmShared.xNull(txtLclAddress1.getText()));
        mrTemp.setLclMobile(DmShared.xNull(txtLclMobile.getText()));
        mrTemp.setLclFax(DmShared.xNull(txtLclFax.getText()));
        mrTemp.setLclPhoneR(DmShared.xNull(txtLclPhoneR.getText()));
        mrTemp.setLclPhoneO(DmShared.xNull(txtLclPhoneO.getText()));
        mrTemp.setLclEmail(DmShared.xNull(txtLclEmail.getText()));
        mrTemp.setRegPerson(DmShared.xNull(txtRegPerson.getText()));
        mrTemp.setRegAddress(DmShared.xNull(txtRegAddress.getText()));
        mrTemp.setRegAddress1(DmShared.xNull(txtRegAddress1.getText()));
        mrTemp.setRegMobile(DmShared.xNull(txtRegMobile.getText()));
        mrTemp.setRegFax(DmShared.xNull(txtRegFax.getText()));
        mrTemp.setRegPhoneR(DmShared.xNull(txtRegPhoneR.getText()));
        mrTemp.setRegPhoneO(DmShared.xNull(txtRegPhoneO.getText()));
        mrTemp.setRegEmail(DmShared.xNull(txtRegEmail.getText()));
        mrTemp.setNatPerson(DmShared.xNull(txtNatPerson.getText()));
        mrTemp.setNatAddress(DmShared.xNull(txtNatAddress.getText()));
        mrTemp.setNatAddress1(DmShared.xNull(txtNatAddress1.getText()));
        mrTemp.setNatMobile(DmShared.xNull(txtNatMobile.getText()));
        mrTemp.setNatFax(DmShared.xNull(txtNatFax.getText()));
        mrTemp.setNatPhoneR(DmShared.xNull(txtNatPhoneR.getText()));
        mrTemp.setNatPhoneO(DmShared.xNull(txtNatPhoneO.getText()));
        mrTemp.setNatEmail(DmShared.xNull(txtNatEmail.getText()));

        return mrTemp;
    }

    private void topControlSave() {
        ItemMakeMaster_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            ItemMakeMaster_Vld.chkSave(mrTemp);

            SrvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtCompanyName).show();
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
                new DmAlertMessageBox(errorMessage, txtCompanyName).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtShortName).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtLclEmail).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtRegEmail).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtNatEmail).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtCompanyName).show();
                break;
        }
    }

    private void topControlCard() {
        SrvAsync.invokeRPC().getNavigator(entryType,new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
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
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<ItemMakeMaster_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<ItemMakeMaster_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<ItemMakeMaster_Mdl> result) {
        ItemMakeMaster_Mdl data;

        if (result.isStatus()) {
            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());

            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());

            txtCompanyName.setText(data.getSharedKeyFieldName());
            txtShortName.setText(data.getShortName());
            txtLclAddress.setText(data.getLclAddress());
            txtLclAddress1.setText(data.getLclAddress1());
            txtLclMobile.setText(data.getLclMobile());
            txtLclPhoneR.setText(data.getLclPhoneR());
            txtLclPhoneO.setText(data.getLclPhoneO());
            txtLclEmail.setText(data.getLclEmail());
            txtLclFax.setText(data.getLclFax());
            txtLclPerson.setText(data.getLclPerson());
            txtRegAddress.setText(data.getRegAddress());
            txtRegAddress1.setText(data.getRegAddress1());
            txtRegMobile.setText(data.getRegMobile());
            txtRegPhoneR.setText(data.getRegPhoneR());
            txtRegPhoneO.setText(data.getRegPhoneO());
            txtRegEmail.setText(data.getRegEmail());
            txtRegFax.setText(data.getRegFax());
            txtRegPerson.setText(data.getRegPerson());
            txtNatAddress.setText(data.getNatAddress());
            txtNatAddress1.setText(data.getNatAddress1());
            txtNatMobile.setText(data.getNatMobile());
            txtNatPhoneR.setText(data.getNatPhoneR());
            txtNatPhoneO.setText(data.getNatPhoneO());
            txtNatEmail.setText(data.getNatEmail());
            txtNatFax.setText(data.getNatFax());
            txtNatPerson.setText(data.getNatPerson());

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
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    private void clear() {
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
    }

    @Override
    public void onKeyPress(KeyPressEvent event) {
        int charCode = event.getCharCode();
        if (event.getSource().equals(txtLclMobile)) {
            if ((charCode > 31) && ((charCode < 48 || charCode > 57) && (charCode != 45 && charCode != 44))) {
                txtLclMobile.cancelKey();
            }
        }
        if (event.getSource().equals(txtLclPhoneO)) {
            if ((charCode > 31) && ((charCode < 48 || charCode > 57) && (charCode != 45 && charCode != 44))) {
                txtLclPhoneO.cancelKey();
            }
        }
        if (event.getSource().equals(txtLclPhoneR)) {
            if ((charCode > 31) && ((charCode < 48 || charCode > 57) && (charCode != 45 && charCode != 44))) {
                txtLclPhoneR.cancelKey();
            }
        }

        if (event.getSource().equals(txtRegMobile)) {
            if ((charCode > 31) && ((charCode < 48 || charCode > 57) && (charCode != 45 && charCode != 44))) {
                txtRegMobile.cancelKey();
            }
        }
        if (event.getSource().equals(txtRegPhoneO)) {
            if ((charCode > 31) && ((charCode < 48 || charCode > 57) && (charCode != 45 && charCode != 44))) {
                txtRegPhoneO.cancelKey();
            }
        }
        if (event.getSource().equals(txtRegPhoneR)) {
            if ((charCode > 31) && ((charCode < 48 || charCode > 57) && (charCode != 45 && charCode != 44))) {
                txtRegPhoneR.cancelKey();
            }
        }

        if (event.getSource().equals(txtNatMobile)) {
            if ((charCode > 31) && ((charCode < 48 || charCode > 57) && (charCode != 45 && charCode != 44))) {
                txtNatMobile.cancelKey();
            }
        }
        if (event.getSource().equals(txtNatPhoneO)) {
            if ((charCode > 31) && ((charCode < 48 || charCode > 57) && (charCode != 45 && charCode != 44))) {
                txtNatPhoneO.cancelKey();
            }
        }
        if (event.getSource().equals(txtNatPhoneR)) {
            if ((charCode > 31) && ((charCode < 48 || charCode > 57) && (charCode != 45 && charCode != 44))) {
                txtNatPhoneR.cancelKey();
            }
        }
    }

    @Override
    public void onBlur(BlurEvent event) {

    }

    private void handlerEvents() {
        txtLclMobile.addBlurHandler(this);
        txtLclMobile.addKeyPressHandler(this);
        txtLclPhoneO.addBlurHandler(this);
        txtLclPhoneO.addKeyPressHandler(this);
        txtLclPhoneR.addBlurHandler(this);
        txtLclPhoneR.addKeyPressHandler(this);
        txtRegMobile.addBlurHandler(this);
        txtRegMobile.addKeyPressHandler(this);
        txtRegPhoneO.addBlurHandler(this);
        txtRegPhoneO.addKeyPressHandler(this);
        txtRegPhoneR.addBlurHandler(this);
        txtRegPhoneR.addKeyPressHandler(this);
        txtNatMobile.addBlurHandler(this);
        txtNatMobile.addKeyPressHandler(this);
        txtNatPhoneO.addBlurHandler(this);
        txtNatPhoneO.addKeyPressHandler(this);
        txtNatPhoneR.addBlurHandler(this);
        txtNatPhoneR.addKeyPressHandler(this);
    }
}
