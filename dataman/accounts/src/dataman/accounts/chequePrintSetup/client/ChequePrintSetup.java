package dataman.accounts.chequePrintSetup.client;

import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import java.util.List;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.menu.Item;
import dataman.accounts.chequePrintSetup.shared.BankName_Mdl;
import dataman.accounts.chequePrintSetup.shared.ChequePrintSetup_Mdl;
import dataman.accounts.chequePrintSetup.shared.ChequePrintSetup_Srv;
import dataman.accounts.chequePrintSetup.shared.ChequePrintSetup_SrvAsync;
import dataman.accounts.chequePrintSetup.shared.ChequePrintSetup_Vld;
import dataman.accounts.chequePrintSetup.shared.Find_Mdl;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public class ChequePrintSetup extends ChequePrintSetup_Dsg implements SelectionHandler<Item>, BlurHandler {

    ServiceInvoker<ChequePrintSetup_SrvAsync> SrvAsync;

    CopyPopup copyPopup = new CopyPopup();
    String bankCode = "";

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setOtherRequired(true);
        topControl.setRefreshRequired(false);

        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                clear();
                txtHBankName.setFocus(true);
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                txtHBankName.setFocus(true);
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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(ChequePrintSetup.this);
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

        copyPopup.addCallBack(new CopyPopup.CallBack() {
            @Override
            public void onOk(ChequePrintSetup_Mdl data) {
                bankCode = data.getBankCode();
                txtChequeDateTop.setText(data.getChequeDateTop());
                txtChequeDateLeft.setText(data.getChequeDateLeft());
                txtFavourOfTop.setText(data.getFavourOfTop());
                txtFavourOfLeft.setText(data.getFavourOfLeft());
                txtAmtInWordsTop.setText(data.getAmtInWordsTop());
                txtAmtInWordsLeft.setText(data.getAmtInWordsLeft());
                txtAmountTop.setText(data.getAmtTop());
                txtAmountLeft.setText(data.getAmtLeft());
                txtCrossChequeTop.setText(data.getCrossChequeTop());
                txtCrossChequeLeft.setText(data.getCrossChequeLeft());
                txtAuthSign.setText(data.getAuthSign());
                txtAuthSignTop.setText(data.getAuthSignTop());
                txtAuthSignLeft.setText(data.getAuthSignLeft());
                txtCompanyName.setText(data.getCompanyName());
                txtCompanyNameTop.setText(data.getCompanyNameTop());
                txtCompanyNameLeft.setText(data.getCompanyNameLeft());
                txtPaperWidth.setText(data.getPaperWidth());
                txtPaperHeight.setText(data.getPaperHeight());

                txtPaperWidthMM.setText("");
                txtPaperHeightMM.setText("");

            }
        });

        txtHBankName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHBankName(topControl.getMode().equalsIgnoreCase("New") ? bankCode : "", false, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<BankName_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHBankName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<BankName_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHBankName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHBankName).show();
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

    public ChequePrintSetup(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        SrvAsync = new ServiceInvoker<>(GWT.create(ChequePrintSetup_Srv.class), ChequePrintSetup.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        textHelpEvents();
        handlerEvents();
        findEvents();
        swi.setWidget(this);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private ChequePrintSetup_Mdl topControlSaveInitialize() {
        ChequePrintSetup_Mdl mrTemp = new ChequePrintSetup_Mdl();
        calculatePprWidthHeightPx();
        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }
        mrTemp.setBankCode(DmShared.xNull(txtHBankName.getTag()));
        mrTemp.setChequeDateTop(DmClient.vNull(txtChequeDateTop.getText(), "0.00"));
        mrTemp.setChequeDateLeft(DmClient.vNull(txtChequeDateLeft.getText(), "0.00"));
        mrTemp.setFavourOfTop(DmClient.vNull(txtFavourOfTop.getText(), "0.00"));
        mrTemp.setFavourOfLeft(DmClient.vNull(txtFavourOfLeft.getText(), "0.00"));
        mrTemp.setAmtInWordsTop(DmClient.vNull(txtAmtInWordsTop.getText(), "0.00"));
        mrTemp.setAmtInWordsLeft(DmClient.vNull(txtAmtInWordsLeft.getText(), "0.00"));
        mrTemp.setAmtTop(DmClient.vNull(txtAmountTop.getText(), "0.00"));
        mrTemp.setAmtLeft(DmClient.vNull(txtAmountLeft.getText(), "0.00"));
        mrTemp.setCrossChequeTop(DmClient.vNull(txtCrossChequeTop.getText(), "0.00"));
        mrTemp.setCrossChequeLeft(DmClient.vNull(txtCrossChequeLeft.getText(), "0.00"));
        mrTemp.setAuthSign(DmShared.xNull(txtAuthSign.getText()));
        mrTemp.setAuthSignTop(DmClient.vNull(txtAuthSignTop.getText(), "0.00"));
        mrTemp.setAuthSignLeft(DmClient.vNull(txtAuthSignLeft.getText(), "0.00"));
        mrTemp.setCompanyName(DmShared.xNull(txtCompanyName.getText()));
        mrTemp.setCompanyNameTop(DmClient.vNull(txtCompanyNameTop.getText(), "0.00"));
        mrTemp.setCompanyNameLeft(DmClient.vNull(txtCompanyNameLeft.getText(), "0.00"));
        mrTemp.setPaperWidth(DmClient.vNull(txtPaperWidth.getText(), "0.00"));
        mrTemp.setPaperHeight(DmClient.vNull(txtPaperHeight.getText(), "0.00"));

        return mrTemp;
    }

    private void topControlSave() {
        ChequePrintSetup_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            ChequePrintSetup_Vld.chkSave(mrTemp);

            SrvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHBankName).show();
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
                new DmAlertMessageBox(errorMessage, txtHBankName).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHBankName).show();
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
        SrvAsync.invokeRPC().getMoveRec(strKey, "", false, new AsyncCallback<DmResult<ChequePrintSetup_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<ChequePrintSetup_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<ChequePrintSetup_Mdl> result) {
        ChequePrintSetup_Mdl data;

        if (result.isStatus()) {

            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());
            txtHBankName.setText(data.getBankName());
            txtHBankName.setTag(data.getBankCode());
            txtChequeDateTop.setText(data.getChequeDateTop());
            txtChequeDateLeft.setText(data.getChequeDateLeft());
            txtFavourOfTop.setText(data.getFavourOfTop());
            txtFavourOfLeft.setText(data.getFavourOfLeft());
            txtAmtInWordsTop.setText(data.getAmtInWordsTop());
            txtAmtInWordsLeft.setText(data.getAmtInWordsLeft());
            txtAmountTop.setText(data.getAmtTop());
            txtAmountLeft.setText(data.getAmtLeft());
            txtCrossChequeTop.setText(data.getCrossChequeTop());
            txtCrossChequeLeft.setText(data.getCrossChequeLeft());
            txtAuthSign.setText(data.getAuthSign());
            txtAuthSignTop.setText(data.getAuthSignTop());
            txtAuthSignLeft.setText(data.getAuthSignLeft());
            txtCompanyName.setText(data.getCompanyName());
            txtCompanyNameTop.setText(data.getCompanyNameTop());
            txtCompanyNameLeft.setText(data.getCompanyNameLeft());
            txtPaperWidth.setText(data.getPaperWidth());
            txtPaperHeight.setText(data.getPaperHeight());
            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());

        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);

        txtPaperHeightMM.setEnabled(false);
        txtPaperWidthMM.setEnabled(false);
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

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    private void clear() {
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        bankCode = "";
    }

    private void handlerEvents() {
        mnuCopy.addSelectionHandler(this);
        txtPaperHeightMM.addBlurHandler(this);
        txtPaperWidthMM.addBlurHandler(this);
    }

    @Override
    public void onSelection(SelectionEvent<Item> event) {
        if (event.getSource().equals(mnuCopy)) {
            if (topControl.getMode().equalsIgnoreCase("New")) {
                copyPopup.show();
            } else {
                new DmAlertMessageBox("Can be use in New Mode only", txtHBankName).show();
            }
        }
    }

    private void calculatePprWidthHeightPx() {
        double px = 3.7795275;
        if (DmShared.iNull(txtPaperWidthMM.getText()).intValue() > 0) {
            txtPaperWidth.setText(DmShared.iNull(Math.floor(px * DmShared.iNull(txtPaperWidthMM.getText()).intValue())).toString());
        }
        if (DmShared.iNull(txtPaperHeightMM.getText()).intValue() > 0) {
            txtPaperHeight.setText(DmShared.iNull(Math.floor(px * DmShared.iNull(txtPaperHeightMM.getText()).intValue())).toString());
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtPaperHeightMM) || event.getSource().equals(txtPaperWidthMM)) {
            calculatePprWidthHeightPx();
        }
    }
}
