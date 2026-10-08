package aarogya.pharmacy.medicineMakeMaster.client;

import dataman.dmBase.self.shared.*;
import aarogya.pharmacy.medicineMakeMaster.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.menu.Item;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import java.util.List;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import java.util.HashMap;

public final class MedicineMakeMaster extends MedicineMakeMaster_Dsg implements KeyPressHandler, BlurHandler, IsWidget, SelectionHandler<Item> {

    ServiceInvoker<MedicineMakeMaster_SrvAsync> SrvAsync;

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());

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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(MedicineMakeMaster.this);
            }
        });

        topControl.addOtherHandeller(new DmTopControl.eOther() {

            @Override
            public void otherClicked() {
                menu.show(topControl.getButton("Other"));
            }
        });

        topControl.setDisplay(true);
    }

    private void textHelpEvents() {
        txtCompanyName.addCallBack(new DmSuggestBox.CallBack() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getSuggestionCompanyName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<String>>>() {

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

    public MedicineMakeMaster(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        SrvAsync = new ServiceInvoker<>(GWT.create(MedicineMakeMaster_Srv.class), MedicineMakeMaster.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        mnuExportCard.addSelectionHandler(this);
        textHelpEvents();
        handlerEvents();
        findEvents();
        swi.setWidget(this);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private MedicineMakeMaster_Mdl topControlSaveInitialize() {
        MedicineMakeMaster_Mdl mrTemp = new MedicineMakeMaster_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }
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
        mrTemp.setPurchaseDisPer(DmClient.vNull(txtPurchaseDisPer.getText(), "0.00"));

        return mrTemp;
    }

    private void topControlSave() {
        MedicineMakeMaster_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            MedicineMakeMaster_Vld.chkSave(mrTemp);

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
            case 600:
                new DmAlertMessageBox(errorMessage, txtPurchaseDisPer).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtCompanyName).show();
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
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<MedicineMakeMaster_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<MedicineMakeMaster_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<MedicineMakeMaster_Mdl> result) {
        MedicineMakeMaster_Mdl data;

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
            txtPurchaseDisPer.setText(DmClient.vNull(data.getPurchaseDisPer(), "0.00"));

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
        if (event.getSource().equals(txtLclMobile)) {
            if (DmShared.xNull(txtLclMobile.getText()).equalsIgnoreCase("")) {
                txtLclMobile.setText(txtLclMobile.getText());
            }
        } else if (event.getSource().equals(txtLclPhoneO)) {
            if (DmShared.xNull(txtLclPhoneO.getText()).equalsIgnoreCase("")) {
                txtLclPhoneO.setText(txtLclPhoneO.getText());
            }
        } else if (event.getSource().equals(txtLclPhoneR)) {
            if (DmShared.xNull(txtLclPhoneR.getText()).equalsIgnoreCase("")) {
                txtLclPhoneR.setText(txtLclPhoneR.getText());
            }
        } else if (event.getSource().equals(txtRegMobile)) {
            if (DmShared.xNull(txtRegMobile.getText()).equalsIgnoreCase("")) {
                txtRegMobile.setText(txtRegMobile.getText());
            }
        } else if (event.getSource().equals(txtRegPhoneO)) {
            if (DmShared.xNull(txtRegPhoneO.getText()).equalsIgnoreCase("")) {
                txtRegPhoneO.setText(txtRegPhoneO.getText());
            }
        } else if (event.getSource().equals(txtRegPhoneR)) {
            if (DmShared.xNull(txtRegPhoneR.getText()).equalsIgnoreCase("")) {
                txtRegPhoneR.setText(txtRegPhoneR.getText());
            }
        } else if (event.getSource().equals(txtNatMobile)) {
            if (DmShared.xNull(txtNatMobile.getText()).equalsIgnoreCase("")) {
                txtNatMobile.setText(txtNatMobile.getText());
            }
        } else if (event.getSource().equals(txtNatPhoneO)) {
            if (DmShared.xNull(txtNatPhoneO.getText()).equalsIgnoreCase("")) {
                txtNatPhoneO.setText(txtNatPhoneO.getText());
            }
        } else if (event.getSource().equals(txtNatPhoneR)) {
            if (DmShared.xNull(txtNatPhoneR.getText()).equalsIgnoreCase("")) {
                txtNatPhoneR.setText(txtNatPhoneR.getText());
            }
        } else if ((event.getSource() == txtPurchaseDisPer)) {
            if (!DmShared.xNull(txtPurchaseDisPer.getText()).equalsIgnoreCase("")) {
                txtPurchaseDisPer.setText(DmClient.vNull(txtPurchaseDisPer.getText(), "0.00"));
            }
        }
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
        txtPurchaseDisPer.addBlurHandler(this);
    }

    private void clear() {
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
    }

    @Override
    public void onSelection(SelectionEvent<Item> event) {
        if (event.getSource().equals(mnuExportCard)) {
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

    private void exportCSV() {
        SrvAsync.invokeRPC().exportCSV(DmRequestBuilder.getSessionId(), new AsyncCallback<DmResult<KeyFieldExtended>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    KeyFieldExtended field = result.getData();
                    startDownloading(DmShared.xNull(field.getSharedKeyFieldName()), DmShared.xNull(field.getSharedKeyFieldCode()));
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
