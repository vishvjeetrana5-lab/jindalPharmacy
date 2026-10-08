package aarogya.master.poolEarningDeductionEntry.client;

import aarogya.master.poolEarningDeductionEntry.client.PoolEarningDeductionEntry_Dsg;
import aarogya.master.poolEarningDeductionEntry.shared.CopyFrom_Mdl;
import aarogya.master.poolEarningDeductionEntry.shared.Find_Mdl;
import aarogya.master.poolEarningDeductionEntry.shared.PoolEarningDeductionEntryGridEdit_Mdl;
import aarogya.master.poolEarningDeductionEntry.shared.PoolEarningDeductionEntry_Mdl;

import aarogya.master.poolEarningDeductionEntry.shared.PoolEarningDeductionEntry_Srv;
import aarogya.master.poolEarningDeductionEntry.shared.PoolEarningDeductionEntry_SrvAsync;
import aarogya.master.poolEarningDeductionEntry.shared.PoolEarningDeductionEntry_Vld;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import java.util.List;

public class PoolEarningDeductionEntry extends PoolEarningDeductionEntry_Dsg implements BlurHandler, ClickHandler, DmRootEvents, AttachEvent.Handler {

    private final ServiceInvoker<PoolEarningDeductionEntry_SrvAsync> srvAsync;
    private final PoolEarningDeductionEntryGridEdit<PoolEarningDeductionEntry_Mdl, Grd_PrpAcs> gateGridEdit = new PoolEarningDeductionEntryGridEdit(grdPoolEarningDetail);
    boolean serviceFlag = false;

    public PoolEarningDeductionEntry(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        this.asWidget().setTitle(widgetName);
        srvAsync = new ServiceInvoker<PoolEarningDeductionEntry_SrvAsync>(GWT.create(PoolEarningDeductionEntry_Srv.class), PoolEarningDeductionEntry.class.getName());
        textHelpEvents();
        topControlEvents(menuPermission);
        findEvents();
        manageEvents();
        gridEvents();

        swi.setWidget(this);

    }

    private void onAdd() {
        clear();
        manageDisplay(true);
        getInvestigation();
        txtHSalaryPeriod.setFocus(true);
//        grdPoolEarningDetail.getGrid().getStore().add(new PoolEarningDeductionEntryGridEdit_Mdl());
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
                manageDisplay(true);
//                grdPoolEarningDetail.getGrid().getStore().add(new PoolEarningDeductionEntryGridEdit_Mdl());
                txtHSalaryPeriod.setFocus(true);

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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(PoolEarningDeductionEntry.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void textHelpEvents() {

        txtHSalaryPeriod.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSalaryPeriod(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSalaryPeriod).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSalaryPeriod.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSalaryPeriod).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHPoolGroupName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHCName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPoolGroupName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPoolGroupName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPoolGroupName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
        txtCopyFrom.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<CopyFrom_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHCopyFrom(DmShared.xNull(txtHPoolGroupName.getTag()), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<CopyFrom_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtCopyFrom).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<CopyFrom_Mdl>> result) {
                        if (result.isStatus()) {
                            txtCopyFrom.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtCopyFrom).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, CopyFrom_Mdl tRow) {
//                txtHSalaryPeriod.setText(tRow.getSalName());
//                txtHSalaryPeriod.setTag(tRow.getSalCode());
//                txtHEmployeeName.setText(tRow.getEmpName());
//                txtHEmployeeName.setTag(tRow.getEmpCode());
                fillGrdDetail(tRow.getSharedKeyFieldCode());
            }
        });

    }

    private void fillGrdDetail(String key) {
        srvAsync.invokeRPC().getGridDetail(key, new AsyncCallback<DmResult<List<PoolEarningDeductionEntryGridEdit_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<PoolEarningDeductionEntryGridEdit_Mdl>> result) {
                if (result.isStatus()) {
                    List<PoolEarningDeductionEntryGridEdit_Mdl> mdl = result.getData();
                    grdPoolEarningDetail.getGrid().getStore().clear();
                    grdPoolEarningDetail.getGrid().getStore().addAll(mdl);
//                    grdEarningDetail.getGrid().getStore().add(new EarningDeductionEntryGridEdit_Mdl());

                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });

    }

    private void topControlSave() {
        PoolEarningDeductionEntry_Mdl mrTemp;

        try {

            mrTemp = topControlSaveInitialize();
            PoolEarningDeductionEntry_Vld.chkSave(mrTemp);

            srvAsync.invokeRPC().saveRecord(mrTemp, DmClient.getVoucherPrefix(), new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHSalaryPeriod).show();
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
                        topControlSaveError(result.getErrorNumber(), result.getValue1(), result.getValue2(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getErrorSource(), e.getErrorSourceCode(), e.getMessage());
        }
    }

    private PoolEarningDeductionEntry_Mdl topControlSaveInitialize() {
        PoolEarningDeductionEntry_Mdl mrTemp = new PoolEarningDeductionEntry_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }
//        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtHSalaryPeriod.getTag2nd()));

//        mrTemp.setToDate(DmShared.dNull(txtToDate.getText()));
//        mrTemp.setFromDate(DmShared.dNull(txtFromDate.getText()));
        mrTemp.setSalCode(DmShared.xNull(txtHSalaryPeriod.getTag()));
        mrTemp.setSalName(DmShared.xNull(txtHSalaryPeriod.getText()));
        mrTemp.setPoolGrpCode(DmShared.xNull(txtHPoolGroupName.getTag()));
        mrTemp.setPoolGrpName(DmShared.xNull(txtHPoolGroupName.getText()));
        mrTemp.getGateDetails().clear();
        mrTemp.getGateDetails().addAll(grdPoolEarningDetail.getGrid().getStore().getAll());

        return mrTemp;
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

    private void clear() {
        grdPoolEarningDetail.getGrid().getStore().clear();
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");

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
                    topControl.setNavigator(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void topControlSaveError(int errorNo, String errorSource, String errorSourceCode, String errorMessage) {
        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtHSalaryPeriod).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtHPoolGroupName).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, new HideEvent.HideHandler() {
                    @Override
                    public void onHide(HideEvent event) {
                        ((CellSelectionModel) grdPoolEarningDetail.getGrid().getSelectionModel()).selectCell(0, 1);
                    }
                }).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHSalaryPeriod).show();
                break;
        }
    }

    private void topControlMoveRec(String strKey) {
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<PoolEarningDeductionEntry_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<PoolEarningDeductionEntry_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<PoolEarningDeductionEntry_Mdl> result) {
        PoolEarningDeductionEntry_Mdl data;
        clear();

        if (result.isStatus()) {
            data = result.getData();
            topControl.setDocId(DmShared.xNull(data.getSharedKeyFieldCode()));
//            txtHSalaryPeriod.setTag2nd(DmShared.xNull(data.getSharedKeyFieldName()));
            txtHSalaryPeriod.setText(DmShared.xNull(data.getSalName()));
            txtHSalaryPeriod.setTag(DmShared.xNull(data.getSalCode()));
            txtHPoolGroupName.setText(DmShared.xNull(data.getPoolGrpName()));
            txtHPoolGroupName.setTag(DmShared.xNull(data.getPoolGrpCode()));
            userControl.setTextBlog(preparedByBlog, DmShared.xNull(data.getSharedPreparedBy()));
            userControl.setTextBlog(modifiedByBlog, DmShared.xNull(data.getSharedModifiedBy()));
            grdPoolEarningDetail.getGrid().getStore().clear();
            grdPoolEarningDetail.getGrid().getStore().addAll(data.getGateDetails());

        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
        manageDisplay(false);
    }

    private void manageDisplay(boolean flag) {

        if (topControl.getMode().equalsIgnoreCase("New")) {
            flwCopyFromTr.getElement().getStyle().setDisplay(Style.Display.BLOCK);
        } else {
            flwCopyFromTr.getElement().getStyle().setDisplay(Style.Display.NONE);
        }

        txtHSalaryPeriod.setEnabled(flag);
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

    private void gridEvents() {
        gateGridEdit.addCallBack(new PoolEarningDeductionEntryGridEdit.CallBack() {

            public void onOk(PoolEarningDeductionEntryGridEdit_Mdl opdService) {
                grdPoolEarningDetail.commitChanges();

            }

            public void onDelete() {
                grdPoolEarningDetail.commitChanges();

            }

        });

        grdPoolEarningDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showGrnEntryGridEdit(event.getRowIndex());
            }
        });

        grdPoolEarningDetail.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showGrnEntryGridEdit(grdPoolEarningDetail.getGrid().getStore().indexOf(grdPoolEarningDetail.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
    }

    private void showGrnEntryGridEdit(int currentRowIndex) {
        if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {
            gateGridEdit.show(currentRowIndex, false, false, false, this);
        } else {
            gateGridEdit.show(currentRowIndex, true, false, true, this);
        }
    }

    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    @Override
    public void onBlur(BlurEvent event) {

    }

    @Override
    public void onClick(ClickEvent event) {

    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            gateGridEdit.hide();

        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getShiftKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_P) {
            rootEvent.stopPropagation();
        }
    }

    private void manageEvents() {

    }

    private void getInvestigation() {
        srvAsync.invokeRPC().getInvestigationDetail(new AsyncCallback<DmResult<List<PoolEarningDeductionEntryGridEdit_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHPoolGroupName).show();
            }

            @Override
            public void onSuccess(DmResult<List<PoolEarningDeductionEntryGridEdit_Mdl>> result) {
                if (result.isStatus()) {
                    grdPoolEarningDetail.getGrid().getStore().clear();
                    grdPoolEarningDetail.getGrid().getStore().addAll(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHPoolGroupName).show();
                }
            }
        });
    }

}
