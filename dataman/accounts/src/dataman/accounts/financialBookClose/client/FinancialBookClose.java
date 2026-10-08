package dataman.accounts.financialBookClose.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.accounts.financialBookClose.shared.DefaultValue_Mdl;
import dataman.accounts.financialBookClose.shared.FinancialBookClose_Mdl;
import dataman.accounts.financialBookClose.shared.FinancialBookClose_Srv;
import dataman.accounts.financialBookClose.shared.FinancialBookClose_SrvAsync;
import dataman.accounts.financialBookClose.shared.FinancialBookClose_Vld;
import dataman.accounts.financialBookClose.shared.Find_Mdl;
import dataman.accounts.financialBookClose.shared.TxtHAccountName_Mdl;
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
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.ledgerPostingDisplayPopup.client.LedgerPostingDisplayPopup;
import java.util.List;

public class FinancialBookClose extends FinancialBookClose_Dsg implements IsWidget {

    private final ServiceInvoker<FinancialBookClose_SrvAsync> SrvAsync;
    protected DefaultValue_Mdl defaultValue = new DefaultValue_Mdl();
    private final LedgerPostingDisplayPopup ledgerPostingDisplayPopup = new LedgerPostingDisplayPopup();
    protected boolean mnuLedgerPostingDisplay = false;

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());

        topControl.setRefreshRequired(false);
        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                clear();
                onAdd();
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                manageDisplay();
                txtHAccountName.setFocus(true);
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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(FinancialBookClose.this);
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

    private void onAdd() {
        txtHVoucherType.setTag(DmShared.xNull(defaultValue.getSharedKeyFieldCode()));
        txtHVoucherType.setText(DmShared.xNull(defaultValue.getSharedKeyFieldName()));
        txtVoucherDate.setText(DmShared.dNull(defaultValue.getVoucherDate()));
        manageDisplay();
        txtHAccountName.setFocus(true);
    }

    private void textHelpEvents(UserMenu menuPermission) {

        for (String controlPermission : menuPermission.getControlPermission()) {
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(mnuLedgerPostingDisplayBlog)) {
                mnuLedgerPostingDisplay = true;
            }
        }

        txtHVoucherType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHVoucherType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHVoucherType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHVoucherType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHVoucherType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHAccountName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHAccountName_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAccountName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHAccountName_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAccountName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHAccountName_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHAccountName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHAccountName).show();
                        }

                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHAccountName_Mdl tRow) {
                if (blnStatus) {
                    txtHAccountName.setTag2nd(tRow.getAccountNameAccDesc());
                }
            }
        });
    }

    private void findEvents() {
        dmFind.setTitle(this.asWidget().getTitle());

        dmFind.addHelpGridCallBack(new DmFind.FindCallBack<Find_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getFind(topControl.getPermissions(), helper, blnToEndOfFile, DmClient.getVoucherPrefix(), new AsyncCallback<DmResultPageing<List<Find_Mdl>>>() {

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

    public FinancialBookClose(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        SrvAsync = new ServiceInvoker<FinancialBookClose_SrvAsync>(GWT.create(FinancialBookClose_Srv.class), FinancialBookClose.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        textHelpEvents(menuPermission);
        manageDisplay();
        findEvents();
        iniDefaultValues(swi);
    }

    private void iniDefaultValues(final SetWidgetInvoker swi) {
        SrvAsync.invokeRPC(true).getDefaultValues(new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    defaultValue = result.getData();
                    swi.setWidget(FinancialBookClose.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void manageDisplay() {
        txtVoucherNo.setEnabled(false);
        txtVoucherDate.setEnabled(false);
        txtHVoucherType.setEnabled(false);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private FinancialBookClose_Mdl topControlSaveInitialize() {
        FinancialBookClose_Mdl mrTemp = new FinancialBookClose_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
            mrTemp.getObjRecId().setRecIdValue(DmShared.xNull(txtVoucherNo.getText()));
            mrTemp.getObjRecId().setCounter(DmShared.iNull(txtVoucherNo.getTag()).longValue());
            mrTemp.getObjRecId().setPrefix(DmShared.xNull(txtVoucherNo.getTag2nd()));
        }
        mrTemp.setVoucherTypeCode(DmShared.xNull(txtHVoucherType.getTag()));
        mrTemp.setVoucherTypeName(DmShared.xNull(txtHVoucherType.getText()));
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtVoucherNo.getText()));
        mrTemp.setvPrefix(DmClient.getVoucherPrefix());
        mrTemp.setVoucherDate(DmShared.dNull(txtVoucherDate.getText()));
        mrTemp.setContraText(DmShared.xNull(txtHAccountName.getText()));
        mrTemp.setContraCode(DmShared.xNull(txtHAccountName.getTag()));
        mrTemp.setContraTextAccDesc(DmShared.xNull(txtHAccountName.getTag2nd()));

        return mrTemp;
    }

    private void topControlSave() {
        FinancialBookClose_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            FinancialBookClose_Vld.chkSave(mrTemp);

            SrvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHAccountName).show();
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
                new DmAlertMessageBox(errorMessage, txtHAccountName).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHAccountName).show();
                break;
        }

    }

    private void topControlCard() {
        SrvAsync.invokeRPC().getNavigator(topControl.getPermissions(), DmClient.getVoucherPrefix(), new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
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
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<FinancialBookClose_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<FinancialBookClose_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<FinancialBookClose_Mdl> result) {
        FinancialBookClose_Mdl data;

        if (result.isStatus()) {
            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());
            txtVoucherDate.setText(DmShared.dNull(data.getVoucherDate()));
            txtVoucherNo.setText(DmShared.xNull(data.getSharedKeyFieldName()));
            txtVoucherNo.setTag(DmShared.iNull(data.getObjRecId().getCounter()).toString());
            txtVoucherNo.setTag2nd(DmShared.xNull(data.getObjRecId().getPrefix()));
            txtHVoucherType.setText(DmShared.xNull(data.getVoucherTypeName()));
            txtHVoucherType.setTag(DmShared.xNull(data.getVoucherTypeCode()));
            txtHAccountName.setText(DmShared.xNull(data.getContraText()));
            txtHAccountName.setTag(DmShared.xNull(data.getContraCode()));
            txtHAccountName.setTag2nd(DmShared.xNull(data.getContraTextAccDesc()));

            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());

        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
        manageDisplay();
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
    }

    private void showLedgerDisplayPopup() {
        if (DmShared.xNull(topControl.getMode()).equalsIgnoreCase("Browse")) {
            ledgerPostingDisplayPopup.show(DmShared.xNull(topControl.getDocId()));
        } else {
            new DmAlertMessageBox("Can be use in Browse Mode only and select a record.", txtHVoucherType).show();
        }
    }

//    @Override
//    public void onSelection(SelectionEvent<Item> event) {
//        if (event.getSource().equals(mnuLedgerPostingDisplay)) {
//            if (topControl.getMode().equalsIgnoreCase("Browse")
//                    && DmShared.iNull(topControl.getDocId()).longValue() > 0) {
//                showLedgerDisplayPopup();
//            } else {
//                new DmAlertMessageBox("Can be use in Browse Mode only.", txtHVoucherType).show();
//            }
//        }
//    }
    @Override
    void menuClick(SelectionEvent<MenuItem> event) {
        if (event.getSource().equals(menuLedgerPostingDisplay)) {
            if (mnuLedgerPostingDisplay && topControl.getMode().equalsIgnoreCase("Browse")
                    && DmShared.iNull(topControl.getDocId()).longValue() > 0) {
                showLedgerDisplayPopup();
            } else {
                new DmAlertMessageBox("Can be use in Browse Mode only.", txtHVoucherType).show();
            }
        }
    }

}
