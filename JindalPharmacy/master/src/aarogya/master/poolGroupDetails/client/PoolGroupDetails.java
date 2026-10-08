package aarogya.master.poolGroupDetails.client;

import aarogya.master.poolGroupDetails.shared.Find_Mdl;
import aarogya.master.poolGroupDetails.shared.PoolGroupDetailsGridEdit_Mdl;
import aarogya.master.poolGroupDetails.shared.PoolGroupDetails_Mdl;
import aarogya.master.poolGroupDetails.shared.PoolGroupDetails_Srv;
import aarogya.master.poolGroupDetails.shared.PoolGroupDetails_SrvAsync;
import aarogya.master.poolGroupDetails.shared.PoolGroupDetails_Vld;
import com.google.gwt.core.client.GWT;
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
import java.util.ArrayList;
import java.util.List;

public class PoolGroupDetails extends PoolGroupDetails_Dsg implements BlurHandler, ClickHandler, DmRootEvents, AttachEvent.Handler {

    private final ServiceInvoker<PoolGroupDetails_SrvAsync> srvAsync;
    private final PoolGroupDetailsGridEdit<PoolGroupDetails_Mdl, Grd_PrpAcs> gateGridEdit = new PoolGroupDetailsGridEdit(grdGateDetail);
//    boolean serviceFlag = false;

    public PoolGroupDetails(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        this.asWidget().setTitle(widgetName);
        srvAsync = new ServiceInvoker<PoolGroupDetails_SrvAsync>(GWT.create(PoolGroupDetails_Srv.class), PoolGroupDetails.class.getName());
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
        txtHPoolGroupName.setFocus(true);
        grdGateDetail.getGrid().getStore().add(new PoolGroupDetailsGridEdit_Mdl());
    }

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());

        topControl.setOtherRequired(false);

        topControl.setRefreshRequired(false);

        for (String controlPermission : menuPermission.getControlPermission()) {
//            if (DmShared.xNull(controlPermission).equalsIgnoreCase(dateTimeEditableBlog)) {
//                dateTimeEditable = true;
//            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(gateOutByBlog)) {
//                isGateOutPermission = true;
//            }
        }

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
                grdGateDetail.getGrid().getStore().add(new PoolGroupDetailsGridEdit_Mdl());
                txtHPoolGroupName.setFocus(true);

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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(PoolGroupDetails.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void textHelpEvents() {

        txtHPoolGroupName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHPoolGroupName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

    }

    private void topControlSave() {
        PoolGroupDetails_Mdl mrTemp;

        try {

            mrTemp = topControlSaveInitialize();

            PoolGroupDetails_Vld.chkSave(mrTemp);

            srvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHPoolGroupName).show();
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

    private PoolGroupDetails_Mdl topControlSaveInitialize() {
        PoolGroupDetails_Mdl mrTemp = new PoolGroupDetails_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }
        mrTemp.setToDate(DmShared.dNull(txtToDate.getText()));
        mrTemp.setFromDate(DmShared.dNull(txtFromDate.getText()));
        mrTemp.setPoolGroupCode(DmShared.xNull(txtHPoolGroupName.getTag()));
        mrTemp.getGateDetails().clear();
        mrTemp.getGateDetails().addAll(grdGateDetail.getGrid().getStore().getAll());
        int total = 0;

        for (int i = 0; i < grdGateDetail.getGrid().getStore().getAll().size(); i++) {
            if (DmShared.xNull(grdGateDetail.getGrid().getStore().get(i).getValueName()).equalsIgnoreCase("%")) {
                 total += DmShared.vNull(grdGateDetail.getGrid().getStore().get(i).getAmount());
            }
           

        }
        mrTemp.setTotal(total);

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
        grdGateDetail.getGrid().getStore().clear();
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
                new DmAlertMessageBox(errorMessage, txtHPoolGroupName).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtFromDate).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtToDate).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, new HideEvent.HideHandler() {
                    @Override
                    public void onHide(HideEvent event) {
                        ((CellSelectionModel) grdGateDetail.getGrid().getSelectionModel()).selectCell(0, 1);
                    }
                }).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHPoolGroupName).show();
                break;
        }
    }

    private void topControlMoveRec(String strKey) {
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<PoolGroupDetails_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<PoolGroupDetails_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<PoolGroupDetails_Mdl> result) {
        PoolGroupDetails_Mdl data;
        clear();

        if (result.isStatus()) {
            data = result.getData();
            topControl.setDocId(DmShared.xNull(data.getSharedKeyFieldCode()));
            txtHPoolGroupName.setText(DmShared.xNull(data.getPoolGroupName()));
            txtHPoolGroupName.setTag(DmShared.xNull(data.getPoolGroupCode()));
            txtToDate.setText(DmShared.xNull(data.getToDate()));
            txtFromDate.setText(DmShared.xNull(data.getFromDate()));

            userControl.setTextBlog(preparedByBlog, DmShared.xNull(data.getSharedPreparedBy()));
            userControl.setTextBlog(modifiedByBlog, DmShared.xNull(data.getSharedModifiedBy()));
            grdGateDetail.getGrid().getStore().clear();
            grdGateDetail.getGrid().getStore().addAll(data.getGateDetails());

        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
        manageDisplay(false);
    }

    private void manageDisplay(boolean flag) {

        txtHPoolGroupName.setEnabled(flag);
//        if (topControl.getMode().equalsIgnoreCase("Edit")) {
//            txtHPoolGroupName.setEnabled(serviceFlag);
//            txtFromDate.setEnabled(serviceFlag);
//            txtToDate.setEnabled(serviceFlag);
//        }
//        txtEntryNo.setEnabled(false);
//        txtDate.setEnabled(false);
//        txtInTime.setEnabled(false);
//        txtSlipNo.setEnabled(false);
//        txtOutDate.setEnabled(false);
//        txtOutTime.setEnabled(false);
//        txtUnitGross.setEnabled(false);
//        txtUnitTare.setEnabled(false);
//        txtUnitNet.setEnabled(false);
//
//        flwInDateTimeContainer.clear();
//        if (flag) {
//            if (!dateTimeEditable) {
//                txtDate.setEnabled(false);
//                txtInTime.setEnabled(false);
//            }
//
//            if (topControl.getMode().equalsIgnoreCase("Edit")) {
//                txtHVoucherType.setEnabled(false);
//                if (dateTimeEditable) {
//                    flwInDateTimeContainer.add(flwInDateTime);
//                }
//            } else {
//                flwInDateTimeContainer.add(flwInDateTime);
//            }
//        }
//        flwGateOutContainer.clear();
//        if (!flag) {  //Browse Mode
//            if (DmShared.xNull(userControl.getTextBlog(gateOutByBlog)).equalsIgnoreCase("")) {
//                if (isGateOutPermission) {
//                    flwGateOutContainer.add(flwGateOutEntry);
//                    txtOutDate.setEnabled(dateTimeEditable);
//                    txtOutTime.setEnabled(dateTimeEditable);
//                    txtSlipNo.setEnabled(true);
//
//                }
//
//            } else {
//                flwGateOutContainer.add(flwGateOutDetail);
//            }
//
//        } else { //New or Edit Mode
//            txtDate.setEnabled(dateTimeEditable);
//            txtInTime.setEnabled(dateTimeEditable);
//        }
//
//        showGateAdviceBtn(flag);
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
        gateGridEdit.addCallBack(new PoolGroupDetailsGridEdit.CallBack() {

            public void onOk(PoolGroupDetailsGridEdit_Mdl opdService) {
                grdGateDetail.commitChanges();

            }

            public void onDelete() {
                grdGateDetail.commitChanges();

            }

        });

        grdGateDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showGrnEntryGridEdit(event.getRowIndex());
            }
        });

        grdGateDetail.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showGrnEntryGridEdit(grdGateDetail.getGrid().getStore().indexOf(grdGateDetail.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
    }

    private void showGrnEntryGridEdit(int currentRowIndex) {
        if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {
            gateGridEdit.show(currentRowIndex, false, false, false, this);
        } else {
            gateGridEdit.show(currentRowIndex, true, true, true, this);
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
        if (event.getSource() == txtToDate) {
            txtToDate.setText(DmClient.returnDate(txtToDate.getText()));
        } else if (event.getSource() == txtFromDate) {
            txtFromDate.setText(DmClient.returnDate(txtFromDate.getText()));
//        } else if (event.getSource() == txtSlipNo) {
//
//            getSlipDetails(txtSlipNo.getText());
//        } else if (event.getSource() == txtGrossWeight) {
//            calculate();
//
//        } else if (event.getSource() == txtTareWeight) {
//            calculate();
//        }

        }
    }

    @Override
    public void onClick(ClickEvent event) {
//        if (event.getSource() == btnSearchGateEntry) {
//            gateEntrySearch.show();
//        } else if (event.getSource() == btnGateOutBy) {
//            if (DmShared.iNull(topControl.getDocId()).longValue() != 0) {
//                if (DmShared.xNull(userControl.getTextBlog(gateOutByBlog)).equalsIgnoreCase("")) {
//                    setGateOut(topControl.getDocId(), true);
//                } else {
//                    setGateOut(topControl.getDocId(), false);
//                }
//            }
//        } else if (event.getSource() == btnClockGateEntry) {
//            if (!topControl.getDocId().equals("")) {
//                getDefaultDateTime(true);
//            }
//        } else if (event.getSource() == btnGateInDateTime) {
//            if (topControl.getDocId().equals("") && topControl.getMode().equalsIgnoreCase("New")) {
//                getDefaultDateTime(false);
//            }
//        } else if (event.getSource() == btnGateAdvice) {
//            gateAdvicePopup.show(topControl.getMode().equalsIgnoreCase("New") ? "" : topControl.getDocId());
//        }
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
        txtToDate.addBlurHandler(this);
        txtFromDate.addBlurHandler(this);

    }

}
