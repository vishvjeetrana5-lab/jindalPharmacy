package aarogya.master.hsnMaster.client;

import dataman.dmBase.self.shared.*;
import aarogya.master.hsnMaster.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.menu.Item;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import java.util.List;

public final class HSNMaster extends HSNMaster_Dsg implements KeyPressHandler, SelectionHandler<Item> {

    private final ServiceInvoker<HSNMaster_SrvAsync> SrvAsync;
    private final RateGridEdit<GrdRateITS_Mdl, GridRate_PrpAcs> grdRateGridEdit = new RateGridEdit(grdRate);
    private int rowIndex = 0;
    private String taxBasedOn = "", prevTaxBasedOn = "";
    private final ExportData exportData = new ExportData();

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());

        topControl.setRefreshRequired(false);

        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                clear();
                onAdd();
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        txtName.setFocus(true);
                    }
                });
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                grdRate.getGrid().getStore().add(new GrdRateITS_Mdl());
                setFlwPanels();
                txtName.setFocus(true);
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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(HSNMaster.this);
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

        grdRateGridEdit.addCallBack(new RateGridEdit.CallBack() {
            @Override
            public void onOk(GrdRateITS_Mdl opdService) {
                grdRate.commitChanges();
            }

            @Override
            public void onDelete() {
                grdRate.commitChanges();
            }
        });
        txtName.addCallBack(new DmSuggestBox.CallBack() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getSuggestionName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<String>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<String>> result) {
                        if (result.isStatus()) {
                            txtName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtName).show();
                        }
                    }
                });
            }
        });

        txtHNature.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHNature(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHNature).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHNature.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHNature).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHTaxBasedOn.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHTaxBasedOn(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHTaxBasedOn).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHTaxBasedOn.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHTaxBasedOn).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus & t1Row != null) {
                    if (!prevTaxBasedOn.equalsIgnoreCase(t1Row.getSharedKeyFieldCode())) {
                        prevTaxBasedOn = t1Row.getSharedKeyFieldCode();
                        setFlwPanels();
                    }
                }
            }
        });

        txtHITSOnPurchase.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHITS(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHITSOnPurchase).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHITSOnPurchase.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHITSOnPurchase).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHITSOnPurchaseReturn.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHITS(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHITSOnPurchaseReturn).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHITSOnPurchaseReturn.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHITSOnPurchaseReturn).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHITSOnSale.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHITS(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHITSOnSale).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHITSOnSale.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHITSOnSale).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHITSOnSaleReturn.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHITS(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHITSOnSaleReturn).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHITSOnSaleReturn.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHITSOnSaleReturn).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHPurchaseAC.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAccountName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPurchaseAC).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPurchaseAC.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPurchaseAC).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHSaleAC.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAccountName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSaleAC).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSaleAC.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSaleAC).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHSaleReturnAC.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAccountName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSaleReturnAC).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSaleReturnAC.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSaleReturnAC).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHPurchaseReturnAC.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAccountName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPurchaseReturnAC).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPurchaseReturnAC.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPurchaseReturnAC).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHItemGSTNature.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHGSTNature(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHItemGSTNature).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHItemGSTNature.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHItemGSTNature).show();
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

    private void gridEvents() {
        grdRate.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showRateGrd(event.getRowIndex());
            }
        });
        grdRate.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showRateGrd(grdRate.getGrid().getStore().indexOf(grdRate.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

    }

    private void showRateGrd(int currentRowIndex) {
        if (topControl.getMode().equalsIgnoreCase("Browse")) {
            grdRateGridEdit.show(currentRowIndex, false, false, false, this);
        } else {
            grdRateGridEdit.show(currentRowIndex, true, true, true, this);
        }
    }

    public HSNMaster(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        SrvAsync = new ServiceInvoker<>(GWT.create(HSNMaster_Srv.class), HSNMaster.class.getName());
        this.asWidget().setTitle(widgetName);
        txtHSN.addKeyPressHandler(this);
        mnuExport.addSelectionHandler(this);
        topControlEvents(menuPermission);
        textHelpEvents();
        findEvents();
        gridEvents();
        swi.setWidget(this);
    }

    private void setFlwPanels() {
        flwMain.clear();

        if (DmShared.xNull(txtHTaxBasedOn.getTag()).equalsIgnoreCase("1")) {
            vl.add(grdRate.getGrid());
            flwMain.add(flwRateGrid);
            if (grdRate.getGrid().getStore().size() == 0) {
                grdRate.getGrid().getStore().add(new GrdRateITS_Mdl());
            }
            txtHITSOnPurchase.setTag("");
            txtHITSOnPurchase.setText("");

            txtHITSOnPurchaseReturn.setTag("");
            txtHITSOnPurchaseReturn.setText("");

            txtHITSOnSale.setTag("");
            txtHITSOnSale.setText("");

            txtHITSOnSaleReturn.setTag("");
            txtHITSOnSaleReturn.setText("");

            txtHSaleAC.setTag("");
            txtHSaleAC.setText("");

            txtHSaleReturnAC.setTag("");
            txtHSaleReturnAC.setText("");

            txtHPurchaseAC.setTag("");
            txtHPurchaseAC.setText("");

            txtHPurchaseReturnAC.setTag("");
            txtHPurchaseReturnAC.setText("");

//            manageDisplay(false);
        } else if (DmShared.xNull(txtHTaxBasedOn.getTag()).equalsIgnoreCase("0")) {
            flwMain.clear();

            vl.get().remove(grdRate.getGrid());
            vl.add(txtHITSOnPurchase);
            vl.add(txtHITSOnSale);
            vl.add(txtHITSOnPurchaseReturn);
            vl.add(txtHITSOnSaleReturn);
            vl.add(txtHPurchaseAC);
            vl.add(txtHSaleAC);
            vl.add(txtHPurchaseReturnAC);
            vl.add(txtHSaleReturnAC);
            flwMain.add(flwITS);
//            manageDisplay(true);
        }
    }

    private HSNMaster_Mdl topControlSaveInitialize() {
        HSNMaster_Mdl mrTemp = new HSNMaster_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtName.getText()));
        mrTemp.setHSN(DmShared.xNull(txtHSN.getText()));
        mrTemp.setTaxBasedOnTag(DmShared.xNull(txtHTaxBasedOn.getTag()));
        mrTemp.setGstNatureTag(DmShared.xNull(txtHItemGSTNature.getTag()));

        mrTemp.setItsOnPurchaseReturnTag(DmShared.xNull(txtHITSOnPurchaseReturn.getTag()));
        mrTemp.setItsOnPurchaseTag(DmShared.xNull(txtHITSOnPurchase.getTag()));
        mrTemp.setItsOnSaleTag(DmShared.xNull(txtHITSOnSale.getTag()));
        mrTemp.setItsOnSaleReturnTag(DmShared.xNull(txtHITSOnSaleReturn.getTag()));

        mrTemp.setAcPurchaseReturnTag(DmShared.xNull(txtHPurchaseReturnAC.getTag()));
        mrTemp.setAcPurchaseTag(DmShared.xNull(txtHPurchaseAC.getTag()));
        mrTemp.setAcSaleTag(DmShared.xNull(txtHSaleAC.getTag()));
        mrTemp.setAcSaleReturnTag(DmShared.xNull(txtHSaleReturnAC.getTag()));

        taxBasedOn = (DmShared.xNull(txtHTaxBasedOn.getTag()));
        prevTaxBasedOn = (DmShared.xNull(txtHTaxBasedOn.getTag()));
        mrTemp.setNatureTag(DmShared.xNull(txtHNature.getTag()));
        if (!DmShared.xNull(txtHTaxBasedOn.getTag()).equalsIgnoreCase("0")) {
            mrTemp.getGrdRateLst().addAll(grdRate.getGrid().getStore().getAll());
        }

        return mrTemp;
    }

    private void topControlSave() {
        HSNMaster_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            HSNMaster_Vld.chkSave(mrTemp);
            SrvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHSN).show();
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
                new DmAlertMessageBox(errorMessage, txtName).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtHSN).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtHNature).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtHItemGSTNature).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtHTaxBasedOn).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtName).show();
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
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<HSNMaster_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<HSNMaster_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<HSNMaster_Mdl> result) {
        HSNMaster_Mdl data;
        clear();
        if (result.isStatus()) {
            data = result.getData();
            txtHTaxBasedOn.setTag(data.getTaxBasedOnTag());
            txtHTaxBasedOn.setText(data.getTaxBasedOnText());
            prevTaxBasedOn = DmShared.xNull(data.getTaxBasedOnTag());

            topControl.setDocId(data.getSharedKeyFieldCode());
            txtName.setText(data.getSharedKeyFieldName());
            txtHItemGSTNature.setTag(data.getGstNatureTag());
            txtHItemGSTNature.setText(data.getGstNatureText());

            txtHITSOnPurchase.setTag(data.getItsOnPurchaseTag());
            txtHITSOnPurchase.setText(data.getItsOnPurchaseText());

            txtHITSOnPurchaseReturn.setTag(data.getItsOnPurchaseReturnTag());
            txtHITSOnPurchaseReturn.setText(data.getItsOnPurchaseReturnText());

            txtHITSOnSale.setTag(data.getItsOnSaleTag());
            txtHITSOnSale.setText(data.getItsOnSaleText());

            txtHITSOnSaleReturn.setTag(data.getItsOnSaleReturnTag());
            txtHITSOnSaleReturn.setText(data.getItsOnSaleReturnText());

            txtHPurchaseAC.setTag(data.getAcPurchaseTag());
            txtHPurchaseAC.setText(data.getAcPurchaseText());

            txtHPurchaseReturnAC.setTag(data.getAcPurchaseReturnTag());
            txtHPurchaseReturnAC.setText(data.getAcPurchaseReturnText());

            txtHSaleAC.setTag(data.getAcSaleTag());
            txtHSaleAC.setText(data.getAcSaleText());

            txtHSaleReturnAC.setTag(data.getAcSaleReturnTag());
            txtHSaleReturnAC.setText(data.getAcSaleReturnText());

            txtHSN.setText(data.getHSN());
            txtHNature.setTag(data.getNatureTag());
            txtHNature.setText(data.getNatureText());
            if (DmShared.xNull(txtHTaxBasedOn.getTag()).equalsIgnoreCase("1")) {
                grdRate.getGrid().getStore().addAll(data.getGrdRateLst());
            }

            setFlwPanels();
            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());

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

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private void clear() {
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        grdRate.getGrid().getStore().clear();
        prevTaxBasedOn = "";
    }

    private void onAdd() {
        if (DmShared.xNull(taxBasedOn).equalsIgnoreCase("1")) {
            txtHTaxBasedOn.setTag("1");
            txtHTaxBasedOn.setText("Rate");
        } else {
            txtHTaxBasedOn.setTag("0");
            txtHTaxBasedOn.setText("Fixed");
        }

        txtHNature.setText("Goods");
        txtHNature.setTag("1");
        setFlwPanels();
    }

    @Override
    public void onKeyPress(KeyPressEvent event) {
        int charCode = event.getCharCode();
        if (event.getSource().equals(txtHSN)) {
            if ((charCode > 31) && (charCode < 48 || charCode > 57)) {
                txtHSN.cancelKey();
            }
        }
    }

    @Override
    public void onSelection(SelectionEvent<Item> event) {
        if (event.getSource().equals(mnuExport)) {
            exportData.show();
        }
    }
}
