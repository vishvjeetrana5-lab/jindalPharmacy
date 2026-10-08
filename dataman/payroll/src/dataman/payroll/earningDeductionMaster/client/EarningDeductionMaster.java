package dataman.payroll.earningDeductionMaster.client;

import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import java.util.List;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.BlurEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import dataman.payroll.earningDeductionMaster.shared.EarningDeductionMaster_Mdl;
import dataman.payroll.earningDeductionMaster.shared.EarningDeductionMaster_Srv;
import dataman.payroll.earningDeductionMaster.shared.EarningDeductionMaster_SrvAsync;
import dataman.payroll.earningDeductionMaster.shared.EarningDeductionMaster_Vld;
import dataman.payroll.earningDeductionMaster.shared.Find_Mdl;
import dataman.payroll.earningDeductionMaster.shared.GrdEarnDed_Mdl;
import java.util.ArrayList;

public class EarningDeductionMaster extends EarningDeductionMaster_Dsg {

    private final ServiceInvoker<EarningDeductionMaster_SrvAsync> srvAsync;
    private final EarningDeductionGridEdit<GrdEarnDed_Mdl, KeyFieldHashCode_T_PrpAcs> EarningDeductionGrid = new EarningDeductionGridEdit(gridEarnDed);

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setRefreshRequired(false);
        topControl.setOtherRequired(false);

        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                onAdd();
                gridEarnDed.getGrid().getStore().add(new GrdEarnDed_Mdl());
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                txtName.setFocus(true);
                gridEarnDed.getGrid().getStore().add(new GrdEarnDed_Mdl());
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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(EarningDeductionMaster.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void textHelpEvents() {
        txtName.addCallBack(new DmSuggestBox.CallBack() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getSuggestionName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<String>>>() {

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

        txtShortName.addCallBack(new DmSuggestBox.CallBack() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getSuggestionShortName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<String>>>() {

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

        txtHNature.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHNature(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                manageTxtEnable();
            }
        });

        txtHClaculateOn.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHCalculateOn(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHClaculateOn).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHClaculateOn.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHClaculateOn).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHRoundOff.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHRoundOff(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHRoundOff).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHRoundOff.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHRoundOff).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });

        txtHLimitForCalcRes.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHLimitForCalcRes(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHLimitForCalcRes).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHLimitForCalcRes.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHLimitForCalcRes).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });

        txtHBasedOn.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHBasedOn(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHBasedOn).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHBasedOn.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHBasedOn).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
//                flwBlank.clear();

                if (DmShared.iNull(txtHBasedOn.getTag()).intValue() == 1) {
                    flwBlank.setVisible(true);
                } else {
                    flwBlank.setVisible(false);
                }
            }
        });
        txtHUseIn.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHUsein(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHUseIn).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHUseIn.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHUseIn).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
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

    private void showEarnDedGrid(int currentRowIndex, String earnDedName) {
        if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {
            EarningDeductionGrid.show(currentRowIndex, false, false, false, earnDedName);
        } else {
            EarningDeductionGrid.show(currentRowIndex, true, true, true, earnDedName);
        }
    }

    private void gridEvents() {
        //================EarningDeductionGridEdit======================================================
        EarningDeductionGrid.addCallBack(new EarningDeductionGridEdit.CallBack() {
            @Override
            public void onOk(GrdEarnDed_Mdl gridEarnDedMdl) {
                gridEarnDed.commitChanges();
            }

            @Override
            public void onDelete() {
                gridEarnDed.commitChanges();
            }
        });

        gridEarnDed.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showEarnDedGrid(event.getRowIndex(), DmShared.xNull(txtName.getText()));
            }
        });

        gridEarnDed.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showEarnDedGrid(gridEarnDed.getGrid().getStore().indexOf(gridEarnDed.getGrid().getSelectionModel().getSelectedItem()), DmShared.xNull(txtName.getText()));
                }
            }
        }, KeyDownEvent.getType());
    }

    public EarningDeductionMaster(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        srvAsync = new ServiceInvoker<>(GWT.create(EarningDeductionMaster_Srv.class), EarningDeductionMaster.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        textHelpEvents();
        findEvents();
        gridEvents();
        flwBlank.setVisible(false);
        swi.setWidget(this);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private EarningDeductionMaster_Mdl topControlSaveInitialize() {
        EarningDeductionMaster_Mdl mrTemp = new EarningDeductionMaster_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtName.getText()));
        mrTemp.setShortName(DmShared.xNull(txtShortName.getText()));
        mrTemp.setSerialNo(DmShared.xNull(txtSerialNo.getText()));
        mrTemp.setNatureCode(DmShared.xNull(txtHNature.getTag()));
        mrTemp.setNatureName(DmShared.xNull(txtHNature.getText()));
        mrTemp.setCalculateOnCode(DmShared.xNull(txtHClaculateOn.getTag()));
        mrTemp.setCalculateOnName(DmShared.xNull(txtHClaculateOn.getText()));
        mrTemp.setRoundCode(DmShared.xNull(txtHRoundOff.getTag()));
        mrTemp.setRoundName(DmShared.xNull(txtHRoundOff.getText()));
        mrTemp.setTypeCode(DmShared.xNull(txtHType.getTag()));
        mrTemp.setTypeName(DmShared.xNull(txtHType.getText()));
        mrTemp.setLimitOfAmount(DmShared.xNull(txtLimitOfAmount.getText()));
        mrTemp.setLimitForCalsRes(DmShared.xNull(txtHLimitForCalcRes.getTag()));
        mrTemp.setBasedOnCode(DmShared.xNull(txtHBasedOn.getTag()));
        mrTemp.setBasedOnName(DmShared.xNull(txtHBasedOn.getText()));
        mrTemp.setUseInEntryCode(DmShared.xNull(txtHUseIn.getTag()));
        mrTemp.setUseInEntryName(DmShared.xNull(txtHUseIn.getText()));
        mrTemp.getGrdEarnDed_Mdl().addAll(gridEarnDed.getGrid().getStore().getAll());
        return mrTemp;
    }

    private void topControlSave() {
        EarningDeductionMaster_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            EarningDeductionMaster_Vld.chkSave(mrTemp);

            srvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtName).show();
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
                new DmAlertMessageBox(errorMessage, txtShortName).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtHNature).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtHClaculateOn).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtHRoundOff).show();
                break;
            case 600:
                new DmAlertMessageBox(errorMessage, txtLimitOfAmount).show();
                break;
            case 700:
                new DmAlertMessageBox(errorMessage, txtHLimitForCalcRes).show();
                break;
            case 800:
                new DmAlertMessageBox(errorMessage, txtHBasedOn).show();
                break;
            case 900:
                new DmAlertMessageBox(errorMessage, txtHLimitForCalcRes).show();
                break;
            case 1000:
                new DmAlertMessageBox(errorMessage, txtHUseIn).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtName).show();
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
                        topControl.setNavigator(result.getData());
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            }
        });
    }

    private void topControlMoveRec(String strKey) {
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<EarningDeductionMaster_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<EarningDeductionMaster_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<EarningDeductionMaster_Mdl> result) {
        EarningDeductionMaster_Mdl data;
//        flwBlank.clear();
        if (result.isStatus()) {
            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());
            txtName.setText(data.getSharedKeyFieldName());
            txtShortName.setText(data.getShortName());
            txtSerialNo.setText(data.getSerialNo());
            txtHNature.setText(data.getNatureName());
            txtHNature.setTag(data.getNatureCode());
            txtHClaculateOn.setText(data.getCalculateOnName());
            txtHClaculateOn.setTag(data.getCalculateOnCode());
            txtHRoundOff.setText(data.getRoundName());
            txtHRoundOff.setTag(data.getRoundCode());
            txtHType.setText(data.getTypeName());
            txtHType.setTag(data.getTypeCode());
            txtHBasedOn.setText(data.getBasedOnName());
            txtHBasedOn.setTag(data.getBasedOnCode());
            txtHUseIn.setText(data.getUseInEntryName());
            txtHUseIn.setTag(data.getUseInEntryCode());
            txtLimitOfAmount.setText(data.getLimitOfAmount());
            if (DmShared.iNull(data.getLimitForCalsRes()).intValue() == 0) {
                txtHLimitForCalcRes.setText("Calculation");
            } else {
                txtHLimitForCalcRes.setText("Restriction");
            }
            txtHLimitForCalcRes.setTag(data.getLimitForCalsRes());
            
            if (DmShared.xNull(data.getSystemDefine()).equalsIgnoreCase("1")) {
                lblIsSytemGenrate.setText("THIS IS SYSTEM DEFINE.");
            } else {
                lblIsSytemGenrate.setText("");
            }
            if (DmShared.iNull(data.getBasedOnCode()).intValue() == 1 || DmShared.xNull(data.getShortName()).trim().equalsIgnoreCase("GS")) {
                flwBlank.setVisible(true);
            } else {
                flwBlank.setVisible(false);
            }
            gridEarnDed.getGrid().getStore().clear();
            gridEarnDed.getGrid().getStore().addAll(data.getGrdEarnDed_Mdl());

            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
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
        gridEarnDed.getGrid().getStore().clear();
        lblIsSytemGenrate.setText("");
    }
    
    private void manageTxtEnable(){
        if (DmShared.xNull(txtHNature.getTag()).trim().equalsIgnoreCase("3")){
            txtHBasedOn.setEnabled(false);
            txtHClaculateOn.setEnabled(false);
            txtHLimitForCalcRes.setEnabled(false);
            txtHRoundOff.setEnabled(false);
        }else {
            txtHBasedOn.setEnabled(true);
            txtHClaculateOn.setEnabled(true);
            txtHLimitForCalcRes.setEnabled(true);
            txtHRoundOff.setEnabled(true);
        }
    }

    private void onAdd() {
        clear();
//        manageDisplay(true);
        txtName.setFocus(true);
        txtHNature.setTag("1");
        txtHNature.setText("Earning");
        txtHRoundOff.setTag("0");
        txtHRoundOff.setText("No");

    }

}
