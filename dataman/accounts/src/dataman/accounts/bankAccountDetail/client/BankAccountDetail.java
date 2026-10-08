package dataman.accounts.bankAccountDetail.client;

import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.accounts.bankAccountDetail.shared.*;
import dataman.dmToolkit.dmFind.client.DmFind;
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

public final class BankAccountDetail extends BankAccountDetail_Dsg {

    ServiceInvoker<BankAccountDetail_SrvAsync> SrvAsync;
    private String accountCode = "", accountName = "";

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());

        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);

        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                clear();
                txtHAccount.setFocus(true);
                txtHDefault.setEnabled(true);

            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                if (!DmShared.xNull(topControl.getDocId()).equalsIgnoreCase("")) {
                    txtAccountNo.setFocus(true);
                    txtHDefault.setEnabled(true);
                    txtHAccount.setEnabled(false);

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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(BankAccountDetail.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void textHelpEvents() {
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

    public BankAccountDetail(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        SrvAsync = new ServiceInvoker<>(GWT.create(BankAccountDetail_Srv.class), BankAccountDetail.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        textHelpEvents();
        findEvents();
        txtHDefault.setEnabled(false);
        swi.setWidget(this);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private BankAccountDetail_Mdl topControlSaveInitialize() {
        BankAccountDetail_Mdl mrTemp = new BankAccountDetail_Mdl();

        if (topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(DmShared.xNull(txtHAccount.getTag()));

        } else {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
            mrTemp.setsNo(txtHAccount.getTag2nd());
        }
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtHAccount.getText()));
        mrTemp.setAccountNo(DmShared.xNull(txtAccountNo.getText()));
        mrTemp.setBankName(DmShared.xNull(txtBankName.getText()));
        mrTemp.setBankBranch(DmShared.xNull(txtBankBranch.getText()));
        mrTemp.setBankBranchAddress(DmShared.xNull(txtBankBranchAddress.getText()));
        mrTemp.setSwiftCode(DmShared.xNull(txtSwiftCode.getText()));
        mrTemp.setIFSCCode(DmShared.xNull(txtIFSCCode.getText()));
        mrTemp.setDefaultTag(DmShared.xNull(txtHDefault.getTag()));

        return mrTemp;
    }

    private void topControlSave() {
        BankAccountDetail_Mdl mrTemp;
        try {
            mrTemp = topControlSaveInitialize();
            BankAccountDetail_Vld.chkSave(mrTemp);

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
                            if (!DmShared.xNull(accountCode).equalsIgnoreCase("")) {
                                txtHAccount.setTag(DmShared.xNull(accountCode));
                                txtHAccount.setText(DmShared.xNull(accountName));
                            }
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
                new DmAlertMessageBox(errorMessage, txtHAccount).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtAccountNo).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtBankName).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtBankBranch).show();
            case 500:
                new DmAlertMessageBox(errorMessage, txtHDefault).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHAccount).show();
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
            SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<BankAccountDetail_Mdl>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<BankAccountDetail_Mdl> result) {
                    topControlMoveRecSuccess(result);
                }
            });
        } catch (Exception e) {
        }
    }

    private void topControlMoveRecSuccess(DmResult<BankAccountDetail_Mdl> result) {
        BankAccountDetail_Mdl data;

        if (result.isStatus()) {
            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode() + "-" + data.getsNo());
            txtHDefault.setEnabled(false);

            txtHAccount.setTag(data.getSharedKeyFieldCode());
            txtHAccount.setText(data.getSharedKeyFieldName());
            txtHAccount.setTag2nd(data.getsNo());
            txtAccountNo.setText(data.getAccountNo());
            txtBankName.setText(data.getBankName());
            txtBankBranch.setText(data.getBankBranch());
            txtBankBranchAddress.setText(data.getBankBranchAddress());
            txtSwiftCode.setText(data.getSwiftCode());
            txtIFSCCode.setText(data.getIFSCCode());

            txtHDefault.setTag(data.getDefaultTag());
            txtHDefault.setText(data.getDefaultText());

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

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    private void clear() {
        if (topControl.getMode().equalsIgnoreCase("New")) {
            txtHDefault.setTag("1");
            txtHDefault.setText("Yes");
        }

    }

    @Override
    public void action(String nature, Object object, DmCallBack callback) {

        nature = DmShared.xNull(nature).trim().toUpperCase();
        try {
            switch (nature) {
                case "BANK_DETAIL":
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

    //------ << FORM IS OPENING FROM ACCOUNT MASTER,EMPLOYEE MASTER ,DIVISION MASTER,MANUFACTURER MASTER,SUPPLIER MASTER>>-----
    public void openWith(HashMap<String, String> param, DmCallBack callback) {
        if (!(DmShared.xNull(param.get("grdAccountCode")).equalsIgnoreCase("") && DmShared.xNull(param.get("grdsNo")).equalsIgnoreCase(""))) {
            String code = (DmShared.xNull(param.get("grdAccountCode")) + "-" + DmShared.xNull(param.get("grdsNo")));
            topControlMoveRec(code);
        } else {
            topControl.btnClick("New");

            if (param != null) {
                txtHAccount.setText(DmShared.xNull(param.get("accountName")));
                txtHAccount.setTag(DmShared.xNull(param.get("accountCode")));
                accountCode = DmShared.xNull(param.get("accountCode"));
                accountName = DmShared.xNull(param.get("accountName"));

            }
        }
        if (callback != null) {
            DmResult<Object> dmrTemp = new DmResult<>();
            dmrTemp.setStatus(true);
            callback.result(dmrTemp);
        }

    }
}
