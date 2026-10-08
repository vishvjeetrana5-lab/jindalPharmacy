package aarogya.master.foreignCurrencyExchange.client;

import aarogya.master.foreignCurrencyExchange.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import java.util.ArrayList;
import java.util.List;

public class ForeignCurrencyExchange extends ForeignCurrencyExchange_Dsg implements BlurHandler {

    private ServiceInvoker<ForeignCurrencyExchange_SrvAsync> SrvAsync;
    private final GridEditForeignCurrencyExchange<GrdForeignCurrencyExchange_Mdl, GrdForeignCurrency_PrpAcs> foreignCurrencyExchangEdit = new GridEditForeignCurrencyExchange(grdForeignCurrency);

    public ForeignCurrencyExchange(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        SrvAsync = new ServiceInvoker<>(GWT.create(ForeignCurrencyExchange_Srv.class), ForeignCurrencyExchange.class.getName());
        this.asWidget().setTitle(widgetName);

        topControlEvents(menuPermission);
        txtExchangeDate.addBlurHandler(this);
        findEvents();
        gridEvents();
        swi.setWidget(this);
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
                if (DmShared.convertDate(txtExchangeDate.getText()).getTime() <= DmShared.convertDate(DmClient.getLoginDate()).getTime()) {
                    new DmAlertMessageBox(" You Cant Edit Because, Exchange Date should be greater than  Current Date.", txtExchangeDate).show();
                    topControl.btnClick("");
                    return;

                }
                txtExchangeDate.setEnabled(false);
                ((CellSelectionModel) grdForeignCurrency.getGrid().getSelectionModel()).selectCell(0, 1);

            }
        });

        topControl.addDeleteHandeller(new DmTopControl.eDelete() {
            @Override
            public void deleteClicked() {
                if (DmShared.convertDate(txtExchangeDate.getText()).getTime() <= DmShared.convertDate(DmClient.getLoginDate()).getTime()) {
                    new DmAlertMessageBox(" You can not delete record Because, Exchange Date should be greater than  Current Date.", txtExchangeDate).show();
                    topControl.btnClick("");
                    return;

                }
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

        topControl.addSaveHandeller(new DmTopControl.eSave() {
            @Override
            public void saveClicked() {
                topControlSave();
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(ForeignCurrencyExchange.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void onAdd() {
        clear();
        fillForeignCurrencyExchangeGrid();
        txtExchangeDate.setFocus(true);
    }

    private void clear() {
        txtExchangeDate.setText("");
        grdForeignCurrency.getGrid().getStore().clear();
        userControl.setTextBlog(modifiedByBlog, "");
    }

    private void findEvents() {
        dmFind.setTitle(this.asWidget().getTitle());

        dmFind.addHelpGridCallBack(new DmFind.FindCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getFind(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), dmFind.getTextBox()).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            dmFind.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), dmFind.getTextBox()).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                flwForm.remove(dmFind);
                flwForm.add(vrtInnerForm);

                if (blnStatus) {
                    topControlMoveRec(tRow.getSharedKeyFieldCode());
                }
            }
        });
    }

    private void gridEvents() {
        grdForeignCurrency.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                try {
                    show(event.getRowIndex());
                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null);
                }
            }
        });
        foreignCurrencyExchangEdit.addCallBack(new GridEditForeignCurrencyExchange.CallBack() {
            @Override
            public void onOk(GrdForeignCurrencyExchange_Mdl foreignCurrencyExchange_Mdl) {

                grdForeignCurrency.commitChanges();
            }

        });
        grdForeignCurrency.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    show(grdForeignCurrency.getGrid().getStore().indexOf(grdForeignCurrency.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
    }

    private void show(int currentRowIndex) {
        if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {
            foreignCurrencyExchangEdit.show(currentRowIndex, false);
        } else {
            foreignCurrencyExchangEdit.show(currentRowIndex, true);
        }
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    private void topControlCard() {
        SrvAsync.invokeRPC().getNavigator(new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtExchangeDate).show();
            }

            @Override
            public void onSuccess(DmResult<List<KeyFieldExtended>> result) {
                if (result.isStatus()) {
                    if (result.isStatus()) {
                        topControl.setNavigator(result.getData());
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), txtExchangeDate).show();
                    }
                }
            }
        });
    }

    private void fillForeignCurrencyExchangeGrid() {
        SrvAsync.invokeRPC().getCurrencyExchangeList(new AsyncCallback<DmResult<List<GrdForeignCurrencyExchange_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdForeignCurrencyExchange_Mdl>> result) {
                List<GrdForeignCurrencyExchange_Mdl> data = new ArrayList<>();
                if (result.isStatus()) {
                    data = result.getData();
                    grdForeignCurrency.getGrid().getStore().clear();
                    grdForeignCurrency.getGrid().getStore().addAll(data);
                    // ((CellSelectionModel) grdForeignCurrency.getGrid().getSelectionModel()).selectCell(0, 0);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtExchangeDate).show();
                }
            }
        });
    }

    private void topControlDelete(String strKey) {
        SrvAsync.invokeRPC().deleteRecord(strKey, new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtExchangeDate).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    topControlCard();
                } else {
                    new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtExchangeDate).show();
                }
            }
        });
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtExchangeDate)) {
            txtExchangeDate.setText(DmClient.returnDate(txtExchangeDate.getText()));
            /*  if (DmShared.dNull(txtExchangeDate.getText()).equalsIgnoreCase("")) {
                onAdd();
            } else {
                fillForeignCurrencyExchangeGrid(DmShared.dNull(txtExchangeDate.getText()));
            }
        }*/
        }
    }

    private void topControlSave() {
        ForeignCurrencyExchange_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            ForeignCurrencyExchange_Vld.chkSave(mrTemp, DmClient.getLoginDate());

            SrvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtExchangeDate).show();
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
                new DmAlertMessageBox(errorMessage, txtExchangeDate).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, new HideEvent.HideHandler() {
                    @Override
                    public void onHide(HideEvent event) {
                        ((CellSelectionModel) grdForeignCurrency.getGrid().getSelectionModel()).selectCell(0, 0);
                    }
                }).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtExchangeDate).show();
                break;
        }
    }

    private ForeignCurrencyExchange_Mdl topControlSaveInitialize() {

        ForeignCurrencyExchange_Mdl mrTemp = new ForeignCurrencyExchange_Mdl();

        if (topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setNewMode(true);
        }
        mrTemp.setExchangeDate(DmShared.xNull(txtExchangeDate.getText()));
        grdForeignCurrency.commitChanges();
        mrTemp.getLstCurrencyExchange().addAll(grdForeignCurrency.getGrid().getStore().getAll());
        return mrTemp;
    }

    private void topControlMoveRec(String strKey) {
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<ForeignCurrencyExchange_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtExchangeDate).show();
            }

            @Override
            public void onSuccess(DmResult<ForeignCurrencyExchange_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<ForeignCurrencyExchange_Mdl> result) {
        ForeignCurrencyExchange_Mdl data;
        clear();
        if (result.isStatus()) {
            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());

            txtExchangeDate.setText(DmShared.dNull(data.getExchangeDate()));

            grdForeignCurrency.getGrid().getStore().clear();
            grdForeignCurrency.getGrid().getStore().addAll(data.getLstCurrencyExchange());
            userControl.setTextBlog(modifiedByBlog, DmShared.xNull(data.getSharedModifiedBy()));
        }
        topControl.setDisplay(true);
    }
}
