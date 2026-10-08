package aarogya.master.cardReceive.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.master.cardReceive.shared.*;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmHelpGridMulti.client.DmHelpGridMulti;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public class CardReceive extends CardReceive_Dsg implements BlurHandler {

    private final ServiceInvoker<CardReceive_SrvAsync> srvAsync;

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setRefreshRequired(false);
        topControl.setOtherRequired(false);

        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                txtSerialNo.setFocus(true);
                txtReceiveDate.setText(DmClient.getLoginDate());
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                txtSerialNo.setFocus(true);
            }
        });

        topControl.addDeleteHandeller(new DmTopControl.eDelete() {
            @Override
            public void deleteClicked() {
                grdDelete.getDmGrid().getGrid().getStore().clear();
                grdDelete.show(txtCardNoFrom, "", "", "");
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
                lblTotalNoofCard.setText("");
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(CardReceive.this);
            }
        });

        topControl.setDisplay(true);
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
                    topControlMoveRec(tRow.getSharedKeyFieldCode());
                }
            }
        });
    }

    private void textHelpEvents() {
        grdDelete.addHelpGridMultiCallBack(new DmHelpGridMulti.HelpGridMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).fillDeleteGrd(helper, blnToEndOfFile, txtReceiveDate.getText(), new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            grdDelete.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, final String keyCode, String keyName) {
                if (blnStatus) {
                    new DmConfirmMessageBox("Are you sure? You want to delete this record.", new DialogHideEvent.DialogHideHandler() {
                        @Override
                        public void onDialogHide(DialogHideEvent event) {
                            if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                                topControlDelete(keyCode);
                            }
                        }
                    }).show();
                }
            }
        });
        grdGenerateCards.addCallBack(new DmGridPaging.CallBack<String>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, String param) {
                srvAsync.invokeRPC(blnToEndOfFile).getReceiveCards(helper, blnToEndOfFile, param, new AsyncCallback<DmResultPageing<List<GrdCardReceive_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtSerialNo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdCardReceive_Mdl>> result) {
                        if (result.isStatus()) {
                            grdGenerateCards.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtSerialNo).show();
                        }
                    }
                });
            }

            @Override
            public String getParam() {
                return txtReceiveDate.getText();
            }
        });
    }

    @Override
    public void onBlur(BlurEvent event) {
        int noOfCard = 0;
        if (event.getSource().equals(txtReceiveDate)) {
            txtReceiveDate.setText(DmClient.returnDate(txtReceiveDate.getText()));
        } else if (event.getSource().equals(txtCardNoFrom) || event.getSource().equals(txtCardNoTo)) {
            displayLabel();
        }
    }

    private void displayLabel() {
        int noOfCard = 0;
        noOfCard = DmShared.iNull(txtCardNoTo.getText()).intValue() - DmShared.iNull(txtCardNoFrom.getText()).intValue();
        noOfCard = noOfCard >= 0 ? (noOfCard + 1) : noOfCard;
        lblTotalNoofCard.setText("Total " + DmShared.iNull(noOfCard));
    }

    public CardReceive(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        srvAsync = new ServiceInvoker<CardReceive_SrvAsync>(GWT.create(CardReceive_Srv.class), CardReceive.class.getName());
        this.asWidget().setTitle(widgetName);
        txtReceiveDate.setText(DmClient.getLoginDate());
        txtReceiveDate.addBlurHandler(this);
        txtCardNoTo.addBlurHandler(this);
        txtCardNoFrom.addBlurHandler(this);
        topControlEvents(menuPermission);
        findEvents();
        textHelpEvents();
        swi.setWidget(this);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
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
                    topControlCard();
                } else {
                    new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                }
            }
        });
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

    private void topControlMoveRec(String strKey) {
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<CardReceive_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<CardReceive_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<CardReceive_Mdl> result) {
        CardReceive_Mdl data;
        if (result.isStatus()) {
            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());
            txtReceiveDate.setText(data.getSharedKeyFieldCode());
            txtSerialNo.setText(data.getSharedKeyFieldName());
            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());
            grdGenerateCards.reQuery();
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
    }

    private void topControlSave() {
        CardReceive_Mdl mrTemp;
        try {
            mrTemp = topControlSaveInitialize();
            CardReceive_Vld.chkSave(mrTemp);
            srvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtSerialNo).show();
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
                new DmAlertMessageBox(errorMessage, txtReceiveDate).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtSerialNo).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtCardNoFrom).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtCardNoTo).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtSerialNo).show();
                break;
        }

        new DmAlertMessageBox(errorMessage, (DmTextBox) null).show();
    }

    private CardReceive_Mdl topControlSaveInitialize() {
        CardReceive_Mdl mrTemp = new CardReceive_Mdl();
        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }
        mrTemp.setSharedKeyFieldCode(DmShared.dNull(txtReceiveDate.getText()));
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtSerialNo.getText()));
        mrTemp.setCounterFormat(DmShared.xNull(txtCounterFormat.getText()));
        mrTemp.setCardNoTo(DmShared.iNull(txtCardNoTo.getText()).intValue());
        mrTemp.setCardNoFrom(DmShared.iNull(txtCardNoFrom.getText()).intValue());
        return mrTemp;
    }

    @Override
    public void searchRecord(String key) {
    }

}
